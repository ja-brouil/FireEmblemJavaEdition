package com.jb.fe.systems.input;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.jb.fe.components.AnimationComponent;
import com.jb.fe.components.MapCursorStateComponent;
import com.jb.fe.components.MapCursorStateComponent.MapCursorState;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.components.UnitStatsComponent;
import com.jb.fe.level.Level;
import com.jb.fe.map.MapCell;
import com.jb.fe.systems.SystemPriorityDictionnary;
import com.jb.fe.systems.movement.MovementUtilityCalculator;
import com.jb.fe.systems.movement.UnitMapCellUpdater;

public class MapCursorInfoUpdateSystem extends EntitySystem{

	// Entites Required
	private Entity mapCursor;
	
	// Game Unit Array
	private ImmutableArray<Entity> allGameUnits;
	
	// Retrievers
	private ComponentMapper<AnimationComponent> animationComponentMapper = ComponentMapper.getFor(AnimationComponent.class);
	private ComponentMapper<PositionComponent> positionComponentMapper = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<MapCursorStateComponent> mapCursorComponentMapper = ComponentMapper.getFor(MapCursorStateComponent.class);
	
	// Square Selector Calulator
	private MovementUtilityCalculator movementUtilityCalculator;
	
	// Calculators
	private UnitMapCellUpdater unitMapCellUpdater;
	
	public MapCursorInfoUpdateSystem() {
		priority = SystemPriorityDictionnary.MapCursorInfoUpdate;
	}
	
	
	@Override
	public void addedToEngine(Engine engine) {
		engine.addEntityListener(Family.all(AnimationComponent.class).get(), new EntityListener() {
			
			@Override
			public void entityRemoved(Entity entity) {
				allGameUnits = engine.getEntitiesFor(Family.all(AnimationComponent.class, UnitStatsComponent.class).get());
			}
			
			@Override
			public void entityAdded(Entity entity) {
				allGameUnits = engine.getEntitiesFor(Family.all(AnimationComponent.class, UnitStatsComponent.class).get());
			}
		});
	}
	
	@Override
	public void update(float delta) {
		// Set Units to Selected
		PositionComponent  mapCursorPositionComponent = positionComponentMapper.get(mapCursor);
		MapCursorStateComponent mapCursorStateComponent = mapCursorComponentMapper.get(mapCursor);

		
		// Movement
		if (mapCursorStateComponent.mapCursorState.equals(MapCursorState.MOVEMENT_ONLY)) {
			
			// Deselect Unit
			movementUtilityCalculator.resetMovementAlgorithms();
			mapCursorStateComponent.unitSelected = null;
			
			for (Entity unit : allGameUnits) {
				PositionComponent unitPositionComponent = positionComponentMapper.get(unit);
				AnimationComponent unitAnimation = animationComponentMapper.get(unit);
				// Reset
				unitAnimation.currentAnimation = unitAnimation.allAnimationObjects.get("Idle");
				
				if (mapCursorPositionComponent.x == unitPositionComponent.x && mapCursorPositionComponent.y == unitPositionComponent.y) {
					unitAnimation.currentAnimation = unitAnimation.allAnimationObjects.get("Hovering");
					mapCursorStateComponent.unitSelected = unit;
				}
			}
		} 
		
		// Unit Selection
		else if (mapCursorStateComponent.mapCursorState.equals(MapCursorState.UNIT_SELECTED)) {
			// Set Animation
			AnimationComponent unitAnimation = animationComponentMapper.get(mapCursorStateComponent.unitSelected);
			unitAnimation.currentAnimation = unitAnimation.allAnimationObjects.get("Selected");
			
			// Calculate Movement
			unitMapCellUpdater.updateCellInfo();
			movementUtilityCalculator.calculateAllPossibleMoves(mapCursorStateComponent.unitSelected);
			
			// Set Cursor to Allow movement
			mapCursorStateComponent.mapCursorState = MapCursorState.WAITING_FOR_VALID_MOVE;
		}
		
		// Check Valid Move
		else if (mapCursorStateComponent.mapCursorState.equals(MapCursorState.VALID_MOVE_CHECK)) {
			// Is the Move Valid?
			MapCell cursorMapCell = movementUtilityCalculator.getMapCell(mapCursor);
			if (cursorMapCell.isOccupied) {
				// reject sound
				mapCursorStateComponent.mapCursorState = MapCursorState.WAITING_FOR_VALID_MOVE;
				return;
			}
			
			// Is Move in the hashset
			if (movementUtilityCalculator.allPossibleMoves.contains(cursorMapCell)) {
				// Start unit movement system
				mapCursorStateComponent.unitSelected.getComponent(UnitStatsComponent.class).isMoving = true;
				movementUtilityCalculator.createPathFindingQueue(cursorMapCell, mapCursorStateComponent.unitSelected);
				mapCursorStateComponent.mapCursorState = MapCursorState.DISABLED;
				
				// Stop Drawing Map Cursor
				AnimationComponent mapCursorAnimationComponent = animationComponentMapper.get(mapCursor);
				mapCursorAnimationComponent.currentAnimation.isDrawing = false;
			} else {
				// Play invalid sound here
				mapCursorStateComponent.mapCursorState = MapCursorState.WAITING_FOR_VALID_MOVE;
			}
		}
	}
	
	public void startSystem(Level level) {
		mapCursor = getEngine().getEntitiesFor(Family.all(MapCursorStateComponent.class).get()).first();
		movementUtilityCalculator = new MovementUtilityCalculator(level);
		unitMapCellUpdater = getEngine().getSystem(UnitMapCellUpdater.class);
	}
}
