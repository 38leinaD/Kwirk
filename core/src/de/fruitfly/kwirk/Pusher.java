package de.fruitfly.kwirk;

import de.fruitfly.kwirk.tile.RefTile;
import de.fruitfly.kwirk.tile.Tile;

public class Pusher extends Entity {
	protected int[][] bitmap;
	
	private static int texIndexer = 0;
	private int texIndex;
	
	public Pusher(int x, int y, int[][] bitmap) {
		super(x, y);
		texIndex = (texIndexer++) % Kwirk.TEXREG_BAR.length;
		this.bitmap = bitmap;
		
	}
	
	@Override
	public void addToLevel(Level level) {
		level.entities.add(this);
		
		addRefTiles(level);
	}
	
	private void addRefTiles(Level level) {
		for (int x=0; x<bitmap.length; x++) {
			for (int y=0; y<bitmap[0].length; y++) {
				if (bitmap[x][y]==0) continue;
				level.tileMap[this.x+x][this.y+y] = new RefTile(this);
			}
		}
	}
	
	private void clearRefTiles(Level level) {
		for (int x=0; x<bitmap.length; x++) {
			for (int y=0; y<bitmap[0].length; y++) {
				if (bitmap[x][y]==0) continue;
				level.tileMap[this.x+x][this.y+y] = null;
			}
		}
	}
	
	public void push(int xoff, int yoff) {
		for (int x=0; x<bitmap.length; x++) {
			for (int y=0; y<bitmap[0].length; y++) {
				if (bitmap[x][y]==0) continue;
				Tile t = Kwirk.level.tileMap[this.x+x+xoff][this.y+y+yoff];
				if (t == null) {
					// unblocked
					continue;
				}
				else if (t instanceof RefTile) {
					RefTile rt = (RefTile) t;
					if (rt.getParent() == this) {
						// false positive; blocked by itself
						continue;
					}
					else {
						// blocked by entity
						return;
					}
				}
				else {
					// blocked
					return;
				}
			}
		}
		
		clearRefTiles(Kwirk.level);
		this.x += xoff;
		this.y += yoff;
		addRefTiles(Kwirk.level);
	}
	
	@Override
	public void render() {
		for (int x=0; x<bitmap.length; x++) {
			for (int y=0; y<bitmap[0].length; y++) {
				if (bitmap[x][y]==0) continue;
				this.renderBlock(Kwirk.TEXREG_BAR[texIndex], this.x+x, this.y+y);				
			}
		}
	}
}
