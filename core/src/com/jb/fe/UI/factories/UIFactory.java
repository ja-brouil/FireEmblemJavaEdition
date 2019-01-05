package com.jb.fe.UI.factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.jb.fe.UI.Text.TextObject;
import com.jb.fe.UI.combatUnitSelector.UnitDamageSelectorFactory;
import com.jb.fe.UI.inventory.InventoryMenuBox;
import com.jb.fe.UI.soundTemp.UISounds;
import com.jb.fe.audio.SoundObject;
import com.jb.fe.components.AnimationComponent;
import com.jb.fe.components.AnimationObject;
import com.jb.fe.components.NameComponent;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.components.SoundComponent;
import com.jb.fe.components.StaticImageComponent;
import com.jb.fe.components.TextComponent;
import com.jb.fe.components.ZOrderComponent;
import com.jb.fe.level.Level;
import com.jb.fe.screens.FireEmblemGame;
import com.jb.fe.systems.graphics.ZOrder;
import com.jb.fe.systems.movement.UnitMapCellUpdater;
import com.jb.fe.systems.movement.UnitMovementSystem;

/*
 * Main Battlefield UI Factory
 */
public class UIFactory {
		
	public static Entity createMapCursor(Level level, AssetManager assetManager, OrthographicCamera camera) {
		Entity mapCursor = new Entity();
		
		// Components
		AnimationComponent animationComponent = new AnimationComponent();
		animationComponent.allAnimationObjects.put("Regular", new AnimationObject(assetManager, "UI/Cursor/cursor.png", 320 / 8, 42, 1f / 12f, 0, 0, 8));
		animationComponent.currentAnimation = animationComponent.allAnimationObjects.get("Regular");
		animationComponent.currentAnimation.width = FireEmblemGame.WIDTH / 15;
		animationComponent.currentAnimation.height = FireEmblemGame.HEIGHT / 10;
		
		PositionComponent positionComponent = new PositionComponent(160,32);
		
		StaticImageComponent staticImageComponent = new StaticImageComponent(assetManager, "UI/Cursor/staticCursor.png");
		staticImageComponent.isEnabled = false;
		staticImageComponent.xOffset = -3.5f;
		
		ZOrderComponent zOrderComponent = new ZOrderComponent(ZOrder.TOP_LAYER);
		
		// Create UI sounds here for all user interfaces
		SoundComponent soundComponent = new SoundComponent();
		soundComponent.allSoundObjects.put("Accept", new SoundObject("sound/accept.wav", assetManager));
		soundComponent.allSoundObjects.put("Back", new SoundObject("sound/backSound.wav", assetManager));
		soundComponent.allSoundObjects.put("Movement", new SoundObject("sound/cursorMovement.wav", assetManager));
		soundComponent.allSoundObjects.put("Invalid", new SoundObject("sound/Not Allowed.mp3", assetManager));
		soundComponent.allSoundObjects.put("Select Unit", new SoundObject("sound/selectUnit.wav", assetManager));
		UISounds.accept = soundComponent.allSoundObjects.get("Accept");
		UISounds.back = soundComponent.allSoundObjects.get("Back");
		UISounds.movement = soundComponent.allSoundObjects.get("Movement");
		UISounds.invalid = soundComponent.allSoundObjects.get("Invalid");
		
		// Add components
		mapCursor.add(animationComponent);
		mapCursor.add(positionComponent);
		mapCursor.add(staticImageComponent);
		mapCursor.add(zOrderComponent);
		mapCursor.add(soundComponent);

		return mapCursor;
	}
	
	
	public static Entity createActionMenu(UnitMovementSystem unitMovementSystem, UnitMapCellUpdater unitMapCellUpdater, AssetManager assetManager) {
		Entity actionMenu = new Entity();
		
		// Components
		NameComponent nameComponent = new NameComponent("Action Menu");
		
		PositionComponent positionComponent = new PositionComponent(0, 70);
		
		TextComponent uiTextComponent = new TextComponent();
		uiTextComponent.textArray.addFirst(new TextObject(0, 50, "Action", 0.25f));
		uiTextComponent.textArray.addFirst(new TextObject(0, 100, "Items", 0.25f));
		uiTextComponent.textArray.addFirst(new TextObject(0, 150, "Trade", 0.25f));
		uiTextComponent.textArray.addFirst(new TextObject(0, 200, "Wait", 0.25f));
		uiTextComponent.isDrawing = false;

		
		StaticImageComponent actionMenuStaticImage = new StaticImageComponent(assetManager, "UI/endturnbox/endturnbox.png");
		actionMenuStaticImage.alpha = 0.8f;
		actionMenuStaticImage.isEnabled = false;
		actionMenuStaticImage.width = FireEmblemGame.WIDTH / 5;
		actionMenuStaticImage.height = 70;
		
		ZOrderComponent zOrderComponent = new ZOrderComponent(ZOrder.TOP_LAYER);
		
		// Add Components
		actionMenu.add(nameComponent);
		actionMenu.add(positionComponent);
		actionMenu.add(uiTextComponent);
		actionMenu.add(actionMenuStaticImage);
		actionMenu.add(zOrderComponent);
		
		return actionMenu;
	}
	
	public static Entity createHand(AssetManager assetManager) {
		Entity hand = new Entity();
		// Components
		StaticImageComponent staticImageComponent = new StaticImageComponent(assetManager, "UI/Cursor/hand.png");
		staticImageComponent.isEnabled = false;
		ZOrderComponent zOrderComponent = new ZOrderComponent(ZOrder.UI_LOWER_LAYER);
		
		PositionComponent positionComponent = new PositionComponent(-100, 181);
		NameComponent nameComponent = new NameComponent("Hand Selector");
		
		hand.add(staticImageComponent);
		hand.add(positionComponent);
		hand.add(nameComponent);
		hand.add(zOrderComponent);

		return hand;
	}
	
	/*
	public void createInventoryMenu() {
		inventoryMenuBox = new InventoryMenuBox(assetManager, engine, hand, actionMenu, null, uiManager);
		inventoryMenuBox.inventoryInputHandle.unitDamagePreview = createDamagePreviewBox();
	}
	
	public Entity createDamagePreviewBox() {
		Entity unitDamagePreview = UnitDamageSelectorFactory.createUnitDamagePreviewEntity(assetManager, uiManager, mapCursor, inventoryMenuBox);
		engine.addEntity(unitDamagePreview);
		return unitDamagePreview;
	}
	*/
}
