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
	
	// Allies
	public Array<Entity> allAllies;

	public Level(String mapFileLocation, AssetManager assetManager, Engine engine) {
		this.engine = engine;
		this.assetManager = assetManager;
		this.mapFileLocation = mapFileLocation;
		
		allEnemies = new Array<>();
		allAllies = new Array<>();
	}
	
	public void startLevel() {
		// Map
		assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		assetManager.load(mapFileLocation, TiledMap.class);
		assetManager.finishLoading();
		levelMap = assetManager.get(mapFileLocation, TiledMap.class);
		
		// Allies
		Entity eirika = UnitFactory.createEirika(assetManager, "Eirika", "units/eirika/eirika copy.png", 0,
				0, true, engine);
		engine.addEntity(eirika);
		allAllies.add(eirika);
		
		// Set Map Bounderies
		setMapBounderies();
		
		// Start MapCell Array
		allLevelMapCells = new MapCell[mapWidthLimit][mapHeightLimit];
		
		// Start Victory Condition
		victoryCondition = new VictoryCondition();
	}

	/*
	 * Property Keys:  width orientation hexsidelength tileheight tilewidth height
	 */
	public void setMapBounderies() {
		mapWidthLimit = levelMap.getProperties().get("width", Integer.class);
		mapHeightLimit = levelMap.getProperties().get("height", Integer.class);
	}
}
