package de.fruitfly.kwirk;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.files.FileHandle;

import de.fruitfly.kwirk.tile.RefTile;
import de.fruitfly.kwirk.tile.Tile;

public class Level {
	public Tile[][] tileMap;
	public List<Entity> entities = new LinkedList<Entity>();
	public String name = "testlevel";
	
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
	
		for (int x=0; x<tileMap.length; x++) {
			for (int y=0; y<tileMap[0].length; y++) {
				Tile t = tileMap[x][y];
				
				if (t != null && !(t instanceof RefTile)) {
					f.writeString(x + "," + y + ": " + t.getClass().getCanonicalName() + "\n", true);
				}
			}
		}
		System.out.println("Saved to " + f.path());
	}
}
