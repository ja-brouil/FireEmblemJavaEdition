package com.jb.fe.components;

import com.badlogic.ashley.core.Component;
import com.jb.fe.systems.graphics.ZOrder;

public class ZOrderComponent implements Component{

	public int zOrder;
	
	public ZOrderComponent() {
		zOrder = ZOrder.MIDDLE_LAYER;
	}
	
	public ZOrderComponent(int zOrder) {
		this.zOrder = zOrder;
	}
}
