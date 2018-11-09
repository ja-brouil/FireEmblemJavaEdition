package com.jb.fe.level;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.jb.fe.map.MapCell;
import com.jb.fe.systems.audio.MusicSystem;
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

	// Units
	public UnitFactory unitFactory;

	// Victory/Defeat condition

	public Level(String mapFileLocation, AssetManager assetManager, Engine engine) {
		this.engine = engine;
		this.assetManager = assetManager;
		this.mapFileLocation = mapFileLocation;
	}
	
	public void startLevel() {
		// Map
		assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		assetManager.load(mapFileLocation, TiledMap.class);
		assetManager.finishLoading();
		levelMap = assetManager.get(mapFileLocation, TiledMap.class);

		// Unit Factory
		unitFactory = new UnitFactory(assetManager);
		
		// Allies
		engine.addEntity(unitFactory.createEirika("Eirika", "units/eirika/eirika copy.png", 2 * MapCell.CELL_SIZE,
				2 * MapCell.CELL_SIZE, true));
		engine.addEntity(unitFactory.createCavalierUnit("Seth", "units/cavalier/cavalierAlly copy.png", 2 * MapCell.CELL_SIZE, 3 * MapCell.CELL_SIZE, true));
		
		// Enemy
		engine.addEntity(unitFactory.createCavalierUnit("Evil Seth", "units/cavalier/cavalierAllyRed.png", 4 * MapCell.CELL_SIZE, 3 * MapCell.CELL_SIZE, false));
		Entity entity = unitFactory.createCavalierUnit("Evil Seth 2", "units/cavalier/cavalierAllyRed.png", 208, 48, false);
		engine.addEntity(entity);
		engine.addEntity(unitFactory.createCavalierUnit("Evil Seth", "units/cavalier/cavalierAllyRed.png", 11 * MapCell.CELL_SIZE, 3 * MapCell.CELL_SIZE, false));
		engine.addEntity(unitFactory.createCavalierUnit("Evil Seth", "units/cavalier/cavalierAllyRed.png", 12 * MapCell.CELL_SIZE, 3 * MapCell.CELL_SIZE, false));
		engine.addEntity(unitFactory.createCavalierUnit("Evil Seth", "units/cavalier/cavalierAllyRed.png", 13 * MapCell.CELL_SIZE, 3 * MapCell.CELL_SIZE, false));
		
		
		// Set Map Bounderies
		setMapBounderies();
		
		// Start MapCell Array
		allLevelMapCells = new MapCell[mapWidthLimit / MapCell.CELL_SIZE][mapHeightLimit / MapCell.CELL_SIZE];
	}
	
	public void setMusic(String songName) {
		MusicSystem musicSystem = engine.getSystem(MusicSystem.class);
		musicSystem.setCurrentSong(songName, true);
	}

	/*
	 * Property Keys:  width orientation hexside length tileheight tilewidth height
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
