package com.jb.fe.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Array;

public class UITextComponent implements Component{

	public Array<String> textArray;
	
	public UITextComponent() {
		textArray = new Array<String>();
	}
}
