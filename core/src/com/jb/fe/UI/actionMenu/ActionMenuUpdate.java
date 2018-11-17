package com.jb.fe.UI.actionMenu;

import com.badlogic.ashley.core.Entity;
import com.jb.fe.components.UIComponent.UpdateUI;

public class ActionMenuUpdate implements UpdateUI{

	private Entity actionMenu;
	
	public ActionMenuUpdate(Entity actionMenu) {
		this.actionMenu = actionMenu;
	};
	
	@Override
	public void updateUI(float delta) {
		
	}
	
	private void getOptions() {
		// Check if there are enemies that you can attack
	}
	
	public static enum Action_Menu_Options {
		Attack, Items, Trade, Wait;
	}
}
