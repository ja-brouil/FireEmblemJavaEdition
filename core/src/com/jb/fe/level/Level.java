package com.jb.fe.level;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Array;
import com.jb.fe.map.MapCell;
import com.jb.fe.units.UnitFactory;

public class Level {

	// Engine
	public Engine engine;
	
	// Asset Manager
	public AssetManager assetManager;

	// Map
	public String mapFileLocation;
	public TiledMap levelMap;
	public MapCell[][] allLevelMapCells;
	
	// Map Bounderies
	public int mapWidthLimit;
	public int mapHeightLimit;

	// Victory/Defeat condition
	public VictoryCondition victoryCondition;
	
	// Enenies
	public Array<Entity> allEnemies;

	public Level(String mapFileLocation, AssetManager assetManager, Engine engine) {
		this.engine = engine;
		this.assetManager = assetManager;
		this.mapFileLocation = mapFileLocation;
		
		allEnemies = new Array<Entity>();
	}
	
	public void startLevel() {
		// Map
		assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		assetManager.load(mapFileLocation, TiledMap.class);
		assetManager.finishLoading();
		levelMap = assetManager.get(mapFileLocation, TiledMap.class);
		
		// Allies
		engine.addEntity(UnitFactory.createEirika(assetManager, "Eirika", "units/eirika/eirika copy.png", 160,
				48, true, engine));
		engine.addEntity(UnitFactory.createCavalierUnit(assetManager, "Seth", "units/cavalier/cavalierAlly copy.png", 2 * MapCell.CELL_SIZE, 3 * MapCell.CELL_SIZE, true, engine));
		
		// Enemy
		/*
		engine.addEntity(UnitFactory.createCavalierUnit(assetManager,"Evil Seth", "units/cavalier/cavalierAllyRed.png", 11 * MapCell.CELL_SIZE, 3 * MapCell.CELL_SIZE, false, engine));
		Entity aggresiveAI = UnitFactory.createCavalierUnit(assetManager, "Evil Seth", "units/cavalier/cavalierAllyRed.png", 5 * MapCell.CELL_SIZE, 9 * MapCell.CELL_SIZE, false, engine);
		aggresiveAI.getComponent(Artifical_IntelligenceComponent.class).ai_Type = AI_TYPE.AGGRESSIVE;
		aggresiveAI.getComponent(UnitStatsComponent.class).setKnight();
		aggresiveAI.getComponent(NameComponent.class).name = "Test";
		engine.addEntity(aggresiveAI);
		*/
		
		
		// Set Map Bounderies
		setMapBounderies();
		
		// Start MapCell Array
		allLevelMapCells = new MapCell[mapWidthLimit / MapCell.CELL_SIZE][mapHeightLimit / MapCell.CELL_SIZE];
		
		// Start Victory Condition
		victoryCondition = new VictoryCondition();
	}

	/*
	 * Property Keys:  width orientation hexside length tileheight tilewidth height
	 */
	public void setMapBounderies() {
		mapWidthLimit = levelMap.getProperties().get("width", Integer.class) * MapCell.CELL_SIZE;
		mapHeightLimit = levelMap.getProperties().get("height", Integer.class) * MapCell.CELL_SIZE;
	}
}
