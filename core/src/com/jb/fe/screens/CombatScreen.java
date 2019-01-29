package com.jb.fe.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.jb.fe.UI.combatUIScreen.CombatScreenUI;
import com.jb.fe.systems.audio.MusicSystem;
import com.jb.fe.systems.audio.SoundSystem;
import com.jb.fe.systems.graphics.CombatAnimationSystem;
import com.jb.fe.systems.inputAndUI.UserInterfaceManager;

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
	private float fadeOutSpeed = 5;
	private float maxTime = 0.5f;
	
	// UI Elements
	private UserInterfaceManager userInterfaceManager;
	
	// Combat Animation System
	private CombatAnimationSystem combatAnimationSystem;
	
	// Engine
	private Engine engine;
	
	// Entity
	private CombatScreenUI combatScreenUI;
	
	// Tests
	float timer = 0;
	float musictimer = 0.5f;
	float volumetest = 1;
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
	}
	
	@Override
	public void render(float delta){
		engine.update(delta);
		
		if (timer == 0) {
			System.out.println("START OF COMBAT PHASE");
		}
		timer += Gdx.graphics.getDeltaTime();
		
		// Fade out song
		if (timer >= 0 && timer <= 0.75f) {
			volumetest = 1 - (timer / musictimer);
			if (volumetest <= 0) { volumetest = 0; }
			musicSystem.getCurrentSong().getSong().setVolume(volumetest);
		}
		
		// Play next song
		if  (timer >= 0.75f && musicSystem.getCurrentSong().getSong().getVolume() <= 0.05f) {
			musicSystem.playSong("Attack", 1f);
		}
		
		// Fade song before 5 seconds
		if (timer >= 4.5f && timer <= 5.5) {
			secondMusicTimer += Gdx.graphics.getDeltaTime();
			volumetest = 1 - (secondMusicTimer / musictimer);
			if (volumetest <= 0) { volumetest = 0; }
			musicSystem.getCurrentSong().getSong().setVolume(volumetest);
		}
		
		if (timer >= 5.5) {
			System.out.println("COMBAT IS DONE");
			fireEmblemGame.setScreen(FireEmblemGame.allGameScreens.get("GameScreen"));
		}
	}
	
	/**
	 * Set combat options
	 */
	@Override
	public void show(){
		System.out.println("Combat screen started!");
		
		// Turn off systems that don't need to be active
		userInterfaceManager.setStates(userInterfaceManager.currentState, combatScreenUI);
	}
	
	/**
	 * Return to main game screen
	 */
	@Override
	public void hide() {
		System.out.println("Combat screen ended!");
		musicSystem.playSong("Ally Battle Theme SD", 1f);
		timer = 0;
		musictimer = 0;
		
		// Turn back on all systems
		userInterfaceManager.setStates(combatScreenUI, userInterfaceManager.allUserInterfaceStates.get("MapCursor"));
	}
}
