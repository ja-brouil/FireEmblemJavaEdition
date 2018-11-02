package com.jb.fe.components;

import com.badlogic.ashley.core.Component;
import com.jb.fe.map.MapCell;

public class UnitStatsComponent implements Component {
	
	// Movement
	public int movementSteps;
	public MapCell currentCell;
	
	// Ally
	public boolean isAlly;
	
	// Default Stats
	public UnitStatsComponent() {
		movementSteps = 4;
		isAlly = true;
		currentCell = null;
	}
	
}
