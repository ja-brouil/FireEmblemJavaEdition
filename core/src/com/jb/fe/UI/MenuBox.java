package com.jb.fe.UI;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.jb.fe.UI.mapcursor.MapCursor;

public abstract class MenuBox {
	
	// Keep Track of the map cursor
	protected MapCursor mapCursor;
	
	// For engine
	protected Entity boxEntity;
	protected boolean updateIsEnabled;
	
	// Screen Position
	public static enum SCREEN_POSITION {
		BOTTOM_LEFT, BOTTOM_RIGHT, TOP_LEFT, TOP_RIGHT;
	}
	
	protected SCREEN_POSITION sPosition;
	
	// Asset Manager
	protected AssetManager assetManager;
	
	// Engine
	protected Engine engine;
	
	public MenuBox(MapCursor mapCursor, AssetManager assetManager, Engine engine) {
		this.mapCursor = mapCursor;
		this.assetManager = assetManager;
		this.engine = engine;
		
		boxEntity = new Entity();
	}
	
	public MenuBox(AssetManager assetManager, Engine engine) {
		this.assetManager = assetManager;
		this.engine = engine;
		
		boxEntity = new Entity();
	}
	
	public Entity getBoxEntity() {
		return boxEntity;
	}
	
	public abstract void update(MapCursor mapCursor);
	public void turnOff() {};
	
	public void setUpdateEnabled(boolean updateIsEnabled) {
		this.updateIsEnabled = updateIsEnabled;
	}
	
	public boolean isUpdatedEnabled() {
		return updateIsEnabled;
	}
}
