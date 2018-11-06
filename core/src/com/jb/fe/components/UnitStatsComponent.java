package com.jb.fe.components;

import java.util.HashSet;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import com.jb.fe.map.MapCell;

public class UnitStatsComponent implements Component {
	
	// Unit Stats
	
	// Unit Level
	public int Level;
	public int currentExperience;
	public final int experiencedToNextLevel = 100;
	
	// Unit Growth Rate
	
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
	
	// Attack Status
	public Unit_State unit_State;
	
	// Ally
	public boolean isAlly;
	
	// Default Stats
	public UnitStatsComponent() {
		
		// Unit level
		Level = 1;
		currentExperience = 100;
		
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
