package com.jb.fe.systems.graphics;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.jb.fe.UI.combatUIScreen.CombatScreenUI;
import com.jb.fe.components.AnimationComponent;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.components.StaticImageComponent;
import com.jb.fe.components.UnitStatsComponent;
import com.jb.fe.components.ZOrderComponent;
import com.jb.fe.systems.camera.CameraSystem;
import com.jb.fe.systems.gamePlay.CombatSystem;
import com.jb.fe.systems.gamePlay.CombatSystemCalculator;

public class CombatAnimationSystem extends EntitySystem {
	
	public static boolean canRetaliate = false;
	public static boolean isProcessing = false;
	public static boolean combatAnimationsAreComplete = false;
	
	public static int damageFromAttacker;
	public static int damageFromDefender;
	
	public float attackingX, attackingY;
	public float defendingX, defendingY;
	
	private enum CombatSystemState {
		Attacking, Defending
	}
	private CombatSystemState combatSystemState;
	
	private ComponentMapper<AnimationComponent> aComponentMapper = ComponentMapper.getFor(AnimationComponent.class);
	private ComponentMapper<PositionComponent> pComponentMapper = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<StaticImageComponent> sComponentMapper = ComponentMapper.getFor(StaticImageComponent.class);
	private ComponentMapper<UnitStatsComponent> uComponentMapper = ComponentMapper.getFor(UnitStatsComponent.class);
	
	private Entity defendingNonMovingEntity;
	
	private float healthTimerAtk;
	private float healthTimerDef;
	
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
		pComponentMapper.get(CombatSystem.attackingUnit).x = CameraSystem.cameraX - CameraSystem.xConstant + 100;
		pComponentMapper.get(CombatSystem.attackingUnit).y = CameraSystem.cameraY - CameraSystem.yConstant + 50;
		
		pComponentMapper.get(CombatSystem.defendingUnit).x = CameraSystem.cameraX - CameraSystem.xConstant + 50;
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
		aComponentMapper.get(CombatSystem.defendingUnit).currentAnimation.isDrawing = false;
		
		// Set First Frame for defending unit
		sComponentMapper.get(defendingNonMovingEntity).staticImage = aComponentMapper.get(CombatSystem.defendingUnit).currentAnimation.animationFrames.getKeyFrames()[0];
		sComponentMapper.get(defendingNonMovingEntity).width = 50;
		sComponentMapper.get(defendingNonMovingEntity).height = 50;
		sComponentMapper.get(defendingNonMovingEntity).isEnabled = true;
		
		pComponentMapper.get(defendingNonMovingEntity).x = CameraSystem.cameraX - CameraSystem.xConstant + 50;
		pComponentMapper.get(defendingNonMovingEntity).y = CameraSystem.cameraY - CameraSystem.yConstant + 50;
		
		// Reset Animation timers
		aComponentMapper.get(CombatSystem.attackingUnit).currentAnimation.animationElapsedTime = 0;
		aComponentMapper.get(CombatSystem.defendingUnit).currentAnimation.animationElapsedTime = 0;
		
	}
	
	@Override
	public void update(float delta) {
		if (!isProcessing) { return; }
		
		// Attacking = seth
		// Defending = bandit
		
		// Process attacking state first
		if (combatSystemState.equals(CombatSystemState.Attacking)) {
			
			AnimationComponent animationComponent = aComponentMapper.get(CombatSystem.attackingUnit);
			
			// Check if attacking animations are done
			if (!animationComponent.currentAnimation.animationFrames.isAnimationFinished(animationComponent.currentAnimation.animationElapsedTime)) {
				return;
			}
			
			// Animations are done so process information after combat
			// There should be a flash/sound play here + Add later
			
			//System.out.println("Sound played!");
			
			// Set HP of the defender
			UnitStatsComponent defUnitStatsComponent = uComponentMapper.get(CombatSystem.defendingUnit);

			// Calculate stat changes
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
			
			// Stop Damage past 0
			if (defUnitStatsComponent.health <= 0) {
				defUnitStatsComponent.health = 0;
				
				// We reached 0, remove the enemy unit	
				// Play Death Sound/ Flash
				System.out.println("Death sound + flash played");
				
				// Flip Units
				for (TextureRegion region : aComponentMapper.get(CombatSystem.defendingUnit).currentAnimation.animationFrames.getKeyFrames()) {
					region.flip(true, false);
				}
				sComponentMapper.get(defendingNonMovingEntity).staticImage.flip(true, false);
				
				// Remove Unit
				getEngine().getSystem(CombatSystem.class).removeEntity(defUnitStatsComponent);
				CombatSystem.defendingUnit = null;
				
				// No retaliation here so we are done
				isProcessing = false;
				
				// Remove the initial entity
				sComponentMapper.get(defendingNonMovingEntity).isEnabled = false;
				CombatAnimationSystem.combatAnimationsAreComplete = true;
				
				return;
			}
			
			// Stop Healing past max Health
			if (defUnitStatsComponent.health > defUnitStatsComponent.maxHealth) {
				defUnitStatsComponent.health = defUnitStatsComponent.maxHealth;
				// No retaliation on healing so we are done
				// TO DO
			}
			
			// Set Retalition
			combatSystemState = CombatSystemState.Defending;
			
			// Graphics
			sComponentMapper.get(defendingNonMovingEntity).isEnabled = false;
			aComponentMapper.get(CombatSystem.defendingUnit).currentAnimation.isDrawing = true;
			for (TextureRegion region : aComponentMapper.get(CombatSystem.defendingUnit).currentAnimation.animationFrames.getKeyFrames()) {
				region.flip(true, false);
			}
			
			
		} else {
			// Defending Unit Attacks back
			AnimationComponent animationComponent = aComponentMapper.get(CombatSystem.defendingUnit);
			
			// Check if attacking animations are done
			if (!animationComponent.currentAnimation.animationFrames.isAnimationFinished(animationComponent.currentAnimation.animationElapsedTime)) {
				return;
			}
			
			// Animations are done so process information after combat
			// There should be a flash/sound play here + Add later
			
			//System.out.println("Sound played!");
			
			// Set HP of the defender
			UnitStatsComponent atkUnitStatsComponent = uComponentMapper.get(CombatSystem.attackingUnit);

			// Calculate stat changes
			if (atkUnitStatsComponent.health != CombatSystemCalculator.currentDefendingHealth - CombatSystemCalculator.AttackingDamage 
					&& atkUnitStatsComponent.health > 0 && atkUnitStatsComponent.health <= atkUnitStatsComponent.maxHealth) {
				healthTimerDef += delta;
				
				if (healthTimerDef >= 0.15f) {
					// Damage
					if (CombatSystemCalculator.AttackingDamage >= 0) {
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
			
			// Stop Damage past 0
			if (atkUnitStatsComponent.health <= 0) {
				atkUnitStatsComponent.health = 0;
				
				// We reached 0, remove the enemy unit	
				// Play Death Sound/ Flash
				System.out.println("Death sound + flash played");
				
				// Remove Unit
				getEngine().getSystem(CombatSystem.class).removeEntity(atkUnitStatsComponent);
				CombatSystem.defendingUnit = null;
				
				// Remove the initial entity
				sComponentMapper.get(defendingNonMovingEntity).isEnabled = false;
			}
			
			// Stop Healing past max Health
			if (atkUnitStatsComponent.health > atkUnitStatsComponent.maxHealth) {
				atkUnitStatsComponent.health = atkUnitStatsComponent.maxHealth;
				// No retaliation on healing so we are done
				// TO DO
			}
			
			// Clean up
			combatSystemState = CombatSystemState.Attacking;
			
			// We are done with animations
			CombatAnimationSystem.combatAnimationsAreComplete = true;
			isProcessing = false;
		}
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
