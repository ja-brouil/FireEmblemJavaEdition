package com.jb.fe.UI.combatUnitSelector;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.jb.fe.UI.Text.TextObject;
import com.jb.fe.UI.inventory.InventoryMenuBox;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.components.StaticImageComponent;
import com.jb.fe.components.TextComponent;
import com.jb.fe.components.UIComponent;
import com.jb.fe.components.ZOrderComponent;
import com.jb.fe.systems.graphics.ZOrder;
import com.jb.fe.systems.inputAndUI.UIManager;

public class UnitDamageSelectorFactory {
	
	public static Entity createUnitDamagePreviewEntity(AssetManager assetManager, UIManager uiManager, Entity mapCursor, InventoryMenuBox inventoryMenuBox) {
		Entity damagePreviewMenuBox = new Entity();
		
		// Graphics
		StaticImageComponent staticImageComponent = new StaticImageComponent(assetManager, "UI/damageBox/damageBox.png");
		staticImageComponent.isEnabled = false;
		staticImageComponent.width = 70;
		staticImageComponent.height = 100;
		PositionComponent positionComponent = new PositionComponent();
		ZOrderComponent zOrderComponent = new ZOrderComponent(ZOrder.UI_LOWER_LAYER);
		
		// Text Component
		TextComponent textComponent = new TextComponent();
		textComponent.isDrawing = false;
		textComponent.textArray.addFirst(new TextObject(50, 50, "Attacking Unit Name", 0.20f));
		textComponent.textArray.addFirst(new TextObject(50, 40, "Def HP", 0.20f));
		textComponent.textArray.addFirst(new TextObject(50, 30, "Atk HP", 0.20f));
		textComponent.textArray.addFirst(new TextObject(50, 35, "Def Might", 0.20f));
		textComponent.textArray.addFirst(new TextObject(50, 25, "Atk Might", 0.20f));
		textComponent.textArray.addFirst(new TextObject(50, 20, "Def Hit", 0.20f));
		textComponent.textArray.addFirst(new TextObject(50, 15, "Atk Hit", 0.20f));
		textComponent.textArray.addFirst(new TextObject(50, 10, "Def Crit", 0.20f));
		textComponent.textArray.addFirst(new TextObject(50, 0, "Atk Crit", 0.20f));
		textComponent.textArray.addFirst(new TextObject(50, 0, "Defending Unit Name", 0.20f));
		textComponent.textArray.addFirst(new TextObject(50, 0, "Defending Unit Item Equipped", 0.20f));
		
		// UI Component
		UIComponent uiComponent = new UIComponent(uiManager);
		uiComponent.inputHandling = new UnitDamageSelectionInput(mapCursor, damagePreviewMenuBox, inventoryMenuBox);
		uiComponent.updateUI = new UnitDamagePreviewUpdate();
		
		// Add everything
		damagePreviewMenuBox.add(staticImageComponent);
		damagePreviewMenuBox.add(positionComponent);
		damagePreviewMenuBox.add(zOrderComponent);
		damagePreviewMenuBox.add(textComponent);
		damagePreviewMenuBox.add(uiComponent);
		return damagePreviewMenuBox;
	}
}
