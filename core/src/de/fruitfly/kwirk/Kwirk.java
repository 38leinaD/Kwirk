package de.fruitfly.kwirk;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import de.fruitfly.kwirk.tile.ExitTile;
import de.fruitfly.kwirk.tile.RefTile;
import de.fruitfly.kwirk.tile.Tile;

public class Kwirk extends ApplicationAdapter {
	public static SpriteBatch batch;
	
	public static Level level;
	

	public static BitmapFont font;
	

	public static int ticker;
	
	public static List<Ticks> tickers = new LinkedList<Ticks>();
	
	public static boolean touchEventLeft = false;
	public static boolean touchEventRight = false;
	public static boolean touchEventUp = false;
	public static boolean touchEventDown = false;

	public static boolean debug_showRefTiles = false;
	
	public Ed editor;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		
		Texture fontTex = new Texture(Gdx.files.internal("04.png"));
		fontTex.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		TextureRegion fontTexReg = new TextureRegion(fontTex);
		//fontTexReg.flip(false, true);
		font = new BitmapFont(Gdx.files.internal("04.fnt"), fontTexReg, true);

		G.debugFont = new BitmapFont(true);
		
		G.sr = new ShapeRenderer();
		ShaderProgram sp = new ShaderProgram(Gdx.files.internal("wall.vsh"), Gdx.files.internal("wall.fsh"));
		System.out.println(sp.getLog());
		G.gl = new SurfaceRenderer(sp);
		G.cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		//cam = new PerspectiveCamera(70, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		Tex.init();
		
		/*
		Texture lvlTex = new Texture(Gdx.files.internal("level.bmp"));
		lvlTex.getTextureData().prepare();
		Pixmap pmap = lvlTex.getTextureData().consumePixmap();
		level.tileMap = new Tile[lvlTex.getWidth()][lvlTex.getHeight()];
		for (int y=0; y<lvlTex.getHeight(); y++) {
			for (int x=0; x<lvlTex.getWidth(); x++) {
				int p = pmap.getPixel(x, lvlTex.getHeight()-y-1);
				if (p == 0x000000ff) {
					WallTile t = new WallTile();
					level.tileMap[x][y] = t;
				}
				else if (p == 0x0000ffff) {
					// player
					player = new Player(x, y);
					player.addToLevel();
				}
				else if (p == 0xff0000ff) {
					// tri right
					Tri t = new Tri(x,y, 270.0f);
					level.entities.add(t);

				}
				else if (p == 0xffff00ff) {
					// tri left
					Tri t = new Tri(x,y, 90.0f);
					level.entities.add(t);

				}
				else if (p == 0xff00ffff) {
					// bar left
					Bar b = new Bar(x,y);
					level.entities.add(b);

				}
				else if (p == 0x00ff00ff) {
					// exit
					ExitTile t = new ExitTile();
					level.tileMap[x][y] = t;
				}
			}
		}
		*/
		level = Level.load(Gdx.files.getFileHandle("C:/Users/daniel.platz/Dropbox/Dev/Java/Games/Kwirk/Project/android/assets/levels/" + "test.lvl", FileType.Absolute));
		
		editor = new Ed();
	}

	boolean loading = true;
	int stage =0;
	public static Matrix4 projectionMatrix2D = new Matrix4();
	
	Vector2 touchDownLeft;
	int touchLeftId = -1;
	Vector2 touchDownRight;
	int touchRightId = -1;
	
	@Override
	public void render () {
		ticker++;
		
		editor.tick();
		
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl.glClearColor(104/255f, 136/255f, 252/255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glDisable(GL20.GL_BLEND);
		Gdx.gl.glDisable(GL20.GL_CULL_FACE);
		
		/*
		cam.position.set(1.0f,-3.0f, 9.0f);
		cam.up.set(0.0f, 0.0f, 1.0f);
		cam.lookAt(5.0f, 3.5f, 2.0f);
		*/
		G.cam.position.set(10.0f, 3.0f, 9.0f);
		G.cam.up.set(0.0f, 0.0f, 1.0f);
		G.cam.lookAt(10.0f, 8.0f, 0.0f);
		((OrthographicCamera)G.cam).zoom = 0.02f;
		//((OrthographicCamera)G.cam).zoom = 0.02f;
		
		G.cam.update();
		
		G.sr.setProjectionMatrix(G.cam.projection);
		G.sr.setTransformMatrix(G.cam.view);
		
		G.sr.setAutoShapeType(true);
		G.sr.begin();
		G.sr.setColor(Color.RED);
		G.sr.line(new Vector3(0.0f, 0.0f, 0.0f), new Vector3(1.0f, 0.0f, 0.0f));
		G.sr.setColor(Color.GREEN);
		G.sr.line(new Vector3(0.0f, 0.0f, 0.0f), new Vector3(0.0f, 1.0f, 0.0f));
		G.sr.setColor(Color.BLUE);
		G.sr.line(new Vector3(0.0f, 0.0f, 0.0f), new Vector3(0.0f, 0.0f, 1.0f));

		if (debug_showRefTiles) {
			G.sr.setColor(Color.RED);
			for (int x=0; x<level.tileMap.length; x++) {
				for (int y=0; y<level.tileMap[0].length; y++) {
					Tile t = level.tileMap[x][y];
					if (t != null) {
						G.sr.box(x, y, 1.0f, 1.0f, 1.0f, 1.0f);
					}
				}
			}
		}
		
		//shapeRenderer.box(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
		G.sr.end();
		
		Tex.TEXTURE_GAME_ART.bind();
		
		G.gl.begin(new Matrix4().idt(), G.cam.view, G.cam.projection, GL20.GL_TRIANGLES);
		for (int i=0; i<level.tileMap.length; i++) {
			for (int j=0; j<level.tileMap[0].length; j++) {
				Tile t = level.tileMap[i][j];
				
				if (t == null || (t instanceof RefTile)) {
					// floor
					TextureRegion tex;
					tex = Tex.TEXREG_FLOOR;
					
					G.gl.normal(0.0f, 0.0f, 1.0f);
					G.gl.texCoord(tex.getU(), tex.getV());
					G.gl.vertex(i, j, 0.0f);
					
					G.gl.normal(0.0f, 0.0f, 1.0f);
					G.gl.texCoord(tex.getU2(), tex.getV());
					G.gl.vertex(i+1, j, 0.0f);
					
					G.gl.normal(0.0f, 0.0f, 1.0f);
					G.gl.texCoord(tex.getU(), tex.getV2());
					G.gl.vertex(i, j+1, 0.0f);
					
					G.gl.normal(0.0f, 0.0f, 1.0f);
					G.gl.texCoord(tex.getU(), tex.getV2());
					G.gl.vertex(i, j+1, 0.0f);
					
					G.gl.normal(0.0f, 0.0f, 1.0f);
					G.gl.texCoord(tex.getU2(), tex.getV());
					G.gl.vertex(i+1, j, 0.0f);
					
					G.gl.normal(0.0f, 0.0f, 1.0f);
					G.gl.texCoord(tex.getU2(), tex.getV2());
					G.gl.vertex(i+1, j+1, 0.0f);
				}
				
				if (t == null) continue;
				if (!(t instanceof ExitTile)) t.render(G.gl, i, j);
			}
		}
		G.gl.end();
		
		
		
		for (Ticks t : tickers) {
			t.tick();
		}
		
		level.tick();
		level.render();
		
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc (GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		G.gl.begin(new Matrix4().idt(), G.cam.view, G.cam.projection, GL20.GL_TRIANGLES);
		for (int i=0; i<level.tileMap.length; i++) {
			for (int j=0; j<level.tileMap[0].length; j++) {
					
				Tile t = level.tileMap[i][j];
				if (t == null) continue;
				if (t instanceof ExitTile) t.render(G.gl, i, j);
			}
		}
		G.gl.end();
		/*
		shapeRenderer.begin();		
		for (int i=0; i<level.tileMap.length; i++) {
			for (int j=0; j<level.tileMap[0].length; j++) {
					
				Tile t = level.tileMap[i][j];
				if (t == null) continue;
				if (t instanceof RefTile) shapeRenderer.point(i, j, 2.0f);
			}
		}
		shapeRenderer.end();
		*/
		projectionMatrix2D.setToOrtho2D(0, Gdx.graphics.getHeight(), Gdx.graphics.getWidth(), -Gdx.graphics.getHeight());

		Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);

		G.sr.setTransformMatrix(new Matrix4().idt());
		G.sr.setProjectionMatrix(projectionMatrix2D);
		G.sr.begin();
		
		if (touchLeftId >= 0 && !Gdx.input.isTouched(touchLeftId)) {
			touchLeftId = -1;
			touchDownLeft = null;
		}
		
		if (touchRightId >= 0 && !Gdx.input.isTouched(touchRightId)) {
			touchRightId = -1;
			touchDownRight = null;
		}
		
		for (int t=0; t<2; t++) {
			if (Gdx.input.isTouched(t)) {
				float x = Gdx.input.getX(0);
				float y = Gdx.graphics.getHeight()-Gdx.input.getY(0);

				if (touchDownLeft == null) {
					if (x < 200) {
						G.sr.circle(x, y, 10.0f);
						touchDownLeft = new Vector2(x, y);
						touchLeftId = t;
					}
				}
				
				if (touchDownRight == null) {
					if (x > Gdx.graphics.getWidth() - 200) {
						G.sr.circle(x, y, 10.0f);
						touchDownRight = new Vector2(x, y);
						touchRightId = t;
					}
				}
	
			}
		}
		
		touchEventDown = false;
		touchEventLeft = false;
		touchEventRight = false;
		touchEventUp = false;
		/*
		if (touchDownLeft != null) {
			float x = Gdx.input.getX(touchLeftId);
			float y = Gdx.graphics.getHeight()-Gdx.input.getY(0);
			
			shapeRenderer.circle(touchDownLeft.x, touchDownLeft.y, 10.0f);
			shapeRenderer.line(touchDownLeft.x, touchDownLeft.y, x, y);
			
			Vector2 v = new Vector2(x, y);
			if (v.y > touchDownLeft.y && v.dst(touchDownLeft) > 50) {
				touchEventUp = true;
			}
			else if (v.y < touchDownLeft.y && v.dst(touchDownLeft) > 50) {
				touchEventDown = true;
			}
			else {
				touchEventLeft = true;
			}
		}
		
		if (touchDownRight != null) {
			float x = Gdx.input.getX(touchRightId);
			float y = Gdx.graphics.getHeight()-Gdx.input.getY(0);
			
			shapeRenderer.circle(touchDownRight.x, touchDownRight.y, 10.0f);
			shapeRenderer.line(touchDownRight.x, touchDownRight.y, x, y);

			Vector2 v = new Vector2(x, y);
			if (v.y > touchDownRight.y && v.dst(touchDownRight) > 50) {
				touchEventUp = true;
			}
			else if (v.y < touchDownRight.y && v.dst(touchDownRight) > 50) {
				touchEventDown = true;
			}
			else {
				touchEventRight = true;
			}
		}*/
		
		//shapeRenderer.line(Gdx.graphics.getWidth() - 200, 0, Gdx.graphics.getWidth() - 200, Gdx.graphics.getHeight(), Color.WHITE, Color.WHITE);
		//shapeRenderer.line(200, 0, 200, Gdx.graphics.getHeight(), Color.WHITE, Color.WHITE);

		G.sr.end();
		
		if (loading) {
			
			batch.setProjectionMatrix(projectionMatrix2D);
			font.setScale(5.0f);
			font.setUseIntegerPositions(true);
			batch.begin();
			
			TextBounds bounds = font.getBounds("Level 1");


				float x = Gdx.graphics.getWidth()/2.0f - bounds.width/2.0f;
				float y = Gdx.graphics.getHeight()/2.0f+bounds.height/2.0f;

			
			font.setColor(Color.BLUE);
			font.draw(batch, "Level 1", x+10, y-10);
			font.setColor(Color.WHITE);
			font.draw(batch, "Level 1", x, y);
	
	
			batch.end();
			
			if (ticker % 60 == 0) loading = false;
		}
		
		editor.render();
	}
}
