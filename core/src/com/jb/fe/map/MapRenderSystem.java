package com.jb.fe.map;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.jb.fe.level.Level;
import com.jb.fe.systems.SystemPriorityDictionnary;

public class MapRenderSystem extends EntitySystem {
	
	// Map Renderer
	private OrthogonalTiledMapRenderer mapRenderer;
	
	// Camera
	private OrthographicCamera gameCamera;
	
	// Level
	private Level currentLevel;
	
	
	public MapRenderSystem(OrthographicCamera gameCamera, SpriteBatch spriteBatch) {
		this.gameCamera = gameCamera;
		
		// Set Engine Priority
		this.priority = SystemPriorityDictionnary.MapRender;
		
		// Star Map Renderer
		mapRenderer = new OrthogonalTiledMapRenderer(null, spriteBatch);
	}
	
	public void setCurrentLevel(Level level) {
		mapRenderer.dispose();
		this.currentLevel = level;
		mapRenderer.setMap(currentLevel.levelMap);
	}
	
	public void setCamera(OrthographicCamera gameCamera) {
		this.gameCamera = gameCamera;
	}
	
	public void update(float deltaTime) {
		mapRenderer.setView(gameCamera);
		mapRenderer.render();
	}
}
