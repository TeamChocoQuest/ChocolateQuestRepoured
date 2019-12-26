package com.teamcqr.chocolatequestrepoured.objects.entity.misc;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.world.World;

public class EntityFlyingSkullMinion extends EntityFlying {
	
	protected Entity summoner;

	public EntityFlyingSkullMinion(World worldIn) {
		super(worldIn);
	}
	
	public void setSummoner(Entity ent) {
		this.summoner = ent;
	}

}
