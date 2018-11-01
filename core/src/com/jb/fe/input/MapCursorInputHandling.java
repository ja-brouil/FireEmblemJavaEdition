package com.jb.fe.input;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.jb.fe.components.InputComponent.InputHandling;
import com.jb.fe.components.MapCursorStateComponent;
import com.jb.fe.components.MapCursorStateComponent.MapCursorState;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.map.MapCell;

public class MapCursorInputHandling implements InputHandling, Component {

	// Map Cursor Elements
	MapCursorStateComponent mapCursorStateComponent;
	PositionComponent positionComponent;

	// Key Delay
	private float keyDelayForMovement;
	private float currentTimerForDelay;

	public MapCursorInputHandling(MapCursorStateComponent mapCursorStateComponent,
			PositionComponent positionComponent) {
		this.mapCursorStateComponent = mapCursorStateComponent;
		this.positionComponent = positionComponent;

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

			// A
			if (Gdx.input.isKeyPressed(Keys.Z)) {
				// Do nothing if there is no unit selected
				if (mapCursorStateComponent.unitSelected == null) { return; }
				mapCursorStateComponent.mapCursorState = MapCursorState.UNIT_SELECTED;
			}
		} else if (mapCursorStateComponent.mapCursorState.equals(MapCursorStateComponent.MapCursorState.DISABLED)) {

		} else if (mapCursorStateComponent.mapCursorState
				.equals(MapCursorStateComponent.MapCursorState.SELECT_UNIT_FOR_ACTION)) {

		} else if (mapCursorStateComponent.mapCursorState
				.equals(MapCursorStateComponent.MapCursorState.UNIT_SELECTED)) {
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
			
			// Back
			if (Gdx.input.isKeyPressed(Keys.X)) {
				mapCursorStateComponent.mapCursorState = MapCursorState.MOVEMENT_ONLY;
			}
		}
	}
}
