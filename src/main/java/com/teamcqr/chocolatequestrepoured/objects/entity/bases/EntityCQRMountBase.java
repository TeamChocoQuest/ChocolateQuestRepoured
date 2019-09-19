package com.teamcqr.chocolatequestrepoured.objects.entity.bases;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public abstract class EntityCQRMountBase extends EntityAnimal {

	public EntityCQRMountBase(World worldIn) {
		super(worldIn);
		// TODO Auto-generated constructor stub
	}
	
	protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 1.25D));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
    }
	
	@Override
	protected boolean canBeRidden(Entity entityIn) {
		return entityIn instanceof AbstractEntityCQR || entityIn instanceof EntityPlayer;
	}

	@Override
	public EntityAgeable createChild(EntityAgeable ageable) {
		return null;
	}

}
