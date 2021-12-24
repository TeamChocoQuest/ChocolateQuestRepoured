package team.cqr.cqrepoured.client.resources.data;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.stream.Collectors;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.client.resources.data.BaseMetadataSectionSerializer;
import team.cqr.cqrepoured.client.resources.data.GlowingMetadataSection.Section;
import team.cqr.cqrepoured.util.JsonUtil;

public class GlowingMetadataSectionSerializer extends BaseMetadataSectionSerializer<GlowingMetadataSection> {

	@Override
	public String getSectionName() {
		return "glowsections";
	}

	@Override
	public GlowingMetadataSection deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject jsonObject = json.getAsJsonObject();
		JsonArray sectionsJsonArray = jsonObject.getAsJsonArray("sections");
		JsonArray partsJsonArray = jsonObject.getAsJsonArray("parts");

		Collection<Section> sections = JsonUtil.stream(sectionsJsonArray, JsonObject.class).map(Section::new).collect(Collectors.toList());
		Collection<String> parts = JsonUtil.stream(partsJsonArray, JsonElement.class).map(JsonElement::getAsString).collect(Collectors.toList());

		return new GlowingMetadataSection(sections, parts);
	}

}
