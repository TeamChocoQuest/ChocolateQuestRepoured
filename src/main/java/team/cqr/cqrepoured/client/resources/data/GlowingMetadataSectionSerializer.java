package team.cqr.cqrepoured.client.resources.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.resources.data.IMetadataSectionSerializer;
import net.minecraft.util.JSONUtils;
import team.cqr.cqrepoured.client.resources.data.GlowingMetadataSection.Section;
import team.cqr.cqrepoured.util.JsonUtil;

public class GlowingMetadataSectionSerializer implements IMetadataSectionSerializer<GlowingMetadataSection> {

	@Override
	public String getMetadataSectionName() {
		return "glowsections";
	}

	@Override
	public GlowingMetadataSection fromJson(JsonObject jsonobject) {
		if (jsonobject.has("sections")) {
			JsonArray jsonarray = JSONUtils.convertToJsonArray(jsonobject.get("sections"), "sections");
			GlowingMetadataSection result = new GlowingMetadataSection(
					JsonUtil.stream(jsonarray, JsonObject.class).map(jsonObj -> new Section(
							JSONUtils.getAsInt(jsonObj, "x1"), JSONUtils.getAsInt(jsonObj, "y1"),
							JSONUtils.getAsInt(jsonObj, "x2"), JSONUtils.getAsInt(jsonObj, "y2"))));
			if (!result.isEmpty()) {
				return result;
			}
		}
		return null;
	}

}
