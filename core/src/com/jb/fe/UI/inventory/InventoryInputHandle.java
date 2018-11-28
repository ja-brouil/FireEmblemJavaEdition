package com.jb.fe.UI.inventory;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.jb.fe.components.InventoryComponent;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.components.UIComponent;
import com.jb.fe.components.UIComponent.InputHandling;
import com.jb.fe.systems.inputAndUI.UIManager;

public class InventoryInputHandle implements InputHandling{
	
	private float keyDelay = 0.08f;
	private float currentDelay;
	
	private Entity hand;
	private UIComponent uiComponent;
	
	public static int itemSelectionNumber;
	
	private ComponentMapper<PositionComponent> pComponentMapper = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<InventoryComponent> iComponentMapper = ComponentMapper.getFor(InventoryComponent.class);
	
	public InventoryInputHandle(Entity hand, UIComponent uiComponent) {
		this.hand = hand;
		this.uiComponent = uiComponent;
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
			}
		}
		
		// Down
		if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			itemSelectionNumber++;
			if (allowHandMovement()) {
				pComponentMapper.get(hand).y -= 20;
				currentDelay = 0;
				inventoryComponent.selectedItem = inventoryComponent.inventory[itemSelectionNumber];
			}
		}
		
		
		// A
		if (Gdx.input.isKeyJustPressed(Keys.Z)) {
			System.out.println("a");
			currentDelay = 0;
		}
		
		// B
		if (Gdx.input.isKeyJustPressed(Keys.X)) {
			// Cancel and go back to the action menu
			
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
}
