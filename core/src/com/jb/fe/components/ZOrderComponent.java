package com.jb.fe.components;

import com.badlogic.ashley.core.Component;
import com.jb.fe.systems.graphics.ZOrderLevel;

public class ZOrderComponent implements Component{

	public int zOrder;
	
	public ZOrderComponent() {
		zOrder = ZOrderLevel.MIDDLE_LAYER;
	}
	
	public ZOrderComponent(int zOrder) {
		this.zOrder = zOrder;
	}
}
