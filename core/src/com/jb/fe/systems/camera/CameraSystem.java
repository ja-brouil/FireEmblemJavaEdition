package com.jb.fe.systems.camera;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jb.fe.UI.mapcursor.MapCursor;
import com.jb.fe.UI.mapcursor.MovementSelection;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.level.Level;
import com.jb.fe.map.MapCell;
import com.jb.fe.systems.SystemPriorityList;
import com.jb.fe.systems.inputAndUI.UserInterfaceManager;

public class CameraSystem extends EntitySystem {
	
	// Game Camera
	private OrthographicCamera gameCam;
	private SpriteBatch spriteBatch;
	private int xConstant = 120;
	private int yConstant = 80;
	
	// Camera Location outside access
	public static float cameraX;
	public static float cameraY;
	
	// User Interface
	private UserInterfaceManager userInterfaceManager;
	
	// Level Limits
	private int maxLevelHeight;
	private int maxLevelWidth;
	
	// Position Component Mapper
	private ComponentMapper<PositionComponent> pComponentMapper = ComponentMapper.getFor(PositionComponent.class);
	
	public CameraSystem(OrthographicCamera gameCam, SpriteBatch spriteBatch) {
		priority = SystemPriorityList.CameraUpdate;
		this.gameCam = gameCam;
		this.spriteBatch = spriteBatch;
	}
	
	public void startSystem(Level level) {
		userInterfaceManager = getEngine().getSystem(UserInterfaceManager.class);
		maxLevelHeight = level.levelMap.getProperties().get("height", Integer.class) * MapCell.CELL_SIZE;
		maxLevelWidth = level.levelMap.getProperties().get("width", Integer.class) * MapCell.CELL_SIZE;
	}
	
	@Override
	public void update(float delta) {
		if (!userInterfaceManager.currentState.getClass().equals(MapCursor.class) && !userInterfaceManager.currentState.getClass().equals(MovementSelection.class)) {
			return;
		}
		
		PositionComponent mapCursorPosition = pComponentMapper.get(userInterfaceManager.currentState.getMainEntity());
		
		// Update Camera position based on where the map cursor is
		checkCameraUpdate();
		setCameraBoundery();
		spriteBatch.setProjectionMatrix(gameCam.combined);
		gameCam.update();
		
		// Update Location
		cameraX = gameCam.position.x;
		cameraY = gameCam.position.y;
		
		// DEBUG
		if (Gdx.input.isKeyJustPressed(Keys.B)) {
			System.out.println("----------------");
			System.out.println("CAMERA POSITION: " + gameCam.position);
			System.out.println("Cam + Cursor Possiton diff: X: " + Math.abs(gameCam.position.x - mapCursorPosition.x) + " Y: " + Math.abs(gameCam.position.y - mapCursorPosition.y));
		}
		
	}
	
	private void setCameraBoundery() {
		
		if (gameCam.position.x - xConstant < 0) {
			gameCam.position.x = xConstant;
		}
		
		if (gameCam.position.x + xConstant > maxLevelWidth) {
			gameCam.position.x = maxLevelWidth - xConstant;
		}
		
		if (gameCam.position.y - yConstant < 0) {
			gameCam.position.y = yConstant;
		}
		
		if (gameCam.position.y + yConstant > maxLevelHeight) {
			gameCam.position.y = maxLevelHeight - yConstant;
		}
		
	}
	
	private void checkCameraUpdate() {
		PositionComponent mapCursorPosition = pComponentMapper.get(userInterfaceManager.currentState.getMainEntity());
		
			if (Math.abs(mapCursorPosition.x - gameCam.position.x) >= xConstant && mapCursorPosition.x - gameCam.position.x > 0) {
				gameCam.translate(MapCell.CELL_SIZE, 0);
			} 
			
			if (Math.abs(mapCursorPosition.x - gameCam.position.x) > xConstant && mapCursorPosition.x - gameCam.position.x < 0) {
				gameCam.translate(-MapCell.CELL_SIZE, 0);
			}

			if (Math.abs(mapCursorPosition.y - gameCam.position.y ) >= yConstant && mapCursorPosition.y - gameCam.position.y > 0) {
				gameCam.translate(0, MapCell.CELL_SIZE);
			}

			if (Math.abs(mapCursorPosition.y - gameCam.position.y) > yConstant && mapCursorPosition.y - gameCam.position.y < 0) {
				gameCam.translate(0, -MapCell.CELL_SIZE);
			}
	}
}
