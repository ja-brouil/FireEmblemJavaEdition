package com.jb.fe.systems.gamePlay;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.jb.fe.UI.TurnChangeTransitionFactory;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.components.SoundComponent;
import com.jb.fe.components.StaticImageComponent;
import com.jb.fe.components.MapCursorStateComponent.MapCursorState;
import com.jb.fe.screens.FireEmblemGame;
import com.jb.fe.systems.audio.MusicSystem;
import com.jb.fe.systems.audio.SoundSystem;
import com.jb.fe.systems.gamePlay.TurnManager.Turn_Status;

public class EndTurnTransition {

	// Entities
	private Entity enemyEndTurn;
	private Entity allyEndTurn;

	// Audio
	private SoundSystem soundSystem;
	private MusicSystem musicSystem;
	private boolean soundAlreadyPlayed;

	// Turn Manager
	private TurnManager turnManager;

	// Mappers
	private ComponentMapper<PositionComponent> pComponentMapper = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<StaticImageComponent> sComponentMapper = ComponentMapper.getFor(StaticImageComponent.class);
	private ComponentMapper<SoundComponent> soundComponentMapper = ComponentMapper.getFor(SoundComponent.class);

	// Numbers
	private float speed = 10;
	private float maxTime = 1f;
	private float currentTime = 0;

	public EndTurnTransition(AssetManager assetManager, Engine engine, SoundSystem soundSystem, MusicSystem musicSystem,
			TurnManager turnManager) {
		this.musicSystem = musicSystem;
		this.turnManager = turnManager;
		this.soundSystem = soundSystem;
		// Create transition entities
		TurnChangeTransitionFactory turnChangeTransitionFactory = new TurnChangeTransitionFactory(assetManager);
		enemyEndTurn = turnChangeTransitionFactory.createTurnPhase("transition/enemy phase.png");
		allyEndTurn = turnChangeTransitionFactory.createTurnPhase("transition/player phase.png");

		engine.addEntity(allyEndTurn);
		engine.addEntity(enemyEndTurn);
	}

	public void update(float delta) {
		// Enemy Transition State
		Entity transitionGraphic;
		if (turnManager.getTurnStatus().equals(Turn_Status.TRANSITION_INTO_ALLY)) {
			transitionGraphic = allyEndTurn;
		} else {
			transitionGraphic = enemyEndTurn;
		}

		PositionComponent positionComponent = pComponentMapper.get(transitionGraphic);
		StaticImageComponent staticImageComponent = sComponentMapper.get(transitionGraphic);
		// Turn drawing on
		staticImageComponent.isEnabled = true;

		// Timer
		currentTime += delta;

		// Sound
		playSound(enemyEndTurn);
		soundAlreadyPlayed = true;

		// Set Alpha
		staticImageComponent.alpha = currentTime / maxTime;

		if (currentTime / maxTime >= 1) {
			staticImageComponent.alpha = 1;
			// Set Image to correct place
			positionComponent.x += speed;

			// Break drawing
			staticImageComponent.setNewImageLocation((int) positionComponent.x, 0, (int) staticImageComponent.width,
					(int) staticImageComponent.height);
			staticImageComponent.width = FireEmblemGame.WIDTH - positionComponent.x;

			// Finish animation | this needs to be changed to when its off the viewport of
			// the camera later
			if (positionComponent.x > FireEmblemGame.WIDTH) {
				positionComponent.x = 0;
				staticImageComponent.setNewImageLocation(0, 0, 240, 32);
				staticImageComponent.isEnabled = false;
				staticImageComponent.width = 240;
				currentTime = 0;
				soundAlreadyPlayed = false;
				
				if (turnManager.getTurnStatus().equals(Turn_Status.TRANSITION_INTO_ALLY)) {
					turnManager.setTurnStatus(Turn_Status.PLAYER_TURN);
					musicSystem.setCurrentSong("Ally Battle Theme SD", true);
					musicSystem.playCurrentSong();
					turnManager.getMapCursorStateComponent().mapCursorState = MapCursorState.MOVEMENT_ONLY;
				} else {
					turnManager.setTurnStatus(Turn_Status.ENEMY_TURN);
					musicSystem.setCurrentSong("Enemy Phase", true);
					musicSystem.playCurrentSong();
				}
				
			}
		}

	}

	private void playSound(Entity transitionGraphic) {
		if (!soundAlreadyPlayed) {
			soundSystem.playSound(soundComponentMapper.get(transitionGraphic).allSoundObjects.get("Transition"));
		}
	}
}