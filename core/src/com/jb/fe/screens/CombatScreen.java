package com.jb.fe.screens;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.jb.fe.UI.combatUIScreen.CombatScreenUI;
import com.jb.fe.components.AnimationComponent;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.systems.audio.MusicSystem;
import com.jb.fe.systems.audio.SoundSystem;
import com.jb.fe.systems.gamePlay.CombatSystem;
import com.jb.fe.systems.gamePlay.TurnManager;
import com.jb.fe.systems.gamePlay.TurnManager.Turn_Status;
import com.jb.fe.systems.graphics.CombatAnimationSystem;
import com.jb.fe.systems.inputAndUI.UserInterfaceManager;
import com.jb.fe.systems.movement.UnitMapCellUpdater;

/**
 * Notice:
 * Move a lot of this code off to new systems for this screen. All of this is here just for test purposes.
 * @author james
 *
 */
public class CombatScreen extends ScreenAdapter {
	
	// Screen access
	private FireEmblemGame fireEmblemGame;
	
	// Audio
	private MusicSystem musicSystem;
	private SoundSystem soundSystem;
	
	// Ending Phase
	private boolean endingPhase;
	
	// Animation Call
	private boolean animationCalled;
	
	// UI Elements
	private UserInterfaceManager userInterfaceManager;
	
	// Combat Animation System
	private CombatAnimationSystem combatAnimationSystem;
	
	// Comp Mappers
	private ComponentMapper<PositionComponent> pComponentMapper = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<AnimationComponent> aComponentMapper = ComponentMapper.getFor(AnimationComponent.class);
	
	// Engine and Systems
	private Engine engine;
	
	// Entity
	private CombatScreenUI combatScreenUI;
	
	// Music fadeout timers
	float timer = 0;
	float musicTimer = 0.5f;
	float volumeLevel = 1;
	float secondMusicTimer = 0;
	
	public CombatScreen(FireEmblemGame fireEmblemGame, GameScreen gameScreen) {
		this.fireEmblemGame = fireEmblemGame;
		musicSystem = this.fireEmblemGame.getMusicSystem();
		soundSystem = this.fireEmblemGame.getSoundSystem();
		userInterfaceManager = gameScreen.getUserInterfaceManager();
		engine = gameScreen.getEngine();
		
		// Add Songs
		musicSystem.addNewSong("Attack", "music/Attack Theme.mp3", gameScreen.getAssetManager());
		musicSystem.addNewSong("Defense", "music/Defense Theme.mp3", gameScreen.getAssetManager());
		
		// Start Combat Screen UI
		combatScreenUI = new CombatScreenUI(gameScreen.getAssetManager(), soundSystem, userInterfaceManager);
		userInterfaceManager.allUserInterfaceStates.put("Combat Screen UI", combatScreenUI);
		engine.addEntity(combatScreenUI.getMainEntity());
		
		// Combat Animation System
		combatAnimationSystem = new CombatAnimationSystem(combatScreenUI);
		engine.addSystem(combatAnimationSystem);
		combatAnimationSystem.createInitialEntity();
		
		// Ending phase
		endingPhase = false;
		
		// Animation
		animationCalled = false;
	}
	
	@Override
	public void render(float delta){
		// Update all regular systems | Does this need to be moved lower?
		engine.update(delta);
		
		// Combat System start
		if (timer == 0) {
			CombatAnimationSystem.isProcessing = true;
		}
		timer += Gdx.graphics.getDeltaTime();
		
		// Fade out current song
		if (timer >= 0 && timer <= 0.75f) {
			volumeLevel = 1 - (timer / musicTimer);
			if (volumeLevel <= 0) { volumeLevel = 0; }
			musicSystem.getCurrentSong().getSong().setVolume(volumeLevel);
			if (!animationCalled) {
				combatAnimationSystem.startAnimation();
				animationCalled = true;
			}
		}
		
		// Play next song (ally vs enemy)
		if  (timer >= 0.75f && musicSystem.getCurrentSong().getSong().getVolume() <= 0.05f && !endingPhase) {
			if (engine.getSystem(TurnManager.class).getTurnStatus().equals(Turn_Status.ENEMY_TURN)) {
				musicSystem.playSong("Defense", 1f);
			} else {
				musicSystem.playSong("Attack", 1f);
			}
		}
		
		// Combat animations take over here
		combatAnimationSystem.update(delta);
		if (CombatAnimationSystem.isProcessing) {
			return;
		}
		
		// Set Song
		if (CombatAnimationSystem.combatAnimationsAreComplete && !endingPhase) {
			
			secondMusicTimer += Gdx.graphics.getDeltaTime();
			volumeLevel = 1 - (secondMusicTimer / musicTimer);;
			if (volumeLevel <= 0) { 
				volumeLevel = 0; 
				endingPhase = true;
			}
			musicSystem.getCurrentSong().getSong().setVolume(volumeLevel);
			return;
		}
		
		// End combat phase and back to normal gameplay
		if (endingPhase) {
			
			// Set Units back to original
			if (CombatSystem.attackingUnit != null) {
				pComponentMapper.get(CombatSystem.attackingUnit).x = combatAnimationSystem.attackingX;
				pComponentMapper.get(CombatSystem.attackingUnit).y = combatAnimationSystem.attackingY;
				aComponentMapper.get(CombatSystem.attackingUnit).currentAnimation = aComponentMapper.get(CombatSystem.attackingUnit).allAnimationObjects.get("Idle");
			}
			
			
			if (CombatSystem.defendingUnit != null) {
				pComponentMapper.get(CombatSystem.defendingUnit).x = combatAnimationSystem.defendingX;
				pComponentMapper.get(CombatSystem.defendingUnit).y = combatAnimationSystem.defendingY;
				aComponentMapper.get(CombatSystem.defendingUnit).currentAnimation = aComponentMapper.get(CombatSystem.defendingUnit).allAnimationObjects.get("Idle");
			}
			
			// Reset Animation System
			animationCalled = false;
			CombatAnimationSystem.combatAnimationsAreComplete = false;
			
			// Update Cells
			engine.getSystem(UnitMapCellUpdater.class).updateCellInfo();
			
			// Back to Game Screen
			fireEmblemGame.setScreen(FireEmblemGame.allGameScreens.get("GameScreen"));
		}
	}
	
	/**
	 * Set combat options
	 */
	@Override
	public void show(){
		System.out.println(timer);
		
		// Turn off systems that don't need to be active
		engine.getSystem(TurnManager.class).setProcessing(false);
		userInterfaceManager.setStates(userInterfaceManager.currentState, combatScreenUI);
	}
	
	/**
	 * Return to main game screen
	 */
	@Override
	public void hide() {
		timer = 0;
		musicTimer = 0;
		secondMusicTimer = 0;
		volumeLevel = 1;
		
		// Turn back on all systems
		engine.getSystem(TurnManager.class).setProcessing(true);
		
		// Something here to be changed when it swaps over to the AI turn. Check what logic order should be changed
		musicSystem.getSong("Enemy Phase").getSong().setVolume(1f);
		musicSystem.getSong("Ally Battle Theme SD").getSong().setVolume(1f);
		engine.getSystem(TurnManager.class).update(Gdx.graphics.getDeltaTime());
		if (engine.getSystem(TurnManager.class).getTurnStatus().equals(Turn_Status.ENEMY_TURN)) {
			musicSystem.playSong("Enemy Phase", 1f);
		} else if (engine.getSystem(TurnManager.class).getTurnStatus().equals(Turn_Status.PLAYER_TURN)){
			musicSystem.playSong("Ally Battle Theme SD", 1f);
		}
		
		// Back to gameplay
		userInterfaceManager.setStates(combatScreenUI, userInterfaceManager.allUserInterfaceStates.get("MapCursor"));
	}
}
