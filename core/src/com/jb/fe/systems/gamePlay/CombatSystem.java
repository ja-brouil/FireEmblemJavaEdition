package com.jb.fe.systems.gamePlay;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.jb.fe.components.MovementStatsComponent;
import com.jb.fe.systems.SystemPriorityDictionnary;

public class CombatSystem extends EntitySystem {
	
	private Entity attackingUnit;
	private Entity defendingUnit;
	
	private ComponentMapper<MovementStatsComponent> uComponentMapper = ComponentMapper.getFor(MovementStatsComponent.class);
	
	public CombatSystem() {
		super(SystemPriorityDictionnary.CombatPhase);
	}
	
	@Override
	public void update(float delta) {
		
	}	
}
