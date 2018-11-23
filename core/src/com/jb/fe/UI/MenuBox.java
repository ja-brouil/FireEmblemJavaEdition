package com.jb.fe.UI;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.jb.fe.components.MapCursorStateComponent;

public abstract class MenuBox {
	
	// Keep Track of the map cursor
	protected Entity mapCursor;
	protected ComponentMapper<MapCursorStateComponent> mComponentMapper = ComponentMapper.getFor(MapCursorStateComponent.class);
	
	// For engine
	protected Entity boxEntity;
	
	// Screen Position
	public static enum SCREEN_POSITION {
		BOTTOM_LEFT, BOTTOM_RIGHT, TOP_LEFT, TOP_RIGHT;
	}
	
	protected SCREEN_POSITION sPosition;
	
	// Asset Manager
	protected AssetManager assetManager;
	
	// Engine
	protected Engine engine;
	
	public MenuBox(Entity mapCursor, AssetManager assetManager, Engine engine) {
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
	
	// Override this to turn this off
	public void turnOff() {};
}
