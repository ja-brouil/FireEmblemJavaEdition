package com.jb.fe.systems.inputAndUI;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.utils.Array;
import com.jb.fe.UI.MenuBox;
import com.jb.fe.UI.mapcursor.MapCursor;
import com.jb.fe.components.StaticImageComponent;
import com.jb.fe.components.TextComponent;

/**
 * Controls the hand and menu boxes
 * @author JamesBrouillet
 *
 */
public class InfoBoxUpdate {
	
	// Boxes
	private Array<MenuBox> allBattlefieldMenuBoxes;

	private ComponentMapper<StaticImageComponent> sComponentMapper = ComponentMapper.getFor(StaticImageComponent.class);
	private ComponentMapper<TextComponent> tComponentMapper = ComponentMapper.getFor(TextComponent.class);
	
	public InfoBoxUpdate() {
		allBattlefieldMenuBoxes = new Array<>();
	}
	
	public void update(MapCursor mapCursor) {
		// Update battlefield boxes
		for (MenuBox battlefieldBox : allBattlefieldMenuBoxes) {
			battlefieldBox.update(mapCursor);
		}
	}
	
	public Array<MenuBox> getAllBattleFieldMenuBoxes(){
		return allBattlefieldMenuBoxes;
	}
	
	// Turn off everything | Turn on everything
	public void turnOffBoxes() {
		for (MenuBox battlefieldBox : allBattlefieldMenuBoxes) {
			battlefieldBox.setUpdateEnabled(false);
			sComponentMapper.get(battlefieldBox.getBoxEntity()).isEnabled = false;
			tComponentMapper.get(battlefieldBox.getBoxEntity()).isDrawing = false;
			battlefieldBox.turnOff();
		}
	}
	
	public void turnOnBoxes(MapCursor mapCursor) {
		for (MenuBox battlefieldBox : allBattlefieldMenuBoxes) {
			battlefieldBox.setUpdateEnabled(true);
			sComponentMapper.get(battlefieldBox.getBoxEntity()).isEnabled = true;
			tComponentMapper.get(battlefieldBox.getBoxEntity()).isDrawing = true;
			battlefieldBox.turnOff();
			battlefieldBox.update(mapCursor);
		}
		
	}
}
