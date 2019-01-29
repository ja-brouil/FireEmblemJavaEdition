package com.jb.fe.systems.graphics;

public final class ZOrder {
	
	// Do not start
	private ZOrder() {}
	
	// Add more if needed
	// Z Order 0 = first, higher number = later
	
	// Map
	public static int BACKGROUND = 0;
	public static int MIDDLE_LAYER = 1;
	public static int TOP_LAYER = 2;
	
	// User Interface
	public static int UI_LOWER_LAYER = 3;
	public static int UI_MIDDLE_LAYER = 4;
	public static int UI_TOP_LAYER = 5;
	
	// Combat Interface
	public static int COMBAT_UI = 6;
	public static int COMBAT_ACTORS = 7;
}
