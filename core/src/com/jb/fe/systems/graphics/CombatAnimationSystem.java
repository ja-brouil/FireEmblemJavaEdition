package com.jb.fe.systems.graphics;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.jb.fe.UI.combatUIScreen.CombatScreenUI;
import com.jb.fe.components.AnimationComponent;
import com.jb.fe.components.MovementStatsComponent;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.components.StaticImageComponent;
import com.jb.fe.components.UnitStatsComponent;
import com.jb.fe.components.ZOrderComponent;
import com.jb.fe.systems.camera.CameraSystem;
import com.jb.fe.systems.gamePlay.CombatSystem;
import com.jb.fe.systems.gamePlay.CombatSystemCalculator;

public class CombatAnimationSystem extends EntitySystem {
	
	// Change these to enumerators later
	public static boolean canRetaliate = false;
	public static boolean isProcessing = false;
	public static boolean combatAnimationsAreComplete = false;
	
	// Numbers for the combat system
	public static int damageFromAttacker;
	public static int damageFromDefender;
	
	// Old positions for return
	public float attackingX, attackingY;
	public float defendingX, defendingY;
	
	// Combat State
	private enum CombatSystemState {
		Attacking, Defending
	}
	private CombatSystemState combatSystemState;
	
	// Mappers
	private ComponentMapper<AnimationComponent> aComponentMapper = ComponentMapper.getFor(AnimationComponent.class);
	private ComponentMapper<PositionComponent> pComponentMapper = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<StaticImageComponent> sComponentMapper = ComponentMapper.getFor(StaticImageComponent.class);
	private ComponentMapper<UnitStatsComponent> uComponentMapper = ComponentMapper.getFor(UnitStatsComponent.class);
	private ComponentMapper<MovementStatsComponent> mComponentMapper = ComponentMapper.getFor(MovementStatsComponent.class);
	
	// Placeholder entities
	private Entity defendingNonMovingEntity;
	private Entity backgroundImageEntity;
	
	// Timers for decreasing HP
	private float healthTimerAtk;
	private float healthTimerDef;
	
	// Access to the UI
	private CombatScreenUI combatScreenUI;
	
	// Add this to the engine priority later
	public CombatAnimationSystem(CombatScreenUI combatScreenUI) {
		setProcessing(false);
		this.combatScreenUI = combatScreenUI;
	}
	
	/**
	 * Call this when you start the animations
	 */
	public void startAnimation() {
		// Set Attacking Unit State
		combatSystemState = CombatSystemState.Attacking;
		healthTimerAtk = 0;
		healthTimerDef = 0;
		
		// Set Initial Locations
		attackingX = pComponentMapper.get(CombatSystem.attackingUnit).x;
		attackingY = pComponentMapper.get(CombatSystem.attackingUnit).y;
		defendingX = pComponentMapper.get(CombatSystem.defendingUnit).x;
		defendingY = pComponentMapper.get(CombatSystem.defendingUnit).y;
		
		// Set New Location
		// Ally
		if (mComponentMapper.get(CombatSystem.attackingUnit).isAlly) {
			pComponentMapper.get(CombatSystem.attackingUnit).x = CameraSystem.cameraX - CameraSystem.xConstant + 110;
			pComponentMapper.get(CombatSystem.attackingUnit).y = CameraSystem.cameraY - CameraSystem.yConstant + 50;
			
			pComponentMapper.get(CombatSystem.defendingUnit).x = CameraSystem.cameraX - CameraSystem.xConstant - 10;
			pComponentMapper.get(CombatSystem.defendingUnit).y = CameraSystem.cameraY - CameraSystem.yConstant + 50;
			
			pComponentMapper.get(defendingNonMovingEntity).x = CameraSystem.cameraX - CameraSystem.xConstant - 10;
			pComponentMapper.get(defendingNonMovingEntity).y = CameraSystem.cameraY - CameraSystem.yConstant + 50;
		} else {
			pComponentMapper.get(CombatSystem.attackingUnit).x = CameraSystem.cameraX - CameraSystem.xConstant - 10;
			pComponentMapper.get(CombatSystem.attackingUnit).y = CameraSystem.cameraY - CameraSystem.yConstant + 50;
			
			pComponentMapper.get(CombatSystem.defendingUnit).x = CameraSystem.cameraX - CameraSystem.xConstant + 110;
			pComponentMapper.get(CombatSystem.defendingUnit).y = CameraSystem.cameraY - CameraSystem.yConstant + 50;
			
			pComponentMapper.get(defendingNonMovingEntity).x = CameraSystem.cameraX - CameraSystem.xConstant + 110;
			pComponentMapper.get(defendingNonMovingEntity).y = CameraSystem.cameraY - CameraSystem.yConstant + 50;
		}
		
		// Set Size and looping
		aComponentMapper.get(CombatSystem.attackingUnit).currentAnimation = aComponentMapper.get(CombatSystem.attackingUnit).allAnimationObjects.get("CombatAnimationRegularHit");
		aComponentMapper.get(CombatSystem.attackingUnit).currentAnimation.isLooping = false;
		aComponentMapper.get(CombatSystem.attackingUnit).currentAnimation.isDrawing = true;
		aComponentMapper.get(CombatSystem.attackingUnit).currentAnimation.width = 100;
		aComponentMapper.get(CombatSystem.attackingUnit).currentAnimation.height = 100;
		
		aComponentMapper.get(CombatSystem.defendingUnit).currentAnimation = aComponentMapper.get(CombatSystem.defendingUnit).allAnimationObjects.get("CombatAnimationRegularHit");
		aComponentMapper.get(CombatSystem.defendingUnit).currentAnimation.width = 100;
		aComponentMapper.get(CombatSystem.defendingUnit).currentAnimation.height = 100;
		aComponentMapper.get(CombatSystem.defendingUnit).currentAnimation.isLooping = false;
		aComponentMapper.get(CombatSystem.defendingUnit).currentAnimation.isDrawing = false;
		
		if (!mComponentMapper.get(CombatSystem.attackingUnit).isAlly) {
			for (TextureRegion animationRegion : aComponentMapper.get(CombatSystem.attackingUnit).currentAnimation.animationFrames.getKeyFrames()) {
				animationRegion.flip(true, false);
			}
		}
		
		if (!mComponentMapper.get(CombatSystem.defendingUnit).isAlly) {
			for (TextureRegion animationRegion : aComponentMapper.get(CombatSystem.defendingUnit).currentAnimation.animationFrames.getKeyFrames()) {
				animationRegion.flip(true, false);
			}
		}
		
		// Set First Frame for defending unit
		sComponentMapper.get(defendingNonMovingEntity).staticImage = aComponentMapper.get(CombatSystem.defendingUnit).currentAnimation.animationFrames.getKeyFrames()[0];
		sComponentMapper.get(defendingNonMovingEntity).width = 100;
		sComponentMapper.get(defendingNonMovingEntity).height = 100;
		sComponentMapper.get(defendingNonMovingEntity).isEnabled = true;
		
		// Reset Animation timers
		aComponentMapper.get(CombatSystem.attackingUnit).currentAnimation.animationElapsedTime = 0;
		aComponentMapper.get(CombatSystem.defendingUnit).currentAnimation.animationElapsedTime = 0;
		
		// Set Background
		// TO DO
	}
	
	@Override
	public void update(float delta) {
		if (!isProcessing) { return; }
		
		// Process Attack First
		if (combatSystemState.equals(CombatSystemState.Attacking)) {
			
			// Cache Attacker Animation
			AnimationComponent atkAnimationComponent = aComponentMapper.get(CombatSystem.attackingUnit);
			
			// Attacking sund and flash needs to be played here at some point
			
			// Check if attacking animations are done
			if (!atkAnimationComponent.currentAnimation.animationFrames.isAnimationFinished(atkAnimationComponent.currentAnimation.animationElapsedTime)){
				return;
			}
			
			// Set HP of the defender
			UnitStatsComponent defUnitStatsComponent = uComponentMapper.get(CombatSystem.defendingUnit);
			if (defUnitStatsComponent.health != CombatSystemCalculator.currentDefendingHealth - CombatSystemCalculator.AttackingDamage 
					&& defUnitStatsComponent.health > 0 && defUnitStatsComponent.health <= defUnitStatsComponent.maxHealth) {
				healthTimerAtk += delta;
				
				if (healthTimerAtk >= 0.15f) {
						// Damage
						if (CombatSystemCalculator.AttackingDamage >= 0) {
							defUnitStatsComponent.health -= 1;
							healthTimerAtk = 0;
							combatScreenUI.setHP(CombatSystem.attackingUnit, CombatSystem.defendingUnit);
						} else {
							// Healing
							defUnitStatsComponent.health += 1;
							healthTimerAtk = 0;
							combatScreenUI.setHP(CombatSystem.attackingUnit, CombatSystem.defendingUnit);
						}
				}
				return;
			}
			
			// Check if the unit has died
			if (defUnitStatsComponent.health <= 0) {
				defUnitStatsComponent.health = 0;
				
				// Play death sound + Flash
				System.out.println("Death sound + flash");
				
				// Remove the unit from the game
				getEngine().getSystem(CombatSystem.class).removeEntity(defUnitStatsComponent, CombatSystem.defendingUnit);
				
				// Can't reliate so we are done
				sComponentMapper.get(defendingNonMovingEntity).isEnabled = false;
				CombatAnimationSystem.combatAnimationsAreComplete = true;
				
				// Remove the Defending Unit
				CombatSystem.defendingUnit = null;
				
				// Clean up
				cleanUp();
				return;
			}
			
			/*
			// To do Healing | Prevent health from going above max
			// Stop Healing past max Health
			if (defUnitStatsComponent.health > defUnitStatsComponent.maxHealth) {
				defUnitStatsComponent.health = defUnitStatsComponent.maxHealth;
				// No retaliation on healing so we are done
				// TO DO
			}
			*/
			
			// Reached here means there is a retaliation
			combatSystemState = CombatSystemState.Defending;
			
			// Prepare other state
			// Graphics
			sComponentMapper.get(defendingNonMovingEntity).isEnabled = false;
			aComponentMapper.get(CombatSystem.defendingUnit).currentAnimation.isDrawing = true;
			
		} else {
			// Cache Defending Unit Animation
			AnimationComponent defAnimationComponent = aComponentMapper.get(CombatSystem.defendingUnit);
			
			// Play Attack Sound
			// TO DO
			
			// Check if Animation is done
			if (!defAnimationComponent.currentAnimation.animationFrames.isAnimationFinished(defAnimationComponent.currentAnimation.animationElapsedTime)) {
				return;
			}
			
			// Set HP of the Attacker
			UnitStatsComponent atkUnitStatsComponent = uComponentMapper.get(CombatSystem.attackingUnit);
			if (atkUnitStatsComponent.health != CombatSystemCalculator.currentAttackingHealth - CombatSystemCalculator.DefendingDamage 
					&& atkUnitStatsComponent.health > 0 && atkUnitStatsComponent.health <= atkUnitStatsComponent.maxHealth) {
				healthTimerDef += delta;						
				
				if (healthTimerDef >= 0.15f) {
					// Damage
					if (CombatSystemCalculator.DefendingDamage >= 0) {
						atkUnitStatsComponent.health -= 1;
						healthTimerDef = 0;
						combatScreenUI.setHP(CombatSystem.attackingUnit, CombatSystem.defendingUnit);
					} else {
						// Healing
						atkUnitStatsComponent.health += 1;
						healthTimerDef = 0;
						combatScreenUI.setHP(CombatSystem.attackingUnit, CombatSystem.defendingUnit);
					}
				}
				
				return;
			}
			
			// Stop Damage going past 0
			if (atkUnitStatsComponent.health <= 0) {
				atkUnitStatsComponent.health = 0;
				
				// We reached 0, remove the enemy unit	
				// Play Death Sound/ Flash
				System.out.println("Death sound + flash played");
				
				// Remove Unit
				getEngine().getSystem(CombatSystem.class).removeEntity(atkUnitStatsComponent, CombatSystem.attackingUnit);
				
				// Remove the initial entity
				sComponentMapper.get(defendingNonMovingEntity).isEnabled = false;
				CombatAnimationSystem.combatAnimationsAreComplete = true;
				
				// Remove Attacking Unit
				CombatSystem.attackingUnit = null;
				
				// Clean up
				cleanUp();
				return;
			}
			
			/*
			// Stop Healing past max Health
			if (atkUnitStatsComponent.health > atkUnitStatsComponent.maxHealth) {
				atkUnitStatsComponent.health = atkUnitStatsComponent.maxHealth;
				// No retaliation on healing so we are done
				// TO DO
			}
			*/
			
			// If we reach here, both units survived their attacks
			// Slight pause + clean up
			cleanUp();
		}	
		
	}
	
	/**
	 * Call this when this needs to go away
	 */
	private void cleanUp(){
		// Flip Attack Frames
		if (CombatSystem.attackingUnit != null && !mComponentMapper.get(CombatSystem.attackingUnit).isAlly) {
			for (TextureRegion animationRegion : aComponentMapper.get(CombatSystem.attackingUnit).currentAnimation.animationFrames.getKeyFrames()) {
				animationRegion.flip(true, false);
			}
		}
		
		if (CombatSystem.defendingUnit != null && !mComponentMapper.get(CombatSystem.defendingUnit).isAlly) {
			for (TextureRegion animationRegion : aComponentMapper.get(CombatSystem.defendingUnit).currentAnimation.animationFrames.getKeyFrames()) {
				animationRegion.flip(true, false);
			}
		}
		
		// Timer Cleanup
		healthTimerAtk = 0;
		healthTimerDef = 0;
		
		// Stop Drawing
		sComponentMapper.get(defendingNonMovingEntity).isEnabled = false;
		
		// System Cleanup
		isProcessing = false;
		combatAnimationsAreComplete = true;
		combatSystemState = CombatSystemState.Attacking;
	}
	
	/**
	 * Create placeholder entities
	 */
	public void createInitialEntity() {
		// Static Image for Defending Unit
		defendingNonMovingEntity = new Entity();
		StaticImageComponent defStaticImageComponent = new StaticImageComponent();
		defStaticImageComponent.isEnabled = false;
		PositionComponent defPositionComponent = new PositionComponent();
		ZOrderComponent defzOrderComponent = new ZOrderComponent(ZOrder.COMBAT_ACTORS);
		
		defendingNonMovingEntity.add(defStaticImageComponent);
		defendingNonMovingEntity.add(defPositionComponent);
		defendingNonMovingEntity.add(defzOrderComponent);
		getEngine().addEntity(defendingNonMovingEntity);
		
		// Background
		backgroundImageEntity = new Entity();
		StaticImageComponent staticImageComponent = new StaticImageComponent();
		staticImageComponent.isEnabled = false;
		PositionComponent positionComponent = new PositionComponent();
		ZOrderComponent zOrderComponent = new ZOrderComponent(ZOrder.UI_TOP_LAYER);
		
		backgroundImageEntity.add(staticImageComponent);
		backgroundImageEntity.add(positionComponent);
		backgroundImageEntity.add(zOrderComponent);
		getEngine().addEntity(backgroundImageEntity);
	}
}
