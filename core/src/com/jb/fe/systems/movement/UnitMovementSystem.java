package com.jb.fe.systems.movement;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.jb.fe.UI.mapcursor.MapCursor;
import com.jb.fe.components.AnimationComponent;
import com.jb.fe.components.Artifical_IntelligenceComponent;
import com.jb.fe.components.MovementStatsComponent;
import com.jb.fe.components.MovementStatsComponent.Unit_State;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.components.SoundComponent;
import com.jb.fe.components.StaticImageComponent;
import com.jb.fe.map.MapCell;
import com.jb.fe.systems.SystemPriorityList;
import com.jb.fe.systems.audio.SoundSystem;
import com.jb.fe.systems.camera.CameraSystem;
import com.jb.fe.systems.gamePlay.AISystem;
import com.jb.fe.systems.inputAndUI.UserInterfaceManager;

/**
 * Controls the unit movements
 * 
 * @author james
 */
public class UnitMovementSystem extends EntitySystem {

	// Entity
	private static Entity unit = null;

	// Mappers
	private ComponentMapper<AnimationComponent> aComponentMapper = ComponentMapper.getFor(AnimationComponent.class);
	private ComponentMapper<PositionComponent> pComponentMapper = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<MovementStatsComponent> sComponentMapper = ComponentMapper
			.getFor(MovementStatsComponent.class);
	private ComponentMapper<SoundComponent> soundComponentMapper = ComponentMapper.getFor(SoundComponent.class);
	private ComponentMapper<Artifical_IntelligenceComponent> aiComponentMapper = ComponentMapper
			.getFor(Artifical_IntelligenceComponent.class);

	// System for Unit Update
	private UnitMapCellUpdater unitMapCellUpdater;
	private UserInterfaceManager userInterfaceManager;
	private AISystem aiSystem;
	private CameraSystem cameraSystem;
	private boolean cameraReset;

	// Sound
	private SoundSystem soundSystem;

	public UnitMovementSystem() {
		priority = SystemPriorityList.MovementUpdate;
		cameraReset = false;
	}

	@Override
	public void update(float delta) {
		if (unit == null) {
			return;
		}

		MovementStatsComponent unitStatsComponent = sComponentMapper.get(unit);
		MapCursor mapCursor = (MapCursor) userInterfaceManager.allUserInterfaceStates.get("MapCursor");
		
		// Reset Camera if it's off screen
		if (!cameraReset) {
			cameraSystem.cameraMovementReset(unit,mapCursor.getMapCursorEntity());
			cameraReset = true;
			return;
		}
		
		// Destination Cell and Starting Cell
		MapCell startingCell = unitStatsComponent.currentCell;
		MapCell nextCell = unitStatsComponent.pathfindingQueue.first();
		
		// Camera Movement
		float x = 0;
		float y = 0;
		
		if (unitStatsComponent.pathfindingQueue.first().position.x % 16 != 8) {
			x = 8;
		}
		
		if (unitStatsComponent.pathfindingQueue.first().position.y % 16 != 0) {
			y = 8;
		}
		
		// Components
		AnimationComponent animationComponent = aComponentMapper.get(unit);
		PositionComponent unitPositionComponent = pComponentMapper.get(unit);

		// Process Unit Movement
		if (startingCell.position.x - nextCell.position.x > 0
				&& Math.abs(startingCell.position.y - nextCell.position.y) >= 0f) {
			// Left
			animationComponent.currentAnimation = animationComponent.allAnimationObjects.get("Left");
			
		} else if (startingCell.position.x - nextCell.position.x < 0
				&& Math.abs(startingCell.position.y - nextCell.position.y) >= 0f) {
			// Right
			animationComponent.currentAnimation = animationComponent.allAnimationObjects.get("Right");
		} else if (startingCell.position.y - nextCell.position.y > 0
				&& Math.abs(startingCell.position.x - nextCell.position.x) >= 0f) {
			// Down
			animationComponent.currentAnimation = animationComponent.allAnimationObjects.get("Down");
		} else if (startingCell.position.y - nextCell.position.y < 0
				&& Math.abs(startingCell.position.x - nextCell.position.x) >= 0f) {
			// Up
			animationComponent.currentAnimation = animationComponent.allAnimationObjects.get("Up");
		}

		// Move Unit || Smooth out over time
		if (Math.abs(unitPositionComponent.x - nextCell.position.x) >= 1.75f
				|| Math.abs(unitPositionComponent.y - nextCell.position.y) >= 1.75f) {
			unitPositionComponent.x += (nextCell.position.x - startingCell.position.x)
					* unitStatsComponent.animationMovementSpeed * Gdx.graphics.getDeltaTime();
			unitPositionComponent.y += (nextCell.position.y - startingCell.position.y)
					* unitStatsComponent.animationMovementSpeed * Gdx.graphics.getDeltaTime();
			// Move Camera with unit
			cameraSystem.followUnitCamera(unit, x ,y);
			
		} else {
			// Finalize movement to prevent rounding errors
			unitPositionComponent.x = nextCell.position.x;
			unitPositionComponent.y = nextCell.position.y;
			unitStatsComponent.currentCell = nextCell;
			unitStatsComponent.pathfindingQueue.removeFirst();
			cameraSystem.followUnitCamera(unit, x, y);
		}

		// Prevent weird crashes when FPS drops or game is paused
		if (Math.abs(unitPositionComponent.x - nextCell.position.x) >= 17f
				|| Math.abs(unitPositionComponent.y - nextCell.position.y) >= 17f) {
			unitPositionComponent.x = nextCell.position.x;
			unitPositionComponent.y = nextCell.position.y;
			unitStatsComponent.currentCell = nextCell;
			unitStatsComponent.pathfindingQueue.removeFirst();
			cameraSystem.followUnitCamera(unit, x , y);
		}

		// Play Sound
		soundSystem.playSound(soundComponentMapper.get(unit).allSoundObjects.get("Movement"));

		// Reset Movement back to the cursor if the queue is empty || this must be
		// modified for AI movement as well
		if (unitStatsComponent.pathfindingQueue.size == 0) {
			unitStatsComponent.isMoving = false;

			// Ally Unit Reset | Enemy Unit Reset
			if (unitStatsComponent.isAlly) {

				// Set Unit to Done status -> This needs to be changed to check later if you did
				// an action first.
				unitStatsComponent.unit_State = Unit_State.CAN_DO_ACTION;

			} else {
				Artifical_IntelligenceComponent artifical_IntelligenceComponent = aiComponentMapper.get(unit);
				// Process Combat in AI System
				aiSystem.processCombat();
				artifical_IntelligenceComponent.isProcessing = false;

				aComponentMapper.get(unit).currentAnimation = aComponentMapper.get(unit).allAnimationObjects
						.get("Idle");
			}

			// Turn off Map Cells
			for (MapCell mapCell : unitStatsComponent.allPossibleMoves) {
				mapCell.blueSquare.getComponent(StaticImageComponent.class).isEnabled = false;
			}

			for (MapCell mapCell : unitStatsComponent.allOutsideAttackMoves) {
				mapCell.redSquare.getComponent(StaticImageComponent.class).isEnabled = false;
			}

			// Update all units position
			unitMapCellUpdater.updateCellInfo();
			
			// Set UI to Action Menu
			if (unitStatsComponent.isAlly) {
				userInterfaceManager.setStates(userInterfaceManager.allUserInterfaceStates.get("MovementSelection"), userInterfaceManager.allUserInterfaceStates.get("ActionMenu"));
				userInterfaceManager.pauseUI();
			}
			
			// Move Map Cursor
			pComponentMapper.get(mapCursor.getMapCursorEntity()).x = unitPositionComponent.x;
			pComponentMapper.get(mapCursor.getMapCursorEntity()).y = unitPositionComponent.y;
			
			// Reset camera
			cameraReset = false;
			
			// Remove unit
			UnitMovementSystem.unit = null;
		}
	}

	public void startSystem() {
		unitMapCellUpdater = getEngine().getSystem(UnitMapCellUpdater.class);
		soundSystem = getEngine().getSystem(SoundSystem.class);
		userInterfaceManager = getEngine().getSystem(UserInterfaceManager.class);
		aiSystem = getEngine().getSystem(AISystem.class);
		cameraSystem = getEngine().getSystem(CameraSystem.class);
	}

	public static void setEntity(Entity unit) {
		UnitMovementSystem.unit = unit;
	}
}
