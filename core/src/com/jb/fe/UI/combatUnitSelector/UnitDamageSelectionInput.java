package com.jb.fe.UI.combatUnitSelector;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.utils.Array;
import com.jb.fe.components.ItemComponent;
import com.jb.fe.components.MovementStatsComponent;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.components.StaticImageComponent;
import com.jb.fe.components.UIComponent.InputHandling;
import com.jb.fe.map.MapCell;

public class UnitDamageSelectionInput implements InputHandling {

	private Entity mapCursor;
	
	private float inputDelay = 0.08f;
	private float currentDelay;

	private ComponentMapper<ItemComponent> itemComponentMapper = ComponentMapper.getFor(ItemComponent.class);
	private ComponentMapper<MovementStatsComponent> mComponentMapper = ComponentMapper
			.getFor(MovementStatsComponent.class);
	private ComponentMapper<PositionComponent> pComponentMapper = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<StaticImageComponent> sComponentMapper = ComponentMapper.getFor(StaticImageComponent.class);

	private Array<Entity> allEnemiesThatCanBeAttacked;
	private static int unitAt = 0;

	public UnitDamageSelectionInput(Entity mapCursor) {
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

			currentDelay = 0;
		}

		// Return to inventory selection
		if (Gdx.input.isKeyJustPressed(Keys.X)) {

			currentDelay = 0;
		}

	}

	// Get all Enemies that can be attacked with the current weapon that is equipped
	public void calculateEnemies(Entity unit, int attackRange) {
		ItemComponent weaponEquipped = itemComponentMapper.get(unit);
		MapCell initialTile = mComponentMapper.get(unit).currentCell;
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
		// Turn on static map cursor
		
	}
	
	public void turnOfF() {
		
	}
}
