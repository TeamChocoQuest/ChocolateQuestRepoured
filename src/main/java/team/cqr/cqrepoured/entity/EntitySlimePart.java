package team.cqr.cqrepoured.entity;

import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.world.World;

public class EntitySlimePart extends SlimeEntity {

	private UUID ownerUuid;

	public EntitySlimePart(World worldIn) {
		super(worldIn);
	}

	public EntitySlimePart(World worldIn, LivingEntity owner) {
		this(worldIn);
		this.ownerUuid = owner.getPersistentID();
	}

	@Override
	protected void initEntityAI() {
		super.registerGoals();
		this.targetTasks.taskEntries.clear();
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		ModifiableAttributeInstance iattributeinstance = this.getEntityAttribute(Attributes.MOVEMENT_SPEED);
		iattributeinstance.setBaseValue(iattributeinstance.getBaseValue() * 0.5D);
	}

	@Override
	public void onUpdate() {
		if (this.ticksExisted > 400) {
			this.setDead();
		}

		super.tick();
	}

	@Override
	protected void collideWithEntity(Entity entityIn) {
		if (entityIn instanceof LivingEntity && entityIn.getPersistentID().equals(this.ownerUuid)) {
			((LivingEntity) entityIn).heal(2.0F);
			this.setDead();
		}
	}

	@Override
	protected boolean canDropLoot() {
		return false;
	}

	@Override
	public void writeEntityToNBT(CompoundNBT compound) {
		super.save(compound);
		compound.setInteger("ticksExisted", this.ticksExisted);
		compound.setTag("ownerUuid", NBTUtil.createUUIDTag(this.ownerUuid));
	}

	@Override
	public void readEntityFromNBT(CompoundNBT compound) {
		super.readEntityFromNBT(compound);
		this.ticksExisted = compound.getInteger("ticksExisted");
		this.ownerUuid = NBTUtil.getUUIDFromTag(compound.getCompoundTag("ownerUuid"));
	}

}
