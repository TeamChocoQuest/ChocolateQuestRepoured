package team.cqr.cqrepoured.util;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class JsonUtil {

	public static <T extends JsonElement> Stream<T> stream(JsonArray jsonArray, Class<T> jsonClass) {
		return IntStream.range(0, jsonArray.size())
				.mapToObj(jsonArray::get)
				.filter(jsonClass::isInstance)
				.map(jsonClass::cast);
	}

}
