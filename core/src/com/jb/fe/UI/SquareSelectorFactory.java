package com.jb.fe.UI;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.components.StaticImageComponent;
import com.jb.fe.components.ZOrderComponent;
import com.jb.fe.map.MapCell;
import com.jb.fe.systems.graphics.ZOrderDictionnary;

public class SquareSelectorFactory {
	
	public static final float alpha = 0.4f;
	
	// Graphics
	private AssetManager assetManager;
	
	public SquareSelectorFactory(AssetManager assetManager) {
		this.assetManager = assetManager;
	}
	
	public Entity createBlueSquare(float x, float y) {
		Entity blueSquare = new Entity();
		
		ZOrderComponent zOrderComponent = new ZOrderComponent(ZOrderDictionnary.BACKGROUND);
		PositionComponent positionComponent = new PositionComponent(x, y);
		StaticImageComponent blueSquareImage = new StaticImageComponent(assetManager, "UI/selected/bluedarkSelect.png", MapCell.CELL_SIZE, MapCell.CELL_SIZE);
		blueSquareImage.alpha = SquareSelectorFactory.alpha;
		blueSquareImage.isEnabled = true;
		
		blueSquare.add(positionComponent);
		blueSquare.add(blueSquareImage);
		blueSquare.add(zOrderComponent);
		
		return blueSquare;
	}
	
	public Entity createRedSquare(float x, float y) {
		Entity redSquare = new Entity();
		
		ZOrderComponent zOrderComponent = new ZOrderComponent(ZOrderDictionnary.BACKGROUND);
		PositionComponent positionComponent = new PositionComponent(x, y);
		StaticImageComponent redSquareImage = new StaticImageComponent(assetManager, "UI/selected/redselect.png", MapCell.CELL_SIZE, MapCell.CELL_SIZE);
		redSquareImage.alpha = SquareSelectorFactory.alpha;
		redSquareImage.isEnabled = true;
		
		redSquare.add(positionComponent);
		redSquare.add(redSquareImage);
		redSquare.add(zOrderComponent);
		
		return redSquare;
	}
}
