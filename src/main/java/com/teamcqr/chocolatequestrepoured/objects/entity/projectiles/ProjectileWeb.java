package com.teamcqr.chocolatequestrepoured.objects.entity.projectiles;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ProjectileWeb extends ProjectileBase {

	private EntityLivingBase shooter;

	public ProjectileWeb(World worldIn) {
		super(worldIn);
	}

	public ProjectileWeb(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	public ProjectileWeb(World worldIn, EntityLivingBase shooter) {
		super(worldIn, shooter);
		this.shooter = shooter;
		this.isImmuneToFire = false;
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if (!this.world.isRemote) {
			if (result.typeOfHit == RayTraceResult.Type.ENTITY) {
				if (result.entityHit instanceof EntityLivingBase) {
					EntityLivingBase entity = (EntityLivingBase) result.entityHit;

					if (result.entityHit == this.shooter) {
						return;
					}

					entity.addPotionEffect(new PotionEffect(MobEffects.POISON, 60, 0));
					entity.setInWeb();
					world.setBlockState(entity.getPosition(), Blocks.WEB.getDefaultState());
					this.setDead();
				}
			}
			super.onImpact(result);
		}
	}

	@Override
	public boolean hasNoGravity() {
		return false;
	}

}
