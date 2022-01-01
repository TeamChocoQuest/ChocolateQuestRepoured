package team.cqr.cqrepoured.client.resources.data;

import java.lang.reflect.Type;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import net.minecraft.client.resources.data.BaseMetadataSectionSerializer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.Tuple;

// TODO: Change to be completely json based, not some weird string monsters
public class GlowingMetadataSectionSerializer extends BaseMetadataSectionSerializer<GlowingMetadataSection> implements JsonSerializer<GlowingMetadataSection> {

	@Override
	public String getSectionName() {
		return "glowsections";
	}

	@Override
	public GlowingMetadataSection deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject jsonobject = JSONUtils.getJsonObject(json, "metadata section");
		if (jsonobject.has("sections")) {
			JsonArray jsonarray = JSONUtils.getJsonArray(jsonobject, "sections");
			GlowingMetadataSection result = new GlowingMetadataSection();
			for (int i = 0; i < jsonarray.size(); ++i) {
				JsonElement jsonelement = jsonarray.get(i);

				int x1 = JSONUtils.getInt(jsonelement.getAsJsonObject(), "x1", 0);
				int y1 = JSONUtils.getInt(jsonelement.getAsJsonObject(), "y1", 0);
				int x2 = JSONUtils.getInt(jsonelement.getAsJsonObject(), "x2", 0);
				int y2 = JSONUtils.getInt(jsonelement.getAsJsonObject(), "y2", 0);

				Tuple<Integer, Integer> pos1 = new Tuple<>(x1, y1);
				Tuple<Integer, Integer> pos2 = new Tuple<>(x2, y2);
				if (pos1.getFirst() <= pos2.getFirst() && pos1.getSecond() <= pos2.getSecond()) {
					result.addSection(pos1, pos2);
				}

			}
			if (!result.isEmpty()) {
				return result;
			}
		}
		return null;
	}

	@Override
	public JsonElement serialize(GlowingMetadataSection src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject jsonobject = new JsonObject();

		if (!src.isEmpty()) {
			JsonArray jsonarray = new JsonArray();

			for (Tuple<Tuple<Integer, Integer>, Tuple<Integer, Integer>> entry : src.getGlowingSections()) {
				JsonObject jsonobject1 = new JsonObject();
				jsonobject1.addProperty("x1", (entry.getFirst().getFirst()));
				jsonobject1.addProperty("y1", (entry.getFirst().getSecond()));
				jsonobject1.addProperty("x2", (entry.getSecond().getFirst()));
				jsonobject1.addProperty("y2", (entry.getSecond().getSecond()));

				jsonarray.add(jsonobject1);
			}

			jsonobject.add("sections", jsonarray);
		}

		return jsonobject;
	}

}
