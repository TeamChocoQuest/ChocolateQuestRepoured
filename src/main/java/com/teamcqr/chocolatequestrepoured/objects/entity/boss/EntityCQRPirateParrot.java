package com.teamcqr.chocolatequestrepoured.objects.entity.boss;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityCQRPirateParrot extends EntityParrot {

	public EntityCQRPirateParrot(World worldIn) {
		super(worldIn);
	}
	
	@Override
	protected void initEntityAI() {
		super.initEntityAI();
		//TODO: Add AI for attacking the enemy
	}
	
	@Override
	public void onDeath(DamageSource cause) {
		super.onDeath(cause);
		world.createExplosion(this, posX, posY, posZ, 2, true);
	}
	
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40);
	}

}
