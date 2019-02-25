package com.jb.fe.systems.graphics;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.jb.fe.components.AnimationComponent;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.components.StaticImageComponent;
import com.jb.fe.components.UnitStatsComponent;
import com.jb.fe.components.ZOrderComponent;
import com.jb.fe.systems.camera.CameraSystem;
import com.jb.fe.systems.gamePlay.CombatSystem;

public class CombatAnimationSystem extends EntitySystem {
	
	public static boolean canRetaliate = false;
	public static boolean isProcessing = false;
	
	public static int damageFromAttacker;
	public static int damageFromDefender;
	
	private ComponentMapper<AnimationComponent> aComponentMapper = ComponentMapper.getFor(AnimationComponent.class);
	private ComponentMapper<PositionComponent> pComponentMapper = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<StaticImageComponent> sComponentMapper = ComponentMapper.getFor(StaticImageComponent.class);
	private ComponentMapper<UnitStatsComponent> uComponentMapper = ComponentMapper.getFor(UnitStatsComponent.class);
	
	private float defX, defY;
	private float atkX, atkY;
	
	private Entity defendingNonMovingEntity;
	
	private float animationTimer;
	private float animationRetaliateTimer;
	
	private float healthSpeedDecrease = 25;
	
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
		aComponentMapper.get(CombatSystem.attackingUnit).currentAnimation.isDrawing = true;
		aComponentMapper.get(CombatSystem.attackingUnit).currentAnimation.width = 50;
		aComponentMapper.get(CombatSystem.attackingUnit).currentAnimation.height = 50;
		
		aComponentMapper.get(CombatSystem.defendingUnit).currentAnimation = aComponentMapper.get(CombatSystem.defendingUnit).allAnimationObjects.get("CombatAnimationRegularHit");
		aComponentMapper.get(CombatSystem.defendingUnit).currentAnimation.width = 50;
		aComponentMapper.get(CombatSystem.defendingUnit).currentAnimation.height = 50;
		aComponentMapper.get(CombatSystem.defendingUnit).currentAnimation.isLooping = false;
		aComponentMapper.get(CombatSystem.attackingUnit).currentAnimation.isDrawing = false;
		
		// Set First Frame for defending unit
		sComponentMapper.get(defendingNonMovingEntity).staticImage = aComponentMapper.get(CombatSystem.attackingUnit).currentAnimation.animationFrames.getKeyFrames()[0];
		sComponentMapper.get(defendingNonMovingEntity).width = 50;
		sComponentMapper.get(defendingNonMovingEntity).height = 50;
		sComponentMapper.get(defendingNonMovingEntity).isEnabled = true;
		
		pComponentMapper.get(defendingNonMovingEntity).x = CameraSystem.cameraX - CameraSystem.xConstant + 50;
		pComponentMapper.get(defendingNonMovingEntity).y = CameraSystem.cameraY - CameraSystem.yConstant + 50;
	}
	
	@Override
	public void update(float delta) {
		if (!isProcessing) { return; }
		
		animationTimer += delta;
		//System.out.println("Here");
		// Check if attacking animations are done
		if (!aComponentMapper.get(CombatSystem.attackingUnit).currentAnimation.animationFrames.isAnimationFinished(animationTimer)) {
			return;
		}
		
		// Set HP of the defender
		UnitStatsComponent defUnitStatsComponent = uComponentMapper.get(CombatSystem.defendingUnit);
		defUnitStatsComponent.health -= (healthSpeedDecrease * Gdx.graphics.getDeltaTime());
		System.out.println("Health: " + defUnitStatsComponent.health);
	}
	 
	/**
	 * Call this when you end the animation
	 */
	public void stopAnimation() {
		// Reset Animation timers
		animationTimer = 0;
		animationRetaliateTimer = 0;
		aComponentMapper.get(CombatSystem.attackingUnit).currentAnimation.animationElapsedTime = 0;
		aComponentMapper.get(CombatSystem.defendingUnit).currentAnimation.animationElapsedTime = 0;
	}
	
	/**
	 * Create initial entity
	 */
	public void createInitialEntity() {
		defendingNonMovingEntity = new Entity();
		StaticImageComponent defStaticImageComponent = new StaticImageComponent();
		defStaticImageComponent.isEnabled = false;
		PositionComponent defPositionComponent = new PositionComponent();
		ZOrderComponent defzOrderComponent = new ZOrderComponent(ZOrder.COMBAT_ACTORS);
		
		defendingNonMovingEntity.add(defStaticImageComponent);
		defendingNonMovingEntity.add(defPositionComponent);
		defendingNonMovingEntity.add(defzOrderComponent);
		getEngine().addEntity(defendingNonMovingEntity);
	}
}
