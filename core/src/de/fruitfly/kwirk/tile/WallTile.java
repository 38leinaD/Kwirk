package de.fruitfly.kwirk.tile;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.fruitfly.kwirk.SurfaceRenderer;
import de.fruitfly.kwirk.Tex;

public class WallTile extends Tile {

	
	public void render(SurfaceRenderer gl, int i, int j) {
		TextureRegion tex;
		
			tex = Tex.TEXREG_WALL;
			
		// front
		gl.normal(0.0f, -1.0f, 0.0f);
		gl.texCoord(tex.getU(), tex.getV());
		gl.vertex(i, j, 0.0f);

		gl.normal(0.0f, -1.0f, 0.0f);
		gl.texCoord(tex.getU2(), tex.getV());
		gl.vertex(i + 1, j, 0.0f);

		gl.normal(0.0f, -1.0f, 0.0f);
		gl.texCoord(tex.getU(), tex.getV2());
		gl.vertex(i, j, 1.0f);

		gl.normal(0.0f, -1.0f, 0.0f);
		gl.texCoord(tex.getU(), tex.getV2());
		gl.vertex(i, j, 1.0f);

		gl.normal(0.0f, -1.0f, 0.0f);
		gl.texCoord(tex.getU2(), tex.getV());
		gl.vertex(i + 1, j, 0.0f);

		gl.normal(0.0f, -1.0f, 0.0f);
		gl.texCoord(tex.getU2(), tex.getV2());
		gl.vertex(i + 1, j, 1.0f);

		// left
		gl.normal(-1.0f, 0.0f, 0.0f);
		gl.texCoord(tex.getU(), tex.getV());
		gl.vertex(i, j+1, 0.0f);
		
		gl.normal(-1.0f, 0.0f, 0.0f);
		gl.texCoord(tex.getU2(), tex.getV());
		gl.vertex(i, j, 0.0f);
		
		gl.normal(-1.0f, 0.0f, 0.0f);
		gl.texCoord(tex.getU(), tex.getV2());
		gl.vertex(i, j, 1.0f);
		
		gl.normal(-1.0f, 0.0f, 0.0f);
		gl.texCoord(tex.getU(), tex.getV());
		gl.vertex(i, j+1, 0.0f);
		
		gl.normal(-1.0f, 0.0f, 0.0f);
		gl.texCoord(tex.getU(), tex.getV2());
		gl.vertex(i, j, 1.0f);
		
		gl.normal(-1.0f, 0.0f, 0.0f);
		gl.texCoord(tex.getU2(), tex.getV2());
		gl.vertex(i, j+1, 1.0f);
				
		
		// right
		gl.normal(-1.0f, 0.0f, 0.0f);
		gl.texCoord(tex.getU(), tex.getV());
		gl.vertex(i+1, j, 0.0f);
		
		gl.normal(-1.0f, 0.0f, 0.0f);
		gl.texCoord(tex.getU2(), tex.getV());
		gl.vertex(i+1, j+1, 0.0f);
		
		gl.normal(-1.0f, 0.0f, 0.0f);
		gl.texCoord(tex.getU(), tex.getV2());
		gl.vertex(i+1, j+1, 1.0f);
		
		gl.normal(-1.0f, 0.0f, 0.0f);
		gl.texCoord(tex.getU(), tex.getV());
		gl.vertex(i+1, j, 0.0f);
		
		gl.normal(-1.0f, 0.0f, 0.0f);
		gl.texCoord(tex.getU(), tex.getV2());
		gl.vertex(i+1, j+1, 1.0f);
		
		gl.normal(-1.0f, 0.0f, 0.0f);
		gl.texCoord(tex.getU2(), tex.getV2());
		gl.vertex(i+1, j, 1.0f);
		
		tex = Tex.TEXREG_CEIL;
		
		gl.normal(0.0f, 0.0f, 1.0f);
		gl.texCoord(tex.getU(), tex.getV());
		gl.vertex(i, j, 1.0f);
		
		gl.normal(0.0f, 0.0f, 1.0f);
		gl.texCoord(tex.getU2(), tex.getV());
		gl.vertex(i+1, j, 1.0f);
		
		gl.normal(0.0f, 0.0f, 1.0f);
		gl.texCoord(tex.getU(), tex.getV2());
		gl.vertex(i, j+1, 1.0f);
		
		gl.normal(0.0f, 0.0f, 1.0f);
		gl.texCoord(tex.getU(), tex.getV2());
		gl.vertex(i, j+1, 1.0f);
		
		gl.normal(0.0f, 0.0f, 1.0f);
		gl.texCoord(tex.getU2(), tex.getV());
		gl.vertex(i+1, j, 1.0f);
		
		gl.normal(0.0f, 0.0f, 1.0f);
		gl.texCoord(tex.getU2(), tex.getV2());
		gl.vertex(i+1, j+1, 1.0f);
	}
}
