package com.teamcqr.chocolatequestrepoured.objects.entity;

import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.world.World;

public class EntitySlimePart extends EntitySlime {

	private UUID ownerUuid;

	public EntitySlimePart(World worldIn) {
		super(worldIn);
	}

	public EntitySlimePart(World worldIn, EntityLivingBase owner) {
		this(worldIn);
		this.ownerUuid = owner.getPersistentID();
	}

	@Override
	protected void initEntityAI() {
		super.initEntityAI();
		this.targetTasks.taskEntries.clear();
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
		iattributeinstance.setBaseValue(iattributeinstance.getBaseValue() * 0.5D);
	}

	@Override
	public void onUpdate() {
		if (this.ticksExisted > 400) {
			this.setDead();
		}

		super.onUpdate();
	}

	@Override
	protected void collideWithEntity(Entity entityIn) {
		if (entityIn instanceof EntityLivingBase && entityIn.getPersistentID().equals(this.ownerUuid)) {
			((EntityLivingBase) entityIn).heal(2.0F);
			this.setDead();
		}
	}

	@Override
	protected boolean canDropLoot() {
		return false;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setInteger("ticksExisted", this.ticksExisted);
		compound.setTag("ownerUuid", NBTUtil.createUUIDTag(this.ownerUuid));
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.ticksExisted = compound.getInteger("ticksExisted");
		this.ownerUuid = NBTUtil.getUUIDFromTag(compound.getCompoundTag("ownerUuid"));
	}

}
