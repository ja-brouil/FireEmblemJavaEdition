package com.jb.fe.systems;

public final class SystemPriorityDictionnary {

	private SystemPriorityDictionnary() {}
	
	// Change Priority Here
	public static final int InputHandle = 0;
	public static final int MapCursorOutOfbounds = 1;
	public static final int CameraUpdate = 2;
	public static final int UnitUpdate = 3;
	public static final int MapCursorInfoUpdate = 4;
	public static final int MovementUpdate = 5;
	public static final int AttackPhase = 6;
	public static final int MapRender = 7;
	public static final int StaticRender = 8;
	public static final int AnimationRender = 9;
}
