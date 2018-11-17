package com.jb.fe.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Array;

/*
 * Wrapper for strings that need to be drawn to the game
 */
public class TextComponent implements Component{

	public boolean isDrawing;
	public Array<String> textArray;
	
	public TextComponent() {
		textArray = new Array<String>();
		isDrawing = false;
	}
}
