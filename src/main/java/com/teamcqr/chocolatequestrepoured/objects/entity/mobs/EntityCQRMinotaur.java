package com.teamcqr.chocolatequestrepoured.objects.entity.mobs;

import com.teamcqr.chocolatequestrepoured.factions.EDefaultFaction;
import com.teamcqr.chocolatequestrepoured.objects.entity.EBaseHealths;
import com.teamcqr.chocolatequestrepoured.objects.entity.ELootTablesNormal;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityCQRMinotaur extends AbstractEntityCQR {

	public EntityCQRMinotaur(World worldIn) {
		super(worldIn);
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {

	}

	@Override
	public float getBaseHealth() {
		return EBaseHealths.MINOTAUR.getValue();
	}

	@Override
	public EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.UNDEAD;
	}

	@Override
	protected ResourceLocation getLootTable() {
		return ELootTablesNormal.ENTITY_MINOTAUR.getLootTable();
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if(source.isFireDamage()) {
			return false;
		}
		return super.attackEntityFrom(source, amount);
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
