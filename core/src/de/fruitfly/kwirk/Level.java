package de.fruitfly.kwirk;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import de.fruitfly.kwirk.entity.Entity;
import de.fruitfly.kwirk.entity.Player;
import de.fruitfly.kwirk.entity.Pusher;
import de.fruitfly.kwirk.entity.Rotator;
import de.fruitfly.kwirk.tile.ExitTile;
import de.fruitfly.kwirk.tile.RefTile;
import de.fruitfly.kwirk.tile.Tile;
import de.fruitfly.kwirk.tile.WallTile;
import de.fruitfly.kwirk.tile.WaterTile;

public class Level {
	private Tile[][] tileMap;
	private RefTile[][] entityTileMap;
	private List<Entity> entities = new LinkedList<Entity>();
	private List<Entity> deletedEntities = new LinkedList<Entity>();
	public String name = "testlevel";
	
	public Level(int w, int h) {
		tileMap = new Tile[w][h];
		entityTileMap = new RefTile[w][h];
	}
	
	public void removeEntity(Entity e) {
		deletedEntities.add(e);
	}
	
	public void tick() {
		for (Entity e : deletedEntities) {
			entities.remove(e);
		}
		
		for (Entity e : entities) {
			e.tick();
		}
	}
	
	public void render() {
		/*
		 * cam.position.set(1.0f,-3.0f, 9.0f); cam.up.set(0.0f, 0.0f, 1.0f);
		 * cam.lookAt(5.0f, 3.5f, 2.0f);
		 */
		G.cam.position.set(getWidth()/1.6f, -2.0f, 8.0f);
		G.cam.up.set(0.0f, 0.0f, 1.0f);
		G.cam.lookAt(getWidth()/2.0f, getHeight()/2.0f, 0.0f);
		((OrthographicCamera) G.cam).zoom = 1.0f;
		// ((OrthographicCamera)G.cam).zoom = 0.02f;

		G.cam.update();

		G.sr.setProjectionMatrix(G.cam.projection);
		G.sr.setTransformMatrix(G.cam.view);
		
		Tex.TEXTURE_GAME_ART.bind();

		G.gl.begin(new Matrix4().idt(), G.cam.view, G.cam.projection, GL20.GL_TRIANGLES);
		for (int i = 0; i < this.getTileMap().length; i++) {
			for (int j = 0; j < this.getTileMap()[0].length; j++) {
				Tile t = this.getTileMap()[i][j];

				if (t == null || (t instanceof RefTile)) {
					// floor
					TextureRegion tex;
					tex = Tex.TEXREG_FLOOR;

					G.gl.normal(0.0f, 0.0f, 1.0f);
					G.gl.texCoord(tex.getU(), tex.getV());
					G.gl.vertex(i, j, 0.0f);

					G.gl.normal(0.0f, 0.0f, 1.0f);
					G.gl.texCoord(tex.getU2(), tex.getV());
					G.gl.vertex(i + 1, j, 0.0f);

					G.gl.normal(0.0f, 0.0f, 1.0f);
					G.gl.texCoord(tex.getU(), tex.getV2());
					G.gl.vertex(i, j + 1, 0.0f);

					G.gl.normal(0.0f, 0.0f, 1.0f);
					G.gl.texCoord(tex.getU(), tex.getV2());
					G.gl.vertex(i, j + 1, 0.0f);

					G.gl.normal(0.0f, 0.0f, 1.0f);
					G.gl.texCoord(tex.getU2(), tex.getV());
					G.gl.vertex(i + 1, j, 0.0f);

					G.gl.normal(0.0f, 0.0f, 1.0f);
					G.gl.texCoord(tex.getU2(), tex.getV2());
					G.gl.vertex(i + 1, j + 1, 0.0f);
				}

				if (t == null)
					continue;
				t.render(G.gl, i, j);
			}
		}
		G.gl.end();
		
		for (Entity e : entities) {
			e.render();
		}
		
		/*
		G.sr.setColor(Color.RED);
		G.sr.begin();
		for (int x = 0; x < this.entityTileMap.length; x++) {
			for (int y = 0; y < this.entityTileMap[0].length; y++) {
				Tile t = this.entityTileMap[x][y];
				if (t != null) {
					G.sr.box(x, y, 1.0f, 1.0f, 1.0f, 1.0f);
				}
			}
		}
		G.sr.end();
		*/
	}
	
	public void save() {
		FileHandle f = Gdx.files.getFileHandle("C:/Users/daniel.platz/Dropbox/Dev/Java/Games/Kwirk/Project/android/assets/levels/" + "test.lvl", FileType.Absolute);
		
		f.writeString("# name: " + this.name + "\n", false);
		f.writeString("size: " + tileMap.length + "," + tileMap[0].length + "\n", true);
		for (int x=0; x<tileMap.length; x++) {
			for (int y=0; y<tileMap[0].length; y++) {
				Tile t = tileMap[x][y];
				
				if (t != null && !(t instanceof RefTile)) {
					String type = null;
					
					if (t instanceof WallTile) {
						type = "WallTile";
					}
					else if (t instanceof ExitTile) {
						type = "ExitTile";
					}
					else if (t instanceof WaterTile) {
						type = "WaterTile";
					}
					
					if (type != null) f.writeString("t: " + x + "," + y + " " + type + "\n", true);
				}
			}
		}
		
		int playerIndex = 0;
		for (Entity e : entities) {

			if (e instanceof Pusher) {
				Pusher p = (Pusher) e;
				StringBuffer sb = new StringBuffer();
				
				for (int xx=0; xx<p.getBitmap().length; xx++) {
					for (int yy=0; yy<p.getBitmap()[0].length; yy++) {
						sb.append(p.getBitmap()[xx][yy]);
						if (yy != p.getBitmap()[0].length-1) {
							sb.append(",");
						}
					}
					if (xx != p.getBitmap().length-1) {
						sb.append(";");
					}
				}
				
				f.writeString("e: " + p.getX() + "," + p.getY() + " Pusher[" + sb.toString() + "]\n", true);
			}
			else if (e instanceof Rotator) {
				Rotator r = (Rotator) e;
				StringBuffer sb = new StringBuffer();
				for (int xx=0; xx<r.getBitmap().length; xx++) {
					for (int yy=0; yy<r.getBitmap()[0].length; yy++) {
						sb.append(r.getBitmap()[xx][yy]);
						if (yy != r.getBitmap()[0].length-1) {
							sb.append(",");
						}
					}
					if (xx != r.getBitmap().length-1) {
						sb.append(";");
					}
				}
				
				f.writeString("e: " + r.getX() + "," + r.getY() + " Rotator[" + sb.toString() + "]\n", true);
			}
			else if (e instanceof Player) {
				if (playerIndex == 0) {
					f.writeString("e: " + e.getX() + "," + e.getY() + " Player\n", true);
				}
				else {
					f.writeString("e: " + e.getX() + "," + e.getY() + " Player" + playerIndex + "\n", true);
				}
				playerIndex++;
			}
		}
		
		System.out.println("Saved to " + f.path());
	}
	
	public static Level load(FileHandle f) {
		String str = f.readString();
		Level l = null;
		boolean firstPlayer = true;
		for (String line : str.split("\n")) {
			if (line.startsWith("size:")) {
				String[] st = line.split(" ")[1].split(",");
				int x = Integer.parseInt(st[0]);
				int y = Integer.parseInt(st[1]);
				
				l = new Level(x, y);
			}
			else if (line.startsWith("#")) {
				// ignore comment
			}
			else if (line.startsWith("t:")) {
				String[] st = line.split(" ")[1].split(",");
				int x = Integer.parseInt(st[0]);
				int y = Integer.parseInt(st[1]);
				String type = line.split(" ")[2];
				Tile t = null;
				if (type.equals("WallTile")) {
					t = new WallTile();
				}
				else if (type.equals("ExitTile")) {
					t = new ExitTile();
				}
				else if (type.equals("WaterTile")) {
					t = new WaterTile();
				}
				l.tileMap[x][y] = t;
			}
			else if (line.startsWith("e:")) {
				String[] st = line.split(" ")[1].split(",");
				int x = Integer.parseInt(st[0]);
				int y = Integer.parseInt(st[1]);
				String type = line.split(" ")[2];
				
				if (type.startsWith("Player")) {
					int playerIndex = 0;
					if (!type.equals("Player")) {
						playerIndex = Integer.parseInt(type.substring("Player".length()));
					}
					Player p = new Player(x, y, firstPlayer ? Tex.TEXREG_KWIRK : Tex.TEXREG_KWURK);
					p.addToLevel(l);
					Kwirk.controlableEntites[playerIndex] = p;
					Kwirk.controlableEntitesNum++;
				}
				else if (type.startsWith("Pusher")) {
					String bm = type.substring(7, type.length()-1);
					String[] lines = bm.split(";");
					int[][] bitmap = new int[lines.length][];
					for (int i=0; i<bitmap.length; i++) {
						String[] linetokens = lines[i].split(",");
						bitmap[i] = new int[linetokens.length];
						for (int j=0; j<linetokens.length; j++) {
							bitmap[i][j] = Integer.parseInt(linetokens[j]);
						}
					}
					Pusher p = new Pusher(x, y, bitmap);
					p.addToLevel(l);
				}
				else if (type.startsWith("Rotator")) {
					String bm = type.substring(8, type.length()-1);
					String[] lines = bm.split(";");
					int[][] bitmap = new int[lines.length][];
					for (int i=0; i<bitmap.length; i++) {
						String[] linetokens = lines[i].split(",");
						bitmap[i] = new int[linetokens.length];
						for (int j=0; j<linetokens.length; j++) {
							bitmap[i][j] = Integer.parseInt(linetokens[j]);
						}
					}
					Rotator r = new Rotator(x, y, bitmap);
					r.addToLevel(l);
				}
				
			}
			else {
				System.out.println("Unknown line: " + line);
			}
		}
		return l;
	}

	public Tile[][] getTileMap() {
		return tileMap;
	}

	public RefTile[][] getEntityTileMap() {
		return entityTileMap;
	}
	
	public List<Entity> getEntities() {
		return entities;
	}
	
	public int getWidth() {
		return tileMap.length;
	}
	
	public int getHeight() {
		return tileMap[0].length;
	}
}
