package com.jb.fe.UI.dialogue;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.jb.fe.UI.UserInterfaceState;
import com.jb.fe.UI.Text.TextObject;
import com.jb.fe.UI.factories.UIFactory;
import com.jb.fe.audio.SoundObject;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.components.TextComponent;
import com.jb.fe.systems.audio.SoundSystem;
import com.jb.fe.systems.inputAndUI.UserInterfaceManager;

/**
 * This state will have all the dialogue text that the characters will use.
 * @author jamesbrouillet
 */

public class DialogueState extends UserInterfaceState {
	
	private Entity dialogueBox;
	private Entity arrow;
	
	private UserInterfaceState nextState;
	
	private SoundObject dialogueSound;
	
	private float currentTimer;
	private float arrowMovementSpeed;
	private float dialogueTextSpeed = 0.022f;
	
	private boolean isWaitingForInput;
	
	private String tempDialogueString;
	private int stringIndex;
	
	public DialogueState(AssetManager assetManager, SoundSystem soundSystem,
			UserInterfaceManager userInterfaceManager, Engine engine) {
		super(assetManager, soundSystem, userInterfaceManager);
		
		dialogueBox = UIFactory.createDialogueBox(assetManager);
		arrow = UIFactory.createArrow(assetManager);
		engine.addEntity(arrow);
		engine.addEntity(dialogueBox);
		
		dialogueSound = new SoundObject("sound/Message.wav", assetManager);
		dialogueSound.delayTimer = 0.15f;
		
		isWaitingForInput = false;
		currentTimer = 0;
		
		arrowMovementSpeed = 0.18f;
		
		tempDialogueString = "TEMP";
		stringIndex = 0;
	}

	@Override
	public void startState() {
		staticImageComponentMapper.get(dialogueBox).isEnabled = true;
		staticImageComponentMapper.get(arrow).isEnabled = true;
		//pComponentMapper.get(dialogueBox).x = 10 + (CameraSystem.cameraX - CameraSystem.xConstant);
		//pComponentMapper.get(dialogueBox).y = 10 + (CameraSystem.cameraY - CameraSystem.yConstant);
		tComponentMapper.get(dialogueBox).isDrawing = true;
		isWaitingForInput = false;
		
		for (TextObject textObject : tComponentMapper.get(dialogueBox).textArray) {
			textObject.isEnabled = false;
		}
	}

	@Override
	public void resetState() {
		isWaitingForInput = false;
		staticImageComponentMapper.get(dialogueBox).isEnabled = false;
		staticImageComponentMapper.get(arrow).isEnabled = false;
		tComponentMapper.get(dialogueBox).isDrawing = false;
		stringIndex = 0;
		tempDialogueString = "TEMP";
		clearDialogue();
	}

	@Override
	public void nextState() {
		userInterfaceManager.setStates(this, nextState);
	}

	@Override
	public void handleInput(float delta) {
		TextComponent textComponent = tComponentMapper.get(dialogueBox);
		if (isWaitingForInput) {
			// Move Arrow
			PositionComponent arrowPosition = pComponentMapper.get(arrow);
			arrowPosition.y += arrowMovementSpeed;
			clampArrow(arrowPosition);
			return;
		}
		
		currentTimer += delta;
		if (currentTimer > dialogueTextSpeed) {
			// Increase letters shown
			if (tempDialogueString.equals("TEMP")){
				tempDialogueString = textComponent.textArray.first().text;
				textComponent.textArray.first().isEnabled = true;
			}
			
			if (stringIndex < tempDialogueString.length()) {
				stringIndex++;
				currentTimer = 0;
				textComponent.textArray.first().text = tempDialogueString.substring(0, stringIndex);
				soundSystem.playSound(dialogueSound);
			}
			
			// Next Object if we are done
			if (stringIndex == tempDialogueString.length()) {
				isWaitingForInput = true;
				stringIndex = 0;
			}
			
			// Next state if we are out of dialogue
			if (textComponent.textArray.size == 0) {
				isWaitingForInput = true;
			}
		}
		
	}
	
	@Override
	public void update(float delta) {
		// Hit Z to advance to the next
		if (Gdx.input.isKeyJustPressed(Keys.Z)) {
			tComponentMapper.get(dialogueBox).textArray.removeFirst();
			stringIndex = 0;
			currentTimer = 0;
			isWaitingForInput = false;
			tempDialogueString = "TEMP";
			if (tComponentMapper.get(dialogueBox).textArray.size == 0) {
				nextState();
			}
		}
	}
	
	public void setNextState(UserInterfaceState nextState) {
		this.nextState = nextState;
	}
	
	private void clampArrow(PositionComponent arrowPosition) {
		if (arrowPosition.y > 13) {
			arrowPosition.y = 13;
			arrowMovementSpeed *= -1;
		} else if (arrowPosition.y < 7){
			arrowPosition.y = 7;
			arrowMovementSpeed *= -1;
		}
	}
	
	/**
	 * Ideal length of String = Seth, it looks like these bandits are attac
	 * @param text Text to Display
	 */
	public void addDialogue(String text) {
		TextObject textObject = new TextObject(20 /*+ (CameraSystem.cameraX - CameraSystem.xConstant)*/, 50 /*+ (CameraSystem.cameraY - CameraSystem.yConstant)*/, text);
		textObject.textFontSize = 0.2f;
		textObject.textColor = Color.BLACK;
		tComponentMapper.get(dialogueBox).textArray.addLast(textObject);
	}
	
	public void clearDialogue() {
		tComponentMapper.get(dialogueBox).textArray.clear();
	}
}
