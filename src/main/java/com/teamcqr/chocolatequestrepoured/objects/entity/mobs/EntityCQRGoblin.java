package com.teamcqr.chocolatequestrepoured.objects.entity.mobs;

import com.teamcqr.chocolatequestrepoured.factions.EDefaultFaction;
import com.teamcqr.chocolatequestrepoured.init.CQRLoottables;
import com.teamcqr.chocolatequestrepoured.init.CQRSounds;
import com.teamcqr.chocolatequestrepoured.objects.entity.EBaseHealths;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class EntityCQRGoblin extends AbstractEntityCQR {

	public EntityCQRGoblin(World worldIn) {
		super(worldIn);
	}

	@Override
	protected ResourceLocation getLootTable() {
		return CQRLoottables.ENTITIES_GOBLIN;
	}

	@Override
	public float getBaseHealth() {
		return EBaseHealths.GOBLIN.getValue();
	}

	@Override
	protected EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.GOBLINS;
	}

	@Override
	public float getDefaultHeight() {
		return super.getDefaultHeight() * 0.75F;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return CQRSounds.GOBLIN_DEATH;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return CQRSounds.GOBLIN_AMBIENT;
	}

	@Override
	protected SoundEvent getDefaultHurtSound(DamageSource damageSourceIn) {
		return CQRSounds.GOBLIN_HURT;
	}

}
