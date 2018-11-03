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
import com.jb.fe.components.MapCursorStateComponent;
import com.jb.fe.components.MapCursorStateComponent.MapCursorState;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.components.UnitStatsComponent;
import com.jb.fe.map.MapCell;
import com.jb.fe.systems.SystemPriorityDictionnary;

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
	private ComponentMapper<UnitStatsComponent> sComponentMapper = ComponentMapper.getFor(UnitStatsComponent.class);
	private ComponentMapper<MapCursorStateComponent> mapCursorStateComponemtMapper = ComponentMapper
			.getFor(MapCursorStateComponent.class);

	// Map Cursor and UI elements
	private Entity mapCursor;

	// System for Unit U pdate
	private UnitMapCellUpdater unitMapCellUpdater;

	public UnitMovementSystem() {
		priority = SystemPriorityDictionnary.MovementUpdate;
	}

	@Override
	public void addedToEngine(Engine engine) {
		allMovableEntities = engine.getEntitiesFor(
				Family.all(PositionComponent.class, UnitStatsComponent.class, AnimationComponent.class).get());

		engine.addEntityListener(
				Family.all(PositionComponent.class, UnitStatsComponent.class, AnimationComponent.class).get(),
				new EntityListener() {

					@Override
					public void entityRemoved(Entity entity) {
						allMovableEntities = engine.getEntitiesFor(
								Family.all(PositionComponent.class, UnitStatsComponent.class, AnimationComponent.class)
										.get());
					}

					@Override
					public void entityAdded(Entity entity) {
						allMovableEntities = engine.getEntitiesFor(
								Family.all(PositionComponent.class, UnitStatsComponent.class, AnimationComponent.class)
										.get());
					}
				});
	}

	@Override
	public void update(float delta) {
		for (int i = 0; i < allMovableEntities.size(); i++) {
			Entity unit = allMovableEntities.get(i);
			UnitStatsComponent unitStatsComponent = sComponentMapper.get(unit);

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

				// Move Unit
				// Smooth out over time
				if (Math.abs(unitPositionComponent.x - nextCell.position.x) >= 1.75f
						|| Math.abs(unitPositionComponent.y - nextCell.position.y) >= 1.75f) {
					unitPositionComponent.x += (nextCell.position.x - startingCell.position.x)
							* unitStatsComponent.animationMovementSpeed * Gdx.graphics.getDeltaTime();
					unitPositionComponent.y += (nextCell.position.y - startingCell.position.y)
							* unitStatsComponent.animationMovementSpeed * Gdx.graphics.getDeltaTime();
					
					System.out.println(unitPositionComponent.x - nextCell.position.x);
				} else {
					// Finalize movement to prevent rounding errors
					unitPositionComponent.x = nextCell.position.x;
					unitPositionComponent.y = nextCell.position.y;
					unitStatsComponent.currentCell = nextCell;
					unitStatsComponent.pathfindingQueue.removeFirst();
				}

				// Reset Movement back to the cursor if the queue is empty
				if (unitStatsComponent.pathfindingQueue.size == 0) {
					unitStatsComponent.isMoving = false;
					MapCursorStateComponent mapCursorStateComponent = mapCursorStateComponemtMapper.get(mapCursor);
					PositionComponent mapPositionComponent = pComponentMapper.get(mapCursor);
					AnimationComponent cursorAnimation = aComponentMapper.get(mapCursor);

					// Move Cursor to where the new unit is now
					mapPositionComponent.x = unitPositionComponent.x;
					mapPositionComponent.y = unitPositionComponent.y;
					cursorAnimation.currentAnimation.isDrawing = true;

					// Enable Cursor Again (This should be set to action menu with unit)
					mapCursorStateComponent.mapCursorState = MapCursorState.MOVEMENT_ONLY;

					// Do an up on unit map location
					unitMapCellUpdater.updateCellInfo();
				}
			}

		}
	}

	public void startSystem() {
		mapCursor = getEngine().getEntitiesFor(Family.all(MapCursorStateComponent.class).get()).first();
		unitMapCellUpdater = getEngine().getSystem(UnitMapCellUpdater.class);
	}
}
