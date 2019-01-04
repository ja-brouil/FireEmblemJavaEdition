package com.jb.fe.systems.inputAndUI;

import java.util.HashMap;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.jb.fe.UI.UserInterfaceState;
import com.jb.fe.systems.SystemPriorityDictionnary;

public class UserInterfaceManager extends EntitySystem {
	
	// Current Unit selected
	public static Entity unitSelected;
	
	public HashMap<String, UserInterfaceState> allUserInterfaceStates;
	public UserInterfaceState currentState;
	
	// User Interface Menu
	public UserInterfaceManager() {
		allUserInterfaceStates = new HashMap<>();
		priority = SystemPriorityDictionnary.HandleInputAndUI;	
	}
	
	@Override
	public void update(float delta) {
		currentState.handleInput(delta);
		currentState.update(delta);
	}
	
	/**
	 * Use this to change states
	 * @param previousState
	 * @param currentState
	 */
	public void setStates(UserInterfaceState previousState, UserInterfaceState currentState) {
		this.currentState = currentState;
		previousState.resetState();
		currentState.startState();
	}
	
	public void startSystem() {
		currentState = allUserInterfaceStates.get("MapCursor");
	}
	
	/**
	 * Pauses or resumes the UI
	 */
	public void pauseUI() {
		if (checkProcessing()) {
			setProcessing(false);
			currentState.resetState();
		} else {
			setProcessing(true);
			currentState.startState();
		}
		
	}
}
