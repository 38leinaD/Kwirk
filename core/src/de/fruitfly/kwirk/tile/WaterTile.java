package de.fruitfly.kwirk.tile;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

import de.fruitfly.kwirk.Kwirk;
import de.fruitfly.kwirk.SurfaceRenderer;
import de.fruitfly.kwirk.Tex;
import de.fruitfly.kwirk.entity.Entity;
import de.fruitfly.kwirk.entity.Pusher;

public class WaterTile extends Tile {

	public void render(SurfaceRenderer gl, int i, int j) {
		TextureRegion tex;
		tex = Tex.TEXREG_FLOOR;
		boolean leftWaterTile = !(Kwirk.level.getTileMap()[i-1][j] instanceof WaterTile);
		if (leftWaterTile) {
			// left
			gl.normal(1.0f, 0.0f, 0.0f);
			gl.texCoord(tex.getU(), tex.getV());
			gl.vertex(i, j, -1.0f);
			
			gl.normal(1.0f, 0.0f, 0.0f);
			gl.texCoord(tex.getU2(), tex.getV());
			gl.vertex(i, j+1, -1.0f);
			
			gl.normal(1.0f, 0.0f, 0.0f);
			gl.texCoord(tex.getU(), tex.getV2());
			gl.vertex(i, j+1, 0.0f);
			
			gl.normal(1.0f, 0.0f, 0.0f);
			gl.texCoord(tex.getU(), tex.getV());
			gl.vertex(i, j, -1.0f);
			
			gl.normal(1.0f, 0.0f, 0.0f);
			gl.texCoord(tex.getU(), tex.getV2());
			gl.vertex(i, j+1, 0.0f);
			
			gl.normal(1.0f, 0.0f, 0.0f);
			gl.texCoord(tex.getU2(), tex.getV2());
			gl.vertex(i, j, 0.0f);
		}
		
		boolean rightWaterTile = !(Kwirk.level.getTileMap()[i+1][j] instanceof WaterTile);

		if (rightWaterTile) {
			gl.normal(-1.0f, 0.0f, 0.0f);
			gl.texCoord(tex.getU(), tex.getV());
			gl.vertex(i+1, j+1, -1.0f);
			
			gl.normal(-1.0f, 0.0f, 0.0f);
			gl.texCoord(tex.getU2(), tex.getV());
			gl.vertex(i+1, j, -1.0f);
			
			gl.normal(-1.0f, 0.0f, 0.0f);
			gl.texCoord(tex.getU(), tex.getV2());
			gl.vertex(i+1, j, 0.0f);
			
			gl.normal(-1.0f, 0.0f, 0.0f);
			gl.texCoord(tex.getU(), tex.getV());
			gl.vertex(i+1, j+1, -1.0f);
			
			gl.normal(-1.0f, 0.0f, 0.0f);
			gl.texCoord(tex.getU(), tex.getV2());
			gl.vertex(i+1, j, 0.0f);
			
			gl.normal(-1.0f, 0.0f, 0.0f);
			gl.texCoord(tex.getU2(), tex.getV2());
			gl.vertex(i+1, j+1, 0.0f);
		}
		
		boolean topWaterTile = !(Kwirk.level.getTileMap()[i][j+1] instanceof WaterTile);
		if (topWaterTile) {
		
			gl.normal(0.0f, -1.0f, 0.0f);
			gl.texCoord(tex.getU(), tex.getV());
			gl.vertex(i, j+1, -0.5f);
			
			gl.normal(0.0f, -1.0f, 0.0f);
			gl.texCoord(tex.getU2(), tex.getV());
			gl.vertex(i+1, j+1, -0.5f);
			
			gl.normal(0.0f, -1.0f, 0.0f);
			gl.texCoord(tex.getU(), tex.getV2());
			gl.vertex(i, j+1, 0.0f);
			
			gl.normal(0.0f, -1.0f, 0.0f);
			gl.texCoord(tex.getU(), tex.getV2());
			gl.vertex(i, j+1, 0.0f);
			
			gl.normal(0.0f, -1.0f, 0.0f);
			gl.texCoord(tex.getU2(), tex.getV());
			gl.vertex(i+1, j+1, -0.5f);
			
			gl.normal(0.0f, -1.0f, 0.0f);
			gl.texCoord(tex.getU2(), tex.getV2());
			gl.vertex(i+1, j+1, 0.0f);
		}

		tex = Tex.TEXREG_WATER;
		
		int strips = 3;
		float stripWidth = 1.0f/strips;
		for (int xx=0; xx<strips; xx++) {
			float ee = MathUtils.sin((float) (2*Math.PI * ((Kwirk.ticker/20.0f+xx)/3.0f)));
			float height = -0.4f + 0.1f*ee;
			float ee1 = MathUtils.sin((float) (2*Math.PI * ((Kwirk.ticker/20.0f+xx+1)/3.0f)));
			float height1 = -0.4f + 0.1f*ee1;
			gl.normal(0.0f, 0.0f, 1.0f);
			gl.texCoord(tex.getU(), tex.getV());
			gl.vertex(i+xx*stripWidth, j, height);
			
			gl.normal(0.0f, 0.0f, 1.0f);
			gl.texCoord(tex.getU2(), tex.getV());
			gl.vertex(i+(xx+1)*stripWidth, j, height1);
			
			gl.normal(0.0f, 0.0f, 1.0f);
			gl.texCoord(tex.getU(), tex.getV2());
			gl.vertex(i+xx*stripWidth, j+1, height);
			
			gl.normal(0.0f, 0.0f, 1.0f);
			gl.texCoord(tex.getU(), tex.getV2());
			gl.vertex(i+xx*stripWidth, j+1, height);
			
			gl.normal(0.0f, 0.0f, 1.0f);
			gl.texCoord(tex.getU2(), tex.getV());
			gl.vertex(i+(xx+1)*stripWidth, j, height1);
			
			gl.normal(0.0f, 0.0f, 1.0f);
			gl.texCoord(tex.getU2(), tex.getV2());
			gl.vertex(i+(xx+1)*stripWidth, j+1, height1);
		}		
	}
	
	public boolean blocks(Entity e) {
		return !(e instanceof Pusher);
	}
}
