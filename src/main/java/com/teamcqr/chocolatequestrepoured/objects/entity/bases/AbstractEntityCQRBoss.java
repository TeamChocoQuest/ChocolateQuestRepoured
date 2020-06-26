package com.teamcqr.chocolatequestrepoured.objects.entity.bases;

import com.teamcqr.chocolatequestrepoured.util.CQRConfig;

import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;

public abstract class AbstractEntityCQRBoss extends AbstractEntityCQR {

	protected String assignedRegionID = null;

	protected final BossInfoServer bossInfoServer = new BossInfoServer(this.getDisplayName(), BossInfo.Color.RED, BossInfo.Overlay.NOTCHED_10);

	public int deathTicks = 0;
	public static final int MAX_DEATH_TICKS = 200;

	public AbstractEntityCQRBoss(World worldIn) {
		super(worldIn);
		this.experienceValue = 50;
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
		if (compound.hasKey("assignedRegion")) {
			this.assignedRegionID = compound.getString("assignedRegion");
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		if (this.assignedRegionID != null) {
			compound.setString("assignedRegion", this.assignedRegionID);
		}
	}

	public void assignRegion(String regionID) {
		if (regionID != null) {
			this.assignedRegionID = regionID;
		}
	}

	@Override
	public boolean isNonBoss() {
		return false;
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();

		this.bossInfoServer.setName(getDisplayName());
		this.bossInfoServer.setPercent(this.getHealth() / this.getMaxHealth());
	}

	@Override
	public void addTrackingPlayer(EntityPlayerMP player) {
		super.addTrackingPlayer(player);
		this.bossInfoServer.addPlayer(player);
	}

	@Override
	public void removeTrackingPlayer(EntityPlayerMP player) {
		super.removeTrackingPlayer(player);
		this.bossInfoServer.removePlayer(player);
	}

	@Override
	public void onDeath(DamageSource cause) {
		super.onDeath(cause);

		// TOOD: Destroy protected region
	}

	@Override
	public void setCustomNameTag(String name) {
		super.setCustomNameTag(name);
		this.bossInfoServer.setName(this.getDisplayName());
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
			this.move(MoverType.SELF, 0, 10 / MAX_DEATH_TICKS / 3, 0);
			if (this.deathTicks == MAX_DEATH_TICKS && !this.world.isRemote) {
				this.world.playSound(this.posX, this.posY, this.posZ, this.getFinalDeathSound(), SoundCategory.MASTER, 1, 1, false);
				this.setDead();
				
				onFinalDeath();
				
				if (this.doesExplodeOnDeath()) {
					this.world.createExplosion(this, this.posX, this.posY, this.posZ, 8.0F, true);
				}
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

}
