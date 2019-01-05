package com.jb.fe.UI.inventory;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.jb.fe.components.InventoryComponent;
import com.jb.fe.components.ItemComponent;
import com.jb.fe.components.NameComponent;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.components.StaticImageComponent;
import com.jb.fe.components.UIComponent.UpdateUI;
import com.jb.fe.components.ZOrderComponent;
import com.jb.fe.systems.graphics.ZOrder;
import com.jb.fe.systems.inputAndUI.UIManager;

public class InventoryBoxUpdate implements UpdateUI{
	
	// UI Entites needed
	private InventoryMenuBox inventoryMenuBox;
	private Entity hand;
	
	private ComponentMapper<InventoryComponent> iComponentMapper = ComponentMapper.getFor(InventoryComponent.class);
	private ComponentMapper<NameComponent> nComponentMapper = ComponentMapper.getFor(NameComponent.class);
	private ComponentMapper<ItemComponent> itemComponentMapper = ComponentMapper.getFor(ItemComponent.class);
	private ComponentMapper<StaticImageComponent> sComponentMapper = ComponentMapper.getFor(StaticImageComponent.class);
	private ComponentMapper<PositionComponent> pComponentMapper = ComponentMapper.getFor(PositionComponent.class);
	private InventoryComponent inventoryComponent;
	
	public InventoryBoxUpdate(InventoryMenuBox inventoryMenuBox, Entity hand) {
		this.inventoryMenuBox = inventoryMenuBox;
		this.hand = hand;
	}

	@Override
	public void updateUI(float delta) {
		setTextInfo();
	}
	
	public void setUnit(Entity unit) {
		inventoryComponent = iComponentMapper.get(unit);
		
		// Turn off inventory
		for (int i = 0; i < inventoryMenuBox.getItemBoxTextComponent().textArray.size; i++) {
			inventoryMenuBox.getItemBoxTextComponent().textArray.get(i).isEnabled = false;
			inventoryMenuBox.getItemBoxTextComponent().textArray.get(i).text = "";
		}
		
		// Set New Inventory | check if this should be - 1 or not later
		for (int i = 0; i < inventoryComponent.amountOfItemsCarried; i++) {
			inventoryMenuBox.getItemBoxTextComponent().textArray.get(i).text = nComponentMapper.get(inventoryComponent.inventory[i]).name;
			inventoryMenuBox.getItemBoxTextComponent().textArray.get(i).isEnabled = true;
			inventoryMenuBox.getItemBoxTextComponent().textArray.get(i).y = (23 + ((5 - i) * 15) + 40);	
		}
		
		// Set Size of the dimensions for the box
		inventoryMenuBox.getStaticImageComponent().height = 20 * inventoryComponent.amountOfItemsCarried;
		inventoryMenuBox.getPositionComponentItemBox().y =  40 + ((inventoryComponent.MAX_INVENTORY_SIZE - inventoryComponent.amountOfItemsCarried) * 16.8f);
		inventoryMenuBox.turnOn();
		
		// Set Hand
		sComponentMapper.get(hand).isEnabled = true;
		hand.getComponent(ZOrderComponent.class).zOrder = ZOrder.UI_MIDDLE_LAYER;
		pComponentMapper.get(hand).x = pComponentMapper.get(inventoryMenuBox.getItemInvBoxEntity()).x - 5;
		pComponentMapper.get(hand).y = inventoryMenuBox.getItemBoxTextComponent().textArray.get(0).y - 10;
		iComponentMapper.get(UIManager.currentGameUnit).selectedItemIndex = 0;
	}
	
	
}