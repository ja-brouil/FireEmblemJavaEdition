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

		// Select unit to attack/heal
		SELECT_UNIT_FOR_ACTION,

		// Off
		DISABLED
	}

	public MapCursorState mapCursorState;

	public MapCursorStateComponent() {
		mapCursorState = MapCursorState.MOVEMENT_ONLY;
	}
}