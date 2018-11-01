package com.jb.fe.systems.movement;

import java.util.HashSet;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.components.StaticImageComponent;
import com.jb.fe.components.UnitStatsComponent;
import com.jb.fe.level.Level;
import com.jb.fe.map.MapCell;

/*
 * Algorithm for calculating Movement System
 */

public class MovementUtilityCalculator {
	
	// Pathfinding arrays
	public HashSet<MapCell> allPossibleMoves;
	public Queue<MapCell> pathfindingQueue;
	
	// All Map Cells
	public Array<MapCell> allMapCells;
	
	// Component Mapper
	public ComponentMapper<PositionComponent> pComponentMapper = ComponentMapper.getFor(PositionComponent.class);
	public ComponentMapper<UnitStatsComponent> uComponentMapper = ComponentMapper.getFor(UnitStatsComponent.class);
	public ComponentMapper<StaticImageComponent> sComponentMapper = ComponentMapper.getFor(StaticImageComponent.class);
	
	public MovementUtilityCalculator(Level level) {
		allPossibleMoves = new HashSet<>();
		pathfindingQueue = new Queue<>();
		
		allMapCells = level.allLevelMapCells;
	}
	
	// Get All possible Moves
	public void calculateAllPossibleMoves(Entity unit) {
		// Reset Queue
		allPossibleMoves.clear();
		pathfindingQueue.clear();
		
		// Stats on unit
		UnitStatsComponent unitStatsComponent = uComponentMapper.get(unit);
		
		// Reset Stats on MapCell
		for (MapCell mapCell : allMapCells) {
			mapCell.isVisited = false;
			mapCell.distanceFromParent = 0;
			mapCell.parentTile = null;
		}
		
		// Remove Colors if not reset
		disableAllColors();
		
		// Get Initial tile and add it to the closed list
		processTile(getMapCell(unit), unitStatsComponent, unitStatsComponent.movementSteps, unit);
	}
	
	private void processTile(MapCell initialTile, UnitStatsComponent unitStatsComponent, int moveSteps, Entity unit) {
		// Add initial tile
		allPossibleMoves.add(initialTile);
		
		// Process all Adj tiles
		for (int i = 0; i < initialTile.adjTiles.size; i++) {
			
			MapCell adjMapCell = initialTile.adjTiles.get(i);
			
			// Next Move Cost
			int nextMoveCost = moveSteps - adjMapCell.movementCost;
			
			if (nextMoveCost >= 0) {
				// Allow ally passage
				if (unitStatsComponent.isAlly) {
					if (adjMapCell.isOccupied && !adjMapCell.occupyingUnit.getComponent(UnitStatsComponent.class).isAlly) {
						continue;
					} else {
						processTile(adjMapCell, unitStatsComponent, nextMoveCost, unit);
					}
				} else {
					if (adjMapCell.isOccupied && adjMapCell.occupyingUnit.getComponent(UnitStatsComponent.class).isAlly) {
						continue;
					} else {
						processTile(adjMapCell, unitStatsComponent, nextMoveCost, unit);
					}
				}
			}
		}
		
		// Enabled Blue Squares
		enableBlueSquares();
	}
	
	// Get Tile from All Tiles
	public MapCell getMapCell(Entity unit) {
		PositionComponent unitPositionComponent = pComponentMapper.get(unit);
		for (MapCell mapCell : allMapCells) {
			if (mapCell.position.x == unitPositionComponent.x && mapCell.position.y == unitPositionComponent.y) {
				return mapCell;
			}
		}
		
		return null;
	}
	
	// Enable Blue squares
	private void enableBlueSquares() {
		for (MapCell mapCell : allPossibleMoves) {
			StaticImageComponent staticImageComponent = sComponentMapper.get(mapCell.blueSquare);
			staticImageComponent.isEnabled = true;
		}
	}
	
	// Disabled all colors on squares
	public void disableAllColors() {
		for (MapCell mapCell : allMapCells) {
			StaticImageComponent blueStaticImageComponent = sComponentMapper.get(mapCell.blueSquare);
			blueStaticImageComponent.isEnabled = false;
			StaticImageComponent redStaticImageComponent = sComponentMapper.get(mapCell.blueSquare);
			redStaticImageComponent.isEnabled = false;
		}
	}
}
