package com.jb.fe.UI.actionMenu;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.jb.fe.components.AnimationComponent;
import com.jb.fe.components.MapCursorStateComponent;
import com.jb.fe.components.MapCursorStateComponent.MapCursorState;
import com.jb.fe.components.MovementStatsComponent;
import com.jb.fe.components.MovementStatsComponent.Unit_State;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.components.SoundComponent;
import com.jb.fe.components.StaticImageComponent;
import com.jb.fe.components.TextComponent;
import com.jb.fe.components.UIComponent;
import com.jb.fe.components.UIComponent.InputHandling;
import com.jb.fe.systems.movement.UnitMapCellUpdater;

public class ActionMenuInput implements InputHandling {

	// Timers
	private float currentDelayTime;
	private float delayTime;
	
	// UI elements
	private Entity entityToProcess;
	private Entity mapCursor;
	private Entity actionMenu;
	private Entity hand;
	
	// Map Cell Updater
	private UnitMapCellUpdater unitMapCellUpdater;
	
	// Component Mappers
	private ComponentMapper<MovementStatsComponent> mComponentMapper = ComponentMapper.getFor(MovementStatsComponent.class);
	private ComponentMapper<PositionComponent> pComponentMapper = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<StaticImageComponent> sComponentMapper = ComponentMapper.getFor(StaticImageComponent.class);
	private ComponentMapper<AnimationComponent> aComponentMapper = ComponentMapper.getFor(AnimationComponent.class);
	
	// UI Component references
	private UIComponent uiComponent;
	
	public ActionMenuInput(Entity mapcursor, Entity actionMenu, Entity hand, UnitMapCellUpdater unitMapCellUpdater, UIComponent uiComponent) {
		this.actionMenu = actionMenu;
		this.mapCursor = mapcursor;
		this.uiComponent = uiComponent;
		this.hand = hand;
		this.unitMapCellUpdater = unitMapCellUpdater;
		delayTime = 0.08f;
	}
	
	@Override
	public void handleInput() {
		sComponentMapper.get(hand).isEnabled = true;
		currentDelayTime += Gdx.graphics.getDeltaTime();
		if (currentDelayTime < delayTime) {
			return;
		}
		
		// Up
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			pComponentMapper.get(hand).y += 15;
			uiComponent.soundSystem.playSound(mapCursor.getComponent(SoundComponent.class).allSoundObjects.get("Movement"));
			currentDelayTime = 0;
		}
		
		// Down
		if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			pComponentMapper.get(hand).y -= 15;
			uiComponent.soundSystem.playSound(mapCursor.getComponent(SoundComponent.class).allSoundObjects.get("Movement"));
			currentDelayTime = 0;
		}
		
		// A Button
		if (Gdx.input.isKeyPressed(Keys.Z)) {
			// For now just accept -> Need to add other functions later
			entityToProcess = uiComponent.currentEntity;
			entityToProcess.getComponent(MovementStatsComponent.class).unit_State = Unit_State.DONE;
			
			// Map Cursor
			mapCursor.getComponent(MapCursorStateComponent.class).mapCursorState = MapCursorState.MOVEMENT_ONLY;
			aComponentMapper.get(mapCursor).currentAnimation.isDrawing = true;
			
			// Play accept sound
			uiComponent.soundSystem.playSound(mapCursor.getComponent(SoundComponent.class).allSoundObjects.get("Accept"));
			
			// This
			sComponentMapper.get(actionMenu).isEnabled = false;
			sComponentMapper.get(hand).isEnabled = false;
			actionMenu.getComponent(TextComponent.class).isDrawing = false;
			uiComponent.inputIsEnabled = false;
			uiComponent.updateIsEnabled = false;
			uiComponent.uiManager.setCurrentUI(mapCursor);
			
			currentDelayTime = 0;
		}
		
		// B Button | send unit back | set mapcursor back
		if (Gdx.input.isKeyPressed(Keys.X)) {
			// Set Unit
			entityToProcess = uiComponent.currentEntity;
			MovementStatsComponent movementStatsComponent = mComponentMapper.get(entityToProcess);
			movementStatsComponent.currentCell = movementStatsComponent.previousCell;
			movementStatsComponent.unit_State = Unit_State.CAN_DO_BOTH;
			PositionComponent positionComponent = pComponentMapper.get(entityToProcess);
			positionComponent.x = movementStatsComponent.currentCell.position.x;
			positionComponent.y = movementStatsComponent.currentCell.position.y;
			
			// Map Cursor
			mapCursor.getComponent(MapCursorStateComponent.class).mapCursorState = MapCursorState.MOVEMENT_ONLY;
			pComponentMapper.get(mapCursor).x = positionComponent.x;
			pComponentMapper.get(mapCursor).y = positionComponent.y;
			mapCursor.getComponent(UIComponent.class).inputIsEnabled = true;
			mapCursor.getComponent(UIComponent.class).updateIsEnabled = true;
			unitMapCellUpdater.updateCellInfo();
			
			// Play Back Sound
			uiComponent.soundSystem.playSound(mapCursor.getComponent(SoundComponent.class).allSoundObjects.get("Back"));
			
			// Stop Drawing
			sComponentMapper.get(actionMenu).isEnabled = false;
			aComponentMapper.get(mapCursor).currentAnimation.isDrawing = true;
			actionMenu.getComponent(TextComponent.class).isDrawing = false;
			hand.getComponent(StaticImageComponent.class).isEnabled = false;
			
			// This
			uiComponent.inputIsEnabled = false;
			uiComponent.updateIsEnabled = false;
			uiComponent.uiManager.setCurrentUI(mapCursor);
			currentDelayTime = 0;
		}
	}
	
	public void setUnitUpdateSystem(UnitMapCellUpdater unitMapCellUpdater) {
		this.unitMapCellUpdater = unitMapCellUpdater;
	}
}
