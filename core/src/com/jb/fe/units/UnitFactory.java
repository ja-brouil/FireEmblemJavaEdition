package com.jb.fe.units;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.jb.fe.audio.SoundObject;
import com.jb.fe.components.AnimationComponent;
import com.jb.fe.components.AnimationObject;
import com.jb.fe.components.Artifical_IntelligenceComponent;
import com.jb.fe.components.InventoryComponent;
import com.jb.fe.components.NameComponent;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.components.SoundComponent;
import com.jb.fe.components.UnitStatsComponent;
import com.jb.fe.components.MovementStatsComponent;
import com.jb.fe.components.ZOrderComponent;
import com.jb.fe.components.Artifical_IntelligenceComponent.AI_TYPE;
import com.jb.fe.components.IconComponent;
import com.jb.fe.systems.graphics.ZOrderLevel;
import com.jb.fe.systems.items.ItemFactory;

public class UnitFactory {

	public static Entity createEirika(AssetManager assetManager, String name, String animationFileLocation, float x, float y, boolean isAlly, Engine engine) {
		Entity Eirika = new Entity();
		
		// Components
		NameComponent nameComponent = new NameComponent(name);
		PositionComponent positionComponent = new PositionComponent(x, y);
		AnimationComponent animationComponent = new AnimationComponent();
		MovementStatsComponent movementStatsComponent = new MovementStatsComponent();
		UnitStatsComponent unitStatsComponent = new UnitStatsComponent();
		ZOrderComponent zOrderComponent = new ZOrderComponent(ZOrderLevel.MIDDLE_LAYER);
		SoundComponent soundComponent = new SoundComponent();
		InventoryComponent inventoryComponent = new InventoryComponent();
		IconComponent iconComponent = new IconComponent();
		
		// Icon
		Entity icon = IconFactory.createUnitIcon(assetManager, "units/eirika/eirikaPortrait.png", engine);
		iconComponent.iconEntity = icon;
		
		//  Unit stats
		unitStatsComponent.setEirika();
		unitStatsComponent.health = 6;
		
		// Movement Sound
		soundComponent.allSoundObjects.put("Movement", new SoundObject("sound/unitMovement/Light Foot Steps 1.wav", assetManager));
		soundComponent.allSoundObjects.get("Movement").delayTimer = 0.24f;

		// Graphics
		animationComponent.allAnimationObjects.put("Hovering", new AnimationObject(assetManager, animationFileLocation,
				32, 32, AnimationObject.DEFAULT_ANIMATION_TIMER, 0, 0, 4));
		animationComponent.allAnimationObjects.put("Left", new AnimationObject(assetManager, animationFileLocation, 32,
				32, AnimationObject.DEFAULT_ANIMATION_TIMER, 0, 1, 4));
		animationComponent.allAnimationObjects.put("Right", new AnimationObject(assetManager, animationFileLocation, 32,
				32, AnimationObject.DEFAULT_ANIMATION_TIMER, 0, 1, 4));
		animationComponent.allAnimationObjects.get("Right").flipTexture(true, false);
		animationComponent.allAnimationObjects.put("Up", new AnimationObject(assetManager, animationFileLocation, 32,
				32, AnimationObject.DEFAULT_ANIMATION_TIMER, 0, 4, 4));
		animationComponent.allAnimationObjects.put("Down", new AnimationObject(assetManager, animationFileLocation, 32,
				32, AnimationObject.DEFAULT_ANIMATION_TIMER, 0, 3, 4));
		animationComponent.allAnimationObjects.put("Idle", new AnimationObject(assetManager, animationFileLocation, 32,
				32, AnimationObject.DEFAULT_ANIMATION_TIMER, 0, 2, 4));
		animationComponent.allAnimationObjects.put("Selected", new AnimationObject(assetManager, animationFileLocation,
				32, 32, AnimationObject.DEFAULT_ANIMATION_TIMER, 0, 3, 4));

		// Set Default Animation
		animationComponent.currentAnimation = animationComponent.allAnimationObjects.get("Idle");

		// Off sets
		animationComponent.allAnimationObjects.get("Left").Xoffset = -6;
		animationComponent.allAnimationObjects.get("Right").Xoffset = -12;
		animationComponent.allAnimationObjects.get("Up").Xoffset = -3;
		animationComponent.allAnimationObjects.get("Down").Xoffset = -4;
		animationComponent.allAnimationObjects.get("Hovering").Xoffset = -3;
		animationComponent.allAnimationObjects.get("Idle").Xoffset = -3;
		animationComponent.allAnimationObjects.get("Selected").Xoffset = -2;

		// Unit Stats
		movementStatsComponent.movementSteps = 5;
		
		// Inventory
		inventoryComponent.inventory[0] = ItemFactory.createWeapon("Iron Sword");
		inventoryComponent.selectedItem = inventoryComponent.inventory[0];

		// Add Components
		Eirika.add(unitStatsComponent);
		Eirika.add(nameComponent);
		Eirika.add(positionComponent);
		Eirika.add(animationComponent);
		Eirika.add(movementStatsComponent);
		Eirika.add(zOrderComponent);
		Eirika.add(soundComponent);
		Eirika.add(inventoryComponent);
		Eirika.add(iconComponent);

		return Eirika;
	}

	public static Entity createCavalierUnit(AssetManager assetManager, String name, String animationFileLocation, float x, float y, boolean isAlly, Engine engine) {
		Entity cavalierUnit = new Entity();

		// Components
		NameComponent nameComponent = new NameComponent(name);
		PositionComponent positionComponent = new PositionComponent(x, y);
		AnimationComponent animationComponent = new AnimationComponent();
		MovementStatsComponent movementStatsComponent = new MovementStatsComponent();
		UnitStatsComponent unitStatsComponent = new UnitStatsComponent();
		ZOrderComponent zOrderComponent = new ZOrderComponent(ZOrderLevel.MIDDLE_LAYER);
		SoundComponent soundComponent = new SoundComponent();
		InventoryComponent inventoryComponent = new InventoryComponent();
		IconComponent iconComponent = new IconComponent();
		
		// Icon
		Entity icon = IconFactory.createUnitIcon(assetManager, "units/cavalier/sethPortrait.png", engine);
		iconComponent.iconEntity = icon;
		
		// Unit stats
		unitStatsComponent.setCavalier();
		
		// Sound
		soundComponent.allSoundObjects.put("Movement", new SoundObject("sound/unitMovement/Horse Steps.wav", assetManager));
		soundComponent.allSoundObjects.get("Movement").delayTimer = 0.35f;
		
		// Graphics
		animationComponent.allAnimationObjects.put("Hovering", new AnimationObject(assetManager, animationFileLocation,
				32, 32, AnimationObject.DEFAULT_ANIMATION_TIMER, 0, 0, 4));
		animationComponent.allAnimationObjects.put("Left", new AnimationObject(assetManager, animationFileLocation, 32,
				32, AnimationObject.DEFAULT_ANIMATION_TIMER, 0, 1, 4));
		animationComponent.allAnimationObjects.put("Right", new AnimationObject(assetManager, animationFileLocation, 32,
				32, AnimationObject.DEFAULT_ANIMATION_TIMER, 0, 1, 4));
		animationComponent.allAnimationObjects.get("Right").flipTexture(true, false);
		animationComponent.allAnimationObjects.put("Up", new AnimationObject(assetManager, animationFileLocation, 32,
				32, AnimationObject.DEFAULT_ANIMATION_TIMER, 0, 4, 4));
		animationComponent.allAnimationObjects.put("Down", new AnimationObject(assetManager, animationFileLocation, 32,
				32, AnimationObject.DEFAULT_ANIMATION_TIMER, 0, 3, 4));
		animationComponent.allAnimationObjects.put("Idle", new AnimationObject(assetManager, animationFileLocation, 32,
				32, AnimationObject.DEFAULT_ANIMATION_TIMER, 0, 2, 4));
		animationComponent.allAnimationObjects.put("Selected", new AnimationObject(assetManager, animationFileLocation,
				32, 32, AnimationObject.DEFAULT_ANIMATION_TIMER, 0, 1, 4));
		animationComponent.allAnimationObjects.get("Selected").flipTexture(true, false);

		// Set Default Animation
		animationComponent.currentAnimation = animationComponent.allAnimationObjects.get("Idle");

		// Off sets
		animationComponent.allAnimationObjects.get("Left").Xoffset = 0;
		animationComponent.allAnimationObjects.get("Right").Xoffset = -12;
		animationComponent.allAnimationObjects.get("Up").Xoffset = -3;
		animationComponent.allAnimationObjects.get("Down").Xoffset = -4;
		animationComponent.allAnimationObjects.get("Hovering").Xoffset = -3;
		animationComponent.allAnimationObjects.get("Idle").Xoffset = -3;
		animationComponent.allAnimationObjects.get("Selected").Xoffset = -12;
		
		// Unit Stats
		movementStatsComponent.movementSteps = 7;
		movementStatsComponent.isAlly = isAlly;
		
		// Add AI component if not ally
		if (!isAlly) {
			Artifical_IntelligenceComponent aiComponent = new Artifical_IntelligenceComponent();
			nameComponent.name = "Red Empire";
			aiComponent.ai_Type = AI_TYPE.PASSIVE;
			cavalierUnit.add(aiComponent);
		}
		
		// Inventory Component
		inventoryComponent.inventory[0] = ItemFactory.createWeapon("Iron Sword");
		inventoryComponent.selectedItem = inventoryComponent.inventory[0];
		
		// Add Components
		cavalierUnit.add(unitStatsComponent);
		cavalierUnit.add(movementStatsComponent);
		cavalierUnit.add(animationComponent);
		cavalierUnit.add(positionComponent);
		cavalierUnit.add(nameComponent);
		cavalierUnit.add(zOrderComponent);
		cavalierUnit.add(soundComponent);
		cavalierUnit.add(inventoryComponent);
		cavalierUnit.add(iconComponent);

		return cavalierUnit;
	}
}