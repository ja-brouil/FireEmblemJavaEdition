package com.jb.fe.systems;

public final class SystemPriorityDictionnary {

	private SystemPriorityDictionnary() {}
	
	// Change Priority Here
	public static final int HandleInputAndUI = 0;
	public static final int MapCursorOutOfbounds = 1;
	public static final int CameraUpdate = 2;
	public static final int AI_System = 6;
	public static final int TurnManager = 3;
	public static final int MapCursorInfoUpdate = 4;
	public static final int InfoBoxUpdate = 5;
	public static final int MovementUpdate = 7;
	public static final int CombatPhase = 8;
	public static final int MapRender = 9;
	public static final int TransitionRender = 10;
	public static final int GraphicsRender = 11;
	public static final int TextRenderer = 12;
}
