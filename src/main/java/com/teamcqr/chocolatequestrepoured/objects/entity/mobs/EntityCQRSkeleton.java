package com.teamcqr.chocolatequestrepoured.objects.entity.mobs;

import com.teamcqr.chocolatequestrepoured.factions.EDefaultFaction;
import com.teamcqr.chocolatequestrepoured.objects.entity.EBaseHealths;
import com.teamcqr.chocolatequestrepoured.objects.entity.ELootTablesNormal;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityCQRSkeleton extends AbstractEntityCQR {

	public EntityCQRSkeleton(World worldIn) {
		super(worldIn);
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {

	}

	@Override
	public float getBaseHealth() {
		return EBaseHealths.SKELETON.getValue();
	}

	@Override
	public EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.UNDEAD;
	}
	
	@Override
	protected ResourceLocation getLootTable() {
		return ELootTablesNormal.ENTITY_SKELETON.getLootTable();
	}

	@Override
	public int getTextureCount() {
		return 1;
	}
	
	@Override
	public boolean canRide() {
		return true;
	}
	
}
