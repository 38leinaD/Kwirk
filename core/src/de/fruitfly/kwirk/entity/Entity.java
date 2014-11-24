package de.fruitfly.kwirk.entity;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import de.fruitfly.kwirk.G;
import de.fruitfly.kwirk.Kwirk;
import de.fruitfly.kwirk.Level;
import de.fruitfly.kwirk.tile.RefTile;
import de.fruitfly.kwirk.tile.Tile;

public class Entity {
	protected int x, y;
	private static Matrix4 modelTransform = new Matrix4().idt();

	protected static int texIndexer = 0;
	protected int texIndex;
	
	public Entity(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void tick() {
		
	}
	
	public void render() {
		
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public void removeFromLevel() {
		for (int x=0; x<Kwirk.level.getEntityTileMap().length; x++) {
			for (int y=0; y<Kwirk.level.getEntityTileMap()[0].length; y++) {
				RefTile t = Kwirk.level.getEntityTileMap()[x][y];
				if (t != null && t.getParent() == this) {
					Kwirk.level.getEntityTileMap()[x][y] = null;
				}
			}
		}
		Kwirk.level.removeEntity(this);
	}
	
	public void addToLevel(Level level) {
		level.getEntities().add(this);
		level.getEntityTileMap()[this.x][this.y] = new RefTile(this);
	}

	public boolean blocks(Entity e) {
		return true;
	}
	
	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	protected void renderBlock(TextureRegion tex, int x, int y, float z) {
		modelTransform.idt();
		modelTransform.translate(x, y, z);
		
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
	
	protected void renderBlock(TextureRegion tex, int x, int y, float z, int xoff, int yoff, float angle) {
		modelTransform.idt();
		modelTransform.translate(x+0.5f, y+0.5f, z);
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
		G.gl.vertex(0, 0, 0.998f);
		
		G.gl.normal(0.0f, -1.0f, 0.0f);
		G.gl.texCoord(tex.getU(), tex.getV2());
		G.gl.vertex(0, 0, 0.998f);
		
		G.gl.normal(0.0f, -1.0f, 0.0f);
		G.gl.texCoord(tex.getU2(), tex.getV());
		G.gl.vertex(1, 0, 0.0f);
		
		G.gl.normal(0.0f, -1.0f, 0.0f);
		G.gl.texCoord(tex.getU2(), tex.getV2());
		G.gl.vertex(1, 0, 0.998f);
		
		// back
		G.gl.normal(0.0f, 1.0f, 0.0f);
		G.gl.texCoord(tex.getU(), tex.getV());
		G.gl.vertex(1, 1, 0.0f);
		
		G.gl.normal(0.0f, 1.0f, 0.0f);
		G.gl.texCoord(tex.getU2(), tex.getV());
		G.gl.vertex(0, 1, 0.0f);
		
		G.gl.normal(0.0f, 1.0f, 0.0f);
		G.gl.texCoord(tex.getU(), tex.getV2());
		G.gl.vertex(1, 1, 0.998f);
		
		G.gl.normal(0.0f, 1.0f, 0.0f);
		G.gl.texCoord(tex.getU(), tex.getV2());
		G.gl.vertex(1, 1, 0.998f);
		
		G.gl.normal(0.0f, 1.0f, 0.0f);
		G.gl.texCoord(tex.getU2(), tex.getV());
		G.gl.vertex(0, 1, 0.0f);
		
		G.gl.normal(0.0f, 1.0f, 0.0f);
		G.gl.texCoord(tex.getU2(), tex.getV2());
		G.gl.vertex(0, 1, 0.998f);
		
		// left
		G.gl.normal(-1.0f, 0.0f, 0.0f);
		G.gl.texCoord(tex.getU(), tex.getV());
		G.gl.vertex(0, 1, 0.0f);
		
		G.gl.normal(-1.0f, 0.0f, 0.0f);
		G.gl.texCoord(tex.getU2(), tex.getV());
		G.gl.vertex(0, 0, 0.0f);
		
		G.gl.normal(-1.0f, 0.0f, 0.0f);
		G.gl.texCoord(tex.getU(), tex.getV2());
		G.gl.vertex(0, 1, 0.998f);
		
		G.gl.normal(-1.0f, 0.0f, 0.0f);
		G.gl.texCoord(tex.getU(), tex.getV2());
		G.gl.vertex(0, 1, 0.998f);
		
		G.gl.normal(-1.0f, 0.0f, 0.0f);
		G.gl.texCoord(tex.getU2(), tex.getV());
		G.gl.vertex(0, 0, 0.0f);
		
		G.gl.normal(-1.0f, 0.0f, 0.0f);
		G.gl.texCoord(tex.getU2(), tex.getV2());
		G.gl.vertex(0, 0, 0.998f);
		
		// right
		G.gl.normal(1.0f, 0.0f, 0.0f);
		G.gl.texCoord(tex.getU(), tex.getV());
		G.gl.vertex(1, 0, 0.0f);
		
		G.gl.normal(1.0f, 0.0f, 0.0f);
		G.gl.texCoord(tex.getU2(), tex.getV());
		G.gl.vertex(1, 1, 0.0f);
		
		G.gl.normal(1.0f, 0.0f, 0.0f);
		G.gl.texCoord(tex.getU(), tex.getV2());
		G.gl.vertex(1, 0, 0.998f);
		
		G.gl.normal(1.0f, 0.0f, 0.0f);
		G.gl.texCoord(tex.getU(), tex.getV2());
		G.gl.vertex(1, 0, 0.998f);
		
		G.gl.normal(1.0f, 0.0f, 0.0f);
		G.gl.texCoord(tex.getU2(), tex.getV());
		G.gl.vertex(1, 1, 0.0f);
		
		G.gl.normal(1.0f, 0.0f, 0.0f);
		G.gl.texCoord(tex.getU2(), tex.getV2());
		G.gl.vertex(1, 1, 0.998f);
		
		// top
		G.gl.normal(0.0f, 0.0f, 1.0f);
		G.gl.texCoord(tex.getU(), tex.getV());
		G.gl.vertex(0, 0, 0.998f);
		
		G.gl.normal(0.0f, 0.0f, 1.0f);
		G.gl.texCoord(tex.getU2(), tex.getV());
		G.gl.vertex(1, 0, 0.998f);
		
		G.gl.normal(0.0f, 0.0f, 1.0f);
		G.gl.texCoord(tex.getU(), tex.getV2());
		G.gl.vertex(0, 1, 0.998f);
		
		G.gl.normal(0.0f, 0.0f, 1.0f);
		G.gl.texCoord(tex.getU(), tex.getV2());
		G.gl.vertex(0, 1, 0.998f);
		
		G.gl.normal(0.0f, 0.0f, 1.0f);
		G.gl.texCoord(tex.getU2(), tex.getV());
		G.gl.vertex(1, 0, 0.998f);
		
		G.gl.normal(0.0f, 0.0f, 1.0f);
		G.gl.texCoord(tex.getU2(), tex.getV2());
		G.gl.vertex(1, 1, 0.998f);
		
		G.gl.end();
	}
}
