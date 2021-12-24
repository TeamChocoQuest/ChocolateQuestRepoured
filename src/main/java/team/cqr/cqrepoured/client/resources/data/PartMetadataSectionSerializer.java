package team.cqr.cqrepoured.client.resources.data;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import net.minecraft.client.resources.data.BaseMetadataSectionSerializer;

public class PartMetadataSectionSerializer extends BaseMetadataSectionSerializer<PartMetadataSection> {

	@Override
	public String getSectionName() {
		return "parts";
	}

	@Override
	public PartMetadataSection deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonArray jsonarray = json.getAsJsonObject().getAsJsonArray("parts");
		Collection<String> parts = IntStream.range(0, jsonarray.size())
				.mapToObj(jsonarray::get)
				.map(JsonElement::getAsString)
				.collect(Collectors.toList());
		return new PartMetadataSection(parts);
	}

}
