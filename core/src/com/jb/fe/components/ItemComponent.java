package com.jb.fe.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.JsonValue;
import com.jb.fe.components.WeaponClass.WeaponType;

public class ItemComponent implements Component{
	
	public WeaponClass weaponClass;
	public boolean isUsable; 	// Can be used by current class
	
	public int uses; 				// durability
	public int might;				// effectiveness | damage
	public int weight;				// affects chance to dodge
	public int hit;					// base hit chance
	public int crit;				// base crit chance
	public int maxRange;			// max range of the weapon
	public int minRange;			// min range of the weapon
	public int effectiveBonus; 		// various bonuses
	public ItemType itemType;	    // Magic or Physical or Usable
	
	public String weaponSoundName;
	
	public Effect specialEffect; // Anything special add a function here
	
	public boolean isBroken; 	// For pooling purposes
	
	public ItemComponent(JsonValue itemJsonValue) {
		uses = itemJsonValue.getInt("Uses");
		might = itemJsonValue.getInt("Might");
		hit = itemJsonValue.getInt("Hit");
		crit = itemJsonValue.getInt("Crit");
		maxRange = itemJsonValue.getInt("MaxRange");
		minRange = itemJsonValue.getInt("MinRange");
		weaponClass = new WeaponClass(getWeaponType(itemJsonValue.getString("ItemClass")));
		itemType = getItemType(itemJsonValue.getString("ItemType"));
		effectiveBonus = itemJsonValue.getInt("EffectiveBonus");
		weaponSoundName = itemJsonValue.getString("ItemSoundName");
		isBroken = false;
		isUsable = true;
	}
	
	private WeaponType getWeaponType(String weaponType) {
		if (weaponType.equalsIgnoreCase("Sword")) {
			return WeaponType.SWORD;
		} else if (weaponType.equalsIgnoreCase("Axe")) {
			return WeaponType.AXE;
		}  else if (weaponType.equalsIgnoreCase("Lance")) {
			return WeaponType.LANCE;
		}  else if (weaponType.equalsIgnoreCase("Bow")) {
			return WeaponType.BOW;
		}  else if (weaponType.equalsIgnoreCase("Dark")) {
			return WeaponType.DARK;
		}  else if (weaponType.equalsIgnoreCase("Elemental")) {
			return WeaponType.ELEMENTAL;
		}  else if (weaponType.equalsIgnoreCase("Healing")) {
			return WeaponType.HEALING;
		}  else if (weaponType.equalsIgnoreCase("Light")) {
			return WeaponType.LIGHT;
		}  else if (weaponType.equalsIgnoreCase("Staves")) {
			return WeaponType.STAVES;
		}  else if (weaponType.equalsIgnoreCase("Promotion")) {
			return WeaponType.PROMOTION;
		}  else if (weaponType.equalsIgnoreCase("No Weakness")) {
			return WeaponType.NO_WEAKNESS;
		}
		
		// Default fall through
		return WeaponType.NO_WEAKNESS;
	}
	
	private ItemType getItemType(String itemType) {
		if (itemType.equalsIgnoreCase("Physical")) {
			return ItemType.PHYSICAL;
		} else if (itemType.equalsIgnoreCase("Magic")) {
			return ItemType.MAGIC;
		} else if (itemType.equalsIgnoreCase("Usable")) {
			return ItemType.USABLE;
		}
		
		// Default fall through
		return ItemType.PHYSICAL;
	}
	
	public interface Effect {
		public void specialEffect();
	}
	
	public static enum ItemType {
		PHYSICAL, MAGIC, USABLE
	}
	
	@Override
	public String toString() {
		return "\nUses: " + uses +
				"\nMight: " + might +
				"\nHit: " + hit +
				"\nCrit: " + crit +
				"\nMaxRange: " + maxRange +
				"\nMinRange: " + minRange +
				"\nWeaponClass: " + weaponClass.toString() +
				"\nItemType: "+ itemType.toString() +
				"\nEffective Bonus: " + effectiveBonus;
	}
}