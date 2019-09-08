package com.teamcqr.chocolatequestrepoured.objects.entity.mobs;

import com.teamcqr.chocolatequestrepoured.factions.EFaction;

import net.minecraft.entity.Entity;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityCQRDummy extends AbstractEntityCQR {

	public EntityCQRDummy(World worldIn) {
		super(worldIn);
		this.setHealingPotions(0);
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {

	}

	@Override
	public float getBaseHealth() {
		return 1;
	}

	@Override
	public EFaction getFaction() {
		return null;
	}

	@Override
	protected void initEntityAI() {

	}

	@Override
	public boolean canBePushed() {
		return false;
	}

	@Override
	protected void collideWithEntity(Entity entityIn) {

	}

}
