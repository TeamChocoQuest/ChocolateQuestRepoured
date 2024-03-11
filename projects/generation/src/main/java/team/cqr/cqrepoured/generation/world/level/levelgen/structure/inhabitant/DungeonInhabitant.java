package team.cqr.cqrepoured.generation.world.level.levelgen.structure.inhabitant;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

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
import net.minecraftforge.registries.ForgeRegistries;
import team.cqr.cqrepoured.common.random.CQRWeightedRandom;
import team.cqr.cqrepoured.common.registration.AbstractRegistratableObject;
import team.cqr.cqrepoured.common.serialization.CodecUtil;
import team.cqr.cqrepoured.common.services.CQRServices;
import team.cqr.cqrepoured.init.CQREntityTypes;

public class DungeonInhabitant extends AbstractRegistratableObject {
	
	protected CQRWeightedRandom<EntityType<?>> entities;
	protected Optional<CQRWeightedRandom<EntityType<?>>> bosses;
	protected Optional<ItemStack> customBanner;
	protected Optional<DyeColor> customBannerColor;
	protected Map<EquipmentSlot, CQRWeightedRandom<ItemStack>> equipmentMap;
	protected Optional<Map<ResourceLocation, Integer>> factionOverride;
	
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
		return this.createRandomEntity(random, (type) -> {
			return type.create(level);
		});
	}
	
	public Entity createRandomBossEntity(RandomSource random, final @Nonnull Function<EntityType<?>, Entity> createFunction) {
		if (this.hasConfiguredBosses()) {
			return this.createRandomEntity(random, createFunction, this.bosses.get());
		}
		return null;
	}
	
	public Entity createRandomEntity(RandomSource random, final @Nonnull Function<EntityType<?>, Entity> createFunction) {
		return this.createRandomEntity(random, createFunction, this.entities);
	}
	
	public Entity createRandomEntity(RandomSource random, final @Nonnull Function<EntityType<?>, Entity> createFunction, CQRWeightedRandom<EntityType<?>> typeList) {
		EntityType<?> type = typeList.next(random);
		Entity result = createFunction.apply(type);
		
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
		
		if (this.factionOverride.isPresent() && CQRServices.FACTION.hasFactionCapabiltiy(entity)) {
            this.factionOverride.get().entrySet().forEach(entry -> {
                CQRServices.FACTION.setReputation(entity, entry.getKey(), entry.getValue());
            });
		}

		this.equipmentMap.entrySet().forEach(entry -> {
			ItemStack current = entity.getItemBySlot(entry.getKey());
			if (current == null || current.isEmpty()) {
				entity.setItemSlot(entry.getKey(), entry.getValue().next(random));
			}
		});
		
	}

	public boolean hasConfiguredBosses() {
		return this.bosses != null && this.bosses.isPresent() && this.bosses.get().numItems() > 0;
	}

}
