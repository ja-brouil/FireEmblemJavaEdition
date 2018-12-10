package com.jb.fe.Utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public final class JsonHandler {

	private static JsonReader jsonReader;
	
	static {
		jsonReader = new JsonReader();
	}
	
	public static void parseJSON(String fileLocation) {
		JsonValue jsonValue = jsonReader.parse(Gdx.files.internal(fileLocation).readString());
		System.out.println(jsonValue);
	}
}
