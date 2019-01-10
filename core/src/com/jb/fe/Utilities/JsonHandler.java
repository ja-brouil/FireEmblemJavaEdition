package com.jb.fe.Utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

/**
 * Utility to read/parse json.
 * @author james
 *
 */
public final class JsonHandler {

	private static JsonReader jsonReader;
	
	static {
		jsonReader = new JsonReader();
	}

	// Parse file
	public static JsonValue parseJSONFile(String fileLocation) {
		JsonValue jsonValue = jsonReader.parse(Gdx.files.internal(fileLocation).readString());
		return jsonValue;
	}
	
	// Write file
	public static void writeJSONFile() {
		
	}
}
