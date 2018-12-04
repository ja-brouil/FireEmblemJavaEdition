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

public class UnitDamageSelectorFactor {
	
	public static Entity createUnitDamagePreviewEntity(AssetManager assetManager, UIManager uiManager, Entity mapCursor, InventoryMenuBox inventoryMenuBox) {
		Entity damagePreviewMenuBox = new Entity();
		
		// Graphics
		StaticImageComponent staticImageComponent = new StaticImageComponent(assetManager, "UI/damageBox/damageBox.png");
		staticImageComponent.isEnabled = true;
		PositionComponent positionComponent = new PositionComponent();
		ZOrderComponent zOrderComponent = new ZOrderComponent(ZOrder.UI_LOWER_LAYER);
		
		// Text Component
		TextComponent textComponent = new TextComponent();
		textComponent.textArray.addFirst(new TextObject(0, 0, "Attacking Unit Name"));
		textComponent.textArray.addFirst(new TextObject(0, 0, "Def HP"));
		textComponent.textArray.addFirst(new TextObject(0, 0, "Atk HP"));
		textComponent.textArray.addFirst(new TextObject(0, 0, "Def Might"));
		textComponent.textArray.addFirst(new TextObject(0, 0, "Atk Might"));
		textComponent.textArray.addFirst(new TextObject(0, 0, "Def Hit"));
		textComponent.textArray.addFirst(new TextObject(0, 0, "Atk Hit"));
		textComponent.textArray.addFirst(new TextObject(0, 0, "Def Crit"));
		textComponent.textArray.addFirst(new TextObject(0, 0, "Atk Crit"));
		textComponent.textArray.addFirst(new TextObject(0, 0, "Defending Unit Name"));
		textComponent.textArray.addFirst(new TextObject(0, 0, "Defending Unit Item Equipped"));
		
		// UI Component
		UIComponent uiComponent = new UIComponent(uiManager);
		uiComponent.inputHandling = new UnitDamageSelectionInput(mapCursor, damagePreviewMenuBox, inventoryMenuBox);
		
		// Add everything
		damagePreviewMenuBox.add(staticImageComponent);
		damagePreviewMenuBox.add(positionComponent);
		damagePreviewMenuBox.add(zOrderComponent);
		damagePreviewMenuBox.add(textComponent);
		damagePreviewMenuBox.add(uiComponent);
		return damagePreviewMenuBox;
	}
}
