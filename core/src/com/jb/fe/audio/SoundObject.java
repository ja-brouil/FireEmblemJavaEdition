package com.jb.fe.audio;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;

/*
 * Represents a Sound Object
 */

public class SoundObject{
	
	// Sound byte
	private Sound sound;
	public float volume;
	public float delayTimer;
	public float currentTimer;
	
	public SoundObject(String fileLocation, AssetManager assetManager) {
		assetManager.load(fileLocation, Sound.class);
		assetManager.finishLoading();
		sound = assetManager.get(fileLocation, Sound.class);
		volume = 1f;
		delayTimer = 0;
		currentTimer = 0;
	}
	
	public Sound getSound() {
		return sound;
	}
	
	public float getVolume() {
		return this.volume;
	}
	
	public void setVolume(float volume) {
		this.volume = volume;
	}
	
}
