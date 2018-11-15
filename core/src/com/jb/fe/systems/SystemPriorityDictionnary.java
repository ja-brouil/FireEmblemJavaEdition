package com.jb.fe.systems;

public final class SystemPriorityDictionnary {

	private SystemPriorityDictionnary() {}
	
	// Change Priority Here
	public static final int InputHandle = 0;
	public static final int MapCursorOutOfbounds = 1;
	public static final int CameraUpdate = 2;
	public static final int AI_System = 5;
	public static final int TurnManager = 3;
	public static final int MapCursorInfoUpdate = 4;
	public static final int MovementUpdate = 6;
	public static final int CombatPhase = 7;
	public static final int MapRender = 8;
	public static final int TransitionRender = 9;
	public static final int GraphicsRender = 10;
}
