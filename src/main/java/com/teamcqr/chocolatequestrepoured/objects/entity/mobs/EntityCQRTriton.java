package com.teamcqr.chocolatequestrepoured.objects.entity.mobs;

import com.teamcqr.chocolatequestrepoured.factions.EDefaultFaction;
import com.teamcqr.chocolatequestrepoured.objects.entity.EBaseHealths;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.util.CQRLootTableList;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EntityCQRTriton extends AbstractEntityCQR {

	public EntityCQRTriton(World worldIn) {
		super(worldIn);
	}

	@Override
	public float getBaseHealth() {
		return EBaseHealths.TRITON.getValue();
	}

	@Override
	public EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.TRITONS;
	}

	@Override
	protected ResourceLocation getLootTable() {
		return CQRLootTableList.ENTITIES_TRITON;
	}

	@Override
	public boolean isSitting() {
		return false;
	}

	@Override
	public int getTextureCount() {
		return 2;
	}

	@Override
	public boolean canMountEntity() {
		return false;
	}
	
	@Override
	protected float getWaterSlowDown() {
		return 0.0F;
	}

}
