package de.fruitfly.kwirk;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

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
				if (isWorldTile(selectedTile.x, selectedTile.y)) {
					Tile t = Kwirk.level.tileMap[selectedTile.x][selectedTile.y];
					if (t instanceof RefTile) {
						Entity e = ((RefTile) t).getParent();
						e.removeFromLevel();
					}
					else {
						Kwirk.level.tileMap[selectedTile.x][selectedTile.y] = null;
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
		private Point createMin, createMax;
		
		public CreateMode() {
			OBJS.add(WallTile.class);
			OBJS.add(ExitTile.class);
			OBJS.add(WaterTile.class);
			OBJS.add(Player.class);
			OBJS.add(Pusher.class);

			createStencil();
		}
		
		public void tick() {
			if (Gdx.input.isButtonPressed(0)) {
				if (isWorldTile(selectedTile.x, selectedTile.y)) {
					Tile t = Kwirk.level.tileMap[selectedTile.x][selectedTile.y];
					if (t != null) {
						//System.out.println("Tile already occupied by " + t);
					}
					else if (objectInstance instanceof Tile){
						Kwirk.level.tileMap[selectedTile.x][selectedTile.y] = (Tile) objectInstance;
						if (createMin == null) {
							createMin = new Point(selectedTile.x, selectedTile.y);
							createMax = new Point(selectedTile.x, selectedTile.y);
						}
						else {
							if (selectedTile.x < createMin.x ) {
								createMin.x = selectedTile.x;
							}
							if  (selectedTile.y < createMin.y) {
								createMin.y = selectedTile.y;
							}
							if (selectedTile.x > createMax.x ) {
								createMax.x = selectedTile.x;
							}
							if  (selectedTile.y > createMax.y) {
								createMax.y = selectedTile.y;
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
				e.setX(selectedTile.x);
				e.setY(selectedTile.y);
			}
		}
		
		@Override
		public boolean scrolled(int amount) {
			objectSelected = (objectSelected + amount) % OBJS.size();
			while (objectSelected < 0) objectSelected += OBJS.size(); // % does not work on negatives...!?
			if (OBJS.get(objectSelected) != Pusher.class) {
				createMin = null;
				createMax = null;
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
				objectInstance = new Player(selectedTile.x, selectedTile.y, Kwirk.TEXREG_KWIRK);
			}
			else if (c == Pusher.class) {
				objectInstance = new EditTile();
			}
		}

		public void render() {
			/*
			if (objectInstance != null) {
				Kwirk.TEXTURE_GAME_ART.bind();
				if (objectInstance instanceof Tile) {
					Tile t = (Tile) objectInstance;
					G.gl.begin(new Matrix4().idt(), G.cam.view, G.cam.projection, GL20.GL_TRIANGLES);
					t.render(G.gl, selectedTile.x, selectedTile.y);
					G.gl.end();
				}
				else if (objectInstance instanceof Entity) {
					Entity e = (Entity) objectInstance;
					e.render();
				}
				else {
					throw new RuntimeException("Unkown object class");
				}
			}
			*/
		}
		
		public String getName() {
			return "Create " + objectInstance;
		}

		@Override
		public boolean keyDown(int keyCode) {
			if (keyCode == Keys.ENTER) {
				if (objectInstance instanceof EditTile) {
					int[][] bitmap = new int[createMax.x - createMin.x + 1][createMax.y - createMin.y + 1];
					for (int x=createMin.x; x<=createMax.x; x++) {
						for (int y=createMin.y; y<=createMax.y; y++) {
							if (Kwirk.level.tileMap[x][y] instanceof EditTile) {
								Kwirk.level.tileMap[x][y] = null;
								bitmap[x - createMin.x][y - createMin.y] = 1;
							}
							
						}
					}
					Pusher p = new Pusher(createMin.x, createMin.y, bitmap);
					p.addToLevel(Kwirk.level);
					createMin = createMax = null;
				}
			}
			return super.keyDown(keyCode);
		}
	}
	
	private Mode mode = null;
	
	private List<Mode> modes;
	private Point selectedTile = new Point();
	
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
		
		selectedTile.setLocation(MathUtils.floor(p.x), MathUtils.floor(p.y));
		//0System.out.println(selectedTile);

		mode.tick();
	}
	
	public void render() {
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
		Kwirk.batch.end();
		
		G.sr.setProjectionMatrix(Kwirk.projectionMatrix2D);
		G.sr.setTransformMatrix(new Matrix4().idt());
		G.sr.begin();
		//G.sr.line(0, 0, 100, 100);
		G.sr.end();
		
		G.sr.setProjectionMatrix(G.cam.projection);
		G.sr.setTransformMatrix(G.cam.view);

		mode.render();
		
		if (selectedTile != null) {
			G.sr.begin();
			G.sr.setColor(Color.WHITE);
			G.sr.box(selectedTile.x, selectedTile.y, 1, 1, 1, 1);
			G.sr.end();
		}
		
	}
	
	private void switchToMode(Mode m) {
		
	}
	
	private boolean isWorldTile(int x, int y) {
		if (x < 0 || x >= Kwirk.level.tileMap.length) return false;
		if (y <0 || y >= Kwirk.level.tileMap[1].length) return false;
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
