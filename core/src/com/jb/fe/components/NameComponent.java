package com.jb.fe.components;

import com.badlogic.ashley.core.Component;

public class NameComponent implements Component{
	
	public String name;
	
	public NameComponent() {
		this.name = "PLACEHOLDER";
	}
	
	public NameComponent(String name) {
		this.name = name;
	}
}
