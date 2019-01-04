package com.jb.fe.systems.gamePlay;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import com.jb.fe.components.Artifical_IntelligenceComponent;
import com.jb.fe.components.MovementStatsComponent;
import com.jb.fe.components.MovementStatsComponent.Unit_State;
import com.jb.fe.systems.SystemPriorityDictionnary;
import com.jb.fe.systems.audio.MusicSystem;
import com.jb.fe.systems.audio.SoundSystem;
import com.jb.fe.systems.inputAndUI.UserInterfaceManager;

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
	
	// Transition
	private EndTurnTransition endTurnTransition;
	private UserInterfaceManager userInterfaceManager;
	
	// Audio
	private MusicSystem musicSystem;
	private SoundSystem soundSystem;

	// Component Mapper
	private ComponentMapper<MovementStatsComponent> uComponentMapper = ComponentMapper.getFor(MovementStatsComponent.class);
	private ComponentMapper<Artifical_IntelligenceComponent> aiComponentMapper = ComponentMapper.getFor(Artifical_IntelligenceComponent.class);

	public TurnManager() {
		priority = SystemPriorityDictionnary.TurnManager;
		enemyUnits = new Queue<Entity>();
		allyUnits = new Array<Entity>();
		turn_Status = Turn_Status.TRANSITION_INTO_ALLY;
	}

	@Override
	public void addedToEngine(Engine engine) {
		engine.addEntityListener(Family.all(MovementStatsComponent.class).get(), new EntityListener() {

			@Override
			public void entityRemoved(Entity entity) {
				allGameUnits = engine.getEntitiesFor(Family.all(MovementStatsComponent.class).get());

				if (uComponentMapper.get(entity).isAlly) {
					allyUnits.removeValue(entity, true);
				} else {
					enemyUnits.removeValue(entity, true);
				}
			}

			@Override
			public void entityAdded(Entity entity) {
				allGameUnits = engine.getEntitiesFor(Family.all(MovementStatsComponent.class).get());

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
			// UI for AI here, move the cursor and camera to where the AI is thinking of moving next
			turn_Status = Turn_Status.TRANSITION_INTO_ENEMY;
			
			// Get all enemies
			sortEnemyUnits();
			
			// Reset done status on the units
			for (Entity allyUnit : allyUnits) {
				uComponentMapper.get(allyUnit).unit_State = Unit_State.CAN_DO_BOTH;
			}
			
			// Stop Music
			musicSystem.stopCurrentSong();
			
		} else if (turn_Status.equals(Turn_Status.ENEMY_TURN)){
			// Enemy Phase
			// Are we empty? Yes -> done, go to player phase | No, keep going
			if (enemyUnits.size == 0 && unitBeingProcessed == null) {
				turn_Status = Turn_Status.TRANSITION_INTO_ALLY;
				
				// Set AI
				userInterfaceManager.pauseUI();
				musicSystem.stopCurrentSong();
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
		} else if (turn_Status.equals(Turn_Status.TRANSITION_INTO_ALLY)) {
			endTurnTransition.update(delta);
		} else if (turn_Status.equals(Turn_Status.TRANSITION_INTO_ENEMY)) {
			endTurnTransition.update(delta);
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
		PLAYER_TURN, ENEMY_TURN, TRANSITION_INTO_ENEMY, TRANSITION_INTO_ALLY
	}
	
	public Turn_Status getTurnStatus() {
		return turn_Status;
	}
	
	public void setTurnStatus(Turn_Status turn_Status) {
		this.turn_Status = turn_Status;
	}
	
	
	public void startSystem(AssetManager assetManager) {
		aiSystem = getEngine().getSystem(AISystem.class);
		musicSystem = getEngine().getSystem(MusicSystem.class);
		soundSystem = getEngine().getSystem(SoundSystem.class);
		endTurnTransition = new EndTurnTransition(assetManager, getEngine(), soundSystem, musicSystem, this);
		userInterfaceManager = getEngine().getSystem(UserInterfaceManager.class);
		endTurnTransition.setUserInterfaceManager(userInterfaceManager);
	}
	
	public UserInterfaceManager getUserInterfaceManager() {
		return userInterfaceManager;
	}
}
