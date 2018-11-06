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
import com.jb.fe.components.UnitStatsComponent;
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

	// Component Mapper
	private ComponentMapper<UnitStatsComponent> uComponentMapper = ComponentMapper.getFor(UnitStatsComponent.class);

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
					return; // Exit, we don't want to end the turn if we find a unit that isn't done.
				}
			}
			
			// All Units are done, move to the AI turn -> Map Cursor will need to be disabled here too
			this.turn_Status = Turn_Status.ENEMY_TURN;
			
			// Reset done status on the units
			for (Entity allyUnit : allyUnits) {
				uComponentMapper.get(allyUnit).unit_State = Unit_State.CAN_DO_BOTH;
			}
		} else {
			// Enemy Phase
			// Do AI here
			turn_Status = Turn_Status.PLAYER_TURN;
			
			// Reset AI done status here
		}
	}
	
	public void sortEnemyUnits() {
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
}
