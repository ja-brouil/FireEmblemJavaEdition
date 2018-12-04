package com.jb.fe.UI.inventory;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.jb.fe.UI.soundTemp.UISounds;
import com.jb.fe.components.InventoryComponent;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.components.UIComponent;
import com.jb.fe.components.UIComponent.InputHandling;
import com.jb.fe.systems.inputAndUI.UIManager;

public class InventoryInputHandle implements InputHandling{
	
	private float keyDelay = 0.08f;
	private float currentDelay;
	
	private Entity hand;
	public Entity unitDamagePreview;
	private UIComponent uiComponent;
	private InventoryMenuBox inventoryMenuBox;
	
	public static int itemSelectionNumber;
	
	private ComponentMapper<PositionComponent> pComponentMapper = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<InventoryComponent> iComponentMapper = ComponentMapper.getFor(InventoryComponent.class);
	
	public InventoryInputHandle(Entity hand, UIComponent uiComponent, InventoryMenuBox inventoryMenuBox, Entity unitDamagePreview) {
		this.unitDamagePreview = unitDamagePreview;
		this.hand = hand;
		this.uiComponent = uiComponent;
		this.inventoryMenuBox = inventoryMenuBox;
		itemSelectionNumber = 0;
	}

	@Override
	public void handleInput() {
		
		currentDelay += Gdx.graphics.getDeltaTime();
		if (currentDelay <= keyDelay) { return; }
		InventoryComponent inventoryComponent = iComponentMapper.get(UIManager.currentGameUnit);
		
		// Up
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			itemSelectionNumber--;
			if (allowHandMovement()) {
				pComponentMapper.get(hand).y += 20;
				currentDelay = 0;
				// Set new equip item
				inventoryComponent.selectedItem = inventoryComponent.inventory[itemSelectionNumber];
				UIComponent.soundSystem.playSound(UISounds.movement);
			}
		}
		
		// Down
		if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			itemSelectionNumber++;
			if (allowHandMovement()) {
				pComponentMapper.get(hand).y -= 20;
				currentDelay = 0;
				inventoryComponent.selectedItem = inventoryComponent.inventory[itemSelectionNumber];
				UIComponent.soundSystem.playSound(UISounds.movement);
			}
		}
		
		
		// A
		if (Gdx.input.isKeyJustPressed(Keys.Z)) {
			UIComponent.soundSystem.playSound(UISounds.accept);
			unitDamagePreview.getComponent(UIComponent.class).inputHandling.turnOn();
			uiComponent.uiManager.setCurrentUI(unitDamagePreview);
			uiComponent.inputIsEnabled = false;
			uiComponent.updateIsEnabled = false;
			inventoryMenuBox.turnOff();
			currentDelay = 0;
		}
		
		// B
		if (Gdx.input.isKeyJustPressed(Keys.X)) {
			// Cancel and go back to the action menu
			uiComponent.inputIsEnabled = false;
			uiComponent.updateIsEnabled = false;
			uiComponent.uiManager.startActionMenu();
			inventoryMenuBox.turnOff();
			UIComponent.soundSystem.playSound(UISounds.back);
			currentDelay = 0;
		}
	}
	
	private boolean allowHandMovement() {
		// If there is only 1 item, no movement allowed
		if (iComponentMapper.get(UIManager.currentGameUnit).amountOfItemsCarried <= 1) {
			return false;
		}
		
		if (itemSelectionNumber < 0) {
			itemSelectionNumber = 0;
			return false;
		}
		
		if (itemSelectionNumber >= iComponentMapper.get(UIManager.currentGameUnit).amountOfItemsCarried) {
			itemSelectionNumber--;
			return false;
		}
		
		return true;
	}
	
	public void turnOn() {}
}
