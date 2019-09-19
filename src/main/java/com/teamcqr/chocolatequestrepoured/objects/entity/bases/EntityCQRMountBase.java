package com.teamcqr.chocolatequestrepoured.objects.entity.bases;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.world.World;

public abstract class EntityCQRMountBase extends EntityAnimal {

	public EntityCQRMountBase(World worldIn) {
		super(worldIn);
		// TODO Auto-generated constructor stub
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
