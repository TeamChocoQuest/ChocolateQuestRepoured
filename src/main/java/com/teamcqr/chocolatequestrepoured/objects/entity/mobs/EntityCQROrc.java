package com.teamcqr.chocolatequestrepoured.objects.entity.mobs;

import com.teamcqr.chocolatequestrepoured.factions.EDefaultFaction;
import com.teamcqr.chocolatequestrepoured.init.CQRLoottables;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EntityCQROrc extends AbstractEntityCQR {

	public EntityCQROrc(World worldIn) {
		super(worldIn);
	}

	@Override
	public float getBaseHealth() {
		return CQRConfig.baseHealths.Orc;
	}

	@Override
	public EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.GOBLINS;
	}

	@Override
	protected ResourceLocation getLootTable() {
		return CQRLoottables.ENTITIES_ORC;
	}

	@Override
	public int getTextureCount() {
		return 3;
	}

}
