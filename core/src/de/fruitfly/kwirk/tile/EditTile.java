package de.fruitfly.kwirk.tile;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.fruitfly.kwirk.Kwirk;
import de.fruitfly.kwirk.SurfaceRenderer;

public class EditTile extends Tile {

	public void render(SurfaceRenderer gl, int i, int j) {
		TextureRegion tex;
		tex = Kwirk.TEXREG_BAR[2];
		
		gl.normal(0.0f, -1.0f, 0.0f);
		gl.texCoord(tex.getU(), tex.getV());
		gl.vertex(i, j, 0.0f);
		
		gl.normal(0.0f, -1.0f, 0.0f);
		gl.texCoord(tex.getU2(), tex.getV());
		gl.vertex(i+1, j, 0.0f);
		
		gl.normal(0.0f, -1.0f, 0.0f);
		gl.texCoord(tex.getU(), tex.getV2());
		gl.vertex(i, j, 1.0f);
		
		gl.normal(0.0f, -1.0f, 0.0f);
		gl.texCoord(tex.getU(), tex.getV2());
		gl.vertex(i, j, 1.0f);
		
		gl.normal(0.0f, -1.0f, 0.0f);
		gl.texCoord(tex.getU2(), tex.getV());
		gl.vertex(i+1, j, 0.0f);
		
		gl.normal(0.0f, -1.0f, 0.0f);
		gl.texCoord(tex.getU2(), tex.getV2());
		gl.vertex(i+1, j, 1.0f);
		
		tex = Kwirk.TEXREG_BAR[2];
		
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
