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
	
	public AnimationComponent() {
		allAnimationObjects = new HashMap<>();

	}
	
}
