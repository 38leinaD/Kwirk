package de.fruitfly.kwirk;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import de.fruitfly.kwirk.tile.RefTile;
import de.fruitfly.kwirk.tile.Tile;

public class Tri extends Entity {

	private float angle = 0.0f;
	private float oldAngle;
	private static Matrix4 modelTransform = new Matrix4().idt();
	private static Matrix4 modelViewProjection = new Matrix4();
	
	public Tri(int x, int y, float angle) {
		super(x, y);
		this.angle = this.oldAngle = angle;
		
		if (angle == 270.0f) {
			Kwirk.level.tileMap[x][y] = new RefTile(this);
			Kwirk.level.tileMap[x][y-1] = new RefTile(this);
			Kwirk.level.tileMap[x][y+1] = new RefTile(this);
			Kwirk.level.tileMap[x+1][y] = new RefTile(this);
		}
	}

	public void render() {
		renderBlock2(Kwirk.TEXREG_TRI_CENTER, this.x, this.y, 0, 0);
		renderBlock2(Kwirk.TEXREG_TRI_ARM, this.x, this.y, 1, 0);
		renderBlock2(Kwirk.TEXREG_TRI_ARM, this.x, this.y, 0, 1);
		renderBlock2(Kwirk.TEXREG_TRI_ARM, this.x, this.y, -1, 0);

	}
	
	public void tick() {
		if (Math.abs(this.angle - this.oldAngle) >= 0.5f) {
			this.angle -= 0.5f;
		}
		else {
			this.oldAngle = this.angle;
		}
	}
	
	public void rotate(boolean clockwise) {
		if (clockwise) {
			this.angle -= 90.0f;
			
		}
		else {
			this.angle += 90.0f;
		}
		this.angle = U.normalizeDegrees(this.angle);
		this.oldAngle = this.angle;
		
		// Reset
		for (int xx=-1; xx<2; xx++) {
			for (int yy=-1; yy<2; yy++) {
				Tile t = Kwirk.level.tileMap[x+xx][y+yy];
				if (t instanceof RefTile && ((RefTile) t).getParent() == this) {
					Kwirk.level.tileMap[x+xx][y+yy] = null;
				}
			}
		}
		// Rebuild
		if (angle == 0) {
			Kwirk.level.tileMap[x-1][y] = new RefTile(this);
			Kwirk.level.tileMap[x][y] = new RefTile(this);
			Kwirk.level.tileMap[x+1][y] = new RefTile(this);
			Kwirk.level.tileMap[x][y+1] = new RefTile(this);
		}
		else if (angle == 90) {
			Kwirk.level.tileMap[x][y+1] = new RefTile(this);
			Kwirk.level.tileMap[x][y] = new RefTile(this);
			Kwirk.level.tileMap[x][y-1] = new RefTile(this);
			Kwirk.level.tileMap[x-1][y] = new RefTile(this);
		}
		else if (angle == 180) {
			Kwirk.level.tileMap[x-1][y] = new RefTile(this);
			Kwirk.level.tileMap[x][y] = new RefTile(this);
			Kwirk.level.tileMap[x+1][y] = new RefTile(this);
			Kwirk.level.tileMap[x][y-1] = new RefTile(this);
		}
		else if (angle == 270) {
			Kwirk.level.tileMap[x][y+1] = new RefTile(this);
			Kwirk.level.tileMap[x][y] = new RefTile(this);
			Kwirk.level.tileMap[x][y-1] = new RefTile(this);
			Kwirk.level.tileMap[x+1][y] = new RefTile(this);
		}
	}
	
	private void renderBlock2(TextureRegion tex, int x, int y, int xoff, int yoff) {
		this.angle = 0.0f;
		modelTransform.idt();
		modelTransform.translate(x+0.5f, y+0.5f, 0.0f);
		modelTransform.rotate(new Vector3(0.0f, 0.0f, 1.0f), angle);
		modelTransform.translate(xoff-0.5f, yoff-0.5f, 0.0f);

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
