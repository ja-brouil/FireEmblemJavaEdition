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
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jb.fe.UI.Text.TextObject;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.components.TextComponent;
import com.jb.fe.systems.SystemPriorityList;

public class TextRenderer extends EntitySystem{

	private SpriteBatch spriteBatch;
	
	private ImmutableArray<Entity> allTextEntities;

	private ComponentMapper<TextComponent> tComponentMapper = ComponentMapper.getFor(TextComponent.class);
	
	// Main font
	private BitmapFont mainFont;
	private GlyphLayout glyphLayout;
	
	public TextRenderer(SpriteBatch spriteBatch, AssetManager assetManager) {
		priority = SystemPriorityList.TextRenderer;
		this.spriteBatch = spriteBatch;
		
		assetManager.load("UI/font/outline.fnt", BitmapFont.class);
		assetManager.finishLoading();
		
		mainFont = assetManager.get("UI/font/outline.fnt", BitmapFont.class);
		mainFont.setUseIntegerPositions(false);
		glyphLayout = new GlyphLayout();
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
					TextObject textObject = textComponent.textArray.get(i);
					if (textObject.isEnabled) {
						mainFont.getData().setScale(textObject.textFontSize);
						mainFont.setColor(textObject.textColor);
						glyphLayout.setText(mainFont, textObject.text, mainFont.getColor(), 2f, textObject.alignment, false);
						mainFont.draw(spriteBatch, glyphLayout, textObject.x, textObject.y);
					}
				}
			}
		}
		spriteBatch.end();
	}
}
