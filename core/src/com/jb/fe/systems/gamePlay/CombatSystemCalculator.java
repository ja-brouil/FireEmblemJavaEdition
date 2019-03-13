package com.jb.fe.systems.gamePlay;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.MathUtils;
import com.jb.fe.components.InventoryComponent;
import com.jb.fe.components.ItemComponent;
import com.jb.fe.components.ItemComponent.ItemType;
import com.jb.fe.components.MovementStatsComponent;
import com.jb.fe.components.UnitStatsComponent;

/**
 * Calculate various battle stats
 * Modify these later to accomodate for support bonuses
 * @author james
 */
public class CombatSystemCalculator {
	
	private Entity attackingUnit;
	private Entity defendingUnit;
	
	private ComponentMapper<UnitStatsComponent> uComponentMapper = ComponentMapper.getFor(UnitStatsComponent.class);
	private ComponentMapper<InventoryComponent> invComponentMapper = ComponentMapper.getFor(InventoryComponent.class);
	private ComponentMapper<ItemComponent> iComponentMapper = ComponentMapper.getFor(ItemComponent.class);
	private ComponentMapper<MovementStatsComponent> mComponentMapper = ComponentMapper.getFor(MovementStatsComponent.class);
	private UnitStatsComponent attackingUnitStats;

	private UnitStatsComponent defendingUnitStats;
	private InventoryComponent attackingInventory;
	private InventoryComponent defendingInventory;
	private MovementStatsComponent defendingMovementStat;
	private ItemComponent attackingItem;
	private ItemComponent defendingItem;
	
	// These are set during the Unit Damage Preview Update Component
	public static int AttackingDamage = 0;
	public static int DefendingDamage = 0;
	public static int currentAttackingHealth;
	public static int currentDefendingHealth;
	
	public CombatSystemCalculator() {}
	
	// Calculations
	// Double Attack
	public boolean hasDoubleAttack(int attackSpeed) {
		if (attackSpeed - defendingUnitStats.speed >= 4) {
			System.out.println("HAS DOUBLE ATTACK");
			return true;
		}
		System.out.println("NO DOUBLE ATTACK");
		return false;
	}
	
	public boolean calculateHitChance() {
		int accuracy = attackingItem.hit + (attackingUnitStats.skill * 2) + (attackingUnitStats.luck / 2) + 5 + (getWeaponBonus() * 15);
		int enemyAvoidance = (getAttackSpeed(defendingInventory, defendingUnitStats) + defendingUnitStats.luck + 5 + (int) defendingMovementStat.currentCell.avoidanceBonus);
		int totalAccuracy = accuracy - enemyAvoidance;
		
		if (totalAccuracy >= 100) {
			return true;
		}
		
		if (totalAccuracy <= 0) {
			return false;
		}
		
		return MathUtils.random(100) <= totalAccuracy;
	}
	
	public int calculateHitChanceNumber() {
		int accuracy = attackingItem.hit + (attackingUnitStats.skill * 2) + (attackingUnitStats.luck / 2) + 5 + (getWeaponBonus() * 15);
		int enemyAvoidance = (getAttackSpeed(defendingInventory, defendingUnitStats) + defendingUnitStats.luck + 5 + (int) defendingMovementStat.currentCell.avoidanceBonus);
		int totalAccuracy = accuracy - enemyAvoidance;
		if (totalAccuracy < 0) {
			totalAccuracy = 0;
		}
		return totalAccuracy;
	}
	
	public boolean calculateCritChance() {
		int criticalRate = attackingItem.crit + (attackingUnitStats.skill / 2) + attackingUnitStats.bonusCrit;
		int battleCriticalRate = criticalRate - defendingUnitStats.luck;
		if (battleCriticalRate >= 100) {
			return true;
		}
		
		if (battleCriticalRate <= 0) {
			return false;
		}
		
		return MathUtils.random(100) <= battleCriticalRate;
	}
	
	public int calculateCritChanceNumber() {
		int criticalRate = attackingItem.crit + (attackingUnitStats.skill / 2) + attackingUnitStats.bonusCrit;
		int battleCriticalRate = criticalRate - defendingUnitStats.luck;
		if (battleCriticalRate < 0) {
			battleCriticalRate = 0;
		}
		return battleCriticalRate;
	}
	
	public int getAttackSpeed(InventoryComponent inventoryComponent, UnitStatsComponent unitStatsComponent) {
		ItemComponent item = iComponentMapper.get(inventoryComponent.selectedItem);
		
		int itemWeightStat = item.weight - unitStatsComponent.consti;
		if (itemWeightStat < 0) { itemWeightStat = 0; }
		
		return unitStatsComponent.speed - (itemWeightStat);
	}
	
	/**
	 * Calculates Damage dealt. Return -10000 if it's a miss. | Needs to be changed. Returning 0 for now
	 * @return
	 */
	public int calculateDamage() {
		int criticalHitDamageIncrease = 1;
		int effectiveBonus = 1; 				// change this later. Example: Bow vs flying = 3
		
		// Damage
		int baseAttack = 0;
		int baseDefence = 0;
		if (attackingItem.itemType.equals(ItemType.PHYSICAL)) {
			baseAttack = attackingUnitStats.str;
			baseDefence = defendingUnitStats.def;
		} else if (attackingItem.itemType.equals(ItemType.MAGIC)) {
			baseAttack = attackingUnitStats.magic;
			baseDefence = defendingUnitStats.res;
		}
		
		int attackDamage = baseAttack + ((attackingItem.might + getWeaponBonus()) * effectiveBonus);
		int defenceReduction = baseDefence + (int) defendingMovementStat.currentCell.defenceBonus; 
		
		// Critical Hits always hit
		if (calculateCritChance()) {
			System.out.println("CRITICAL HIT OCCURED");
			criticalHitDamageIncrease = 3;
			int finalDamageAmount = (attackDamage * criticalHitDamageIncrease) - defenceReduction;
			if (finalDamageAmount < 0) {
				return 0;
			}
			return finalDamageAmount;
		}
		
		// If Miss return -10000 | For now it's 0 but later change this so that it has a No Damage animation instead of miss
		if (!calculateHitChance()) {
			System.out.println("IT MISSED");
			return 0;
		}
		
		int finalDamageAmount = attackDamage - defenceReduction;
		if (finalDamageAmount < 0) {
			System.out.println("NO DAMAGE");
			return 0;
		}
		
		return finalDamageAmount;
	}
	
	// Use this method for the damage preview
	public int calculateDamagePreview() {
		int effectiveBonus = 1; 				// change this later. Example: Bow vs flying = 3
		
		// Damage
		int baseAttack = 0;
		int baseDefence = 0;
		if (attackingItem.itemType.equals(ItemType.PHYSICAL)) {
			baseAttack = attackingUnitStats.str;
			baseDefence = defendingUnitStats.def;
		} else if (attackingItem.itemType.equals(ItemType.MAGIC)) {
			baseAttack = attackingUnitStats.magic;
			baseDefence = defendingUnitStats.res;
		}
		
		int attackDamage = baseAttack + ((attackingItem.might + getWeaponBonus()) * effectiveBonus);
		int defenceReduction = baseDefence + (int) defendingMovementStat.currentCell.defenceBonus; 
		
		int finalDamageAmount = attackDamage - defenceReduction;
		if (finalDamageAmount < 0) {
			return 0;
		}
		
		return finalDamageAmount;
	}
	
	// Utility functions
	private void getComponents() {
		attackingUnitStats = uComponentMapper.get(attackingUnit);
		defendingUnitStats = uComponentMapper.get(defendingUnit);
		attackingInventory = invComponentMapper.get(attackingUnit);
		defendingInventory = invComponentMapper.get(defendingUnit);
		attackingItem = iComponentMapper.get(attackingInventory.selectedItem);
		defendingItem = iComponentMapper.get(defendingInventory.selectedItem);
		defendingMovementStat = mComponentMapper.get(defendingUnit);
	}
	
	public void clearEntities() {
		this.attackingUnit = null;
		this.defendingUnit = null;
		this.attackingUnitStats = null;
		this.defendingUnitStats = null;
		this.attackingInventory = null;
		this.defendingInventory = null;
		this.attackingItem = null;
		this.defendingItem = null;
		this.defendingMovementStat = null;
	}
	
	public void setUnits(Entity attackingUnit, Entity defendingUnit) {
		clearEntities();
		this.attackingUnit = attackingUnit;
		this.defendingUnit = defendingUnit;
		getComponents();
	}
	
	public int getWeaponBonus() {
		if (attackingItem.weaponClass.strongAgainst.equals(defendingItem.weaponClass.itemClass)) {
			return 1;
		}
		
		if (defendingItem.weaponClass.strongAgainst.equals(attackingItem.weaponClass.itemClass)) {
			return -1;
		}
		
		return 0;
	}
	
	public Entity getAttackingUnit() {
		return attackingUnit;
	}
	
	public Entity getDefendingUnit() {
		return defendingUnit;
	}

	public UnitStatsComponent getAttackingUnitStats() {
		return attackingUnitStats;
	}

	public InventoryComponent getAttackingInventory() {
		return attackingInventory;
	}

	public InventoryComponent getDefendingInventory() {
		return defendingInventory;
	}

	public MovementStatsComponent getDefendingMovementStat() {
		return defendingMovementStat;
	}

	public ItemComponent getAttackingItem() {
		return attackingItem;
	}

	public ItemComponent getDefendingItem() {
		return defendingItem;
	}
}