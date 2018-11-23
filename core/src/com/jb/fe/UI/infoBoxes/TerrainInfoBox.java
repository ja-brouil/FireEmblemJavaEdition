package com.jb.fe.UI.infoBoxes;


import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Align;
import com.jb.fe.components.MapCursorStateComponent.MAP_CURSOR_QUADRANT;
import com.jb.fe.UI.MenuBox;
import com.jb.fe.UI.Text.TextObject;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.components.StaticImageComponent;
import com.jb.fe.components.TextComponent;
import com.jb.fe.components.UIComponent;
import com.jb.fe.components.UIComponent.UpdateUI;
import com.jb.fe.components.ZOrderComponent;
import com.jb.fe.systems.graphics.ZOrder;
import com.jb.fe.systems.movement.MovementUtilityCalculator;

public class TerrainInfoBox extends MenuBox {

	private TextComponent textComponent;
	private PositionComponent positionComponent;
	
	private StaticImageComponent staticImageComponent;
	private ZOrderComponent zOrderComponent;
	
	private float nameConstX, nameConstY, avdConstX, avdConstY, avdNumConstX, avdNumConstY, defConstX, defConstY, defNumConstX, defNumConstY;
	private float fontSize;

	public TerrainInfoBox(Entity mapCursor, AssetManager assetManager, Engine engine) {
		super(mapCursor, assetManager, engine);
		sPosition = SCREEN_POSITION.BOTTOM_LEFT;
		
		boxEntity = new Entity();
		positionComponent = new PositionComponent(4, 5);
		
		// Initialize constants
		fontSize = 0.2f;
		nameConstX = 22f;
		nameConstY = 31f;
		avdConstX = 17f;
		avdConstY = 17;
		avdNumConstX = 32f;
		avdNumConstY = 17f;
		defConstX = 17f;
		defConstY = 10f;
		defNumConstX = 32f;
		defNumConstY = 10f;
		
		// Text Objects -> Text
		textComponent = new TextComponent();
		textComponent.textArray.addFirst(new TextObject(positionComponent.x + nameConstX, positionComponent.y + nameConstY, "--", fontSize, Align.center));
		textComponent.textArray.addLast(new TextObject(positionComponent.x + avdConstX, positionComponent.y + avdConstY, "AVD:", fontSize, Align.center));
		textComponent.textArray.addLast(new TextObject(positionComponent.x + avdNumConstX, positionComponent.y  + avdNumConstY, "0", fontSize, Align.center));
		textComponent.textArray.addLast(new TextObject(positionComponent.x + defConstX, positionComponent.y + defConstY, "DEF:", fontSize, Align.center));
		textComponent.textArray.addLast(new TextObject(positionComponent.x + defNumConstX, positionComponent.y + defNumConstY, "0", fontSize, Align.center));
		boxEntity.add(textComponent);
		
		// Graphics
		staticImageComponent = new StaticImageComponent(assetManager, "UI/terrainInfo/terrainInfoBox.png");
		staticImageComponent.isEnabled = true;
		staticImageComponent.alpha = 0.8f;
		zOrderComponent = new ZOrderComponent(ZOrder.UI_LOWER_LAYER);
		
		// Update
		UIComponent uiComponent = new UIComponent();
		uiComponent.updateUI = new TerrainInfoBoxUpdate();
		uiComponent.updateIsEnabled = true;
		
		boxEntity.add(staticImageComponent);
		boxEntity.add(zOrderComponent);
		boxEntity.add(positionComponent);
		boxEntity.add(uiComponent);
		
		engine.addEntity(boxEntity);
	}

	private class TerrainInfoBoxUpdate implements UpdateUI {
		@Override
		public void updateUI(float delta) {
			if (mComponentMapper.get(mapCursor).mapCursorQuandrant.equals(MAP_CURSOR_QUADRANT.TOP_LEFT)) {
				sPosition = SCREEN_POSITION.BOTTOM_RIGHT;
				positionComponent.x = 185;
				positionComponent.y = 5;
			} else if (mComponentMapper.get(mapCursor).mapCursorQuandrant.equals(MAP_CURSOR_QUADRANT.TOP_RIGHT)) {
				sPosition = SCREEN_POSITION.TOP_LEFT;
				positionComponent.x = 4;
				positionComponent.y = 5;
			} else if (mComponentMapper.get(mapCursor).mapCursorQuandrant.equals(MAP_CURSOR_QUADRANT.BOTTOM_LEFT)) {
				sPosition = SCREEN_POSITION.BOTTOM_RIGHT;
				positionComponent.x = 185;
				positionComponent.y = 5;
			} else if (mComponentMapper.get(mapCursor).mapCursorQuandrant.equals(MAP_CURSOR_QUADRANT.BOTTOM_RIGHT)) {
				sPosition = SCREEN_POSITION.BOTTOM_LEFT;
				positionComponent.x = 4;
				positionComponent.y = 5;
			}
			
			// Update Text
			textComponent.textArray.get(0).text = MovementUtilityCalculator.getMapCellStatic(mapCursor).tileName;
			textComponent.textArray.get(2).text = Integer.toString((int) MovementUtilityCalculator.getMapCellStatic(mapCursor).avoidanceBonus);
			textComponent.textArray.get(4).text = Integer.toString((int) MovementUtilityCalculator.getMapCellStatic(mapCursor).defenceBonus);
			
			// Update Text Position
			textComponent.textArray.get(0).x = positionComponent.x + nameConstX;
			textComponent.textArray.get(0).y = positionComponent.y + nameConstY;
			textComponent.textArray.get(1).x = positionComponent.x + avdConstX;
			textComponent.textArray.get(1).y = positionComponent.y + avdConstY;
			textComponent.textArray.get(2).x = positionComponent.x + avdNumConstX;
			textComponent.textArray.get(2).y = positionComponent.y + avdNumConstY;
			textComponent.textArray.get(3).x = positionComponent.x + defConstX;
			textComponent.textArray.get(3).y = positionComponent.y + defConstY;
			textComponent.textArray.get(4).x = positionComponent.x + defNumConstX;
			textComponent.textArray.get(4).y = positionComponent.y + defNumConstY;
		}
	}
}
