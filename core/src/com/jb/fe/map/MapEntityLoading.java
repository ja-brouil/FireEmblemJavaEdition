package com.jb.fe.map;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.MapObject;
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
		setAllies(level.levelMap.getLayers().get("Allies").getObjects());
	}
	
	private void loadEnemies(MapObjects enemyObjectLayer) {
		enemyObjectLayer.forEach((enemyObject) -> {
			MapProperties enemyObjectProp = enemyObject.getProperties();
			Entity enemy = createUnit(enemyObjectProp);
			engine.addEntity(enemy);
			currentLevel.allEnemies.add(enemy);
		});
	}
	
	
	private void setAllies(MapObjects allyMapObjectLayer) {
		for (MapObject allyObject : allyMapObjectLayer) {
			MapProperties allyObjectProp = allyObject.getProperties();
			String allyObjectNameString = allyObjectProp.get("Name", String.class);
			
			// Create all objects that aren't in the array list
			for (int i = 0; i < currentLevel.allAllies.size; i++) {
				String allyArrayListName = nComponentMapper.get(currentLevel.allAllies.get(i)).name;
				if (allyArrayListName.equalsIgnoreCase(allyObjectNameString)) {
					PositionComponent positionComponent = pComponentMapper.get(currentLevel.allAllies.get(i));
					positionComponent.x = allyObjectProp.get("x", Float.class);
					positionComponent.y = allyObjectProp.get("y", Float.class);
					allyMapObjectLayer.remove(allyObject);
				}
			}
		}
		
		for (MapObject allyObject : allyMapObjectLayer) {
			MapProperties allyObjectProp = allyObject.getProperties();
			Entity newAlly = createUnit(allyObjectProp);
			engine.addEntity(newAlly);
			currentLevel.allAllies.add(newAlly);
		}
	}
	
	/**
	 * Calls this only on the first level to create the allies that don't exist.
	 */
	public void loadFirstAllies() {
		
	}
	
	// Unit Class Ceation
	private Entity createUnit(MapProperties unitProp) {
		Entity unit = null;
		String unitClass = unitProp.get("Class", String.class);
		if (unitClass.equals("Bandit")) {
			unit = UnitFactory.createBandit(assetManager, 
					"units/bandit/banditRed.png", engine, unitProp);
		} else if (unitClass.equals("Paladin")) {
			unit = UnitFactory.createCavalierUnit(assetManager, "units/cavalier/cavalierAlly copy.png", engine, unitProp);
		}
		return unit;
	}
}
