package com.jb.fe.units;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.JsonValue;
import com.jb.fe.components.ItemComponent;
import com.jb.fe.components.NameComponent;

public class ItemFactory {
	
	public static Entity createWeapon(JsonValue itemJsonValue) {
		Entity item = new Entity();
		
		NameComponent nameComponent = new NameComponent(itemJsonValue.getString("Name"));
		ItemComponent itemComponent = new ItemComponent(itemJsonValue);
		
		item.add(nameComponent);
		item.add(itemComponent);
		return item;
	}
}
