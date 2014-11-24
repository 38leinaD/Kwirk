package de.fruitfly.kwirk;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

import de.fruitfly.kwirk.entity.Entity;
import de.fruitfly.kwirk.entity.Player;
import de.fruitfly.kwirk.entity.Pusher;
import de.fruitfly.kwirk.entity.Rotator;
import de.fruitfly.kwirk.tile.EditTile;
import de.fruitfly.kwirk.tile.ExitTile;
import de.fruitfly.kwirk.tile.RefTile;
import de.fruitfly.kwirk.tile.Tile;
import de.fruitfly.kwirk.tile.WallTile;
import de.fruitfly.kwirk.tile.WaterTile;

public class Ed implements InputProcessor {
	
	private abstract class Mode extends InputAdapter {
		public void leave() {
			
		}
		
		public void tick() {
			
		}
		
		public void render() {
			
		}	
		
		public String getName() {
			return null;
		}
	}
	
	private class EraseMode extends Mode {
		public void tick() {
			if (Gdx.input.isButtonPressed(0)) {
				if (isWorldTile(selectedTileX, selectedTileY)) {
					Tile t = Kwirk.level.getTileMap()[selectedTileX][selectedTileY];
					if (t != null) {
						Kwirk.level.getTileMap()[selectedTileX][selectedTileY] = null;
					}
					RefTile rt = Kwirk.level.getEntityTileMap()[selectedTileX][selectedTileY];
					if (rt != null) {
						Entity e = rt.getParent();
						e.removeFromLevel();
					}
					else {
						
					}
				}
			}
		}
		
		public String getName() {
			return "Erase";
		}
	}
	
	private class CreateMode extends Mode {
		private List<Class> OBJS = new LinkedList<Class>();
		private int objectSelected = 0;
		private Object objectInstance = null;
		private int createMinX, createMinY, createMaxX, createMaxY;
		
		public CreateMode() {
			OBJS.add(WallTile.class);
			OBJS.add(ExitTile.class);
			OBJS.add(WaterTile.class);
			OBJS.add(Player.class);
			OBJS.add(Pusher.class);
			OBJS.add(Rotator.class);

			createStencil();
		}
		
		public void tick() {
			if (Gdx.input.isButtonPressed(0)) {
				if (isWorldTile(selectedTileX, selectedTileY)) {
					Tile t = Kwirk.level.getTileMap()[selectedTileX][selectedTileY];
					Tile rt = Kwirk.level.getEntityTileMap()[selectedTileX][selectedTileY];
					if (t != null || rt != null) {
						//System.out.println("Tile already occupied by " + t);
					}
					else if (objectInstance instanceof Tile){
						if (objectInstance instanceof EditTile) {
							Kwirk.level.getEntityTileMap()[selectedTileX][selectedTileY] = (EditTile)objectInstance;
						}
						else {
							Kwirk.level.getTileMap()[selectedTileX][selectedTileY] = (Tile) objectInstance;
						}
						if (createMinX == Integer.MAX_VALUE) {
							createMinX = selectedTileX;
							createMinY = selectedTileY;
							createMaxX = selectedTileX;
							createMaxY = selectedTileY;
						}
						else {
							if (selectedTileX < createMinX ) {
								createMinX = selectedTileX;
							}
							if  (selectedTileY < createMinY) {
								createMinY = selectedTileY;
							}
							if (selectedTileX > createMaxX ) {
								createMaxX = selectedTileX;
							}
							if  (selectedTileY > createMaxY) {
								createMaxY = selectedTileY;
							}
						}
					}
					else if (objectInstance instanceof Entity) {
						Entity e = (Entity) objectInstance;
						e.addToLevel(Kwirk.level);
					}
					createStencil();
				}
			}
			
			if (objectInstance instanceof Entity) {
				Entity e = (Entity) objectInstance;
				e.setX(selectedTileX);
				e.setY(selectedTileY);
			}
		}
		
		@Override
		public boolean scrolled(int amount) {
			objectSelected = (objectSelected + amount) % OBJS.size();
			while (objectSelected < 0) objectSelected += OBJS.size(); // % does not work on negatives...!?
			if (OBJS.get(objectSelected) != Pusher.class) {
				createMinX = Integer.MAX_VALUE;
				createMinY = Integer.MAX_VALUE;
				createMaxX = Integer.MIN_VALUE;
				createMaxY = Integer.MIN_VALUE;
			}
			createStencil();
			return true;
		}

		private void createStencil() {
			Class c = OBJS.get(objectSelected);
			if (c == WallTile.class) {
				objectInstance = new WallTile();
			}
			else if (c == ExitTile.class) {
				objectInstance = new ExitTile();
			}
			else if (c == WaterTile.class) {
				objectInstance = new WaterTile();
			}
			else if (c == Player.class) {
				objectInstance = new Player(selectedTileX, selectedTileY, Tex.TEXREG_KWIRK);
			}
			else if (c == Pusher.class) {
				objectInstance = new EditTile(null);
			}
			else if (c == Rotator.class) {
				objectInstance = new EditTile(null);
			}
		}

		public String getName() {
			return "Create " +  OBJS.get(objectSelected).getSimpleName();
		}

		@Override
		public boolean keyDown(int keyCode) {
			if (keyCode == Keys.ENTER) {
				Class c = OBJS.get(objectSelected);
				if (c == Pusher.class) {
					if (objectInstance instanceof EditTile && createMinX != Integer.MAX_VALUE) {
						int[][] bitmap = new int[createMaxX - createMinX + 1][createMaxY - createMinY + 1];
						for (int x=createMinX; x<=createMaxX; x++) {
							for (int y=createMinY; y<=createMaxY; y++) {
								if (Kwirk.level.getEntityTileMap()[x][y] instanceof EditTile) {
									Kwirk.level.getEntityTileMap()[x][y] = null;
									bitmap[x - createMinX][y - createMinY] = 1;
								}
								
							}
						}
						Pusher p = new Pusher(createMinX, createMinY, bitmap);
						p.addToLevel(Kwirk.level);
						createMinX = Integer.MAX_VALUE;
						createMinY = Integer.MAX_VALUE;
						createMaxX = Integer.MIN_VALUE;
						createMaxY = Integer.MIN_VALUE;
					}
				}
				else if (c == Rotator.class) {
					int centerX = selectedTileX;
					int centerY = selectedTileY;
					
					int rightX = createMaxX - centerX;
					int leftX = centerX - createMinX;
					int sideX = Math.max(rightX, leftX);
					
					int topY = createMaxY - centerY;
					int bottomY = centerY - createMinY;
					int sideY = Math.max(topY, bottomY);
					int side = Math.max(sideX, sideY);

					int[][] bitmap = new int[2*side+1][2*side+1];

					for (int x=centerX-side; x<=centerX+side; x++) {
						for (int y=centerY-side; y<=centerY+side; y++) {
							if (Kwirk.level.getEntityTileMap()[x][y] instanceof EditTile) {
								Kwirk.level.getEntityTileMap()[x][y] = null;
								bitmap[x-(centerX-side)][y-(centerY-side)] = 1;
							}
						}
					}
					
					Rotator r = new Rotator(centerX, centerY, bitmap);
					r.addToLevel(Kwirk.level);
					
					createMinX = Integer.MAX_VALUE;
					createMinY = Integer.MAX_VALUE;
					createMaxX = Integer.MIN_VALUE;
					createMaxY = Integer.MIN_VALUE;
				}
			}
			return super.keyDown(keyCode);
		}
	}
	
	private Mode mode = null;
	
	private List<Mode> modes;
	private int selectedTileX, selectedTileY;
	
	public Ed() {
		modes = new LinkedList<Mode>();
		modes.add(new EraseMode());
		modes.add(new CreateMode());

		mode = modes.get(1);
		
		Gdx.input.setInputProcessor(this);
	}
	
	public void tick() {
		for (int i=0; i<=9; i++) {
			if (Gdx.input.isKeyJustPressed(Keys.NUM_0 + i)) {
				if (i<modes.size()) {
					mode = modes.get(i);
				}
			}
		}
		
		if (Gdx.input.isKeyJustPressed(Keys.S)) {
			System.out.println("Save level.");
			Kwirk.level.save();
		}
		
		float x = Gdx.input.getX();
		float y = Gdx.input.getY();
		
		Vector3 p = new Vector3();
		Ray r = G.cam.getPickRay(x, y);
		float alpha = -r.origin.z/r.direction.z;

		r.getEndPoint(p, alpha);
//		System.out.println(p.x + ", " + p.y + ", " + p.z);
		
		selectedTileX = MathUtils.floor(p.x);
		selectedTileY = MathUtils.floor(p.y);
		//0System.out.println(selectedTile);

		mode.tick();
	}
	
	public void render() {
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);

		G.gl.begin(new Matrix4().idt(), G.cam.view, G.cam.projection, GL20.GL_TRIANGLES);
		for (int i = 0; i < Kwirk.level.getEntityTileMap().length; i++) {
			for (int j = 0; j < Kwirk.level.getEntityTileMap()[0].length; j++) {
				RefTile t = Kwirk.level.getEntityTileMap()[i][j];

				if (t != null && t instanceof EditTile) {
					t.render(G.gl, i, j);
				}
			}
		}
		G.gl.end();
		Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);

		Kwirk.batch.begin();
		Kwirk.font.setScale(0.5f);
		for (int i=0; i<modes.size(); i++) {
			Mode m = modes.get(i);
			if (m == mode) {
				Kwirk.font.setColor(Color.RED);
			}
			else {
				Kwirk.font.setColor(Color.WHITE);
			}
			Kwirk.font.draw(Kwirk.batch, i +") " + m.getName(), 10, 10+i*15);
		}
		Kwirk.font.setColor(Color.WHITE);
		Kwirk.font.draw(Kwirk.batch, "(" + selectedTileX + "," + selectedTileY + ")", 10, 10+2*15);
		Kwirk.batch.end();
		
		G.sr.setProjectionMatrix(G.cam.projection);
		G.sr.setTransformMatrix(G.cam.view);

		mode.render();
		
			G.sr.setAutoShapeType(true);
			G.sr.begin();
			G.sr.setColor(Color.WHITE);
			G.sr.box(selectedTileX, selectedTileY, 1, 1, 1, 1);
			G.sr.end();
		
	}
	
	private void switchToMode(Mode m) {
		
	}
	
	private boolean isWorldTile(int x, int y) {
		if (x < 0 || x >= Kwirk.level.getTileMap().length) return false;
		if (y <0 || y >= Kwirk.level.getTileMap()[1].length) return false;
		return true;
	}

	@Override
	public boolean keyDown(int keycode) {
		return mode.keyDown(keycode);
	}

	@Override
	public boolean keyUp(int keycode) {
		return mode.keyUp(keycode);
	}

	@Override
	public boolean keyTyped(char character) {
		return mode.keyTyped(character);
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return mode.touchDown(screenX, screenY, pointer, button);
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return mode.touchUp(screenX, screenY, pointer, button);
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return mode.touchDragged(screenX, screenY, pointer);
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return mode.mouseMoved(screenX, screenY);
	}

	@Override
	public boolean scrolled(int amount) {
		return mode.scrolled(amount);
	}
}
