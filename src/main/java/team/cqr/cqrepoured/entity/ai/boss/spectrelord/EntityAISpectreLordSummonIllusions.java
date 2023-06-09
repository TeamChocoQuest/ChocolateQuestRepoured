package team.cqr.cqrepoured.entity.ai.boss.spectrelord;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.BlockMode;
import net.minecraft.world.level.ClipContext.FluidMode;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.server.level.ServerLevel;
import team.cqr.cqrepoured.entity.ai.spells.AbstractEntityAISpell;
import team.cqr.cqrepoured.entity.ai.spells.IEntityAISpellAnimatedVanilla;
import team.cqr.cqrepoured.entity.ai.target.TargetUtil;
import team.cqr.cqrepoured.entity.boss.spectrelord.EntityCQRSpectreLord;
import team.cqr.cqrepoured.entity.boss.spectrelord.EntitySpectreLordIllusion;
import team.cqr.cqrepoured.faction.Faction;

public class EntityAISpectreLordSummonIllusions extends AbstractEntityAISpell<EntityCQRSpectreLord> implements IEntityAISpellAnimatedVanilla {

	private final int amount;
	private final int lifeTime;

	public EntityAISpectreLordSummonIllusions(EntityCQRSpectreLord entity, int cooldown, int chargingTicks, int amount, int lifeTime) {
		super(entity, cooldown, chargingTicks, 1);
		this.setup(true, true, true, true);
		this.amount = Math.max(amount, 1);
		this.lifeTime = lifeTime;
	}

	@Override
	public void startCastingSpell() {
		super.startCastingSpell();

		if (this.entity.getSummonedEntities().isEmpty()) {
			this.summonIllusions();
		} else {
			this.absorbIllusions();
		}
	}

	private void summonIllusions() {
		Vec3 start = this.entity.getEyePosition(1.0F);
		double d = this.random.nextDouble() * 360.0D;

		for (int i = 0; i < this.amount; i++) {
			double d1 = d + ((double) i / (double) this.amount + (this.random.nextDouble() - 0.5D) * 0.1D) * 360.0D;
			Vec3 look = Vec3.directionFromRotation(30.0F, (float) d1);
			Vec3 end = start.add(look.scale(8.0D));
			ClipContext rtc = new ClipContext(start, end, BlockMode.COLLIDER, FluidMode.NONE, null);
			BlockHitResult result = this.world.clip(rtc);//this.world.rayTraceBlocks(start, end, false, true, false);

			double x;
			double y;
			double z;
			if (result != null) {
				x = result.getLocation().x;
				y = result.getLocation().y;
				z = result.getLocation().z;
				if (result.getDirection() != Direction.UP) {
					double dx = this.entity.getX() - x;
					double dz = this.entity.getZ() - z;
					double d2 = 0.5D / Math.sqrt(dx * dx + dz * dz);
					x += dx * d2;
					z += dz * d2;
				}
			} else {
				x = end.x;
				y = end.y;
				z = end.z;
			}

			EntitySpectreLordIllusion illusion = new EntitySpectreLordIllusion(this.world, this.entity, this.lifeTime, i == 0, i == 2);
			illusion.setPos(x, y, z);
			this.entity.tryEquipSummon(illusion, this.world.random);
			illusion.finalizeSpawn((IServerWorld) this.world, this.world.getCurrentDifficultyAt(illusion.blockPosition()), MobSpawnType.EVENT, null, null);
			this.entity.addSummonedEntityToList(illusion);
			this.world.addFreshEntity(illusion);
			((ServerLevel) this.world).addParticle(ParticleTypes.EFFECT, illusion.getX(), illusion.getY() + 0.5D * illusion.getBbHeight(), illusion.getZ(), /*8,*/ 0.25D, 0.25D, 0.25D/*, 0.5D*/);
		}
	}

	private void absorbIllusions() {
		super.startCastingSpell();
		float heal = 0.05F;
		for (Entity e : this.entity.getSummonedEntities()) {
			if (e.distanceToSqr(this.entity) <= 32.0D * 32.0D) {
				heal += 0.05F;
				e.remove();
				((ServerLevel) this.world).addParticle(ParticleTypes.INSTANT_EFFECT, e.getX(), e.getY() + e.getBbHeight() * 0.5D, e.getZ(), /*4,*/ 0.25D, 0.25D, 0.25D/*, 0.5D*/);
			}
		}
		AABB aabb = new AABB(this.entity.getX() - 8.0D, this.entity.getY() - 0.5D, this.entity.getZ() - 8.0D, this.entity.getX() + 8.0D, this.entity.getY() + this.entity.getBbHeight() + 0.5D, this.entity.getZ() + 8.0D);
		Faction faction = this.entity.getFaction();
		for (LivingEntity e : this.world.getEntitiesOfClass(LivingEntity.class, aabb, e -> TargetUtil.PREDICATE_ATTACK_TARGET.apply(e) && (faction == null || !faction.isAlly(e)))) {
			heal += 0.05F;
			e.hurt(DamageSource.mobAttack(this.entity).bypassArmor(), 4.0F);
			e.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1, false, false));
		}
		this.entity.heal(this.entity.getMaxHealth() * heal);
		// TODO spawn shockwave entity
	}

	@Override
	protected SoundEvent getStartChargingSound() {
		return SoundEvents.EVOKER_PREPARE_SUMMON;
	}

	@Override
	public int getWeight() {
		if (this.entity.getSummonedEntities().isEmpty()) {
			return 10;
		}
		return this.entity.getHealth() / this.entity.getMaxHealth() < 0.3334F ? 40 : 20;
	}

	@Override
	public boolean ignoreWeight() {
		return false;
	}

	@Override
	public float getRed() {
		return 0.5F;
	}

	@Override
	public float getGreen() {
		return 0.95F;
	}

	@Override
	public float getBlue() {
		return 1.0F;
	}

}
