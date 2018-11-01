package com.jb.fe.components;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.jb.fe.map.MapCell;

public class SquareSelectorFactory {
	
	public static final float alpha = 0.4f;
	
	// Graphics
	private AssetManager assetManager;
	
	public SquareSelectorFactory(AssetManager assetManager) {
		this.assetManager = assetManager;
	}
	
	public Entity createBlueSquare(float x, float y) {
		Entity blueSquare = new Entity();
		
		PositionComponent positionComponent = new PositionComponent(x, y);
		StaticImageComponent blueSquareImage = new StaticImageComponent(assetManager, "UI/selected/bluedarkSelect.png", MapCell.CELL_SIZE, MapCell.CELL_SIZE);
		blueSquareImage.alpha = SquareSelectorFactory.alpha;
		blueSquareImage.isEnabled = false;
		
		blueSquare.add(positionComponent);
		blueSquare.add(blueSquareImage);
		
		return blueSquare;
	}
	
	public Entity createRedSquare(float x, float y) {
		Entity redSquare = new Entity();
		PositionComponent positionComponent = new PositionComponent(x, y);
		StaticImageComponent redSquareImage = new StaticImageComponent(assetManager, "UI/selected/redselect.png", MapCell.CELL_SIZE, MapCell.CELL_SIZE);
		redSquareImage.alpha = SquareSelectorFactory.alpha;
		redSquareImage.isEnabled = false;
		
		redSquare.add(positionComponent);
		redSquare.add(redSquareImage);
		
		return redSquare;
	}
}
