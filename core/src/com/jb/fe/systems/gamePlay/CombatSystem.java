package com.jb.fe.systems.gamePlay;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.jb.fe.components.InventoryComponent;
import com.jb.fe.components.ItemComponent;
import com.jb.fe.components.UnitStatsComponent;
import com.jb.fe.systems.SystemPriorityDictionnary;

public class CombatSystem extends EntitySystem{

	public static Entity attackingUnit;
	public static Entity defendingUnit;
	public static boolean isProcessing;
	
	private ComponentMapper<UnitStatsComponent> uComponentMapper = ComponentMapper.getFor(UnitStatsComponent.class);
	private ComponentMapper<ItemComponent> iComponentMapper = ComponentMapper.getFor(ItemComponent.class);
	private ComponentMapper<InventoryComponent> invComponentMapper = ComponentMapper.getFor(InventoryComponent.class);
	
	public CombatSystem() {
		priority = SystemPriorityDictionnary.CombatPhase;
		isProcessing = false;
	}

	@Override
	public void update(float delta) {
		if (!isProcessing) { return; }
		
		// Process attacking unit first
		UnitStatsComponent defendingUnitStats = uComponentMapper.get(defendingUnit);
		if (CombatSystemCalculator.AttackingDamage != -10000) {
			defendingUnitStats.health -= CombatSystemCalculator.AttackingDamage;
		} else {
			// TO DO MISS ANIMATION
		}
		
		// Process Item reduction/destruction
		InventoryComponent attackingInventoryComponent = invComponentMapper.get(attackingUnit);
		ItemComponent attackingItemComponent = iComponentMapper.get(attackingInventoryComponent.selectedItem);
		
		attackingItemComponent.uses--;
		if (attackingItemComponent.uses <= 0) {
			attackingInventoryComponent.removeItem(attackingInventoryComponent.selectedItemIndex);
		}
		
		if (defendingUnitStats.health <= 0 ) {
			defendingUnitStats.health = 0;
			getEngine().removeEntity(defendingUnit);
			isProcessing = false;
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
			attackingUnitStats.health = 0;
			getEngine().removeEntity(attackingUnit);
		}
		
		// Turn off system
		isProcessing = false;
	}
}
