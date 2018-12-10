package com.jb.fe.UI.actionMenu;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.jb.fe.UI.inventory.InventoryMenuBox;
import com.jb.fe.components.AnimationComponent;
import com.jb.fe.components.InventoryComponent;
import com.jb.fe.components.MapCursorStateComponent;
import com.jb.fe.components.MapCursorStateComponent.MapCursorState;
import com.jb.fe.components.MovementStatsComponent;
import com.jb.fe.components.MovementStatsComponent.Unit_State;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.components.SoundComponent;
import com.jb.fe.components.StaticImageComponent;
import com.jb.fe.components.TextComponent;
import com.jb.fe.components.UIComponent;
import com.jb.fe.components.UIComponent.UpdateUI;
import com.jb.fe.screens.FireEmblemGame;
import com.jb.fe.systems.inputAndUI.UIManager;

public class ActionMenuUpdate implements UpdateUI {
	
	private UIComponent uiComponent;
	private Entity actionMenu;
	private Entity hand;
	private Entity mapCursor;
	private InventoryMenuBox inventoryMenuBox;
	
	private ComponentMapper<PositionComponent> pComponentMapper = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<StaticImageComponent> sComponentMapper = ComponentMapper.getFor(StaticImageComponent.class);
	private ComponentMapper<TextComponent> tComponentMapper = ComponentMapper.getFor(TextComponent.class);
	private ComponentMapper<AnimationComponent> aComponentMapper = ComponentMapper.getFor(AnimationComponent.class);
	
	public static Action_Menu_State action_Menu_State = Action_Menu_State.Idle;
	public static Action_Menu_Options curren_Action_Menu_Options;
	
	public ActionMenuUpdate(UIComponent uiComponent, Entity actionMenu, Entity hand, Entity mapCursor, InventoryMenuBox inventoryMenuBox) {
		this.uiComponent = uiComponent;
		this.actionMenu = actionMenu;
		this.hand = hand;
		this.mapCursor = mapCursor;
		this.inventoryMenuBox = inventoryMenuBox;
	};
	
	@Override
	public void updateUI(float delta) {
		if (action_Menu_State.equals(Action_Menu_State.Idle)) {
			sComponentMapper.get(hand).isEnabled = true;
			// Set Position
			PositionComponent actionMenuPositionComponent = pComponentMapper.get(actionMenu);
			PositionComponent selectedUnit = pComponentMapper.get(UIManager.currentGameUnit);
			PositionComponent handPosition = pComponentMapper.get(hand); 
			handPosition.x = actionMenuPositionComponent.x - 5;
			
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
			
			// Prevent Hand from going out of bounds
			preventHandOutOfBounds(handPosition);
			
			// Set Size depending on options
			// add size change here
			sComponentMapper.get(actionMenu).isEnabled = true;
		} else if (action_Menu_State.equals(Action_Menu_State.Process)) {
			sComponentMapper.get(hand).isEnabled = false;
			doAction(curren_Action_Menu_Options, mapCursor);
			action_Menu_State = Action_Menu_State.Idle;
		}
	}
	
	private void preventHandOutOfBounds(PositionComponent positionComponent) {
		if (positionComponent.y < 76) {
			positionComponent.y = 76;
		}
		
		if (positionComponent.y > 121) {
			positionComponent.y = 121;
		}
	}
	
	public void doAction(Action_Menu_Options action_Menu_Options, Entity mapCursor) {
		if (action_Menu_Options.equals(Action_Menu_Options.Action)) {
			processAction(action_Menu_Options);
		} else if (action_Menu_Options.equals(Action_Menu_Options.Items)) {
			processItems(mapCursor, action_Menu_Options);
		} else if (action_Menu_Options.equals(Action_Menu_Options.Trade)) {
			processTrade(mapCursor, action_Menu_Options);
		} else if (action_Menu_Options.equals(Action_Menu_Options.Wait)) {
			processWait(mapCursor, action_Menu_Options);
		}
	}
	
	private void processAction(Action_Menu_Options action_Menu_Options) {
		// If we have no items, don't do anything
		if (UIManager.currentGameUnit.getComponent(InventoryComponent.class).amountOfItemsCarried <= 0) {
			action_Menu_State = Action_Menu_State.Idle;
			UIComponent.soundSystem.playSound(mapCursor.getComponent(SoundComponent.class).allSoundObjects.get("Invalid"));
		} else {
			turnOff();
			inventoryMenuBox.setUnit(UIManager.currentGameUnit);
			UIComponent.soundSystem.playSound(mapCursor.getComponent(SoundComponent.class).allSoundObjects.get("Accept"));
			inventoryMenuBox.turnOn();
			uiComponent.uiManager.setCurrentUI(inventoryMenuBox.getBoxEntity());
		}
	}
	
	private void processTrade(Entity mapCursor, Action_Menu_Options action_Menu_Options) {
		System.out.println("TRADE SELECTED");
		turnOffMenu(mapCursor, action_Menu_Options);
	}
	
	private void processItems(Entity mapCursor, Action_Menu_Options action_Menu_Options) {
		System.out.println("ITEMS SELECTED");
		turnOffMenu(mapCursor, action_Menu_Options);
	}
	
	private void processWait(Entity mapCursor, Action_Menu_Options action_Menu_Options) {
		turnOffMenu(mapCursor, action_Menu_Options);
	}
	
	// Helper DEBUG FUNCTION
	private void turnOffMenu(Entity mapCursor, Action_Menu_Options action_Menu_Options) {

		UIManager.currentGameUnit.getComponent(MovementStatsComponent.class).unit_State = Unit_State.DONE;
		
		// Map Cursor
		mapCursor.getComponent(MapCursorStateComponent.class).mapCursorState = MapCursorState.MOVEMENT_ONLY;
		aComponentMapper.get(mapCursor).currentAnimation.isDrawing = true;
		
		// Finish animation
		aComponentMapper.get(UIManager.currentGameUnit).currentAnimation = aComponentMapper.get(UIManager.currentGameUnit).allAnimationObjects.get("Idle");
		
		// Play accept sound
		UIComponent.soundSystem.playSound(mapCursor.getComponent(SoundComponent.class).allSoundObjects.get("Accept"));
		
		turnOff();
		uiComponent.uiManager.setCurrentUI(mapCursor);
		action_Menu_State = Action_Menu_State.Idle;
		
		// Reset Hand and selection
		pComponentMapper.get(hand).y = 121;
		ActionMenuInput.indexOption = 0;
	}
	
	public void turnOff() {
		sComponentMapper.get(actionMenu).isEnabled = false;
		sComponentMapper.get(hand).isEnabled = false;
		actionMenu.getComponent(TextComponent.class).isDrawing = false;
		uiComponent.inputIsEnabled = false;
		uiComponent.updateIsEnabled = false;
	}
	
	public static enum Action_Menu_Options {
		Action, Items, Trade, Wait;
	}
	
	public static enum Action_Menu_State {
		Idle, Process
	}
	
	public void setInventoryMenu(InventoryMenuBox inventoryMenuBox) {
		this.inventoryMenuBox = inventoryMenuBox;
	}
}
