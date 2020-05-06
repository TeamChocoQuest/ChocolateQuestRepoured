package com.teamcqr.chocolatequestrepoured.objects.mounts;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.EntityCQRGiantSilverfishBase;
import com.teamcqr.chocolatequestrepoured.util.CQRLootTableList;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EntityGiantSilverfishGreen extends EntityCQRGiantSilverfishBase {

	public EntityGiantSilverfishGreen(World worldIn) {
		super(worldIn);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ResourceLocation getLootTable() {
		return CQRLootTableList.ENTITIES_GIANT_SILVERFISH_GREEN;
	}

}
