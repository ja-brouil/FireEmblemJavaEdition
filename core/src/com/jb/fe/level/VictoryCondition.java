package com.jb.fe.level;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.jb.fe.map.MapCell;

public class VictoryCondition {
	public static enum VictoryConditionType {
		ROUT, SEIZE, SURVIVE, ESCORT, ASSASSINATION;
	}
	
	public VictoryConditionType victoryConditionType;
	
	// Rout
	public Array<Entity> allEnemies;
	
	// Seize
	public MapCell throneCell;
	
	// Survive
	public int numberOfTurnsToSurvive;
	public int currentNumberOfTurns;
	
	// Escort
	public Entity unitToEscort;
	public MapCell destinationCellToEscort;
	
	// Assassination
	public Entity entityToAssassinate;
	public int daysLeftToAssassinate;
	
	public VictoryCondition() {
		victoryConditionType = VictoryConditionType.SEIZE;
	}
}
