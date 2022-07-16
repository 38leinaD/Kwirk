package de.fruitfly.kwirk;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

import de.fruitfly.kwirk.entity.Player;

public class Kwirk extends ApplicationAdapter {
	public static SpriteBatch batch;

	public static Level level;

	public static BitmapFont font;

	public static int ticker;

	public static List<Ticks> tickers = new LinkedList<Ticks>();
	
	public Ed editor;
	
	public static Player[] controlableEntites = new Player[10];
	public static int controlableEntitesNum = 0;
	public static int controlledEntity = 0;
	
	private String[] levels = {
		"levels/original/level01.txt",
		"levels/original/level02.txt",
		"levels/original/level03.txt",
		"levels/original/level04.txt",
		"levels/original/level05.txt",
		"levels/original/level06.txt",
		"levels/original/level07.txt",
		"levels/original/level08.txt",
		"levels/original/level09.txt",
		"levels/original/level10.txt",
		"levels/original/level11.txt",
		"levels/original/level12.txt",
		"levels/original/level13.txt",
		"levels/original/level14.txt",
		"levels/original/level15.txt",
	};
	
	@Override
	public void create() {
		_instance = this;
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
		
		this.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		// cam = new PerspectiveCamera(70, Gdx.graphics.getWidth(),
		// Gdx.graphics.getHeight());

		Tex.init();

		/*
		loadLevel(Gdx.files
						.getFileHandle(
								"C:/Users/daniel.platz/Dropbox/Dev/Java/Games/Kwirk/Project/android/assets/levels/"
										+ "test.lvl", FileType.Absolute));

		editor = new Ed();
		*/
		G.soundBeam = Gdx.audio.newSound(Gdx.files.internal("sounds/beam.wav"));
		G.soundPusher = Gdx.audio.newSound(Gdx.files.internal("sounds/pusher.wav"));
		G.soundRotate = Gdx.audio.newSound(Gdx.files.internal("sounds/rotate.wav"));
		G.soundWalk = Gdx.audio.newSound(Gdx.files.internal("sounds/walk.wav"));
		
		loadLevel(Gdx.files.internal(levels[levelIndex]));
	}

	@Override
	public void resize(int width, int height) {
		int viewSize = 20;
		if (level != null) {
			viewSize = Math.max(level.getWidth(), level.getHeight());
		}
	viewSize-=2;
		float ratio = width/(float)height;
		int w, h;
		if (ratio > 1.0f) {
			// width > height
			h = viewSize;
			w = (int) (viewSize * ratio);
		}
		else {
			// height > width
			w = viewSize;
			h = (int) (viewSize / ratio);
		}
		G.cam = new OrthographicCamera(w, h);
	}

	public static boolean loading = true;
	int stage = 0;
	public static Matrix4 projectionMatrix2D = new Matrix4();

	private int levelIndex = 0;
	private boolean gameComplete = false;
	
	private void loadLevel(FileHandle fh) {
		ticker = 1;
		loading = true;
		for (int i=0; i<controlableEntites.length; i++) {
			controlableEntites[i] = null;
		}
		controlledEntity = 0;
		controlableEntitesNum = 0;
		level = Level.load(fh);
		resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}
	
	private void reloadCurrentLevel() {
		loadLevel(Gdx.files.internal(levels[levelIndex]));
	}
	
	public void levelCompleted(Level l) {
		if (levelIndex+1 >= levels.length) {
			gameComplete = true;
			return;
		}
		levelIndex++;
		reloadCurrentLevel();
	}
	
	
	private static Vector3 UP = new Vector3(0.0f, 1.0f, 0.0f);
	private static Vector3 DOWN = new Vector3(0.0f, -1.0f, 0.0f);
	private static Vector3 RIGHT = new Vector3(1.0f, 0.0f, 0.0f);
	private static Vector3 LEFT = new Vector3(-1.0f, 0.0f, 0.0f);
	
	private void tick() {
		ticker++;
		if (editor != null) editor.tick();

		Player player = controlableEntites[controlledEntity];
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			player.move(-1, 0);
		}
		else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			player.move(1, 0);
		}
		
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			player.move(0, 1);
		}
		else if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			player.move(0, -1);
		}
		
		if (Gdx.input.isKeyJustPressed(Keys.TAB)) {
			int i=0;
			for (; i<controlableEntites.length; i++) {
				if (controlableEntites[i] == null) break;
			}
			G.soundBeam.play();
			controlledEntity = (controlledEntity+1) % i;
		}
		
		
		if (Gdx.input.isKeyJustPressed(Keys.R) || Gdx.input.isTouched(2)) {
			reloadCurrentLevel();
			return;
		}
		
		if (editor == null && Gdx.input.isButtonPressed(0)) {
			float x = Gdx.input.getX();
			float y = Gdx.input.getY();
			
			Vector3 touchPos = new Vector3();
			Ray r = G.cam.getPickRay(x, y);
			float alpha = -r.origin.z/r.direction.z;
	
			r.getEndPoint(touchPos, alpha);
			
			Vector3 playerPos = null;
			for (int i=0; i<controlableEntitesNum; i++) {
				Player newPlayer = controlableEntites[i];
				if (newPlayer != player) {
					playerPos = new Vector3(newPlayer.getX()+0.5f, newPlayer.getY()+0.5f, 0.0f);
					if (playerPos.dst(touchPos) <= 0.7) {
						controlledEntity = i;
						player = null;
						break;
					}
				}
			}
			
			if (player != null) {
				playerPos = new Vector3(player.getX()+0.5f, player.getY()+0.5f, 0.0f);
				touchPos.sub(playerPos);
				
				if (touchPos.len() < 0.5f) {
					//System.out.println("ignore touch " + touchPos.len());
					return;
				}
				touchPos.nor();
				
				if (touchPos.dot(UP) > 0.8f) {
					player.move(0, 1);
				}
				else if (touchPos.dot(DOWN) > 0.8f) {
					player.move(0, -1);
				}
				else if (touchPos.dot(LEFT) > 0.8f) {
					player.move(-1, 0);
				}
				else if (touchPos.dot(RIGHT) > 0.8f) {
					player.move(1, 0);
				}
			}
		}
		
		for (Ticks t : tickers) {
			t.tick();
		}

		level.tick();
	}
	
	@Override
	public void render() {
		tick();

		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl.glClearColor(104 / 255f, 136 / 255f, 252 / 255f, 1);
		//Gdx.gl.glClearColor(0.2f, 0.4f, 0.6f, 1);
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glDisable(GL20.GL_BLEND);
		Gdx.gl.glEnable(GL20.GL_CULL_FACE);
		//Gdx.gl.glCullFace(GL20.GL_BACK);
		
		level.render();
		
		projectionMatrix2D.setToOrtho2D(0, Gdx.graphics.getHeight(),
				Gdx.graphics.getWidth(), -Gdx.graphics.getHeight());

		Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
		
		if (loading) {

			batch.setProjectionMatrix(projectionMatrix2D);
			font.setScale(5.0f);
			font.setUseIntegerPositions(true);
			batch.begin();

			String levelName = "Level " + (levelIndex+1);
			TextBounds bounds = font.getBounds(levelName);

			float x = Gdx.graphics.getWidth() / 2.0f - bounds.width / 2.0f;
			float y = Gdx.graphics.getHeight() / 2.0f - bounds.height / 2.0f;

			font.setColor(Color.BLUE);
			font.draw(batch, levelName, x + 10, y - 10);
			font.setColor(Color.WHITE);
			font.draw(batch, levelName, x, y);

			batch.end();

			if (ticker % 60 == 0)
				loading = false;
		}

		if (gameComplete) {
			batch.setProjectionMatrix(projectionMatrix2D);
			font.setScale(5.0f);
			font.setUseIntegerPositions(true);
			batch.begin();

			String levelName = "You Won!";
			TextBounds bounds = font.getBounds(levelName);

			float x = Gdx.graphics.getWidth() / 2.0f - bounds.width / 2.0f;
			float y = Gdx.graphics.getHeight() / 2.0f - bounds.height / 2.0f;

			font.setColor(Color.BLUE);
			font.draw(batch, levelName, x + 10, y - 10);
			font.setColor(Color.WHITE);
			font.draw(batch, levelName, x, y);
			
			font.setScale(1.0f);
			font.draw(batch, "Thanks for playing this demo.", x, y+150);
			font.draw(batch, "                              - Dan", x, y+200);

			batch.end();
		}
		
		if (editor != null) editor.render();
	}
	
	private static Kwirk _instance = null;
	public static Kwirk getInstance() {
		return _instance;
	}
}
