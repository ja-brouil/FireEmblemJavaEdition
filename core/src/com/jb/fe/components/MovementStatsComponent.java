package com.jb.fe.components;

import java.util.HashSet;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import com.jb.fe.map.MapCell;

public class MovementStatsComponent implements Component {
	
	// Movement
	public int movementSteps;
	public float animationMovementSpeed;
	
	// Tile
	public MapCell currentCell;
	public MapCell destinationCell;
	
	// Pathfinding Algorithm ARrays
	public HashSet<MapCell> allPossibleMoves;
	public Array<MapCell> allOutsideAttackMoves;
	public Queue<MapCell> pathfindingQueue;
	
	// Animation system
	public boolean isMoving;
	
	// Attack Status
	public Unit_State unit_State;
	
	// Ally
	public boolean isAlly;
	
	// Default Stats
	public MovementStatsComponent() {
		
		// Movement Defaults
		allPossibleMoves = new HashSet<>();
		allOutsideAttackMoves = new Array<>();
		pathfindingQueue = new Queue<MapCell>();
		movementSteps = 4;
		isAlly = true;
		currentCell = null;
		isMoving = false;
		animationMovementSpeed = 4f;
		
		// Unit State
		unit_State = Unit_State.CAN_DO_BOTH;
	}
	
	public static enum Unit_State {
		CAN_DO_ACTION,
		CAN_ONLY_MOVE,
		CAN_DO_BOTH, 
		DONE
	}	
}
