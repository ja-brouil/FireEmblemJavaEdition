package com.jb.fe.systems.audio;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.jb.fe.audio.SoundObject;

/*
 * Player for all the sounds
 */

public class SoundSystem {
	
	private HashMap<String, SoundObject> allSoundObjects;
	
	public SoundSystem() {
		allSoundObjects = new HashMap<>();
	}
	
	public void addSound(String soundName, String fileLocation, AssetManager assetManager) {
		allSoundObjects.put(soundName, new SoundObject(fileLocation, assetManager));
	}
	
	public void removeSound(String soundName) {
		allSoundObjects.remove(soundName);
	}
	
	public void playSound(String soundName) {
		// Check if delay is 0
		SoundObject soundObject = allSoundObjects.get(soundName);
		
		if (soundObject.delayTimer == 0) {
			soundObject.getSound().play(soundObject.getVolume());
		} else {
			soundObject.currentTimer += Gdx.graphics.getDeltaTime();
			if (soundObject.currentTimer >= soundObject.delayTimer) {
				soundObject.getSound().play(soundObject.volume);
				soundObject.currentTimer = 0;
			}
		}
	}
	
	public SoundObject getSoundObject(String soundName) {
		return allSoundObjects.get(soundName);
	}
}
