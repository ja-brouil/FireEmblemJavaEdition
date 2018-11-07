package com.jb.fe.systems.gamePlay;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import com.jb.fe.components.AnimationComponent;
import com.jb.fe.components.Artifical_IntelligenceComponent;
import com.jb.fe.components.MapCursorStateComponent;
import com.jb.fe.components.StaticImageComponent;
import com.jb.fe.components.UnitStatsComponent;
import com.jb.fe.components.MapCursorStateComponent.MapCursorState;
import com.jb.fe.components.UnitStatsComponent.Unit_State;
import com.jb.fe.systems.SystemPriorityDictionnary;

/*
 * Controls experience and status of the units when they have moved and stuff.
 */
public class TurnManager extends EntitySystem {

	// Turn Status
	private Turn_Status turn_Status;

	// All Game Units
	private ImmutableArray<Entity> allGameUnits;
	private Queue<Entity> enemyUnits;
	private Array<Entity> allyUnits;
	
	// Unit being processed
	private Entity unitBeingProcessed;
	
	// AI Engine
	private AISystem aiSystem;
	
	// UI Elements -> Will need a manager for this later
	private Entity mapCursor;

	// Component Mapper
	private ComponentMapper<UnitStatsComponent> uComponentMapper = ComponentMapper.getFor(UnitStatsComponent.class);
	private ComponentMapper<Artifical_IntelligenceComponent> aiComponentMapper = ComponentMapper.getFor(Artifical_IntelligenceComponent.class);
	private ComponentMapper<MapCursorStateComponent> cursorStateComponentMapper = ComponentMapper.getFor(MapCursorStateComponent.class);
	private ComponentMapper<AnimationComponent> aComponentMapper = ComponentMapper.getFor(AnimationComponent.class);
	private ComponentMapper<StaticImageComponent> sComponentMapper = ComponentMapper.getFor(StaticImageComponent.class);

	public TurnManager() {
		priority = SystemPriorityDictionnary.TurnManager;
		enemyUnits = new Queue<Entity>();
		allyUnits = new Array<Entity>();
		turn_Status = Turn_Status.PLAYER_TURN;
	}

	@Override
	public void addedToEngine(Engine engine) {
		engine.addEntityListener(Family.all(UnitStatsComponent.class).get(), new EntityListener() {

			@Override
			public void entityRemoved(Entity entity) {
				allGameUnits = engine.getEntitiesFor(Family.all(UnitStatsComponent.class).get());

				if (uComponentMapper.get(entity).isAlly) {
					allyUnits.removeValue(entity, true);
				} else {
					enemyUnits.removeValue(entity, true);
				}
			}

			@Override
			public void entityAdded(Entity entity) {
				allGameUnits = engine.getEntitiesFor(Family.all(UnitStatsComponent.class).get());

				if (uComponentMapper.get(entity).isAlly) {
					allyUnits.add(entity);
				} else {
					enemyUnits.addFirst(entity);
				}
			}
		});
	}
	
	@Override
	public void update(float delta) {
		// Player Phase
		if (turn_Status.equals(Turn_Status.PLAYER_TURN)) {
			for (Entity allyUnit : allyUnits) {
				if (!uComponentMapper.get(allyUnit).unit_State.equals(Unit_State.DONE)) {
					// Exit, we don't want to end the turn if we find a unit that isn't done.
					return; 
				}
			}
			
			// All Units are done, move to the AI turn
			this.turn_Status = Turn_Status.ENEMY_TURN;
			cursorStateComponentMapper.get(mapCursor).mapCursorState = MapCursorState.DISABLED;
			aComponentMapper.get(mapCursor).currentAnimation.isDrawing = false;
			sComponentMapper.get(mapCursor).isEnabled = false;
			
			// Get all enemies
			sortEnemyUnits();
			
			// Reset done status on the units
			for (Entity allyUnit : allyUnits) {
				uComponentMapper.get(allyUnit).unit_State = Unit_State.CAN_DO_BOTH;
			}
		} else {
			// Enemy Phase
			// Are we empty? Yes -> done, go to player phase | No, keep going
			if (enemyUnits.size == 0 && unitBeingProcessed == null) {
				turn_Status = Turn_Status.PLAYER_TURN;
				cursorStateComponentMapper.get(mapCursor).mapCursorState = MapCursorState.MOVEMENT_ONLY;
				aComponentMapper.get(mapCursor).currentAnimation.isDrawing = true;
				sComponentMapper.get(mapCursor).isEnabled = true;
				return;
			}
			
			// Unit
			if (unitBeingProcessed == null) {
				unitBeingProcessed = enemyUnits.removeFirst();
				aiComponentMapper.get(unitBeingProcessed).isProcessing = true;
				aiSystem.setEnemyUnit(unitBeingProcessed);
				aiSystem.processAI();
				return;
			}
			
			// Are we still processing the unit? Yes -> do nothing, No -> move on to the next unit
			if (!aiComponentMapper.get(unitBeingProcessed).isProcessing) {
				unitBeingProcessed = null;
			}
		}
	}
	
	public void sortEnemyUnits() {
		enemyUnits.clear();
		for (Entity entity : allGameUnits) {
			if (!uComponentMapper.get(entity).isAlly) {
				enemyUnits.addFirst(entity);
			}
		}
	}
	
	public static enum Turn_Status {
		PLAYER_TURN, ENEMY_TURN
	}
	
	public void setTurnStatus(Turn_Status turn_Status) {
		this.turn_Status = turn_Status;
	}
	
	public void startSystem() {
		aiSystem = getEngine().getSystem(AISystem.class);
		mapCursor = getEngine().getEntitiesFor(Family.all(MapCursorStateComponent.class).get()).first();
	}
}
