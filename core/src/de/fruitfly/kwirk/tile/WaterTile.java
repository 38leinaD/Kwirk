package de.fruitfly.kwirk.tile;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

import de.fruitfly.kwirk.Entity;
import de.fruitfly.kwirk.Kwirk;
import de.fruitfly.kwirk.Pusher;
import de.fruitfly.kwirk.SurfaceRenderer;

public class WaterTile extends Tile {

	public void render(SurfaceRenderer gl, int i, int j) {
		TextureRegion tex;
		tex = Kwirk.TEXREG_FLOOR;
		
		boolean topWaterTile = !(Kwirk.level.tileMap[i][j+1] instanceof WaterTile);
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
			
			
			tex = Kwirk.TEXREG_WATER;
			
			int strips = 10;
			float stripWidth = 1.0f/strips;
			for (int xx=0; xx<strips; xx++) {
				float ee = MathUtils.sin((float) (2*Math.PI/strips) * (Kwirk.ticker/10+xx)%10);
				float height = -0.4f + 0.1f*ee;
				gl.normal(0.0f, 0.0f, 1.0f);
				gl.texCoord(tex.getU(), tex.getV());
				gl.vertex(i+xx*stripWidth, j, height);
				
				gl.normal(0.0f, 0.0f, 1.0f);
				gl.texCoord(tex.getU2(), tex.getV());
				gl.vertex(i+(xx+1)*stripWidth, j, height);
				
				gl.normal(0.0f, 0.0f, 1.0f);
				gl.texCoord(tex.getU(), tex.getV2());
				gl.vertex(i+xx*stripWidth, j+1, height);
				
				gl.normal(0.0f, 0.0f, 1.0f);
				gl.texCoord(tex.getU(), tex.getV2());
				gl.vertex(i+xx*stripWidth, j+1, height);
				
				gl.normal(0.0f, 0.0f, 1.0f);
				gl.texCoord(tex.getU2(), tex.getV());
				gl.vertex(i+(xx+1)*stripWidth, j, height);
				
				gl.normal(0.0f, 0.0f, 1.0f);
				gl.texCoord(tex.getU2(), tex.getV2());
				gl.vertex(i+(xx+1)*stripWidth, j+1, height);
			}
		}
		else {
			tex = Kwirk.TEXREG_WATER;
			gl.normal(0.0f, 0.0f, 1.0f);
			gl.texCoord(tex.getU(), tex.getV());
			gl.vertex(i, j, -0.5f);
			
			gl.normal(0.0f, 0.0f, 1.0f);
			gl.texCoord(tex.getU2(), tex.getV());
			gl.vertex((i+1), j, -0.5f);
			
			gl.normal(0.0f, 0.0f, 1.0f);
			gl.texCoord(tex.getU(), tex.getV2());
			gl.vertex(i, j+1.2f, -0.5f);
			
			gl.normal(0.0f, 0.0f, 1.0f);
			gl.texCoord(tex.getU(), tex.getV2());
			gl.vertex(i, j+1.2f, -0.5f);
			
			gl.normal(0.0f, 0.0f, 1.0f);
			gl.texCoord(tex.getU2(), tex.getV());
			gl.vertex((i+1), j, -0.5f);
			
			gl.normal(0.0f, 0.0f, 1.0f);
			gl.texCoord(tex.getU2(), tex.getV2());
			gl.vertex((i+1), j+1.2f, -0.5f);
		}		
	}
	
	public boolean blocks(Entity e) {
		return !(e instanceof Pusher);
	}
}
