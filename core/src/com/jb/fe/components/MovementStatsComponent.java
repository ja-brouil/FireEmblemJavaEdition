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
	public MapCell previousCell;
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
	
	// Movement Penalties
	public int defaultPenalty;
	public int mountainPenalty;
	public int hillPenalty;
	public int forestPenalty;
	public int fortressPenalty;
	public int buildingPenalty;
	public int riverPenalty;
	public int seaPenalty;
	
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
		animationMovementSpeed = 8f;
		
		// Penalty Defaults
		defaultPenalty = 0;
		mountainPenalty = 0;
		hillPenalty = 0;
		forestPenalty = 0;
		fortressPenalty = 0;
		buildingPenalty = 0;
		riverPenalty = 0;
		seaPenalty = 0;
		
		// Unit State
		unit_State = Unit_State.CAN_DO_BOTH;
	}
	
	public MovementStatsComponent(int movementSteps, boolean isAlly) {
		// Movement Defaults
		allPossibleMoves = new HashSet<>();
		allOutsideAttackMoves = new Array<>();
		pathfindingQueue = new Queue<MapCell>();
		this.movementSteps = movementSteps;
		this.isAlly = isAlly;
		currentCell = null;
		isMoving = false;
		animationMovementSpeed = 8f;
		
		// Penalty Defaults
		defaultPenalty = 0;
		mountainPenalty = 0;
		hillPenalty = 0;
		forestPenalty = 0;
		fortressPenalty = 0;
		buildingPenalty = 0;
		riverPenalty = 0;
		seaPenalty = 0;
		
		// Unit State
		unit_State = Unit_State.CAN_DO_BOTH;
	}
	
	public static enum Unit_State {
		CAN_DO_ACTION,
		CAN_ONLY_MOVE,
		CAN_DO_BOTH, 
		DONE
	}
	
	public void setMovementPenalties(int defaultPenalty, int mountainPenalty, int hillPenalty, int forestPenalty, int fortressPenalty, int buildingPenalty, int riverPenalty, int seaPenalty) {
		this.defaultPenalty = defaultPenalty;
		this.mountainPenalty = mountainPenalty;
		this.hillPenalty = hillPenalty;
		this.forestPenalty = forestPenalty;
		this.fortressPenalty = forestPenalty;
		this.buildingPenalty = buildingPenalty;
		this.riverPenalty = riverPenalty;
		this.seaPenalty = seaPenalty;
	}
}
