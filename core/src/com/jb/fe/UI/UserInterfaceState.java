package com.jb.fe.UI;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.assets.AssetManager;
import com.jb.fe.components.AnimationComponent;
import com.jb.fe.components.MovementStatsComponent;
import com.jb.fe.components.NameComponent;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.components.StaticImageComponent;
import com.jb.fe.components.TextComponent;
import com.jb.fe.components.UnitStatsComponent;
import com.jb.fe.systems.audio.SoundSystem;
import com.jb.fe.systems.inputAndUI.UserInterfaceManager;

public abstract class UserInterfaceState {
	
	protected AssetManager assetManager;
	protected SoundSystem soundSystem;
	protected UserInterfaceManager userInterfaceManager;
	
	// Component Mappers
	protected ComponentMapper<AnimationComponent> animationComponentMapper = ComponentMapper.getFor(AnimationComponent.class);
	protected ComponentMapper<MovementStatsComponent> mStatComponentMapper = ComponentMapper.getFor(MovementStatsComponent.class);
	protected ComponentMapper<StaticImageComponent> staticImageComponentMapper = ComponentMapper.getFor(StaticImageComponent.class);
	protected ComponentMapper<PositionComponent> pComponentMapper = ComponentMapper.getFor(PositionComponent.class);
	protected ComponentMapper<NameComponent> nComponentMapper = ComponentMapper.getFor(NameComponent.class);
	protected ComponentMapper<UnitStatsComponent> uComponentMapper = ComponentMapper.getFor(UnitStatsComponent.class);
	protected ComponentMapper<TextComponent> tComponentMapper = ComponentMapper.getFor(TextComponent.class);
	
	public UserInterfaceState(AssetManager assetManager, SoundSystem soundSystem, UserInterfaceManager userInterfaceManager) {
		this.assetManager = assetManager;
		this.soundSystem = soundSystem;
		this.userInterfaceManager = userInterfaceManager;
	}
	
	/**
	 * Called whenever this becomes the current State
	 */
	public abstract void startState();
	
	/**
	 * Called whenever this is removed FROM the current state
	 */
	public abstract void resetState();
	
	/**
	 * Called whenever the next state is needed
	 */
	public abstract void nextState();
	
	/**
	 * Update input from player
	 * @param delta
	 */
	public abstract void handleInput(float delta);
	
	/**
	 * Called every frame. Use only if needed to run logic every frame
	 * @param delta
	 */
	public abstract void update(float delta);
}
