package com.jb.fe.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.jb.fe.UI.introscreenUI.IntroScreenUserInterface;
import com.jb.fe.systems.audio.MusicSystem;
import com.jb.fe.systems.audio.SoundSystem;
import com.jb.fe.systems.graphics.RenderSystem;
import com.jb.fe.systems.inputAndUI.UserInterfaceManager;

public class IntroScreen extends ScreenAdapter {
	
	// Main Game access
	private FireEmblemGame fireEmblemGame;
	
	// Engine
	private Engine engine;
	
	// Audio
	private MusicSystem musicSystem;
	private SoundSystem soundSystem;
	
	// Audio Filepaths
	private String musicFilePath = "music/feMainTheme.mp3";
	//private String audioFilePath;
	
	// User Interface Manager for the Intro Screen
	private UserInterfaceManager introUserInterfaceManager;
	
	public IntroScreen(Engine engine, FireEmblemGame fireEmblemGame) {
		this.fireEmblemGame = fireEmblemGame;
		this.engine = engine;
		this.musicSystem = fireEmblemGame.getMusicSystem();
		this.soundSystem = fireEmblemGame.getSoundSystem();
	}
	
	/**
	 * Call this when starting the intro scene
	 */
	public void startIntro(){
		// Start User interface manager
		introUserInterfaceManager = new UserInterfaceManager(fireEmblemGame);
		introUserInterfaceManager.allUserInterfaceStates.put("Intro Screen", new IntroScreenUserInterface(fireEmblemGame.getAssetManager(), soundSystem, introUserInterfaceManager));
	
		// Add Entity to engine
		((IntroScreenUserInterface) introUserInterfaceManager.allUserInterfaceStates.get("Intro Screen")).addEntities(engine);
		
		// Start graphic engine
		engine.getSystem(RenderSystem.class).setProcessing(true);
		
		// Play Intro Music
		musicSystem.addNewSong("Intro Song", musicFilePath, fireEmblemGame.getAssetManager());
		musicSystem.playSong("Intro Song", 0.5f);
	}
	
	@Override
	public void render(float delta) {
		engine.update(delta);
	}
	
	/**
	 * Call this when going to the next screen
	 * @param nextScreen The next screen to move to
	 * @param game
	 */
	public void nextScreen(ScreenAdapter nextScreen, FireEmblemGame game) {
		// Remove User Interface Manager
		engine.removeSystem(introUserInterfaceManager);
		
		// To the next screen
		game.setScreen(nextScreen);
	}
}
