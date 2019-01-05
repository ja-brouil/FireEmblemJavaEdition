package com.jb.fe.UI.inventory;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.jb.fe.UI.UserInterfaceState;
import com.jb.fe.UI.factories.UIFactory;
import com.jb.fe.UI.soundTemp.UISounds;
import com.jb.fe.components.InventoryComponent;
import com.jb.fe.systems.audio.SoundSystem;
import com.jb.fe.systems.inputAndUI.UserInterfaceManager;

public class InventoryMenuState extends UserInterfaceState {

	private Entity hand;
	private InventoryMenuBox inventoryMenuBox;
	
	private float keyDelayTimer = 0.08f;
	private float currentDelay;

	private InventoryComponent unitInventoryComponent;

	public InventoryMenuState(AssetManager assetManager, SoundSystem soundSystem,
			UserInterfaceManager userInterfaceManager, Engine engine, Entity hand) {
		super(assetManager, soundSystem, userInterfaceManager);
		this.hand = hand;
		inventoryMenuBox = UIFactory.createInventoryMenu(assetManager, engine);
		
	}

	@Override
	public void startState() {
		unitInventoryComponent = iComponentMapper.get(UserInterfaceManager.unitSelected);
		unitInventoryComponent.selectedItem = unitInventoryComponent.inventory[unitInventoryComponent.selectedItemIndex];
	}

	@Override
	public void resetState() {

	}

	@Override
	public void nextState() {

	}

	@Override
	public void handleInput(float delta) {
		currentDelay += delta;
		if (currentDelay <= keyDelayTimer) {
			return;
		}

		// Up
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			unitInventoryComponent.selectedItemIndex--;
			if (allowHandMovement()) {
				pComponentMapper.get(hand).y += 20;
				currentDelay = 0;
				// Set new equip item
				unitInventoryComponent.selectedItem = unitInventoryComponent.inventory[unitInventoryComponent.selectedItemIndex];
				soundSystem.playSound(UISounds.movement);
			}
		}

		// Down
		if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			unitInventoryComponent.selectedItemIndex++;
			if (allowHandMovement()) {
				pComponentMapper.get(hand).y -= 20;
				currentDelay = 0;
				unitInventoryComponent.selectedItem = unitInventoryComponent.inventory[unitInventoryComponent.selectedItemIndex];
				soundSystem.playSound(UISounds.movement);
			}
		}

		// A
		if (Gdx.input.isKeyJustPressed(Keys.Z)) {
			nextState();
			
			/*
			soundSystem.playSound(UISounds.accept);
			unitDamagePreview.getComponent(UIComponent.class).inputHandling.turnOn();
			uiComponent.uiManager.setCurrentUI(unitDamagePreview);
			uiComponent.inputIsEnabled = false;
			uiComponent.updateIsEnabled = false;
			inventoryMenuBox.turnOff();
			currentDelay = 0;
			*/
		}

		// B
		if (Gdx.input.isKeyJustPressed(Keys.X)) {
			// Cancel and go back to the action menu
			/*
			uiComponent.inputIsEnabled = false;
			uiComponent.updateIsEnabled = false;
			uiComponent.uiManager.startActionMenu();
			inventoryMenuBox.turnOff();
			UIComponent.soundSystem.playSound(UISounds.back);
			currentDelay = 0;
			*/
		}
	}

	// Utilities
	private boolean allowHandMovement() {
		// If there is only 1 item, no movement allowed
		InventoryComponent inventoryComponent = iComponentMapper.get(UserInterfaceManager.unitSelected);
		if (inventoryComponent.amountOfItemsCarried <= 1) {

			if (inventoryComponent.selectedItemIndex < 0) {
				inventoryComponent.selectedItemIndex = 0;
				return false;
			}

			if (inventoryComponent.selectedItemIndex >= inventoryComponent.amountOfItemsCarried) {
				inventoryComponent.selectedItemIndex--;
				return false;
			}
			return false;
		}

		if (inventoryComponent.selectedItemIndex < 0) {
			inventoryComponent.selectedItemIndex = 0;
			return false;
		}

		if (inventoryComponent.selectedItemIndex >= inventoryComponent.amountOfItemsCarried) {
			inventoryComponent.selectedItemIndex--;
			return false;
		}

		return true;
	}
	
	private void setTextInfo() {
		tComponentMapper.get(inventoryMenuBox.getItemInvBoxEntity()).textArray.get(3).text = "Atk " + Integer.toString(itemComponentMapper.get(unitInventoryComponent.selectedItem).might);
		tComponentMapper.get(inventoryMenuBox.getItemInvBoxEntity()).textArray.get(2).text = "Hit " + Integer.toString(itemComponentMapper.get(unitInventoryComponent.selectedItem).might);
		tComponentMapper.get(inventoryMenuBox.getItemInvBoxEntity()).textArray.get(1).text = "Crit " + Integer.toString(itemComponentMapper.get(unitInventoryComponent.selectedItem).might);
		tComponentMapper.get(inventoryMenuBox.getItemInvBoxEntity()).textArray.get(0).text = "Uses " + Integer.toString(itemComponentMapper.get(unitInventoryComponent.selectedItem).might);
	}
}
