package com.jb.fe.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jb.fe.input.MapCursorFactory;
import com.jb.fe.level.Level;
import com.jb.fe.map.MapCellInfoSystem;
import com.jb.fe.map.MapRenderSystem;
import com.jb.fe.systems.audio.MusicSystem;
import com.jb.fe.systems.audio.SoundSystem;
import com.jb.fe.systems.gamePlay.TurnManager;
import com.jb.fe.systems.graphics.RenderSystem;
import com.jb.fe.systems.input.InputHandlingSystem;
import com.jb.fe.systems.input.MapCursorInfoUpdateSystem;
import com.jb.fe.systems.input.MapCursorOutOfBoundsSystem;
import com.jb.fe.systems.movement.UnitMapCellUpdater;
import com.jb.fe.systems.movement.UnitMovementSystem;

public class GameScreen extends ScreenAdapter{
	
	// Engine
	private Engine engine;
	
	// Level
	private Level currentLevel;
	
	// User Interface
	private MapCursorFactory mapCursorFactory;
	
	// Systems Required
	private MapRenderSystem mapRenderSystem;
	private MapCellInfoSystem mapCellInfoSystem;
	private RenderSystem animationSystem;
	private InputHandlingSystem inputHandlingSystem;
	private MapCursorOutOfBoundsSystem mapCursorOutOfBoundsSystem;
	private MapCursorInfoUpdateSystem mapCursorInfoUpdateSystem;
	private UnitMapCellUpdater unitMapCellUpdater;
	private UnitMovementSystem unitMovementSystem;
	private TurnManager turnManagerSystem;
	
	public GameScreen(MusicSystem musicSystem, SoundSystem soundSystem, Engine engine, AssetManager assetManager, SpriteBatch spriteBatch, OrthographicCamera gameCamera) {
		this.engine = engine;
		
		// Start Systems
		mapRenderSystem = new MapRenderSystem(gameCamera, spriteBatch);
		mapCellInfoSystem = new MapCellInfoSystem(assetManager, engine);
		animationSystem = new RenderSystem(spriteBatch);
		inputHandlingSystem = new InputHandlingSystem();
		mapCursorOutOfBoundsSystem = new MapCursorOutOfBoundsSystem();
		mapCursorInfoUpdateSystem = new MapCursorInfoUpdateSystem();
		unitMapCellUpdater = new UnitMapCellUpdater();
		unitMovementSystem = new UnitMovementSystem();
		turnManagerSystem = new TurnManager();
		
		// Add Systems to the Engine
		engine.addSystem(mapRenderSystem);
		engine.addSystem(animationSystem);
		engine.addSystem(inputHandlingSystem);
		engine.addSystem(mapCursorOutOfBoundsSystem);
		engine.addSystem(mapCursorInfoUpdateSystem);
		engine.addSystem(unitMapCellUpdater);
		engine.addSystem(unitMovementSystem);
		engine.addSystem(soundSystem);
		engine.addSystem(musicSystem);
		engine.addSystem(turnManagerSystem);
	
		// Start First Level
		currentLevel = new Level("levels/level1/level1.tmx", assetManager, engine);
		currentLevel.startLevel();
		
		// User Interface
		mapCursorFactory = new MapCursorFactory(assetManager, soundSystem);
		engine.addEntity(mapCursorFactory.createMapCursor());
		
		// Set First Audio
		musicSystem.addNewSong("Ally Battle Theme", "music/FE Level1 HD Good.mp3", assetManager);
		musicSystem.addNewSong("Ally Battle Theme SD", "music/FE Level1 SD.mp3", assetManager);
		currentLevel.setMusic("Ally Battle Theme SD");
		
		// Set Level
		setNewMap(currentLevel);
	}
	
	@Override
	public void show() {

	}
	
	@Override
	public void render(float delta) {
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
		unitMapCellUpdater.startSystem(level);
		unitMovementSystem.startSystem();
	}
}
