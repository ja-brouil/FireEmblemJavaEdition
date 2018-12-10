package com.jb.fe.UI.combatUnitSelector;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.utils.Array;
import com.jb.fe.UI.inventory.InventoryMenuBox;
import com.jb.fe.UI.soundTemp.UISounds;
import com.jb.fe.components.AnimationComponent;
import com.jb.fe.components.InventoryComponent;
import com.jb.fe.components.ItemComponent;
import com.jb.fe.components.MovementStatsComponent;
import com.jb.fe.components.MovementStatsComponent.Unit_State;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.components.StaticImageComponent;
import com.jb.fe.components.TextComponent;
import com.jb.fe.components.UIComponent;
import com.jb.fe.components.UIComponent.InputHandling;
import com.jb.fe.map.MapCell;
import com.jb.fe.screens.FireEmblemGame;
import com.jb.fe.systems.gamePlay.CombatSystem;
import com.jb.fe.systems.inputAndUI.UIManager;

public class UnitDamageSelectionInput implements InputHandling {

	private Entity mapCursor;
	private Entity damagePreviewBoxEntity;
	private InventoryMenuBox inventoryMenuBox;
	private UnitDamagePreviewUpdate unitDamagePreviewUpdate;

	private ComponentMapper<MovementStatsComponent> mComponentMapper = ComponentMapper
			.getFor(MovementStatsComponent.class);
	private ComponentMapper<PositionComponent> pComponentMapper = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<StaticImageComponent> sComponentMapper = ComponentMapper.getFor(StaticImageComponent.class);
	private ComponentMapper<TextComponent> tComponentMapper = ComponentMapper.getFor(TextComponent.class);
	private ComponentMapper<UIComponent> uiComponentMapper = ComponentMapper.getFor(UIComponent.class);

	private Array<Entity> allEnemiesThatCanBeAttacked;
	private Array<MapCell> redCellArray;
	private static int unitAt = 0;

	public UnitDamageSelectionInput(Entity mapCursor, Entity damagePreviewBoxEntity,
			InventoryMenuBox inventoryMenuBox) {
		this.mapCursor = mapCursor;
		this.damagePreviewBoxEntity = damagePreviewBoxEntity;
		this.inventoryMenuBox = inventoryMenuBox;
		this.unitDamagePreviewUpdate = new UnitDamagePreviewUpdate();
		allEnemiesThatCanBeAttacked = new Array<>();
		redCellArray = new Array<MapCell>();
	}

	@Override
	public void handleInput() {

		// Cycle through all the Enemies
		if ((Gdx.input.isKeyJustPressed(Keys.UP) || Gdx.input.isKeyJustPressed(Keys.LEFT))
				&& allEnemiesThatCanBeAttacked.size > 0) {
			unitAt--;
			cycleInt();
			setCursorPosition();
			setDefendingEnemy();
			if (allEnemiesThatCanBeAttacked.size != 1) {
				UIComponent.soundSystem.playSound(UISounds.movement);
			}
		}

		if ((Gdx.input.isKeyJustPressed(Keys.DOWN) || Gdx.input.isKeyJustPressed(Keys.RIGHT))
				&& allEnemiesThatCanBeAttacked.size > 0) {
			unitAt++;
			cycleInt();
			setCursorPosition();
			setDefendingEnemy();
			if (allEnemiesThatCanBeAttacked.size != 1) {
				UIComponent.soundSystem.playSound(UISounds.movement);
			}
		}

		// Proceed to damage phase
		if (Gdx.input.isKeyJustPressed(Keys.Z)) {

			if (allEnemiesThatCanBeAttacked.size <= 0) {
				UIComponent.soundSystem.playSound(UISounds.invalid);
				return;
			}

			// Sound and UI
			UIComponent.soundSystem.playSound(UISounds.accept);
			sComponentMapper.get(mapCursor).isEnabled = false;
			sComponentMapper.get(damagePreviewBoxEntity).isEnabled = true;

			// Combat Preview Numbers
			unitDamagePreviewUpdate.calculateDamage(UIManager.currentGameUnit, allEnemiesThatCanBeAttacked.get(unitAt));

			// Combat animations/Whatever cool shit you want to use here. For now, just
			// boring old numbers changing | Turn this into a system
			// later so that we can take units out when they are "dead" or if Eirika dies it
			// should be game over.
			CombatSystem.attackingUnit = UIManager.currentGameUnit;
			CombatSystem.defendingUnit = allEnemiesThatCanBeAttacked.get(unitAt);
			CombatSystem.isProcessing = true;

			// Get out of Combat preview
			turnOff();
			disableDamagePreview();

			// Unit is done
			UIManager.currentGameUnit.getComponent(MovementStatsComponent.class).unit_State = Unit_State.DONE;
			UIManager.currentGameUnit.getComponent(AnimationComponent.class).currentAnimation = UIManager.currentGameUnit.getComponent(AnimationComponent.class).allAnimationObjects.get("Idle");

			// Set Cursor back to control
			uiComponentMapper.get(damagePreviewBoxEntity).uiManager.startMapCursor();
		}

		// Return to inventory selection
		if (Gdx.input.isKeyJustPressed(Keys.X)) {
			turnOff();
			disableDamagePreview();
			setToPreviousMenu();
			UIComponent.soundSystem.playSound(UISounds.back);
		}
	}

	// Get all Enemies that can be attacked with the current weapon that is equipped
	private void calculateEnemies(Entity unit, int attackRange) {
		MapCell initialTile = mComponentMapper.get(unit).currentCell;

		for (int i = 0; i < initialTile.adjTiles.size; i++) {

			MapCell adjMapCell = initialTile.adjTiles.get(i);

			// Next Attack Range
			int nextAttackRange = attackRange - 1;
			if (nextAttackRange >= 0) {
				redCellArray.add(adjMapCell);
				if (!adjMapCell.isOccupied) {
					calculateEnemies(unit, nextAttackRange);
				} else {
					// Is the unit an enemy?
					if (!mComponentMapper.get(adjMapCell.occupyingUnit).isAlly
							&& !allEnemiesThatCanBeAttacked.contains(unit, true)) {
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

	private void setDefendingEnemy() {
		unitDamagePreviewUpdate.getCombatSystemCalculator().setUnits(UIManager.currentGameUnit,
				allEnemiesThatCanBeAttacked.get(unitAt));
		setLocationOfDamageBox(pComponentMapper.get(damagePreviewBoxEntity));
	}

	public void turnOn() {
		// Reset Stats
		allEnemiesThatCanBeAttacked.clear();
		redCellArray.clear();
		unitAt = 0;

		// Calculate all enemies
		calculateEnemies(UIManager.currentGameUnit,
				UIManager.currentGameUnit.getComponent(InventoryComponent.class).selectedItem
						.getComponent(ItemComponent.class).maxRange);
		if (allEnemiesThatCanBeAttacked.size > 0) {
			sComponentMapper.get(mapCursor).isEnabled = true;
			setDefendingEnemy();
			setCursorPosition();
			enableDamagePreview();
		}

		// Enable attack squares
		for (MapCell mapCell : redCellArray) {
			sComponentMapper.get(mapCell.redSquare).isEnabled = true;
		}
	}

	private void enableDamagePreview() {
		// Turn on static map cursor and set the position of it
		sComponentMapper.get(mapCursor).isEnabled = true;
		setCursorPosition();

		// Damage box preview enable
		sComponentMapper.get(damagePreviewBoxEntity).isEnabled = true;

		// Enable Text
		tComponentMapper.get(damagePreviewBoxEntity).isDrawing = true;
	}

	public void turnOff() {
		for (MapCell mapCell : redCellArray) {
			sComponentMapper.get(mapCell.redSquare).isEnabled = false;
		}

		// Turn off static map cursor
		sComponentMapper.get(mapCursor).isEnabled = false;

		// Turn this off
		disableDamagePreview();
	}

	private void setToPreviousMenu() {
		uiComponentMapper.get(damagePreviewBoxEntity).uiManager.setCurrentUI(inventoryMenuBox.getBoxEntity());
		inventoryMenuBox.turnOn();
	}

	private void disableDamagePreview() {
		// Turn off static map cursor
		sComponentMapper.get(mapCursor).isEnabled = false;

		// Damage box and text
		sComponentMapper.get(damagePreviewBoxEntity).isEnabled = false;
		tComponentMapper.get(damagePreviewBoxEntity).isDrawing = false;
	}

	private void setLocationOfDamageBox(PositionComponent positionComponent) {
		if (pComponentMapper.get(UIManager.currentGameUnit).x <= FireEmblemGame.WIDTH / 2) {
			positionComponent.x = FireEmblemGame.WIDTH - (sComponentMapper.get(damagePreviewBoxEntity).width + 10);
			positionComponent.y = 40;
			unitDamagePreviewUpdate.setStatisticsText(tComponentMapper.get(damagePreviewBoxEntity), true);
		} else {
			positionComponent.x = 10;
			positionComponent.y = 40;
			unitDamagePreviewUpdate.setStatisticsText(tComponentMapper.get(damagePreviewBoxEntity), false);
		}
	}
}
