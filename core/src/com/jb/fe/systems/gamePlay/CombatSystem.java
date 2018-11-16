package com.jb.fe.systems.gamePlay;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.jb.fe.components.InventoryComponent;
import com.jb.fe.components.ItemComponent;
import com.jb.fe.components.ItemComponent.ItemType;
import com.jb.fe.components.UnitStatsComponent;
import com.jb.fe.systems.SystemPriorityDictionnary;

public class CombatSystem extends EntitySystem{
	
	private Entity attackingUnit;
	private Entity defendingUnit;
	
	private ComponentMapper<UnitStatsComponent> uComponentMapper = ComponentMapper.getFor(UnitStatsComponent.class);
	private ComponentMapper<InventoryComponent> invComponentMapper = ComponentMapper.getFor(InventoryComponent.class);
	private ComponentMapper<ItemComponent> iComponentMapper = ComponentMapper.getFor(ItemComponent.class);
	private UnitStatsComponent attackingUnitStats;
	private UnitStatsComponent defendingUnitStats;
	private InventoryComponent attackingInventory;
	private InventoryComponent defendingInventory;
	private ItemComponent attackingItem;
	private ItemComponent defendingItem;
	
	public CombatSystem() {
		priority = SystemPriorityDictionnary.CombatPhase;
		setProcessing(false);
	}
	
	// Calculations
	// Double Attack
	public boolean hasDoubleAttack() {
		ItemComponent item = iComponentMapper.get(attackingInventory.selectedItem);
		
		int itemWeightStat = item.weight - attackingUnitStats.consti;
		if (itemWeightStat < 0) { itemWeightStat = 0; }
		
		int attackSpeed = attackingUnitStats.speed - (itemWeightStat);
		if (attackSpeed - defendingUnitStats.speed >= 4) {
			return true;
		}
		return false;
	}
	
	// Utility functions
	public void getComponents() {
		attackingUnitStats = uComponentMapper.get(attackingUnit);
		defendingUnitStats = uComponentMapper.get(defendingUnit);
		attackingInventory = invComponentMapper.get(attackingUnit);
		defendingInventory = invComponentMapper.get(defendingUnit);
		attackingItem = iComponentMapper.get(attackingInventory.selectedItem);
		defendingItem = iComponentMapper.get(defendingInventory.selectedItem);
	}
	
	public void clearEntities() {
		this.attackingUnit = null;
		this.defendingUnit = null;
		this.attackingUnitStats = null;
		this.defendingUnitStats = null;
		this.attackingInventory = null;
		this.defendingInventory = null;
		this.attackingItem = null;
	}
	
	public WeaponBonusStatus hasWeaponBonus() {
		// Bow
		if (attackingItem.itemType.equals(ItemType.BOW)) {
			return WeaponBonusStatus.NEUTRAL;
		}
		
		// Neutral
		if (attackingItem.itemType.equals(defendingItem.itemType)) {
			return WeaponBonusStatus.NEUTRAL;
		}
		
		// Strong
		
		
		// Weak
		return WeaponBonusStatus.WEAK;
		
	}
	
	public enum WeaponBonusStatus {
		BONUS, WEAK, NEUTRAL
	}
}