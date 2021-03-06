package com.jb.fe.UI.inventory;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Align;
import com.jb.fe.UI.MenuBox;
import com.jb.fe.UI.Text.TextObject;
import com.jb.fe.UI.mapcursor.MapCursor;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.components.StaticImageComponent;
import com.jb.fe.components.TextComponent;
import com.jb.fe.components.ZOrderComponent;
import com.jb.fe.screens.FireEmblemGame;
import com.jb.fe.systems.camera.CameraSystem;
import com.jb.fe.systems.graphics.ZOrder;

public class InventoryMenuBox extends MenuBox {
	
	// Item Box Info
	private Entity itemInvInfoBox;
	private Entity topinventoryDecorationEntity;
	private Entity bottomInventoryDecorationEntity;
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
		itemInventoryBoxImage = new StaticImageComponent(assetManager, "UI/itemBox/itemBoxNoDeco.png");
		itemInventoryBoxImage.isEnabled = false;
		itemInventoryBoxImage.width = 90;
		itemInventoryBoxImage.height = 120;
		
		itemInvPositionComponent = new PositionComponent(10,40);
		
		zOrderComponentInv = new ZOrderComponent(ZOrder.UI_LOWER_LAYER);
		
		itemBoxTextComponent = new TextComponent();
		itemBoxTextComponent.isDrawing = false;
		
		// Both of these are placeholders
		itemBoxTextComponent.textArray.addLast(new TextObject(itemInvPositionComponent.x + 30, itemInvPositionComponent.y + 23, "Iron Sword", 0.2f, Align.left));
		itemBoxTextComponent.textArray.addLast(new TextObject(itemInvPositionComponent.x + 30, itemInvPositionComponent.y + 38, "Rapier", 0.2f, Align.left));
		itemBoxTextComponent.textArray.addLast(new TextObject(itemInvPositionComponent.x + 30, itemInvPositionComponent.y + 53, "Bow", 0.2f, Align.left));
		itemBoxTextComponent.textArray.addLast(new TextObject(itemInvPositionComponent.x + 30, itemInvPositionComponent.y + 68, "Staff", 0.2f, Align.left));
		itemBoxTextComponent.textArray.addLast(new TextObject(itemInvPositionComponent.x + 30, itemInvPositionComponent.y + 83, "Tome", 0.2f, Align.left));
		itemBoxTextComponent.textArray.addLast(new TextObject(itemInvPositionComponent.x + 30, itemInvPositionComponent.y + 98, "Spear", 0.2f, Align.left));
		
		itemInvInfoBox.add(itemInventoryBoxImage);
		itemInvInfoBox.add(itemInvPositionComponent);
		itemInvInfoBox.add(zOrderComponentInv);
		itemInvInfoBox.add(itemBoxTextComponent);
		engine.addEntity(itemInvInfoBox);
		
		// Item Info Box
		itemStatsInfoImage = new StaticImageComponent(assetManager, "UI/itemBox/itemBox.png");
		itemStatsInfoImage.isEnabled = false;
		itemStatsInfoImage.width = 80;
		itemStatsInfoImage.height = 60;
		
		itemStatsInfoPosition = new PositionComponent(FireEmblemGame.WIDTH - itemStatsInfoImage.width - 12, 10);
		
		zOrderComponentStats = new ZOrderComponent(ZOrder.UI_LOWER_LAYER);
		itemStatsTextComponent = new TextComponent();
		itemStatsTextComponent.isDrawing = false;
		
		// Placeholder test
		itemStatsTextComponent.textArray.addFirst(new TextObject(itemStatsInfoPosition.x + 40, itemStatsInfoPosition.y + 48, "Weapon Stats", 0.2f, Align.center));
		itemStatsTextComponent.textArray.addFirst(new TextObject(itemStatsInfoPosition.x + 8, itemStatsInfoPosition.y + 34, "Atk 10", 0.2f, Align.left));
		itemStatsTextComponent.textArray.addFirst(new TextObject(itemStatsInfoPosition.x + 8, itemStatsInfoPosition.y + 14, "Hit 100", 0.2f, Align.left));
		itemStatsTextComponent.textArray.addFirst(new TextObject(itemStatsInfoPosition.x + 42, itemStatsInfoPosition.y + 34, "Crit 5", 0.2f, Align.left));
		itemStatsTextComponent.textArray.addFirst(new TextObject(itemStatsInfoPosition.x + 42, itemStatsInfoPosition.y + 14, "Uses 50", 0.2f, Align.left));
		
		boxEntity.add(itemStatsInfoImage);
		boxEntity.add(itemStatsInfoPosition);
		boxEntity.add(zOrderComponentStats);
		boxEntity.add(itemStatsTextComponent);
		engine.addEntity(boxEntity);
		
	}
	
	public Entity getItemInvBoxEntity() {
		return itemInvInfoBox;
	}
	
	public Entity getBottomEntityDecoration() {
		return bottomInventoryDecorationEntity;
	}
	
	public Entity getTopDecorationEntity() {
		return topinventoryDecorationEntity;
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
		
		// Set correct position
		itemStatsInfoPosition.x = (CameraSystem.cameraX - CameraSystem.xConstant) + (FireEmblemGame.WIDTH - itemStatsInfoImage.width - 12);
		itemStatsInfoPosition.y = (CameraSystem.cameraY - CameraSystem.yConstant) + 10;
		
		itemInvPositionComponent.x = 10 + (CameraSystem.cameraX - CameraSystem.xConstant);
	}

	@Override
	public void update(MapCursor mapCursor) {}
} 