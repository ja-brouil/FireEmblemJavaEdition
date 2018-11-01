package com.jb.fe.audio;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;

/*
 * Contains music with various variables for music controls
 */

public class MusicObject implements Component{
	
	private Music song;
	public float volume;
	
	public MusicObject(String fileLocation, AssetManager assetManager) {
		assetManager.load(fileLocation, Music.class);
		assetManager.finishLoading();
		song = assetManager.get(fileLocation, Music.class);
		volume = 1f;
	}
	
	public Music getSong() {
		return song;
	}
	
}