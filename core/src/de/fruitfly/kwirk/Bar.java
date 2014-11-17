package de.fruitfly.kwirk;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;

import de.fruitfly.kwirk.tile.RefTile;
import de.fruitfly.kwirk.tile.Tile;

public class Bar extends Entity {
	public int length;
	private static Matrix4 modelTransform = new Matrix4().idt();
	
	public Bar(int x, int y) {
		this.x = x;
		this.y = y;
		this.length = 2;

		Kwirk.level.tileMap[x][y] = new RefTile(this);
		Kwirk.level.tileMap[x+1][y] = new RefTile(this);
	}

	public void render() {
		renderBlock(Kwirk.TEXREG_BAR[0], this.x, this.y, 0, 0);
		renderBlock(Kwirk.TEXREG_BAR[2], this.x+1, this.y, 0, 0);
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
	
	private void renderBlock(TextureRegion tex, int x, int y, int xoff, int yoff) {
		modelTransform.idt();
		modelTransform.translate(x, y, 0.0f);
		
		G.gl.begin(modelTransform, G.cam.view, G.cam.projection, GL20.GL_TRIANGLES);
		
		// front
		G.gl.normal(0.0f, -1.0f, 0.0f);
		G.gl.texCoord(tex.getU(), tex.getV());
		G.gl.vertex(0, 0, 0.0f);
		
		G.gl.normal(0.0f, -1.0f, 0.0f);
		G.gl.texCoord(tex.getU2(), tex.getV());
		G.gl.vertex(1, 0, 0.0f);
		
		G.gl.normal(0.0f, -1.0f, 0.0f);
		G.gl.texCoord(tex.getU(), tex.getV2());
		G.gl.vertex(0, 0, 0.999f);
		
		G.gl.normal(0.0f, -1.0f, 0.0f);
		G.gl.texCoord(tex.getU(), tex.getV2());
		G.gl.vertex(0, 0, 0.999f);
		
		G.gl.normal(0.0f, -1.0f, 0.0f);
		G.gl.texCoord(tex.getU2(), tex.getV());
		G.gl.vertex(1, 0, 0.0f);
		
		G.gl.normal(0.0f, -1.0f, 0.0f);
		G.gl.texCoord(tex.getU2(), tex.getV2());
		G.gl.vertex(1, 0, 0.999f);
		
		// back
		G.gl.normal(0.0f, 1.0f, 0.0f);
		G.gl.texCoord(tex.getU(), tex.getV());
		G.gl.vertex(1, 1, 0.0f);
		
		G.gl.normal(0.0f, 1.0f, 0.0f);
		G.gl.texCoord(tex.getU2(), tex.getV());
		G.gl.vertex(0, 1, 0.0f);
		
		G.gl.normal(0.0f, 1.0f, 0.0f);
		G.gl.texCoord(tex.getU(), tex.getV2());
		G.gl.vertex(1, 1, 0.999f);
		
		G.gl.normal(0.0f, 1.0f, 0.0f);
		G.gl.texCoord(tex.getU(), tex.getV2());
		G.gl.vertex(1, 1, 0.999f);
		
		G.gl.normal(0.0f, 1.0f, 0.0f);
		G.gl.texCoord(tex.getU2(), tex.getV());
		G.gl.vertex(0, 1, 0.0f);
		
		G.gl.normal(0.0f, 1.0f, 0.0f);
		G.gl.texCoord(tex.getU2(), tex.getV2());
		G.gl.vertex(0, 1, 0.999f);
		
		// left
		G.gl.normal(-1.0f, 0.0f, 0.0f);
		G.gl.texCoord(tex.getU(), tex.getV());
		G.gl.vertex(0, 1, 0.0f);
		
		G.gl.normal(-1.0f, 0.0f, 0.0f);
		G.gl.texCoord(tex.getU2(), tex.getV());
		G.gl.vertex(0, 0, 0.0f);
		
		G.gl.normal(-1.0f, 0.0f, 0.0f);
		G.gl.texCoord(tex.getU(), tex.getV2());
		G.gl.vertex(0, 1, 0.999f);
		
		G.gl.normal(-1.0f, 0.0f, 0.0f);
		G.gl.texCoord(tex.getU(), tex.getV2());
		G.gl.vertex(0, 1, 0.999f);
		
		G.gl.normal(-1.0f, 0.0f, 0.0f);
		G.gl.texCoord(tex.getU2(), tex.getV());
		G.gl.vertex(0, 0, 0.0f);
		
		G.gl.normal(-1.0f, 0.0f, 0.0f);
		G.gl.texCoord(tex.getU2(), tex.getV2());
		G.gl.vertex(0, 0, 0.999f);
		
		// right
		G.gl.normal(1.0f, 0.0f, 0.0f);
		G.gl.texCoord(tex.getU(), tex.getV());
		G.gl.vertex(1, 0, 0.0f);
		
		G.gl.normal(1.0f, 0.0f, 0.0f);
		G.gl.texCoord(tex.getU2(), tex.getV());
		G.gl.vertex(1, 1, 0.0f);
		
		G.gl.normal(1.0f, 0.0f, 0.0f);
		G.gl.texCoord(tex.getU(), tex.getV2());
		G.gl.vertex(1, 0, 0.999f);
		
		G.gl.normal(1.0f, 0.0f, 0.0f);
		G.gl.texCoord(tex.getU(), tex.getV2());
		G.gl.vertex(1, 0, 0.999f);
		
		G.gl.normal(1.0f, 0.0f, 0.0f);
		G.gl.texCoord(tex.getU2(), tex.getV());
		G.gl.vertex(1, 1, 0.0f);
		
		G.gl.normal(1.0f, 0.0f, 0.0f);
		G.gl.texCoord(tex.getU2(), tex.getV2());
		G.gl.vertex(1, 1, 0.999f);
		
		// top
		G.gl.normal(0.0f, 0.0f, 1.0f);
		G.gl.texCoord(tex.getU(), tex.getV());
		G.gl.vertex(0, 0, 0.999f);
		
		G.gl.normal(0.0f, 0.0f, 1.0f);
		G.gl.texCoord(tex.getU2(), tex.getV());
		G.gl.vertex(1, 0, 0.999f);
		
		G.gl.normal(0.0f, 0.0f, 1.0f);
		G.gl.texCoord(tex.getU(), tex.getV2());
		G.gl.vertex(0, 1, 0.999f);
		
		G.gl.normal(0.0f, 0.0f, 1.0f);
		G.gl.texCoord(tex.getU(), tex.getV2());
		G.gl.vertex(0, 1, 0.999f);
		
		G.gl.normal(0.0f, 0.0f, 1.0f);
		G.gl.texCoord(tex.getU2(), tex.getV());
		G.gl.vertex(1, 0, 0.999f);
		
		G.gl.normal(0.0f, 0.0f, 1.0f);
		G.gl.texCoord(tex.getU2(), tex.getV2());
		G.gl.vertex(1, 1, 0.999f);
		
		G.gl.end();
	}
}
