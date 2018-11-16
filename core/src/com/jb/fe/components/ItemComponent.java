package com.jb.fe.components;

import com.badlogic.ashley.core.Component;

public class ItemComponent implements Component{
	
	public ItemType itemType;
	public boolean isUsable; // Can be used by current class
	
	public int uses; 		// durability
	public int might;		// effectiveness
	public int weight;		// affects chance to dodge
	public int hit;			// base hit chance
	public int crit;		// base crit chance
	public int maxRange;	// max range of the weapon
	public int minRange;	// min range of the weapon
	
	public String weaponSoundName;
	
	public Effect specialEffect; // Anything special add a function here
	
	public boolean isBroken; // For pooling purposes
	
	// Iron Sword default
	public ItemComponent() {
		uses = 45;
		might = 5;
		hit = 90;
		crit = 0;
		maxRange = 1;
		minRange = 1;
		itemType = null;
		weaponSoundName = "Iron Sword Sound";
		isBroken = false;
		isUsable = true;
	}
	
	public interface Effect {
		public void specialEffect();
	}
}

// Wrapper for item classes
class ItemType {
	
	public enum ItemClass {
		SWORD,
		AXE,
		SPEAR,
		ELEMENTAL,
		LIGHT,
		DARK,
		STAVES,
		HEALING,
		PROMOTION,
		BOW,
		NO_WEAKNESS
	}
	
	public ItemClass itemType;
	public ItemClass oppositeType;
	
	public ItemType(ItemClass itemClass) {
		itemType = itemClass;
		
		getOpposite();
	}
	
	public void getOpposite() {
		switch (itemType) {
		case SWORD:
			
			break;
		case AXE:
			
			break;
		case SPEAR:
			
			break;
		case ELEMENTAL:
			
			break;
		default:
			
			break;
		}
	}
}