package team.cqr.cqrepoured.world.structure.generation.inhabitants;

import java.util.Map;
import java.util.Optional;

import javax.annotation.Nonnull;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ForgeRegistries;
import team.cqr.cqrepoured.capability.faction.IFactionRelationCapability;
import team.cqr.cqrepoured.init.CQRCapabilities;
import team.cqr.cqrepoured.init.CQREntityTypes;
import team.cqr.cqrepoured.serialization.CodecUtil;
import team.cqr.cqrepoured.util.CQRWeightedRandom;
import team.cqr.cqrepoured.util.registration.AbstractRegistratableObject;

public class DungeonInhabitant extends AbstractRegistratableObject {
	
	CQRWeightedRandom<EntityType<?>> entities;
	Optional<CQRWeightedRandom<EntityType<?>>> bosses;
	Optional<ItemStack> customBanner;
	Optional<DyeColor> customBannerColor;
	Map<EquipmentSlot, CQRWeightedRandom<ItemStack>> equipmentMap;
	Optional<Map<ResourceLocation, Integer>> factionOverride;
	
	public static final Codec<DungeonInhabitant> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				CQRWeightedRandom.createCodec(ForgeRegistries.ENTITY_TYPES.getCodec()).fieldOf("entities").forGetter(obj -> obj.entities),
				CQRWeightedRandom.createCodec(ForgeRegistries.ENTITY_TYPES.getCodec()).optionalFieldOf("bosses").forGetter(obj -> obj.bosses),
				ItemStack.CODEC.optionalFieldOf("banner").forGetter(obj -> obj.customBanner),
				DyeColor.CODEC.optionalFieldOf("banner-color").forGetter(obj -> obj.customBannerColor),
				Codec.unboundedMap(CodecUtil.EQUIPMENT_SLOT_CODEC, CQRWeightedRandom.createCodec(ItemStack.CODEC)).optionalFieldOf("default-equipment", Map.of()).forGetter(obj -> obj.equipmentMap),
				Codec.unboundedMap(ResourceLocation.CODEC, Codec.INT).optionalFieldOf("reputation-settings").forGetter(obj -> obj.factionOverride)
			).apply(instance, DungeonInhabitant::new);
	});
	
	public DungeonInhabitant(CQRWeightedRandom<EntityType<?>> entities,	Optional<CQRWeightedRandom<EntityType<?>>> bosses, Optional<ItemStack> customBanner, Optional<DyeColor> customBannerColor, Map<EquipmentSlot, CQRWeightedRandom<ItemStack>> equipmentMap, Optional<Map<ResourceLocation, Integer>> factionOverride) {
		super();
		
		this.entities = entities;
		this.bosses = bosses;
		this.customBanner = customBanner;
		this.customBannerColor = customBannerColor;
		this.equipmentMap = equipmentMap;
		this.factionOverride = factionOverride;
	}
	
	public void prepareEntityNBT(final CompoundTag tag, RandomSource random, boolean boss) {
		if (tag == null || tag.isEmpty()) {
			return;
		}
		if (tag.getString("id").equals(CQREntityTypes.DUMMY.getId().toString())) {
			EntityType<?> type = null;
			if (boss && this.bosses.isPresent()) {
				type = this.bosses.get().next(random);
			} else {
				type = this.entities.next(random);
			}
			tag.putString("id", ForgeRegistries.ENTITY_TYPES.getKey(type).toString());
		}
	}
	
	public Entity createRandomEntity(RandomSource random, final @Nonnull Level level) {
		EntityType<?> type = this.entities.next(random);
		Entity result = type.create(level);
		
		if (result instanceof Mob mob) {
			this.prepare(mob, random);
		}
		
		return result;
	}
	
	public void prepare(final BannerBlockEntity banner) {
		if (banner == null || this.customBanner.isEmpty()) {
			return;
		}
		if (this.customBannerColor.isPresent()) {
			banner.fromItem(this.customBanner.get(), this.customBannerColor.get());
		} else {
			banner.fromItem(this.customBanner.get());
		}
	}
	
	public void prepare(final Mob entity, RandomSource random) {
		if (entity == null) {
			return;
		}
		
		if (this.factionOverride.isPresent()) {
			LazyOptional<IFactionRelationCapability> lOpCap = entity.getCapability(CQRCapabilities.FACTION_RELATION);
			if (lOpCap.isPresent()) {
				Optional<IFactionRelationCapability> opCap = lOpCap.resolve();
				if (opCap.isPresent()) {
					IFactionRelationCapability relationCap = opCap.get();
					this.factionOverride.get().entrySet().forEach(entry -> {
						relationCap.setReputationTowards(entry.getKey(), entry.getValue());
					});
				}
			}
		}

		this.equipmentMap.entrySet().forEach(entry -> {
			ItemStack current = entity.getItemBySlot(entry.getKey());
			if (current == null || current.isEmpty()) {
				entity.setItemSlot(entry.getKey(), entry.getValue().next(random));
			}
		});
		
	}

}
