package com.jb.fe.UI.actionMenu;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.components.StaticImageComponent;
import com.jb.fe.components.TextComponent;
import com.jb.fe.components.UIComponent;
import com.jb.fe.components.UIComponent.UpdateUI;
import com.jb.fe.screens.FireEmblemGame;

public class ActionMenuUpdate implements UpdateUI {
	
	private UIComponent uiComponent;
	private Entity actionMenu;
	private Entity hand;
	
	private ComponentMapper<PositionComponent> pComponentMapper = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<StaticImageComponent> sComponentMapper = ComponentMapper.getFor(StaticImageComponent.class);
	private ComponentMapper<TextComponent> tComponentMapper = ComponentMapper.getFor(TextComponent.class);
	
	public ActionMenuUpdate(UIComponent uiComponent, Entity actionMenu, Entity hand) {
		this.uiComponent = uiComponent;
		this.actionMenu = actionMenu;
		this.hand = hand;
	};
	
	@Override
	public void updateUI(float delta) {
		// Set Position
		PositionComponent actionMenuPositionComponent = pComponentMapper.get(actionMenu);
		PositionComponent selectedUnit = pComponentMapper.get(uiComponent.currentEntity);
		pComponentMapper.get(hand).x = actionMenuPositionComponent.x - 3;
		
		// Change this to camera view once camera has been implemented
		if (selectedUnit.x <= FireEmblemGame.WIDTH / 2) {
			actionMenuPositionComponent.x = FireEmblemGame.WIDTH - (FireEmblemGame.WIDTH / 5) - 10;
		} else {
			actionMenuPositionComponent.x = 10;
		}
		
		// Set Text Position
		TextComponent textComponent = tComponentMapper.get(actionMenu);
		textComponent.isDrawing = true;
		for (int i = 0; i < textComponent.textArray.size; i++) {
			textComponent.textArray.get(i).x = actionMenuPositionComponent.x + 11.75f;
			textComponent.textArray.get(i).y = actionMenuPositionComponent.y + (i * 15) + 16;
		}
		
		// Set Size depending on options
		// add size change here
		sComponentMapper.get(actionMenu).isEnabled = true;
	}
	
	public static enum Action_Menu_Options {
		Attack, Items, Trade, Wait;
	}
}
