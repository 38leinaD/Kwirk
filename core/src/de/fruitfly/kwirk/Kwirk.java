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

	public Ed editor;

	@Override
	public void create() {
		batch = new SpriteBatch();

		Texture fontTex = new Texture(Gdx.files.internal("04.png"));
		fontTex.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		TextureRegion fontTexReg = new TextureRegion(fontTex);
		font = new BitmapFont(Gdx.files.internal("04.fnt"), fontTexReg, true);

		G.debugFont = new BitmapFont(true);

		G.sr = new ShapeRenderer();
		ShaderProgram sp = new ShaderProgram(Gdx.files.internal("wall.vsh"),
				Gdx.files.internal("wall.fsh"));

		G.gl = new SurfaceRenderer(sp);
		G.cam = new OrthographicCamera(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		// cam = new PerspectiveCamera(70, Gdx.graphics.getWidth(),
		// Gdx.graphics.getHeight());

		Tex.init();

		level = Level
				.load(Gdx.files
						.getFileHandle(
								"C:/Users/daniel.platz/Dropbox/Dev/Java/Games/Kwirk/Project/android/assets/levels/"
										+ "test.lvl", FileType.Absolute));

		editor = new Ed();
	}

	boolean loading = true;
	int stage = 0;
	public static Matrix4 projectionMatrix2D = new Matrix4();

	Vector2 touchDownLeft;
	int touchLeftId = -1;
	Vector2 touchDownRight;
	int touchRightId = -1;

	@Override
	public void render() {
		ticker++;

		editor.tick();

		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl.glClearColor(104 / 255f, 136 / 255f, 252 / 255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glDisable(GL20.GL_BLEND);
		Gdx.gl.glDisable(GL20.GL_CULL_FACE);

		for (Ticks t : tickers) {
			t.tick();
		}

		level.tick();
		level.render();
		
		projectionMatrix2D.setToOrtho2D(0, Gdx.graphics.getHeight(),
				Gdx.graphics.getWidth(), -Gdx.graphics.getHeight());

		Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
		
		if (loading) {

			batch.setProjectionMatrix(projectionMatrix2D);
			font.setScale(5.0f);
			font.setUseIntegerPositions(true);
			batch.begin();

			TextBounds bounds = font.getBounds("Level 1");

			float x = Gdx.graphics.getWidth() / 2.0f - bounds.width / 2.0f;
			float y = Gdx.graphics.getHeight() / 2.0f + bounds.height / 2.0f;

			font.setColor(Color.BLUE);
			font.draw(batch, "Level 1", x + 10, y - 10);
			font.setColor(Color.WHITE);
			font.draw(batch, "Level 1", x, y);

			batch.end();

			if (ticker % 60 == 0)
				loading = false;
		}

		editor.render();
	}
}
