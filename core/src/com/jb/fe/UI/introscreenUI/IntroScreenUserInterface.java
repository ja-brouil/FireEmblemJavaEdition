package com.jb.fe.UI.introscreenUI;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.jb.fe.UI.UserInterfaceState;
import com.jb.fe.components.AnimationComponent;
import com.jb.fe.components.AnimationObject;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.components.StaticImageComponent;
import com.jb.fe.components.ZOrderComponent;
import com.jb.fe.screens.FireEmblemGame;
import com.jb.fe.screens.GameScreen;
import com.jb.fe.screens.IntroScreen;
import com.jb.fe.systems.audio.SoundSystem;
import com.jb.fe.systems.graphics.ZOrder;
import com.jb.fe.systems.inputAndUI.UserInterfaceManager;

public class IntroScreenUserInterface extends UserInterfaceState {

	private Entity introScreenBackground;
	private Entity introScreenSword;
	private Entity introScreenLogo;
	private Entity pressStartAnimationEntity;
	
	public IntroScreenUserInterface(AssetManager assetManager, SoundSystem soundSystem,
			UserInterfaceManager userInterfaceManager) {
		super(assetManager, soundSystem, userInterfaceManager);
		
		// Entities
		introScreenBackground = new Entity();
		introScreenBackground.add(new StaticImageComponent(assetManager, "titleScreen/menuBackground.png", 240, 160, 240, 160));
		introScreenBackground.add(new PositionComponent());
		introScreenBackground.add(new ZOrderComponent(ZOrder.BACKGROUND));
		
		introScreenSword = new Entity();
		introScreenSword.add(new StaticImageComponent(assetManager, "titleScreen/menuSword.png", 240, 160, 240, 160));
		introScreenSword.add(new PositionComponent());
		introScreenSword.add(new ZOrderComponent(ZOrder.MIDDLE_LAYER));
		
		introScreenLogo = new Entity();
		introScreenLogo.add(new StaticImageComponent(assetManager, "titleScreen/menuTitle.png", 240, 160));
		introScreenLogo.add(new PositionComponent());
		introScreenLogo.add(new ZOrderComponent(ZOrder.TOP_LAYER));
		
		pressStartAnimationEntity = new Entity();
		AnimationComponent animationComponent = new AnimationComponent();
		animationComponent.allAnimationObjects.put("Press Start", new AnimationObject(assetManager, "titleScreen/pressStart.png", 80, 15, AnimationObject.DEFAULT_ANIMATION_TIMER, 8));
		animationComponent.currentAnimation = animationComponent.allAnimationObjects.get("Press Start");
		animationComponent.currentAnimation.animationFrames.setPlayMode(PlayMode.LOOP_PINGPONG);
		pressStartAnimationEntity.add(animationComponent);
		pressStartAnimationEntity.add(new PositionComponent(FireEmblemGame.WIDTH / 2 - 40, 30));
		pressStartAnimationEntity.add(new ZOrderComponent(ZOrder.MIDDLE_LAYER));
	}

	@Override
	public void startState() {
		
	}

	@Override
	public void resetState() {
		
	}

	@Override
	public void nextState() {
		
	}

	@Override
	public void handleInput(float delta) {
		// Press any key to go to the next screen
		if (Gdx.input.isKeyJustPressed(Keys.ANY_KEY)) {
			// Off to the next screen
			
			((IntroScreen) userInterfaceManager.fireEmblemGame.getScreen()).nextScreen(FireEmblemGame.allGameScreens.get("GameScreen"));
			
			((GameScreen) FireEmblemGame.allGameScreens.get("GameScreen")).startGameScreen();
		}
	}
	
	public void addEntities(Engine engine) {
		engine.addEntity(introScreenBackground);
		engine.addEntity(introScreenLogo);
		engine.addEntity(introScreenSword);
		engine.addEntity(pressStartAnimationEntity);
	}
	
}
