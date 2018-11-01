package com.jb.fe.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Array;
import com.jb.fe.map.MapCell;

public class SelectorMapCellComponent implements Component{
	
	public Array<MapCell> blueMapCells;
	public Array<MapCell> redMapCells;
	
	public SelectorMapCellComponent() {
		blueMapCells = new Array<MapCell>();
		redMapCells = new Array<MapCell>();
	}
}
