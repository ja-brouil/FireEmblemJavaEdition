package com.jb.fe.UI.combatUIScreen;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Align;
import com.jb.fe.UI.UserInterfaceState;
import com.jb.fe.UI.Text.TextObject;
import com.jb.fe.UI.combatUnitSelector.UnitDamageMenuState;
import com.jb.fe.components.NameComponent;
import com.jb.fe.components.PositionComponent;
import com.jb.fe.components.StaticImageComponent;
import com.jb.fe.components.TextComponent;
import com.jb.fe.components.ZOrderComponent;
import com.jb.fe.screens.FireEmblemGame;
import com.jb.fe.systems.audio.SoundSystem;
import com.jb.fe.systems.camera.CameraSystem;
import com.jb.fe.systems.gamePlay.CombatSystem;
import com.jb.fe.systems.graphics.ZOrder;
import com.jb.fe.systems.inputAndUI.UserInterfaceManager;

public class CombatScreenUI extends UserInterfaceState{
	
	// Entity for combat UI
	private Entity combatScreenEntity;
	
	// All Components
	private StaticImageComponent staticImageComponent;
	private PositionComponent positionComponent;
	private ZOrderComponent zOrderComponent;
	private TextComponent textComponent;

	public CombatScreenUI(AssetManager assetManager, SoundSystem soundSystem,
			UserInterfaceManager userInterfaceManager) {
		super(assetManager, soundSystem, userInterfaceManager);
		
		combatScreenEntity = new Entity();
		
		staticImageComponent = new StaticImageComponent(assetManager, "UI/combatUI/combatUI.png");
		staticImageComponent.width = FireEmblemGame.WIDTH * FireEmblemGame.CONSTANT;
		staticImageComponent.height = FireEmblemGame.HEIGHT * FireEmblemGame.CONSTANT;
		staticImageComponent.isEnabled = false;
		
		positionComponent = new PositionComponent(0,0);
		
		zOrderComponent = new ZOrderComponent(ZOrder.COMBAT_UI);
		
		// Text Order
		// Enemy Name, Enemy Hit, Enemy Damage, Enemy Crit, Enemy Weapon Name, Enemy HP
		// Ally Name, Ally Hit, Ally Dmg, Ally Crit, Ally Weapon Name, Ally HP
		textComponent = new TextComponent();
		textComponent.isDrawing = false;
		textComponent.textArray.addFirst(new TextObject(positionComponent.x + 25, positionComponent.y + (FireEmblemGame.HEIGHT - 13), "Enemy Name", 0.2f, Align.center));
		textComponent.textArray.addLast(new TextObject(positionComponent.x + 27, positionComponent.y + 47, "100", 0.2f, Align.center));
		textComponent.textArray.addLast(new TextObject(positionComponent.x + 27, positionComponent.y + 39, "100", 0.2f, Align.center));
		textComponent.textArray.addLast(new TextObject(positionComponent.x + 27, positionComponent.y + 31, "100", 0.2f, Align.center));
		textComponent.textArray.addLast(new TextObject(positionComponent.x + 80, positionComponent.y + 35, "Swordslayer", 0.19f, Align.center, Color.BLACK));
		textComponent.textArray.addLast(new TextObject(positionComponent.x + 20, positionComponent.y + 15, "HP 100", 0.2f, Align.center));
		textComponent.textArray.addLast(new TextObject(positionComponent.x + (FireEmblemGame.WIDTH - 30), positionComponent.y + (FireEmblemGame.HEIGHT - 13), "Ally Name", 0.2f, Align.center));
		textComponent.textArray.addLast(new TextObject(positionComponent.x + (FireEmblemGame.WIDTH - 14), positionComponent.y + 47, "100", 0.2f, Align.center));
		textComponent.textArray.addLast(new TextObject(positionComponent.x + (FireEmblemGame.WIDTH - 14), positionComponent.y + 39, "100", 0.2f, Align.center));
		textComponent.textArray.addLast(new TextObject(positionComponent.x + (FireEmblemGame.WIDTH - 14), positionComponent.y + 31, "100", 0.2f, Align.center));
		textComponent.textArray.addLast(new TextObject(positionComponent.x + 143, positionComponent.y + 35, "Rapier", 0.19f, Align.center, Color.BLACK));
		textComponent.textArray.addLast(new TextObject(positionComponent.x + 143, positionComponent.y + 15, "HP 100", 0.2f, Align.center));
		textComponent.textArray.forEach((textObject) -> {
			textObject.isEnabled = true;
		});
		
		combatScreenEntity.add(textComponent);
		combatScreenEntity.add(zOrderComponent);
		combatScreenEntity.add(positionComponent);
		combatScreenEntity.add(staticImageComponent);
	}

	@Override
	public void startState() {
		// Enable and set position
		staticImageComponentMapper.get(combatScreenEntity).isEnabled = true;
		
		pComponentMapper.get(combatScreenEntity).x = CameraSystem.cameraX - CameraSystem.xConstant;
		pComponentMapper.get(combatScreenEntity).y = CameraSystem.cameraY - CameraSystem.yConstant;
		
		// Set Text Objects
		textComponent.isDrawing = true;
		
		
		// Enemy
		textComponent.textArray.get(1).text = UnitDamageMenuState.atkHit;
		textComponent.textArray.get(1).x = positionComponent.x + 27;
		textComponent.textArray.get(1).y = positionComponent.y + 47;
		textComponent.textArray.get(2).text = UnitDamageMenuState.atkDmg;
		textComponent.textArray.get(2).x = positionComponent.x + 27;
		textComponent.textArray.get(2).y = positionComponent.y + 39;
		textComponent.textArray.get(3).text = UnitDamageMenuState.atkCrit;
		textComponent.textArray.get(3).x = positionComponent.x + 27;
		textComponent.textArray.get(3).y = positionComponent.y + 31;
		
		// Ally
		textComponent.textArray.get(7).text = UnitDamageMenuState.defHit;
		textComponent.textArray.get(7).x = positionComponent.x + (FireEmblemGame.WIDTH - 14);
		textComponent.textArray.get(7).y = positionComponent.y + 47;
		textComponent.textArray.get(8).text = UnitDamageMenuState.defDmg;
		textComponent.textArray.get(8).x = positionComponent.x + (FireEmblemGame.WIDTH - 14);
		textComponent.textArray.get(8).y = positionComponent.y + 39;
		textComponent.textArray.get(9).text = UnitDamageMenuState.defCrit;
		textComponent.textArray.get(9).x = positionComponent.x + (FireEmblemGame.WIDTH - 14);
		textComponent.textArray.get(9).y = positionComponent.y + 31;
		
		// Set entities
		Entity enemy;
		Entity ally;
		
		// ALLY IS DEFENDING -> USE DEF FOR ALLY | USE ATK FOR ENEMY
		if (mStatComponentMapper.get(CombatSystem.defendingUnit).isAlly) {
			ally = CombatSystem.defendingUnit;
			enemy = CombatSystem.attackingUnit;
			
			// Enemy
			textComponent.textArray.get(1).text = UnitDamageMenuState.atkHit;
			textComponent.textArray.get(1).x = positionComponent.x + 27;
			textComponent.textArray.get(1).y = positionComponent.y + 47;
			textComponent.textArray.get(2).text = UnitDamageMenuState.atkDmg;
			textComponent.textArray.get(2).x = positionComponent.x + 27;
			textComponent.textArray.get(2).y = positionComponent.y + 39;
			textComponent.textArray.get(3).text = UnitDamageMenuState.atkCrit;
			textComponent.textArray.get(3).x = positionComponent.x + 27;
			textComponent.textArray.get(3).y = positionComponent.y + 31;
			
			// Ally
			textComponent.textArray.get(7).text = UnitDamageMenuState.defHit;
			textComponent.textArray.get(7).x = positionComponent.x + (FireEmblemGame.WIDTH - 14);
			textComponent.textArray.get(7).y = positionComponent.y + 47;
			textComponent.textArray.get(8).text = UnitDamageMenuState.defDmg;
			textComponent.textArray.get(8).x = positionComponent.x + (FireEmblemGame.WIDTH - 14);
			textComponent.textArray.get(8).y = positionComponent.y + 39;
			textComponent.textArray.get(9).text = UnitDamageMenuState.defCrit;
			textComponent.textArray.get(9).x = positionComponent.x + (FireEmblemGame.WIDTH - 14);
			textComponent.textArray.get(9).y = positionComponent.y + 31;
			
		} else {
			// ALLY IS ATTACKING -> USE ATK FOR ALLY | USE DEF FOR ENEMY
			ally = CombatSystem.attackingUnit;
			enemy = CombatSystem.defendingUnit;
			
			// Enemy
			textComponent.textArray.get(1).text = UnitDamageMenuState.defHit;
			textComponent.textArray.get(1).x = positionComponent.x + 27;
			textComponent.textArray.get(1).y = positionComponent.y + 47;
			textComponent.textArray.get(2).text = UnitDamageMenuState.defDmg;
			textComponent.textArray.get(2).x = positionComponent.x + 27;
			textComponent.textArray.get(2).y = positionComponent.y + 39;
			textComponent.textArray.get(3).text = UnitDamageMenuState.defCrit;
			textComponent.textArray.get(3).x = positionComponent.x + 27;
			textComponent.textArray.get(3).y = positionComponent.y + 31;
			
			// Ally
			textComponent.textArray.get(7).text = UnitDamageMenuState.atkHit;
			textComponent.textArray.get(7).x = positionComponent.x + (FireEmblemGame.WIDTH - 14);
			textComponent.textArray.get(7).y = positionComponent.y + 47;
			textComponent.textArray.get(8).text = UnitDamageMenuState.atkDmg;
			textComponent.textArray.get(8).x = positionComponent.x + (FireEmblemGame.WIDTH - 14);
			textComponent.textArray.get(8).y = positionComponent.y + 39;
			textComponent.textArray.get(9).text = UnitDamageMenuState.atkCrit;
			textComponent.textArray.get(9).x = positionComponent.x + (FireEmblemGame.WIDTH - 14);
			textComponent.textArray.get(9).y = positionComponent.y + 31;
		}
		
		// Set Enemy name and equipment
		textComponent.textArray.get(0).text = nComponentMapper.get(enemy).name;
		textComponent.textArray.get(0).x = positionComponent.x + 25;
		textComponent.textArray.get(0).y = positionComponent.y + (FireEmblemGame.HEIGHT - 13);
		
		textComponent.textArray.get(4).text = nComponentMapper.get(iComponentMapper.get(enemy).selectedItem).name;
		textComponent.textArray.get(4).x = positionComponent.x + 80;
		textComponent.textArray.get(4).y = positionComponent.y + 35;
		
		textComponent.textArray.get(5).text = "HP " + Integer.toString(uComponentMapper.get(enemy).health);
		textComponent.textArray.get(5).x = positionComponent.x + 20;
		textComponent.textArray.get(5).y = positionComponent.y + 15;
		
		// Set Ally Name and equipment
		textComponent.textArray.get(6).text = nComponentMapper.get(ally).name;
		textComponent.textArray.get(6).x = positionComponent.x + (FireEmblemGame.WIDTH - 30);
		textComponent.textArray.get(6).y = positionComponent.y + (FireEmblemGame.HEIGHT - 13);
		
		textComponent.textArray.get(10).text = nComponentMapper.get(iComponentMapper.get(ally).selectedItem).name;
		textComponent.textArray.get(10).x = positionComponent.x + 147;
		textComponent.textArray.get(10).y = positionComponent.y + 35;
		
		textComponent.textArray.get(11).text = "HP " + Integer.toString(uComponentMapper.get(ally).health);
		textComponent.textArray.get(11).x = positionComponent.x + 143;
		textComponent.textArray.get(11).y = positionComponent.y + 15;
		
		// Set Graphic for HP
		// TO DO
	}

	@Override
	public void resetState() {
		staticImageComponentMapper.get(combatScreenEntity).isEnabled = false;
		textComponent.isDrawing = false;
	}

	@Override
	public void nextState() {
		// Not needed
	}

	@Override
	public void handleInput(float delta) {
		// Not needed
	}
	
	@Override
	public Entity getMainEntity() {
		return this.combatScreenEntity;
	}
	
	public void setHP(Entity ally, Entity enemy) {
		if (mStatComponentMapper.get(ally).isAlly) {
			textComponent.textArray.get(5).text = "HP " + Integer.toString(uComponentMapper.get(enemy).health);
			textComponent.textArray.get(5).x = positionComponent.x + 20;
			textComponent.textArray.get(5).y = positionComponent.y + 15;
			
			textComponent.textArray.get(11).text = "HP " + Integer.toString(uComponentMapper.get(ally).health);
			textComponent.textArray.get(11).x = positionComponent.x + 143;
			textComponent.textArray.get(11).y = positionComponent.y + 15;
		} else {
			textComponent.textArray.get(5).text = "HP " + Integer.toString(uComponentMapper.get(ally).health);
			textComponent.textArray.get(5).x = positionComponent.x + 20;
			textComponent.textArray.get(5).y = positionComponent.y + 15;
			
			textComponent.textArray.get(11).text = "HP " + Integer.toString(uComponentMapper.get(enemy).health);
			textComponent.textArray.get(11).x = positionComponent.x + 143;
			textComponent.textArray.get(11).y = positionComponent.y + 15;
		}
	}
}
