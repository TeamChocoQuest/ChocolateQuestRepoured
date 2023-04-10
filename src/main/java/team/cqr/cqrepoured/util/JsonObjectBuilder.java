package team.cqr.cqrepoured.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonObjectBuilder {

	private final JsonObject json = new JsonObject();

	public static JsonObjectBuilder builder() {
		return new JsonObjectBuilder();
	}

	public JsonObjectBuilder add(String key, JsonElement value) {
		json.add(key, value);
		return this;
	}

	public JsonObjectBuilder addProperty(String key, Boolean value) {
		json.addProperty(key, value);
		return this;
	}

	public JsonObjectBuilder addProperty(String key, Character value) {
		json.addProperty(key, value);
		return this;
	}

	public JsonObjectBuilder addProperty(String key, Number value) {
		json.addProperty(key, value);
		return this;
	}

	public JsonObjectBuilder addProperty(String key, String value) {
		json.addProperty(key, value);
		return this;
	}

	public JsonObject build() {
		return json;
	}

}
