package de.fruitfly.kwirk;

import de.fruitfly.kwirk.tile.RefTile;
import de.fruitfly.kwirk.tile.Tile;

public class Entity {
	protected int x, y;

	public void tick() {
		
	}
	
	public void render() {
		
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public void removeFromLevel() {
		for (int x=0; x<Kwirk.level.tileMap.length; x++) {
			for (int y=0; y<Kwirk.level.tileMap[0].length; y++) {
				Tile t = Kwirk.level.tileMap[x][y];
				if (t != null && t instanceof RefTile && ((RefTile) t).getParent() == this) {
					Kwirk.level.tileMap[x][y] = null;
				}
			}
		}
		Kwirk.level.entities.remove(this);
	}
	
	public void addToLevel() {
		Kwirk.level.entities.add(this);
		Kwirk.level.tileMap[this.x][this.y] = new RefTile(this);
	}
}
