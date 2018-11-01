package com.jb.fe.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jb.fe.level.Level;
import com.jb.fe.map.MapCellInfoSystem;
import com.jb.fe.map.MapRenderSystem;
import com.jb.fe.systems.audio.MusicSystem;
import com.jb.fe.systems.audio.SoundSystem;
import com.jb.fe.systems.graphics.AnimationSystem;
import com.jb.fe.systems.graphics.StaticImageSystem;
import com.jb.fe.systems.input.InputHandlingSystem;
import com.jb.fe.systems.input.MapCursorInfoUpdateSystem;
import com.jb.fe.systems.input.MapCursorOutOfBoundsSystem;

public class GameScreen extends ScreenAdapter{
	
	// Audio
	private MusicSystem musicSystem;
	private SoundSystem soundSystem;
	
	// Engine
	private Engine engine;
	
	// Level
	private Level currentLevel;
	
	// Systems Required
	private MapRenderSystem mapRenderSystem;
	private MapCellInfoSystem mapCellInfoSystem;
	private AnimationSystem animationSystem;
	private StaticImageSystem staticImageSystem;
	private InputHandlingSystem inputHandlingSystem;
	private MapCursorOutOfBoundsSystem mapCursorOutOfBoundsSystem;
	private MapCursorInfoUpdateSystem mapCursorInfoUpdateSystem;
	
	public GameScreen(MusicSystem musicSystem, SoundSystem soundSystem, Engine engine, AssetManager assetManager, SpriteBatch spriteBatch, OrthographicCamera gameCamera) {
		this.soundSystem = soundSystem;
		this.musicSystem = musicSystem;
		this.engine = engine;
		
		// Start Systems
		mapRenderSystem = new MapRenderSystem(gameCamera, spriteBatch);
		mapCellInfoSystem = new MapCellInfoSystem(assetManager, engine);
		animationSystem = new AnimationSystem(spriteBatch);
		staticImageSystem = new StaticImageSystem(spriteBatch);
		inputHandlingSystem = new InputHandlingSystem();
		mapCursorOutOfBoundsSystem = new MapCursorOutOfBoundsSystem();
		mapCursorInfoUpdateSystem = new MapCursorInfoUpdateSystem();
		
		// Add Systems to the Engine
		engine.addSystem(staticImageSystem);
		engine.addSystem(mapRenderSystem);
		engine.addSystem(animationSystem);
		engine.addSystem(inputHandlingSystem);
		engine.addSystem(mapCursorOutOfBoundsSystem);
		engine.addSystem(mapCursorInfoUpdateSystem);
	
		// Start First Level
		currentLevel = new Level("levels/level1/level1.tmx", assetManager, engine);
		
		// Set Level
		setNewMap(currentLevel);
	}
	
	@Override
	public void show() {

	}
	
	@Override
	public void render(float delta) {
		// Start spritebatch
		engine.update(delta);
	}
	
	/**
	 * Change to the next level
	 * @param level
	 */
	public void setNewMap(Level level) {
		mapRenderSystem.setCurrentLevel(level);
		mapCellInfoSystem.processTiles(level);
		mapCursorOutOfBoundsSystem.startSystem(level);
		mapCursorInfoUpdateSystem.startSystem(level);
	}
}
