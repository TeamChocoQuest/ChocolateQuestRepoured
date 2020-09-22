package com.teamcqr.chocolatequestrepoured.objects.entity.projectiles;

import java.util.function.Consumer;

import com.teamcqr.chocolatequestrepoured.init.ModBlocks;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.volcano.GeneratorVolcano;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;

import net.minecraft.block.BlockAir;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
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
					this.world.setBlockState(entity.getPosition(), ModBlocks.TEMPORARY_WEB.getDefaultState());
					this.setDead();
				}
			}
			else if(DungeonGenUtils.percentageRandom(0.3)) {
				GeneratorVolcano.forEachSpherePosition(this.getPosition(), DungeonGenUtils.randomBetween(1, 3), new Consumer<BlockPos.MutableBlockPos>() {

					@Override
					public void accept(MutableBlockPos t) {
						if(world.getBlockState(t).getBlock() instanceof BlockAir) world.setBlockState(t, ModBlocks.TEMPORARY_WEB.getDefaultState());
					}});
			}
			super.onImpact(result);
		}
	}

	@Override
	public boolean hasNoGravity() {
		return false;
	}

}
