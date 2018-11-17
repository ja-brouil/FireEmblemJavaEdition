package com.jb.fe.UI;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.jb.fe.components.InputComponent.InputHandling;
import com.jb.fe.components.MapCursorStateComponent;
import com.jb.fe.components.MapCursorStateComponent.MapCursorState;
import com.jb.fe.components.MovementStatsComponent;
import com.jb.fe.components.MovementStatsComponent.Unit_State;
import com.jb.fe.components.NameComponent;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.components.SoundComponent;
import com.jb.fe.map.MapCell;
import com.jb.fe.systems.audio.SoundSystem;

/**
 * Controls Map Cursor | Fix Mouse clicks later
 * @author james
 *
 */

public class MapCursorInputHandling implements InputHandling{

	// Map Cursor Elements
	private MapCursorStateComponent mapCursorStateComponent;
	private PositionComponent positionComponent;
	private ComponentMapper<MovementStatsComponent> uComponentMapper = ComponentMapper.getFor(MovementStatsComponent.class);

	// Key Delay
	private float keyDelayForMovement;
	private float currentTimerForDelay;

	// Sound
	private SoundSystem soundSystem;

	// MapCursor
	private Entity mapCursor;

	public MapCursorInputHandling(MapCursorStateComponent mapCursorStateComponent, PositionComponent positionComponent,
			SoundSystem soundSystem, Entity mapCursor, OrthographicCamera camera) {
		this.mapCursorStateComponent = mapCursorStateComponent;
		this.positionComponent = positionComponent;
		this.soundSystem = soundSystem;
		this.mapCursor = mapCursor;

		keyDelayForMovement = 0.08f;
	}


	@Override
	public void handleInput() {
		// Debug + Global stuff
		if (Gdx.input.isKeyJustPressed(Keys.B)) {
			System.out.println("------------------------------------");
			System.out.println("Cursor Info: " + "\n"
					+ "X: " + positionComponent.x + "\n"
					+ "Y: " + positionComponent.y + "\n"
					+ "X Coordinate: " + positionComponent.x / MapCell.CELL_SIZE + "\n"
					+ "Y Coordinate: " + positionComponent.y / MapCell.CELL_SIZE);
			if (mapCursorStateComponent.unitSelected == null) {
				System.out.println("Occupying Unit: null");
			} else {
				System.out.println("Occupying Unit: " + mapCursorStateComponent.unitSelected.getComponent(NameComponent.class).name);
				System.out.println("Occupying Unit Status: " + mapCursorStateComponent.unitSelected.getComponent(MovementStatsComponent.class).toString());
			}
			System.out.println("------------------------------------");
		}
		
		
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
			if (Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.DOWN)
					|| Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.RIGHT)) {
				soundSystem.playSound(mapCursor.getComponent(SoundComponent.class).allSoundObjects.get("Movement"));
			}

			// A
			if (Gdx.input.isKeyJustPressed(Keys.Z)) {
				// Do nothing if there is no unit selected --> Switch to open menu later
				if (mapCursorStateComponent.unitSelected == null) {
					return;
				}

				// If Unit cannot do anything anymore, do nothing
				if (uComponentMapper.get(mapCursorStateComponent.unitSelected).unit_State.equals(Unit_State.DONE)) {
					return;
				}

				mapCursorStateComponent.mapCursorState = MapCursorState.UNIT_SELECTED;
				soundSystem.playSound(mapCursor.getComponent(SoundComponent.class).allSoundObjects.get("Select Unit"));
				currentTimerForDelay = 0;
			}
		} else if (mapCursorStateComponent.mapCursorState
				.equals(MapCursorStateComponent.MapCursorState.WAITING_FOR_VALID_MOVE)) {
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
			if (Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.DOWN)
					|| Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.RIGHT)) {
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
