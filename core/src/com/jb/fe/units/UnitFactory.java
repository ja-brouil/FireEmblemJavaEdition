package com.jb.fe.units;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.jb.fe.components.AnimationComponent;
import com.jb.fe.components.AnimationObject;
import com.jb.fe.components.NameComponent;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.components.UnitStatsComponent;

/*
 * This creates player units
 */
public class UnitFactory {

	// Asset Manager
	private AssetManager assetManager;

	// Sound Effects
	// TO DO

	public UnitFactory(AssetManager assetManager) {
		this.assetManager = assetManager;
	}

	/*
	 * Create Eirika
	 */
	public Entity createEirika(String name, String animationFileLocation, float x, float y, boolean isAlly) {
		Entity Eirika = new Entity();

		// Components
		NameComponent nameComponent = new NameComponent(name);
		PositionComponent positionComponent = new PositionComponent(x, y);
		AnimationComponent animationComponent = new AnimationComponent();
		UnitStatsComponent unitStatsComponent = new UnitStatsComponent();

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

		// Z order
		animationComponent.zOrder = 1;

		// Unit Stats
		unitStatsComponent.isAlly = isAlly;

		// Add Components
		Eirika.add(nameComponent);
		Eirika.add(positionComponent);
		Eirika.add(animationComponent);
		Eirika.add(unitStatsComponent);

		return Eirika;
	}

	/**
	 * Creates a cavalier unit
	 * 
	 * @return
	 */
	public Entity createCavalierUnit(String name, String animationFileLocation, float x, float y, boolean isAlly) {
		Entity cavalierUnit = new Entity();

		// Components
		NameComponent nameComponent = new NameComponent(name);
		PositionComponent positionComponent = new PositionComponent(x, y);
		AnimationComponent animationComponent = new AnimationComponent();
		UnitStatsComponent unitStatsComponent = new UnitStatsComponent();

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
		unitStatsComponent.movementSteps = 7;
		unitStatsComponent.isAlly = isAlly;
		
		// Add Components
		cavalierUnit.add(unitStatsComponent);
		cavalierUnit.add(animationComponent);
		cavalierUnit.add(positionComponent);
		cavalierUnit.add(nameComponent);

		return cavalierUnit;
	}
}