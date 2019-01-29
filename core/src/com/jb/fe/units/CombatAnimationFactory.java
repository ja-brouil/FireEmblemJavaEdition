package com.jb.fe.units;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;

public class CombatAnimationFactory {

	public static Entity createCombatAnimationEntity(AssetManager assetManager, Engine engine, String fileLocationString) {
		Entity combatAnimationEntity = new Entity();
		
		
		engine.addEntity(combatAnimationEntity);
		return combatAnimationEntity;
	}
}
