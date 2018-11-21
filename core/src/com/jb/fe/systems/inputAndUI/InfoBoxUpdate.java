package com.jb.fe.systems.inputAndUI;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.utils.Array;
import com.jb.fe.UI.infoBoxes.MenuBox;
import com.jb.fe.components.MapCursorStateComponent;
import com.jb.fe.components.MapCursorStateComponent.MapCursorState;
import com.jb.fe.components.StaticImageComponent;
import com.jb.fe.components.TextComponent;
import com.jb.fe.components.UIComponent;
import com.jb.fe.systems.SystemPriorityDictionnary;

/**
 * Controls the hand and menu boxes
 * @author JamesBrouillet
 *
 */
public class InfoBoxUpdate extends EntitySystem{

	// UI elements
	private Entity mapCursor;
	
	// Boxes
	private Array<MenuBox> allBattlefieldMenuBoxes;
	
	private ComponentMapper<UIComponent> uiComponentMapper = ComponentMapper.getFor(UIComponent.class);
	private ComponentMapper<StaticImageComponent> sComponentMapper = ComponentMapper.getFor(StaticImageComponent.class);
	private ComponentMapper<TextComponent> tComponentMapper = ComponentMapper.getFor(TextComponent.class);
	private ComponentMapper<MapCursorStateComponent> mComponentMapper = ComponentMapper.getFor(MapCursorStateComponent.class);
	
	public InfoBoxUpdate() {
		allBattlefieldMenuBoxes = new Array<>();
		priority = SystemPriorityDictionnary.InfoBoxUpdate;
	}
	
	@Override
	public void update(float delta) {
		// If Map Cursor is disabled, turn off UI
		if (!mComponentMapper.get(mapCursor).mapCursorState.equals(MapCursorState.MOVEMENT_ONLY)) {
			turnOffBoxes();
			return;
		} else {
			turnOnBoxes();
		}
		
		// Update battlefield boxes
		for (MenuBox battlefieldBox : allBattlefieldMenuBoxes) {
			uiComponentMapper.get(battlefieldBox.getBoxEntity()).updateUI.updateUI(delta);
		}
	}
	
	public Array<MenuBox> getAllBattleFieldMenuBoxes(){
		return allBattlefieldMenuBoxes;
	}
	
	// Turn off everything | Turn on everything
	public void turnOffBoxes() {
		for (MenuBox battlefieldBox : allBattlefieldMenuBoxes) {
			uiComponentMapper.get(battlefieldBox.getBoxEntity()).updateIsEnabled = false;
			sComponentMapper.get(battlefieldBox.getBoxEntity()).isEnabled = false;
			tComponentMapper.get(battlefieldBox.getBoxEntity()).isDrawing = false;
			battlefieldBox.turnOff();
		}
	}
	
	public void turnOnBoxes() {
		for (MenuBox battlefieldBox : allBattlefieldMenuBoxes) {
			uiComponentMapper.get(battlefieldBox.getBoxEntity()).updateIsEnabled = true;
			sComponentMapper.get(battlefieldBox.getBoxEntity()).isEnabled = true;
			tComponentMapper.get(battlefieldBox.getBoxEntity()).isDrawing = true;
			battlefieldBox.turnOff();
		}
	}
	
	public void setMapCursor(Entity mapCursor) {
		this.mapCursor = mapCursor;
	}
}
