package com.jb.fe.components; 

import com.badlogic.ashley.core.Component;

public class InputComponent implements Component {

	// Key booleans
	public boolean isEnabled;
	public InputHandling inputHandling;
	
	public InputComponent() {
		isEnabled = true;
	}
	
	public interface InputHandling{
		public void handleInput();
	}
}
