package de.fruitfly.kwirk.entity;

import de.fruitfly.kwirk.G;
import de.fruitfly.kwirk.Kwirk;
import de.fruitfly.kwirk.Level;
import de.fruitfly.kwirk.Tex;
import de.fruitfly.kwirk.tile.RefTile;
import de.fruitfly.kwirk.tile.Tile;
import de.fruitfly.kwirk.tile.WaterTile;

public class Pusher extends Entity {
	protected int[][] bitmap;
	
	private boolean floating = false;
	private float z = 0;
	
	public Pusher(int x, int y, int[][] bitmap) {
		super(x, y);
		texIndex = (texIndexer++) % Tex.TEXREG_BAR.length;
		this.bitmap = bitmap;
		
	}
	
	@Override
	public void addToLevel(Level level) {
		level.getEntities().add(this);
		addRefTiles(level);
	}
	
	private void addRefTiles(Level level) {
		for (int x=0; x<bitmap.length; x++) {
			for (int y=0; y<bitmap[0].length; y++) {
				if (bitmap[x][y]==0) continue;
				level.getEntityTileMap()[this.x+x][this.y+y] = new RefTile(this);
			}
		}
	}
	
	private void clearRefTiles(Level level) {
		for (int x=0; x<bitmap.length; x++) {
			for (int y=0; y<bitmap[0].length; y++) {
				if (bitmap[x][y]==0) continue;
				level.getEntityTileMap()[this.x+x][this.y+y] = null;
			}
		}
	}
	
	private void clearWaterTiles(Level level) {
		for (int x=0; x<bitmap.length; x++) {
			for (int y=0; y<bitmap[0].length; y++) {
				if (bitmap[x][y]==0) continue;
				level.getTileMap()[this.x+x][this.y+y] = null;
			}
		}
	}
	
	public boolean push(int xoff, int yoff) {
		for (int x=0; x<bitmap.length; x++) {
			for (int y=0; y<bitmap[0].length; y++) {
				if (bitmap[x][y]==0) continue;
				Tile t = Kwirk.level.getTileMap()[this.x+x+xoff][this.y+y+yoff];
				if (t != null && t.blocks(this)) {
					// blocking tile
					return false;
				}
				else {
					RefTile rt = Kwirk.level.getEntityTileMap()[this.x+x+xoff][this.y+y+yoff];
					if (rt != null && rt.getParent() != this) {
						return false;
					}
				}
			}
		}
		
		G.soundPusher.play();
		
		clearRefTiles(Kwirk.level);
		this.x += xoff;
		this.y += yoff;
		checkFloating();
		addRefTiles(Kwirk.level);
		return true;
	}
	
	private void checkFloating() {
		floating = true;
		for (int x=0; x<bitmap.length; x++) {
			for (int y=0; y<bitmap[0].length; y++) {
				if (bitmap[x][y]==0) continue;
				Tile t = Kwirk.level.getTileMap()[this.x+x][this.y+y];
				if (!(t instanceof WaterTile)) {
					floating = false;
					return;
				}
			}
		}
	}

	public int[][] getBitmap() {
		return bitmap;
	}
	
	@Override
	public void tick() {
		if (floating) {
			z-=0.03f;
			if (z<=-1.0f) {
				z=-1.0f;
				floating = false;
				clearWaterTiles(Kwirk.level);
				removeFromLevel();
			}
		}
	}

	@Override
	public void render() {
		for (int x=0; x<bitmap.length; x++) {
			for (int y=0; y<bitmap[0].length; y++) {
				if (bitmap[x][y]==0) continue;
				this.renderBlock(Tex.TEXREG_BAR[texIndex], this.x+x, this.y+y, z);				
			}
		}
	}
}
