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
	
	// Bonuses
	public int bonusCrit;
	public int bonusDodge;
	public int bonusHit;
	
	// Unit Level
	public int Level;
	public int currentExperience;
	public final int experiencedToNextLevel = 100;
	
	// Stat upgrade probability
	public int strChance;
	public int skillChance;
	public int speedChance;
	public int magicChance;
	public int luckChance;
	public int defChance;
	public int resChance;
	public int constiChance;
	public int maxHPChance;
	
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
		
		// Ally vs Enemy
		if (unitProperties.get("isAlly", Boolean.class)) {
			strChance = unitProperties.get("strChance", Integer.class);
			skillChance = unitProperties.get("skillChance", Integer.class);
			speedChance = unitProperties.get("speedChance", Integer.class);
			magicChance = unitProperties.get("magicChance", Integer.class);
			luckChance = unitProperties.get("luckChance", Integer.class);
			defChance = unitProperties.get("defChance", Integer.class);
			resChance = unitProperties.get("resChance", Integer.class);
			constiChance = unitProperties.get("constiChance", Integer.class);
			maxHPChance = unitProperties.get("maxHPChance", Integer.class);
		} else {
			enemyDefaults();
		}

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
		strChance = 60;
		skillChance = 50;
		speedChance = 40;
		magicChance = 10;
		luckChance = 30;
		defChance = 30;
		resChance = 50;
		constiChance = 60;
		maxHPChance = 70;
	}
	
	private void enemyDefaults() {
		strChance = 0;
		skillChance = 0;
		speedChance = 0;
		magicChance = 0;
		luckChance = 0;
		defChance = 0;
		resChance = 0;
		constiChance = 0;
		maxHPChance = 0;
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
