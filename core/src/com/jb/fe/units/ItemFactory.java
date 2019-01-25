package com.jb.fe.units;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.JsonValue;
import com.jb.fe.components.ItemComponent;
import com.jb.fe.components.NameComponent;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.components.StaticImageComponent;
import com.jb.fe.components.ZOrderComponent;
import com.jb.fe.systems.graphics.ZOrder;

public class ItemFactory {
	
	public static Entity createWeapon(JsonValue itemJsonValue, AssetManager assetManager, Engine engine) {
		Entity item = new Entity();
		
		// Item components
		NameComponent nameComponent = new NameComponent(itemJsonValue.getString("Name"));
		ItemComponent itemComponent = new ItemComponent(itemJsonValue);
		
		// Item Icon
		StaticImageComponent itemIconImageComponent = new StaticImageComponent(assetManager, itemJsonValue.getString("FileLocation"));
		itemIconImageComponent.isEnabled = false;
		itemIconImageComponent.width = 12;
		itemIconImageComponent.height = 12;
		PositionComponent itemIconPositionComponent = new PositionComponent(100, 100);
		ZOrderComponent zOrderComponent = new ZOrderComponent(ZOrder.UI_MIDDLE_LAYER);
		
		item.add(itemIconImageComponent);
		item.add(itemIconPositionComponent);
		item.add(zOrderComponent);
		item.add(nameComponent);
		item.add(itemComponent);
		engine.addEntity(item);
		return item;
	}
}