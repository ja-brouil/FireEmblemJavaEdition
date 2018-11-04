package com.jb.fe.components;

import java.util.HashMap;

import com.badlogic.ashley.core.Component;
import com.jb.fe.audio.SoundObject;

public class SoundComponent implements Component{
	
	public HashMap<String, SoundObject> allSoundObjects;
	
	public SoundComponent() {
		allSoundObjects = new HashMap<>();
	}
}
