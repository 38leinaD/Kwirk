package de.fruitfly.kwirk;

import java.awt.Point;
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

import de.fruitfly.kwirk.tile.ExitTile;
import de.fruitfly.kwirk.tile.RefTile;
import de.fruitfly.kwirk.tile.Tile;
import de.fruitfly.kwirk.tile.WallTile;

public class Edit implements InputProcessor {
	
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
		
		public CreateMode() {
			OBJS.add(WallTile.class);
			OBJS.add(ExitTile.class);
			
			createStencil();
		}
		
		public void tick() {
			if (Gdx.input.isButtonPressed(0)) {
				if (isWorldTile(selectedTile.x, selectedTile.y)) {
					Tile t = Kwirk.level.tileMap[selectedTile.x][selectedTile.y];
					if (t != null) {
						//System.out.println("Tile already occupied by " + t);
					}
					else {
						Kwirk.level.tileMap[selectedTile.x][selectedTile.y] = new WallTile();
					}
				}
			}
		}
		
		@Override
		public boolean scrolled(int amount) {
			objectSelected = (objectSelected + amount) % OBJS.size();
			while (objectSelected < 0) objectSelected += OBJS.size(); // % does not work on negatives...!?
			createStencil();
			return true;
		}

		private void createStencil() {
			try {
				objectInstance = OBJS.get(objectSelected).newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}			
		}

		public void render() {
			if (objectInstance != null) {
				if (objectInstance instanceof Tile) {
					Tile t = (Tile) objectInstance;
					Kwirk.TEXTURE_GAME_ART.bind();
					G.gl.begin(new Matrix4().idt(), G.cam.view, G.cam.projection, GL20.GL_TRIANGLES);
					t.render(G.gl, selectedTile.x, selectedTile.y);
					G.gl.end();
				}
				else if (objectInstance instanceof Entity) {
					Entity e = (Entity) objectInstance;
				}
				else {
					throw new RuntimeException("Unkown object class");
				}
			}
		}
		
		public String getName() {
			return "Create " + objectInstance;
		}
	}
	
	private Mode mode = null;
	
	private List<Mode> modes;
	private Point selectedTile = new Point();
	
	public Edit() {
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
		G.debugFont.setScale(1.0f);
		for (int i=0; i<modes.size(); i++) {
			Mode m = modes.get(i);
			if (m == mode) {
				G.debugFont.setColor(Color.RED);
			}
			else {
				G.debugFont.setColor(Color.WHITE);
			}
			G.debugFont.draw(Kwirk.batch, i +") " + m.getName(), 10, 10+i*17);
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
