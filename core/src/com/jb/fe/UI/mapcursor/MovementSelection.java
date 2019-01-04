package com.jb.fe.UI.mapcursor;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.jb.fe.UI.UserInterfaceState;
import com.jb.fe.UI.soundTemp.UISounds;
import com.jb.fe.components.AnimationComponent;
import com.jb.fe.components.MovementStatsComponent;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.components.SoundComponent;
import com.jb.fe.level.Level;
import com.jb.fe.map.MapCell;
import com.jb.fe.systems.audio.SoundSystem;
import com.jb.fe.systems.inputAndUI.UserInterfaceManager;
import com.jb.fe.systems.movement.MovementUtilityCalculator;
import com.jb.fe.systems.movement.UnitMapCellUpdater;
import com.jb.fe.systems.movement.UnitMovementSystem;

public class MovementSelection extends UserInterfaceState{

	private float currentTimerForDelay;
	private float keyDelayForMovement;
	
	private Entity mapCursor;
	private Level level;
	
	private UnitMapCellUpdater unitMapCellUpdater;
	private MovementUtilityCalculator movementUtilityCalculator;
	
	public MovementSelection(AssetManager assetManager, SoundSystem soundSystem,
			UserInterfaceManager userInterfaceManager, Entity mapCursor, Level level, UnitMapCellUpdater unitMapCellUpdater, MovementUtilityCalculator movementUtilityCalculator) {
		super(assetManager, soundSystem, userInterfaceManager);
		this.mapCursor = mapCursor;
		this.level = level;
		this.unitMapCellUpdater = unitMapCellUpdater;
		this.movementUtilityCalculator = movementUtilityCalculator;
		
		keyDelayForMovement = 0.08f;
	}

	@Override
	public void startState() {
		animationComponentMapper.get(mapCursor).currentAnimation.isDrawing = true;
		staticImageComponentMapper.get(mapCursor).isEnabled = false;
		
		// Set Animation of the unit selected
		if (mStatComponentMapper.get(UserInterfaceManager.unitSelected).isAlly) {
			animationComponentMapper.get(UserInterfaceManager.unitSelected).currentAnimation = animationComponentMapper.get(UserInterfaceManager.unitSelected).allAnimationObjects.get("Selected");
		}
		
		// Calculate Movement
		unitMapCellUpdater.updateCellInfo();
		movementUtilityCalculator.calculateAllPossibleMoves(UserInterfaceManager.unitSelected);
	}

	@Override
	public void resetState() {
		animationComponentMapper.get(mapCursor).currentAnimation.isDrawing = false;
		staticImageComponentMapper.get(mapCursor).isEnabled = false;
		movementUtilityCalculator.resetMovementAlgorithms();
	}

	@Override
	public void nextState() {
		// Check if unit selected is not an ally
		if (!mStatComponentMapper.get(UserInterfaceManager.unitSelected).isAlly) {
			userInterfaceManager.setStates(this, userInterfaceManager.allUserInterfaceStates.get("MapCursor"));
			soundSystem.playSound(mapCursor.getComponent(SoundComponent.class).allSoundObjects.get("Invalid"));
			currentTimerForDelay = 0;
			return;
		}
		
		// Proceed if unit selected is an ally
		MapCell cursorMapCell = movementUtilityCalculator.getMapCell(mapCursor);
		if (cursorMapCell.isOccupied && !cursorMapCell.occupyingUnit.equals(UserInterfaceManager.unitSelected)) {
			soundSystem.playSound(UISounds.invalid);
			return;
		}
		
		// Check against hashset
		if (movementUtilityCalculator.getAllPossibleMoves().contains(cursorMapCell)) {
			MovementStatsComponent movementStatsComponent = mStatComponentMapper.get(UserInterfaceManager.unitSelected);
			movementStatsComponent.isMoving = true;
			movementStatsComponent.previousCell = movementStatsComponent.currentCell;
			movementUtilityCalculator.createPathFindingQueue(cursorMapCell, UserInterfaceManager.unitSelected);
			
			// Start movement
			UnitMovementSystem.setEntity(UserInterfaceManager.unitSelected);
			userInterfaceManager.pauseUI();
			
			// Accept Sound
			soundSystem.playSound(UISounds.accept);
			
			// Swap State in the manager here
			
		} else {
			soundSystem.playSound(UISounds.invalid);
		}
	}

	@Override
	public void handleInput(float delta) {
		currentTimerForDelay += Gdx.graphics.getDeltaTime();
		if (currentTimerForDelay <= keyDelayForMovement) {
			return;
		}
		
		PositionComponent positionComponent = pComponentMapper.get(mapCursor);

		// Up
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			positionComponent.y += MapCell.CELL_SIZE;
			currentTimerForDelay = 0;
		}

		// Down
		if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			positionComponent.y -= MapCell.CELL_SIZE;
			currentTimerForDelay = 0;
		}

		// Left
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			positionComponent.x -= MapCell.CELL_SIZE;
			currentTimerForDelay = 0;
		}

		// Right
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			positionComponent.x += MapCell.CELL_SIZE;
			currentTimerForDelay = 0;
		}

		// Sound Played only once
		if ((Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.DOWN)
				|| Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.RIGHT)) && preventOutOfBounds()) {
			soundSystem.playSound(mapCursor.getComponent(SoundComponent.class).allSoundObjects.get("Movement"));
			preventOutOfBounds();
			return;
		}

		// Back
		if (Gdx.input.isKeyJustPressed(Keys.X)) {
			userInterfaceManager.setStates(this, userInterfaceManager.allUserInterfaceStates.get("MapCursor"));
			soundSystem.playSound(mapCursor.getComponent(SoundComponent.class).allSoundObjects.get("Back"));
			currentTimerForDelay = 0;
		}

		// Check Valid Moves
		if (Gdx.input.isKeyJustPressed(Keys.Z)) {
			currentTimerForDelay = 0;
			nextState();
		}
	}

	@Override
	public void update(float delta) {
		
	}
	
	private boolean preventOutOfBounds() {
		PositionComponent positionComponent = pComponentMapper.get(mapCursor);
		AnimationComponent animationComponent = animationComponentMapper.get(mapCursor);
		
		if (positionComponent.x < 0) {
			positionComponent.x = 0;
			return false;
		}
		
		if (positionComponent.x > level.mapWidthLimit - (animationComponent.currentAnimation.width * 2)) {
			positionComponent.x = level.mapWidthLimit - (animationComponent.currentAnimation.width * 2);
			return false;
		}
		
		if (positionComponent.y < 0) {
			positionComponent.y = 0;
			return false;
		}
		
		if (positionComponent.y > level.mapHeightLimit - animationComponent.currentAnimation.height) {
			positionComponent.y = level.mapHeightLimit - animationComponent.currentAnimation.height;
			return false;
		}
		
		return true;
	}
}
