package com.jb.fe.UI;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.jb.fe.UI.actionMenu.ActionMenuUpdate;
import com.jb.fe.audio.SoundObject;
import com.jb.fe.components.AnimationComponent;
import com.jb.fe.components.AnimationObject;
import com.jb.fe.components.InputComponent;
import com.jb.fe.components.MapCursorStateComponent;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.components.SoundComponent;
import com.jb.fe.components.StaticImageComponent;
import com.jb.fe.components.UIComponent;
import com.jb.fe.components.UITextComponent;
import com.jb.fe.components.ZOrderComponent;
import com.jb.fe.components.MapCursorStateComponent.MapCursorState;
import com.jb.fe.components.NameComponent;
import com.jb.fe.level.Level;
import com.jb.fe.screens.FireEmblemGame;
import com.jb.fe.systems.audio.SoundSystem;
import com.jb.fe.systems.graphics.ZOrderDictionnary;
import com.jb.fe.systems.input.MapCursorInfoUpdate;

/*
 * Creates a map cursor for the main battlefield
 */
public class UIFactory {
	
	// Graphic Variables
	private AssetManager assetManager;
	
	// Audio
	private SoundSystem soundSystem;
	
	// Camera
	private OrthographicCamera camera;
	
	public UIFactory(AssetManager assetManager, SoundSystem soundSystem, OrthographicCamera camera) {
		this.assetManager = assetManager;
		this.soundSystem = soundSystem;
		this.camera = camera;
	}
	
	public Entity createMapCursor(Level level, Engine engine) {
		Entity mapCursor = new Entity();
		
		// Components
		AnimationComponent animationComponent = new AnimationComponent();
		animationComponent.allAnimationObjects.put("Regular", new AnimationObject(assetManager, "UI/Cursor/cursor.png", 320 / 8, 42, 1f / 12f, 0, 0, 8));
		animationComponent.currentAnimation = animationComponent.allAnimationObjects.get("Regular");
		animationComponent.currentAnimation.width = FireEmblemGame.WIDTH / 15;
		animationComponent.currentAnimation.height = FireEmblemGame.HEIGHT / 10;
		
		PositionComponent positionComponent = new PositionComponent(16,16);
		
		MapCursorStateComponent mapCursorStateComponent = new MapCursorStateComponent();
		mapCursorStateComponent.mapCursorState = MapCursorState.DISABLED;
		
		StaticImageComponent staticImageComponent = new StaticImageComponent(assetManager, "UI/Cursor/staticCursor.png");
		staticImageComponent.xOffset = -3.5f;
		
		ZOrderComponent zOrderComponent = new ZOrderComponent(ZOrderDictionnary.TOP_LAYER);
		
		SoundComponent soundComponent = new SoundComponent();
		soundComponent.allSoundObjects.put("Accept", new SoundObject("sound/accept.wav", assetManager));
		soundComponent.allSoundObjects.put("Back", new SoundObject("sound/backSound.wav", assetManager));
		soundComponent.allSoundObjects.put("Movement", new SoundObject("sound/cursorMovement.wav", assetManager));
		soundComponent.allSoundObjects.put("Invalid", new SoundObject("sound/Not Allowed.mp3", assetManager));
		soundComponent.allSoundObjects.put("Select Unit", new SoundObject("sound/selectUnit.wav", assetManager));
		
		InputComponent inputComponent = new InputComponent();
		inputComponent.inputHandling = new MapCursorInputHandling(mapCursorStateComponent, positionComponent, soundSystem, mapCursor, camera);
		inputComponent.isEnabled = true;
		
		UIComponent uiComponent = new UIComponent();
		MapCursorInfoUpdate mapCursorInfoUpdate = new MapCursorInfoUpdate();
		mapCursorInfoUpdate.startSystem(level, engine, mapCursor);
		uiComponent.updateUI = mapCursorInfoUpdate;
		uiComponent.isEnabled = true;
		
		NameComponent nameComponent = new NameComponent("Map Cursor");
		
		// Add components
		mapCursor.add(nameComponent);
		mapCursor.add(uiComponent);
		mapCursor.add(animationComponent);
		mapCursor.add(positionComponent);
		mapCursor.add(mapCursorStateComponent);
		mapCursor.add(inputComponent);
		mapCursor.add(staticImageComponent);
		mapCursor.add(zOrderComponent);
		mapCursor.add(soundComponent);
		return mapCursor;
	}
	
	public Entity createActionMenu() {
		Entity actionMenu = new Entity();
		
		// Components
		NameComponent nameComponent = new NameComponent("Action Menu");
		
		PositionComponent positionComponent = new PositionComponent();
		
		InputComponent inputComponent = new InputComponent();
		
		UIComponent uiComponent = new UIComponent();
		uiComponent.updateUI = new ActionMenuUpdate(actionMenu);
		
		UITextComponent uiTextComponent = new UITextComponent();
		uiTextComponent.textArray.add("Attack");
		uiTextComponent.textArray.add("Items");
		uiTextComponent.textArray.add("Trade");
		uiTextComponent.textArray.add("Wait");
		
		// Add Components
		actionMenu.add(nameComponent);
		actionMenu.add(positionComponent);
		actionMenu.add(uiTextComponent);
		
		return actionMenu;
	}
	
	public Entity createDamagePreview() {
		Entity damagePreview = new Entity();
		
		
		return damagePreview;
	}
}
