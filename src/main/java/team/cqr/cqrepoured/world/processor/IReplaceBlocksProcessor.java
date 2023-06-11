package team.cqr.cqrepoured.world.processor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.command.arguments.BlockStateArgument;
import net.minecraft.command.arguments.BlockStateInput;
import team.cqr.cqrepoured.util.CQRWeightedRandom;

public interface IReplaceBlocksProcessor {

	CQRWeightedRandom<BlockState> getResultingBlockStates();
	
	Set<BlockStateInput> getFilters();
	
	@Nullable
	public static BlockStateInput createInputFromString(final String input) {
		if(input == null) {
			return null;
		}
		if(input.isEmpty()) {
			return null;
		}
		
		StringReader sr = new StringReader(input);
		try {
			return BlockStateArgument.block().parse(sr);
		} catch (CommandSyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Nullable
	public static BlockState parseBlockState(final String input) {
		BlockStateInput bsi = createInputFromString(input);
		if(bsi != null) {
			return bsi.getState();
		}
		return null;
	}
	
	public default boolean tryAddFilter(final String input) {
		BlockStateInput tmp = createInputFromString(input);
		if(tmp != null) {
			return this.getFilters().add(tmp);
		}
		return false;
	}
	
	public default boolean tryAddResult(final JsonObject element) {
		if(element == null) {
			return false;
		}
		Set<Map.Entry<String, JsonElement>> entries = element.entrySet();
		if(entries.isEmpty()) {
			return false;
		}
		Map<String, JsonElement> entryMap = new HashMap<>(entries.size());
		entries.forEach(entry -> {
			entryMap.put(entry.getKey(), entry.getValue());
		});
		if(entryMap.containsKey("weight") && entryMap.containsKey("state")) {
			JsonElement weightJO = entryMap.get("weight");
			JsonElement stateJO = entryMap.get("state");
			if(!(weightJO.isJsonPrimitive() && stateJO.isJsonPrimitive())) {
				return false;
			}
			int weight;
			String state;
			try {
				state = stateJO.getAsString();
				weight = weightJO.getAsInt();
			} catch(ClassCastException cce) {
				return false;
			} catch(IllegalStateException ise) {
				return false;
			}
			return this.tryAddResult(state, weight);
		}
		return false;
	}
	
	public default boolean tryAddResult(final String state, final int weight) {
		if(state == null) {
			return false;
		}
		if(state.isEmpty()) {
			return false;
		}
		if(weight < 0 ) {
			return false;
		}
		BlockState replacement = parseBlockState(state);
		if(replacement == null) {
			return false;
		}
		this.getResultingBlockStates().add(replacement, weight);
		return true;
	}

}
