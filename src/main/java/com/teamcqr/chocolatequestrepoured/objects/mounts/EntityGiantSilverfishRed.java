package com.teamcqr.chocolatequestrepoured.objects.mounts;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.EntityCQRGiantSilverfishBase;
import com.teamcqr.chocolatequestrepoured.util.CQRLootTableList;

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
		return CQRLootTableList.ENTITIES_GIANT_SILVERFISH_RED;
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (source.isFireDamage()) {
			return false;
		}
		return super.attackEntityFrom(source, amount);
	}

}
