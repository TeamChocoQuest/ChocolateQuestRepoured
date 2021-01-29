package team.cqr.cqrepoured.objects.entity.bases;

import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import team.cqr.cqrepoured.util.CQRConfig;

public abstract class AbstractEntityCQRBoss extends AbstractEntityCQR {

	public int deathTicks = 0;
	public static final int MAX_DEATH_TICKS = 200;

	public AbstractEntityCQRBoss(World worldIn) {
		super(worldIn);
		this.experienceValue = 50;
		this.enableBossBar();
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount, boolean sentFromPart) {
		int nearbyPlayerCount = 0;
		for (EntityPlayer player : this.world.playerEntities) {
			if (this.getDistanceSq(player) < 100.0D * 100.0D) {
				nearbyPlayerCount++;
			}
		}
		for (int i = 0; i < nearbyPlayerCount - 1; i++) {
			amount *= 1.0F - CQRConfig.mobs.bossDamageReductionPerPlayer;
		}
		return super.attackEntityFrom(source, amount, sentFromPart);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		if (this.hasCustomName()) {
			this.bossInfoServer.setName(this.getDisplayName());
		}
	}

	@Override
	public boolean isNonBoss() {
		return false;
	}

	@Override
	public void onLivingUpdate() {
		if (CQRConfig.bosses.enableHealthRegen && !this.hasAttackTarget() && this.lastTickWithAttackTarget + 100 < this.ticksExisted && this.ticksExisted % 5 == 0) {
			this.heal(this.getMaxHealth() * 0.005F);
		}
		super.onLivingUpdate();
	}

	@Override
	protected void onDeathUpdate() {
		if (this.usesEnderDragonDeath()) {
			if (this.isSitting()) {
				this.setSitting(false);
			}
			// super.onDeathUpdate();
			++this.deathTicks;
			if (this.deathTicks >= 180 && this.deathTicks <= MAX_DEATH_TICKS) {
				float f = (this.rand.nextFloat() - 0.5F) * 8.0F;
				float f1 = (this.rand.nextFloat() - 0.5F) * 4.0F;
				float f2 = (this.rand.nextFloat() - 0.5F) * 8.0F;
				this.world.spawnParticle(this.getDeathAnimParticles(), this.posX + (double) f, this.posY + 2.0D + (double) f1, this.posZ + (double) f2, 0.0D, 0.0D, 0.0D);
			}
			this.setNoGravity(true);
			// DONE: Do this correctly. It is meant to move the boss up 10 blocks while he dies, atm this is not correct
			this.move(MoverType.SELF, 0, (10.0D / (double) MAX_DEATH_TICKS), 0);
			if (this.deathTicks == MAX_DEATH_TICKS && !this.world.isRemote) {
				this.world.playSound(this.posX, this.posY, this.posZ, this.getFinalDeathSound(), SoundCategory.MASTER, 1, 1, false);
				this.setDead();

				if (this.doesExplodeOnDeath()) {
					this.world.createExplosion(this, this.posX, this.posY, this.posZ, 8.0F, true);
				}
				
				this.onFinalDeath();
			}
		} else {
			super.onDeathUpdate();
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

	protected EnumParticleTypes getDeathAnimParticles() {
		return EnumParticleTypes.EXPLOSION_HUGE;
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

}
