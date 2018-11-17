package com.jb.fe.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jb.fe.UI.UIFactory;
import com.jb.fe.level.Level;
import com.jb.fe.map.MapCellInfoSystem;
import com.jb.fe.map.MapRenderSystem;
import com.jb.fe.systems.audio.MusicSystem;
import com.jb.fe.systems.audio.SoundSystem;
import com.jb.fe.systems.gamePlay.AISystem;
import com.jb.fe.systems.gamePlay.TurnManager;
import com.jb.fe.systems.graphics.RenderSystem;
import com.jb.fe.systems.input.UIManager;
import com.jb.fe.systems.movement.UnitMapCellUpdater;
import com.jb.fe.systems.movement.UnitMovementSystem;

public class GameScreen extends ScreenAdapter{
	
	// Engine
	private Engine engine;
	
	// Level
	private Level currentLevel;
	
	// User Interface
	private UIFactory mapCursorFactory;
	
	// Systems Required
	private MapRenderSystem mapRenderSystem;
	private MapCellInfoSystem mapCellInfoSystem;
	private RenderSystem animationSystem;
	private UnitMapCellUpdater unitMapCellUpdater;
	private UnitMovementSystem unitMovementSystem;
	private TurnManager turnManagerSystem;
	private AISystem aiSystem;
	private UIManager uiManager;
	
	public GameScreen(MusicSystem musicSystem, SoundSystem soundSystem, Engine engine, AssetManager assetManager, SpriteBatch spriteBatch, OrthographicCamera gameCamera) {
		this.engine = engine;
		
		// Start Systems
		mapRenderSystem = new MapRenderSystem(gameCamera, spriteBatch);
		mapCellInfoSystem = new MapCellInfoSystem(assetManager, engine);
		animationSystem = new RenderSystem(spriteBatch);
		unitMapCellUpdater = new UnitMapCellUpdater();
		unitMovementSystem = new UnitMovementSystem();
		turnManagerSystem = new TurnManager();
		aiSystem = new AISystem();
		uiManager = new UIManager();
		
		// Add Systems to the Engine
		engine.addSystem(mapRenderSystem);
		engine.addSystem(animationSystem);
		engine.addSystem(unitMapCellUpdater);
		engine.addSystem(unitMovementSystem);
		engine.addSystem(soundSystem);
		engine.addSystem(musicSystem);
		engine.addSystem(turnManagerSystem);
		engine.addSystem(aiSystem);
		engine.addSystem(uiManager);
	
		// Start First Level
		currentLevel = new Level("levels/level1/level1.tmx", assetManager, engine);
		currentLevel.startLevel();
		
		// User Interface
		mapCursorFactory = new UIFactory(assetManager, soundSystem, gameCamera);
		engine.addEntity(mapCursorFactory.createMapCursor(currentLevel, engine));
		
		// Set Audio
		musicSystem.addNewSong("Ally Battle Theme", "music/FE Level1 HD Good.mp3", assetManager);
		musicSystem.addNewSong("Ally Battle Theme SD", "music/FE Level1 SD.mp3", assetManager);
		musicSystem.addNewSong("Enemy Phase", "music/enemy theme.mp3", assetManager);
		
		// Set Level
		setNewMap(currentLevel);
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
		uiManager.startSystem(level);
		unitMapCellUpdater.startSystem(level);
		unitMovementSystem.startSystem();
		turnManagerSystem.startSystem(level.assetManager);
		unitMapCellUpdater.updateCellInfo();
	}
}
