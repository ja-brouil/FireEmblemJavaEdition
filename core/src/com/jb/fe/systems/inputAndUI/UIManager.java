package com.jb.fe.systems.inputAndUI;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.jb.fe.components.UIComponent;
import com.jb.fe.systems.SystemPriorityDictionnary;

/**
 * Controls the UI and user input
 * @author james
 *
 */
public class UIManager extends EntitySystem {
	
	// UI Elements for Battle field
	private Entity mapcursor;
	private Entity actionMenu;
	
	private Entity currentUI;
	
	private boolean pauseUI;
	
	// Game Unit that we are processing
	public static Entity currentGameUnit;
	
	private ComponentMapper<UIComponent> uComponentMapper = ComponentMapper.getFor(UIComponent.class);
	
	public UIManager() {
		priority = SystemPriorityDictionnary.HandleInputAndUI;
		pauseUI = false;
	}
	
	@Override
	public void update(float delta) {
			if (pauseUI) {
				return;
			}
	
			UIComponent uiComponent = uComponentMapper.get(currentUI);

			if (uiComponent.inputIsEnabled) {
				uiComponent.inputHandling.handleInput();
			}
			
			if (uiComponent.updateIsEnabled) {
				uiComponent.updateUI.updateUI(delta);
			}
	}
	
	// Helpers functions to pass data
	public void startActionMenu() {
		actionMenu.getComponent(UIComponent.class).inputIsEnabled = true;
		actionMenu.getComponent(UIComponent.class).updateIsEnabled = true;
		setCurrentUI(actionMenu);
	}
	
	public void startSystem() {
		currentUI = mapcursor;
	}
	
	public void setMapCursor(Entity mapCursor) {
		this.mapcursor = mapCursor;
	}
	
	public void setActionMenu(Entity actionMenu) {
		this.actionMenu = actionMenu;
	}
	
	public void setCurrentUI(Entity currentUI) {
		this.currentUI = currentUI;
	}
	
	public Entity getCurrentUIComponent() {
		return currentUI;
	}
	
	public Entity getMapCursor() {
		return mapcursor;
	}
	
	public Entity getActionMenu() {
		return actionMenu;
	}
	
	public boolean getPauseStatus() {
		return pauseUI;
	}
	
	public void setPauseStatus(boolean pauseUI) {
		this.pauseUI = pauseUI;
	}
}
