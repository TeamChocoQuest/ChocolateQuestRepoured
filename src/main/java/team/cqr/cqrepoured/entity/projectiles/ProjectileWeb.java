package team.cqr.cqrepoured.entity.projectiles;

import net.minecraft.block.AirBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import team.cqr.cqrepoured.init.CQRBlocks;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.world.structure.generation.generators.volcano.GeneratorVolcano;

public class ProjectileWeb extends ProjectileBase {

	private LivingEntity shooter;

	public ProjectileWeb(World worldIn) {
		super(worldIn);
	}

	public ProjectileWeb(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	public ProjectileWeb(World worldIn, LivingEntity shooter) {
		super(worldIn, shooter);
		this.shooter = shooter;
		this.isImmuneToFire = false;
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if (!this.world.isRemote) {
			if (result.typeOfHit == RayTraceResult.Type.ENTITY) {
				if (result.entityHit instanceof LivingEntity) {
					LivingEntity entity = (LivingEntity) result.entityHit;

					if (result.entityHit == this.shooter) {
						return;
					}

					entity.addPotionEffect(new EffectInstance(Effects.POISON, 60, 0));
					entity.setInWeb();
					this.world.setBlockState(entity.getPosition(), CQRBlocks.TEMPORARY_WEB.getDefaultState());
					this.setDead();
				}
			} else if (DungeonGenUtils.percentageRandom(75)) {
				GeneratorVolcano.forEachSpherePosition(this.getPosition(), DungeonGenUtils.randomBetween(1, 3), t -> {
					if (ProjectileWeb.this.world.getBlockState(t).getBlock() instanceof AirBlock) {
						ProjectileWeb.this.world.setBlockState(t, CQRBlocks.TEMPORARY_WEB.getDefaultState());
					}
				});
			}
			super.onImpact(result);
		}
	}

	@Override
	public boolean hasNoGravity() {
		return false;
	}

}
