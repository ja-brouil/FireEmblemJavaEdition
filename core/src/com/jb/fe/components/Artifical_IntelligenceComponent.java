package com.jb.fe.components;

import com.badlogic.ashley.core.Component;

public class Artifical_IntelligenceComponent implements Component{

	// Active
	public boolean isProcessing;
	
	// AI Type
	public AI_TYPE ai_Type;
	
	public Artifical_IntelligenceComponent() {
		isProcessing = true;
		ai_Type = AI_TYPE.AGGRESSIVE;
	}
	
	// Locate all enemies
	
	public enum AI_TYPE {
		PASSIVE,
		AGGRESSIVE,
	}
}
