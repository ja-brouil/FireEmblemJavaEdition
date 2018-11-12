package com.jb.fe.systems.graphics;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.components.StaticImageComponent;
import com.jb.fe.components.TransitionMethodComponent;
import com.jb.fe.components.TransitionMethodComponent.TransitionType;
import com.jb.fe.systems.SystemPriorityDictionnary;

/**
 * Controls the transition methods
 * @author JamesBrouillet
 *
 */
public class TransitionSystem extends EntitySystem {

	// All Transition entities
	private ImmutableArray<Entity> allTransitionEntities;
	
	// Component Mappers
	private ComponentMapper<PositionComponent> pComponentMapper = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<StaticImageComponent> sComponentMapper = ComponentMapper.getFor(StaticImageComponent.class);
	private ComponentMapper<TransitionMethodComponent> tComponentMapper = ComponentMapper.getFor(TransitionMethodComponent.class);

	
	public TransitionSystem() {
		this.priority = SystemPriorityDictionnary.TransitionRender;
	}

	@Override
	public void addedToEngine(Engine engine) {
		engine.addEntityListener(Family.all(StaticImageComponent.class, TransitionMethodComponent.class).get(), new EntityListener() {
			
			@Override
			public void entityAdded(Entity entity) {
				allTransitionEntities = engine.getEntitiesFor(Family.all(StaticImageComponent.class, TransitionMethodComponent.class).get());
			}
			
			@Override
			public void entityRemoved(Entity entity) {
				allTransitionEntities = engine.getEntitiesFor(Family.all(StaticImageComponent.class, TransitionMethodComponent.class).get());
			}
		});
	}
	
	@Override
	public void update(float delta) {
		for (Entity entity : allTransitionEntities) {
			TransitionMethodComponent transitionMethodComponent = tComponentMapper.get(entity);
			if(!transitionMethodComponent.isActive) {
				continue;
			}
			
			// Left to the Right
			if (transitionMethodComponent.transitionType.equals(TransitionType.LEFT_TO_RIGHT)) {
				playLeftToRight(delta, entity);
			}
		}
	}
	
	// Deconstruct to the right side
	private void playLeftToRight(float delta, Entity entity) {
		StaticImageComponent staticImageComponent = sComponentMapper.get(entity);
		
	}
}
