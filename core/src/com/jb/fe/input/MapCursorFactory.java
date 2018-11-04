package com.jb.fe.input;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.jb.fe.audio.SoundObject;
import com.jb.fe.components.AnimationComponent;
import com.jb.fe.components.AnimationObject;
import com.jb.fe.components.InputComponent;
import com.jb.fe.components.MapCursorStateComponent;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.components.SoundComponent;
import com.jb.fe.components.StaticImageComponent;
import com.jb.fe.components.ZOrderComponent;
import com.jb.fe.screens.FireEmblemGame;
import com.jb.fe.systems.audio.SoundSystem;
import com.jb.fe.systems.graphics.ZOrderDictionnary;

/*
 * Creates a map cursor for the main battlefield
 */
public class MapCursorFactory {
	
	// Graphic Variables
	private AssetManager assetManager;
	
	// Audio
	private SoundSystem soundSystem;
	
	public MapCursorFactory(AssetManager assetManager, SoundSystem soundSystem) {
		this.assetManager = assetManager;
		this.soundSystem = soundSystem;
	}
	
	public Entity createMapCursor() {
		Entity mapCursor = new Entity();
		
		// Components
		AnimationComponent animationComponent = new AnimationComponent();
		animationComponent.allAnimationObjects.put("Regular", new AnimationObject(assetManager, "UI/Cursor/cursor.png", 320 / 8, 42, 1f / 12f, 0, 0, 8));
		animationComponent.currentAnimation = animationComponent.allAnimationObjects.get("Regular");
		animationComponent.currentAnimation.width = FireEmblemGame.WIDTH / 15;
		animationComponent.currentAnimation.height = FireEmblemGame.HEIGHT / 10;
		
		PositionComponent positionComponent = new PositionComponent(16,16);
		
		MapCursorStateComponent mapCursorStateComponent = new MapCursorStateComponent();
		
		StaticImageComponent staticImageComponent = new StaticImageComponent(assetManager, "UI/Cursor/staticCursor.png");
		staticImageComponent.xOffset = -3.5f;
		
		ZOrderComponent zOrderComponent = new ZOrderComponent(ZOrderDictionnary.TOP_LAYER);
		
		SoundComponent soundComponent = new SoundComponent();
		soundComponent.allSoundObjects.put("Accept", new SoundObject("sound/accept.wav", assetManager));
		soundComponent.allSoundObjects.put("Back", new SoundObject("sound/backSound.wav", assetManager));
		soundComponent.allSoundObjects.put("Movement", new SoundObject("sound/cursorMovement.wav", assetManager));
		soundComponent.allSoundObjects.put("Invalid", new SoundObject("sound/Not Allowed.mp3", assetManager));
		
		InputComponent inputComponent = new InputComponent();
		inputComponent.inputHandling = new MapCursorInputHandling(mapCursorStateComponent, positionComponent, soundSystem, mapCursor);
		
		// Add components
		mapCursor.add(animationComponent);
		mapCursor.add(positionComponent);
		mapCursor.add(mapCursorStateComponent);
		mapCursor.add(inputComponent);
		mapCursor.add(staticImageComponent);
		mapCursor.add(zOrderComponent);
		mapCursor.add(soundComponent);
		return mapCursor;
	}
}
