package com.jb.fe.components;

import com.badlogic.ashley.core.Component;

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
	
	// Class
	public ClassList unitClass;
	
	// Unit Level
	public int Level;
	public int currentExperience;
	public final int experiencedToNextLevel = 100;
	
	public UnitStatsComponent() {}
	
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
	}
	
	// Cavalier
	public void setCavalier() {
		health = 20;
		maxHealth = 20;
		str = 7;
		skill = 5;
		speed = 7;
		magic = 2;
		luck = 2;
		def = 6;
		res = 1;
	}
	
	// Knight
	public void setKnight() {
		
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
}
