package com.teamcqr.chocolatequestrepoured.objects.entity.mobs;

import com.teamcqr.chocolatequestrepoured.factions.EDefaultFaction;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;

import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EntityCQRMandril extends AbstractEntityCQR {

	public EntityCQRMandril(World worldIn) {
		super(worldIn);
	}

	@Override
	protected void initEntityAI() {
		super.initEntityAI();
		this.tasks.addTask(9, new EntityAILeapAtTarget(this, 0.6F));
	}

	@Override
	protected ResourceLocation getLootTable() {
		return null;
	}

	@Override
	public float getBaseHealth() {
		return CQRConfig.baseHealths.Mandril;
	}

	@Override
	public EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.BEASTS;
	}

	@Override
	public boolean canMountEntity() {
		return false;
	}

	@Override
	public float getEyeHeight() {
		return this.height * 0.84F;
	}

	@Override
	public float getDefaultWidth() {
		return 0.6F;
	}

	@Override
	public float getDefaultHeight() {
		return 1.9F;
	}

}
