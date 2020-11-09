package com.teamcqr.chocolatequestrepoured.objects.entity.boss.subparts;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRNetherDragon;

import net.minecraft.block.Block;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class EntityCQRNetherDragonSegment extends MultiPartEntityPart {

	private EntityCQRNetherDragon dragon;
	private int partIndex = 0;
	private int realID = 0;

	private static final DataParameter<Integer> PART_INDEX = EntityDataManager.<Integer>createKey(EntityCQRNetherDragonSegment.class, DataSerializers.VARINT);
	private static final DataParameter<Boolean> IS_SKELETAL = EntityDataManager.<Boolean>createKey(EntityCQRNetherDragonSegment.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> IS_REMOVED = EntityDataManager.<Boolean>createKey(EntityCQRNetherDragonSegment.class, DataSerializers.BOOLEAN);

	public EntityCQRNetherDragonSegment(EntityCQRNetherDragon dragon, int partID, boolean skeletal) {
		super((IEntityMultiPart) dragon, "dragonPart" + partID, 0.5F, 0.5F);

		this.setSize(1.25F, 1.25F);
		this.dragon = dragon;
		this.partIndex = dragon.INITIAL_SEGMENT_COUNT - partID;
		this.realID = partID;
		this.dataManager.set(PART_INDEX, this.partIndex);
		this.setIsSkeletal(skeletal);

		// String partName, float width, float height
		this.setInvisible(false);
	}

	public void onRemovedFromBody() {
		this.dataManager.set(IS_REMOVED, true);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(PART_INDEX, this.partIndex);
		this.dataManager.register(IS_SKELETAL, false);
		this.dataManager.register(IS_REMOVED, false);
	}

	public int getPartIndex() {
		return this.dataManager.get(PART_INDEX);
	}

	public boolean isSkeletal() {
		return this.dataManager.get(IS_SKELETAL) || this.dragon == null || this.dragon.getSkeleProgress() >= this.realID;
	}

	private void setIsSkeletal(Boolean val) {
		this.dataManager.set(IS_SKELETAL, val);
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (source.isExplosion() || source.isFireDamage() || this.dragon == null) {
			return false;
		}

		return this.dragon.attackEntityFromPart(this, source, amount);
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		++this.ticksExisted;

		if (this.dataManager.get(IS_REMOVED)) {
			// this.world.removeEntityDangerously(this);
			this.setDead();
		}
		if (this.world.isRemote && (this.dragon == null || this.dragon.isDead)) {
			this.setDead();
		}

	}

	@Override
	public boolean isNonBoss() {
		return this.dragon.isNonBoss();
	}

	// As this is a part it does not make any noises
	@Override
	protected void playStepSound(BlockPos pos, Block blockIn) {
	}

	@Override
	public void setRotation(float yaw, float pitch) {
		super.setRotation(yaw, pitch);
	}

	@Override
	public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
		if (this.dragon == null || this.dragon.isDead) {
			return false;
		}
		return this.dragon.processInitialInteract(player, hand);
	}

	@Nullable
	public EntityCQRNetherDragon getParent() {
		return this.dragon;
	}

	public void explode() {
		if (!this.world.isRemote) {
			this.world.createExplosion(this, this.posX, this.posY, this.posZ, 1, false);
		}
	}

	public void switchToSkeletalState() {
		this.setIsSkeletal(true);
		if (!this.world.isRemote) {
			this.world.createExplosion(this, this.posX, this.posY, this.posZ, 0, false);
		}
	}

	public void die() {
		this.explode();
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setBoolean("skeletal", this.isSkeletal());
		compound.setInteger("realID", this.realID);
		compound.setInteger("partIndex", this.partIndex);
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.setIsSkeletal(compound.getBoolean("skeletal"));
		this.realID = compound.getInteger("realID");
		this.partIndex = compound.getInteger("partIndex");
	}

}
