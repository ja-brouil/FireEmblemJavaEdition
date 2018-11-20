package com.jb.fe.UI.infoBoxes;


import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.jb.fe.UI.TextObject;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.components.StaticImageComponent;
import com.jb.fe.components.TextComponent;
import com.jb.fe.components.ZOrderComponent;
import com.jb.fe.systems.graphics.ZOrderDictionnary;

public class TerrainInfoBox extends MenuBox{

	private TextComponent textComponent;
	private PositionComponent positionComponent;
	
	private StaticImageComponent staticImageComponent;
	private ZOrderComponent zOrderComponent;

	public TerrainInfoBox(Entity mapCursor, AssetManager assetManager, Engine engine) {
		super(mapCursor, assetManager, engine);
		
		Entity menuBox = new Entity();
		positionComponent = new PositionComponent(180, 140);
		
		// Text Objects -> Text
		textComponent = new TextComponent();
		textComponent.textArray.addFirst(new TextObject(positionComponent.x  + 22f, positionComponent.y + 31f, "--", 0.2f));
		textComponent.textArray.addFirst(new TextObject(positionComponent.x + 15f, positionComponent.y + 17f, "AVD:", 0.2f));
		textComponent.textArray.addFirst(new TextObject(positionComponent.x + 30f, positionComponent.y  + 17f, "0", 0.2f));
		textComponent.textArray.addFirst(new TextObject(positionComponent.x + 15f, positionComponent.y + 10f, "DEF: ", 0.2f));
		textComponent.textArray.addFirst(new TextObject(positionComponent.x, positionComponent.y, "0", 0.2f));
		menuBox.add(textComponent);
		
		staticImageComponent = new StaticImageComponent(assetManager, "UI/terrainInfo/terrainInfoBox.png");
		zOrderComponent = new ZOrderComponent(ZOrderDictionnary.UI_TOP_LAYER);
		menuBox.add(staticImageComponent);
		menuBox.add(zOrderComponent);
		
		engine.addEntity(menuBox);
	}

	
}
