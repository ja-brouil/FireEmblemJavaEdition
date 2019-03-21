package com.jb.fe.UI.introscreenUI;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.jb.fe.UI.UserInterfaceState;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.components.StaticImageComponent;
import com.jb.fe.components.ZOrderComponent;
import com.jb.fe.systems.audio.SoundSystem;
import com.jb.fe.systems.graphics.ZOrder;
import com.jb.fe.systems.inputAndUI.UserInterfaceManager;

public class IntroScreenUserInterface extends UserInterfaceState {

	private Entity introScreenBackground;
	private Entity introScreenSword;
	private Entity introScreenLogo;
	
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
		introScreenLogo.add(new ZOrderComponent(ZOrder.MIDDLE_LAYER));
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
		
	}
	
	public void addEntities(Engine engine) {
		engine.addEntity(introScreenBackground);
		engine.addEntity(introScreenLogo);
		engine.addEntity(introScreenSword);
	}
	
}
