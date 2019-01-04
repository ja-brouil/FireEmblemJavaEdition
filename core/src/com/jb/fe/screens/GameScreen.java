package com.jb.fe.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jb.fe.UI.infoBoxes.TerrainInfoBox;
import com.jb.fe.UI.infoBoxes.UnitInfoBox;
import com.jb.fe.UI.infoBoxes.VictoryInfoBox;
import com.jb.fe.UI.mapcursor.MapCursor;
import com.jb.fe.UI.mapcursor.MovementSelection;
import com.jb.fe.level.Level;
import com.jb.fe.map.MapCellInfoSystem;
import com.jb.fe.map.MapEntityLoading;
import com.jb.fe.map.MapRenderSystem;
import com.jb.fe.systems.audio.MusicSystem;
import com.jb.fe.systems.audio.SoundSystem;
import com.jb.fe.systems.gamePlay.AISystem;
import com.jb.fe.systems.gamePlay.CombatSystem;
import com.jb.fe.systems.gamePlay.TurnManager;
import com.jb.fe.systems.inputAndUI.InfoBoxUpdate;
import com.jb.fe.systems.inputAndUI.UserInterfaceManager;
import com.jb.fe.systems.movement.MovementUtilityCalculator;
import com.jb.fe.systems.movement.UnitMapCellUpdater;
import com.jb.fe.systems.movement.UnitMovementSystem;

public class GameScreen extends ScreenAdapter{

	private Engine engine;
	private AssetManager assetManager;
	private OrthographicCamera gameCamera;
	private SoundSystem soundSystem;
	
	// Level
	private Level currentLevel;
	
	// Map Loading
	private MapRenderSystem mapRenderSystem;
	private MapCellInfoSystem mapCellInfoSystem;
	private MapEntityLoading mapEntityLoading;
	
	// Systems Required
	private UnitMapCellUpdater unitMapCellUpdater;
	private UnitMovementSystem unitMovementSystem;
	private TurnManager turnManagerSystem;
	private AISystem aiSystem;
	private InfoBoxUpdate infoBoxUpdate;
	private UserInterfaceManager userInterfaceManager;
	private CombatSystem combatSystem;
	private MovementUtilityCalculator movementUtilityCalculator;
	
	public GameScreen(MusicSystem musicSystem, SoundSystem soundSystem, Engine engine, AssetManager assetManager, SpriteBatch spriteBatch, OrthographicCamera gameCamera) {
		this.engine = engine;
		this.soundSystem = soundSystem;
		this.assetManager = assetManager;
		this.gameCamera = gameCamera;
		
		// Start Systems
		mapRenderSystem = new MapRenderSystem(gameCamera, spriteBatch);
		mapCellInfoSystem = new MapCellInfoSystem(assetManager, engine);
		mapEntityLoading = new MapEntityLoading(engine, assetManager);
		unitMapCellUpdater = new UnitMapCellUpdater();
		unitMovementSystem = new UnitMovementSystem();
		turnManagerSystem = new TurnManager();
		aiSystem = new AISystem();
		infoBoxUpdate = new InfoBoxUpdate();
		userInterfaceManager = new UserInterfaceManager();
		combatSystem = new CombatSystem();
		
		// Add Systems to the Engine
		engine.addSystem(mapRenderSystem);
		engine.addSystem(unitMapCellUpdater);
		engine.addSystem(unitMovementSystem);
		engine.addSystem(soundSystem);
		engine.addSystem(musicSystem);
		engine.addSystem(turnManagerSystem);
		engine.addSystem(aiSystem);
		engine.addSystem(userInterfaceManager);
		engine.addSystem(combatSystem);
	
		// Start First Level
		currentLevel = new Level("levels/level1/level1.tmx", assetManager, engine);
		currentLevel.startLevel();
		
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
		mapEntityLoading.loadMap(level);
		unitMapCellUpdater.startSystem(level);
		unitMovementSystem.startSystem();
		turnManagerSystem.startSystem(level.assetManager);
		unitMapCellUpdater.updateCellInfo();
		combatSystem.loadLevel(currentLevel);
		movementUtilityCalculator = new MovementUtilityCalculator(level, unitMapCellUpdater);
		aiSystem.setMovementCalculator(movementUtilityCalculator);
		
		createUserInterface();
	}
	
	private void createUserInterface() {
		// User Interface Creation
		MapCursor mapCursor = new MapCursor(assetManager, currentLevel, gameCamera, engine, soundSystem, userInterfaceManager, infoBoxUpdate);
		MovementSelection movementSelection = new MovementSelection(assetManager, soundSystem, userInterfaceManager, mapCursor.getMapCursorEntity(), currentLevel, unitMapCellUpdater, movementUtilityCalculator);
		
		// Add everything
		userInterfaceManager.allUserInterfaceStates.put("MapCursor", mapCursor);
		userInterfaceManager.allUserInterfaceStates.put("MovementSelection", movementSelection);
		
		// Info Boxes
		TerrainInfoBox terrainInfoBox = new TerrainInfoBox(mapCursor, assetManager, engine);
		VictoryInfoBox victoryInfoBox = new VictoryInfoBox(mapCursor, assetManager, engine, currentLevel);
		UnitInfoBox unitInfoBox = new UnitInfoBox(mapCursor, assetManager, engine);
		infoBoxUpdate.getAllBattleFieldMenuBoxes().add(unitInfoBox);
		infoBoxUpdate.getAllBattleFieldMenuBoxes().add(terrainInfoBox);
		infoBoxUpdate.getAllBattleFieldMenuBoxes().add(victoryInfoBox);
		
		userInterfaceManager.startSystem();
	}
}
