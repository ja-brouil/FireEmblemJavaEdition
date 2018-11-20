package com.jb.fe.UI;

import com.badlogic.gdx.graphics.Color;

public class TextObject {

	public float x, y;
	public String text;
	public boolean isEnabled;
	public float textFontSize;
	public Color textColor;
	
	public TextObject(float x, float y, String text) {
		this.x = x;
		this.y = y;
		this.text = text;
		isEnabled = true;
		textFontSize = 0.33f;
		textColor = Color.WHITE;
	}
	
	public TextObject(float x, float y, String text, float textFontSize) {
		this.x = x;
		this.y = y;
		this.text = text;
		isEnabled = true;
		this.textFontSize = textFontSize;
		textColor = Color.WHITE;
	}
	
	public TextObject() {
		this(0, 0, "PlaceHolder");
	}

}
