package de.dakror.gravityrun.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import de.dakror.gravityrun.GravityRun;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280;
		config.height = 720;
		config.title = "GravityRun";
		config.vSyncEnabled = false;
		new LwjglApplication(new GravityRun(), config);
	}
}
