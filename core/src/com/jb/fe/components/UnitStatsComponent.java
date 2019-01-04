package com.jb.fe.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.maps.MapProperties;

public class UnitStatsComponent implements Component{

	// Unit Stats
	public int health;
	public int maxHealth;
	
	public int str; 		// physical damage
	public int skill;		// accuracy rate
	public int speed;		// steal and double attack rate and avoidance rate
	public int magic;		// magic damage
	public int luck;		// critical hit rate, adds to avoidance rate
	public int def;			// physical damage defence
	public int res;			// magical damage defence
	public int consti;		// allows to weild heavier weapons
	
	// Class
	public ClassList unitClass;
	
	// Bonuses
	public int bonusCrit;
	public int bonusDodge;
	public int bonusHit;
	
	// Unit Level
	public int Level;
	public int currentExperience;
	public final int experiencedToNextLevel = 100;
	
	public UnitStatsComponent() {}
	
	public UnitStatsComponent(MapProperties unitProperties) {
		health = unitProperties.get("Health", Integer.class);
		maxHealth = unitProperties.get("MaxHealth", Integer.class);
		str = unitProperties.get("Str", Integer.class);
		skill = unitProperties.get("Skill", Integer.class);
		speed = unitProperties.get("Speed", Integer.class);
		magic = unitProperties.get("Magic", Integer.class);
		luck = unitProperties.get("Luck", Integer.class);;
		def = unitProperties.get("Defense", Integer.class);;
		res = unitProperties.get("Res", Integer.class);;
		consti = unitProperties.get("Consti", Integer.class);
		bonusCrit = unitProperties.get("BonusCrit", Integer.class);
		bonusDodge = unitProperties.get("BonusDodge", Integer.class);
		bonusHit = unitProperties.get("BonusHit", Integer.class);
	}
	
	// Stat templates
	// Eirika
	public void setEirika() {
		health = 16;
		maxHealth = 16;
		str = 4;
		skill = 8;
		speed = 8;
		magic = 4;
		luck = 5;
		def = 3;
		res = 1;
		consti = 5;
		bonusCrit = 5;
		bonusDodge = 5;
		bonusHit = 5;
	}
	
	public static enum ClassList {
		LORD,
		CAVALIER,
		KNIGHT,
		PRIEST,
		BANDIT,
		THIEF,
		ARCHER
	}
	
	@Override
	public String toString() {
		return "Current Health: " + health +
				"\nMax Health: " + maxHealth
				+ "\nStr: " + str +
				"\nSkill: " + skill+
				"\nSpeed: " + speed+
				"\nMagic: " + magic+
				"\nLuck: " + luck +
				"\nDef: " + def +
				"\nRes: " + res +
				"\nConsti: " + consti +
				"\nBonus Crit: " + bonusCrit +
				"\nBonus Dodge: " + bonusDodge +
				"\nBonus Hit: " + bonusHit;
	}
}
