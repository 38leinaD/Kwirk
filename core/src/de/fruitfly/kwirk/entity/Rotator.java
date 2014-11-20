package de.fruitfly.kwirk.entity;

import de.fruitfly.kwirk.Kwirk;
import de.fruitfly.kwirk.Level;
import de.fruitfly.kwirk.Tex;
import de.fruitfly.kwirk.tile.RefTile;
import de.fruitfly.kwirk.tile.Tile;
import de.fruitfly.kwirk.tile.WaterTile;

public class Rotator extends Entity {
	protected int[][] bitmap;
	protected int[][] tmpBitmap;

	private static int texIndexer = 0;
	private int texIndex;
	
	public Rotator(int x, int y, int[][] bitmap) {
		super(x, y);
		texIndex = (texIndexer++) % Tex.TEXREG_BAR.length;
		this.bitmap = bitmap;
		this.tmpBitmap = new int[bitmap.length][bitmap[0].length];
	}
	
	@Override
	public void addToLevel(Level level) {
		level.getEntities().add(this);
		addRefTiles(level);
	}
	
	private void addRefTiles(Level level) {
		int sideX = (bitmap.length - 1)/2;
		int sideY = (bitmap[0].length - 1)/2;
		for (int x=0; x<bitmap.length; x++) {
			for (int y=0; y<bitmap[0].length; y++) {
				if (bitmap[x][y]==0) continue;
				level.getEntityTileMap()[getX()-sideX+x][getY()-sideY+y] = new RefTile(this);
			}
		}
	}
	
	private void clearRefTiles(Level level) {
		int sideX = (bitmap.length - 1)/2;
		int sideY = (bitmap[0].length - 1)/2;
		for (int x=0; x<bitmap.length; x++) {
			for (int y=0; y<bitmap[0].length; y++) {
				if (bitmap[x][y]==0) continue;
				level.getEntityTileMap()[getX()-sideX+x][getY()-sideY+y] = null;
			}
		}
	}
	
	public void push(Player p, int xoff, int yoff) {
		if (xoff != 0) {
			if (xoff > 0 && p.getY() > getY()) {
				// rotate +90deg
			}
			else {
				// rotate -90deg
			}
		}
		else if (yoff != 0) {
			if (yoff > 0 && p.getX() > getX()) {
				// rotate +90deg
			}
			else {
				// rotate -90deg
			}
		}
		/*
		for (int x=0; x<bitmap.length; x++) {
			for (int y=0; y<bitmap[0].length; y++) {
				if (bitmap[x][y]==0) continue;
				Tile t = Kwirk.level.getTileMap()[this.x+x+xoff][this.y+y+yoff];
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
				else if (t.blocks(this)) {
					// blocked
					return;
				}
			}
		}
		
		clearRefTiles(Kwirk.level);
		this.x += xoff;
		this.y += yoff;
		checkFloating();
		addRefTiles(Kwirk.level);
		*/
	}

	public int[][] getBitmap() {
		return bitmap;
	}
	
	@Override
	public void tick() {
		
	}

	@Override
	public void render() {
		for (int x=0; x<bitmap.length; x++) {
			for (int y=0; y<bitmap[0].length; y++) {
				if (bitmap[x][y]==0) continue;
				this.renderBlock(Tex.TEXREG_BAR[texIndex], this.x+x, this.y+y, 0.0f);				
			}
		}
	}
}
