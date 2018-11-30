package com.jb.fe.components;

import com.badlogic.ashley.core.Component;
import com.jb.fe.systems.audio.SoundSystem;
import com.jb.fe.systems.inputAndUI.UIManager;

public class UIComponent implements Component {

	public boolean updateIsEnabled;
	public boolean inputIsEnabled;
	public UpdateUI updateUI;
	public InputHandling inputHandling;
	
	public UIManager uiManager;
	public static SoundSystem soundSystem;
	
	public UIComponent(UIManager uiManager, SoundSystem soundSystem) {
		UIComponent.soundSystem = soundSystem;
		this.uiManager = uiManager;
		updateIsEnabled = true;
		inputIsEnabled = true;
	}
	
	public UIComponent(UIManager uiManager) {
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
