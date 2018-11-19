package com.jb.fe.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.jb.fe.systems.audio.SoundSystem;
import com.jb.fe.systems.inputAndUI.UIManager;

public class UIComponent implements Component {

	public boolean updateIsEnabled;
	public boolean inputIsEnabled;
	public UpdateUI updateUI;
	public InputHandling inputHandling;
	
	public UIManager uiManager;
	
	public Entity currentEntity;
	
	public SoundSystem soundSystem;
	
	public UIComponent(UIManager uiManager, SoundSystem soundSystem) {
		this.soundSystem = soundSystem;
		this.uiManager = uiManager;
		updateIsEnabled = true;
		inputIsEnabled = true;
	}
	
	public interface UpdateUI {
		public void updateUI(float delta);
	}
	
	public interface InputHandling{
		public void handleInput();
	}
}
