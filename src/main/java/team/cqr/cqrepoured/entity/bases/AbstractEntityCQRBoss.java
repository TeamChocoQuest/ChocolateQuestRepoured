package team.cqr.cqrepoured.entity.bases;

import com.github.alexthe666.iceandfire.entity.util.IBlacklistedFromStatues;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraft.world.phys.Vec3;
import team.cqr.cqrepoured.config.CQRConfig;

public abstract class AbstractEntityCQRBoss extends AbstractEntityCQR implements IBlacklistedFromStatues {

	public static final int MAX_DEATH_TICKS = 200;

	protected AbstractEntityCQRBoss(EntityType<? extends AbstractEntityCQR> type, Level worldIn) {
		super(type, worldIn);
		this.xpReward = 50;
		this.enableBossBar();
	}

	@Override
	public boolean hurt(DamageSource source, float amount, boolean sentFromPart) {
		int nearbyPlayerCount = 0;
		for (Player player : this.level().players()) {
			if (this.distanceToSqr(player) < 100.0D * 100.0D) {
				nearbyPlayerCount++;
			}
		}
		for (int i = 0; i < nearbyPlayerCount - 1; i++) {
			amount *= 1.0F - CQRConfig.SERVER_CONFIG.mobs.bossDamageReductionPerPlayer.get();
		}
		return super.hurt(source, amount, sentFromPart);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		if (this.hasCustomName() && this.bossInfoServer != null) {
			this.bossInfoServer.setName(this.getDisplayName());
		}
	}
	
	@Override
	public boolean isBoss() {
		return true;
	}

	@Override
	public void tick() {
		if (this.canHealWhenIdling() && CQRConfig.SERVER_CONFIG.bosses.enableHealthRegen.get() && !this.hasAttackTarget() && this.lastTickWithAttackTarget + 100 < this.tickCount && this.tickCount % 5 == 0) {
			this.heal(this.getMaxHealth() * 0.005F);
		}
		super.tick();
	}

	protected boolean canHealWhenIdling() {
		return true;
	}

	@Override
	protected void tickDeath() {
		if (this.usesEnderDragonDeath()) {
			if (this.isSitting()) {
				this.setSitting(false);
			}
			// super.onDeathUpdate();
			++this.deathTime;
			if (this.deathTime >= 180 && this.deathTime <= MAX_DEATH_TICKS) {
				float f = (this.random.nextFloat() - 0.5F) * 8.0F;
				float f1 = (this.random.nextFloat() - 0.5F) * 4.0F;
				float f2 = (this.random.nextFloat() - 0.5F) * 8.0F;
				if(this.level().isClientSide) {
					this.level().addParticle(this.getDeathAnimParticles(), this.getX() + f, this.getY() + 2.0D + f1, this.getZ() + f2, 0.0D, 0.0D, 0.0D);
				} else {
					((ServerLevel)this.level()).sendParticles(this.getDeathAnimParticles(), this.getX() + f, this.getY() + 2.0D + f1, this.getZ() + f2, 1, 0.0D, 0.0D, 0.0D, 1.0);
				}
			}
			this.setNoGravity(true);
			// DONE: Do this correctly. It is meant to move the boss up 10 blocks while he dies, atm this is not correct
			this.move(MoverType.SELF, new Vec3(0, (10.0D / MAX_DEATH_TICKS), 0));
			if (this.deathTime == MAX_DEATH_TICKS && !this.level().isClientSide) {
				this.playSound(this.getFinalDeathSound(), 10.0F, 1.0F);
				this.discard();

				if (this.doesExplodeOnDeath()) {
					this.level().explode(this, this.getX(), this.getY(), this.getZ(), 8.0F, ExplosionInteraction.MOB);
				}

				this.onFinalDeath();
			}
		} else {
			super.tickDeath();
		}
	}

	protected void onFinalDeath() {

	}

	protected SoundEvent getFinalDeathSound() {
		return this.getDeathSound();
	}

	protected boolean doesExplodeOnDeath() {
		return false;
	}

	protected boolean usesEnderDragonDeath() {
		return false;
	}

	protected ParticleOptions getDeathAnimParticles() {
		return ParticleTypes.EXPLOSION;
	}

	@Override
	public boolean canTameEntity() {
		return false;
	}

	@Override
	public boolean canMountEntity() {
		return false;
	}

	// Damage cap stuff
	@Override
	protected boolean damageCapEnabled() {
		return CQRConfig.SERVER_CONFIG.bossDamageCaps.enableDamageCapForBosses.get();
	}

	@Override
	protected float maxDamageInPercentOfMaxHP() {
		return (float)(double)CQRConfig.SERVER_CONFIG.bossDamageCaps.maxDamageInPercentOfMaxHP.get();
	}

	@Override
	protected float maxUncappedDamage() {
		return (float)(double)CQRConfig.SERVER_CONFIG.bossDamageCaps.maxUncappedDamage.get();
	}

	@Override
	public boolean canBeTurnedToStone() {
		return !CQRConfig.SERVER_CONFIG.bosses.blackListBossesFromIaFGorgonHead.get();
	}

	protected void spawnDeathPoofParticles() {
		if (!(this.level() instanceof ServerLevel)) {
			return;
		}
		// Copied from EntityLivingBase
		int hbVolume = (int) (this.getBbWidth() * this.getBbHeight() * this.getBbWidth());
		hbVolume *= 4;
		for (int k = 0; k < hbVolume; ++k) {
			double d2 = this.random.nextGaussian() * 0.02D;
			double d0 = this.random.nextGaussian() * 0.02D;
			double d1 = this.random.nextGaussian() * 0.02D;
			((ServerLevel) this.level()).sendParticles(
					ParticleTypes.EXPLOSION.getType(),
					this.getX() + this.random.nextFloat() * this.getBbWidth() * 2.0F - this.getBbWidth(), 
					this.getY() + this.random.nextFloat() * this.getBbHeight(), 
					this.getZ() + this.random.nextFloat() * this.getBbWidth() * 2.0F - this.getBbWidth(), 
					1,
					d2, 
					d0, 
					d1,
					0.05
			);
		}
	}

}
