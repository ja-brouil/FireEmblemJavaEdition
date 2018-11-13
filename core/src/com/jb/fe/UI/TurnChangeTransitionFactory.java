package com.jb.fe.UI;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.components.StaticImageComponent;
import com.jb.fe.components.TransitionMethodComponent;

public class TurnChangeTransitionFactory {

	private AssetManager assetManager;
	
	public TurnChangeTransitionFactory(AssetManager assetManager) {
		this.assetManager = assetManager;
	}

	public Entity createTurnPhase(String fileLocation) {
		Entity transitionPhase = new Entity();
		
		PositionComponent positionComponent = new PositionComponent();
		StaticImageComponent staticImageComponent = new StaticImageComponent(assetManager, fileLocation);
		TransitionMethodComponent transitionMethodComponent = new TransitionMethodComponent(3);
		
		
		transitionPhase.add(transitionMethodComponent);
		transitionPhase.add(staticImageComponent);
		transitionPhase.add(positionComponent);
		return transitionPhase;
	}
}
