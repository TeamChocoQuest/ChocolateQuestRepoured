package com.teamcqr.chocolatequestrepoured.objects.entity.mobs;

import com.teamcqr.chocolatequestrepoured.factions.EDefaultFaction;
import com.teamcqr.chocolatequestrepoured.objects.entity.EBaseHealths;
import com.teamcqr.chocolatequestrepoured.objects.entity.ELootTablesNormal;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityCQROgre extends AbstractEntityCQR {

	public EntityCQROgre(World worldIn) {
		super(worldIn);
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {

	}

	@Override
	public float getBaseHealth() {
		return EBaseHealths.OGRE.getValue();
	}

	@Override
	public EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.GOBLINS;
	}
	
	@Override
	protected ResourceLocation getLootTable() {
		return ELootTablesNormal.ENTITY_OGRE.getLootTable();
	}
	
	@Override
	public int getTextureCount() {
		return 1;
	}
	
	@Override
	public boolean canRide() {
		return false;
	}

}
