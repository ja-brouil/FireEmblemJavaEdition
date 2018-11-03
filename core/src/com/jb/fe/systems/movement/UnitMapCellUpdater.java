package com.jb.fe.systems.movement;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.utils.Array;
import com.jb.fe.components.AnimationComponent;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.components.UnitStatsComponent;
import com.jb.fe.level.Level;
import com.jb.fe.map.MapCell;
import com.jb.fe.systems.SystemPriorityDictionnary;

public class UnitMapCellUpdater extends EntitySystem{
	
	// All Game Entities 
	private ImmutableArray<Entity> allGameEntities;
	
	// Map Cells
	private Array<MapCell> allMapCells;
	
	// Unit Stats
	private ComponentMapper<PositionComponent> pComponentMapper = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<UnitStatsComponent> unitStatsMapper = ComponentMapper.getFor(UnitStatsComponent.class);
	
	public UnitMapCellUpdater() {
		// This should only be called when needed
		priority = SystemPriorityDictionnary.UnitUpdate;
		setProcessing(false);
	}
	
	// Listeners
	@Override
	public void addedToEngine(Engine engine) {
		allGameEntities =  engine.getEntitiesFor(Family.all(PositionComponent.class, UnitStatsComponent.class, AnimationComponent.class).get());
		
		engine.addEntityListener(Family.all(PositionComponent.class, UnitStatsComponent.class, AnimationComponent.class).get(), new EntityListener() {
			
			@Override
			public void entityRemoved(Entity entity) {
				allGameEntities =  engine.getEntitiesFor(Family.all(PositionComponent.class, UnitStatsComponent.class, AnimationComponent.class).get());
			}
			
			@Override
			public void entityAdded(Entity entity) {
				allGameEntities =  engine.getEntitiesFor(Family.all(PositionComponent.class, UnitStatsComponent.class, AnimationComponent.class).get());
			}
		});
	}
	
	// Update Cell and Units
	public void updateCellInfo() {
		// Reset Cell Stats
		for (MapCell mapCell : allMapCells) {
			mapCell.isOccupied = false;
			mapCell.occupyingUnit = null;
		}
		
		// Update Units + Reset Moving Variable
		for (Entity unit : allGameEntities) {
			PositionComponent unitPositionComponent = pComponentMapper.get(unit);
			for (int i = 0; i < allMapCells.size; i++) {
				if (allMapCells.get(i).position.x == unitPositionComponent.x && allMapCells.get(i).position.y == unitPositionComponent.y) {
					UnitStatsComponent unitStatsComponent = unitStatsMapper.get(unit);
					unitStatsComponent.currentCell = allMapCells.get(i);
					unitStatsComponent.isMoving = false;
					allMapCells.get(i).isOccupied = true;
					allMapCells.get(i).occupyingUnit = unit;
					break;
				}
			}
			
		}
	}
	
	// Get all Game Units
	public void getAllGameUnits() {
		allGameEntities = getEngine().getEntitiesFor(Family.all(PositionComponent.class, UnitStatsComponent.class, AnimationComponent.class).get());	
	}
	
	// Set Tiles
	public void setMapCells(Array<MapCell> allMapCells) {
		this.allMapCells = allMapCells;
	}
	
	
	public void startSystem(Level level) {
		allMapCells = level.allLevelMapCells;
	}
}
