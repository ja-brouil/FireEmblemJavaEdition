package com.jb.fe.components;

import com.badlogic.ashley.core.Component;

public class TransitionMethodComponent implements Component{
	
	public boolean isActive;
	public TransitionType transitionType;
	
	public float timer;
	public float maxTimer;
	
	public float speed;
	
	public TransitionMethodComponent(float maxTimer) {
		isActive = false;
		transitionType = TransitionType.LEFT_TO_RIGHT;
		this.maxTimer = maxTimer;
	}
	
	public enum TransitionType {
		FADE_TO_BLACK,
		LEFT_TO_RIGHT
	}
}
