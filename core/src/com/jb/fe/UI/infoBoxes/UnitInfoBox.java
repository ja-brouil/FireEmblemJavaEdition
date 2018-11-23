package com.jb.fe.UI.infoBoxes;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Align;
import com.jb.fe.UI.MenuBox;
import com.jb.fe.UI.Text.TextObject;
import com.jb.fe.components.IconComponent;
import com.jb.fe.components.MapCursorStateComponent.MAP_CURSOR_QUADRANT;
import com.jb.fe.components.NameComponent;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.components.StaticImageComponent;
import com.jb.fe.components.TextComponent;
import com.jb.fe.components.UIComponent;
import com.jb.fe.components.UnitStatsComponent;
import com.jb.fe.components.UIComponent.UpdateUI;
import com.jb.fe.components.ZOrderComponent;
import com.jb.fe.screens.FireEmblemGame;
import com.jb.fe.systems.graphics.ZOrder;

public class UnitInfoBox extends MenuBox {

	private Entity emptyHPBar;
	private PositionComponent emptyHPPositionComponent;
	private StaticImageComponent emptyHPStaticImage;
	private ZOrderComponent emptyHPZorder;
	
	private Entity healthyHPBar;
	private PositionComponent healthyPositionComponent;
	private StaticImageComponent healthyStaticImage;
	private ZOrderComponent healthyZOrder;
	
	private StaticImageComponent backgroundStaticImageComponent;
	private ZOrderComponent backgroundZorder;
	private UIComponent uiComponent;
	private UnitInfoBoxUpdate unitInfoBoxUpdate;
	
	private TextComponent textComponent;
	private Color tealColor;
	
	private PositionComponent positionComponent;
	
	private ComponentMapper<PositionComponent> pComponentMapper = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<NameComponent> nComponentMapper = ComponentMapper.getFor(NameComponent.class);
	private ComponentMapper<UnitStatsComponent> uComponentMapper = ComponentMapper.getFor(UnitStatsComponent.class);
	
	private float emptyHPBarX, emptyHPBarY, fullHPBarX, fullHPBarY, hpX, hpY, nameX, nameY, portraitX, portraitY;
	private PositionComponent lastPortrait;
	
	public UnitInfoBox(Entity mapCursor, AssetManager assetManager, Engine engine) {
		super(mapCursor, assetManager, engine);
		// Initial last
		lastPortrait = new PositionComponent();
		
		// Offsets
		emptyHPBarX = 45f;
		emptyHPBarY = 7f;
		fullHPBarX = 45;
		fullHPBarY = 7f;
		hpX = 65f;
		hpY = 21f;
		portraitX = 10;
		portraitY = 4;
		nameX = 65;
		nameY = 31;
		
		// Color
		tealColor = new Color(34 / 255f, 141 / 255f, 148 / 255f, 1);
		
		// Background Container
		positionComponent = new PositionComponent(-500, -500);
		backgroundStaticImageComponent = new StaticImageComponent(assetManager, "UI/unitInfo/unitInfoBox.png");
		backgroundStaticImageComponent.isEnabled = true;
		backgroundStaticImageComponent.alpha = 0.8f;
		backgroundStaticImageComponent.width = 90;
		backgroundStaticImageComponent.height = 40;
		backgroundZorder = new ZOrderComponent(ZOrder.UI_LOWER_LAYER);
		
		uiComponent = new UIComponent();
		uiComponent.updateIsEnabled = true;
		unitInfoBoxUpdate = new UnitInfoBoxUpdate();
		uiComponent.updateUI = unitInfoBoxUpdate;
		
		textComponent = new TextComponent();
		textComponent.isDrawing = true;
		textComponent.textArray.addFirst(new TextObject(0, 0, "name", 0.19f, Align.center));
		textComponent.textArray.get(0).textColor = tealColor;
		textComponent.textArray.addFirst(new TextObject(positionComponent.x, positionComponent.y, "HP: 0 / 0", 0.18f, Align.center));
		textComponent.textArray.get(0).textColor = tealColor;
		
		boxEntity.add(textComponent);
		boxEntity.add(positionComponent);
		boxEntity.add(backgroundStaticImageComponent);
		boxEntity.add(backgroundZorder);
		boxEntity.add(uiComponent);
		engine.addEntity(boxEntity);
		
		// Empty Bar
		emptyHPBar = new Entity();
		
		emptyHPPositionComponent = new PositionComponent(-100, -100);
		emptyHPStaticImage = new StaticImageComponent(assetManager, "UI/unitInfo/emptyHealthBar.png");
		emptyHPStaticImage.isEnabled = true;
		emptyHPStaticImage.width = 40;
		emptyHPStaticImage.height = 5;
		
		emptyHPZorder = new ZOrderComponent(ZOrder.UI_MIDDLE_LAYER);
		
		emptyHPBar.add(emptyHPPositionComponent);
		emptyHPBar.add(emptyHPStaticImage);
		emptyHPBar.add(emptyHPZorder);
		engine.addEntity(emptyHPBar);
		
		// Full Bar
		healthyHPBar = new Entity();
		
		healthyPositionComponent = new PositionComponent(-100, -100);
		healthyStaticImage = new StaticImageComponent(assetManager, "UI/unitInfo/fullHealthBar.png");
		healthyStaticImage.isEnabled = true;
		healthyStaticImage.width = 5;
		healthyStaticImage.height = 5;
		
		healthyZOrder = new ZOrderComponent(ZOrder.UI_TOP_LAYER);

		healthyHPBar.add(healthyPositionComponent);
		healthyHPBar.add(healthyStaticImage);
		healthyHPBar.add(healthyZOrder);
		engine.addEntity(healthyHPBar);
	}
	
	@Override
	public void turnOff() {
		unitInfoBoxUpdate.noDrawingPosition();
	}
	
	private class UnitInfoBoxUpdate implements UpdateUI {
		@Override
		public void updateUI(float delta) {
			// Do not draw anything if map cursor has no units
			if (mComponentMapper.get(mapCursor).unitSelected == null) {
				noDrawingPosition();
				return;
			}
			
			// Set Last portrait
			lastPortrait = pComponentMapper.get(mComponentMapper.get(mapCursor).unitSelected.getComponent(IconComponent.class).iconEntity);

			// Set Position
			if (mComponentMapper.get(mapCursor).mapCursorQuandrant.equals(MAP_CURSOR_QUADRANT.TOP_LEFT)) {
				sPosition = SCREEN_POSITION.BOTTOM_LEFT;
				positionComponent.x = 4;
				positionComponent.y = 4;
			} else if (mComponentMapper.get(mapCursor).mapCursorQuandrant.equals(MAP_CURSOR_QUADRANT.TOP_RIGHT)) {
				sPosition = SCREEN_POSITION.TOP_LEFT;
				positionComponent.x = 3;
				positionComponent.y = FireEmblemGame.HEIGHT - 40;
			} else if (mComponentMapper.get(mapCursor).mapCursorQuandrant.equals(MAP_CURSOR_QUADRANT.BOTTOM_LEFT)) {
				sPosition = SCREEN_POSITION.TOP_LEFT;
				positionComponent.x = 3;
				positionComponent.y = FireEmblemGame.HEIGHT - 40;
			} else if (mComponentMapper.get(mapCursor).mapCursorQuandrant.equals(MAP_CURSOR_QUADRANT.BOTTOM_RIGHT)) {
				sPosition = SCREEN_POSITION.TOP_LEFT;
				positionComponent.x = 3;
				positionComponent.y = FireEmblemGame.HEIGHT - 40;
			}
			
			// Set HP Bar Positions
			emptyHPPositionComponent.x = positionComponent.x + emptyHPBarX;
			emptyHPPositionComponent.y = positionComponent.y + emptyHPBarY;
			healthyPositionComponent.x = positionComponent.x + fullHPBarX;
			healthyPositionComponent.y = positionComponent.y + fullHPBarY;
			
			// Set Width of full HP Bar
			healthyStaticImage.width = calculateRemainingHPWidth(mComponentMapper.get(mapCursor).unitSelected);
			
			// Get Portrait of the unit to draw
			PositionComponent unitPositionComponent = pComponentMapper.get(mComponentMapper.get(mapCursor).unitSelected.getComponent(IconComponent.class).iconEntity);
			unitPositionComponent.x = positionComponent.x + portraitX;
			unitPositionComponent.y = positionComponent.y + portraitY;
			
			// Set Strings for Text
			textComponent.textArray.get(0).text = nComponentMapper.get(mComponentMapper.get(mapCursor).unitSelected).name; 
			textComponent.textArray.get(0).x = positionComponent.x + nameX;
			textComponent.textArray.get(0).y = positionComponent.y + nameY;
			
			textComponent.textArray.get(1).text = calculateHP(mComponentMapper.get(mapCursor).unitSelected);
			textComponent.textArray.get(1).x = positionComponent.x + hpX;
			textComponent.textArray.get(1).y = positionComponent.y + hpY;
		}
		
		private void noDrawingPosition() {
			positionComponent.x = -500;
			positionComponent.y = -500;
			lastPortrait.x = positionComponent.x;
			lastPortrait.y = positionComponent.y;
			emptyHPPositionComponent.x = positionComponent.x + emptyHPBarX;
			emptyHPPositionComponent.y = positionComponent.y + emptyHPBarY;
			healthyPositionComponent.x = positionComponent.x + fullHPBarX;
			healthyPositionComponent.y = positionComponent.y + fullHPBarY;
			textComponent.textArray.get(0).x = positionComponent.x + nameX;
			textComponent.textArray.get(0).y = positionComponent.y + nameY;
			textComponent.textArray.get(1).x = positionComponent.x + hpX;
			textComponent.textArray.get(1).y = positionComponent.y + hpY;
		}
		
		private String calculateHP(Entity unit) {			
			return "HP: " + uComponentMapper.get(unit).health + " / "  + uComponentMapper.get(unit).maxHealth;
		}
		
		private float calculateRemainingHPWidth(Entity unit) {
			return ((float) uComponentMapper.get(unit).health / (float) uComponentMapper.get(unit).maxHealth) * 40f;
		}
	}
}
