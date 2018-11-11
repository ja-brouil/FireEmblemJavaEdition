package com.jb.fe.systems.audio;

import java.util.HashMap;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.assets.AssetManager;
import com.jb.fe.audio.MusicObject;

/*
 * Controls all the music in the game. 
 */
public class MusicSystem extends EntitySystem{
	
	// Music List
	private HashMap<String, MusicObject> allMusicObjects;
	
	private MusicObject currentSong;
	
	public MusicSystem() {
		allMusicObjects = new HashMap<>();
		setProcessing(false);
	}
	
	public MusicObject getSong(String songName) {
		return allMusicObjects.get(songName);
	}
	
	public MusicObject getCurrentSong() {
		return currentSong;
	}
	
	public void setCurrentSong(String songName, boolean isLoop) {
		currentSong = allMusicObjects.get(songName);
		currentSong.getSong().setLooping(isLoop);
	}
	
	public void removeSong(String songName) {
		allMusicObjects.remove(songName);
	}
	
	public void addNewSong(String songName, String fileLocation, AssetManager assetManager) {
		allMusicObjects.put(songName, new MusicObject(fileLocation, assetManager));
	}
	
	// Only one song should be playing at a time
	public void playSong(String songName) {
		if (currentSong != null && currentSong.getSong().isPlaying()) {
			currentSong.getSong().stop();
		}
		MusicObject musicObject = allMusicObjects.get(songName);
		currentSong = musicObject;
		playCurrentSong();
	}
	
	public void playCurrentSong() {
		if (currentSong != null) {
			currentSong.getSong().setVolume(currentSong.volume);
			currentSong.getSong().play();
		}
	}
	
	public void stopCurrentSong() {
		if (currentSong != null && currentSong.getSong().isPlaying()) {
			currentSong.getSong().stop();
		}
	}
	
	/*
	 * Will pause or resume the song
	 */
	public void pauseCurrentSong() {
		if (currentSong.getSong().isPlaying()) {
			currentSong.getSong().pause();
		} else {
			currentSong.getSong().play();
		}
	}
}
