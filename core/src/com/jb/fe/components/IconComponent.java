package com.jb.fe.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

/**
 * Wrapper class for Icons so that they can be attached to entities
 * @author james
 *
 */
public class IconComponent implements Component{
	
	public Entity iconEntity;
	
	public IconComponent() {}
	public IconComponent(Entity iconEntity) {
		this.iconEntity = iconEntity;
	}
}
