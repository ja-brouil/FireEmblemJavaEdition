package com.jb.fe.systems.inputAndUI;

import java.util.HashMap;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.jb.fe.UI.UserInterfaceState;

public class UserInterfaceManager extends EntitySystem {
	
	// Current Unit selected
	public static Entity unitSelected;
	
	public HashMap<String, UserInterfaceState> allUserInterfaceStates;
	
	public UserInterfaceState previousState;
	public UserInterfaceState currentState;
	public UserInterfaceState nextState;
	
	// User Interface Menu
	public UserInterfaceManager() {
		allUserInterfaceStates = new HashMap<>();
	}
	
	@Override
	public void update(float delta) {
		currentState.update(delta);
	}
	
	public void setStates(UserInterfaceState previousState, UserInterfaceState currentState, UserInterfaceState nextState) {
		this.previousState = previousState;
		this.currentState = currentState;
		this.nextState = nextState;
	}
}
