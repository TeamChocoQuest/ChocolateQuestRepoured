package team.cqr.cqrepoured.entity.bases;

import com.github.alexthe666.iceandfire.entity.IBlacklistedFromStatues;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import team.cqr.cqrepoured.config.CQRConfig;

public abstract class AbstractEntityCQRBoss extends AbstractEntityCQR implements IBlacklistedFromStatues {

	public static final int MAX_DEATH_TICKS = 200;

	protected AbstractEntityCQRBoss(EntityType<? extends AbstractEntityCQR> type, World worldIn) {
		super(type, worldIn);
		this.xpReward = 50;
		this.enableBossBar();
	}

	@Override
	public boolean hurt(DamageSource source, float amount, boolean sentFromPart) {
		int nearbyPlayerCount = 0;
		for (PlayerEntity player : this.level.players()) {
			if (this.distanceToSqr(player) < 100.0D * 100.0D) {
				nearbyPlayerCount++;
			}
		}
		for (int i = 0; i < nearbyPlayerCount - 1; i++) {
			amount *= 1.0F - CQRConfig.mobs.bossDamageReductionPerPlayer;
		}
		return super.hurt(source, amount, sentFromPart);
	}

	@Override
	public void readAdditionalSaveData(CompoundNBT compound) {
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
		if (this.canHealWhenIdling() && CQRConfig.bosses.enableHealthRegen && !this.hasAttackTarget() && this.lastTickWithAttackTarget + 100 < this.tickCount && this.tickCount % 5 == 0) {
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
				if(this.level.isClientSide) {
					this.level.addParticle(this.getDeathAnimParticles(), this.getX() + f, this.getY() + 2.0D + f1, this.getZ() + f2, 0.0D, 0.0D, 0.0D);
				} else {
					((ServerWorld)this.level).sendParticles(this.getDeathAnimParticles(), this.getX() + f, this.getY() + 2.0D + f1, this.getZ() + f2, 1, 0.0D, 0.0D, 0.0D, 1.0);
				}
			}
			this.setNoGravity(true);
			// DONE: Do this correctly. It is meant to move the boss up 10 blocks while he dies, atm this is not correct
			this.move(MoverType.SELF, new Vector3d(0, (10.0D / MAX_DEATH_TICKS), 0));
			if (this.deathTime == MAX_DEATH_TICKS && !this.level.isClientSide) {
				this.playSound(this.getFinalDeathSound(), 10.0F, 1.0F);
				this.remove();

				if (this.doesExplodeOnDeath()) {
					this.level.explode(this, this.getX(), this.getY(), this.getZ(), 8.0F, Explosion.Mode.DESTROY);
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

	protected IParticleData getDeathAnimParticles() {
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
		return CQRConfig.bossDamageCaps.enableDamageCapForBosses;
	}

	@Override
	protected float maxDamageInPercentOfMaxHP() {
		return CQRConfig.bossDamageCaps.maxDamageInPercentOfMaxHP;
	}

	@Override
	protected float maxUncappedDamage() {
		return CQRConfig.bossDamageCaps.maxUncappedDamage;
	}

	@Override
	public boolean canBeTurnedToStone() {
		return !CQRConfig.bosses.blackListBossesFromIaFGorgonHead;
	}

	protected void spawnDeathPoofParticles() {
		if (!(this.level instanceof ServerWorld)) {
			return;
		}
		// Copied from EntityLivingBase
		int hbVolume = (int) (this.getBbWidth() * this.getBbHeight() * this.getBbWidth());
		hbVolume *= 4;
		for (int k = 0; k < hbVolume; ++k) {
			double d2 = this.random.nextGaussian() * 0.02D;
			double d0 = this.random.nextGaussian() * 0.02D;
			double d1 = this.random.nextGaussian() * 0.02D;
			((ServerWorld) this.level).sendParticles(
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
