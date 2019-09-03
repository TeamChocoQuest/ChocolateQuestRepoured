package com.teamcqr.chocolatequestrepoured.objects.entity.mobs;

import com.teamcqr.chocolatequestrepoured.factions.EFaction;
import com.teamcqr.chocolatequestrepoured.objects.entity.EBaseHealths;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityCQRZombie extends AbstractEntityCQR {

	public EntityCQRZombie(World worldIn) {
		super(worldIn);
		this.setSize(0.6F, 1.95F);
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {

	}

	@Override
	public float getBaseHealth() {
		return EBaseHealths.UNDEAD.getValue();
	}

	@Override
	public EFaction getFaction() {
		return EFaction.UNDEAD;
	}

}
