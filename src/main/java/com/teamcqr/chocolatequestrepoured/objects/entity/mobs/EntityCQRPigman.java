package com.teamcqr.chocolatequestrepoured.objects.entity.mobs;

import com.teamcqr.chocolatequestrepoured.factions.EFaction;
import com.teamcqr.chocolatequestrepoured.objects.entity.EBaseHealths;

import net.minecraft.world.World;

public class EntityCQRPigman extends AbstractEntityCQR {

	public EntityCQRPigman(World worldIn) {
		super(worldIn);
		this.setSize(1.0F, 2.3F);
	}

	@Override
	public float getBaseHealth() {
		return EBaseHealths.PIGMEN.getValue();
	}

	@Override
	public EFaction getFaction() {
		return EFaction.UNDEAD;
	}

}
