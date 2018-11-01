package com.jb.fe.systems.input;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.jb.fe.components.InputComponent;
import com.jb.fe.systems.SystemPriorityDictionnary;

public class InputHandlingSystem extends EntitySystem {
	
	private ImmutableArray<Entity> allInputEntities;
	private ComponentMapper<InputComponent> inputComponentMapper = ComponentMapper.getFor(InputComponent.class);
	
	public InputHandlingSystem() {
		priority = SystemPriorityDictionnary.InputHandle;
	}
	
	@Override
	public void addedToEngine(Engine engine) {
		getAllInputEntities();
		
		engine.addEntityListener(Family.all(InputComponent.class).get(), new EntityListener() {
			
			@Override
			public void entityRemoved(Entity entity) {
				getAllInputEntities();
			}
			
			@Override
			public void entityAdded(Entity entity) {
				getAllInputEntities();
			}
		});
	}
	
	@Override
	public void update(float delta) {
		for (Entity entity : allInputEntities) {
			InputComponent inputComponent = inputComponentMapper.get(entity);
			
			inputComponent.inputHandling.handleInput();
		}
	}
	
	/*
	 * Returns all input entities
	 */
	public void getAllInputEntities() {
		allInputEntities = getEngine().getEntitiesFor(Family.all(InputComponent.class).get());
	}
	
}
