package com.jb.fe.systems.movement;

import java.util.HashSet;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.components.StaticImageComponent;
import com.jb.fe.components.InventoryComponent;
import com.jb.fe.components.ItemComponent;
import com.jb.fe.components.MovementStatsComponent;
import com.jb.fe.level.Level;
import com.jb.fe.map.MapCell;

/*
 * Algorithm for calculating Movement System
 */

public class MovementUtilityCalculator {

	// Pathfinding arrays
	private HashSet<MapCell> allPossibleMoves;
	private Array<MapCell> attackCells;
	private Queue<MapCell> pathfindingQueue;

	// All Map Cells
	private MapCell[][] allMapCells;

	// Component Mapper
	private ComponentMapper<PositionComponent> pComponentMapper = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<MovementStatsComponent> uComponentMapper = ComponentMapper.getFor(MovementStatsComponent.class);
	private ComponentMapper<StaticImageComponent> sComponentMapper = ComponentMapper.getFor(StaticImageComponent.class);
	private ComponentMapper<InventoryComponent> invComponentMapper = ComponentMapper.getFor(InventoryComponent.class);
	private ComponentMapper<ItemComponent> iComponentMapper = ComponentMapper.getFor(ItemComponent.class);

	// Cell Updated
	private UnitMapCellUpdater unitMapCellUpdater;

	public MovementUtilityCalculator(Level level, UnitMapCellUpdater unitMapCellUpdater) {
		allPossibleMoves = new HashSet<>();
		attackCells = new Array<>();
		pathfindingQueue = new Queue<>();
		this.unitMapCellUpdater = unitMapCellUpdater;
		allMapCells = level.allLevelMapCells;
	}

	// Get All possible Moves
	public void calculateAllPossibleMoves(Entity unit) {
		// Update all the cells
		unitMapCellUpdater.updateCellInfo();

		// Stats on unit
		MovementStatsComponent unitStatsComponent = uComponentMapper.get(unit);
		InventoryComponent inventoryComponent = invComponentMapper.get(unit);

		// Reset Queue
		allPossibleMoves.clear();
		attackCells.clear();
		unitStatsComponent.allPossibleMoves.clear();
		unitStatsComponent.allOutsideAttackMoves.clear();

		// Reset Stats on MapCell
		for (int outer = 0; outer < allMapCells.length; outer++) {
			for (int inner = 0; inner < allMapCells[0].length; inner++) {
				allMapCells[outer][inner].isVisited = false;
				allMapCells[outer][inner].distanceFromParent = 0;
				allMapCells[outer][inner].parentTile = null;
			}
		}

		// Remove Colors if not reset
		resetMovementAlgorithms();

		// Process Movement and Attack Tiles
		processTile(getMapCell(unit), unitStatsComponent, unitStatsComponent.movementSteps, unit);
		for (MapCell attackMapCell : allPossibleMoves) {
			processAttackTile(attackMapCell, iComponentMapper.get(inventoryComponent.selectedItem).maxRange, unitStatsComponent, unit);
		}
		
		// Light Squares
		enableSquares();

		// Get Parent Tiles
		setParentTiles(getMapCell(unit));

		// Set Stats
		unitStatsComponent.allPossibleMoves = allPossibleMoves;
		unitStatsComponent.allOutsideAttackMoves = attackCells;
	}

	private void processTile(MapCell initialTile, MovementStatsComponent unitStatsComponent, int moveSteps, Entity unit) {
		// Add initial tile
		allPossibleMoves.add(initialTile);

		// Process all Adj tiles
		for (int i = 0; i < initialTile.adjTiles.size; i++) {

			MapCell adjMapCell = initialTile.adjTiles.get(i);

			// Next Move Cost
			int nextMoveCost = moveSteps - adjMapCell.movementCost;

			if (nextMoveCost >= 0) {
				// Allow ally passage
				if (adjMapCell.isOccupied && !(adjMapCell.occupyingUnit
						.getComponent(MovementStatsComponent.class).isAlly == unitStatsComponent.isAlly)) {
					continue;
				} else {
					processTile(adjMapCell, unitStatsComponent, nextMoveCost, unit);
				}

			}
		}
	}

	// Process Attack Tiles
	private void processAttackTile(MapCell initialTile, int attackRange, MovementStatsComponent unitStatsComponent, Entity checkingUnit) {
		
		// Can't attack if ally unit is on the tile that you can "reach"
		if (initialTile.isOccupied && !initialTile.occupyingUnit.equals(checkingUnit) && (initialTile.occupyingUnit.getComponent(MovementStatsComponent.class).isAlly == unitStatsComponent.isAlly)) {
			attackRange -= 1;
		}
		
		for (int i = 0; i < initialTile.adjTiles.size; i++) {

			MapCell adjMapCell = initialTile.adjTiles.get(i);

			// Next Attack Range
			int nextAttackRange = attackRange - 1;

			if (nextAttackRange >= 0) {
				if (!allPossibleMoves.contains(adjMapCell)) {
					if (!adjMapCell.isOccupied) {
						attackCells.add(adjMapCell);
						processAttackTile(adjMapCell, nextAttackRange, unitStatsComponent, checkingUnit);
					} else if (adjMapCell.isOccupied && !(adjMapCell.occupyingUnit.getComponent(MovementStatsComponent.class).isAlly == unitStatsComponent.isAlly)) {
						attackCells.add(adjMapCell);
						processAttackTile(adjMapCell, nextAttackRange, unitStatsComponent, checkingUnit);
					}
				}
			}
		}
	}

	// Set Parent Tiles || replace this maybe with a*?
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
		MovementStatsComponent unitStatsComponent = uComponentMapper.get(unit);

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
	
	public void createPathFindingQueueAStart(MapCell destination, Entity unit) {
		// Clear Queue
		pathfindingQueue.clear();
		uComponentMapper.get(unit).pathfindingQueue.clear();
		
		MapCell nextCell = destination;
		MapCell startingCell = uComponentMapper.get(unit).currentCell;
		
		while(!nextCell.equals(startingCell)) {
			pathfindingQueue.addFirst(nextCell);
			nextCell = nextCell.parentTileAStar;
		}
		
		uComponentMapper.get(unit).pathfindingQueue = pathfindingQueue;
	}

	// Get Tile from All Tiles
	public MapCell getMapCell(Entity unit) {
		PositionComponent unitPositionComponent = pComponentMapper.get(unit);
		int x = (int) unitPositionComponent.x / MapCell.CELL_SIZE;
		int y = (int) unitPositionComponent.y / MapCell.CELL_SIZE;

		return allMapCells[x][y];
	}

	// Enable Squares
	public void enableSquares() {
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
		for (int outer = 0; outer < allMapCells.length; outer++) {
			for (int inner = 0; inner < allMapCells[outer].length; inner++) {
				StaticImageComponent staticImageComponent = sComponentMapper.get(allMapCells[outer][inner].blueSquare);
				staticImageComponent.isEnabled = false;
				StaticImageComponent redStaticImage = sComponentMapper.get(allMapCells[outer][inner].redSquare);
				redStaticImage.isEnabled = false;
			}
		}
	}
	
	/*
	 * Calculate the cost of going to the end node
	 */
	public int calculateHCost(MapCell initial, MapCell destination) {
		int startingNodeY = (int) initial.position.y / MapCell.CELL_SIZE;
		int startingNodeX = (int) initial.position.x / MapCell.CELL_SIZE;
		int totalVerticalCost = 0;
		int totalHorizontalCost = 0;
		
		if (startingNodeX > startingNodeY) {
			// North | South
			int verticalMovement = 1;
			if (destination.position.y - destination.position.y < 0) {
				verticalMovement = -1;
			} else if (destination.position.y - destination.position.y == 0){
				verticalMovement = 0;
			}
			
			
			for (int i = 0; i < verticalTileAmount(initial, destination); i++) {
				totalVerticalCost += allMapCells[startingNodeX][startingNodeY + verticalMovement].movementCost;
			}
			
			// West | East
			int horizontalMovement = 1;
			if (destination.position.x - destination.position.x < 0) {
				horizontalMovement = -1;
			} else if (destination.position.x - destination.position.x == 0) {
				horizontalMovement = 0;
			}
			

			for (int i = 0; i < horizontalTileAmount(initial, destination); i++) {
				totalHorizontalCost += allMapCells[startingNodeX + horizontalMovement][startingNodeY].movementCost;
			}
		} else {
			// West | East
			int horizontalMovement = 1;
			if (destination.position.x - destination.position.x < 0) {
				horizontalMovement = -1;
			} else if (destination.position.x - destination.position.x == 0) {
				horizontalMovement = 0;
			}
			
			for (int i = 0; i < horizontalTileAmount(initial, destination); i++) {
				totalHorizontalCost += allMapCells[startingNodeX + horizontalMovement][startingNodeY].movementCost;
			}
			
			// North | South
			int verticalMovement = 1;
			if (destination.position.y - destination.position.y < 0) { 
				verticalMovement = -1;
			} else if (destination.position.y - destination.position.y == 0){
				verticalMovement = 0;
			}
			
			
			for (int i = 0; i < verticalTileAmount(initial, destination); i++) {
				totalVerticalCost += allMapCells[startingNodeX][startingNodeY + verticalMovement].movementCost;
			}
		}
		
		return totalVerticalCost + totalHorizontalCost;
	}
	
	public int verticalTileAmount(MapCell initial, MapCell destination) {
		return Math.abs(((int) (initial.position.y / MapCell.CELL_SIZE)) - ((int) (destination.position.y / MapCell.CELL_SIZE)));
	}
	
	public int horizontalTileAmount(MapCell initial, MapCell destination) {
		return Math.abs(((int) (initial.position.x / MapCell.CELL_SIZE)) - ((int) (destination.position.x / MapCell.CELL_SIZE)));
	}
	
	public HashSet<MapCell> getAllPossibleMoves() {
		return allPossibleMoves;
	}
}
