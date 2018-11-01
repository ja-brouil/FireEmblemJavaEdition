package com.jb.fe.level;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Array;
import com.jb.fe.input.MapCursorFactory;
import com.jb.fe.map.MapCell;
import com.jb.fe.units.PlayerUnitFactory;

public class Level {
	
	// Engine
	private Engine engine;

	// Map
	public TiledMap levelMap;
	public Array<MapCell> allLevelMapCells;
	
	// Map Bounderies
	public int mapWidthLimit;
	public int mapHeightLimit;
	
	// Units
	public PlayerUnitFactory playerUnitFactory;
	
	// User Interface
	public MapCursorFactory mapCursorFactory;
	
	// Victory/Defeat condition
	
	public Level(String mapFileLocation, AssetManager assetManager, Engine engine) {
		this.engine = engine;
		// Map
		assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		assetManager.load(mapFileLocation, TiledMap.class);
		assetManager.finishLoading();
		levelMap = assetManager.get(mapFileLocation, TiledMap.class);
		
		// Array
		allLevelMapCells = new Array<MapCell>();
		
		// Unit Factory
		playerUnitFactory = new PlayerUnitFactory(assetManager);
		engine.addEntity(playerUnitFactory.createEirika("Eirika", "units/eirika/eirika copy.png", 2 * MapCell.CELL_SIZE , 2 * MapCell.CELL_SIZE));
		
		// Map cursor
		mapCursorFactory = new MapCursorFactory(assetManager);
		engine.addEntity(mapCursorFactory.createMapCursor());
		
		// Set Map Bounderies
		setMapBounderies();
	}
	
	/*
	 * Property Keys
	 * width
		orientation
		hexsidelength
		tileheight
		tilewidth
		height
	 */
	public void setMapBounderies() {
		mapWidthLimit = levelMap.getProperties().get("width", Integer.class) * MapCell.CELL_SIZE;
		mapHeightLimit = levelMap.getProperties().get("height", Integer.class) * MapCell.CELL_SIZE;
	}
	
	public void addNewAlly() {
		// To do
		System.out.println(engine + " Added new ally to engine!");
	}
	
	public void addNewEnemy() {
		// to do 
		System.out.println(engine + " Added new enemy to engine!");
	}
}
