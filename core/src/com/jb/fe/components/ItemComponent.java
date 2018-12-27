package com.jb.fe.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.MathUtils;
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
	
	// Iron Sword default
	public ItemComponent() {
		uses =  45; // 45 + MathUtils.random(-10, 10);
		might = 5 + MathUtils.random(0, 3);
		hit = 90 + MathUtils.random(0, 10);
		crit = 0 + MathUtils.random(0, 10);
		maxRange = 1;
		minRange = 1;
		weaponClass = new WeaponClass(WeaponType.SWORD);
		itemType = ItemType.PHYSICAL;
		effectiveBonus = 1;
		weaponSoundName = "Iron Sword Sound";
		isBroken = false;
		isUsable = true;
	}
	
	public interface Effect {
		public void specialEffect();
	}
	
	public static enum ItemType {
		PHYSICAL, MAGIC, USABLE
	}
}