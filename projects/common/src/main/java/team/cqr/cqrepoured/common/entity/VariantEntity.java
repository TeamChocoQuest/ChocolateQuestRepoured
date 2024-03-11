package team.cqr.cqrepoured.common.entity;

import java.util.Optional;

import com.mojang.serialization.DataResult;

import de.dertoaster.multihitboxlib.api.ICustomHitboxProfileSupplier;
import de.dertoaster.multihitboxlib.entity.hitbox.HitboxProfile;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.VariantHolder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent.FinalizeSpawn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import team.cqr.cqrepoured.common.CQRConstants;
import team.cqr.cqrepoured.common.CQRepoured;
import team.cqr.cqrepoured.common.datapack.EntityProfileDatapackRegistries;
import team.cqr.cqrepoured.common.entity.profile.EntityProfile;
import team.cqr.cqrepoured.common.entity.profile.variant.AssetEntry;
import team.cqr.cqrepoured.common.entity.profile.variant.AttributeEntry;
import team.cqr.cqrepoured.common.entity.profile.variant.DamageCap;
import team.cqr.cqrepoured.common.entity.profile.variant.DamageEntry;
import team.cqr.cqrepoured.common.entity.profile.variant.EntityVariant;
import team.cqr.cqrepoured.common.entity.profile.variant.SizeEntry;
import team.cqr.cqrepoured.common.reference.WeakReferenceLazyLoadField;

public class VariantEntity extends Monster implements VariantHolder<EntityVariant>, ICustomHitboxProfileSupplier {
	
	/*
	 * Variants can change the following things:
	 *  - size
	 *  - damage (weaknesses and resistances)
	 *  - attributes
	 *  - model, texture, animations
	 */
	
	// Forge crap
	@EventBusSubscriber(modid = CQRepoured.MODID, bus = Bus.FORGE)
	public static class EventListener {
		
		@SubscribeEvent
		public static void onFinalizeSpawn(FinalizeSpawn event) {
			if (event.getEntity() instanceof VariantEntity ve) {
				EntityType<?> type = ve.getType();
				Optional<EntityProfile> profile = EntityProfileDatapackRegistries.getProfile(type, event.getLevel().registryAccess());
				if (profile.isPresent()) {
					EntityVariant variant = profile.get().getRandomVariant(ve.getRandom());
					if (variant != null) {
						ve.setVariant(variant);
						
						if (variant.hasAssetVariants()) {
							ve.assetEntry = variant.getRandomAssetIndex(ve.getRandom());
						}
						
						// And now: Attributes
						for (AttributeEntry entry : variant.attributes()) {
							Attribute attribute = entry.attribute();
							double value = entry.value();
							
							if (ve.getAttribute(attribute) == null) {
								// TODO: Warning
							} else {
								ve.getAttribute(attribute).setBaseValue(value);
							}
						}
					}
				}
			}
		}
		
		public static void onEntitySize(EntityEvent.Size event) {
			Entity entity = event.getEntity();
			if (entity != null && entity instanceof VariantEntity ve) {
				if (ve.getVariant() == null || ve.getVariant().size() == null) {
					return;
				}
				SizeEntry sizeEntry = ve.getVariant().size();
				switch(event.getPose()) {
				case CROUCHING:
					event.setNewSize(EntityDimensions.scalable(sizeEntry.width(), sizeEntry.height()).scale(ve.getScale()).scale(1F, 0.75F), true);
					break;
				case SITTING:
					event.setNewSize(EntityDimensions.scalable(sizeEntry.width(), sizeEntry.height()).scale(ve.getScale()).scale(1F, 0.66F), true);
					break;
				case DIGGING:
				case EMERGING:
					event.setNewSize(EntityDimensions.scalable(sizeEntry.width(), sizeEntry.height()).scale(ve.getScale()).scale(1F, 0.5F), true);
					break;
				case SLEEPING:
					event.setNewSize(EntityDimensions.scalable(sizeEntry.width(), sizeEntry.height()).scale(ve.getScale()).scale(1F, 0.5F), true);
					break;
				default:
					event.setNewSize(EntityDimensions.scalable(sizeEntry.width(), sizeEntry.height()).scale(ve.getScale()), true);
					break;
				}
				
			}
		}
		
	}
	
	private EntityVariant variant;
	private int assetEntry = -1;
	private WeakReferenceLazyLoadField<AssetEntry> assetEntryCache = new WeakReferenceLazyLoadField<>(this::loadAssetEntryLazily);
	
	private AssetEntry loadAssetEntryLazily() {
		if (this.assetEntry < 0) {
			return null;
		}
		if (this.getVariant() == null || !this.getVariant().hasAssetVariants()) {
			return null;
		}
		AssetEntry result = this.getVariant().getAssetAt(this.assetEntry);
		// Out of list
		if (result == null) {
			this.assetEntry = this.getVariant().getRandomAssetIndex(this.getRandom());
			result = this.getVariant().getAssetAt(this.assetEntry);
		}
		return result;
	}
	
	@Override
	public boolean hurt(DamageSource pSource, float pAmount) {
		if (this.getVariant() == null) {
			return super.hurt(pSource, pAmount);
		}
		// Otherwise, we'll need to intercept
		
		DamageEntry damageConfig = this.getVariant().damageConfig();
		// First: damage multipliers
		if (pSource.is(DamageTypeTags.IS_FIRE) && damageConfig.fireImmune()) {
			return false;
		}
		RegistryAccess access = this.level().registryAccess();
		Float value = damageConfig.damageTypeMultipliers().getOrDefault(access.registryOrThrow(Registries.DAMAGE_TYPE).getKey(pSource.type()), null);
		if (value != null) {
			pAmount *= value.floatValue();
		}
		
		// Second: min damage
		if (pAmount < damageConfig.minDamage()) {
			return false;
		}
		
		// Third: damage cap
		if (damageConfig.damageCap().isPresent()) {
			DamageCap cap = damageConfig.damageCap().get();
			pAmount = cap.capDamage(pAmount, this::getMaxHealth);
		}
		
		return super.hurt(pSource, pAmount);
	}
	
	public Optional<AssetEntry> getClientOverrides() {
		if (this.assetEntry >= 0 && this.getVariant() != null && this.assetEntryCache != null) {
			return Optional.ofNullable(this.assetEntryCache.get());
		}
		return Optional.empty();
	}

	public VariantEntity(EntityType<? extends VariantEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag pCompound) {
		super.addAdditionalSaveData(pCompound);
		
		if (this.variant != null) {
			DataResult<Tag> dataResult = EntityVariant.CODEC.encodeStart(NbtOps.INSTANCE, this.getVariant());
			Optional<Tag> optResult = dataResult.result();
			if (optResult.isPresent()) {
				pCompound.put(CQRConstants.NBT.KEY_ENTITY_VARIANT, optResult.get());
			} 
		}
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag pCompound) {
		super.readAdditionalSaveData(pCompound);
		
		if (pCompound.contains(CQRConstants.NBT.KEY_ENTITY_VARIANT)) {
			Tag nbtTag = pCompound.get(CQRConstants.NBT.KEY_ENTITY_VARIANT);
			DataResult<EntityVariant> dataResult = EntityVariant.CODEC.parse(NbtOps.INSTANCE, nbtTag);
			Optional<EntityVariant> optResult = dataResult.result();
			if (!optResult.isEmpty()) {
				this.setVariant(optResult.get());
			}
		}
		if (pCompound.contains(CQRConstants.NBT.KEY_ENTITY_VARIANT_ASSETS, Tag.TAG_INT)) {
			
		}
	}
	
	@Override
	public void setVariant(EntityVariant pVariant) {
		this.variant = pVariant;
	}

	@Override
	public EntityVariant getVariant() {
		return this.variant;
	}
	
	public Optional<HitboxProfile> getHitboxProfile() {
		if (this.getVariant() != null && this.getVariant().getOptHitboxProfile() != null) {
			return this.getVariant().getOptHitboxProfile();
		}
		return Optional.empty();
	}
	
}
