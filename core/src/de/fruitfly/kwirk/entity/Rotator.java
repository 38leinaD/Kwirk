package de.fruitfly.kwirk.entity;

import java.awt.Point;

import de.fruitfly.kwirk.Kwirk;
import de.fruitfly.kwirk.Level;
import de.fruitfly.kwirk.Tex;
import de.fruitfly.kwirk.tile.RefTile;
import de.fruitfly.kwirk.tile.Tile;
import de.fruitfly.kwirk.tile.WaterTile;

public class Rotator extends Entity {
	protected int[][] bitmap;
	protected int[][] oldBitmap;

	private static int texIndexer = 0;
	private int texIndex;
	private boolean rotating = false;
	private float rotationAngle;
	
	public Rotator(int x, int y, int[][] bitmap) {
		super(x, y);
		texIndex = (texIndexer++) % Tex.TEXREG_BAR.length;
		this.bitmap = bitmap;
		this.oldBitmap = new int[bitmap.length][bitmap[0].length];
	}
	
	@Override
	public void addToLevel(Level level) {
		level.getEntities().add(this);
		addRefTiles(level);
	}
	
	private void addRefTiles(Level level) {
		int s = (bitmap.length)/2;
		for (int x=0; x<bitmap.length; x++) {
			for (int y=0; y<bitmap[0].length; y++) {
				if (bitmap[x][y]==0) continue;
				level.getEntityTileMap()[getX()-s+x][getY()-s+y] = new RefTile(this);
			}
		}
	}
	
	private void clearRefTiles(Level level) {
		int s = (bitmap.length)/2;
		for (int x=0; x<bitmap.length; x++) {
			for (int y=0; y<bitmap[0].length; y++) {
				if (bitmap[x][y]==0) continue;
				level.getEntityTileMap()[getX()-s+x][getY()-s+y] = null;
			}
		}
	}
	
	private static void resetBitmap(int[][] bm) {
		for (int x=0; x<bm.length; x++) {
			for (int y=0; y<bm[0].length; y++) {
				bm[x][y]=0;
			}
		}
	}
	
	public void push(Player p, int xoff, int yoff) {
		assert xoff != 0 && yoff == 0 || xoff == 0 && yoff != 0;
		System.out.println(getY());
		int rotationDir;
		if (xoff != 0) {
			if (p.getY() == getY()) {
				return;
			}
			else if ((xoff > 0 && p.getY() > getY()) || (xoff < 0 && p.getY() < getY())) {
				// rotate -90deg
				rotationDir = -1;
			}
			else {
				// rotate +90deg
				rotationDir = +1;
			}
		}
		else /*(yoff != 0)*/ {
			if (p.getX() == getX()) {
				return;
			}
			else if ((yoff > 0 && p.getX() > getX()) || (yoff < 0 && p.getX() < getX())) {
				// rotate +90deg
				rotationDir = 1;
			}
			else {
				// rotate -90deg
				rotationDir = -1;
			}
		}
		
		int m00, m10, m11, m01;
		if (rotationDir == 1) {
			m00=0; m10=-1;
			m01=1; m11=0;
		}
		else {
			m00=0; m10=1;
			m01=-1; m11=0;
		}
		
		resetBitmap(oldBitmap);
		int s = bitmap.length/2;
		
		// create rotated bitmap
		for (int x=0; x<bitmap.length; x++) {
			for (int y=0; y<bitmap[0].length; y++) {
				int xx = x - s;
				int yy = y - s;
				int fx = (xx*m00 + yy*m10) + s;
				int fy = (xx*m01 + yy*m11) + s;
				oldBitmap[fx][fy] = bitmap[x][y];
			}
		}
		
		// check if rotated bitmap is blocked by other entity or tile
		for (int x=0; x<oldBitmap.length; x++) {
			for (int y=0; y<oldBitmap[0].length; y++) {
				if (oldBitmap[x][y]==0) continue;
				Tile t = Kwirk.level.getTileMap()[getX()-s+x][getY()-s+y];
				if (t != null && t.blocks(this)) {
					// blocking tile
					return;
				}
				else {
					RefTile rt = Kwirk.level.getEntityTileMap()[getX()-s+x][getY()-s+y];
					if (rt != null && rt.getParent() != this && rt.getParent() != p) {
						return;
					}
				}
			}
		}
		
		// check where player can go
		Point playerMove = null;
		for (int i=1; i<=2; i++) {
			// either xoff or yoff, so one is zeroed here
			if (oldBitmap[p.getX()-getX()+s+i*xoff][p.getY()-getY()+s+i*yoff] == 0) {
				playerMove = new Point(xoff*i, yoff*i);
				break;
			}
		}
		
		if (playerMove == null) {
			// cancel it; nowhere for player to go
			System.out.println("Move canceled; nowhere for player to move");
			return;
		}
		p.move(playerMove.x, playerMove.y);
		
		clearRefTiles(Kwirk.level);
		int[][] tmp = bitmap;
		bitmap = oldBitmap;
		oldBitmap = tmp;
		addRefTiles(Kwirk.level);
		
		rotating = true;
		rotationAngle = rotationDir*90.0f;
	}

	public int[][] getBitmap() {
		return bitmap;
	}
	
	@Override
	public void tick() {
		if (rotating) {

			if (rotationAngle < 0.0f) {
				rotationAngle += 5.0f;
			}
			else if (rotationAngle > 0.0f){
				rotationAngle -= 5.0f;
			}
			
			if (Math.abs(rotationAngle) < 5.0f) {
				rotating = false;
				rotationAngle = 0.0f;
			}
		}
	}

	@Override
	public void render() {
		int s = bitmap.length/2;
		
		if (rotating) {
			float angle = 0.0f;
			if (rotationAngle < 0.0f) {
				angle = -90.0f - rotationAngle;
			}
			else if (rotationAngle > 0.0f) {
				angle = 90.0f - rotationAngle;
			}

			for (int x=0; x<oldBitmap.length; x++) {
				for (int y=0; y<oldBitmap[0].length; y++) {
					if (oldBitmap[x][y]==0) continue;
					this.renderBlock(Tex.TEXREG_BAR[texIndex], this.x, this.y, 0.0f, -s+x, -s+y, angle);				
				}
			}
		}
		else {
			for (int x=0; x<bitmap.length; x++) {
				for (int y=0; y<bitmap[0].length; y++) {
					if (bitmap[x][y]==0) continue;
					this.renderBlock(Tex.TEXREG_BAR[texIndex], this.x-s+x, this.y-s+y, 0.0f);				
				}
			}
		}
	}
}
