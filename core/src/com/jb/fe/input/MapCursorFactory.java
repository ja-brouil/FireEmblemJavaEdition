package com.jb.fe.input;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.jb.fe.components.AnimationComponent;
import com.jb.fe.components.AnimationObject;
import com.jb.fe.components.InputComponent;
import com.jb.fe.components.MapCursorStateComponent;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.screens.FireEmblemGame;

/*
 * Creates a map cursor for the main battlefield
 */
public class MapCursorFactory {
	
	// Graphic Variables
	private AssetManager assetManager;
	
	// Audio
	// to add
	
	
	// Static Graphic
	// To add
	
	public MapCursorFactory(AssetManager assetManager) {
		this.assetManager = assetManager;
	}
	
	public Entity createMapCursor() {
		Entity mapCursor = new Entity();
		
		// Components
		AnimationComponent animationComponent = new AnimationComponent();
		animationComponent.allAnimationObjects.put("Regular", new AnimationObject(assetManager, "UI/Cursor/cursor.png", 320 / 8, 42, 1f / 12f, 0, 0, 8));
		animationComponent.zOrder = 0;
		animationComponent.currentAnimation = animationComponent.allAnimationObjects.get("Regular");
		animationComponent.currentAnimation.width = FireEmblemGame.WIDTH / 15;
		animationComponent.currentAnimation.height = FireEmblemGame.HEIGHT / 10;
		
		PositionComponent positionComponent = new PositionComponent(16,16);
		
		MapCursorStateComponent mapCursorStateComponent = new MapCursorStateComponent();
		
		InputComponent inputComponent = new InputComponent();
		inputComponent.inputHandling = new MapCursorInputHandling(mapCursorStateComponent, positionComponent);
		
		// Add components
		mapCursor.add(animationComponent);
		mapCursor.add(positionComponent);
		mapCursor.add(mapCursorStateComponent);
		mapCursor.add(inputComponent);
		return mapCursor;
	}
}
