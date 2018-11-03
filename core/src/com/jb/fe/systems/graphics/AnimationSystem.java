package com.jb.fe.systems.graphics;

import java.util.Comparator;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.jb.fe.components.AnimationComponent;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.systems.SystemPriorityDictionnary;

public class AnimationSystem extends EntitySystem {
	
	// All Animations
	private ImmutableArray<Entity> animationEntities;
	private Array<Entity> sortedAnimationEntities;
	
	// Sort
	private Comparator<Entity> compareEntityListener;
	
	// Component Mappers
	private ComponentMapper<AnimationComponent> animationComponentMapper = ComponentMapper.getFor(AnimationComponent.class);
	private ComponentMapper<PositionComponent> positionComponentMapper = ComponentMapper.getFor(PositionComponent.class);
	
	// SpriteBatch
	private SpriteBatch spriteBatch;
	
	// Synchronized Timer
	private float synchronizedAnimationTimer;
	
	public AnimationSystem(SpriteBatch spriteBatch){
		this.spriteBatch = spriteBatch;
		sortedAnimationEntities = new Array<>();
		compareEntityListener = (Entity a, Entity b) -> {
			return animationComponentMapper.get(b).zOrder - animationComponentMapper.get(a).zOrder;
		};
		
		// Engine Priority
		priority = SystemPriorityDictionnary.AnimationRender;
	}
	
	// Entity Listener
	@Override
	public void addedToEngine(Engine engine) {
		engine.addEntityListener(Family.all(PositionComponent.class, AnimationComponent.class).get(), new EntityListener() {
			
			@Override
			public void entityRemoved(Entity entity) {
				animationEntities = engine.getEntitiesFor(Family.all(AnimationComponent.class, PositionComponent.class).get());
				sortedAnimationEntities.add(entity);
				sortedAnimationEntities.sort(compareEntityListener);
			}
			
			@Override
			public void entityAdded(Entity entity) {
				animationEntities = engine.getEntitiesFor(Family.all(AnimationComponent.class, PositionComponent.class).get());
				sortedAnimationEntities.removeValue(entity, true);
				sortedAnimationEntities.sort(compareEntityListener);
			}
		});
		
		// First Set of Entities
		animationEntities = engine.getEntitiesFor(Family.all(AnimationComponent.class, PositionComponent.class).get());
		for (Entity entity : animationEntities) {
			sortedAnimationEntities.add(entity);
		}
		sortedAnimationEntities.sort(compareEntityListener);
	}
	
	// Update Function
	@Override
	public void update(float delta) {
		// Update Synchronized Timer
		synchronizedAnimationTimer += delta;
		
		for (Entity entity : animationEntities) {
			AnimationComponent animationComponent = animationComponentMapper.get(entity);
			PositionComponent positionComponent = positionComponentMapper.get(entity);
			
			// Update Timer
			if (animationComponent.currentAnimation.isDrawing) {
				
				// Set Timer
				if (animationComponent.currentAnimation.useSynchronizedTimer) {
					animationComponent.currentAnimation.animationElapsedTime = synchronizedAnimationTimer;
				} else {
					animationComponent.currentAnimation.animationElapsedTime += delta;
				}
				
				// Render out animation
				spriteBatch.draw(animationComponent.currentAnimation.animationFrames.getKeyFrame(animationComponent.currentAnimation.animationElapsedTime,
						animationComponent.currentAnimation.isLooping), positionComponent.x + animationComponent.currentAnimation.Xoffset, 
						positionComponent.y + animationComponent.currentAnimation.YoffSet, 
						animationComponent.currentAnimation.width, animationComponent.currentAnimation.height);
				
				
				// End timer if not looping
				if (!animationComponent.currentAnimation.isLooping) {
					if (animationComponent.currentAnimation.animationFrames.isAnimationFinished(animationComponent.currentAnimation.animationElapsedTime)) {
						animationComponent.currentAnimation.animationElapsedTime = 0;
					}
				}
			}
		}
		
		spriteBatch.end();
	}
}
