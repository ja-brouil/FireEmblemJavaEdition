package com.jb.fe.components;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/*
 * Animation object
 */
public class AnimationObject{
	
	// Global default animation timer
	public static final float DEFAULT_ANIMATION_TIMER = 1f / 6f;

	// Timers
	public float animationElapsedTime;
	public float animationTimerLength;
	public boolean useSynchronizedTimer;
	
	// Offsets
	public float Xoffset;
	public float YoffSet;
	
	// Width and Heigh if needed
	public float width;
	public float height;
	
	// Z order
	public int zOrder;
	
	// Single Animation use
	public boolean isLooping;
	
	// Enable Use
	public boolean isDrawing = true;
	
	// Animation Frame
	public Animation<TextureRegion> animationFrames;
	
	public AnimationObject(AssetManager assetManager, String fileLocation, int width, int height, float animationTimerLength, int col, int row, int frameAmount) {
		zOrder = 0;
		YoffSet = 0;
		Xoffset = 0;
		isLooping = true;
		
		// Process Animation Creation
		if(!assetManager.isLoaded(fileLocation, Texture.class)) {
			assetManager.load(fileLocation, Texture.class);
			assetManager.finishLoading();
		}
		
		Texture tmpTexture = assetManager.get(fileLocation, Texture.class);
		TextureRegion[][] tmp = TextureRegion.split(tmpTexture, width, height);
		TextureRegion[] animationKeyFrames = new TextureRegion[frameAmount];
		for (int i = 0; i < animationKeyFrames.length; i++) {
			animationKeyFrames[i] = tmp[row][i];
		}
		
		animationFrames = new Animation<>(animationTimerLength, animationKeyFrames);
		
		// Set Default Width/Height
		this.width = animationKeyFrames[0].getRegionWidth();
		this.height = animationKeyFrames[0].getRegionHeight();
		
		// Synchronize Default
		useSynchronizedTimer = true;
	}
	
	public AnimationObject(AssetManager assetManager, String fileLocation, int width, int height, float animationTimerLength, int col, int row, int frameAmount, int zOrder) {
		this(assetManager, fileLocation, width, height, animationTimerLength, col, row, frameAmount);
		this.zOrder = zOrder;
	}
	
	/**
	 * Flip texture if needed
	 * @param flipX along the X axis
	 * @param flipY along the y axis
	 */
	public void flipTexture(boolean flipX, boolean flipY) {
		for (TextureRegion keyFrame : animationFrames.getKeyFrames()) {
			keyFrame.flip(flipX, flipY);
		}
	}
}
