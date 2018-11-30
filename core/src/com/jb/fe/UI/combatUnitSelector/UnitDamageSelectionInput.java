package com.jb.fe.UI.combatUnitSelector;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import com.jb.fe.components.InventoryComponent;
import com.jb.fe.components.ItemComponent;
import com.jb.fe.components.MovementStatsComponent;
import com.jb.fe.components.NameComponent;
import com.jb.fe.components.UnitStatsComponent;
import com.jb.fe.components.UIComponent.InputHandling;
import com.jb.fe.map.MapCell;
import com.jb.fe.systems.inputAndUI.UIManager;

public class UnitDamageSelectionInput implements InputHandling {

	private float inputDelay = 0.08f;
	private float currentDelay;

	private ComponentMapper<InventoryComponent> iComponentMapper = ComponentMapper.getFor(InventoryComponent.class);
	private ComponentMapper<NameComponent> nComponentMapper = ComponentMapper.getFor(NameComponent.class);
	private ComponentMapper<ItemComponent> itemComponentMapper = ComponentMapper.getFor(ItemComponent.class);
	private ComponentMapper<MovementStatsComponent> mComponentMapper = ComponentMapper
			.getFor(MovementStatsComponent.class);
	private ComponentMapper<UnitStatsComponent> uComponentMapper = ComponentMapper.getFor(UnitStatsComponent.class);

	private Array<Entity> allEnemiesThatCanBeAttacked;
	private int unitAt = 0;

	public UnitDamageSelectionInput() {
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
			
			currentDelay = 0;
		}

		if (Gdx.input.isKeyPressed(Keys.DOWN)) {

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
		if ()
	}
}
