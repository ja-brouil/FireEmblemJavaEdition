package com.jb.fe.UI.combatUIScreen;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.jb.fe.UI.UserInterfaceState;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.components.StaticImageComponent;
import com.jb.fe.components.ZOrderComponent;
import com.jb.fe.screens.FireEmblemGame;
import com.jb.fe.systems.audio.SoundSystem;
import com.jb.fe.systems.camera.CameraSystem;
import com.jb.fe.systems.graphics.ZOrder;
import com.jb.fe.systems.inputAndUI.UserInterfaceManager;

public class CombatScreenUI extends UserInterfaceState{
	
	private Entity combatScreenEntity;

	public CombatScreenUI(AssetManager assetManager, SoundSystem soundSystem,
			UserInterfaceManager userInterfaceManager) {
		super(assetManager, soundSystem, userInterfaceManager);
		
		combatScreenEntity = new Entity();
		
		StaticImageComponent staticImageComponent = new StaticImageComponent(assetManager, "UI/combatUI/combatUI.png");
		staticImageComponent.width = FireEmblemGame.WIDTH * FireEmblemGame.CONSTANT;
		staticImageComponent.height = FireEmblemGame.HEIGHT * FireEmblemGame.CONSTANT;
		staticImageComponent.isEnabled = false;
		
		PositionComponent positionComponent = new PositionComponent();
		
		ZOrderComponent zOrderComponent = new ZOrderComponent(ZOrder.COMBAT_UI);
		combatScreenEntity.add(zOrderComponent);
		combatScreenEntity.add(positionComponent);
		combatScreenEntity.add(staticImageComponent);
		
		
	}

	@Override
	public void startState() {
		// Enable and set position
		staticImageComponentMapper.get(combatScreenEntity).isEnabled = true;
		
		pComponentMapper.get(combatScreenEntity).x = CameraSystem.cameraX - CameraSystem.xConstant;
		pComponentMapper.get(combatScreenEntity).y = CameraSystem.cameraY - CameraSystem.yConstant;
	}

	@Override
	public void resetState() {
		staticImageComponentMapper.get(combatScreenEntity).isEnabled = false;
	}

	@Override
	public void nextState() {
		
	}

	@Override
	public void handleInput(float delta) {
		
	}
	
	@Override
	public Entity getMainEntity() {
		return combatScreenEntity;
	}
}
