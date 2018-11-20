package com.jb.fe.systems.graphics;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
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

	private ComponentMapper<TextComponent> tComponentMapper = ComponentMapper.getFor(TextComponent.class);
	
	// Main font
	private BitmapFont mainFont;
	
	public TextRenderer(SpriteBatch spriteBatch, AssetManager assetManager) {
		priority = SystemPriorityDictionnary.TextRenderer;
		this.spriteBatch = spriteBatch;
		assetManager.load("UI/font/outline.fnt", BitmapFont.class);
		assetManager.finishLoading();
		mainFont = assetManager.get("UI/font/outline.fnt", BitmapFont.class);
		mainFont.setUseIntegerPositions(false);
	}
	
	@Override
	public void addedToEngine(Engine engine) {
		allTextEntities = engine.getEntitiesFor(Family.all(TextComponent.class).get());
		
		engine.addEntityListener(Family.all(TextComponent.class).get(), new EntityListener() {
			
			@Override
			public void entityRemoved(Entity entity) {
				allTextEntities = engine.getEntitiesFor(Family.all(TextComponent.class).get());
			}
			
			@Override
			public void entityAdded(Entity entity) {
				allTextEntities = engine.getEntitiesFor(Family.all(TextComponent.class).get());
			}
		});
	}
	
	// This part needs to be better thought out | Maybe render per object? We don't have that many screens
	@Override
	public void update(float delta) {
		allTextEntities = getEngine().getEntitiesFor(Family.all(PositionComponent.class, TextComponent.class).get());
		spriteBatch.begin();
		for  (Entity textEntity : allTextEntities) {
			TextComponent textComponent = tComponentMapper.get(textEntity);
			if (textComponent.isDrawing) {
				for (int i = 0; i < textComponent.textArray.size; i++) {
					if (textComponent.textArray.get(i).isEnabled) {
						mainFont.getData().setScale(textComponent.textArray.get(i).textFontSize);
						mainFont.setColor(textComponent.textArray.get(i).textColor);
						mainFont.draw(spriteBatch, textComponent.textArray.get(i).text, textComponent.textArray.get(i).x, textComponent.textArray.get(i).y);
					}
				}
			}
		}
		spriteBatch.end();
	}
}
