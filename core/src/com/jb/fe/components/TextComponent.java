package com.jb.fe.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Queue;
import com.jb.fe.UI.Text.TextObject;

/*
 * Wrapper for strings that need to be drawn to the game
 */
public class TextComponent implements Component{

	public Queue<TextObject> textArray;
	public boolean isDrawing;
	
	public TextComponent() {
		textArray = new Queue<TextObject>();
		isDrawing = false;
	}
}