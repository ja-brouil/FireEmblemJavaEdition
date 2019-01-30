package com.jb.fe.systems.graphics;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.EntitySystem;
import com.jb.fe.components.AnimationComponent;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.systems.camera.CameraSystem;
import com.jb.fe.systems.gamePlay.CombatSystem;

public class CombatAnimationSystem extends EntitySystem {
	
	public static boolean canRetaliate = false;
	public static boolean isProcessing = false;
	
	private ComponentMapper<AnimationComponent> aComponentMapper = ComponentMapper.getFor(AnimationComponent.class);
	private ComponentMapper<PositionComponent> pComponentMapper = ComponentMapper.getFor(PositionComponent.class);
	
	private float defX, defY;
	private float atkX, atkY;
	
	// Add this to the engine priority later
	public CombatAnimationSystem() {
		setProcessing(false);
	}
	
	/**
	 * Call this when you start the animations
	 */
	public void startAnimation() {
		// Attacking Unit
		atkX = pComponentMapper.get(CombatSystem.attackingUnit).x;
		atkY = pComponentMapper.get(CombatSystem.attackingUnit).y;
		
		// Defending Unit
		defX = pComponentMapper.get(CombatSystem.defendingUnit).x;
		defY = pComponentMapper.get(CombatSystem.defendingUnit).y;
		
		// Set New Location
		pComponentMapper.get(CombatSystem.attackingUnit).x = CameraSystem.cameraX - CameraSystem.xConstant + 50;
		pComponentMapper.get(CombatSystem.attackingUnit).y = CameraSystem.cameraY - CameraSystem.yConstant + 50;
		
		pComponentMapper.get(CombatSystem.defendingUnit).x = CameraSystem.cameraX - CameraSystem.xConstant + 100;
		pComponentMapper.get(CombatSystem.defendingUnit).y = CameraSystem.cameraY - CameraSystem.yConstant + 50;
		
		// Set Size and looping
		aComponentMapper.get(CombatSystem.attackingUnit).currentAnimation = aComponentMapper.get(CombatSystem.attackingUnit).allAnimationObjects.get("CombatAnimationRegularHit");
		aComponentMapper.get(CombatSystem.attackingUnit).currentAnimation.isLooping = false;
		aComponentMapper.get(CombatSystem.attackingUnit).currentAnimation.width = 50;
		aComponentMapper.get(CombatSystem.attackingUnit).currentAnimation.height = 50;
		aComponentMapper.get(CombatSystem.defendingUnit).currentAnimation = aComponentMapper.get(CombatSystem.defendingUnit).allAnimationObjects.get("CombatAnimationRegularHit");
		aComponentMapper.get(CombatSystem.defendingUnit).currentAnimation.width = 50;
		aComponentMapper.get(CombatSystem.defendingUnit).currentAnimation.height = 50;
		aComponentMapper.get(CombatSystem.defendingUnit).currentAnimation.isLooping = false;
	}
	
	@Override
	public void update(float delta) {
		if (!isProcessing) { return; }
	}
	
	/**
	 * Call this when you end the animation
	 */
	public void stopAnimation() {
		
	}
}
