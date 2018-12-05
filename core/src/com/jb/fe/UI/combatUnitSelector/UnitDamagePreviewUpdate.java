package com.jb.fe.UI.combatUnitSelector;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.jb.fe.components.NameComponent;
import com.jb.fe.components.TextComponent;
import com.jb.fe.components.UIComponent.UpdateUI;
import com.jb.fe.screens.FireEmblemGame;
import com.jb.fe.systems.gamePlay.CombatSystemCalculator;
import com.jb.fe.systems.inputAndUI.UIManager;

public class UnitDamagePreviewUpdate implements UpdateUI {

	private ComponentMapper<NameComponent> nComponentMapper = ComponentMapper.getFor(NameComponent.class);
	private CombatSystemCalculator combatSystemCalculator;
	public UnitDamagePreviewState unitDamagePreviewState;
	
	public UnitDamagePreviewUpdate() {
		combatSystemCalculator = new CombatSystemCalculator();
		this.unitDamagePreviewState = UnitDamagePreviewState.SELECTING_UNIT;
	}
	
	@Override
	public void updateUI(float delta) {}
	
	public void calculateDamage(Entity attackingUnit, Entity defendingUnit) {
		combatSystemCalculator.setUnits(attackingUnit, defendingUnit);
		System.out.println(combatSystemCalculator.calculateDamage()); //DEBUG FOR NOW
	}
	
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
	public void setStatisticsText(TextComponent textComponent, boolean isRight) {
		int xOffset = 0;
		if (isRight) {
			xOffset = FireEmblemGame.WIDTH - 80;
		} else {
			xOffset = 10;
		}
		
		// Defending Unit Item Equipped
		textComponent.textArray.get(0).text = nComponentMapper.get(combatSystemCalculator.getDefendingInventory().selectedItem).name;
		textComponent.textArray.get(0).x = 5 + xOffset;
		textComponent.textArray.get(0).y = 53;
		
		// Defending Unit name
		textComponent.textArray.get(1).text = nComponentMapper.get(combatSystemCalculator.getDefendingUnit()).name;
		textComponent.textArray.get(1).x = 5 + xOffset;
		textComponent.textArray.get(1).y = 63;
		
		// Attack Crit
		textComponent.textArray.get(2).text = nComponentMapper.get(combatSystemCalculator.getDefendingInventory().selectedItem).name;
		textComponent.textArray.get(2).x = 70 - 10 + xOffset;
		textComponent.textArray.get(2).y = 30;
		
		// Attacking Numbers First
		// Atk Crit
		textComponent.textArray.get(2).text = Integer.toString(combatSystemCalculator.calculateCritChanceNumber());
		textComponent.textArray.get(2).x = 70 - 19 + xOffset;
		textComponent.textArray.get(2).y = 81;
		
		// Atk Hit
		textComponent.textArray.get(4).text = Integer.toString(combatSystemCalculator.calculateHitChanceNumber());
		textComponent.textArray.get(4).x = 70 - 19 + xOffset;
		textComponent.textArray.get(4).y = 94;
		
		// Atk Might
		textComponent.textArray.get(6).text = Integer.toString(combatSystemCalculator.getAttackingItem().might);
		textComponent.textArray.get(6).x = 70 - 19 + xOffset;
		textComponent.textArray.get(6).y = 107;
		
		// Atk HP
		textComponent.textArray.get(8).text = Integer.toString(combatSystemCalculator.getAttackingUnitStats().health);
		textComponent.textArray.get(8).x = 70 - 19 + xOffset;
		textComponent.textArray.get(8).y = 120;
		
		// Attack Unit Name
		textComponent.textArray.get(10).text = nComponentMapper.get(combatSystemCalculator.getAttackingUnit()).name;
		textComponent.textArray.get(10).x = 70 - 40 + xOffset;
		textComponent.textArray.get(10).y = 134;
		
		// Attacking unit damage
		CombatSystemCalculator.AttackingDamage = combatSystemCalculator.calculateDamage();
		
		// Defending Numbers
		// Swap Units
		Entity swapEntity = combatSystemCalculator.getDefendingUnit();
		combatSystemCalculator.setUnits(swapEntity, UIManager.currentGameUnit);
		
		// Def Crit
		textComponent.textArray.get(3).text = Integer.toString(combatSystemCalculator.calculateCritChanceNumber());
		textComponent.textArray.get(3).x = xOffset + 8;
		textComponent.textArray.get(3).y = 81;
		
		// Def Hit
		textComponent.textArray.get(5).text = Integer.toString(combatSystemCalculator.calculateHitChanceNumber());
		textComponent.textArray.get(5).x = xOffset + 8;
		textComponent.textArray.get(5).y = 94;
		
		// Def Might
		textComponent.textArray.get(7).text = Integer.toString(combatSystemCalculator.getAttackingItem().might);
		textComponent.textArray.get(7).x = xOffset + 8;
		textComponent.textArray.get(7).y = 107;
		
		// Def HP
		textComponent.textArray.get(9).text = Integer.toString(combatSystemCalculator.getAttackingUnitStats().health);
		textComponent.textArray.get(9).x = xOffset + 8;
		textComponent.textArray.get(9).y = 120;
		
		// Defending unit damage
		CombatSystemCalculator.DefendingDamage = combatSystemCalculator.calculateDamage();
	}
	
	public static enum UnitDamagePreviewState {
		SELECTING_UNIT, READY_FOR_COMBAT;
	}
	
	public CombatSystemCalculator getCombatSystemCalculator() {
		return combatSystemCalculator;
	}
}