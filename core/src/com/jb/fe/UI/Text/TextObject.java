package com.jb.fe.UI.Text;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Align;

public class TextObject {

	// Position and alignment
	public float x, y;
	public int alignment;
	
	// Font Scale & color
	public float textFontSize;
	public Color textColor;
	
	// Text to be disabled
	public String text;
	
	// Enable
	public boolean isEnabled;
	
	public TextObject(float x, float y, String text) {
		this.x = x;
		this.y = y;
		this.text = text;
		isEnabled = true;
		textFontSize = 0.33f;
		textColor = Color.WHITE;
		alignment = Align.left;
	}
	
	public TextObject(float x, float y, String text, float textFontSize) {
		this.x = x;
		this.y = y;
		this.text = text;
		isEnabled = true;
		this.textFontSize = textFontSize;
		textColor = Color.WHITE;
		alignment = Align.left;
	}
	
	public TextObject(float x, float y, String text, float textFontSize, int alignment) {
		this.x = x;
		this.y = y;
		this.text = text;
		isEnabled = true;
		this.textFontSize = textFontSize;
		textColor = Color.WHITE;
		this.alignment = alignment;
	}
	
	public TextObject() {
		this(0, 0, "PlaceHolder");
	}

}
