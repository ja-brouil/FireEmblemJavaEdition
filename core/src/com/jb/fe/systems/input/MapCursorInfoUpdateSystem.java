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
import com.jb.fe.components.SoundComponent;
import com.jb.fe.components.StaticImageComponent;
import com.jb.fe.components.UnitStatsComponent;
import com.jb.fe.level.Level;
import com.jb.fe.map.MapCell;
import com.jb.fe.systems.SystemPriorityDictionnary;
import com.jb.fe.systems.audio.SoundSystem;
import com.jb.fe.systems.gamePlay.AISystem;
import com.jb.fe.systems.movement.MovementUtilityCalculator;
import com.jb.fe.systems.movement.UnitMapCellUpdater;

public class MapCursorInfoUpdateSystem extends EntitySystem{

	// Entites Required
	private Entity mapCursor;
	
	// Game Unit Array
	private ImmutableArray<Entity> allGameUnits;
	
	// Retrievers
	private ComponentMapper<AnimationComponent> animationComponentMapper = ComponentMapper.getFor(AnimationComponent.class);
	private ComponentMapper<MapCursorStateComponent> mapCursorComponentMapper = ComponentMapper.getFor(MapCursorStateComponent.class);
	private ComponentMapper<UnitStatsComponent> unitStatComponentMapper = ComponentMapper.getFor(UnitStatsComponent.class);
	private ComponentMapper<StaticImageComponent> staticImageComponentMapper = ComponentMapper.getFor(StaticImageComponent.class);
	private ComponentMapper<PositionComponent> pComponentMapper = ComponentMapper.getFor(PositionComponent.class);
	
	// Square Selector Calulator
	private MovementUtilityCalculator movementUtilityCalculator;
	
	// Calculators
	private UnitMapCellUpdater unitMapCellUpdater;
	
	// Sound
	private SoundSystem soundSystem;
	
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
		MapCursorStateComponent mapCursorStateComponent = mapCursorComponentMapper.get(mapCursor);
		
		// Movement
		if (mapCursorStateComponent.mapCursorState.equals(MapCursorState.MOVEMENT_ONLY)) {
			
			// Deselect Unit
			movementUtilityCalculator.resetMovementAlgorithms();
			mapCursorStateComponent.unitSelected = null;
			
			PositionComponent mapCursorPositionComponent = pComponentMapper.get(mapCursor);
			
			for (Entity unit : allGameUnits) {
				AnimationComponent unitAnimation = animationComponentMapper.get(unit);
				UnitStatsComponent unitStatsComponent = unitStatComponentMapper.get(unit);
				PositionComponent positionComponent = pComponentMapper.get(unit);
				// Reset
				unitAnimation.currentAnimation = unitAnimation.allAnimationObjects.get("Idle");

				if (positionComponent.x == mapCursorPositionComponent.x && positionComponent.y == mapCursorPositionComponent.y) {
					// Select only ally units for movement purposes
					if (unitStatsComponent.isAlly) {
						unitAnimation.currentAnimation = unitAnimation.allAnimationObjects.get("Hovering");
						animationComponentMapper.get(mapCursor).currentAnimation.isDrawing = false;
						staticImageComponentMapper.get(mapCursor).isEnabled = true;
						mapCursorStateComponent.unitSelected = unit;
						
					}
					mapCursorStateComponent.unitSelected = unit;
					
				}
			}
			
			// Set Animation Back || Prevent from updating if not animating
			if (mapCursorStateComponent.unitSelected == null) {
				animationComponentMapper.get(mapCursor).currentAnimation.isDrawing = true;
				staticImageComponentMapper.get(mapCursor).isEnabled = false;
			}
		} 
		
		// Unit Selection
		else if (mapCursorStateComponent.mapCursorState.equals(MapCursorState.UNIT_SELECTED)) {
			animationComponentMapper.get(mapCursor).currentAnimation.isDrawing = true;
			staticImageComponentMapper.get(mapCursor).isEnabled = false;
			
			// Only units that are allies can be selected!
			UnitStatsComponent unitStatsComponent = unitStatComponentMapper.get(mapCursorStateComponent.unitSelected);
			if (unitStatsComponent.isAlly) {
				// Set Animation
				AnimationComponent unitAnimation = animationComponentMapper.get(mapCursorStateComponent.unitSelected);
				unitAnimation.currentAnimation = unitAnimation.allAnimationObjects.get("Selected");
				
				// Calculate Movement
				unitMapCellUpdater.updateCellInfo();
				movementUtilityCalculator.calculateAllPossibleMoves(mapCursorStateComponent.unitSelected);
				
				// Set Cursor to Allow movement
				mapCursorStateComponent.mapCursorState = MapCursorState.WAITING_FOR_VALID_MOVE;
			} else {
				// See Enemy Move tile
				unitMapCellUpdater.updateCellInfo();
				movementUtilityCalculator.calculateAllPossibleMoves(mapCursorStateComponent.unitSelected);
				mapCursorStateComponent.mapCursorState = MapCursorState.WAITING_FOR_VALID_MOVE;
			}
		}
		
		// Check Valid Move
		else if (mapCursorStateComponent.mapCursorState.equals(MapCursorState.VALID_MOVE_CHECK)) {
			// Is the unit 'selected' an ally?
			UnitStatsComponent unitStatsComponent = unitStatComponentMapper.get(mapCursorStateComponent.unitSelected);
			if (!unitStatsComponent.isAlly) {
				soundSystem.playSound(mapCursor.getComponent(SoundComponent.class).allSoundObjects.get("Invalid"));
				mapCursorStateComponent.mapCursorState = MapCursorState.MOVEMENT_ONLY;
				return;
			}
			
			// Is the Move Valid?
			MapCell cursorMapCell = movementUtilityCalculator.getMapCell(mapCursor);
			if (cursorMapCell.isOccupied && !cursorMapCell.occupyingUnit.equals(mapCursorStateComponent.unitSelected)) {
				soundSystem.playSound(mapCursor.getComponent(SoundComponent.class).allSoundObjects.get("Invalid"));
				mapCursorStateComponent.mapCursorState = MapCursorState.WAITING_FOR_VALID_MOVE;
				return;
			}
			
			// Is Move in the hashset
			if (movementUtilityCalculator.getAllPossibleMoves().contains(cursorMapCell)) {
				// Start unit movement system
				mapCursorStateComponent.unitSelected.getComponent(UnitStatsComponent.class).isMoving = true;
				movementUtilityCalculator.createPathFindingQueue(cursorMapCell, mapCursorStateComponent.unitSelected);
				mapCursorStateComponent.mapCursorState = MapCursorState.DISABLED;
				
				// Stop Drawing Map Cursor
				AnimationComponent mapCursorAnimationComponent = animationComponentMapper.get(mapCursor);
				mapCursorAnimationComponent.currentAnimation.isDrawing = false;
				
				// Accept Sound
				soundSystem.playSound(mapCursor.getComponent(SoundComponent.class).allSoundObjects.get("Accept"));
			} else {
				soundSystem.playSound(mapCursor.getComponent(SoundComponent.class).allSoundObjects.get("Invalid"));
				mapCursorStateComponent.mapCursorState = MapCursorState.WAITING_FOR_VALID_MOVE;
			}
		} else if (mapCursorStateComponent.mapCursorState.equals(MapCursorState.DISABLED)) {
			AnimationComponent cursorAnimation = animationComponentMapper.get(mapCursor);
			StaticImageComponent staticImageComponent = staticImageComponentMapper.get(mapCursor);
			
			cursorAnimation.currentAnimation.isDrawing = false;
			staticImageComponent.isEnabled = false;
		}
	}
	
	public void startSystem(Level level) {
		mapCursor = getEngine().getEntitiesFor(Family.all(MapCursorStateComponent.class).get()).first();
		unitMapCellUpdater = getEngine().getSystem(UnitMapCellUpdater.class);
		movementUtilityCalculator = new MovementUtilityCalculator(level, unitMapCellUpdater);
		soundSystem = getEngine().getSystem(SoundSystem.class);
		getEngine().getSystem(AISystem.class).setMovementCalculator(movementUtilityCalculator);
	}
}
