package com.jb.fe.systems.input;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.jb.fe.components.AnimationComponent;
import com.jb.fe.components.MapCursorStateComponent;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.level.Level;
import com.jb.fe.systems.SystemPriorityDictionnary;

public class MapCursorOutOfBoundsSystem extends EntitySystem {
	
	private Level level;
	private Entity mapCursor;
	
	private ComponentMapper<PositionComponent> positionComponentMapper = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<AnimationComponent> animationComponentMapper = ComponentMapper.getFor(AnimationComponent.class);
	
	public MapCursorOutOfBoundsSystem() {
		priority = SystemPriorityDictionnary.MapCursorOutOfbounds;
	}
	
	@Override
	public void update(float delta) {
		// Prevent out of bounds
		PositionComponent positionComponent = positionComponentMapper.get(mapCursor);
		AnimationComponent animationComponent = animationComponentMapper.get(mapCursor);
		
		if (positionComponent.x < 0) {
			positionComponent.x = 0;
		}
		
		if (positionComponent.x > level.mapWidthLimit - (animationComponent.currentAnimation.width * 2)) {
			positionComponent.x = level.mapWidthLimit - (animationComponent.currentAnimation.width * 2);
		}
		
		if (positionComponent.y < 0) {
			positionComponent.y = 0;
		}
		
		if (positionComponent.y > level.mapHeightLimit - animationComponent.currentAnimation.height) {
			positionComponent.y = level.mapHeightLimit - animationComponent.currentAnimation.height;
		}
	}
	
	public void startSystem(Level level) {
		this.level = level;
		mapCursor = getEngine().getEntitiesFor(Family.all(MapCursorStateComponent.class).get()).first();
	}
}