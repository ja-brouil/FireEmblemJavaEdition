package com.jb.fe.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jb.fe.UI.UIFactory;
import com.jb.fe.UI.infoBoxes.TerrainInfoBox;
import com.jb.fe.level.Level;
import com.jb.fe.map.MapCellInfoSystem;
import com.jb.fe.map.MapRenderSystem;
import com.jb.fe.systems.audio.MusicSystem;
import com.jb.fe.systems.audio.SoundSystem;
import com.jb.fe.systems.gamePlay.AISystem;
import com.jb.fe.systems.gamePlay.TurnManager;
import com.jb.fe.systems.inputAndUI.InfoBoxUpdate;
import com.jb.fe.systems.inputAndUI.ActionMenuMapCursorManager;
import com.jb.fe.systems.movement.UnitMapCellUpdater;
import com.jb.fe.systems.movement.UnitMovementSystem;

public class GameScreen extends ScreenAdapter{
	
	// Engine
	private Engine engine;
	
	// Level
	private Level currentLevel;
	
	// User Interface
	private UIFactory UIFactory;
	
	// Systems Required
	private MapRenderSystem mapRenderSystem;
	private MapCellInfoSystem mapCellInfoSystem;
	private UnitMapCellUpdater unitMapCellUpdater;
	private UnitMovementSystem unitMovementSystem;
	private TurnManager turnManagerSystem;
	private AISystem aiSystem;
	private ActionMenuMapCursorManager uiManager;
	private InfoBoxUpdate infoBoxUpdate;
	
	public GameScreen(MusicSystem musicSystem, SoundSystem soundSystem, Engine engine, AssetManager assetManager, SpriteBatch spriteBatch, OrthographicCamera gameCamera) {
		this.engine = engine;
		
		// Start Systems
		mapRenderSystem = new MapRenderSystem(gameCamera, spriteBatch);
		mapCellInfoSystem = new MapCellInfoSystem(assetManager, engine);
		unitMapCellUpdater = new UnitMapCellUpdater();
		unitMovementSystem = new UnitMovementSystem();
		turnManagerSystem = new TurnManager();
		aiSystem = new AISystem();
		uiManager = new ActionMenuMapCursorManager();
		infoBoxUpdate = new InfoBoxUpdate();
		
		// Add Systems to the Engine
		engine.addSystem(mapRenderSystem);
		engine.addSystem(unitMapCellUpdater);
		engine.addSystem(unitMovementSystem);
		engine.addSystem(soundSystem);
		engine.addSystem(musicSystem);
		engine.addSystem(turnManagerSystem);
		engine.addSystem(aiSystem);
		engine.addSystem(uiManager);
		engine.addSystem(infoBoxUpdate);
	
		// Start First Level
		currentLevel = new Level("levels/level1/level1.tmx", assetManager, engine);
		currentLevel.startLevel();
		
		// User Interface
		UIFactory = new UIFactory(assetManager, soundSystem, gameCamera, uiManager);
		Entity mapCursor = UIFactory.createMapCursor(currentLevel, engine);
		Entity actionMenu = UIFactory.createActionMenu(unitMovementSystem, unitMapCellUpdater, engine);
		
		uiManager.startSystem();
		engine.addEntity(mapCursor);
		engine.addEntity(actionMenu);
		
		// Info Boxes
		TerrainInfoBox terrainInfoBox = new TerrainInfoBox(mapCursor, assetManager, engine);
		infoBoxUpdate.getAllBattleFieldMenuBoxes().add(terrainInfoBox);
		
		// Set Audio
		musicSystem.addNewSong("Ally Battle Theme", "music/FE Level1 HD Good.mp3", assetManager);
		musicSystem.addNewSong("Ally Battle Theme SD", "music/FE Level1 SD.mp3", assetManager);
		musicSystem.addNewSong("Enemy Phase", "music/enemy theme.mp3", assetManager);
		
		// Set Level
		setNewMap(currentLevel, mapCursor);
	}
	
	@Override
	public void render(float delta) {
		engine.update(delta);
	}
	
	/**
	 * Change to the next level
	 * @param level
	 */
	public void setNewMap(Level level, Entity mapCursor) {
		mapRenderSystem.setCurrentLevel(level);
		mapCellInfoSystem.processTiles(level);
		unitMapCellUpdater.startSystem(level);
		unitMovementSystem.startSystem(uiManager);
		turnManagerSystem.startSystem(level.assetManager, uiManager);
		unitMapCellUpdater.updateCellInfo();
		infoBoxUpdate.setMapCursor(mapCursor);
	}
}
