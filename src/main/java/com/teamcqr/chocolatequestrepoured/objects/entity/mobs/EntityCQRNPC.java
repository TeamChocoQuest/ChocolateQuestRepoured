package com.teamcqr.chocolatequestrepoured.objects.entity.mobs;

import com.teamcqr.chocolatequestrepoured.factions.EDefaultFaction;
import com.teamcqr.chocolatequestrepoured.init.CQRLoottables;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EntityCQRNPC extends AbstractEntityCQR {

	public EntityCQRNPC(World worldIn) {
		super(worldIn);
	}

	@Override
	public float getBaseHealth() {
		return CQRConfig.baseHealths.NPC;
	}

	@Override
	public EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.INQUISITION;
	}

	@Override
	protected ResourceLocation getLootTable() {
		return CQRLoottables.ENTITIES_NPC;
	}

}
