package com.teamcqr.chocolatequestrepoured.objects.mounts;

import com.teamcqr.chocolatequestrepoured.objects.entity.ELootTablesNormal;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.EntityCQRGiantSilverfishBase;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EntityGiantSilverfishNormal extends EntityCQRGiantSilverfishBase {

	public EntityGiantSilverfishNormal(World worldIn) {
		super(worldIn);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ResourceLocation getLootTable() {
		return ELootTablesNormal.ENTITY_GIANT_SILVERFISH.getLootTable();
	}

}
