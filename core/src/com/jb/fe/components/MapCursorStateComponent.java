package com.jb.fe.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

public class MapCursorStateComponent implements Component {
	
	// Data for the MapCursor
	public Entity unitSelected;

	// Set the map cursor to this
	public static enum MapCursorState {
		// Movement only
		MOVEMENT_ONLY,

		// Unit Selected
		UNIT_SELECTED,
		
		// Waiting for valid move
		WAITING_FOR_VALID_MOVE,
		
		// Check for Valid move
		VALID_MOVE_CHECK,

		// Select unit to attack/heal
		SELECT_UNIT_FOR_ACTION,

		// Off
		DISABLED
	}

	public MapCursorState mapCursorState;
	
	// MapCursor Quadrant
	public static enum MAP_CURSOR_QUADRANT {
		TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT;
	}
	
	public MAP_CURSOR_QUADRANT mapCursorQuandrant = MAP_CURSOR_QUADRANT.TOP_LEFT;

	public MapCursorStateComponent() {
		mapCursorState = MapCursorState.MOVEMENT_ONLY;
	}
}