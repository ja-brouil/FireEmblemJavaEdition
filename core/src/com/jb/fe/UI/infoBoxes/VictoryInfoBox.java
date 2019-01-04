package com.jb.fe.UI.infoBoxes;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Align;
import com.jb.fe.UI.MenuBox;
import com.jb.fe.UI.Text.TextObject;
import com.jb.fe.UI.mapcursor.MapCursor;
import com.jb.fe.UI.mapcursor.MapCursor.MAP_CURSOR_QUADRANT;
import com.jb.fe.components.NameComponent;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.components.StaticImageComponent;
import com.jb.fe.components.TextComponent;
import com.jb.fe.components.ZOrderComponent;
import com.jb.fe.level.Level;
import com.jb.fe.level.VictoryCondition.VictoryConditionType;
import com.jb.fe.systems.graphics.ZOrder;

public class VictoryInfoBox extends MenuBox {

	private PositionComponent positionComponent;
	private StaticImageComponent staticImageComponent;
	private ZOrderComponent zOrderComponent;
	
	private String victoryString;
	private TextComponent textComponent;
	
	private Level level;
	
	public VictoryInfoBox(MapCursor mapCursor, AssetManager assetManager, Engine engine, Level level) {
		super(mapCursor, assetManager, engine);
		this.level = level;
		
		boxEntity = new Entity();
		
		positionComponent = new PositionComponent(-100,-100);
		staticImageComponent = new StaticImageComponent(assetManager, "UI/victoryBox/victoryConditionBox.png");
		staticImageComponent.width = 55;
		staticImageComponent.height = 15;
		staticImageComponent.alpha = 0.8f;
		zOrderComponent = new ZOrderComponent(ZOrder.UI_LOWER_LAYER);
		
		textComponent = new TextComponent();
		textComponent.textArray.addFirst(new TextObject(positionComponent.x, positionComponent.y, victoryString));
		textComponent.isDrawing = true;
		textComponent.textArray.first().alignment = Align.center;
		textComponent.textArray.first().textFontSize = 0.2f;
		updateTextString();
		
		boxEntity.add(positionComponent);
		boxEntity.add(staticImageComponent);
		boxEntity.add(textComponent);
		boxEntity.add(zOrderComponent);
		
		engine.addEntity(boxEntity);
	}
	
	@Override
	public void update(MapCursor mapCursor) {
		if (mapCursor.getMapCursorQuandrant().equals(MAP_CURSOR_QUADRANT.TOP_LEFT)) {
			sPosition = SCREEN_POSITION.TOP_RIGHT;
			positionComponent.x = 180;
			positionComponent.y = 140;
		} else if (mapCursor.getMapCursorQuandrant().equals(MAP_CURSOR_QUADRANT.TOP_RIGHT)) {
			sPosition = SCREEN_POSITION.BOTTOM_RIGHT;
			positionComponent.x = 176;
			positionComponent.y = 5;
		} else if (mapCursor.getMapCursorQuandrant().equals(MAP_CURSOR_QUADRANT.BOTTOM_LEFT)) {
			sPosition = SCREEN_POSITION.TOP_RIGHT;
			positionComponent.x = 180;
			positionComponent.y = 140;
		} else if (mapCursor.getMapCursorQuandrant().equals(MAP_CURSOR_QUADRANT.BOTTOM_RIGHT)) {
			sPosition = SCREEN_POSITION.TOP_RIGHT;
			positionComponent.x = 180;
			positionComponent.y = 140;
		}
		textComponent.textArray.first().x = positionComponent.x + 27;
		textComponent.textArray.first().y = positionComponent.y + 10;
	}


	// TO DO : Change font size as required to fit in the box later
	public void updateTextString() {
		if (level.victoryCondition.victoryConditionType.equals(VictoryConditionType.SEIZE)) {
			victoryString = "Seize Throne";
		} else if (level.victoryCondition.victoryConditionType.equals(VictoryConditionType.ASSASSINATION)) {
			victoryString = "Assassinate " + level.victoryCondition.entityToAssassinate.getComponent(NameComponent.class).name;
		} else if (level.victoryCondition.victoryConditionType.equals(VictoryConditionType.ESCORT)) {
			victoryString = "Escort " + level.victoryCondition.unitToEscort.getComponent(NameComponent.class).name + " to " + level.victoryCondition.destinationCellToEscort.tileName;
		} else if (level.victoryCondition.victoryConditionType.equals(VictoryConditionType.ROUT)) {
			victoryString = "Eliminate all enemies.";
		} else if (level.victoryCondition.victoryConditionType.equals(VictoryConditionType.SURVIVE)) {
			victoryString = "Survive " + level.victoryCondition.numberOfTurnsToSurvive + "days | Current Day: " + level.victoryCondition.currentNumberOfTurns;
		}
		textComponent.textArray.first().text = victoryString;
	}
}
