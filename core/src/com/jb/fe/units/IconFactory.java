package com.jb.fe.units;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.components.StaticImageComponent;
import com.jb.fe.components.ZOrderComponent;
import com.jb.fe.systems.graphics.ZOrder;

public class IconFactory {

	
	public static  Entity createUnitIcon(AssetManager assetManager, String fileLocation, Engine engine) {
		Entity unitIcon = new Entity();
		
		StaticImageComponent staticImageComponent = new StaticImageComponent(assetManager, fileLocation);
		staticImageComponent.isEnabled = true;
		staticImageComponent.width = staticImageComponent.wholeImage.getWidth();
		staticImageComponent.height = 44 - 15;
		PositionComponent positionComponent = new PositionComponent(-500, -500);
		ZOrderComponent zOrderComponent = new ZOrderComponent(ZOrder.UI_MIDDLE_LAYER);
		
		unitIcon.add(staticImageComponent);
		unitIcon.add(positionComponent);
		unitIcon.add(zOrderComponent);
		
		engine.addEntity(unitIcon);
		return unitIcon;
	}
	
	public static Entity createItemIcon() {
		Entity itemIcon = new Entity();
		return itemIcon;
	}
}
