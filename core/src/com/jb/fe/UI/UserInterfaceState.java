package com.jb.fe.UI;

public abstract class UserInterfaceState {
	
	public abstract void startState();
	public abstract void resetState();
	public abstract void handleInput(float delta);
	public abstract void update(float delta);
}
