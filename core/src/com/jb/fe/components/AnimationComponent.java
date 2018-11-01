package com.jb.fe.components;

import java.util.HashMap;

import com.badlogic.ashley.core.Component;
/**
 * Wrapper class for Animation objects for components
 * @author james
 *
 */
public class AnimationComponent implements Component{
	
	// All Animations
	public HashMap<String, AnimationObject> allAnimationObjects;
	
	// Animation to draw
	public AnimationObject currentAnimation;
	
	// Set this for sorting
	public int zOrder;
	
	public AnimationComponent() {
		allAnimationObjects = new HashMap<>();
		zOrder = 0;
	}
	
	public AnimationComponent(int zOrder) {
		this();
		this.zOrder = zOrder;
	}
	
}
