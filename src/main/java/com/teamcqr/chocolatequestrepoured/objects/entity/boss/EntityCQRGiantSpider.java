package com.teamcqr.chocolatequestrepoured.objects.entity.boss;

import com.teamcqr.chocolatequestrepoured.factions.EDefaultFaction;
import com.teamcqr.chocolatequestrepoured.init.ModLoottables;
import com.teamcqr.chocolatequestrepoured.objects.entity.EBaseHealths;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQRBoss;

import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EntityCQRGiantSpider extends AbstractEntityCQRBoss {

	public EntityCQRGiantSpider(World worldIn) {
		super(worldIn);
	}
	
	@Override
	public float getDefaultHeight() {
		return 1F;
	}
	
	@Override
	public float getDefaultWidth() {
		return 3.5F;
	}

	@Override
	protected ResourceLocation getLootTable() {
		return ModLoottables.ENTITIES_SPIDER;
	}

	@Override
	public float getBaseHealth() {
		return EBaseHealths.GIANT_SPIDER.getValue();
	}

	@Override
	public EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.BEASTS;
	}
	
	@Override
	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.ARTHROPOD;
	}

}
