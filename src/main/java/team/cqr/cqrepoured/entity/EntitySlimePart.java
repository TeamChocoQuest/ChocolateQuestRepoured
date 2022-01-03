package team.cqr.cqrepoured.entity;

import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.world.World;

public class EntitySlimePart extends SlimeEntity {

	private UUID ownerUuid;

	public EntitySlimePart(EntityType<? extends EntitySlimePart> type, World worldIn) {
		super(type, worldIn);
	}

	public EntitySlimePart(World worldIn, LivingEntity owner) {
		this(worldIn);
		this.ownerUuid = owner.getUUID();
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.targetSelector.availableGoals.clear();
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		ModifiableAttributeInstance iattributeinstance = this.getEntityAttribute(Attributes.MOVEMENT_SPEED);
		iattributeinstance.setBaseValue(iattributeinstance.getBaseValue() * 0.5D);
	}

	@Override
	public void tick() {
		if (this.tickCount > 400) {
			this.remove();
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
