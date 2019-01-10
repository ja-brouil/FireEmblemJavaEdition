package com.jb.fe.systems.gamePlay;

import com.badlogic.gdx.utils.JsonValue;
import com.jb.fe.Utilities.JsonHandler;

/**
 * Contains all the items
 * @author james
 */
public class ItemManager {
	
	private static JsonValue allItems;
	private static String jsonItemFileLocationString = "items/ItemList.json";
	
	static {
		allItems = JsonHandler.parseJSONFile(jsonItemFileLocationString);
	}
	
	/*
	 * Returns an item from the item list
	 * @param key : Name of the item
	 */
	public static JsonValue getItem(String key) {
		return allItems.get(key);
	}
}
