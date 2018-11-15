package com.jb.fe.map;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.jb.fe.components.PositionComponent;

public class MapCell {
	
	// Cell Size
	public static final int CELL_SIZE = 16;
	
	// Coordinates
	public PositionComponent position;
	
	// Square Colors
	public Entity blueSquare;
	public Entity redSquare;
	
	// Cell Type
	public static enum CellType {
		Grass, Forest, Fortress, Throne, Village, Impassable;
	}
	
	public CellType cellType;
	
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
	
	public MapCell() {
		position = new PositionComponent();
		adjTiles = new Array<MapCell>();
		cellType = CellType.Grass;
		defenceBonus = 0;
		avoidanceBonus = 0;
		movementCost = MovementTileValues.NORMAL;
		isVisited = false;
		occupyingUnit = null;
		parentTile = null;
		distanceFromParent = 0;
		isOccupied = false;
		gCost = 0;
		hCost = 0;
		parentTileAStar = null;
	}
	
	public static class MovementTileValues {
		public static final int NORMAL = 1;
		public static final int FOREST = 2;
		public static final int FORTRESS = 2;
		public static final int IMPASSABLE = 100;
	}
	
	public static class DefenseTileBonus {
		public static final int GRASS_DEF = 0;
		public static final int THRONE_DEF = 3;
		public static final int FOREST_DEF = 1;
		public static final int FORTRESS_DEF = 2;
		public static final int VILLAGE_DEF = 1;
		public static final int IMPASSABLE_DEF = 1;
	}
	
	public static class AvoidanceTileBonus {
		public static final int GRASS_AVD = 0;
		public static final int THRONE_AVD = 20;
		public static final int FOREST_AVD = 20;
		public static final int FORTRESS_AVD = 10;
		public static final int VILLAGE_AVD = 5;
		public static final int IMPASSABLE_AVD = 0;
	}
	
	public int getFCost() {
		return gCost + hCost;
	}
}
