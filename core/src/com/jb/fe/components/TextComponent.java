package com.jb.fe.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.jb.fe.UI.TextObject;

/*
 * Wrapper for strings that need to be drawn to the game
 */
public class TextComponent implements Component{

	public Array<TextObject> textArray;
	public boolean isDrawing;
	public float textFontSize;
	public Color textColor;
	
	public TextComponent() {
		textArray = new Array<TextObject>();
		isDrawing = false;
		textFontSize = 0.33f;
		textColor = Color.WHITE;
	}
}