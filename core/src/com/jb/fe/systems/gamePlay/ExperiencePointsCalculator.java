package com.jb.fe.systems.gamePlay;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.math.MathUtils;
import com.jb.fe.components.UnitStatsComponent;

public class ExperiencePointsCalculator extends EntitySystem {
	
	// Unit Stats Component Mapper
	private static ComponentMapper<UnitStatsComponent> uComponentMapper = ComponentMapper.getFor(UnitStatsComponent.class);
	
	public static int getExpEarned(Entity ally, Entity enemy) {
		
		
		
		return 0;
	}
	
	/**
	 * Calculate how many level up points. Minimum of 2 upgrades if everything is not upgraded
	 */
	public static void levelUp() {
		int skillUpgradeCount = 0;
		
		int randomChance = MathUtils.random(0, 100);
		
	}
	
}
