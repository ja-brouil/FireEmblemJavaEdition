package com.jb.fe.UI.inventory;

import com.badlogic.ashley.core.ComponentMapper;
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
import com.jb.fe.systems.inputAndUI.UIManager;

public class InventoryMenuBox extends MenuBox{

	public UIComponent uiComponent;
	public InventoryBoxUpdate inventoryBoxUpdate;
	public InventoryInputHandle inventoryInputHandle;
	
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
	
	// Hand
	private Entity hand;
	private ComponentMapper<StaticImageComponent> sComponentMapper = ComponentMapper.getFor(StaticImageComponent.class);
	
	public InventoryMenuBox(AssetManager assetManager, Engine engine, Entity hand, Entity actionMenu, Entity unitDamageSelectionPreview, UIManager uiManager) {
		super(assetManager, engine);
		this.hand = hand;
		
		// Inventory Info
		itemInvInfoBox = new Entity();
		itemInventoryBoxImage = new StaticImageComponent(assetManager, "UI/itemBox/itemBox.png");
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
		itemStatsTextComponent.textArray.addFirst(new TextObject(itemStatsInfoPosition.x + 33, itemStatsInfoPosition.y + 48, "Affin", 0.2f, Align.center));
		itemStatsTextComponent.textArray.addFirst(new TextObject(itemStatsInfoPosition.x + 8, itemStatsInfoPosition.y + 34, "Atk 10", 0.2f, Align.left));
		itemStatsTextComponent.textArray.addFirst(new TextObject(itemStatsInfoPosition.x + 8, itemStatsInfoPosition.y + 14, "Hit 100", 0.2f, Align.left));
		itemStatsTextComponent.textArray.addFirst(new TextObject(itemStatsInfoPosition.x + 42, itemStatsInfoPosition.y + 34, "Crit 5", 0.2f, Align.left));
		itemStatsTextComponent.textArray.addFirst(new TextObject(itemStatsInfoPosition.x + 42, itemStatsInfoPosition.y + 14, "Uses 50", 0.2f, Align.left));
		
		uiComponent = new UIComponent(uiManager);
		inventoryBoxUpdate = new InventoryBoxUpdate(this, hand);
		uiComponent.updateUI = inventoryBoxUpdate;
		inventoryInputHandle = new InventoryInputHandle(hand, uiComponent, this, null);
		uiComponent.inputHandling = inventoryInputHandle;
		
		boxEntity.add(itemStatsInfoImage);
		boxEntity.add(itemStatsInfoPosition);
		boxEntity.add(zOrderComponentStats);
		boxEntity.add(itemStatsTextComponent);
		boxEntity.add(uiComponent);
	}

	public TextComponent getItemBoxTextComponent() {
		return itemBoxTextComponent;
	}
	
	public TextComponent getItemStatsTextComponent() {
		return itemStatsTextComponent;
	}
	
	public StaticImageComponent getStaticImageComponent() {
		return itemInventoryBoxImage;
	}
	
	public PositionComponent getPositionComponentItemBox() {
		return itemInvPositionComponent;
	}
	
	public Entity getItemInvBoxEntity() {
		return itemInvInfoBox;
	}
	
	public void setUnit(Entity unit) {
		inventoryBoxUpdate.setUnit(unit);
	}
	
	public void setUnitDamageSelection(Entity unitDamageSelection) {
		inventoryInputHandle.unitDamagePreview = unitDamageSelection;
	}
	
	@Override
	public void turnOff() {
		itemInventoryBoxImage.isEnabled = false;
		itemBoxTextComponent.isDrawing = false;
		
		itemStatsInfoImage.isEnabled = false;
		itemStatsTextComponent.isDrawing = false;
		
		uiComponent.inputIsEnabled = false;
		uiComponent.updateIsEnabled = false;
		
		sComponentMapper.get(hand).isEnabled = false;
	}
	
	public void turnOn() {
		itemInventoryBoxImage.isEnabled = true;
		itemBoxTextComponent.isDrawing = true;
		
		itemStatsInfoImage.isEnabled = true;
		itemStatsTextComponent.isDrawing = true;
		
		uiComponent.inputIsEnabled = true;
		uiComponent.updateIsEnabled = true;
		
		sComponentMapper.get(hand).isEnabled = true;
	}
}