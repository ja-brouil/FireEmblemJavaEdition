package com.jb.fe.UI;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.jb.fe.audio.SoundObject;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.components.SoundComponent;
import com.jb.fe.components.StaticImageComponent;
import com.jb.fe.components.ZOrderComponent;
import com.jb.fe.screens.FireEmblemGame;
import com.jb.fe.systems.graphics.ZOrderDictionnary;

public class TurnChangeTransitionFactory {

	private AssetManager assetManager;
	
	public TurnChangeTransitionFactory(AssetManager assetManager) {
		this.assetManager = assetManager;
	}

	public Entity createTurnPhase(String fileLocation) {
		Entity transitionPhase = new Entity();
		
		PositionComponent positionComponent = new PositionComponent();
		StaticImageComponent staticImageComponent = new StaticImageComponent(assetManager, fileLocation);
		SoundComponent soundComponent = new SoundComponent();
		ZOrderComponent zOrderComponent = new ZOrderComponent(ZOrderDictionnary.TOP_LAYER);
		
		positionComponent.y = (FireEmblemGame.HEIGHT / 2) - (staticImageComponent.wholeImage.getHeight() / 2);
		staticImageComponent.isEnabled = false;
		staticImageComponent.height = 32;
		staticImageComponent.width = 240;
		soundComponent.allSoundObjects.put("Transition", new SoundObject("sound/Next Turn.wav", assetManager));
		
		
		transitionPhase.add(soundComponent);
		transitionPhase.add(zOrderComponent);
		transitionPhase.add(staticImageComponent);
		transitionPhase.add(positionComponent);
		return transitionPhase;
	}
}
