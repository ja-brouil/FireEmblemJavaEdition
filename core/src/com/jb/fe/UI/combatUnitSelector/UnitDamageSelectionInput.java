package com.jb.fe.UI.combatUnitSelector;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.utils.Array;
import com.jb.fe.UI.inventory.InventoryMenuBox;
import com.jb.fe.UI.soundTemp.UISounds;
import com.jb.fe.components.ItemComponent;
import com.jb.fe.components.MovementStatsComponent;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.components.StaticImageComponent;
import com.jb.fe.components.TextComponent;
import com.jb.fe.components.UIComponent;
import com.jb.fe.components.UIComponent.InputHandling;
import com.jb.fe.map.MapCell;
import com.jb.fe.screens.FireEmblemGame;
import com.jb.fe.systems.inputAndUI.UIManager;

public class UnitDamageSelectionInput implements InputHandling {

	private Entity mapCursor;
	private Entity damagePreviewBoxEntity;
	private InventoryMenuBox inventoryMenuBox;
	
	private float inputDelay = 0.08f;
	private float currentDelay;

	private ComponentMapper<ItemComponent> itemComponentMapper = ComponentMapper.getFor(ItemComponent.class);
	private ComponentMapper<MovementStatsComponent> mComponentMapper = ComponentMapper
			.getFor(MovementStatsComponent.class);
	private ComponentMapper<PositionComponent> pComponentMapper = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<StaticImageComponent> sComponentMapper = ComponentMapper.getFor(StaticImageComponent.class);
	private ComponentMapper<TextComponent> tComponentMapper = ComponentMapper.getFor(TextComponent.class);
	private ComponentMapper<UIComponent> uiComponentMapper = ComponentMapper.getFor(UIComponent.class);

	private Array<Entity> allEnemiesThatCanBeAttacked;
	private static int unitAt = 0;

	public UnitDamageSelectionInput(Entity mapCursor, Entity damagePreviewBoxEntity, InventoryMenuBox inventoryMenuBox) {
		this.mapCursor = mapCursor;
		this.damagePreviewBoxEntity = damagePreviewBoxEntity;
		this.inventoryMenuBox = inventoryMenuBox;
		allEnemiesThatCanBeAttacked = new Array<>();
	}

	@Override
	public void handleInput() {

		currentDelay += Gdx.graphics.getDeltaTime();
		if (currentDelay <= inputDelay) {
			return;
		}

		// Cycle through all the Enemies
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			unitAt--;
			cycleInt();
			setCursorPosition();
			currentDelay = 0;
		}

		if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			unitAt++;
			setCursorPosition();
			currentDelay = 0;
		}

		// Proceed to damage phase
		if (Gdx.input.isKeyJustPressed(Keys.Z)) {
			// Do the combat stuff here
			System.out.println("COMBAT STARTED");
			currentDelay = 0;
		}

		// Return to inventory selection
		if (Gdx.input.isKeyJustPressed(Keys.X)) {
			turnOff();
			UIComponent.soundSystem.playSound(UISounds.back);
			currentDelay = 0;
		}

	}

	// Get all Enemies that can be attacked with the current weapon that is equipped
	public void calculateEnemies(Entity unit, int attackRange) {
		ItemComponent weaponEquipped = itemComponentMapper.get(unit);
		MapCell initialTile = mComponentMapper.get(unit).currentCell;
		allEnemiesThatCanBeAttacked.clear();
		for (int i = 0; i < initialTile.adjTiles.size; i++) {

			MapCell adjMapCell = initialTile.adjTiles.get(i);

			// Next Attack Range
			int nextAttackRange = weaponEquipped.maxRange - 1;

			if (nextAttackRange >= 0) {
				if (!adjMapCell.isOccupied) {
					calculateEnemies(unit, nextAttackRange);
				} else {
					// Is the unit an enemy?
					if (!mComponentMapper.get(adjMapCell.occupyingUnit).isAlly) {
						allEnemiesThatCanBeAttacked.add(adjMapCell.occupyingUnit);
					}
				}
			}
		}
	}
	
	private void cycleInt() {
		if (unitAt < 0) {
			unitAt = allEnemiesThatCanBeAttacked.size - 1;
		}
		
		if (unitAt > allEnemiesThatCanBeAttacked.size - 1) {
			unitAt = 0;
		}
	}
	
	private void setCursorPosition() {
		// Move Cursor to unit in the list
		pComponentMapper.get(mapCursor).x = pComponentMapper.get(allEnemiesThatCanBeAttacked.get(unitAt)).x;
		pComponentMapper.get(mapCursor).y = pComponentMapper.get(allEnemiesThatCanBeAttacked.get(unitAt)).y;
	}
	
	public void turnOn() {
		// Turn on static map cursor and set the position of it
		sComponentMapper.get(mapCursor).isEnabled = true;
		setCursorPosition();
		
		// Damage box preview enable
		sComponentMapper.get(damagePreviewBoxEntity).isEnabled = true;
		setLocationOfDamageBox(pComponentMapper.get(damagePreviewBoxEntity));
		
		// Enable Text
		tComponentMapper.get(damagePreviewBoxEntity).isDrawing = true;
	}
	
	public void turnOff() {
		// Turn off static map cursor
		sComponentMapper.get(mapCursor).isEnabled = false;
		
		// Damage box and text
		sComponentMapper.get(damagePreviewBoxEntity).isEnabled = false;
		tComponentMapper.get(damagePreviewBoxEntity).isDrawing = false;
		
		// Set Previous menu
		uiComponentMapper.get(damagePreviewBoxEntity).uiManager.setCurrentUI(inventoryMenuBox.getBoxEntity());
		inventoryMenuBox.turnOn();
	}
	
	private void setLocationOfDamageBox(PositionComponent positionComponent) {
		if (pComponentMapper.get(UIManager.currentGameUnit).x <= FireEmblemGame.WIDTH / 2) {
			positionComponent.x = 10;
		} else {
			positionComponent.x = FireEmblemGame.WIDTH - (sComponentMapper.get(damagePreviewBoxEntity).width + 10);
		}
	}
}
