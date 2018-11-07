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
import com.jb.fe.components.Artifical_IntelligenceComponent.AI_TYPE;
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
	private Artifical_IntelligenceComponent artifical_IntelligenceComponent;
	
	
	// Entity to process
	private Entity enemyUnit;
	
	public AISystem() {
		// This system should only be started when the AI is needed.
		priority = SystemPriorityDictionnary.AI_System;
		setProcessing(false);
		
		reachableUnits = new Array<Entity>();
		allyUnits = new Array<Entity>();
		enemyUnits = new Array<Entity>();
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
	
	public void processAI() {
		findAllEnemies();
	}
	
	// Find all reachable enemies
	private void findAllEnemies() {
		// Get Movement Tiles
		movementUtilityCalculator.calculateAllPossibleMoves(enemyUnit);
		
		// Clear Arrays
		reachableUnits.clear();
		UnitStatsComponent enemyUnitComponent = uComponentMapper.get(enemyUnit);
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
		if (artifical_IntelligenceComponent.ai_Type.equals(AI_TYPE.PASSIVE)) {
			processPassiveAI();
		} else if (artifical_IntelligenceComponent.ai_Type.equals(AI_TYPE.AGGRESSIVE)) {
			processAggresiveAI();
		}
	}
	
	// Process AI Types
	// Passive
	private void processPassiveAI() {
		if (reachableUnits.size == 0) {
			artifical_IntelligenceComponent.isProcessing = false;
			enemyUnit = null;
		} else {
			findUnitToAttack();
		}
	}
	
	// Aggressive
	private void processAggresiveAI() {
		
		artifical_IntelligenceComponent.isProcessing = false;
		enemyUnit = null;
	}
	
	// Healer
	
	// Boss
	
	// Find Unit to attack
	private void findUnitToAttack() {
		// Initialize stats for finding enemy -> Add moving to Eirika if we find her -> add other checks here
		int hp = 100000;
		Entity currentUnitToAttack = null;
		UnitStatsComponent allyUnitStatsComponent;
		for (Entity allyUnit : reachableUnits) {
			allyUnitStatsComponent = uComponentMapper.get(allyUnit);
			
			if (allyUnitStatsComponent.health < hp) {
				currentUnitToAttack = allyUnit;
				hp = allyUnitStatsComponent.health;
			}
		}
		
		findTileToMoveTo(currentUnitToAttack);
	}
	
	// Find Tile to Move to -> This algorithm will have to be modified for ranged units
	private void findTileToMoveTo(Entity unitToAttack) {
		// Check if unit is melee or ranged
		UnitStatsComponent enemyStats = uComponentMapper.get(enemyUnit);
		if (enemyStats.attackRange == 1) {
			for (MapCell mapCell : uComponentMapper.get(unitToAttack).currentCell.adjTiles) {
				if (enemyStats.allPossibleMoves.contains(mapCell)) {
						if (mapCell.isOccupied) {
							if (mapCell.occupyingUnit.equals(enemyUnit)) {
								enemyStats.destinationCell = mapCell;
								movementUtilityCalculator.createPathFindingQueue(enemyStats.destinationCell, enemyUnit);
								enemyStats.isMoving = true;
								enemyUnit = null;
								return;
							}
						} else {
							enemyStats.destinationCell = mapCell;
							movementUtilityCalculator.createPathFindingQueue(enemyStats.destinationCell, enemyUnit);
							enemyStats.isMoving = true;
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
