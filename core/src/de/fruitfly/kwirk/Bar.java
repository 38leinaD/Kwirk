package de.fruitfly.kwirk;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;

import de.fruitfly.kwirk.tile.RefTile;
import de.fruitfly.kwirk.tile.Tile;

public class Bar extends Entity {
	public int length;
	
	private static int texIndexer = 0;
	private int texIndex;
	
	public Bar(int x, int y) {
		super(x, y);
		texIndex = (texIndexer++) % Tex.TEXREG_BAR.length;
		this.length = 2;

		Kwirk.level.tileMap[x][y] = new RefTile(this);
		Kwirk.level.tileMap[x+1][y] = new RefTile(this);
	}

	public void render() {
		renderBlock(Tex.TEXREG_BAR[texIndex], this.x, this.y, 0.0f);
		renderBlock(Tex.TEXREG_BAR[texIndex], this.x+1, this.y, 0.0f);
	}
	
	public void tick() {
		
	}
	
	private boolean isBlocked(int x, int y) {
		Tile t =Kwirk.level.tileMap[x][y];
		if (t == null) return false;
		if (t instanceof RefTile) {
			return ((RefTile) t).getParent() != this;
		}
		return true;
	}
	
	public void push(int xoff, int yoff) {
		for (int xx=0; xx<this.length; xx++) {
			if (isBlocked(x+xoff+xx, y+yoff)) return;
		}
		
		for (int xx=0; xx<this.length; xx++) {
			Kwirk.level.tileMap[x+xx][y] = null;
		}
		this.x += xoff;
		this.y += yoff;
		for (int xx=0; xx<this.length; xx++) {
			Kwirk.level.tileMap[x+xx][y] = new RefTile(this);
		}
	}
	
	
}
