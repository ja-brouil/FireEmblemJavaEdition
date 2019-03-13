package com.jb.fe.systems.gamePlay;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.utils.Array;
import com.jb.fe.components.InventoryComponent;
import com.jb.fe.components.ItemComponent;
import com.jb.fe.components.MovementStatsComponent;
import com.jb.fe.components.UnitStatsComponent;
import com.jb.fe.level.Level;
import com.jb.fe.screens.GameScreen;
import com.jb.fe.systems.SystemPriorityList;
import com.jb.fe.systems.graphics.CombatAnimationSystem;
import com.jb.fe.systems.movement.UnitMapCellUpdater;

public class CombatSystem extends EntitySystem{

	public static Entity attackingUnit;
	public static Entity defendingUnit;
	public static boolean isProcessing;
	
	// Statistics
	public static int attackingDamage;
	public static int defendingDamage;
	
	private Level level;
	private UnitMapCellUpdater unitMapCellUpdater;
	
	private ComponentMapper<UnitStatsComponent> uComponentMapper = ComponentMapper.getFor(UnitStatsComponent.class);
	private ComponentMapper<ItemComponent> iComponentMapper = ComponentMapper.getFor(ItemComponent.class);
	private ComponentMapper<InventoryComponent> invComponentMapper = ComponentMapper.getFor(InventoryComponent.class);
	private ComponentMapper<MovementStatsComponent> mComponentMapper = ComponentMapper.getFor(MovementStatsComponent.class);
	
	public CombatSystem(UnitMapCellUpdater unitMapCellUpdater, GameScreen gameSreen) {
		priority = SystemPriorityList.CombatPhase;
		this.unitMapCellUpdater = unitMapCellUpdater;
		isProcessing = false;
	}

	@Override
	public void update(float delta) {
		if (!isProcessing) { return; }
		
		// Process attacking unit first
		UnitStatsComponent defendingUnitStats = uComponentMapper.get(defendingUnit);
		if (CombatSystemCalculator.AttackingDamage == -10000) {
			System.out.println("DAMAGE MISS");
			//defendingUnitStats.health -= CombatSystemCalculator.AttackingDamage;
		} 
		
		// Process Item reduction/destruction
		InventoryComponent attackingInventoryComponent = invComponentMapper.get(attackingUnit);
		ItemComponent attackingItemComponent = iComponentMapper.get(attackingInventoryComponent.selectedItem);
		
		attackingItemComponent.uses--;
		if (attackingItemComponent.uses <= 0) {
			attackingInventoryComponent.removeItem(attackingInventoryComponent.selectedItemIndex);
		}
		
		// Start Animation
		CombatAnimationSystem.isProcessing = true; 
		System.out.println("COMBAT ANIMATION STARTING");
		if (defendingUnitStats.health - CombatSystemCalculator.AttackingDamage <= 0 ) {
			isProcessing = false;
			
			//defendingUnitStats.health = 0;
			return;
		}	
		
		// Process defending unit
		UnitStatsComponent attackingUnitStats = uComponentMapper.get(attackingUnit);
		if (CombatSystemCalculator.DefendingDamage != -10000) {
			attackingUnitStats.health -= CombatSystemCalculator.DefendingDamage;
		} else {
			// TO DO MISS ANIMATION
		}
		
		// Process Item reduction and destroy it if it is out of uses
		InventoryComponent defendingInventoryComponent = invComponentMapper.get(defendingUnit);
		ItemComponent defendingItemComponent = iComponentMapper.get(defendingInventoryComponent.selectedItem);
		defendingItemComponent.uses--;
		if (defendingItemComponent.uses <= 0) {
			defendingInventoryComponent.removeItem(defendingInventoryComponent.selectedItemIndex);
		}
		
		if (attackingUnitStats.health <= 0) {
			/*
			attackingUnitStats.health = 0;
			getEngine().removeEntity(attackingUnit);
			
			// Remove unit from Level array
			if (mComponentMapper.get(attackingUnit).isAlly) {
				removeUnitFromLevelArray(level.allAllies, attackingUnit);
			} else {
				removeUnitFromLevelArray(level.allEnemies, attackingUnit);
			}
			*/
		}
		
		// Combat animations
		System.out.println("COMBAT RETALIATION STARTING");
		
		// Turn off system
		isProcessing = false;
		
		// Update cells
		unitMapCellUpdater.updateCellInfo();
	}

	
	private void removeUnitFromLevelArray(Array<Entity> levelArray, Entity unitToRemove) {
		levelArray.removeValue(unitToRemove, true);
	}
	
	// Load Level
	public void loadLevel(Level level) {
		this.level = level;
	}
	
	// Remove entity upon death
	public void removeEntity(UnitStatsComponent defendingUnitStats, Entity unitToRemove) {
		defendingUnitStats.health = 0;
		
		getEngine().removeEntity(unitToRemove);
		
		// Remove unit from Level array
		if (mComponentMapper.get(unitToRemove).isAlly) {
			removeUnitFromLevelArray(level.allAllies, unitToRemove);
		} else {
			removeUnitFromLevelArray(level.allEnemies, unitToRemove);
		}
		
		unitMapCellUpdater.updateCellInfo();
	}
}
