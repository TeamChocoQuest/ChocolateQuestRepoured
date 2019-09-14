package com.teamcqr.chocolatequestrepoured.objects.entity.mobs;

import com.teamcqr.chocolatequestrepoured.factions.EFaction;
import com.teamcqr.chocolatequestrepoured.objects.entity.EBaseHealths;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityCQREnderman extends AbstractEntityCQR {

	public EntityCQREnderman(World worldIn) {
		super(worldIn);
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {

	}

	@Override
	public float getBaseHealth() {
		return EBaseHealths.ENDERMAN.getValue();
	}

	@Override
	public EFaction getFaction() {
		return EFaction.ENDERMEN;
	}
	
	@Override
	public double getSizeVariation() {
		return 0D;
	} 

}
