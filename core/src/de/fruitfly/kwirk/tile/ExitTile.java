package de.fruitfly.kwirk.tile;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.fruitfly.kwirk.Entity;
import de.fruitfly.kwirk.Kwirk;
import de.fruitfly.kwirk.SurfaceRenderer;
import de.fruitfly.kwirk.Ticks;

public class ExitTile extends Tile implements Ticks {
	public void tick() {

	}

	public void render(SurfaceRenderer gl, int x, int y) {
		
		TextureRegion tex = Kwirk.TEXREG_STAIRS;

		int numStairs = 5;
		float stairWidth = 1.0f / numStairs;
		for (int i = 0; i < numStairs; i++) {
			gl.normal(0.0f, 0.0f, 1.0f);
			gl.texCoord(tex.getU(), tex.getV());
			gl.vertex(x + i * stairWidth, y, -1 + i * stairWidth);

			gl.normal(0.0f, 0.0f, 1.0f);
			gl.texCoord(tex.getU2(), tex.getV());
			gl.vertex(x + (i + 1) * stairWidth, y, -1 + i * stairWidth);

			gl.normal(0.0f, 0.0f, 1.0f);
			gl.texCoord(tex.getU(), tex.getV2());
			gl.vertex(x + i * stairWidth, y + 1, -1 + i * stairWidth);

			gl.normal(0.0f, 0.0f, 1.0f);
			gl.texCoord(tex.getU(), tex.getV2());
			gl.vertex(x + i * stairWidth, y + 1, -1 + i * stairWidth);

			gl.normal(0.0f, 0.0f, 1.0f);
			gl.texCoord(tex.getU2(), tex.getV());
			gl.vertex(x + (i + 1) * stairWidth, y, -1 + i * stairWidth);

			gl.normal(0.0f, 0.0f, 1.0f);
			gl.texCoord(tex.getU2(), tex.getV2());
			gl.vertex(x + (i + 1) * stairWidth, y + 1, -1 + i * stairWidth);
		}

		tex = Kwirk.TEXREG_DARK;

		gl.normal(0.0f, 0.0f, 1.0f);
		gl.texCoord(tex.getU(), tex.getV());
		gl.vertex(x, y+1, -1.0f);

		gl.normal(0.0f, 0.0f, 1.0f);
		gl.texCoord(tex.getU2(), tex.getV());
		gl.vertex(x + 1, y+1, -1.0f);

		gl.normal(0.0f, 0.0f, 1.0f);
		gl.texCoord(tex.getU(), tex.getV2());
		gl.vertex(x, y+1, 0.0f);

		gl.normal(0.0f, 0.0f, 1.0f);
		gl.texCoord(tex.getU(), tex.getV2());
		gl.vertex(x, y+1, 0.0f);

		gl.normal(0.0f, 0.0f, 1.0f);
		gl.texCoord(tex.getU2(), tex.getV());
		gl.vertex(x + 1, y+1, -1.0f);

		gl.normal(0.0f, 0.0f, 1.0f);
		gl.texCoord(tex.getU2(), tex.getV2());
		gl.vertex(x + 1, y+1, 0.0f);
	}
	
	public boolean blocks(Entity e) {
		return false;
	}
}
