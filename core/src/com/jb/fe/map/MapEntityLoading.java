package com.jb.fe.map;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.jb.fe.components.NameComponent;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.level.Level;
import com.jb.fe.units.UnitFactory;

public class MapEntityLoading {

	private AssetManager assetManager;
	private Engine engine;
	
	private Level currentLevel;
	
	private ComponentMapper<NameComponent> nComponentMapper = ComponentMapper.getFor(NameComponent.class);
	private ComponentMapper<PositionComponent> pComponentMapper = ComponentMapper.getFor(PositionComponent.class);
	
	public MapEntityLoading(Engine engine, AssetManager assetManager) {
		this.assetManager = assetManager;
		this.engine = engine;
	}
	
	public void loadMap(Level level) {
		this.currentLevel = level;
		
		// Load Enemies
		loadEnemies(level.levelMap.getLayers().get("Enemies").getObjects());
		
		// Load Allies
		loadAllies(level.levelMap.getLayers().get("Allies").getObjects());
	}
	
	private void loadEnemies(MapObjects enemyObjectLayer) {
		enemyObjectLayer.forEach((enemyObject) -> {
			MapProperties enemyObjectProp = enemyObject.getProperties();
			Entity enemy = createUnit(enemyObjectProp);
			engine.addEntity(enemy);
			currentLevel.allEnemies.add(enemy);
		});
	}
	
	
	private void loadAllies(MapObjects allyMapObjectLayer) {
		// Check if ally exists
		allyMapObjectLayer.forEach((allyObject) -> {
			MapProperties allyObjectProp = allyObject.getProperties();
			
			String nameObject = allyObjectProp.get("Name", String.class);
			for (int i = 0; i < currentLevel.allAllies.size; i++) {
				String nameInArray = nComponentMapper.get(currentLevel.allAllies.get(i)).name;
				if (nameObject.equals(nameInArray)){
					// set new position
					PositionComponent positionComponent = pComponentMapper.get(currentLevel.allAllies.get(i));
					positionComponent.x = allyObjectProp.get("x", Float.class);
					positionComponent.y = allyObjectProp.get("y", Float.class);
				} else {
					// Create Entity based on the stats // Only Cavalier
					Entity ally = createUnit(allyObjectProp);
					engine.addEntity(ally);
					currentLevel.allAllies.add(ally);
				}
			}
		});
	}
	
	// Unit Class Ceation
	private Entity createUnit(MapProperties unitProp) {
		Entity unit = null;
		String unitClass = unitProp.get("Class", String.class);
		if (unitClass.equals("Bandit")) {
			unit = UnitFactory.createBandit(assetManager, 
					"units/bandit/banditRed.png", engine, unitProp);
		} else if (unitClass.equals("Paladin")) {
			unit = UnitFactory.createCavalierUnit(assetManager, unitProp.get("Name", String.class), "units/cavalier/cavalierAlly copy.png", 
					unitProp.get("x", Float.class),unitProp.get("y", Float.class), true, engine);
		}
		return unit;
	}
}
