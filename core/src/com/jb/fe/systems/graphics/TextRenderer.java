package com.jb.fe.systems.graphics;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.components.TextComponent;
import com.jb.fe.systems.SystemPriorityDictionnary;

public class TextRenderer extends EntitySystem{

	private SpriteBatch spriteBatch;
	
	private ImmutableArray<Entity> allTextEntities;
	
	private ComponentMapper<PositionComponent> pComponentMapper = ComponentMapper.getFor(PositionComponent.class);
	private ComponentMapper<TextComponent> tComponentMapper = ComponentMapper.getFor(TextComponent.class);
	
	// Main font
	private BitmapFont mainFont;
	
	public TextRenderer(SpriteBatch spriteBatch, AssetManager assetManager) {
		priority = SystemPriorityDictionnary.TextRenderer;
		this.spriteBatch = spriteBatch;
		
		assetManager.load("UI/font/main font.fnt", BitmapFont.class);
		assetManager.finishLoading();
		mainFont = assetManager.get("UI/font/main font.fnt", BitmapFont.class);
	}
	
	@Override
	public void addedToEngine(Engine engine) {
		allTextEntities = engine.getEntitiesFor(Family.all(PositionComponent.class, TextComponent.class).get());
		
		
	}
	
	// This part needs to be better thought out | Maybe render per object? We don't have that many screens
	@Override
	public void update(float delta) {
		allTextEntities = getEngine().getEntitiesFor(Family.all(PositionComponent.class, TextComponent.class).get());
		spriteBatch.begin();
		for  (Entity textEntity : allTextEntities) {
			PositionComponent positionComponent = pComponentMapper.get(textEntity);
			TextComponent textComponent = tComponentMapper.get(textEntity);
			
			
		}
		mainFont.getData().setScale(0.33f);
		mainFont.draw(spriteBatch, "Hello World", 50, 50);
		spriteBatch.end();
	}

}
