package com.jb.fe.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.jb.fe.systems.audio.SoundSystem;
import com.jb.fe.systems.inputAndUI.ActionMenuMapCursorManager;

public class UIComponent implements Component {

	public boolean updateIsEnabled;
	public boolean inputIsEnabled;
	public UpdateUI updateUI;
	public InputHandling inputHandling;
	
	public ActionMenuMapCursorManager uiManager;
	
	public Entity currentEntity;
	
	public SoundSystem soundSystem;
	
	public UIComponent(ActionMenuMapCursorManager uiManager, SoundSystem soundSystem) {
		this.soundSystem = soundSystem;
		this.uiManager = uiManager;
		updateIsEnabled = true;
		inputIsEnabled = true;
	}
	
	public UIComponent() {
		updateIsEnabled = false;
		inputIsEnabled = false;
	}
	
	public interface UpdateUI {
		public void updateUI(float delta);
	}
	
	public interface InputHandling{
		public void handleInput();
	}
}
