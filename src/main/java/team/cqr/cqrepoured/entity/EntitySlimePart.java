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

	public EntitySlimePart(EntityType<? extends EntitySlimePart> type, World worldIn, LivingEntity owner) {
		this(type, worldIn);
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
		ModifiableAttributeInstance iattributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
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
	protected void doPush(Entity pEntity) {
		this.push(pEntity);
	}
	
	@Override
	public void push(Entity entityIn) {
		if (entityIn instanceof LivingEntity && entityIn.getUUID().equals(this.ownerUuid)) {
			((LivingEntity) entityIn).heal(2.0F);
			this.remove();
		}
	}
	
	@Override
	protected boolean shouldDropLoot() {
		return false;
	}

	@Override
	public void addAdditionalSaveData(CompoundNBT compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("ticksExisted", this.tickCount);
		compound.put("ownerUuid", NBTUtil.createUUID(this.ownerUuid));
	}

	@Override
	public void readAdditionalSaveData(CompoundNBT compound) {
		super.readAdditionalSaveData(compound);
		this.tickCount = compound.getInt("ticksExisted");
		this.ownerUuid = NBTUtil.loadUUID(compound.getCompound("ownerUuid"));
	}

}
