package com.teamcqr.chocolatequestrepoured.objects.entity.projectiles;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ProjectileLargeFireball extends ProjectileBase {

	public ProjectileLargeFireball(World worldIn) {
		super(worldIn);
	}

	public ProjectileLargeFireball(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	public ProjectileLargeFireball(World worldIn, EntityLivingBase shooter) {
		super(worldIn, shooter);
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		super.onImpact(result);
		// DONE: Explode
		this.world.createExplosion(this.thrower, posX, posY, posZ, 3.0F, true);
	}

}
