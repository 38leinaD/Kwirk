package de.fruitfly.kwirk.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import de.fruitfly.kwirk.Kwirk;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.x = 1920;
		config.y = 0;
//		config.width = 1920;
//		config.height = 1080;
		config.width = 1024;
		config.height = 768;
		new LwjglApplication(new Kwirk(), config);
	}
}
