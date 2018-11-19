package com.jb.fe.UI;

public class TextObject {

	public float x, y;
	public String text;
	public boolean isEnabled;
	
	public TextObject(float x, float y, String text) {
		this.x = x;
		this.y = y;
		this.text = text;
		isEnabled = true;
	}
	
	public TextObject() {
		this(0, 0, "PlaceHolder");
	}

}
