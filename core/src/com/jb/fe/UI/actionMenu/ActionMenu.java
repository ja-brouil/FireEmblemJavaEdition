package com.jb.fe.UI.actionMenu;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.MathUtils;
import com.jb.fe.UI.UserInterfaceState;
import com.jb.fe.UI.Text.TextObject;
import com.jb.fe.UI.factories.UIFactory;
import com.jb.fe.UI.soundTemp.UISounds;
import com.jb.fe.components.MovementStatsComponent;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.components.TextComponent;
import com.jb.fe.components.MovementStatsComponent.Unit_State;
import com.jb.fe.screens.FireEmblemGame;
import com.jb.fe.systems.audio.SoundSystem;
import com.jb.fe.systems.inputAndUI.UserInterfaceManager;
import com.jb.fe.systems.movement.UnitMapCellUpdater;
import com.jb.fe.systems.movement.UnitMovementSystem;

public class ActionMenu extends UserInterfaceState {
	
	// Entities
	private Entity hand;
	private Entity actionMenu;
	private Entity mapCursor;
	
	// Options
	private int currentOption;
	private Action_Menu_Options[] allActionMenuOptions;
	
	// Timers
	private float currentDelayTime;
	private float keyDelayTime;

	// Unit Map Cell Updater
	private UnitMapCellUpdater unitMapCellUpdater;
	
	public ActionMenu(AssetManager assetManager, SoundSystem soundSystem, UserInterfaceManager userInterfaceManager, UnitMapCellUpdater unitMapCellUpdater,
			Entity mapCursor, Engine engine) {
		super(assetManager, soundSystem, userInterfaceManager);
		this.unitMapCellUpdater = unitMapCellUpdater;
		this.mapCursor = mapCursor;
		
		currentDelayTime = 0;
		keyDelayTime = 0.08f;
		
		currentOption = 0;
		allActionMenuOptions = new Action_Menu_Options[4];
		allActionMenuOptions[0] = Action_Menu_Options.Action;
		allActionMenuOptions[1] = Action_Menu_Options.Items;
		allActionMenuOptions[2] = Action_Menu_Options.Trade;
		allActionMenuOptions[3] = Action_Menu_Options.Wait;
		
		hand = UIFactory.createHand(assetManager);
		actionMenu = UIFactory.createActionMenu(engine.getSystem(UnitMovementSystem.class), unitMapCellUpdater, assetManager);
		engine.addEntity(hand);
		engine.addEntity(actionMenu);
	}

	@Override
	public void startState() {
		TextComponent textComponent = tComponentMapper.get(actionMenu);
		
		PositionComponent actionMenuPositionComponent = pComponentMapper.get(actionMenu);
		PositionComponent selectedUnitPosition = pComponentMapper.get(UserInterfaceManager.unitSelected);
		PositionComponent handPositionComponent = pComponentMapper.get(hand);
		
		staticImageComponentMapper.get(actionMenu).isEnabled = true;
		staticImageComponentMapper.get(hand).isEnabled = true;
		textComponent.isDrawing = true;
		
		handPositionComponent.x = actionMenuPositionComponent.x - 5;
		handPositionComponent.y = 121;
		
		// Change this with camera view once camera has been implemented | Might need to change sizes here later depending on options
		if (selectedUnitPosition.x <= FireEmblemGame.WIDTH / 2) {
			actionMenuPositionComponent.x = FireEmblemGame.WIDTH - (FireEmblemGame.WIDTH / 5) - 10;
		} else {
			actionMenuPositionComponent.x = 10;
		}
		
		for (int i = 0; i < textComponent.textArray.size; i++) {
			TextObject actionMenuTextObject = textComponent.textArray.get(i);
			actionMenuTextObject.x = actionMenuPositionComponent.x + 11.75f;
			actionMenuTextObject.y = actionMenuPositionComponent.y + (i * 15) + 16;
		}
		
		// Reset Option
		currentOption = 0;
	}

	@Override
	public void resetState() {
		staticImageComponentMapper.get(actionMenu).isEnabled = false;
		staticImageComponentMapper.get(hand).isEnabled = false;
		tComponentMapper.get(actionMenu).isDrawing = false;
	}

	@Override
	public void nextState() {
		// Check which option was selected
		if (allActionMenuOptions[currentOption] == Action_Menu_Options.Action) {
			if (iComponentMapper.get(UserInterfaceManager.unitSelected).amountOfItemsCarried == 0) {
				soundSystem.playSound(UISounds.invalid);
				return;
			} else {
				// Send to Inventory Menu
				soundSystem.playSound(UISounds.accept);
				userInterfaceManager.setStates(this, userInterfaceManager.allUserInterfaceStates.get("InventoryMenu"));
			}
		} else if (allActionMenuOptions[currentOption] == Action_Menu_Options.Items) {
			// Items | for now just do nothing
			soundSystem.playSound(UISounds.accept);
		} else  if (allActionMenuOptions[currentOption] == Action_Menu_Options.Trade) {
			// Trade options | for now just do nothing
			soundSystem.playSound(UISounds.accept);
		} else if (allActionMenuOptions[currentOption] == Action_Menu_Options.Wait) {
			soundSystem.playSound(UISounds.accept);
			mStatComponentMapper.get(UserInterfaceManager.unitSelected).unit_State = Unit_State.DONE;
			UserInterfaceManager.unitSelected = null;
			unitMapCellUpdater.updateCellInfo();
			
			userInterfaceManager.setStates(this, userInterfaceManager.allUserInterfaceStates.get("MapCursor"));
		}
	}

	@Override
	public void handleInput(float delta) {
		currentDelayTime += delta;
		if (currentDelayTime <= keyDelayTime) { return; }
		
		// Up | Down
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			pComponentMapper.get(hand).y += 15;
			currentDelayTime = 0;
			currentOption--;
			if (currentOption >= 0) {
				soundSystem.playSound(UISounds.movement);
			}
			currentOption = MathUtils.clamp(currentOption, 0, 3);
			preventHandOutOfBounds(pComponentMapper.get(hand));
		} else if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			pComponentMapper.get(hand).y -= 15;
			currentDelayTime = 0;
			currentOption++;
			if (currentOption <= 3) {
				soundSystem.playSound(UISounds.movement);
			}
			currentOption = MathUtils.clamp(currentOption, 0, 3);
			preventHandOutOfBounds(pComponentMapper.get(hand));
		} else if (Gdx.input.isKeyJustPressed(Keys.X)) {
			// Return to cursor movement
			soundSystem.playSound(UISounds.back);
			
			// Return Unit back to original spot
			MovementStatsComponent movementStatsComponent = mStatComponentMapper.get(UserInterfaceManager.unitSelected);
			movementStatsComponent.currentCell = movementStatsComponent.previousCell;
			movementStatsComponent.unit_State = Unit_State.CAN_DO_BOTH;
			
			PositionComponent unitPositionComponent = pComponentMapper.get(UserInterfaceManager.unitSelected);
			unitPositionComponent.x = movementStatsComponent.currentCell.position.x;
			unitPositionComponent.y = movementStatsComponent.currentCell.position.y;
			
			// Move Map Cursor to unitPosition
			PositionComponent cursorPositionComponent = pComponentMapper.get(mapCursor);
			cursorPositionComponent.x = unitPositionComponent.x;
			cursorPositionComponent.y = unitPositionComponent.y;
			unitMapCellUpdater.updateCellInfo();
			
			// Set State
			userInterfaceManager.setStates(this, userInterfaceManager.allUserInterfaceStates.get("MapCursor"));
			
		} else if (Gdx.input.isKeyJustPressed(Keys.Z)) {
			nextState();
		}
	}
	
	// Menu Options
	public static enum Action_Menu_Options {
		Action, Items, Trade, Wait;
	}
	
	// Utilities
	private void preventHandOutOfBounds(PositionComponent positionComponent) {
		if (positionComponent.y < 76) {
			positionComponent.y = 76;
		}
		
		if (positionComponent.y > 121) {
			positionComponent.y = 121;
		}
	}
	
	public Entity getHandEntity() {
		return hand;
	}

}