package de.fruitfly.kwirk.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import de.fruitfly.kwirk.G;
import de.fruitfly.kwirk.Kwirk;
import de.fruitfly.kwirk.Tex;
import de.fruitfly.kwirk.tile.ExitTile;
import de.fruitfly.kwirk.tile.RefTile;
import de.fruitfly.kwirk.tile.Tile;
import de.fruitfly.kwirk.tile.WaterTile;

public class Player extends Entity {
	private int newAngle;
	private float angle;
	private boolean moving = false;
	private static Matrix4 modelTransform = new Matrix4().idt();
	private int ticker;
	private TextureRegion[] skin;
	
	public Player(int x, int y, TextureRegion[] skin) {
		super(x, y);
		this.interpX = x;
		this.interpY = y;
		this.newAngle = 180;
		this.angle = this.newAngle;
		this.skin = skin;
	}

	public void tick() {
		ticker++;
		interpolateMove();
		interpolateRotation();

		if (exitTicker > 0) exitTicker--;
	}
	
	private void rotate(int keyAngle) {
		if (moving) return;
		
		if (keyAngle == newAngle) return;
		
		if (Math.abs(newAngle - angle) <= 0.01f) {
			// steady -> start turn
			//if (normalizeDegrees(newAngle - keyAngle) == 180) {
				newAngle = keyAngle;
				angle = keyAngle;
			/*}
			else {
				newAngle = keyAngle;
			}
			*/
		}
	}
	
	private void interpolateRotation() {
		if (newAngle != angle) {
			// turning
			if (Math.abs(newAngle - angle) <= 0.01f) {
				angle = newAngle; 
			}
			else {
				if ((newAngle - angle) < (angle - newAngle)) {
					angle -= 10.0f;
				}
				else {
					angle += 10.0f;
				}
				
				if (angle >= 360.0f) angle -= 360.0f;
				if (angle < 0.0f) angle += 360.0f;
			}
		}
	}
	
	private float interpX, interpY;
	private float velX, velY;
	private float bob;
	private int exitTicker = -1; 
	
	private void interpolateMove() {
		if (!moving) return;
		interpX += velX;
		interpY += velY;
		
		if (Math.abs(x - interpX) <= 0.1f && Math.abs(y - interpY) <= 0.1f) {
			moving = false;
			velX = 0.0f;
			velY = 0.0f;
			interpX = x;
			interpY = y;
			G.soundWalk.stop();
			if (Kwirk.level.getTileMap()[x][y] instanceof ExitTile) {
				G.soundBeam.play();
				exitTicker = 40;
			}
		}
		else {
			bob = 0.05f*MathUtils.sin(ticker*0.2f)+0.1f;
		}
	}

	public void move(int xoff, int yoff) {
		if (moving) return;

		if (xoff < 0) {
			this.rotate(90);
		}
		else if (xoff > 0) {
			this.rotate(270);
		}
		else if (yoff > 0) {
			this.rotate(0);
		}
		else {
			this.rotate(180);
		}
		
		if (isBlocked(x + xoff, y + yoff)) {
			push(x, y, xoff, yoff);
			return;
		}
		
		_move(xoff, yoff);
	}
	
	protected void _move(int xoff, int yoff) {
		G.soundWalk.play();
		Kwirk.level.getEntityTileMap()[x][y] = null;
		interpX = x;
		interpY = y;
		velX = xoff * 0.08f;
		velY = yoff * 0.08f;
		x = x + xoff;
		y = y + yoff;
		moving = true;
		Kwirk.level.getEntityTileMap()[x][y] = new RefTile(this);
	}
	
	private void push(int playerX, int playerY, int pushX, int pushY) {
		RefTile rt = Kwirk.level.getEntityTileMap()[playerX+pushX][playerY+pushY];
		Tile t = Kwirk.level.getTileMap()[playerX+pushX][playerY+pushY];
		
		// we cannot push when there is a watertile where we want to push as we cannot stand here.
		if (t instanceof WaterTile) return;
		
		if (rt != null) {
			Entity entity = rt.getParent();
			if (entity instanceof Pusher) {
				Pusher p = (Pusher) entity;
				if (p.push(pushX, pushY)) move(pushX, pushY);;
			}
			else if (entity instanceof Rotator) {
				Rotator r = (Rotator) entity;
				r.push(this, pushX, pushY);
			}
		}
	}

	private boolean isBlocked(int x, int y) {
		return (Kwirk.level.getTileMap()[x][y] != null && Kwirk.level.getTileMap()[x][y].blocks(this)) ||
				(Kwirk.level.getEntityTileMap()[x][y] != null && Kwirk.level.getEntityTileMap()[x][y].getParent().blocks(this));
	}

	public void render() {
		float x = this.interpX;
		float y = this.interpY;
		
		TextureRegion tex;
		
		modelTransform.idt();
		modelTransform.translate(x+0.5f, y+0.5f, bob);
		modelTransform.scl(0.8f);
		if (ticker > 0 && ticker < 20) {
			float t = (float) (Math.PI/2.0f*ticker/20.0f);
			modelTransform.translate(0.0f, 0.0f, (10-10*MathUtils.sin(t)));
			modelTransform.scale(MathUtils.sin(t), MathUtils.sin(t), 1+(10-10*MathUtils.sin(t)));
		}
		else if (exitTicker > 0) {
			float t = (float) (Math.PI/2.0f*exitTicker/40.0f);
			modelTransform.translate(0.0f, 0.0f, (10-10*MathUtils.sin(t)));
			modelTransform.scale(MathUtils.sin(t), MathUtils.sin(t), 1+(10-10*MathUtils.sin(t)));
		}
		else if (exitTicker == 0) {
			Kwirk.getInstance().levelCompleted(Kwirk.level);
			return;
		}
		modelTransform.rotate(new Vector3(0.0f, 0.0f, 1.0f), angle);
		//modelViewProjection.set(Kwirk.cam.combined).mul(modelTransform);
		
		G.gl.begin(modelTransform, G.cam.view, G.cam.projection, GL20.GL_TRIANGLES);
		
		if (this == Kwirk.controlableEntites[Kwirk.controlledEntity]) {
			skin = Tex.TEXREG_KWIRK;
		}
		else {
			skin = Tex.TEXREG_KWIRK_STONE;
		}
		
		if (ticker % 80 < 5) {
			tex = skin[1];
		}
		else {
			tex = skin[0];
		}
		// back
		G.gl.normal(0.0f, 1.0f, 0.0f);
		G.gl.texCoord(tex.getU(), tex.getV());
		G.gl.vertex(0.5f, 0.5f, 0.0f);
		
		G.gl.normal(0.0f, 1.0f, 0.0f);
		G.gl.texCoord(tex.getU2(), tex.getV());
		G.gl.vertex(-0.5f, 0.5f, 0.0f);
		
		G.gl.normal(0.0f, 1.0f, 0.0f);
		G.gl.texCoord(tex.getU(), tex.getV2());
		G.gl.vertex(0.5f, 0.5f, 0.999f);
		
		G.gl.normal(0.0f, 1.0f, 0.0f);
		G.gl.texCoord(tex.getU(), tex.getV2());
		G.gl.vertex(0.5f, 0.5f, 0.999f);
		
		G.gl.normal(0.0f, 1.0f, 0.0f);
		G.gl.texCoord(tex.getU2(), tex.getV());
		G.gl.vertex(-0.5f, 0.5f, 0.0f);
		
		G.gl.normal(0.0f, 1.0f, 0.0f);
		G.gl.texCoord(tex.getU2(), tex.getV2());
		G.gl.vertex(-0.5f, 0.5f, 0.999f);
		
		tex = skin[2];
		
		// front
		G.gl.normal(0.0f, -1.0f, 0.0f);
		G.gl.texCoord(tex.getU(), tex.getV());
		G.gl.vertex(-0.5f, -0.5f, 0.0f);
		
		G.gl.normal(0.0f, -1.0f, 0.0f);
		G.gl.texCoord(tex.getU2(), tex.getV());
		G.gl.vertex(0.5f, -0.5f, 0.0f);
		
		G.gl.normal(0.0f, -1.0f, 0.0f);
		G.gl.texCoord(tex.getU(), tex.getV2());
		G.gl.vertex(-0.5f, -0.5f, 0.999f);
		
		G.gl.normal(0.0f, -1.0f, 0.0f);
		G.gl.texCoord(tex.getU(), tex.getV2());
		G.gl.vertex(-0.5f, -0.5f, 0.999f);
		
		G.gl.normal(0.0f, -1.0f, 0.0f);
		G.gl.texCoord(tex.getU2(), tex.getV());
		G.gl.vertex(0.5f, -0.5f, 0.0f);
		
		G.gl.normal(0.0f, -1.0f, 0.0f);
		G.gl.texCoord(tex.getU2(), tex.getV2());
		G.gl.vertex(0.5f, -0.5f, 0.999f);

		// left
		G.gl.normal(-1.0f, 0.0f, 0.0f);
		G.gl.texCoord(tex.getU(), tex.getV());
		G.gl.vertex(-0.5f, 0.5f, 0.0f);
		
		G.gl.normal(-1.0f, 0.0f, 0.0f);
		G.gl.texCoord(tex.getU2(), tex.getV());
		G.gl.vertex(-0.5f, -0.5f, 0.0f);
		
		G.gl.normal(-1.0f, 0.0f, 0.0f);
		G.gl.texCoord(tex.getU(), tex.getV2());
		G.gl.vertex(-0.5f, 0.5f, 0.999f);
		
		G.gl.normal(-1.0f, 0.0f, 0.0f);
		G.gl.texCoord(tex.getU(), tex.getV2());
		G.gl.vertex(-0.5f, 0.5f, 0.999f);
		
		G.gl.normal(-1.0f, 0.0f, 0.0f);
		G.gl.texCoord(tex.getU2(), tex.getV());
		G.gl.vertex(-0.5f, -0.5f, 0.0f);
		
		G.gl.normal(-1.0f, 0.0f, 0.0f);
		G.gl.texCoord(tex.getU2(), tex.getV2());
		G.gl.vertex(-0.5f, -0.5f, 0.999f);
		
		// right
		G.gl.normal(1.0f, 0.0f, 0.0f);
		G.gl.texCoord(tex.getU(), tex.getV());
		G.gl.vertex(0.5f, -0.5f, 0.0f);
		
		G.gl.normal(1.0f, 0.0f, 0.0f);
		G.gl.texCoord(tex.getU2(), tex.getV());
		G.gl.vertex(0.5f, 0.5f, 0.0f);
		
		G.gl.normal(1.0f, 0.0f, 0.0f);
		G.gl.texCoord(tex.getU(), tex.getV2());
		G.gl.vertex(0.5f, -0.5f, 0.999f);
		
		G.gl.normal(1.0f, 0.0f, 0.0f);
		G.gl.texCoord(tex.getU(), tex.getV2());
		G.gl.vertex(0.5f, -0.5f, 0.999f);
		
		G.gl.normal(1.0f, 0.0f, 0.0f);
		G.gl.texCoord(tex.getU2(), tex.getV());
		G.gl.vertex(0.5f, 0.5f, 0.0f);
		
		G.gl.normal(1.0f, 0.0f, 0.0f);
		G.gl.texCoord(tex.getU2(), tex.getV2());
		G.gl.vertex(0.5f, 0.5f, 0.999f);
		
		// top
		G.gl.normal(0.0f, 0.0f, 1.0f);
		G.gl.texCoord(tex.getU(), tex.getV());
		G.gl.vertex(-0.5f, -0.5f, 0.999f);
		
		G.gl.normal(0.0f, 0.0f, 1.0f);
		G.gl.texCoord(tex.getU2(), tex.getV());
		G.gl.vertex(0.5f, -0.5f, 0.999f);
		
		G.gl.normal(0.0f, 0.0f, 1.0f);
		G.gl.texCoord(tex.getU(), tex.getV2());
		G.gl.vertex(-0.5f, 0.5f, 0.999f);
		
		G.gl.normal(0.0f, 0.0f, 1.0f);
		G.gl.texCoord(tex.getU(), tex.getV2());
		G.gl.vertex(-0.5f, 0.5f, 0.999f);
		
		G.gl.normal(0.0f, 0.0f, 1.0f);
		G.gl.texCoord(tex.getU2(), tex.getV());
		G.gl.vertex(0.5f, -0.5f, 0.999f);
		
		G.gl.normal(0.0f, 0.0f, 1.0f);
		G.gl.texCoord(tex.getU2(), tex.getV2());
		G.gl.vertex(0.5f, 0.5f, 0.999f);
		
		// bottom
		G.gl.normal(0.0f, 0.0f, -1.0f);
		G.gl.texCoord(tex.getU(), tex.getV());
		G.gl.vertex(0.5f, -0.5f, 0.0f);
		
		G.gl.normal(0.0f, 0.0f, -1.0f);
		G.gl.texCoord(tex.getU2(), tex.getV());
		G.gl.vertex(-0.5f, -0.5f, 0.0f);
		
		G.gl.normal(0.0f, 0.0f, -1.0f);
		G.gl.texCoord(tex.getU(), tex.getV2());
		G.gl.vertex(0.5f, 0.5f, 0.0f);
		
		G.gl.normal(0.0f, 0.0f, -1.0f);
		G.gl.texCoord(tex.getU(), tex.getV2());
		G.gl.vertex(0.5f, 0.5f, 0.0f);
		
		G.gl.normal(0.0f, 0.0f, -1.0f);
		G.gl.texCoord(tex.getU2(), tex.getV());
		G.gl.vertex(-0.5f, -0.5f, 0.0f);
		
		G.gl.normal(0.0f, 0.0f, -1.0f);
		G.gl.texCoord(tex.getU2(), tex.getV2());
		G.gl.vertex(-0.5f, 0.5f, 0.0f);
		
		G.gl.end();
		
	}

	@Override
	public void setX(int x) {
		super.setX(x);
		this.interpX = x;
	}

	@Override
	public void setY(int y) {
		super.setY(y);
		this.interpY = y;
	}
	
}
