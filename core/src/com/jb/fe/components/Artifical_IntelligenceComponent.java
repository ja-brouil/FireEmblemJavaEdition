package com.jb.fe.components;

import com.badlogic.ashley.core.Component;

public class Artifical_IntelligenceComponent implements Component{

	// Active
	public boolean isActive;
	
	//
	public AI_TYPE ai_Type;
	
	public Artifical_IntelligenceComponent() {
		isActive = true;
	}
	
	// Locate all enemies
	
	public enum AI_TYPE {
		PASSIVE,
		AGGRESSIVE,
	}
}
