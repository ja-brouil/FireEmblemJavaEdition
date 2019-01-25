package com.jb.fe.UI.combatUnitSelector;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;
import com.jb.fe.UI.UserInterfaceState;
import com.jb.fe.UI.factories.UIFactory;
import com.jb.fe.UI.soundTemp.UISounds;
import com.jb.fe.components.MovementStatsComponent.Unit_State;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.components.TextComponent;
import com.jb.fe.map.MapCell;
import com.jb.fe.screens.FireEmblemGame;
import com.jb.fe.systems.audio.SoundSystem;
import com.jb.fe.systems.camera.CameraSystem;
import com.jb.fe.systems.gamePlay.CombatSystem;
import com.jb.fe.systems.gamePlay.CombatSystemCalculator;
import com.jb.fe.systems.inputAndUI.UserInterfaceManager;

/**
 * Displays damage preview
 * 
 * @author jamesbrouillet
 */

public class UnitDamageMenuState extends UserInterfaceState {

	// Entities
	private Entity unitDamageSelectorMenuBox;
	private Entity mapCursor;
	
	// Arrays for enemy calculations
	private Array<Entity> allEntitiesThatCanBeActedUpon;
	private Array<MapCell> allRedCells;
	private int unitSelection;
	
	// Combat System Calculator
	private CombatSystemCalculator combatSystemCalculator;

	public UnitDamageMenuState(AssetManager assetManager, SoundSystem soundSystem,
			UserInterfaceManager userInterfaceManager, CombatSystemCalculator combatSystemCalculator, Engine engine, Entity mapCursor) {
		super(assetManager, soundSystem, userInterfaceManager);
		this.combatSystemCalculator = combatSystemCalculator;
		this.mapCursor = mapCursor;
		
		unitDamageSelectorMenuBox = UIFactory.createUnitDamagePreviewEntity(assetManager);
		engine.addEntity(unitDamageSelectorMenuBox);
		
		allEntitiesThatCanBeActedUpon = new Array<Entity>();
		allRedCells = new Array<MapCell>();
		unitSelection = 0;
	}

	@Override
	public void startState() {
		// Turn boxes
		staticImageComponentMapper.get(unitDamageSelectorMenuBox).isEnabled = false;
		staticImageComponentMapper.get(mapCursor).isEnabled = false;
		tComponentMapper.get(unitDamageSelectorMenuBox).isDrawing = false;
		
		// Reset Stats
		allEntitiesThatCanBeActedUpon.clear();
		allRedCells.clear();
		unitSelection = 0;
		
		// Calculate Entities
		calculateEntities();
	}

	@Override
	public void resetState() {
		// Turn off
		staticImageComponentMapper.get(unitDamageSelectorMenuBox).isEnabled = false;
		staticImageComponentMapper.get(mapCursor).isEnabled = false;
		tComponentMapper.get(unitDamageSelectorMenuBox).isDrawing = false;
		
		// Turn off red squares
		for (MapCell mapCell : allRedCells) {
			staticImageComponentMapper.get(mapCell.redSquare).isEnabled = false;
		}
		
		// Remove Icons
		for (Entity entityThatCanbeActedOn : allEntitiesThatCanBeActedUpon) {
			staticImageComponentMapper.get(iComponentMapper.get(entityThatCanbeActedOn).selectedItem).isEnabled = false;
		}
	}

	@Override
	public void nextState() {
		userInterfaceManager.setStates(this, userInterfaceManager.allUserInterfaceStates.get("MapCursor"));
	}

	@Override
	public void handleInput(float delta) {
		
		// Cycle through all the enemies
		if ((Gdx.input.isKeyJustPressed(Keys.UP) || Gdx.input.isKeyJustPressed(Keys.LEFT)) && allEntitiesThatCanBeActedUpon.size > 0) {
			staticImageComponentMapper.get(iComponentMapper.get(allEntitiesThatCanBeActedUpon.get(unitSelection)).selectedItem).isEnabled = false;
			unitSelection--;
			cycleInt();
			setCursorPosition();
			setDefendingUnit();
			return;
		}  else if ((Gdx.input.isKeyJustPressed(Keys.DOWN) || Gdx.input.isKeyJustPressed(Keys.RIGHT)) && allEntitiesThatCanBeActedUpon.size > 0) {
			staticImageComponentMapper.get(iComponentMapper.get(allEntitiesThatCanBeActedUpon.get(unitSelection)).selectedItem).isEnabled = false;
			unitSelection++;
			cycleInt();
			setCursorPosition();
			setDefendingUnit();
			return;
		}
		
		// Accept the combat selection
		if (Gdx.input.isKeyJustPressed(Keys.Z)) {
			if (allEntitiesThatCanBeActedUpon.size <= 0) {
				soundSystem.playSound(UISounds.invalid);
				return;
			}
			// Combat animations/Whatever cool shit you want to use here. For now, just
			// boring old numbers changing | Turn this into a system
			// later so that we can take units out when they are "dead" or if Eirika dies it
			// should be game over.
			CombatSystem.attackingUnit = UserInterfaceManager.unitSelected;
			CombatSystem.defendingUnit = allEntitiesThatCanBeActedUpon.get(unitSelection);
			CombatSystem.isProcessing = true;
			
			// Unit is done
			mStatComponentMapper.get(UserInterfaceManager.unitSelected).unit_State = Unit_State.DONE;
			animationComponentMapper.get(UserInterfaceManager.unitSelected).currentAnimation = animationComponentMapper.get(UserInterfaceManager.unitSelected).allAnimationObjects.get("Idle");
			nextState();
			return;
		}
		
		if (Gdx.input.isKeyJustPressed(Keys.X)) {
			// Back to inventory menu
			soundSystem.playSound(UISounds.back);
			userInterfaceManager.setStates(this, userInterfaceManager.allUserInterfaceStates.get("InventoryMenu"));
		}
		
	}

	// Utilities
	/* Order of the Array
	 *  0 Defending Unit Item Equipped
		1 Defending Unit Name
		2 Atk Crit
		3 Def Crit
		4 Atk Hit
		5 Def Hit
		6 Atk Might
		7 Def Might
		8 Atk HP
		9 Def HP
		10 Attacking Unit Name
	 * 
	 */
	private void setText(boolean isRight) {
		int xOffset = 0;
		if (isRight) {
			xOffset = (int) (FireEmblemGame.WIDTH - 80 + (CameraSystem.cameraX - CameraSystem.xConstant));
		} else {
			xOffset = (int) (10 + (CameraSystem.cameraX - CameraSystem.xConstant));
		}
		int yOffset = (int) CameraSystem.cameraY - CameraSystem.yConstant;

		TextComponent textComponent = tComponentMapper.get(unitDamageSelectorMenuBox);
		textComponent.isDrawing = true;
		
		// Defending Unit Item Equipped
		textComponent.textArray.get(0).text = nComponentMapper
				.get(combatSystemCalculator.getDefendingInventory().selectedItem).name;
		textComponent.textArray.get(0).x = 5 + xOffset;
		textComponent.textArray.get(0).y = 53 + yOffset;

		// Defending Unit name
		textComponent.textArray.get(1).text = nComponentMapper.get(combatSystemCalculator.getDefendingUnit()).name;
		textComponent.textArray.get(1).x = 5 + xOffset;
		textComponent.textArray.get(1).y = 63 + yOffset;

		// Attack Crit
		textComponent.textArray.get(2).text = nComponentMapper
				.get(combatSystemCalculator.getDefendingInventory().selectedItem).name;
		textComponent.textArray.get(2).x = 70 - 10 + xOffset;
		textComponent.textArray.get(2).y = 30 + yOffset;

		// Attacking Numbers First
		// Atk Crit
		textComponent.textArray.get(2).text = Integer.toString(combatSystemCalculator.calculateCritChanceNumber());
		textComponent.textArray.get(2).x = 70 - 19 + xOffset;
		textComponent.textArray.get(2).y = 81 + yOffset;

		// Atk Hit
		textComponent.textArray.get(4).text = Integer.toString(combatSystemCalculator.calculateHitChanceNumber());
		textComponent.textArray.get(4).x = 70 - 19 + xOffset;
		textComponent.textArray.get(4).y = 94 + yOffset;

		// Atk Might
		textComponent.textArray.get(6).text = Integer.toString(combatSystemCalculator.calculateDamagePreview());
		textComponent.textArray.get(6).x = 70 - 19 + xOffset;
		textComponent.textArray.get(6).y = 107 + yOffset;

		// Atk HP
		textComponent.textArray.get(8).text = Integer.toString(combatSystemCalculator.getAttackingUnitStats().health);
		textComponent.textArray.get(8).x = 70 - 19 + xOffset;
		textComponent.textArray.get(8).y = 120 + yOffset;

		// Attack Unit Name
		textComponent.textArray.get(10).text = nComponentMapper.get(combatSystemCalculator.getAttackingUnit()).name;
		textComponent.textArray.get(10).x = 70 - 40 + xOffset;
		textComponent.textArray.get(10).y = 134 + yOffset;

		// Attacking unit damage
		CombatSystemCalculator.AttackingDamage = combatSystemCalculator.calculateDamage();

		// Defending Numbers
		// Swap Units
		Entity swapEntity = combatSystemCalculator.getDefendingUnit();
		combatSystemCalculator.setUnits(swapEntity, UserInterfaceManager.unitSelected);

		// Def Crit
		textComponent.textArray.get(3).text = Integer.toString(combatSystemCalculator.calculateCritChanceNumber());
		textComponent.textArray.get(3).x = xOffset + 8;
		textComponent.textArray.get(3).y = 81 + yOffset;

		// Def Hit
		textComponent.textArray.get(5).text = Integer.toString(combatSystemCalculator.calculateHitChanceNumber());
		textComponent.textArray.get(5).x = xOffset + 8;
		textComponent.textArray.get(5).y = 94 + yOffset;

		// Def Might
		textComponent.textArray.get(7).text = Integer.toString(combatSystemCalculator.calculateDamagePreview());
		textComponent.textArray.get(7).x = xOffset + 8;
		textComponent.textArray.get(7).y = 107 + yOffset;

		// Def HP
		textComponent.textArray.get(9).text = Integer.toString(combatSystemCalculator.getAttackingUnitStats().health);
		textComponent.textArray.get(9).x = xOffset + 8;
		textComponent.textArray.get(9).y = 120 + yOffset;

		// Defending unit damage
		CombatSystemCalculator.DefendingDamage = combatSystemCalculator.calculateDamage();
	}
	
	/**
	 * Sets the cursor to the enemy selection
	 */
	private void setCursorPosition() {
		pComponentMapper.get(mapCursor).x = pComponentMapper.get(allEntitiesThatCanBeActedUpon.get(unitSelection)).x;
		pComponentMapper.get(mapCursor).y = pComponentMapper.get(allEntitiesThatCanBeActedUpon.get(unitSelection)).y;
	}
	
	/**
	 * Sets the defending enemy
	 */
	private void setDefendingUnit() {
		combatSystemCalculator.setUnits(UserInterfaceManager.unitSelected, allEntitiesThatCanBeActedUpon.get(unitSelection));
		setDamageBoxLocation();
		setItemEquippedIcon();
	}
	
	/**
	 * Sets the location of the damage box
	 */
	private void setDamageBoxLocation() {
		PositionComponent unitPosition = pComponentMapper.get(UserInterfaceManager.unitSelected);
		PositionComponent damageBoxPosition = pComponentMapper.get(unitDamageSelectorMenuBox);
		
		damageBoxPosition.y = 40 + (CameraSystem.cameraY - CameraSystem.yConstant);
		if (unitPosition.x <= (FireEmblemGame.WIDTH / 2) + (CameraSystem.cameraX - CameraSystem.xConstant)) {
			damageBoxPosition.x = FireEmblemGame.WIDTH - (staticImageComponentMapper.get(unitDamageSelectorMenuBox).width + 10) + (CameraSystem.cameraX - CameraSystem.xConstant);
			setText(true);
		} else {
			damageBoxPosition.x = 10 + (CameraSystem.cameraX - CameraSystem.xConstant);
			setText(false);
		}
	}
	
	/**
	 * Set the location for the item icon
	 */
	private void setItemEquippedIcon() {
		TextComponent textComponent = tComponentMapper.get(unitDamageSelectorMenuBox);
		staticImageComponentMapper.get(iComponentMapper.get(allEntitiesThatCanBeActedUpon.get(unitSelection)).selectedItem).isEnabled = true;
		pComponentMapper.get(iComponentMapper.get(allEntitiesThatCanBeActedUpon.get(unitSelection)).selectedItem).x = textComponent.textArray.get(1).x + 45;
		pComponentMapper.get(iComponentMapper.get(allEntitiesThatCanBeActedUpon.get(unitSelection)).selectedItem).y = textComponent.textArray.get(1).y - 12;
	}
	
	/**
	 * Calculate enemies/allys around
	 */
	private void calculateEntities() {
		// Reset stats
		allEntitiesThatCanBeActedUpon.clear();
		allRedCells.clear();
		unitSelection = 0;
		
		// Calculate all enemies
		int weaponRange = itemComponentMapper.get((iComponentMapper.get(UserInterfaceManager.unitSelected).selectedItem)).maxRange;
		calculateEnemies(UserInterfaceManager.unitSelected, weaponRange);
		
		// Enable preview if more than 0 entities can be affected
		if (allEntitiesThatCanBeActedUpon.size > 0) {
			staticImageComponentMapper.get(mapCursor).isEnabled = true;
			staticImageComponentMapper.get(unitDamageSelectorMenuBox).isEnabled = true;
			setDefendingUnit();
			setCursorPosition();
		}
		
		// Enable Red Squares
		for (MapCell mapCell : allRedCells) {
			staticImageComponentMapper.get(mapCell.redSquare).isEnabled = true;
		}
	}
	
	private void calculateEnemies(Entity unit, int attackRange) {
		MapCell initialTile = mStatComponentMapper.get(unit).currentCell;

		for (int i = 0; i < initialTile.adjTiles.size; i++) {

			MapCell adjMapCell = initialTile.adjTiles.get(i);

			// Next Attack Range
			int nextAttackRange = attackRange - 1;
			if (nextAttackRange >= 0) {
				allRedCells.add(adjMapCell);
				if (!adjMapCell.isOccupied) {
					calculateEnemies(unit, nextAttackRange);
				} else {
					// Is the unit an enemy?
					if (!mStatComponentMapper.get(adjMapCell.occupyingUnit).isAlly
							&& !allEntitiesThatCanBeActedUpon.contains(unit, true)) {
						allEntitiesThatCanBeActedUpon.add(adjMapCell.occupyingUnit);
					}
				}
			}
		}
	}
	
	/**
	 * Cycle through the enemies
	 */
	private void cycleInt() {
		if (unitSelection < 0) {
			unitSelection = allEntitiesThatCanBeActedUpon.size - 1;
		}

		if (unitSelection > allEntitiesThatCanBeActedUpon.size - 1) {
			unitSelection = 0;
		}
	}
}
