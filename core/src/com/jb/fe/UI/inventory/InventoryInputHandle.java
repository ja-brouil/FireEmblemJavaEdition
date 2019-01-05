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
	
	private ComponentMapper<PositionComponent> pComponentMapper = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<InventoryComponent> iComponentMapper = ComponentMapper.getFor(InventoryComponent.class);
	
	public InventoryInputHandle(Entity hand, UIComponent uiComponent, InventoryMenuBox inventoryMenuBox, Entity unitDamagePreview) {
		this.unitDamagePreview = unitDamagePreview;
		this.hand = hand;
		this.uiComponent = uiComponent;
		this.inventoryMenuBox = inventoryMenuBox;
	}

	@Override
	public void handleInput() {
		
		currentDelay += Gdx.graphics.getDeltaTime();
		if (currentDelay <= keyDelay) { return; }
		InventoryComponent inventoryComponent = iComponentMapper.get(UIManager.currentGameUnit);
		inventoryComponent.selectedItem = inventoryComponent.inventory[inventoryComponent.selectedItemIndex];
		
		
	}
	
	private boolean allowHandMovement() {
		// If there is only 1 item, no movement allowed
				InventoryComponent inventoryComponent = iComponentMapper.get(UIManager.currentGameUnit);
				if (iComponentMapper.get(UIManager.currentGameUnit).amountOfItemsCarried <= 1) {
					
					if (inventoryComponent.selectedItemIndex < 0) {
						inventoryComponent.selectedItemIndex = 0;
						return false;
					}
					
					if (inventoryComponent.selectedItemIndex >= iComponentMapper.get(UIManager.currentGameUnit).amountOfItemsCarried) {
						inventoryComponent.selectedItemIndex--;
						return false;
					}
					return false;
				}
				
				if (inventoryComponent.selectedItemIndex < 0) {
					inventoryComponent.selectedItemIndex = 0;
					return false;
				}
				
				if (inventoryComponent.selectedItemIndex >= iComponentMapper.get(UIManager.currentGameUnit).amountOfItemsCarried) {
					inventoryComponent.selectedItemIndex--;
					return false;
				}
		
		return true;
	}
	
	public void turnOn() {}
}
