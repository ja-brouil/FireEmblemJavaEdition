package com.jb.fe.UI.inventory;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Align;
import com.jb.fe.UI.MenuBox;
import com.jb.fe.UI.Text.TextObject;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.components.StaticImageComponent;
import com.jb.fe.components.TextComponent;
import com.jb.fe.components.UIComponent;
import com.jb.fe.components.ZOrderComponent;
import com.jb.fe.screens.FireEmblemGame;
import com.jb.fe.systems.graphics.ZOrder;

public class InventoryMenuBox extends MenuBox{

	public UIComponent uiComponent;

	// Item Box Info
	private Entity itemInvInfoBox;
	private StaticImageComponent itemInventoryBoxImage;
	private PositionComponent itemInvPositionComponent;
	private ZOrderComponent zOrderComponentInv;
	private TextComponent itemBoxTextComponent;
	
	// Item Stats info
	private StaticImageComponent itemStatsInfoImage;
	private PositionComponent itemStatsInfoPosition;
	private ZOrderComponent zOrderComponentStats;
	private TextComponent itemStatsTextComponent;
	
	public InventoryMenuBox(AssetManager assetManager, Engine engine) {
		super(assetManager, engine);
		
		// Inventory Info
		itemInvInfoBox = new Entity();
		itemInventoryBoxImage = new StaticImageComponent(assetManager, "UI/itemBox/itemBox.png");
		itemInventoryBoxImage.isEnabled = true;
		itemInventoryBoxImage.width = 80;
		itemInventoryBoxImage.height = 120;
		
		itemInvPositionComponent = new PositionComponent(10,40);
		
		zOrderComponentInv = new ZOrderComponent(ZOrder.UI_LOWER_LAYER);
		
		itemBoxTextComponent = new TextComponent();
		itemBoxTextComponent.isDrawing = true;
		// Both of these are placeholders
		itemBoxTextComponent.textArray.addFirst(new TextObject(itemInvPositionComponent.x + 30, itemInvPositionComponent.y + 23, "Iron Sword", 0.2f, Align.left));
		itemBoxTextComponent.textArray.addFirst(new TextObject(itemInvPositionComponent.x + 30, itemInvPositionComponent.y + 38, "Rapier", 0.2f, Align.left));
		itemBoxTextComponent.textArray.addFirst(new TextObject(itemInvPositionComponent.x + 30, itemInvPositionComponent.y + 53, "Iron Sword", 0.2f, Align.left));
		itemBoxTextComponent.textArray.addFirst(new TextObject(itemInvPositionComponent.x + 30, itemInvPositionComponent.y + 68, "Rapier", 0.2f, Align.left));
		itemBoxTextComponent.textArray.addFirst(new TextObject(itemInvPositionComponent.x + 30, itemInvPositionComponent.y + 83, "Iron Sword", 0.2f, Align.left));
		itemBoxTextComponent.textArray.addFirst(new TextObject(itemInvPositionComponent.x + 30, itemInvPositionComponent.y + 98, "Rapier", 0.2f, Align.left));
		
		itemInvInfoBox.add(itemInventoryBoxImage);
		itemInvInfoBox.add(itemInvPositionComponent);
		itemInvInfoBox.add(zOrderComponentInv);
		itemInvInfoBox.add(itemBoxTextComponent);
		engine.addEntity(itemInvInfoBox);
		
		// Item Info Box
		itemStatsInfoImage = new StaticImageComponent(assetManager, "UI/itemBox/itemBox.png");
		itemStatsInfoImage.isEnabled = true;
		itemStatsInfoImage.width = 80;
		itemStatsInfoImage.height = 60;
		
		itemStatsInfoPosition = new PositionComponent(FireEmblemGame.WIDTH - itemStatsInfoImage.width - 12, 10);
		
		zOrderComponentStats = new ZOrderComponent(ZOrder.UI_LOWER_LAYER);
		itemStatsTextComponent = new TextComponent();
		itemStatsTextComponent.isDrawing = true;
		// Placeholder testx
		itemStatsTextComponent.textArray.addFirst(new TextObject(itemStatsInfoPosition.x + 33, itemStatsInfoPosition.y + 48, "Affin", 0.2f, Align.center));
		itemStatsTextComponent.textArray.addFirst(new TextObject(itemStatsInfoPosition.x + 8, itemStatsInfoPosition.y + 34, "Atk 10", 0.2f, Align.left));
		itemStatsTextComponent.textArray.addFirst(new TextObject(itemStatsInfoPosition.x + 8, itemStatsInfoPosition.y + 14, "Hit 100", 0.2f, Align.left));
		itemStatsTextComponent.textArray.addFirst(new TextObject(itemStatsInfoPosition.x + 42, itemStatsInfoPosition.y + 34, "Crit 5", 0.2f, Align.left));
		itemStatsTextComponent.textArray.addFirst(new TextObject(itemStatsInfoPosition.x + 42, itemStatsInfoPosition.y + 14, "Uses 50", 0.2f, Align.left));
		
		boxEntity.add(itemStatsInfoImage);
		boxEntity.add(itemStatsInfoPosition);
		boxEntity.add(zOrderComponentStats);
		boxEntity.add(itemStatsTextComponent);
		
		turnOn();
	}

	public TextComponent getItemBoxTextComponent() {
		return itemBoxTextComponent;
	}
	
	public TextComponent getItemStatsTextComponent() {
		return itemStatsTextComponent;
	}
	
	@Override
	public void turnOff() {
		itemInventoryBoxImage.isEnabled = false;
		itemBoxTextComponent.isDrawing = false;
		
		itemStatsInfoImage.isEnabled = false;
		itemStatsTextComponent.isDrawing = false;
	}
	
	public void turnOn() {
		itemInventoryBoxImage.isEnabled = true;
		itemBoxTextComponent.isDrawing = true;
		
		itemStatsInfoImage.isEnabled = true;
		itemStatsTextComponent.isDrawing = true;
	}
}