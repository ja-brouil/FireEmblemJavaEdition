package com.jb.fe.systems.gamePlay;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.utils.Array;
import com.jb.fe.components.Artifical_IntelligenceComponent;
import com.jb.fe.components.UnitStatsComponent;
import com.jb.fe.map.MapCell;
import com.jb.fe.systems.SystemPriorityDictionnary;

public class AISystem extends EntitySystem{

	// All Units
	private ImmutableArray<Entity> allGameEntities;
	private Array<Entity> allyUnits;
	private Array<Entity> enemyUnits;
	
	// Component Mappers 
	private ComponentMapper<UnitStatsComponent> uComponentMapper = ComponentMapper.getFor(UnitStatsComponent.class);
	private ComponentMapper<Artifical_IntelligenceComponent> aiComponentMapper = ComponentMapper.getFor(Artifical_IntelligenceComponent.class);
	
	// Entity to process
	private Entity enemyUnit;
	
	public AISystem() {
		// This system should only be started when the AI is needed.
		priority = SystemPriorityDictionnary.AI_System;
		setProcessing(false);
	}

	@Override
	public void addedToEngine(Engine engine) {
		engine.addEntityListener(Family.all(UnitStatsComponent.class).get(), new EntityListener() {
			
			@Override
			public void entityRemoved(Entity entity) {
				allGameEntities = engine.getEntitiesFor(Family.all(UnitStatsComponent.class).get());
				
				if(uComponentMapper.get(entity).isAlly) {
					allyUnits.removeValue(entity, true);
				} else {
					enemyUnits.removeValue(entity, true);
				}
			}
			
			@Override
			public void entityAdded(Entity entity) {
				allGameEntities = engine.getEntitiesFor(Family.all(UnitStatsComponent.class).get());
				
				if(uComponentMapper.get(entity).isAlly) {
					allyUnits.add(entity);
				} else {
					enemyUnits.add(entity);
				}
			}
		});
	}
	
	@Override
	public void update(float delta) {
		
	}
	
	// Movement algorithm
	private void findMovementTile() {
		UnitStatsComponent enemyStatsComponent = uComponentMapper.get(enemyUnit);
		MapCell currentCell = enemyStatsComponent.currentCell;
	}
	
	/*
	 * Sort entities based on alliance
	 */
	public void sortEntities(){
		// Clear Arrays
		allGameEntities = getEngine().getEntitiesFor(Family.all(UnitStatsComponent.class).get());
		allyUnits.clear();
		enemyUnits.clear();
		
		// Sort entities based on alliance
		for (Entity gameUnit : allGameEntities) {
			UnitStatsComponent unitStatsComponent = uComponentMapper.get(gameUnit);
			if (unitStatsComponent.isAlly) {
				allyUnits.add(gameUnit);
			} else {
				enemyUnits.add(gameUnit);
			}
		}
	}
	
	public Entity getEnemyEntity() {
		return enemyUnit;
	}
	
	public void setEnemyUnit(Entity enemyUnit) {
		this.enemyUnit = enemyUnit;
	}
}
