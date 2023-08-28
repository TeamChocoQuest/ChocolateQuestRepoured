package team.cqr.cqrepoured.capability.faction;

import java.util.Map;
import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.faction.Faction;

public class FactionRelationCapabilityImplementation implements IFactionRelationCapability {
	
	private Map<ResourceLocation, Integer> reputationStorage = new Object2IntArrayMap<>(0);
	private static Codec<Map<ResourceLocation, Integer>> MAP_CODEC = Codec.unboundedMap(ResourceLocation.CODEC, Codec.INT);

	@Override
	public boolean hasInformationFor(Faction faction) {
		return this.reputationStorage.containsKey(faction.getId());
	}

	@Override
	public int getRelationFor(Faction faction) {
		return this.reputationStorage.getOrDefault(faction.getId(), 0);
	}

	@Override
	public CompoundTag serializeNBT() {
		DataResult<Tag> dr = MAP_CODEC.encodeStart(NbtOps.INSTANCE, this.reputationStorage);
		Optional<Tag> optResult = dr.result();
		if (optResult.isPresent()) {
			CompoundTag result = new CompoundTag();
			result.put("value", optResult.get());
			return result;
		} else {
			return new CompoundTag();
		}
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		if (nbt.isEmpty()) {
			return;
		}
		Tag tag = nbt.get("value");
		DataResult<Map<ResourceLocation, Integer>> dr = MAP_CODEC.parse(NbtOps.INSTANCE, tag);
		Optional<Map<ResourceLocation, Integer>> optResult = dr.result();
		if (optResult.isPresent()) {
			this.reputationStorage = optResult.get();
		}
	}

	@Override
	public void setReputationTowards(ResourceLocation id, int value) {
		this.reputationStorage.put(id, value);
	}


}
