package com.jb.fe.UI.mapcursor;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.jb.fe.UI.UserInterfaceState;
import com.jb.fe.UI.factories.UIFactory;
import com.jb.fe.components.AnimationComponent;
import com.jb.fe.components.ItemComponent;
import com.jb.fe.components.MovementStatsComponent.Unit_State;
import com.jb.fe.components.NameComponent;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.components.SoundComponent;
import com.jb.fe.level.Level;
import com.jb.fe.map.MapCell;
import com.jb.fe.screens.FireEmblemGame;
import com.jb.fe.systems.audio.SoundSystem;
import com.jb.fe.systems.inputAndUI.InfoBoxUpdate;
import com.jb.fe.systems.inputAndUI.UserInterfaceManager;

public class MapCursor extends UserInterfaceState {
	
	// Entity for engine
	private Entity mapCursor; 
	private InfoBoxUpdate infoBoxUpdate;
	
	// MapCursor Quadrant
	public static enum MAP_CURSOR_QUADRANT {
		TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT;
	}
	private MAP_CURSOR_QUADRANT mapCursorQuandrant;
	private MapCell currentMapCell;
	
	private Level level;
	
	private float timerDelay;
	private float currentDelay;
	
	public MapCursor(AssetManager assetManager, Level level, Engine engine, SoundSystem soundSystem, UserInterfaceManager userInterfaceManager, InfoBoxUpdate infoBoxUpdate) {
		super(assetManager, soundSystem, userInterfaceManager);
		mapCursorQuandrant = MAP_CURSOR_QUADRANT.TOP_LEFT;
		mapCursor = UIFactory.createMapCursor(level, assetManager);
		engine.addEntity(mapCursor);
		
		this.infoBoxUpdate = infoBoxUpdate;
		this.level = level;
		timerDelay = 0.08f;
		currentDelay = 0;
	}

	@Override
	public void startState() {
		UserInterfaceManager.unitSelected = null;
		checkUnit();
		mapCursorChecks();
		infoBoxUpdate.update(this);
		infoBoxUpdate.turnOnBoxes();
	}

	@Override
	public void resetState() {
		animationComponentMapper.get(mapCursor).currentAnimation.isDrawing = false;
		staticImageComponentMapper.get(mapCursor).isEnabled = false;
		infoBoxUpdate.turnOffBoxes();
	}
	
	@Override
	public void nextState() {
		// Do nothing if there is no unit selected --> Switch to open menu later
		if (UserInterfaceManager.unitSelected == null) {;
			return;
		}

		// If Unit cannot do anything anymore, do nothing
		if (mStatComponentMapper.get(UserInterfaceManager.unitSelected).unit_State.equals(Unit_State.DONE)) {
			return;
		}

		// Proceed to the next state
		soundSystem.playSound(mapCursor.getComponent(SoundComponent.class).allSoundObjects.get("Select Unit"));
		currentDelay = 0;
		userInterfaceManager.setStates(userInterfaceManager.allUserInterfaceStates.get("MapCursor"), userInterfaceManager.allUserInterfaceStates.get("MovementSelection"));
	}

	@Override
	public void handleInput(float delta) {
		PositionComponent positionComponent = pComponentMapper.get(mapCursor);
		currentDelay += delta;
		
		// Debug : Press B to display info of unit
		if (Gdx.input.isKeyJustPressed(Keys.B)) {
			System.out.println("------------------------------------");
			System.out.println("Cursor Info: " + "\n"
					+ "X: " + positionComponent.x + "\n"
					+ "Y: " + positionComponent.y + "\n"
					+ "X Coordinate: " + positionComponent.x / MapCell.CELL_SIZE + "\n"
					+ "Y Coordinate: " + positionComponent.y / MapCell.CELL_SIZE);
			if (UserInterfaceManager.unitSelected != null) {
				System.out.println("------------------------------------");
				System.out.println("Occupying Unit: " + nComponentMapper.get(UserInterfaceManager.unitSelected).name);
				System.out.println("------------------------------------");
				System.out.println("Unit Stats: " + uComponentMapper.get(UserInterfaceManager.unitSelected).toString());
				System.out.println("------------------------------------");
				System.out.println("Current Unit equipped weapon: \n" + iComponentMapper.get(UserInterfaceManager.unitSelected).selectedItem.getComponent(NameComponent.class).name + iComponentMapper.get(UserInterfaceManager.unitSelected).selectedItem.getComponent(ItemComponent.class).toString());
			}
		}
		// Debug
		
		// Prevent too fast keystrokes
		if (currentDelay <= timerDelay) {
			return;
		}
		
		// Up
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			positionComponent.y += MapCell.CELL_SIZE;
			currentDelay = 0;
		}

		// Down
		else if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			positionComponent.y -= MapCell.CELL_SIZE;
			currentDelay = 0;
		}

		// Left
		else if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			positionComponent.x -= MapCell.CELL_SIZE;
			currentDelay = 0;
		}

		// Right
		else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			positionComponent.x += MapCell.CELL_SIZE;
			currentDelay = 0;
		}
		
		// Sound Played only once and prevent out of bounds
		if ((Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.DOWN)
				|| Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.RIGHT)) && preventOutOfBounds()) {
			soundSystem.playSound(mapCursor.getComponent(SoundComponent.class).allSoundObjects.get("Movement"));
			preventOutOfBounds();
			mapCursorChecks();
			return;
		}
		
		// A
		if (Gdx.input.isKeyJustPressed(Keys.Z)) {
			nextState();
		}
	}

	public MAP_CURSOR_QUADRANT getMapCursorQuandrant() {
		return mapCursorQuandrant;
	}
	
	public Entity getMapCursorEntity() {
		return mapCursor;
	}
	
	// Utility Functions
	private boolean preventOutOfBounds() {
		PositionComponent positionComponent = pComponentMapper.get(mapCursor);
		AnimationComponent animationComponent = animationComponentMapper.get(mapCursor);
		
		if (positionComponent.x < 0) {
			positionComponent.x = 0;
			return false;
		}
		
		if (positionComponent.x > (level.mapWidthLimit * MapCell.CELL_SIZE) - animationComponent.currentAnimation.width) {
			positionComponent.x = (level.mapWidthLimit * MapCell.CELL_SIZE) - animationComponent.currentAnimation.width;
			return false;
		}
		
		if (positionComponent.y < 0) {
			positionComponent.y = 0;
			return false;
		}
		
		if (positionComponent.y > (level.mapHeightLimit * MapCell.CELL_SIZE) - animationComponent.currentAnimation.height) {
			positionComponent.y = (level.mapHeightLimit * MapCell.CELL_SIZE) - animationComponent.currentAnimation.height;
			return false;
		}
		
		return true;
	}
	
	// Call this anytime the cursor moves
	private void mapCursorChecks() {
		quandrantCheck();
		checkUnit();
		infoBoxUpdate.update(this);
	}
	
	private void quandrantCheck() {
		// Get Quadrant for info box movement
		if (pComponentMapper.get(mapCursor).x >= (FireEmblemGame.WIDTH / 2)) {
			if (pComponentMapper.get(mapCursor).y >= (FireEmblemGame.HEIGHT / 2)) {
				mapCursorQuandrant = MAP_CURSOR_QUADRANT.TOP_RIGHT;
			} else {
				mapCursorQuandrant = MAP_CURSOR_QUADRANT.BOTTOM_RIGHT;
			}
		} else {
			if (pComponentMapper.get(mapCursor).y < (FireEmblemGame.HEIGHT / 2)) {
				mapCursorQuandrant = MAP_CURSOR_QUADRANT.BOTTOM_LEFT;
			} else {
				mapCursorQuandrant = MAP_CURSOR_QUADRANT.TOP_LEFT;
			}
		}
	}
	
	private void checkUnit() {
		// Reset Animations
		animationComponentMapper.get(mapCursor).currentAnimation.isDrawing = true;
		staticImageComponentMapper.get(mapCursor).isEnabled = false;
		for (Entity allyUnit : level.allAllies) {
			AnimationComponent animationComponent = animationComponentMapper.get(allyUnit);
			animationComponent.currentAnimation = animationComponent.allAnimationObjects.get("Idle");
		}
		
		// Get current unit on cell we are on
		PositionComponent positionComponent = pComponentMapper.get(mapCursor);
		currentMapCell = level.allLevelMapCells[(int) positionComponent.x / MapCell.CELL_SIZE][(int) positionComponent.y / MapCell.CELL_SIZE];
		if (currentMapCell.isOccupied) {
			if (mStatComponentMapper.get(currentMapCell.occupyingUnit).isAlly && mStatComponentMapper.get(currentMapCell.occupyingUnit).unit_State != Unit_State.DONE) {
				animationComponentMapper.get(currentMapCell.occupyingUnit).currentAnimation = animationComponentMapper.get(currentMapCell.occupyingUnit).allAnimationObjects.get("Hovering");
				animationComponentMapper.get(mapCursor).currentAnimation.isDrawing = false;
				staticImageComponentMapper.get(mapCursor).isEnabled = true;
			}
			UserInterfaceManager.unitSelected = currentMapCell.occupyingUnit;
		} else {
			animationComponentMapper.get(mapCursor).currentAnimation.isDrawing = true;
			staticImageComponentMapper.get(mapCursor).isEnabled = false;
			UserInterfaceManager.unitSelected = null;
		}
	}
	
	public InfoBoxUpdate getInfoBoxUpdate() {
		return infoBoxUpdate;
	}
	
	@Override
	public Entity getMainEntity() {
		return mapCursor;
	}
}
