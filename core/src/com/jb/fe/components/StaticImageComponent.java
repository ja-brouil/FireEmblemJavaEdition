package com.jb.fe.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class StaticImageComponent implements Component{

	// Texture
	public TextureRegion staticImage;
	public Texture wholeImage;
	
	// Dimension
	public float width, height;
	
	// Offset
	public float xOffset = 0;
	public float yOffset = 0;
	
	// Enabled
	public boolean isEnabled;
	
	// Alpha
	public float alpha = 1f;
	
	public StaticImageComponent(AssetManager assetManager, String fileLocation) {
		if (!assetManager.isLoaded(fileLocation, Texture.class)) {
			assetManager.load(fileLocation, Texture.class);
			assetManager.finishLoading();
		}
		
		wholeImage = assetManager.get(fileLocation, Texture.class);
		staticImage = new TextureRegion(wholeImage, wholeImage.getWidth(), wholeImage.getHeight());
		
		this.width = wholeImage.getWidth();
		this.height = wholeImage.getWidth();
		
		isEnabled = true;
	}
	
	public StaticImageComponent(AssetManager assetManager, String fileLocation, int width, int height) {
		if (!assetManager.isLoaded(fileLocation, Texture.class)) {
			assetManager.load(fileLocation, Texture.class);
			assetManager.finishLoading();
		}
		
		wholeImage = assetManager.get(fileLocation, Texture.class);
		staticImage = new TextureRegion(wholeImage, width, height);
		
		this.width = staticImage.getRegionWidth();
		this.height = staticImage.getRegionHeight();
		
		isEnabled = true;
	}
	
	public StaticImageComponent(AssetManager assetManager, String fileLocation, int width, int height, float drawingWidth, float drawingHeight) {
		this(assetManager, fileLocation, width, height);
		
		this.width = drawingWidth;
		this.height = drawingHeight;
		
		isEnabled = true;
	}
	
	// Move the Section to a specific section
	public void setNewImageLocation(int x, int y, int width, int height) {
		staticImage.setRegion(x, y, width, height);
	}
}
