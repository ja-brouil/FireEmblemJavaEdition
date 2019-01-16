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
import com.jb.fe.components.TextComponent;
import com.jb.fe.components.ZOrderComponent;
import com.jb.fe.systems.audio.SoundSystem;
import com.jb.fe.systems.camera.CameraSystem;
import com.jb.fe.systems.graphics.ZOrder;
import com.jb.fe.systems.inputAndUI.UserInterfaceManager;
/**
 * Box entity = item info
 * Other entity = inventory size
 * Displays the inventory menu state
 * @author jamesbrouillet
 *
 */

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
		unitInventoryComponent.selectedItemIndex = 0;
		unitInventoryComponent.selectedItem = unitInventoryComponent.inventory[unitInventoryComponent.selectedItemIndex];
		
		// Set new inventory
		tComponentMapper.get(inventoryMenuBox.getItemInvBoxEntity()).textArray.forEach((textObject) -> {
			textObject.isEnabled = false;
			textObject.text = "";
		});
		
		TextComponent textComponent = tComponentMapper.get(inventoryMenuBox.getItemInvBoxEntity());
		for (int i = 0; i < unitInventoryComponent.amountOfItemsCarried; i++) {
			textComponent.textArray.get(i).text = nComponentMapper.get(unitInventoryComponent.inventory[i]).name;
			textComponent.textArray.get(i).isEnabled = true;
			textComponent.textArray.get(i).y = (23 + ((5 - i) * 15) + 40) + (CameraSystem.cameraY - CameraSystem.yConstant);
		}
		
		// Box Dimensions
		staticImageComponentMapper.get(inventoryMenuBox.getItemInvBoxEntity()).height = 20 * unitInventoryComponent.amountOfItemsCarried;
		staticImageComponentMapper.get(inventoryMenuBox.getItemInvBoxEntity()).isEnabled = true;
		pComponentMapper.get(inventoryMenuBox.getItemInvBoxEntity()).y = (40 + (CameraSystem.cameraY - CameraSystem.yConstant)) + ((unitInventoryComponent.MAX_INVENTORY_SIZE - unitInventoryComponent.amountOfItemsCarried) * 16.8f);
		setTextInfo();
		
		// Turn everything on
		inventoryMenuBox.turnOn();
				
		// Hand
		staticImageComponentMapper.get(hand).isEnabled = true;
		hand.getComponent(ZOrderComponent.class).zOrder = ZOrder.UI_MIDDLE_LAYER;
		pComponentMapper.get(hand).x = pComponentMapper.get(inventoryMenuBox.getItemInvBoxEntity()).x - 5 + (CameraSystem.cameraX - CameraSystem.xConstant);
		pComponentMapper.get(hand).y = tComponentMapper.get(inventoryMenuBox.getItemInvBoxEntity()).textArray.get(0).y - 10 + (CameraSystem.cameraY - CameraSystem.yConstant);
	}

	@Override
	public void resetState() {
		staticImageComponentMapper.get(hand).isEnabled = false;
		inventoryMenuBox.turnOff();
	}

	@Override
	public void nextState() {
		userInterfaceManager.setStates(this, userInterfaceManager.allUserInterfaceStates.get("UnitDamagePreview"));
		soundSystem.playSound(UISounds.accept);
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
				setTextInfo();
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
				setTextInfo();
				soundSystem.playSound(UISounds.movement);
			}
		}

		// A
		if (Gdx.input.isKeyJustPressed(Keys.Z)) {
			currentDelay = 0;
			nextState();
		}

		// B
		if (Gdx.input.isKeyJustPressed(Keys.X)) {
			// Cancel and go back to the action menu
			soundSystem.playSound(UISounds.back);
			currentDelay = 0;
			userInterfaceManager.setStates(this, userInterfaceManager.allUserInterfaceStates.get("ActionMenu"));
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
		tComponentMapper.get(inventoryMenuBox.getBoxEntity()).textArray.get(3).text = "Atk " + Integer.toString(itemComponentMapper.get(unitInventoryComponent.selectedItem).might);
		tComponentMapper.get(inventoryMenuBox.getBoxEntity()).textArray.get(2).text = "Hit " + Integer.toString(itemComponentMapper.get(unitInventoryComponent.selectedItem).hit);
		tComponentMapper.get(inventoryMenuBox.getBoxEntity()).textArray.get(1).text = "Crit " + Integer.toString(itemComponentMapper.get(unitInventoryComponent.selectedItem).crit);
		tComponentMapper.get(inventoryMenuBox.getBoxEntity()).textArray.get(0).text = "Uses " + Integer.toString(itemComponentMapper.get(unitInventoryComponent.selectedItem).uses);
	}
}
