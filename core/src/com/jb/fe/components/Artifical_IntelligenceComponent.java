package com.jb.fe.components;

import com.badlogic.ashley.core.Component;

public class Artifical_IntelligenceComponent implements Component{

	// Active
	public boolean isProcessing;
	
	// Process combat
	public boolean shouldAttack;
	
	// AI Type
	public AI_TYPE ai_Type;
	
	public Artifical_IntelligenceComponent() {
		isProcessing = true;
		shouldAttack = false;
		ai_Type = AI_TYPE.AGGRESSIVE;
	}
	
	public enum AI_TYPE {
		PASSIVE,
		AGGRESSIVE,
	}
}
