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
import com.jb.fe.components.InventoryComponent;
import com.jb.fe.components.ItemComponent;
import com.jb.fe.components.MovementStatsComponent;
import com.jb.fe.components.NameComponent;
import com.jb.fe.components.UnitStatsComponent;
import com.jb.fe.map.MapCell;
import com.jb.fe.systems.SystemPriorityList;
import com.jb.fe.systems.movement.MovementUtilityCalculator;
import com.jb.fe.systems.movement.UnitMovementSystem;

public class AISystem extends EntitySystem{

	// All Units
	private ImmutableArray<Entity> allGameEntities;
	private Array<Entity> allyUnits;
	private Array<Entity> enemyUnits;
	
	// Reachable Units
	private Array<Entity> reachableUnits;
	
	// Movement Calculator
	private MovementUtilityCalculator movementUtilityCalculator;
	
	// Combat Calculator
	private CombatSystemCalculator combatSystemCalculator;
	
	// Component Mappers 
	private ComponentMapper<MovementStatsComponent> movementComponentMapper = ComponentMapper.getFor(MovementStatsComponent.class);
	private ComponentMapper<Artifical_IntelligenceComponent> aiComponentMapper = ComponentMapper.getFor(Artifical_IntelligenceComponent.class);
	private ComponentMapper<NameComponent> nameComponentMapper = ComponentMapper.getFor(NameComponent.class);
	private ComponentMapper<UnitStatsComponent> uComponentMapper = ComponentMapper.getFor(UnitStatsComponent.class);
	private ComponentMapper<InventoryComponent> invComponentMapper = ComponentMapper.getFor(InventoryComponent.class);
	private ComponentMapper<ItemComponent> iComponentMapper = ComponentMapper.getFor(ItemComponent.class);
	
	// Processing Component
	private Artifical_IntelligenceComponent artifical_IntelligenceComponent;
	private MovementStatsComponent enemyUnitComponent;
	
	// Eirika for Pathfinding Purposes
	private Entity eirika;
	private Comparator<MapCell> compareMovementCost;
	
	// Entity to process
	private Entity enemyUnit;
	private Entity currentUnitToAttack;
	
	public AISystem() {
		// This system should only be started when the AI is needed.
		priority = SystemPriorityList.AI_System;
		setProcessing(false);
		
		reachableUnits = new Array<Entity>();
		allyUnits = new Array<Entity>();
		enemyUnits = new Array<Entity>();
		compareMovementCost = (MapCellA, MapCellB) -> {
			return MapCellA.getFCost() - MapCellB.getFCost();
		};
	}

	@Override
	public void addedToEngine(Engine engine) {
		engine.addEntityListener(Family.all(MovementStatsComponent.class,  UnitStatsComponent.class).get(), new EntityListener() {
			
			@Override
			public void entityRemoved(Entity entity) {
				allGameEntities = engine.getEntitiesFor(Family.all(MovementStatsComponent.class, UnitStatsComponent.class).get());
				
				if(movementComponentMapper.get(entity).isAlly) {
					allyUnits.removeValue(entity, true);
				} else {
					enemyUnits.removeValue(entity, true);
				}
			}
			
			@Override
			public void entityAdded(Entity entity) {
				allGameEntities = engine.getEntitiesFor(Family.all(MovementStatsComponent.class,  UnitStatsComponent.class).get());
				
				// Set Eirika
				if (nameComponentMapper.get(entity).name.equals("Eirika")) {
					eirika = entity;
				}
				
				if(movementComponentMapper.get(entity).isAlly) {
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
		enemyUnitComponent = movementComponentMapper.get(enemyUnit);
		artifical_IntelligenceComponent = aiComponentMapper.get(enemyUnit);
		
		// Start Unit Processing
		artifical_IntelligenceComponent.isProcessing = true;
		
		for (Entity allyUnit : allyUnits) {
			MovementStatsComponent allyUnitStatComponent = movementComponentMapper.get(allyUnit);
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
				movementUtilityCalculator.resetMovementUtilities();
				enemyUnit = null;
			}
		} else {
			// Become aggressive once an enemy has been detected
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
		MapCell eirikaMapCell = movementComponentMapper.get(eirika).currentCell;
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
			if (enemyUnitComponent.allPossibleMoves.contains(testMapCell) && !testMapCell.isOccupied) {
				finalMoveCell =  testMapCell;
			}
		}
		
		// Prevent null errors
		if (finalMoveCell == null) {
			finalMoveCell = enemyUnitComponent.currentCell;
		}
		movementUtilityCalculator.createPathFindingQueue(finalMoveCell, enemyUnit);
		UnitMovementSystem.setEntity(enemyUnit);
		enemyUnit = null;
	}
	
	// Healer
	
	// Boss
	
	// Patrol
	
	// Find Unit to attack
	private void findUnitToAttack() {
		// Initialize stats for finding enemy -> Add moving to Eirika if we find her -> add other checks here
		int lowestHP = 100000;
		currentUnitToAttack = null;
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
	
	// Find Tile to Move to -> if we reached this far, that means there is an enemy that we can attack and move to
	// First part: if the tile that we can move to is occupied by us, don't move.
	// Second part: if the tile is not occupied by us, then just move to that tile
	private void findTileToMoveTo(Entity unitToAttack) {
		// Check if unit is melee or ranged
		if (iComponentMapper.get(invComponentMapper.get(unitToAttack).selectedItem).maxRange == 1) {
			for (MapCell mapCell : movementComponentMapper.get(unitToAttack).currentCell.adjTiles) {
				if (enemyUnitComponent.allPossibleMoves.contains(mapCell)) {
						if (mapCell.isOccupied) {
							if (mapCell.occupyingUnit.equals(enemyUnit)) {
								enemyUnitComponent.destinationCell = mapCell;
								movementUtilityCalculator.createPathFindingQueue(enemyUnitComponent.destinationCell, enemyUnit);
								enemyUnitComponent.isMoving = true;
								UnitMovementSystem.setEntity(enemyUnit);
								aiComponentMapper.get(enemyUnit).shouldAttack = true;
								return;
							}
						} else {
							enemyUnitComponent.destinationCell = mapCell;
							movementUtilityCalculator.createPathFindingQueue(enemyUnitComponent.destinationCell, enemyUnit);
							enemyUnitComponent.isMoving = true;
							UnitMovementSystem.setEntity(enemyUnit);
							aiComponentMapper.get(enemyUnit).shouldAttack = true;
							return;
						}
				}
			}
		} else {
			System.out.println("ranged unit check!");
		}
	}
	
	// Process Combat
	public void processCombat() {
		if (enemyUnit != null && aiComponentMapper.get(enemyUnit).shouldAttack) {
			// Set Units
			CombatSystem.attackingUnit = enemyUnit;
			CombatSystem.defendingUnit = currentUnitToAttack;
			combatSystemCalculator.setUnits(enemyUnit, currentUnitToAttack);
			
			// Calculate Numbers
			CombatSystemCalculator.AttackingDamage = combatSystemCalculator.calculateDamage();
			
			// Swap
			combatSystemCalculator.setUnits(currentUnitToAttack, enemyUnit);
			CombatSystemCalculator.DefendingDamage = combatSystemCalculator.calculateDamage();
			
			// Process
			CombatSystem.isProcessing = true;
			aiComponentMapper.get(enemyUnit).shouldAttack = false;
			enemyUnit = null;
		}
	}
	
	/*
	 * Sort entities based on alliance
	 */
	public void sortEntities(){
		// Clear Arrays
		allGameEntities = getEngine().getEntitiesFor(Family.all(MovementStatsComponent.class).get());
		allyUnits.clear();
		enemyUnits.clear();
		
		// Sort entities based on alliance
		for (Entity gameUnit : allGameEntities) {
			MovementStatsComponent unitStatsComponent = movementComponentMapper.get(gameUnit);
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
	
	public void setCombatSystemCalculator(CombatSystemCalculator combatSystemCalculator) {
		this.combatSystemCalculator = combatSystemCalculator;
	}
}
