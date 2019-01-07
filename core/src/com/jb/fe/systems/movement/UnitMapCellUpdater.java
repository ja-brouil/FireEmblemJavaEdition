package com.jb.fe.systems.movement;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.jb.fe.components.AnimationComponent;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.components.MovementStatsComponent;
import com.jb.fe.level.Level;
import com.jb.fe.map.MapCell;

public class UnitMapCellUpdater extends EntitySystem{
	
	// All Game Entities 
	private ImmutableArray<Entity> allGameEntities;
	
	// Map Cells
	private Level level;
	private MapCell[][] allMapCells;
	
	// Unit Stats
	private ComponentMapper<PositionComponent> pComponentMapper = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<MovementStatsComponent> unitStatsMapper = ComponentMapper.getFor(MovementStatsComponent.class);
	
	public UnitMapCellUpdater() {
		// This should only be called when needed
		setProcessing(false);
	}
	
	// Listeners
	@Override
	public void addedToEngine(Engine engine) {
		allGameEntities =  engine.getEntitiesFor(Family.all(PositionComponent.class, MovementStatsComponent.class, AnimationComponent.class).get());
	}
	
	// Update Cell and Units
	public void updateCellInfo() {
		// Reset Cell Stats
		for (int outer = 0; outer < allMapCells.length; outer++) {
			for (int inner = 0; inner < allMapCells[outer].length; inner++) {
				allMapCells[outer][inner].isOccupied = false;
				allMapCells[outer][inner].occupyingUnit = null;
			}
		}
		
		// Update Units + Reset Moving Variable
		for (Entity unit : allGameEntities) {
			PositionComponent unitPositionComponent = pComponentMapper.get(unit);
			MovementStatsComponent unitStatsComponent = unitStatsMapper.get(unit);
			
			// Get Cell coordinates
			int x = (int) unitPositionComponent.x / MapCell.CELL_SIZE;
			int y = (int) unitPositionComponent.y / MapCell.CELL_SIZE;
			
			unitStatsComponent.currentCell = allMapCells[x][y];
			unitStatsComponent.isMoving = false;
			allMapCells[x][y].isOccupied = true;
			allMapCells[x][y].occupyingUnit = unit;	
		}
		
		level.allLevelMapCells = allMapCells;
	}
	
	// Get all Game Units
	public void getAllGameUnits() {
		allGameEntities = getEngine().getEntitiesFor(Family.all(PositionComponent.class, MovementStatsComponent.class, AnimationComponent.class).get());	
	}
	
	// Set Tiles
	public void setMapCells(MapCell[][] allMapCells) {
		this.allMapCells = allMapCells;
	}
	
	
	public void startSystem(Level level) {
		this.level = level;
		allMapCells = level.allLevelMapCells;
		getEngine().addEntityListener(Family.all(PositionComponent.class, MovementStatsComponent.class, AnimationComponent.class).get(), new EntityListener() {
			
			@Override
			public void entityRemoved(Entity entity) {
				allGameEntities =  getEngine().getEntitiesFor(Family.all(PositionComponent.class, MovementStatsComponent.class, AnimationComponent.class).get());
				updateCellInfo();
			}
			
			@Override
			public void entityAdded(Entity entity) {
				allGameEntities =  getEngine().getEntitiesFor(Family.all(PositionComponent.class, MovementStatsComponent.class, AnimationComponent.class).get());
				updateCellInfo();
			}
		});
	}
}
