package com.jb.fe.systems.gamePlay;

import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.utils.Array;
import com.jb.fe.components.Artifical_IntelligenceComponent;
import com.jb.fe.components.Artifical_IntelligenceComponent.AI_TYPE;
import com.jb.fe.components.NameComponent;
import com.jb.fe.components.UnitStatsComponent;
import com.jb.fe.map.MapCell;
import com.jb.fe.systems.SystemPriorityDictionnary;
import com.jb.fe.systems.movement.MovementUtilityCalculator;

public class AISystem extends EntitySystem{

	// All Units
	private ImmutableArray<Entity> allGameEntities;
	private Array<Entity> allyUnits;
	private Array<Entity> enemyUnits;
	
	// Reachable Units
	private Array<Entity> reachableUnits;
	
	// Movement Calculator
	private MovementUtilityCalculator movementUtilityCalculator;
	
	// Component Mappers 
	private ComponentMapper<UnitStatsComponent> uComponentMapper = ComponentMapper.getFor(UnitStatsComponent.class);
	private ComponentMapper<Artifical_IntelligenceComponent> aiComponentMapper = ComponentMapper.getFor(Artifical_IntelligenceComponent.class);
	private ComponentMapper<NameComponent> nameComponentMapper = ComponentMapper.getFor(NameComponent.class);
	
	// Processing Component
	private Artifical_IntelligenceComponent artifical_IntelligenceComponent;
	private UnitStatsComponent enemyUnitComponent;
	
	// Eirika for Pathfinding Purposes
	private Entity eirika;
	private Comparator<MapCell> compareMovementCost;
	
	// Entity to process
	private Entity enemyUnit;
	
	public AISystem() {
		// This system should only be started when the AI is needed.
		priority = SystemPriorityDictionnary.AI_System;
		setProcessing(false);
		
		reachableUnits = new Array<Entity>();
		allyUnits = new Array<Entity>();
		enemyUnits = new Array<Entity>();
		compareMovementCost = (MapCellA, MapCellB) -> {
			return MapCellA.fCost - MapCellB.fCost;
		};
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
				
				// Set Eirika
				if (nameComponentMapper.get(entity).name.equals("Eirika")) {
					eirika = entity;
				}
				
				if(uComponentMapper.get(entity).isAlly) {
					allyUnits.add(entity);
				} else {
					enemyUnits.add(entity);
				}
			}
		});
	}
	
	public void processAI() {
		findAllEnemies();
	}
	
	// Find all reachable enemies
	private void findAllEnemies() {
		// Get Movement Tiles
		movementUtilityCalculator.calculateAllPossibleMoves(enemyUnit);
		
		// Clear Arrays
		reachableUnits.clear();
		enemyUnitComponent = uComponentMapper.get(enemyUnit);
		artifical_IntelligenceComponent = aiComponentMapper.get(enemyUnit);
		
		// Start Unit Processing
		artifical_IntelligenceComponent.isProcessing = true;
		
		for (Entity allyUnit : allyUnits) {
			UnitStatsComponent allyUnitStatComponent = uComponentMapper.get(allyUnit);
			if (enemyUnitComponent.allOutsideAttackMoves.contains(allyUnitStatComponent.currentCell, true)) {
				reachableUnits.add(allyUnit);
			}
		}
		
		// Process AI type
		findEnemy();
	}
	
	// Process AI Types
	// Passive
	private void findEnemy() {
		if (reachableUnits.size == 0) {
			if (artifical_IntelligenceComponent.ai_Type == AI_TYPE.AGGRESSIVE) {
				processAggresiveAI();
			} else {
				artifical_IntelligenceComponent.isProcessing = false;
				enemyUnit = null;
			}
		} else {
			// Become aggresive once an enemy has been detected
			artifical_IntelligenceComponent.ai_Type = AI_TYPE.AGGRESSIVE;
			findUnitToAttack();
		}
	}
	
	// Aggressive
	/*
	 * might have to some slight changes here
	 */
	private void processAggresiveAI() {
		// Locate where is Eirika
		MapCell eirikaMapCell = uComponentMapper.get(eirika).currentCell;
		MapCell destinationCell = null;
		MapCell currentCell = null;
		for (MapCell mapCell : eirikaMapCell.adjTiles) {
			if (mapCell.movementCost < 100) {
				destinationCell = mapCell;
				break;
			}
		}
		
		// Reset Heuristic costs
		for (MapCell mapCell : enemyUnitComponent.allPossibleMoves) {
			mapCell.gCost = 0;
			mapCell.hCost = 0;
			mapCell.fCost = 0;
			mapCell.parentTileAStar = null;
		}
		
		// Find A* path to the destination cell
		PriorityQueue<MapCell> openList = new PriorityQueue<>(compareMovementCost);
		HashSet<MapCell> closedList = new HashSet<>();
		openList.add(enemyUnitComponent.currentCell);
		
		// Process while queue is not empty
		while(!openList.isEmpty()) {
			
			currentCell = openList.poll();	
			closedList.add(currentCell);
			
			// If we have found our destination, exit right away
			if (currentCell.equals(destinationCell)) {
				break;
			}
			
			for (MapCell adjcell : currentCell.adjTiles) {
				// Do not process unwalkable tiles or if the closed list contains the adjcell already
				if (adjcell.movementCost >= 50 || closedList.contains(adjcell)){
					continue;
				}
				
				// Calculate heuristics
				int newMovementCostToNeighbor = currentCell.gCost + adjcell.movementCost;
				if (newMovementCostToNeighbor < adjcell.gCost || !openList.contains(adjcell)) {
					adjcell.gCost = newMovementCostToNeighbor;
					adjcell.hCost = movementUtilityCalculator.calculateHCost(adjcell, destinationCell);
					adjcell.parentTileAStar = currentCell;
					
					// Add to openList
					if (!openList.contains(adjcell)) {
						openList.add(adjcell);
					}
				}
			}
		}
		
		// Find out which cell exists in the current unit's allowed movements
		enemyUnitComponent.destinationCell = currentCell;
		movementUtilityCalculator.createPathFindingQueueAStart(enemyUnitComponent.destinationCell, enemyUnit);
		
		int max = enemyUnitComponent.pathfindingQueue.size;
		MapCell finalMoveCell = null;
		for (int i = 0 ; i < max; i++) {
			MapCell testMapCell = enemyUnitComponent.pathfindingQueue.removeFirst();
			if (enemyUnitComponent.allPossibleMoves.contains(testMapCell)) {
				finalMoveCell =  testMapCell;
			}
		}
		movementUtilityCalculator.createPathFindingQueue(finalMoveCell, enemyUnit);
		enemyUnitComponent.isMoving = true;
		enemyUnit = null;
	}
	
	// Healer
	
	// Boss
	
	// Find Unit to attack
	private void findUnitToAttack() {
		// Initialize stats for finding enemy -> Add moving to Eirika if we find her -> add other checks here
		int lowestHP = 100000;
		Entity currentUnitToAttack = null;
		UnitStatsComponent allyUnitStatsComponent;
		for (Entity allyUnit : reachableUnits) {
			allyUnitStatsComponent = uComponentMapper.get(allyUnit);
			
			// Lowest HP
			if (allyUnitStatsComponent.health < lowestHP) {
				currentUnitToAttack = allyUnit;
				lowestHP = allyUnitStatsComponent.health;
			} // Add extra checks here
		}
		
		findTileToMoveTo(currentUnitToAttack);
	}
	
	// Find Tile to Move to -> This algorithm will have to be modified for ranged units
	private void findTileToMoveTo(Entity unitToAttack) {
		// Check if unit is melee or ranged
		if (enemyUnitComponent.attackRange == 1) {
			for (MapCell mapCell : uComponentMapper.get(unitToAttack).currentCell.adjTiles) {
				if (enemyUnitComponent.allPossibleMoves.contains(mapCell)) {
						if (mapCell.isOccupied) {
							if (mapCell.occupyingUnit.equals(enemyUnit)) {
								enemyUnitComponent.destinationCell = mapCell;
								movementUtilityCalculator.createPathFindingQueue(enemyUnitComponent.destinationCell, enemyUnit);
								enemyUnitComponent.isMoving = true;
								enemyUnit = null;
								return;
							}
						} else {
							enemyUnitComponent.destinationCell = mapCell;
							movementUtilityCalculator.createPathFindingQueue(enemyUnitComponent.destinationCell, enemyUnit);
							enemyUnitComponent.isMoving = true;
							enemyUnit = null;
							return;
						}
				}
			}
		} else {
			System.out.println("ranged unit check!");
		}
	}
	
	// Process Combat
	
	
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
	
	public void setMovementCalculator(MovementUtilityCalculator movementUtilityCalculator) {
		this.movementUtilityCalculator = movementUtilityCalculator;
	}
}
