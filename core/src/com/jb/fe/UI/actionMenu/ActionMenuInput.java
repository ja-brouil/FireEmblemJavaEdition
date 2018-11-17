package com.jb.fe.UI.actionMenu;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.jb.fe.components.InputComponent.InputHandling;

public class ActionMenuInput implements InputHandling{

	private float currentDelayTime;
	private float delayTime;
	
	private Entity actionMenu;
	private Entity entityToProcess;
	
	public ActionMenuInput(Entity actionMenu) {
		this.actionMenu = actionMenu;
	}
	
	@Override
	public void handleInput() {
		currentDelayTime += Gdx.graphics.getDeltaTime();
		if (currentDelayTime < delayTime) {
			return;
		}
		
		// Up
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			
			currentDelayTime = 0;
		}
		
		// Down
		if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			
			currentDelayTime = 0;
		}
		
		// A Button
		if (Gdx.input.isKeyPressed(Keys.Z)) {
			
			currentDelayTime = 0;
		}
		
		// B Button
		if (Gdx.input.isKeyPressed(Keys.X)) {
			
			currentDelayTime = 0;
		}
	}
}
