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
import com.jb.fe.components.PositionComponent;
import com.jb.fe.components.StaticImageComponent;
import com.jb.fe.systems.SystemPriorityDictionnary;

public class StaticImageSystem extends EntitySystem{
	
	// All Static Images
	private ImmutableArray<Entity> allStaticEntities;
	private Array<Entity> sortedStaticEntities;
	
	// Component Mappers
	private ComponentMapper<PositionComponent> pComponentMapper = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<StaticImageComponent> sComponentMapper = ComponentMapper.getFor(StaticImageComponent.class);
	
	// Sort
	private Comparator<Entity> compareStatics;
	
	// SpriteBatch
	private SpriteBatch spriteBatch;
	
	public StaticImageSystem(SpriteBatch spriteBatch) {
		// Engine Priority
		this.priority = SystemPriorityDictionnary.StaticRender;
		this.spriteBatch = spriteBatch;

		sortedStaticEntities = new Array<Entity>();
		
		compareStatics = (Entity a, Entity b) -> {
			return sComponentMapper.get(b).zOrder - sComponentMapper.get(a).zOrder;
		};
	}
	
	// Entity Listeners
	@Override
	public void addedToEngine(Engine engine) {
		engine.addEntityListener(Family.all(PositionComponent.class, StaticImageComponent.class).get(), new EntityListener() {
			
			@Override
			public void entityAdded(Entity entity) {
				allStaticEntities = engine.getEntitiesFor(Family.all(PositionComponent.class, StaticImageComponent.class).get());
				sortedStaticEntities.add(entity);
				sortedStaticEntities.sort(compareStatics);
			}
			
			@Override
			public void entityRemoved(Entity entity) {
				allStaticEntities = engine.getEntitiesFor(Family.all(PositionComponent.class, StaticImageComponent.class).get());
				sortedStaticEntities.removeValue(entity, true);
				sortedStaticEntities.sort(compareStatics);
			}
		});
		
		// First Drawing
		allStaticEntities = engine.getEntitiesFor(Family.all(PositionComponent.class, StaticImageComponent.class).get());
		for (Entity entity : allStaticEntities) {
			sortedStaticEntities.add(entity);
		}
		sortedStaticEntities.sort(compareStatics);
	}
	
	@Override
	public void update(float delta) {
		spriteBatch.begin();
		for (Entity entity : sortedStaticEntities) {
			
			PositionComponent positionComponent = pComponentMapper.get(entity);
			StaticImageComponent staticImageComponent = sComponentMapper.get(entity);
			
			if (staticImageComponent.isEnabled) {
				spriteBatch.setColor(1, 1, 1, staticImageComponent.alpha);
				spriteBatch.draw(staticImageComponent.staticImage, positionComponent.x, positionComponent.y, staticImageComponent.width, staticImageComponent.height);
				spriteBatch.setColor(1, 1, 1, 1);
			}
		}
	}
}
