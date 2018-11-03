package com.jb.fe.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.jb.fe.screens.FireEmblemGame;

public class DesktopLauncher {
	
	// Dimensions
	public static final int WIDTH = 240;
	public static final int HEIGHT = 160;
	public static final int CONSTANT = 5;
	public static final String TITLE = "Fire Emblem Java Edition";
	
	public static void main (String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		// Frame Settings
		config.title = TITLE;
		config.height = HEIGHT * CONSTANT;
		config.width = WIDTH * CONSTANT;
		config.x = -1;
		config.y = -1;
		
		// Windows
		config.vSyncEnabled = false;
		
		// Mac
		config.useHDPI = true;
		
		new LwjglApplication(new FireEmblemGame(), config);
	}
}