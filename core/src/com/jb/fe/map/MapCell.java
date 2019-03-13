package com.jb.fe.map;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.jb.fe.components.PositionComponent;

public class MapCell {
	
	// Cell Size
	public static final int CELL_SIZE = 16;
	
	// Tile Name
	public String tileName;
	
	// Coordinates
	public PositionComponent position;
	
	// Square Colors
	public Entity blueSquare;
	public Entity redSquare;
	
	// Game Play
	public float defenceBonus;
	public float avoidanceBonus;
	public int movementCost;
	
	// Unit On Cell if there is
	public Entity occupyingUnit;
	
	// Algorithm Search variables
	public boolean isVisited;
	public MapCell parentTile;
	public Array<MapCell> adjTiles;
	public int distanceFromParent;
	public boolean isOccupied;
	
	// gCost = distance to starting node
	// hCost = distance to end node
	// fCost = gCost + hCost
	public int gCost;
	public int hCost;
	public MapCell parentTileAStar;
	
	// Background for battle scene
	public String backgroundFilePathString;
	
	public MapCell() {
		position = new PositionComponent();
		adjTiles = new Array<MapCell>();
		defenceBonus = 0;
		avoidanceBonus = 0;
		movementCost = 1;
		isVisited = false;
		occupyingUnit = null;
		parentTile = null;
		distanceFromParent = 0;
		isOccupied = false;
		gCost = 0;
		hCost = 0;
		parentTileAStar = null;
		tileName = "PlaceHolder";
	}
	
	public int getFCost() {
		return gCost + hCost;
	}
}
