package com.teamcqr.chocolatequestrepoured.objects.entity.mobs;

import com.teamcqr.chocolatequestrepoured.factions.EFaction;
import com.teamcqr.chocolatequestrepoured.objects.entity.EBaseHealths;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityCQRInquisitor extends AbstractEntityCQR {

	public EntityCQRInquisitor(World worldIn) {
		super(worldIn);
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {

	}

	@Override
	public float getBaseHealth() {
		return EBaseHealths.INQUISITOR.getValue();
	}

	@Override
	public EFaction getFaction() {
		return EFaction.INQUISITION;
	}

}
