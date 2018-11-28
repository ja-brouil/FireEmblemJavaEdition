package com.jb.fe.units;

import com.badlogic.ashley.core.Entity;
import com.jb.fe.components.ItemComponent;
import com.jb.fe.components.NameComponent;
import com.jb.fe.components.PositionComponent;

public class ItemFactory {
	
	public static Entity createWeapon(String name) {
		Entity item = new Entity();
		
		PositionComponent positionComponent = new PositionComponent();
		NameComponent nameComponent = new NameComponent(name);
		ItemComponent itemComponent = new ItemComponent();
		
		item.add(positionComponent);
		item.add(nameComponent);
		item.add(itemComponent);
		
		return item;
	}
}
