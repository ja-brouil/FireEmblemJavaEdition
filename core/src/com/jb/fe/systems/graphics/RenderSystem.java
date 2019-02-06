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
import com.jb.fe.components.StaticImageComponent;
import com.jb.fe.components.ZOrderComponent;
import com.jb.fe.systems.SystemPriorityList;

public class RenderSystem extends EntitySystem {
	
	// All Animations
	private ImmutableArray<Entity> animationEntities;
	private Array<Entity> sortedAnimationEntities;
	
	// Sort
	private Comparator<Entity> compareEntityListener;
	private Comparator<Entity> compareXAxis;
	private Comparator<Entity> compareYAxis;
	
	// Component Mappers
	private ComponentMapper<AnimationComponent> animationComponentMapper = ComponentMapper.getFor(AnimationComponent.class);
	private ComponentMapper<StaticImageComponent> staticImageComponentMapper = ComponentMapper.getFor(StaticImageComponent.class);
	private ComponentMapper<PositionComponent> positionComponentMapper = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<ZOrderComponent> zOrderComponentMapper = ComponentMapper.getFor(ZOrderComponent.class);
	
	// SpriteBatch
	private SpriteBatch spriteBatch;
	
	// Synchronized Timer
	private float synchronizedAnimationTimer;
	
	public RenderSystem(SpriteBatch spriteBatch){
		this.spriteBatch = spriteBatch;
		sortedAnimationEntities = new Array<>();
		compareEntityListener = (Entity a, Entity b) -> {
			return zOrderComponentMapper.get(a).zOrder - zOrderComponentMapper.get(b).zOrder;
		};
		compareXAxis = (Entity a, Entity b) -> {
			return (int) positionComponentMapper.get(b).x - (int) positionComponentMapper.get(a).x;
		};
		compareYAxis = (Entity a, Entity b) -> {
			return (int) positionComponentMapper.get(b).y - (int) positionComponentMapper.get(a).y;
		};
		
		// Engine Priority
		priority = SystemPriorityList.GraphicsRender;
	}
	
	// Entity Listener
	@Override
	public void addedToEngine(Engine engine) {
		engine.addEntityListener(Family.one(ZOrderComponent.class).get(), new EntityListener() {
			
			@Override
			public void entityAdded(Entity entity) {
				animationEntities = engine.getEntitiesFor(Family.one(ZOrderComponent.class).get());
				sortedAnimationEntities.add(entity);
				sortedAnimationEntities.sort(compareEntityListener);
			}
			
			@Override
			public void entityRemoved(Entity entity) {
				animationEntities = engine.getEntitiesFor(Family.one(ZOrderComponent.class).get());
				sortedAnimationEntities.removeValue(entity, true);
				sortedAnimationEntities.sort(compareEntityListener);
			}
		});
		
		// First Set of Entities
		animationEntities = engine.getEntitiesFor(Family.one(ZOrderComponent.class).get());
		for (Entity entity : animationEntities) {
			sortedAnimationEntities.add(entity);
		}
		sortedAnimationEntities.sort(compareEntityListener);
	}
	
	// Update Function
	@Override
	public void update(float delta) {
		spriteBatch.begin();
		// Sort Animation entites by x and y
		sortedAnimationEntities.sort(compareXAxis);
		sortedAnimationEntities.sort(compareYAxis);
		
		// Sort by Z
		sortedAnimationEntities.sort(compareEntityListener);
		
		// Update Synchronized Timer
		synchronizedAnimationTimer += delta;
		
		for (Entity entity : sortedAnimationEntities) {
			AnimationComponent animationComponent = animationComponentMapper.get(entity);
			StaticImageComponent staticImageComponent = staticImageComponentMapper.get(entity);
			PositionComponent positionComponent = positionComponentMapper.get(entity);
			
			// Animation
			if (animationComponent != null) {
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
				}
			}
			
			// Static Image
			if (staticImageComponent != null) {
				if (staticImageComponent.isEnabled) {
					spriteBatch.setColor(1, 1, 1, staticImageComponent.alpha);
					spriteBatch.draw(staticImageComponent.staticImage, positionComponent.x + staticImageComponent.xOffset, positionComponent.y + staticImageComponent.yOffset, staticImageComponent.width, staticImageComponent.height);
					spriteBatch.setColor(1, 1, 1, 1);
				}
			}
		}
		
		spriteBatch.end();
	}
}
