package com.jb.fe.components;

import com.badlogic.ashley.core.Component;

public class UnitStatsComponent implements Component {
	
	// Movement
	public int movementSteps;
	
	// Ally
	public boolean isAlly;
	
	// Default Stats
	public UnitStatsComponent() {
		movementSteps = 4;
		isAlly = true;
	}
	
}
