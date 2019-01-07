package com.jb.fe.systems.audio;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.jb.fe.audio.SoundObject;

/*
 * Player for all the sounds
 */

public class SoundSystem extends EntitySystem {
	
	public SoundSystem() {
		setProcessing(false);
	}
	
	public void playSound(SoundObject soundObject) {
		// Check if delay is 0
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
}
