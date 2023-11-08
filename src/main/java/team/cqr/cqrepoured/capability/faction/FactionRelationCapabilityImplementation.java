package team.cqr.cqrepoured.capability.faction;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import team.cqr.cqrepoured.faction.EReputationState;
import team.cqr.cqrepoured.faction.EReputationState.EReputationStateRough;
import team.cqr.cqrepoured.faction.EntityFactionInformation;
import team.cqr.cqrepoured.faction.Faction;
import team.cqr.cqrepoured.init.CQRDatapackLoaders;

public class FactionRelationCapabilityImplementation implements IFactionRelationCapability {
	
	private WeakReference<Entity> holder = null;
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

	@Override
	public EReputationStateRough getRoughReputationOf(Entity other) {
		Entity holder = this.getHolder();
		if (holder != null) {
			EntityFactionInformation efi = CQRDatapackLoaders.getEntityFactionInformation(holder.getType());
			if (efi != null) {
				Map<Faction, EReputationState> repMap = new HashMap<>(this.reputationStorage.size());
				this.reputationStorage.forEach((id, val) -> {
					CQRDatapackLoaders.getFaction(id).ifPresent(f -> {
						repMap.putIfAbsent(f, EReputationState.getByInt(val));
					});
				});
				return efi.getRoughReputationOf(other, repMap);
			}
			// No faction information for us => checking is not worth it
		}
		return EReputationStateRough.NEUTRAL;
	}

	@Override
	public Entity getHolder() {
		if (this.holder != null) {
			return this.holder.get();
		}
		return null;
	}

	@Override
	public void setHolder(Entity value) {
		this.holder = new WeakReference<>(value);
	}

}
