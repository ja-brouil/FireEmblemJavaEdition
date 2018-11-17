package com.jb.fe.components;

import com.badlogic.ashley.core.Component;

public class UIComponent implements Component{

	public boolean isEnabled;
	public UpdateUI updateUI;
	
	public UIComponent() {
		isEnabled = true;
	}
	
	public UIComponent(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	
	public interface UpdateUI {
		public void updateUI(float delta);
	}
}
