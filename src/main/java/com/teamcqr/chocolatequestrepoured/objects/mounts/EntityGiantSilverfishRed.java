package com.teamcqr.chocolatequestrepoured.objects.mounts;

import com.teamcqr.chocolatequestrepoured.objects.entity.ELootTablesNormal;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.EntityCQRGiantSilverfishBase;

import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EntityGiantSilverfishRed extends EntityCQRGiantSilverfishBase {

	public EntityGiantSilverfishRed(World worldIn) {
		super(worldIn);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected ResourceLocation getLootTable() {
		return ELootTablesNormal.ENTITY_GIANT_SILVERFISH_RED.getLootTable();
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if(source.isFireDamage()) {
			return false;
		}
		return super.attackEntityFrom(source, amount);
	}

}
