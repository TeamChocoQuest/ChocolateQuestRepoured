package com.teamcqr.chocolatequestrepoured.objects.entity.mobs;

import com.teamcqr.chocolatequestrepoured.factions.EDefaultFaction;
import com.teamcqr.chocolatequestrepoured.init.CQRLoottables;
import com.teamcqr.chocolatequestrepoured.objects.entity.EBaseHealths;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EntityCQRHuman extends AbstractEntityCQR {

	public EntityCQRHuman(World worldIn) {
		super(worldIn);
	}

	@Override
	protected ResourceLocation getLootTable() {
		return CQRLoottables.ENTITIES_HUMAN;
	}

	@Override
	public float getBaseHealth() {
		return EBaseHealths.HUMAN.getValue();
	}

	@Override
	protected EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.PLAYERS;
	}
	
	@Override
	public int getTextureCount() {
		return 10;
	}

}
