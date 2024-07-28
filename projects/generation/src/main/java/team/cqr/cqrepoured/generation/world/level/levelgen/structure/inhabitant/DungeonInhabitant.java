package team.cqr.cqrepoured.generation.world.level.levelgen.structure.inhabitant;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import javax.annotation.Nonnull;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraftforge.registries.ForgeRegistries;
import team.cqr.cqrepoured.common.init.CQRCommonEntityTags;
import team.cqr.cqrepoured.common.random.RandomUtil;
import team.cqr.cqrepoured.common.serialization.CodecUtil;
import team.cqr.cqrepoured.common.services.CQRServices;

public class DungeonInhabitant {

	protected SimpleWeightedRandomList<EntityType<?>> entities;
	protected Optional<SimpleWeightedRandomList<EntityType<?>>> bosses;
	protected Optional<ItemStack> customBanner;
	protected Optional<DyeColor> customBannerColor;
	protected Map<EquipmentSlot, SimpleWeightedRandomList<ItemStack>> equipmentMap;
	protected Optional<Map<ResourceLocation, Integer>> factionOverride;

	public static final Codec<DungeonInhabitant> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				SimpleWeightedRandomList.wrappedCodec(ForgeRegistries.ENTITY_TYPES.getCodec()).fieldOf("entities").forGetter(obj -> obj.entities),
				SimpleWeightedRandomList.wrappedCodec(ForgeRegistries.ENTITY_TYPES.getCodec()).optionalFieldOf("bosses").forGetter(obj -> obj.bosses),
				ItemStack.CODEC.optionalFieldOf("banner").forGetter(obj -> obj.customBanner),
				DyeColor.CODEC.optionalFieldOf("banner_color").forGetter(obj -> obj.customBannerColor),
				CodecUtil.stringMap(EquipmentSlot::name, EquipmentSlot::valueOf, SimpleWeightedRandomList.wrappedCodec(ItemStack.CODEC), EquipmentSlot.values()).optionalFieldOf("default_equipment", Map.of()).forGetter(obj -> obj.equipmentMap),
				Codec.unboundedMap(ResourceLocation.CODEC, Codec.INT).optionalFieldOf("reputation_settings").forGetter(obj -> obj.factionOverride))
				.apply(instance, DungeonInhabitant::new);
	});

	public DungeonInhabitant(SimpleWeightedRandomList<EntityType<?>> entities, Optional<SimpleWeightedRandomList<EntityType<?>>> bosses,
			Optional<ItemStack> customBanner, Optional<DyeColor> customBannerColor, Map<EquipmentSlot, SimpleWeightedRandomList<ItemStack>> equipmentMap,
			Optional<Map<ResourceLocation, Integer>> factionOverride) {
		this.entities = entities;
		this.bosses = bosses;
		this.customBanner = customBanner;
		this.customBannerColor = customBannerColor;
		this.equipmentMap = equipmentMap;
		this.factionOverride = factionOverride;
	}

	public void prepareEntityNBT(final CompoundTag tag, RandomSource random, boolean boss) {
		if (tag == null || tag.isEmpty() || !tag.contains("id", Tag.TAG_STRING)) {
			return;
		}
		ResourceLocation id = ResourceLocation.tryParse(tag.getString("id"));
		if (id == null) {
			return;
		}
		EntityType<?> entityType = ForgeRegistries.ENTITY_TYPES.getValue(id);
		// Should NEVER happen
		if (entityType == null) {
			return;
		}
		if (entityType.is(CQRCommonEntityTags.DUMMY_ENTITIES)) {
			EntityType<?> type = null;
			if (boss && this.bosses.isPresent()) {
				type = RandomUtil.getOrThrow(this.bosses.get(), random);
			} else {
				type = RandomUtil.getOrThrow(this.entities, random);
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

	public Entity createRandomEntity(RandomSource random, final @Nonnull Function<EntityType<?>, Entity> createFunction,
			SimpleWeightedRandomList<EntityType<?>> typeList) {
		EntityType<?> type = RandomUtil.getOrThrow(typeList, random);
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

		this.applyFactionOverride(entity);
		this.applyEquipment(entity, random);
	}

	private void applyFactionOverride(Mob entity) {
		if (this.factionOverride.isEmpty()) {
			return;
		}
		for (Map.Entry<ResourceLocation, Integer> entry : this.factionOverride.get().entrySet()) {
			CQRServices.FACTION.setReputation(entity, entry.getKey(), entry.getValue());
		}
	}

	private void applyEquipment(Mob entity, RandomSource random) {
		if (this.equipmentMap.isEmpty()) {
			return;
		}
		for (Map.Entry<EquipmentSlot, SimpleWeightedRandomList<ItemStack>> entry : this.equipmentMap.entrySet()) {
			if (entry.getValue().isEmpty()) {
				continue;
			}
			ItemStack current = entity.getItemBySlot(entry.getKey());
			if (current.isEmpty()) {
				continue;
			}
			entity.setItemSlot(entry.getKey(), RandomUtil.getOrThrow(entry.getValue(), random));
		}
	}

	public boolean hasConfiguredBosses() {
		return this.bosses != null && this.bosses.isPresent() && !this.bosses.get().isEmpty();
	}

}
