package com.jb.fe.components;

import com.badlogic.ashley.core.Component;
import com.jb.fe.map.MapCell;

public class PositionComponent implements Component{

	public float x, y;
	
	public PositionComponent(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public PositionComponent() {
		this.x = 0;
		this.y = 0;
	}
	
	@Override
	public String toString() {
		return "Position X:" + x + " Y: " + y;
	}
	
	public String toStringDivided() {
		return "Position X:" + (x / MapCell.CELL_SIZE) + " Y: " + (y / MapCell.CELL_SIZE);
	}
}
