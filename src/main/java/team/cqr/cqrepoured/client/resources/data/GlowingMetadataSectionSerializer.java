package team.cqr.cqrepoured.client.resources.data;

import com.google.gson.*;
import net.minecraft.resources.data.IMetadataSectionSerializer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.Tuple;

import java.lang.reflect.Type;

// TODO: Change to be completely json based, not some weird string monsters
public class GlowingMetadataSectionSerializer implements JsonSerializer<GlowingMetadataSection>, IMetadataSectionSerializer<GlowingMetadataSection> {

	@Override
	public String getMetadataSectionName() {
		return "glowsections";
	}

	@Override
	public GlowingMetadataSection fromJson(JsonObject jsonobject) {
		//JsonObject jsonobject = JSONUtils.convertToJsonObject(jsonelement, "metadata section");
		if (jsonobject.has("sections")) {
			JsonArray jsonarray = JSONUtils.convertToJsonArray(jsonobject.get("sections"), "sections");
			GlowingMetadataSection result = new GlowingMetadataSection();
			for (int i = 0; i < jsonarray.size(); ++i) {
				JsonElement jsonelement = jsonarray.get(i);

				int x1 = JSONUtils.getAsInt(jsonelement.getAsJsonObject(), "x1", 0);
				int y1 = JSONUtils.getAsInt(jsonelement.getAsJsonObject(), "y1", 0);
				int x2 = JSONUtils.getAsInt(jsonelement.getAsJsonObject(), "x2", 0);
				int y2 = JSONUtils.getAsInt(jsonelement.getAsJsonObject(), "y2", 0);

				Tuple<Integer, Integer> pos1 = new Tuple<>(x1, y1);
				Tuple<Integer, Integer> pos2 = new Tuple<>(x2, y2);
				if (pos1.getA() <= pos2.getA() && pos1.getB() <= pos2.getB()) {
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
				jsonobject1.addProperty("x1", (entry.getA().getA()));
				jsonobject1.addProperty("y1", (entry.getA().getB()));
				jsonobject1.addProperty("x2", (entry.getB().getA()));
				jsonobject1.addProperty("y2", (entry.getB().getB()));

				jsonarray.add(jsonobject1);
			}

			jsonobject.add("sections", jsonarray);
		}

		return jsonobject;
	}

}
