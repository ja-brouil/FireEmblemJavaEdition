package com.jb.fe.systems.movement;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.jb.fe.components.AnimationComponent;
import com.jb.fe.components.Artifical_IntelligenceComponent;
import com.jb.fe.components.MapCursorStateComponent;
import com.jb.fe.components.MapCursorStateComponent.MapCursorState;
import com.jb.fe.components.MovementStatsComponent.Unit_State;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.components.SoundComponent;
import com.jb.fe.components.StaticImageComponent;
import com.jb.fe.components.MovementStatsComponent;
import com.jb.fe.map.MapCell;
import com.jb.fe.systems.SystemPriorityDictionnary;
import com.jb.fe.systems.audio.SoundSystem;

/**
 * Controls the unit movements
 * 
 * @author james
 *
 */
public class UnitMovementSystem extends EntitySystem {

	// All Entities
	private ImmutableArray<Entity> allMovableEntities;

	// Mappers
	private ComponentMapper<AnimationComponent> aComponentMapper = ComponentMapper.getFor(AnimationComponent.class);
	private ComponentMapper<PositionComponent> pComponentMapper = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<MovementStatsComponent> sComponentMapper = ComponentMapper.getFor(MovementStatsComponent.class);
	private ComponentMapper<MapCursorStateComponent> mapCursorStateComponemtMapper = ComponentMapper
			.getFor(MapCursorStateComponent.class);
	private ComponentMapper<SoundComponent> soundComponentMapper = ComponentMapper.getFor(SoundComponent.class);
	private ComponentMapper<Artifical_IntelligenceComponent> aiComponentMapper = ComponentMapper.getFor(Artifical_IntelligenceComponent.class);

	// Map Cursor and UI elements
	private Entity mapCursor;

	// System for Unit Update
	private UnitMapCellUpdater unitMapCellUpdater;
	
	// Sound
	private SoundSystem soundSystem;

	public UnitMovementSystem() {
		priority = SystemPriorityDictionnary.MovementUpdate;
	}

	@Override
	public void addedToEngine(Engine engine) {
		allMovableEntities = engine.getEntitiesFor(
				Family.all(PositionComponent.class, MovementStatsComponent.class, AnimationComponent.class).get());

		engine.addEntityListener(
				Family.all(PositionComponent.class, MovementStatsComponent.class, AnimationComponent.class).get(),
				new EntityListener() {

					@Override
					public void entityRemoved(Entity entity) {
						allMovableEntities = engine.getEntitiesFor(
								Family.all(PositionComponent.class, MovementStatsComponent.class, AnimationComponent.class)
										.get());
					}

					@Override
					public void entityAdded(Entity entity) {
						allMovableEntities = engine.getEntitiesFor(
								Family.all(PositionComponent.class, MovementStatsComponent.class, AnimationComponent.class)
										.get());
					}
				});
	}

	@Override
	public void update(float delta) {
		for (int i = 0; i < allMovableEntities.size(); i++) {
			Entity unit = allMovableEntities.get(i);
			MovementStatsComponent unitStatsComponent = sComponentMapper.get(unit);
			
			if (unitStatsComponent.isMoving) {
				// Destination Cell and Starting Cell
				MapCell startingCell = unitStatsComponent.currentCell;
				MapCell nextCell = unitStatsComponent.pathfindingQueue.first();

				// Components
				AnimationComponent animationComponent = aComponentMapper.get(unit);
				PositionComponent unitPositionComponent = pComponentMapper.get(unit);

				// Process Unit Movement
				if (startingCell.position.x - nextCell.position.x > 0
						&& Math.abs(startingCell.position.y - nextCell.position.y) >= 0f) {
					// Left
					animationComponent.currentAnimation = animationComponent.allAnimationObjects.get("Left");
				} else if (startingCell.position.x - nextCell.position.x < 0
						&& Math.abs(startingCell.position.y - nextCell.position.y) >= 0f) {
					// Right
					animationComponent.currentAnimation = animationComponent.allAnimationObjects.get("Right");
				} else if (startingCell.position.y - nextCell.position.y > 0
						&& Math.abs(startingCell.position.x - nextCell.position.x) >= 0f) {
					// Down
					animationComponent.currentAnimation = animationComponent.allAnimationObjects.get("Down");
				} else if (startingCell.position.y - nextCell.position.y < 0
						&& Math.abs(startingCell.position.x - nextCell.position.x) >= 0f) {
					// Up
					animationComponent.currentAnimation = animationComponent.allAnimationObjects.get("Up");
				}

				// Move Unit || Smooth out over time
				if (Math.abs(unitPositionComponent.x - nextCell.position.x) >= 1.75f
						|| Math.abs(unitPositionComponent.y - nextCell.position.y) >= 1.75f) {
					unitPositionComponent.x += (nextCell.position.x - startingCell.position.x)
							* unitStatsComponent.animationMovementSpeed * Gdx.graphics.getDeltaTime();
					unitPositionComponent.y += (nextCell.position.y - startingCell.position.y)
							* unitStatsComponent.animationMovementSpeed * Gdx.graphics.getDeltaTime();
				} else {
					// Finalize movement to prevent rounding errors
					unitPositionComponent.x = nextCell.position.x;
					unitPositionComponent.y = nextCell.position.y;
					unitStatsComponent.currentCell = nextCell;
					unitStatsComponent.pathfindingQueue.removeFirst();
				}
				
				// Prevent weird crashes when FPS drops or game is paused
				if (Math.abs(unitPositionComponent.x - nextCell.position.x) >= 17f
						|| Math.abs(unitPositionComponent.y - nextCell.position.y) >= 17f) {
					unitPositionComponent.x = nextCell.position.x;
					unitPositionComponent.y = nextCell.position.y;
					unitStatsComponent.currentCell = nextCell;
					unitStatsComponent.pathfindingQueue.removeFirst();
				}
				
				// Play Sound
				soundSystem.playSound(soundComponentMapper.get(unit).allSoundObjects.get("Movement"));

				// Reset Movement back to the cursor if the queue is empty || this must be modified for AI movement as well
				if (unitStatsComponent.pathfindingQueue.size == 0) {
					unitStatsComponent.isMoving = false;
					MapCursorStateComponent mapCursorStateComponent = mapCursorStateComponemtMapper.get(mapCursor);
					PositionComponent mapPositionComponent = pComponentMapper.get(mapCursor);
					AnimationComponent cursorAnimation = aComponentMapper.get(mapCursor);
					
					// Ally Unit Reset | Enemy Unit Reset
					if (unitStatsComponent.isAlly) {
						// Enable Cursor Again (This should be set to action menu with unit)
						mapCursorStateComponent.mapCursorState = MapCursorState.MOVEMENT_ONLY;
						
						// Set Correct Animation Status -> Modify this later if unit did an action
						aComponentMapper.get(unit).currentAnimation = aComponentMapper.get(unit).allAnimationObjects.get("Idle");
						
						// Set Unit to Done status -> This needs to be changed to check later if you did an action first.
						unitStatsComponent.unit_State = Unit_State.DONE;
						
						// Move Cursor to where the new unit is now
						mapPositionComponent.x = unitPositionComponent.x;
						mapPositionComponent.y = unitPositionComponent.y;
						cursorAnimation.currentAnimation.isDrawing = true;
						
					} else {
						Artifical_IntelligenceComponent artifical_IntelligenceComponent = aiComponentMapper.get(unit);
						artifical_IntelligenceComponent.isProcessing = false;
					
						aComponentMapper.get(unit).currentAnimation = aComponentMapper.get(unit).allAnimationObjects.get("Idle");
					}
					
					// Turn off Map Cells
					for (MapCell mapCell : unitStatsComponent.allPossibleMoves) {
						mapCell.blueSquare.getComponent(StaticImageComponent.class).isEnabled = false;
					}
					
					for (MapCell mapCell : unitStatsComponent.allOutsideAttackMoves) {
						mapCell.redSquare.getComponent(StaticImageComponent.class).isEnabled = false;
					}
					
					// Update all units position
					unitMapCellUpdater.updateCellInfo();
				}
			}

		}
	}

	public void startSystem() {
		mapCursor = getEngine().getEntitiesFor(Family.all(MapCursorStateComponent.class).get()).first();
		unitMapCellUpdater = getEngine().getSystem(UnitMapCellUpdater.class);
		soundSystem = getEngine().getSystem(SoundSystem.class);
	}
}
