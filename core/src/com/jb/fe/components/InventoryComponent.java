package com.jb.fe.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

public class InventoryComponent implements Component{
	
	// Inventory
	public Entity[] inventory;
	
	// Selected item
	public Entity selectedItem;
	public int selectedItemIndex = 0;
	
	// Counts
	public final int MAX_INVENTORY_SIZE = 6;
	public int amountOfItemsCarried = 0;
	
	public InventoryComponent() {
		inventory = new Entity[MAX_INVENTORY_SIZE];
	}
	
	public void addItem(Entity item) {
		inventory[amountOfItemsCarried] = item;
		amountOfItemsCarried++;
	}
	
	public void removeItem(int index) {
		inventory[index] = null;
		amountOfItemsCarried--;
		moveAllItemsDown();
	}
	
	private void moveAllItemsDown() {
		for (int i = 0; i < MAX_INVENTORY_SIZE; i++) {
			if (inventory[i] == null && i + 1 < MAX_INVENTORY_SIZE) {
				inventory[i] = inventory[i + 1];
				inventory[i + 1] = null;
			}
		}
	}
}