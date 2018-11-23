package com.jb.fe.UI.inventory;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.jb.fe.components.InventoryComponent;
import com.jb.fe.components.ItemComponent;
import com.jb.fe.components.NameComponent;
import com.jb.fe.components.UIComponent.UpdateUI;

public class InventoryBoxUpdate implements UpdateUI{
	
	private InventoryMenuBox inventoryMenuBox;
	
	private ComponentMapper<InventoryComponent> iComponentMapper = ComponentMapper.getFor(InventoryComponent.class);
	private ComponentMapper<NameComponent> nComponentMapper = ComponentMapper.getFor(NameComponent.class);
	private ComponentMapper<ItemComponent> itemComponentMapper = ComponentMapper.getFor(ItemComponent.class);
	private InventoryComponent inventoryComponent;
	
	public InventoryBoxUpdate(InventoryMenuBox inventoryMenuBox) {
		this.inventoryMenuBox = inventoryMenuBox;
	}

	@Override
	public void updateUI(float delta) {
		// Set Text of equipped item | Atk, Hit, Crit, Avoid
		inventoryMenuBox.getItemStatsTextComponent().textArray.get(1).text = "Atk " + Integer.toString(itemComponentMapper.get(inventoryComponent.selectedItem).might);
		inventoryMenuBox.getItemStatsTextComponent().textArray.get(2).text = "Hit " + Integer.toString(itemComponentMapper.get(inventoryComponent.selectedItem).hit);
		inventoryMenuBox.getItemStatsTextComponent().textArray.get(3).text = "Crit " + Integer.toString(itemComponentMapper.get(inventoryComponent.selectedItem).crit);
		inventoryMenuBox.getItemStatsTextComponent().textArray.get(4).text = "Uses " + Integer.toString(itemComponentMapper.get(inventoryComponent.selectedItem).uses);
	}
	
	public void setUnit(Entity unit) {
		inventoryComponent = iComponentMapper.get(unit);
		
		// Turn off inventory
		for (int i = 0; i < inventoryComponent.MAX_INVENTORY_SIZE; i++) {
			if (i <= inventoryComponent.amountOfItemsCarried) {
				inventoryMenuBox.getItemBoxTextComponent().textArray.get(i).isEnabled = true;
			} else {
				inventoryMenuBox.getItemBoxTextComponent().textArray.get(i).isEnabled = false;
			}
		}
		
		// Set New Inventory | check if this should be - 1 or not later
		for (int i = 0; i < inventoryComponent.amountOfItemsCarried - 1; i++) {
			inventoryMenuBox.getItemBoxTextComponent().textArray.get(i).text = nComponentMapper.get(inventoryComponent.inventory[i]).name;
		}
	}

}
