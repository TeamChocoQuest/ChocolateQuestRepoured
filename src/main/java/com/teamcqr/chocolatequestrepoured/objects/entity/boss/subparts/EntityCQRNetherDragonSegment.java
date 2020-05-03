package com.teamcqr.chocolatequestrepoured.objects.entity.boss.subparts;

import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRNetherDragon;

import net.minecraft.block.Block;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class EntityCQRNetherDragonSegment extends MultiPartEntityPart {

	// TODO: Add the tail peek

	private final EntityCQRNetherDragon dragon;
	private int partIndex = 0;
	
	private static final DataParameter<Integer> PART_INDEX = EntityDataManager.<Integer>createKey(EntityCQRNetherDragonSegment.class, DataSerializers.VARINT);
	
	public EntityCQRNetherDragonSegment(EntityCQRNetherDragon dragon, int partID) {
		super((IEntityMultiPart) dragon, "dragonPart" + partID, 0.5F, 0.5F);

		this.setSize(1.25F, 1.25F);

		this.dragon = dragon;
		this.partIndex = EntityCQRNetherDragon.SEGMENT_COUNT - partID;
		this.dataManager.set(PART_INDEX, this.partIndex);

		// String partName, float width, float height
		this.setInvisible(false);
	}
	
	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(PART_INDEX, this.partIndex);
	}
	
	public int getPartIndex() {
		return this.dataManager.get(PART_INDEX);
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		return this.dragon.attackEntityFromPart(this, source, amount);
		// return super.attackEntityFrom(source, amount);
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		++this.ticksExisted;
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
		if (this.dragon.isDead) {
			return false;
		}
		return this.dragon.processInitialInteract(player, hand);
	}

	public void explode() {
		if(!world.isRemote) {
			this.world.createExplosion(this, posX, posY, posZ, 3, false);
		}
		setDead();
	}

}
