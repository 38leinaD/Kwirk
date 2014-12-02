package de.fruitfly.kwirk;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Tex {
	public static Texture TEXTURE_GAME_ART;
	public static TextureRegion TEXREG_WALL;
	public static TextureRegion TEXREG_CEIL;
	public static TextureRegion TEXREG_DARK;
	public static TextureRegion TEXREG_STAIRS;

	public static TextureRegion TEXREG_FLOOR;
	public static TextureRegion TEXREG_WATER;

	public static TextureRegion TEXREG_TRI_CENTER;
	public static TextureRegion TEXREG_TRI_ARM;
	public static TextureRegion[] TEXREG_KWIRK;
	public static TextureRegion[] TEXREG_KWURK;
	public static TextureRegion[] TEXREG_KWIRK_STONE;

	public static TextureRegion[] TEXREG_BAR;

	public static TextureRegion[] TEXREG_EXIT_PULSE;
	
	public static void init() {
		
		TEXTURE_GAME_ART = new Texture(Gdx.files.internal("tex.png"));
	
		TEXREG_WALL = new TextureRegion(TEXTURE_GAME_ART, 16, 0, 16, 16);
		TEXREG_WALL.flip(false, true);
		TEXREG_CEIL = new TextureRegion(TEXTURE_GAME_ART, 16, 0, 16, 16);
		TEXREG_CEIL.flip(false, true);
		
		TEXREG_FLOOR = new TextureRegion(TEXTURE_GAME_ART, 32, 0, 16, 16);
		TEXREG_FLOOR.flip(false, true);
		
		TEXREG_WATER = new TextureRegion(TEXTURE_GAME_ART, 64, 32, 16, 16);
		TEXREG_WATER.flip(false, true);
		
		TEXREG_DARK = new TextureRegion(TEXTURE_GAME_ART, 0, 48, 16, 16);
		TEXREG_DARK.flip(false, true);
		
		TEXREG_TRI_CENTER = new TextureRegion(TEXTURE_GAME_ART, 48, 0, 16, 16);
		TEXREG_TRI_CENTER.flip(false, true);
		
		TEXREG_TRI_ARM = new TextureRegion(TEXTURE_GAME_ART, 64, 0, 16, 16);
		TEXREG_TRI_ARM.flip(false, true);
		
		TEXREG_KWIRK = new TextureRegion[3];
		TEXREG_KWIRK[0] = new TextureRegion(TEXTURE_GAME_ART, 0, 16, 16, 16);
		TEXREG_KWIRK[0].flip(false, true);
		
		TEXREG_KWIRK[1] = new TextureRegion(TEXTURE_GAME_ART, 0, 32, 16, 16);
		TEXREG_KWIRK[1].flip(false, true);
		
		TEXREG_KWIRK[2] = new TextureRegion(TEXTURE_GAME_ART, 16, 16, 16, 16);
		TEXREG_KWIRK[2].flip(false, true);
		
		TEXREG_KWURK = new TextureRegion[3];
		TEXREG_KWURK[0] = new TextureRegion(TEXTURE_GAME_ART, 0, 64, 16, 16);
		TEXREG_KWURK[0].flip(false, true);
		
		TEXREG_KWURK[1] = new TextureRegion(TEXTURE_GAME_ART, 0, 80, 16, 16);
		TEXREG_KWURK[1].flip(false, true);
		
		TEXREG_KWURK[2] = new TextureRegion(TEXTURE_GAME_ART, 16, 64, 16, 16);
		TEXREG_KWURK[2].flip(false, true);
		
		
		TEXREG_KWIRK_STONE = new TextureRegion[3];
		TEXREG_KWIRK_STONE[0] = new TextureRegion(TEXTURE_GAME_ART, 32, 64, 16, 16);
		TEXREG_KWIRK_STONE[0].flip(false, true);
		
		TEXREG_KWIRK_STONE[1] = new TextureRegion(TEXTURE_GAME_ART, 32, 80, 16, 16);
		TEXREG_KWIRK_STONE[1].flip(false, true);
		
		TEXREG_KWIRK_STONE[2] = new TextureRegion(TEXTURE_GAME_ART, 48, 64, 16, 16);
		TEXREG_KWIRK_STONE[2].flip(false, true);
		
		TEXREG_STAIRS = new TextureRegion(TEXTURE_GAME_ART, 48, 32, 16, 16);
		TEXREG_STAIRS.flip(false, true);
		
		TEXREG_BAR = new TextureRegion[3];
		TEXREG_BAR[0] = new TextureRegion(TEXTURE_GAME_ART, 96, 0, 16, 16);
		TEXREG_BAR[0].flip(false, true);
		TEXREG_BAR[1] = new TextureRegion(TEXTURE_GAME_ART, 96, 16, 16, 16);
		TEXREG_BAR[1].flip(false, true);
		TEXREG_BAR[2] = new TextureRegion(TEXTURE_GAME_ART, 96, 32, 16, 16);
		TEXREG_BAR[2].flip(false, true);
		
		TEXREG_EXIT_PULSE = new TextureRegion[4];
		
		TEXREG_EXIT_PULSE[0] = new TextureRegion(TEXTURE_GAME_ART, 32, 16, 16, 16);
		TEXREG_EXIT_PULSE[0].flip(false, true);
		TEXREG_EXIT_PULSE[1] = new TextureRegion(TEXTURE_GAME_ART, 48, 16, 16, 16);
		TEXREG_EXIT_PULSE[1].flip(false, true);
		TEXREG_EXIT_PULSE[2] = new TextureRegion(TEXTURE_GAME_ART, 64, 16, 16, 16);
		TEXREG_EXIT_PULSE[2].flip(false, true);
		TEXREG_EXIT_PULSE[3] = new TextureRegion(TEXTURE_GAME_ART, 80, 16, 16, 16);
		TEXREG_EXIT_PULSE[3].flip(false, true);
		
	}
}
