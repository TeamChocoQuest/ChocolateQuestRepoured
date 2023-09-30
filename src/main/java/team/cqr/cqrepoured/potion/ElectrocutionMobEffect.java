package team.cqr.cqrepoured.potion;

import java.lang.ref.WeakReference;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.tslat.effectslib.api.ExtendedMobEffect;
import net.tslat.effectslib.api.ExtendedMobEffectHolder;
import software.bernie.geckolib.core.object.Color;
import team.cqr.cqrepoured.entity.IMechanical;
import team.cqr.cqrepoured.init.CQRCreatureAttributes;
import team.cqr.cqrepoured.util.EntityUtil;

public class ElectrocutionMobEffect extends ExtendedMobEffect {
	
	public static class SpreadTargetData {
		
		private Optional<UUID> casterUUID = Optional.empty();
		private Optional<UUID> targetUUID = Optional.empty();
		private int remainingTicks = 0;
		private int remainingSpreads = 0;
		private int cooldown = 0;
		
		// Not saved
		private WeakReference<Entity> targetEntity;
		private WeakReference<Entity> casterEntity;
		
		public SpreadTargetData() {
			
		}
		
		public SpreadTargetData(Optional<UUID> caster, Optional<UUID> target, int ticks, int spreads, int cooldown) {
			this.casterUUID = caster;
			this.targetUUID = target;
			this.remainingTicks = ticks;
			this.remainingSpreads = spreads;
			this.cooldown = cooldown;
		}
		
		public Optional<UUID> getCasterUUID() {
			return casterUUID;
		}
		public Optional<UUID> getTargetUUID() {
			return targetUUID;
		}
		public int getRemainingTicks() {
			return remainingTicks;
		}
		public int getRemainingSpreads() {
			return remainingSpreads;
		}
		public int getCooldown() {
			return cooldown;
		}
		
		public Entity getTarget(Level level) {
			if (this.getTargetUUID() == null || this.getTargetUUID().isEmpty()) {
				return null;
			}
			if (this.targetEntity == null) {
				Entity worldEntity = EntityUtil.getEntityByUUID(level, this.getTargetUUID().get());
				this.targetEntity = new WeakReference<Entity>(worldEntity);
				return worldEntity;
			} else {
				return this.targetEntity.get();
			}
		}
		
		public Entity getCaster(Level level) {
			if (this.getCasterUUID() == null || this.getCasterUUID().isEmpty()) {
				return null;
			}
			if (this.targetEntity == null) {
				Entity worldEntity = EntityUtil.getEntityByUUID(level, this.getCasterUUID().get());
				this.casterEntity = new WeakReference<Entity>(worldEntity);
				return worldEntity;
			} else {
				return this.casterEntity.get();
			}
		}
		
		public static final Codec<SpreadTargetData> CODEC = RecordCodecBuilder.create(instance -> {
			return instance.group(
					UUIDUtil.CODEC.optionalFieldOf("caster").forGetter(SpreadTargetData::getCasterUUID),
					UUIDUtil.CODEC.optionalFieldOf("target").forGetter(SpreadTargetData::getTargetUUID),
					Codec.INT.optionalFieldOf("remaining", 0).forGetter(SpreadTargetData::getRemainingTicks),
					Codec.INT.optionalFieldOf("spreads", 0).forGetter(SpreadTargetData::getRemainingSpreads),
					Codec.INT.optionalFieldOf("cooldown", 0).forGetter(SpreadTargetData::getCooldown)
			).apply(instance, SpreadTargetData::new);
		});
	}

	public static final Color COLOR = Color.ofRGBA(39.0F, 251.0F, 244.0F, 0.8F);
	
	public ElectrocutionMobEffect() {
		super(MobEffectCategory.HARMFUL, COLOR.argbInt());
	}
	
	@Override
	public boolean shouldCureEffect(MobEffectInstance effectInstance, ItemStack stack, LivingEntity entity) {
		return false;
	}
	
	@Override
	public void afterOutgoingAttack(LivingEntity entity, LivingEntity victim, MobEffectInstance effectInstance, DamageSource source, float amount) {
		super.afterOutgoingAttack(entity, victim, effectInstance, source, amount);
		// If the attack was successful, maybe electrocute the victim too?
	}
	
	@Override
	public boolean doClientSideEffectTick(MobEffectInstance effectInstance, LivingEntity entity) {
		// We don't want the vanilla particles here, so let's skip them
		return true;
	}
	
	private static SpreadTargetData getSpreadData(LivingEntity entity, @Nonnull MobEffectInstance effectInstance) {
		if (effectInstance instanceof ExtendedMobEffectHolder holder) {
			Object dataObj = holder.getExtendedMobEffectData();
			if (dataObj == null) {
				holder.setExtendedMobEffectData(new SpreadTargetData());
			}
			if (dataObj != null || dataObj instanceof SpreadTargetData) {
				return (SpreadTargetData) dataObj;
			}
		}
		return null;
	}
	
	@Override
	public void tick(LivingEntity entity, @Nullable MobEffectInstance effectInstance, int amplifier) {
		super.tick(entity, effectInstance, amplifier);

		// First: Damage
		final float damage = 1 * amplifier; // TODO: Move base damage into config
		DamageSource ds = entity.level().damageSources().lightningBolt();
		if ((entity instanceof IMechanical || entity.getMobType() == CQRCreatureAttributes.MECHANICAL) && entity.isInWaterOrBubble()) {
			entity.hurt(ds, damage);
		} else {
			entity.hurt(ds, damage);
		}
		// Second: If it has a target capacity => spread
		if (effectInstance != null ) {
			SpreadTargetData data = ElectrocutionMobEffect.getSpreadData(entity, effectInstance);
			
			@Nullable Entity target = data.getTarget(entity.level());
			
			if (target != null) {
				
			} else {
				
			}
		}
	}
	
	@Override
	public void write(CompoundTag nbt, MobEffectInstance effectInstance) {
		super.write(nbt, effectInstance);
		if (effectInstance instanceof ExtendedMobEffectHolder holder && holder.getExtendedMobEffectData() instanceof SpreadTargetData data) {
			DataResult<Tag> dr = SpreadTargetData.CODEC.encodeStart(NbtOps.INSTANCE, data);
			Optional<Tag> optTag = dr.result();
			optTag.ifPresent(tag -> nbt.put("electrocution-data", tag));
		}
	}
	
	@Override
	public void read(CompoundTag nbt, MobEffectInstance effectInstance) {
		super.read(nbt, effectInstance);
		if (effectInstance instanceof ExtendedMobEffectHolder holder && nbt.contains("electrocution-data")) {
			Tag tag = nbt.get("electrocution-data");
			if (tag == null) {
				return;
			}
			DataResult<SpreadTargetData> dr = SpreadTargetData.CODEC.parse(NbtOps.INSTANCE, tag);
			Optional<SpreadTargetData> optObj = dr.result();
			optObj.ifPresent(holder::setExtendedMobEffectData);
		}
	}

}
