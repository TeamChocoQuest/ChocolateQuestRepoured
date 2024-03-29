package team.cqr.cqrepoured.potion;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.tslat.effectslib.api.ExtendedMobEffect;
import net.tslat.effectslib.api.ExtendedMobEffectHolder;
import mod.azure.azurelib.core.object.Color;
import team.cqr.cqrepoured.api.effect.SynchableMobEffect;
import team.cqr.cqrepoured.common.random.CQRWeightedRandom;
import team.cqr.cqrepoured.entity.IMechanical;
import team.cqr.cqrepoured.faction.IFactionRelated;
import team.cqr.cqrepoured.init.CQRCreatureAttributes;
import team.cqr.cqrepoured.util.EntityUtil;

public class ElectrocutionMobEffect extends ExtendedMobEffect implements SynchableMobEffect {
	
	static final int[] DISTANCE_MAP = generateDistanceMap(5);
	private static int[] generateDistanceMap(int i) {
		int[] result = new int[i];
		
		for (int j = 0; j < i; j++) {
			final int value = 8 * (j + 1);
			result[j] = value * value;
		}
		
		return result;
	}
	
	public static class SpreadTargetData {
		
		private Optional<UUID> casterUUID = Optional.empty();
		private Optional<UUID> targetUUID = Optional.empty();
		private int remainingTicks = 0;
		private int remainingSpreads = 0;
		private int cooldown = 0;
		
		// Not saved
		private WeakReference<LivingEntity> targetEntity;
		private WeakReference<LivingEntity> casterEntity;
		
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
		
		public LivingEntity getTarget(Level level) {
			if (this.getTargetUUID() == null || this.getTargetUUID().isEmpty()) {
				return null;
			}
			if (this.targetEntity == null) {
				Entity worldEntity = EntityUtil.getEntityByUUID(level, this.getTargetUUID().get());
				if (worldEntity instanceof LivingEntity le) {
					this.targetEntity = new WeakReference<LivingEntity>(le);
					return le;
				}
				this.targetUUID = Optional.empty();
				return null;
			} else {
				return this.targetEntity.get();
			}
		}
		
		public Entity getCaster(Level level) {
			if (this.getCasterUUID() == null || this.getCasterUUID().isEmpty()) {
				return null;
			}
			if (this.casterEntity == null) {
				Entity worldEntity = EntityUtil.getEntityByUUID(level, this.getCasterUUID().get());
				if (worldEntity instanceof LivingEntity le) {
					this.casterEntity = new WeakReference<LivingEntity>(le);
					return le;
				}
				this.casterUUID = Optional.empty();
				return null;
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

		public void reset() {
			this.targetUUID = Optional.empty();
			this.remainingTicks = 0;
			this.cooldown = 10;
			this.remainingSpreads += 1;
		}

		public void tick() {
			if (this.cooldown > 0) {
				this.cooldown--;
			}
			if (this.remainingTicks > 0) {
				this.remainingTicks--;
			}
			
			// Reset target if the remaining ticks are 0
			if (this.remainingTicks <= 0 || this.cooldown > 0) {
				this.targetUUID = Optional.empty();
			}
		}
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
		Entity caster = null;
		if (effectInstance != null) {
			SpreadTargetData data = ElectrocutionMobEffect.getSpreadData(entity, effectInstance);
			caster = data.getCaster(entity.level());
		}
		DamageSource ds = entity.damageSources().source(DamageTypes.LIGHTNING_BOLT, caster);
		if ((entity instanceof IMechanical || entity.getMobType() == CQRCreatureAttributes.MECHANICAL) && entity.isInWaterOrBubble()) {
			entity.hurt(ds, damage);
		} else {
			entity.hurt(ds, damage);
		}
		// Second: If it has a target capacity => spread
		if (effectInstance != null ) {
			SpreadTargetData data = ElectrocutionMobEffect.getSpreadData(entity, effectInstance);
			
			data.tick();
			
			@Nullable LivingEntity target = data.getTarget(entity.level());
			
			if (target != null) {
				// Validate target
				// If there is no line of sight => cut the connection
				// If it is dead => Yeah no
				// If it is too far away => Cut
				if (!target.isAlive() || !entity.hasLineOfSight(target) || entity.distanceToSqr(target) >= getMaxDistanceForAmplifier(amplifier) ) {
					data.reset();
				} else {
					// Target is still valid or roughly valid
					// Check for the effect on the target and update it accordingly
					addOrUpdateChainedEffect(target, data, this, effectInstance);
				}
			} else {
				// No target
				// Check if you can actually choose a target and spread
				if (effectInstance.isInfiniteDuration() || effectInstance.getDuration() >= 20 /*TODO: Config entry*/) {
					// Effect will last long enough
					if (data.getRemainingSpreads() != 0 && data.getCooldown() <= 0 && data.getRemainingTicks() == 0) {
						// Enough spreads remain and there is no cooldown
						// Let's search for a valid target
						// If we found one, set it
						target = getTarget(entity, this, data);
						data.targetUUID = Optional.ofNullable(target.getUUID());
						data.remainingSpreads--;
						// TODO: Move this to config entries
						data.remainingTicks = 100 + entity.getRandom().nextInt(100);
					}
				}
			}
		}
	}
	
	private static LivingEntity getTarget(LivingEntity entity, MobEffect effect, SpreadTargetData data) {
		List<LivingEntity> possibleTargets = entity.level().getNearbyEntities(LivingEntity.class, TargetingConditions.forCombat().range(16).selector(new Predicate<LivingEntity>() {
			@Override
			public boolean test(LivingEntity le) {
				MobEffectInstance mei = le.getEffect(effect);
				if (mei != null && mei instanceof ExtendedMobEffectHolder emeh) {
					// Only valid if it has no chained target
					Object addData = emeh.getExtendedMobEffectData();
					if (addData != null && addData instanceof SpreadTargetData std) {
						if (std.getTargetUUID().isPresent()) {
							return false;
						}
					}
				}
				// Also check factions
				if (le instanceof IFactionRelated ifr) {
					// If it is an ally or member of the original caster => no spread
					if (ifr.isAlly(data.getCaster(le.level()))) {
						return false;
					}
				}
				return true;
			}
		}), entity, entity.getBoundingBox().inflate(8, 4, 8));
		
		CQRWeightedRandom<LivingEntity> wr = new CQRWeightedRandom<>();
		possibleTargets.forEach(le -> {
			wr.add(le, (int) Math.round(entity.distanceToSqr(le)));
		});
		
		return wr.next(entity.getRandom());
	}

	protected static void addOrUpdateChainedEffect(LivingEntity target, SpreadTargetData data, ElectrocutionMobEffect effect, MobEffectInstance casterInstance) {
		MobEffectInstance instance = target.getEffect(effect);
		if (instance == null) {
			instance = new MobEffectInstance(effect, 20, Math.max(casterInstance.getAmplifier() - 1, 1), casterInstance.isAmbient(), casterInstance.isVisible(), casterInstance.showIcon());
			if (instance instanceof ExtendedMobEffectHolder emeh) {
				SpreadTargetData targetData = new SpreadTargetData(Optional.ofNullable(data.getCasterUUID().get()), Optional.empty(), 0, data.getRemainingSpreads() - 1, 100);
				((ExtendedMobEffectHolder) instance).setExtendedMobEffectData(targetData);
				
				target.addEffect(instance);
			}
		} else if(instance instanceof ExtendedMobEffectHolder emeh) {
			// TODO: Reset duration
		}
	}

	private double getMaxDistanceForAmplifier(final int amplifier) {
		int index = Mth.clamp(amplifier, 0, DISTANCE_MAP.length - 1);
		return DISTANCE_MAP[index];
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
