package com.jb.fe.components;

import java.util.HashSet;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import com.jb.fe.map.MapCell;

public class UnitStatsComponent implements Component {
	
	// Unit Stats
	
	// Attack Range
	public int attackRange;
	
	// Movement
	public int movementSteps;
	public float animationMovementSpeed;
	public MapCell currentCell;
	public MapCell destinationCell;
	public HashSet<MapCell> allPossibleMoves;
	public Array<MapCell> allOutsideAttackMoves;
	public Queue<MapCell> pathfindingQueue;
	public boolean isMoving;
	
	// Ally
	public boolean isAlly;
	
	// Default Stats
	public UnitStatsComponent() {
		
		// Movement Defaults
		allPossibleMoves = new HashSet<>();
		allOutsideAttackMoves = new Array<>();
		pathfindingQueue = new Queue<MapCell>();
		movementSteps = 4;
		attackRange = 1;
		isAlly = true;
		currentCell = null;
		isMoving = false;
		animationMovementSpeed = 4f;
	}
	
}
