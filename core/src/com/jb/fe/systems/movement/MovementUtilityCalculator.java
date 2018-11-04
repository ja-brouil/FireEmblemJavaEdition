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
	public Array<MapCell> attackCells;
	public Queue<MapCell> pathfindingQueue;

	// All Map Cells
	public Array<MapCell> allMapCells;

	// Component Mapper
	public ComponentMapper<PositionComponent> pComponentMapper = ComponentMapper.getFor(PositionComponent.class);
	public ComponentMapper<UnitStatsComponent> uComponentMapper = ComponentMapper.getFor(UnitStatsComponent.class);
	public ComponentMapper<StaticImageComponent> sComponentMapper = ComponentMapper.getFor(StaticImageComponent.class);

	public MovementUtilityCalculator(Level level) {
		allPossibleMoves = new HashSet<>();
		attackCells = new Array<>();
		pathfindingQueue = new Queue<>();

		allMapCells = level.allLevelMapCells;
	}

	// Get All possible Moves
	public void calculateAllPossibleMoves(Entity unit) {
		// Stats on unit
		UnitStatsComponent unitStatsComponent = uComponentMapper.get(unit);

		// Reset Queue
		allPossibleMoves.clear();
		attackCells.clear();
		unitStatsComponent.allPossibleMoves.clear();
		unitStatsComponent.allOutsideAttackMoves.clear();
		
		// Reset Stats on MapCell
		for (MapCell mapCell : allMapCells) {
			mapCell.isVisited = false;
			mapCell.distanceFromParent = 0;
			mapCell.parentTile = null;
		}

		// Remove Colors if not reset
		resetMovementAlgorithms();

		// Process Movement and Attack Tiles
		processTile(getMapCell(unit), unitStatsComponent, unitStatsComponent.movementSteps, unit);
		processAttackTile(getMapCell(unit), unitStatsComponent);
		
		// Enabled Squares
		enableSquares();

		// Get Parent Tiles
		setParentTiles(getMapCell(unit));

		// Set Stats
		unitStatsComponent.allPossibleMoves = allPossibleMoves;
		unitStatsComponent.allOutsideAttackMoves = attackCells;
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
					if (adjMapCell.isOccupied
							&& !adjMapCell.occupyingUnit.getComponent(UnitStatsComponent.class).isAlly) {
						continue;
					} else {
						processTile(adjMapCell, unitStatsComponent, nextMoveCost, unit);
					}
				} else {
					if (adjMapCell.isOccupied
							&& adjMapCell.occupyingUnit.getComponent(UnitStatsComponent.class).isAlly) {
						continue;
					} else {
						processTile(adjMapCell, unitStatsComponent, nextMoveCost, unit);
					}
				}
			}
		}
	}
	
	// Process Attack Tiles
	private void processAttackTile(MapCell initialTile, UnitStatsComponent unitStatsComponent) {
		for (MapCell mapCell : allPossibleMoves) {	
			for (int i = 0; i < mapCell.adjTiles.size; i++) {
				MapCell adjCell = mapCell.adjTiles.get(i);
				if (!allPossibleMoves.contains(adjCell)) {
					attackCells.add(adjCell);
				}
			}
		}
		
		// Process Ranged Units
		for (int i = 1; i < unitStatsComponent.attackRange; i++) {
			Array<MapCell> newAttackCells = new Array<MapCell>();
			for (int h = 0; h < attackCells.size; h++) {
				MapCell mapCell = attackCells.get(h);
				for (int j = 0; j < mapCell.adjTiles.size; j++) {
					MapCell adjCell = mapCell.adjTiles.get(j);
					if (!allPossibleMoves.contains(adjCell) && !attackCells.contains(adjCell, true)) {
						newAttackCells.add(adjCell);
					}
				}
			}
			attackCells.addAll(newAttackCells);
		}
	}

	// Set Parent Tiles
	private void setParentTiles(MapCell initialCell) {
		for (MapCell mapCell : allPossibleMoves) {
			mapCell.parentTile = null;
			mapCell.isVisited = false;
		}

		// Open List for tiles to be processed
		Queue<MapCell> openList = new Queue<MapCell>();

		// Set initial tile to itself
		initialCell.parentTile = initialCell;
		openList.addFirst(initialCell);

		// Must be part of the valid tile set and not visited yet
		while (openList.size > 0) {
			MapCell startingCell = openList.removeFirst();

			for (int i = 0; i < startingCell.adjTiles.size; i++) {
				MapCell adjCell = startingCell.adjTiles.get(i);

				if (allPossibleMoves.contains(adjCell) && !adjCell.isVisited) {
					adjCell.isVisited = true;
					adjCell.parentTile = startingCell;

					// Add to List
					openList.addLast(adjCell);
				}
			}
		}
	}

	// Get Pathfinding Queue
	public void createPathFindingQueue(MapCell destinationCell, Entity unit) {
		// Starting cell
		UnitStatsComponent unitStatsComponent = uComponentMapper.get(unit);

		// Clear Queue
		pathfindingQueue.clear();
		unitStatsComponent.pathfindingQueue.clear();

		MapCell nextCell = destinationCell;
		MapCell startingCell = unitStatsComponent.currentCell;
		pathfindingQueue.addFirst(destinationCell);

		// Work backward until we have found the destination cell
		while (!nextCell.equals(startingCell)) {
			pathfindingQueue.addFirst(nextCell.parentTile);
			nextCell = nextCell.parentTile;
		}

		unitStatsComponent.pathfindingQueue = pathfindingQueue;
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

	// Enable Squares
	private void enableSquares() {
		for (MapCell mapCell : allPossibleMoves) {
			sComponentMapper.get(mapCell.blueSquare).isEnabled = true;
		}
		
		for (MapCell mapCell : attackCells) {
			sComponentMapper.get(mapCell.redSquare).isEnabled = true;
		}
	}
	
	// Reset to default
	public void resetMovementAlgorithms() {
		// Reset Colors
		for (MapCell mapCell : allMapCells) {
			StaticImageComponent blueStaticImageComponent = sComponentMapper.get(mapCell.blueSquare);
			blueStaticImageComponent.isEnabled = false;
			StaticImageComponent redStaticImageComponent = sComponentMapper.get(mapCell.redSquare);
			redStaticImageComponent.isEnabled = false;
		}
	}
}
