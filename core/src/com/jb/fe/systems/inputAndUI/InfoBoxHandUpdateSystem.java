package com.jb.fe.systems.inputAndUI;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;

/**
 * Controls the hand and menu boxes
 * @author JamesBrouillet
 *
 */
public class InfoBoxHandUpdateSystem extends EntitySystem{

	// UI elements
	private Entity mapCursor;
	
	// Boxes
	
	// Hand
	private Entity hand;
	
	public InfoBoxHandUpdateSystem(Entity hand) {
		this.hand = hand;
	}
	
	@Override
	public void update(float delta) {
		
	}
}
