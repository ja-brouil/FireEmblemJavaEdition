package com.jb.fe.units;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.MapProperties;
import com.jb.fe.audio.SoundObject;
import com.jb.fe.components.AnimationComponent;
import com.jb.fe.components.AnimationObject;
import com.jb.fe.components.Artifical_IntelligenceComponent;
import com.jb.fe.components.Artifical_IntelligenceComponent.AI_TYPE;
import com.jb.fe.components.IconComponent;
import com.jb.fe.components.InventoryComponent;
import com.jb.fe.components.MovementStatsComponent;
import com.jb.fe.components.NameComponent;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.components.SoundComponent;
import com.jb.fe.components.UnitStatsComponent;
import com.jb.fe.components.ZOrderComponent;
import com.jb.fe.systems.gamePlay.ItemManager;
import com.jb.fe.systems.graphics.ZOrder;

// Fix diamond of the box later
public class UnitFactory {

	public static Entity createEirika(AssetManager assetManager, String name, String animationFileLocation, float x, float y, boolean isAlly, Engine engine) {
		Entity Eirika = new Entity();
		
		// Components
		NameComponent nameComponent = new NameComponent(name);
		PositionComponent positionComponent = new PositionComponent(x, y);
		AnimationComponent animationComponent = new AnimationComponent();
		MovementStatsComponent movementStatsComponent = new MovementStatsComponent();
		UnitStatsComponent unitStatsComponent = new UnitStatsComponent();
		ZOrderComponent zOrderComponent = new ZOrderComponent(ZOrder.MIDDLE_LAYER);
		SoundComponent soundComponent = new SoundComponent();
		InventoryComponent inventoryComponent = new InventoryComponent();
		IconComponent iconComponent = new IconComponent();
		
		// Icon
		Entity icon = IconFactory.createUnitIcon(assetManager, "units/eirika/eirikaPortrait.png", engine);
		iconComponent.iconEntity = icon;
		
		//  Unit stats
		unitStatsComponent.setEirika();
		
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

		// Combat Animation
		animationComponent.allAnimationObjects.put("CombatAnimationRegularHit", new AnimationObject(assetManager, 
				"units/eirika/erikaBattleAnimation.png", 60, 80, 
				1/3f, 0, 0, 11));
		animationComponent.allAnimationObjects.get("CombatAnimationRegularHit").useSynchronizedTimer = false;
		
		// Set Default Animation
		animationComponent.currentAnimation = animationComponent.allAnimationObjects.get("Idle");
		animationComponent.currentAnimation.isDrawing = true;

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
		inventoryComponent.addItem(ItemFactory.createWeapon(ItemManager.getItem("Rapier"), assetManager, engine));
		inventoryComponent.addItem(ItemFactory.createWeapon(ItemManager.getItem("Iron Sword"), assetManager, engine));
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

	public static Entity createCavalierUnit(AssetManager assetManager, String animationFileLocation, Engine engine, MapProperties unitStats) {
		Entity cavalierUnit = new Entity();
		
		// Stats
		String name = unitStats.get("Name", String.class);
		float x = unitStats.get("x", Float.class);
		float y = unitStats.get("y", Float.class);
		int move = unitStats.get("Move", Integer.class);
		boolean isAlly = unitStats.get("isAlly", Boolean.class);
		
		// Components
		NameComponent nameComponent = new NameComponent(name);
		PositionComponent positionComponent = new PositionComponent(x, y);
		AnimationComponent animationComponent = new AnimationComponent();
		MovementStatsComponent movementStatsComponent = new MovementStatsComponent(move, isAlly);
		UnitStatsComponent unitStatsComponent = new UnitStatsComponent(unitStats);
		ZOrderComponent zOrderComponent = new ZOrderComponent(ZOrder.MIDDLE_LAYER);
		SoundComponent soundComponent = new SoundComponent();
		InventoryComponent inventoryComponent = new InventoryComponent();
		IconComponent iconComponent = new IconComponent();
		
		// Icon | Default Icon for now
		Entity icon = IconFactory.createUnitIcon(assetManager, "units/cavalier/sethPortrait.png", engine);
		iconComponent.iconEntity = icon;
		
		
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
		
		// Combat
		animationComponent.allAnimationObjects.put("CombatAnimationRegularHit", new AnimationObject(assetManager, 
				"units/cavalier/paladinAttackAnimation.png", 88, 94, 
				1/5f, 0, 0, 26));
		animationComponent.allAnimationObjects.get("CombatAnimationRegularHit").useSynchronizedTimer = false;
		
		// Off sets
		animationComponent.allAnimationObjects.get("Left").Xoffset = 0;
		animationComponent.allAnimationObjects.get("Right").Xoffset = -12;
		animationComponent.allAnimationObjects.get("Up").Xoffset = -3;
		animationComponent.allAnimationObjects.get("Down").Xoffset = -4;
		animationComponent.allAnimationObjects.get("Hovering").Xoffset = -3;
		animationComponent.allAnimationObjects.get("Idle").Xoffset = -3;
		animationComponent.allAnimationObjects.get("Selected").Xoffset = -12;
		
		// Add AI component if not ally
		if (!isAlly) {
			Artifical_IntelligenceComponent aiComponent = new Artifical_IntelligenceComponent();
			boolean isAggresive = unitStats.get("aiType", Boolean.class);
			if (isAggresive) {
				aiComponent.ai_Type = AI_TYPE.AGGRESSIVE;
			} else {
				aiComponent.ai_Type = AI_TYPE.PASSIVE;
			}
			cavalierUnit.add(aiComponent);
		}
		
		// Inventory Component
		inventoryComponent.addItem(ItemFactory.createWeapon(ItemManager.getItem("Steel Sword"), assetManager, engine));
		inventoryComponent.addItem(ItemFactory.createWeapon(ItemManager.getItem("Silver Lance"), assetManager, engine));
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
	
	public static Entity createBandit(AssetManager assetManager, String animationFileLocation, Engine engine, MapProperties unitStats) {
		Entity bandit = new Entity();
		
		// Properties from tiled
		String name = unitStats.get("Name", String.class);
		float x = unitStats.get("x", Float.class);
		float y = unitStats.get("y", Float.class);
		int moveSteps = unitStats.get("Move", Integer.class);
		boolean isAlly = unitStats.get("isAlly", Boolean.class);
		String equippedItemName = unitStats.get("Weapon", String.class);
		
		// Components
		NameComponent nameComponent = new NameComponent(name);
		PositionComponent positionComponent = new PositionComponent(x, y);
		AnimationComponent animationComponent = new AnimationComponent();
		MovementStatsComponent movementStatsComponent = new MovementStatsComponent();
		UnitStatsComponent unitStatsComponent = new UnitStatsComponent(unitStats);
		ZOrderComponent zOrderComponent = new ZOrderComponent(ZOrder.MIDDLE_LAYER);
		SoundComponent soundComponent = new SoundComponent();
		InventoryComponent inventoryComponent = new InventoryComponent();
		IconComponent iconComponent = new IconComponent();
		
		// Icon
		Entity icon = IconFactory.createUnitIcon(assetManager, "units/enemyPortrait/normalSoldierPortrait.png", engine);
		iconComponent.iconEntity = icon;
		
		
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
		
		// Animation Offsets
		animationComponent.allAnimationObjects.get("Left").Xoffset = -8;
		animationComponent.allAnimationObjects.get("Right").Xoffset = -8;
		animationComponent.allAnimationObjects.get("Up").Xoffset = -8;
		animationComponent.allAnimationObjects.get("Down").Xoffset = -8;
		animationComponent.allAnimationObjects.get("Hovering").Xoffset = -8;
		animationComponent.allAnimationObjects.get("Idle").Xoffset = -8;
		animationComponent.allAnimationObjects.get("Selected").Xoffset = -8;

		// Set Default Animation
		animationComponent.currentAnimation = animationComponent.allAnimationObjects.get("Idle");
		
		// Combat
		animationComponent.allAnimationObjects.put("CombatAnimationRegularHit", new AnimationObject(assetManager, "units/bandit/fighterRegular.png", 
				243, 213, 1/10f, 0, 0, 25));
		animationComponent.allAnimationObjects.get("CombatAnimationRegularHit").useSynchronizedTimer = false;
		
		// Unit Stats
		movementStatsComponent.movementSteps = moveSteps;
		movementStatsComponent.isAlly = isAlly;
		
		// Add AI component if not ally
		if (!isAlly) {
			Artifical_IntelligenceComponent aiComponent = new Artifical_IntelligenceComponent();
			boolean isAggresive = unitStats.get("aiType", Boolean.class);
			if (isAggresive) {
				aiComponent.ai_Type = AI_TYPE.AGGRESSIVE;
			} else {
				aiComponent.ai_Type = AI_TYPE.PASSIVE;
			}
			bandit.add(aiComponent);
		}
		
		// Inventory Component
		inventoryComponent.addItem(ItemFactory.createWeapon(ItemManager.getItem(equippedItemName), assetManager, engine));
		inventoryComponent.selectedItem = inventoryComponent.inventory[0];
		
		// Add Components
		bandit.add(unitStatsComponent);
		bandit.add(movementStatsComponent);
		bandit.add(animationComponent);
		bandit.add(positionComponent);
		bandit.add(nameComponent);
		bandit.add(zOrderComponent);
		bandit.add(soundComponent);
		bandit.add(inventoryComponent);
		bandit.add(iconComponent);
		
		return bandit;
	}
}