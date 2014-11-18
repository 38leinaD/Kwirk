package de.fruitfly.kwirk;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import de.fruitfly.kwirk.tile.ExitTile;
import de.fruitfly.kwirk.tile.RefTile;
import de.fruitfly.kwirk.tile.Tile;
import de.fruitfly.kwirk.tile.WallTile;

public class Level {
	public Tile[][] tileMap;
	public List<Entity> entities = new LinkedList<Entity>();
	public String name = "testlevel";
	
	public Level(int w, int h) {
		tileMap = new Tile[w][h];
	}
	
	public void tick() {
		for (Entity e : entities) {
			e.tick();
		}
	}
	
	public void render() {
		for (Entity e : entities) {
			e.render();
		}
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
					
					if (type != null) f.writeString("t: " + x + "," + y + " " + type + "\n", true);
				}
			}
		}
		
		for (Entity e : entities) {

			if (e instanceof Pusher) {
				Pusher p = (Pusher) e;
				StringBuffer sb = new StringBuffer();
				
				for (int xx=0; xx<p.bitmap.length; xx++) {
					for (int yy=0; yy<p.bitmap[0].length; yy++) {
						sb.append(p.bitmap[xx][yy]);
						if (yy != p.bitmap[0].length-1) {
							sb.append(",");
						}
					}
					if (xx != p.bitmap.length-1) {
						sb.append(";");
					}
				}
				
				f.writeString("e: " + p.getX() + "," + p.getY() + " Pusher[" + sb.toString() + "]\n", true);
			}
			else if (e instanceof Player) {
				f.writeString("e: " + e.getX() + "," + e.getY() + " Player\n", true);
			}
		}
		
		System.out.println("Saved to " + f.path());
	}
	
	public static Level load(FileHandle f) {
		String str = f.readString();
		Level l = null;
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
				l.tileMap[x][y] = t;
			}
			else if (line.startsWith("e:")) {
				String[] st = line.split(" ")[1].split(",");
				int x = Integer.parseInt(st[0]);
				int y = Integer.parseInt(st[1]);
				String type = line.split(" ")[2];
				
				if (type.startsWith("Player")) {
					Player p = new Player(x, y);
					p.addToLevel(l);
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
				
			}
			else {
				System.out.println("Unknown line: " + line);
			}
		}
		return l;
	}
}
