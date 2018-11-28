package com.jb.fe.UI.inventory;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.jb.fe.components.InventoryComponent;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.components.UIComponent.InputHandling;
import com.jb.fe.systems.inputAndUI.UIManager;

public class InventoryInputHandle implements InputHandling{
	
	private float keyDelay = 0.08f;
	private float currentDelay;
	
	private Entity hand;
	public static int itemSelectionNumber;
	
	private ComponentMapper<PositionComponent> pComponentMapper = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<InventoryComponent> iComponentMapper = ComponentMapper.getFor(InventoryComponent.class);
	
	public InventoryInputHandle(Entity hand) {
		this.hand = hand;
		itemSelectionNumber = 0;
	}

	@Override
	public void handleInput() {
		currentDelay += Gdx.graphics.getDeltaTime();
		System.out.println(iComponentMapper.get(UIManager.currentGameUnit).amountOfItemsCarried);
		System.out.println(allowHandMovement());
		if (currentDelay <= keyDelay) { return; }
		
		// Up
		if (allowHandMovement()) {
			if (Gdx.input.isKeyPressed(Keys.UP)) {
				System.out.println("up");
				pComponentMapper.get(hand).y += 20;
				currentDelay = 0;
			}
			
			// Down
			if (Gdx.input.isKeyPressed(Keys.DOWN)) {
				System.out.println("down");
				pComponentMapper.get(hand).y -= 20;
				currentDelay = 0;
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
			System.out.println("b");
			currentDelay = 0;
		}
	}
	
	private boolean allowHandMovement() {
		// If there is only 1 item, no movement allowed
		if (iComponentMapper.get(UIManager.currentGameUnit).amountOfItemsCarried <= 1) {
			return false;
		}
		
		
		
		return true;
	}
}
