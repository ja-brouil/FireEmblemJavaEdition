package com.jb.fe.input;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.jb.fe.components.InputComponent.InputHandling;
import com.jb.fe.components.MapCursorStateComponent;
import com.jb.fe.components.MapCursorStateComponent.MapCursorState;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.components.SoundComponent;
import com.jb.fe.map.MapCell;
import com.jb.fe.systems.audio.SoundSystem;

public class MapCursorInputHandling implements InputHandling, Component {

	// Map Cursor Elements
	MapCursorStateComponent mapCursorStateComponent;
	PositionComponent positionComponent;

	// Key Delay
	private float keyDelayForMovement;
	private float currentTimerForDelay;
	
	// Sound
	private SoundSystem soundSystem;
	
	// MapCursor
	private Entity mapCursor;

	public MapCursorInputHandling(MapCursorStateComponent mapCursorStateComponent,
			PositionComponent positionComponent, SoundSystem soundSystem, Entity mapCursor) {
		this.mapCursorStateComponent = mapCursorStateComponent;
		this.positionComponent = positionComponent;
		this.soundSystem = soundSystem;
		this.mapCursor = mapCursor;

		keyDelayForMovement = 0.08f;
	}

	@Override
	public void handleInput() {
		// Movement only
		if (mapCursorStateComponent.mapCursorState.equals(MapCursorStateComponent.MapCursorState.MOVEMENT_ONLY)) {

			// Timer Delay
			currentTimerForDelay += Gdx.graphics.getDeltaTime();
			if (currentTimerForDelay <= keyDelayForMovement) {
				return;
			}

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
			if (Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.DOWN) || Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.RIGHT)) {
				soundSystem.playSound(mapCursor.getComponent(SoundComponent.class).allSoundObjects.get("Movement"));
			}

			// A
			if (Gdx.input.isKeyJustPressed(Keys.Z)) {
				// Do nothing if there is no unit selected --> Switch to open menu later
				if (mapCursorStateComponent.unitSelected == null) { return; }
				mapCursorStateComponent.mapCursorState = MapCursorState.UNIT_SELECTED;
				soundSystem.playSound(mapCursor.getComponent(SoundComponent.class).allSoundObjects.get("Accept"));
				currentTimerForDelay = 0;
			}
		} else if (mapCursorStateComponent.mapCursorState.equals(MapCursorStateComponent.MapCursorState.WAITING_FOR_VALID_MOVE)) {
			currentTimerForDelay += Gdx.graphics.getDeltaTime();
			if (currentTimerForDelay <= keyDelayForMovement) {
				return;
			}

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
			if (Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.DOWN) || Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.RIGHT)) {
				soundSystem.playSound(mapCursor.getComponent(SoundComponent.class).allSoundObjects.get("Movement"));
			}
			
			// Back
			if (Gdx.input.isKeyJustPressed(Keys.X)) {
				mapCursorStateComponent.mapCursorState = MapCursorState.MOVEMENT_ONLY;
				soundSystem.playSound(mapCursor.getComponent(SoundComponent.class).allSoundObjects.get("Back"));
				currentTimerForDelay = 0;
			}
			
			// Move Unit
			if (Gdx.input.isKeyJustPressed(Keys.Z)) {
				mapCursorStateComponent.mapCursorState = MapCursorState.VALID_MOVE_CHECK;
				currentTimerForDelay = 0;
			}
		}
	}
}
