package com.jb.fe.screens;

import java.util.HashMap;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jb.fe.systems.audio.MusicSystem;
import com.jb.fe.systems.audio.SoundSystem;

/*
 * Preload stage happens here. This is for every system that is required for the entire game
 */

public class FireEmblemGame extends Game {

	// Size
	public static final int WIDTH = 240;
	public static final int HEIGHT = 160;
	public static final int CONSTANT = 5;
	public static final String TITLE = "Fire Emblem";

	// Graphics
	private AssetManager assetManager;
	private SpriteBatch spriteBatch;

	// Camera
	private OrthographicCamera gameCamera;

	// Audio
	private SoundSystem soundSystem;
	private MusicSystem musicSystem;

	// Screens
	private HashMap<String, ScreenAdapter> allGameScreens;

	// Engine
	private Engine engine;

	public FireEmblemGame() {}


	public void create() {

		// Camera
		gameCamera = new OrthographicCamera(WIDTH, HEIGHT);
		gameCamera.translate(WIDTH / 2, HEIGHT / 2);
		gameCamera.update();
		
		// Asset Manager
		assetManager = new AssetManager();

		// Graphics
		spriteBatch = new SpriteBatch();
		spriteBatch.setProjectionMatrix(gameCamera.combined);

		// Engine
		engine = new Engine();
		
		// Audio
		musicSystem = new MusicSystem();
		soundSystem = new SoundSystem();

		// Screens
		allGameScreens = new HashMap<>();
		allGameScreens.put("GameScreen", new GameScreen(musicSystem, soundSystem, engine, assetManager, spriteBatch, gameCamera));
		allGameScreens.put("GameOverScreen", new GameOverScreen());
		allGameScreens.put("PauseScreen", new PauseScreen());
		this.setScreen(allGameScreens.get("GameScreen"));
	}
	
	@Override
	public void resume() {
		// Resume all audio
		musicSystem.pauseCurrentSong();
	}
	
	@Override
	public void pause() {
		// Pause audio
		musicSystem.pauseCurrentSong();
	}

	@Override
	public void render() {
		// DEBUG
		Gdx.graphics.setTitle(TITLE + " | Memory usage: " + (Gdx.app.getNativeHeap() / 1000000) + "mb" + " | FPS: "
				+ Gdx.graphics.getFramesPerSecond());
		// DEBUG
		
		// Clear Screen
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// Start Main Loop Cycle
		this.getScreen().render(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void dispose() {
		assetManager.dispose();
		spriteBatch.dispose();
		allGameScreens.forEach((key, screen) -> {
			screen.dispose();
		});
	}
	
}
