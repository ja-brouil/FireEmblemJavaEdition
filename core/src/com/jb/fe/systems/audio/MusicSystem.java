package com.jb.fe.systems.audio;

import java.util.HashMap;

import com.badlogic.gdx.assets.AssetManager;
import com.jb.fe.audio.MusicObject;

/*
 * Controls all the music in the game. 
 */
public class MusicSystem {
	
	// Music List
	private HashMap<String, MusicObject> allMusicObjects;
	
	private MusicObject currentSong;
	
	public MusicSystem() {
		allMusicObjects = new HashMap<>();
	}
	
	public MusicObject getSong(String songName) {
		return allMusicObjects.get(songName);
	}
	
	public MusicObject getCurrentSong() {
		return currentSong;
	}
	
	public void setCurrentSong(String songName) {
		currentSong = allMusicObjects.get(songName);
	}
	
	public void removeSong(String songName) {
		allMusicObjects.remove(songName);
	}
	
	public void addNewSong(String songName, String fileLocation, AssetManager assetManager) {
		allMusicObjects.put(songName, new MusicObject(fileLocation, assetManager));
	}
	
	public void playSong(String songName) {
		MusicObject musicObject = allMusicObjects.get(songName);
		musicObject.getSong().setVolume(musicObject.volume);
		
	}
	
	public void playCurrentSong() {
		if (currentSong != null) {
			currentSong.getSong().setVolume(currentSong.volume);
			currentSong.getSong().play();
		}
	}
}
