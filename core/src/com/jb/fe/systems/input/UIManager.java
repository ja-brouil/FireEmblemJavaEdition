package com.jb.fe.systems.input;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.jb.fe.components.InputComponent;
import com.jb.fe.components.NameComponent;
import com.jb.fe.components.UIComponent;
import com.jb.fe.level.Level;
import com.jb.fe.systems.SystemPriorityDictionnary;

/**
 * Controls the UI and user input
 * @author james
 *
 */
public class UIManager extends EntitySystem {
	
	private ImmutableArray<Entity> uiEntities;
	
	private ComponentMapper<UIComponent> uComponentMapper = ComponentMapper.getFor(UIComponent.class);
	private ComponentMapper<InputComponent> iComponentMapper = ComponentMapper.getFor(InputComponent.class);
	private ComponentMapper<NameComponent> nComponentMapper = ComponentMapper.getFor(NameComponent.class);
	
	public UIManager() {
		priority = SystemPriorityDictionnary.HandleInputAndUI;
	}
	
	@Override
	public void addedToEngine(Engine engine) {
		engine.addEntityListener(Family.all(UIComponent.class, InputComponent.class).get(), new EntityListener() {
			
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
	
	private void getAllInputEntities() {
		uiEntities = getEngine().getEntitiesFor(Family.all(InputComponent.class, UIComponent.class).get());
	}
	
	@Override
	public void update(float delta) {
		for (Entity uiEntity : uiEntities) {
			UIComponent uiComponent = uComponentMapper.get(uiEntity);
			InputComponent inputComponent = iComponentMapper.get(uiEntity);
			
			if (uiComponent.isEnabled) {
				uiComponent.updateUI.updateUI(delta);;
			}
			
			if (inputComponent.isEnabled) {
				inputComponent.inputHandling.handleInput();
			}
		}
	}
	
	public Entity getUIEntity(String name) {
		for (Entity uiEntity : uiEntities) {
			NameComponent nameComponent = nComponentMapper.get(uiEntity);
			if (nameComponent.name.equals(name)) {
				return uiEntity;
			}
		}
		
		return null;
	}
	
	public void startSystem(Level level) {
		
	}
}
