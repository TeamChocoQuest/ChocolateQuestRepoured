package team.cqr.cqrepoured.entity.projectiles;

import net.minecraft.entity.LivingEntity;
import net.minecraftforge.entity.PartEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import team.cqr.cqrepoured.config.CQRConfig;

public class ProjectileHotFireball extends ThrowableEntity {

	public ProjectileHotFireball(World worldIn) {
		super(worldIn);
		this.setSize(0.5F, 0.5F);
	}

	public ProjectileHotFireball(World worldIn, LivingEntity shooter, double x, double y, double z) {
		super(worldIn, x, y, z);
		this.thrower = shooter;
		this.setSize(0.5F, 0.5F);
	}

	public ProjectileHotFireball(World worldIn, LivingEntity shooter) {
		super(worldIn, shooter);
		this.setSize(0.5F, 0.5F);
	}

	@Override
	public boolean hasNoGravity() {
		return true;
	}

	@Override
	public void onUpdate() {
		if (this.ticksExisted > 400) {
			this.world.createExplosion(this.thrower, this.posX, this.posY, this.posZ, 1.5F, true);
			this.setDead();
		}

		super.tick();
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if (this.world.isRemote) {
			return;
		}
		if (result.typeOfHit == Type.ENTITY) {
			if (result.entityHit == this.thrower) {
				return;
			}

			if (result.entityHit instanceof PartEntity && ((PartEntity) result.entityHit).parent == this.thrower) {
				return;
			}

			if (result.entityHit instanceof LivingEntity) {
				if (((LivingEntity) result.entityHit).isActiveItemStackBlocking() && ((LivingEntity) result.entityHit).getActiveItemStack().getItem() instanceof ShieldItem) {
					this.motionX = -this.motionX;
					this.motionY = -this.motionY;
					this.motionZ = -this.motionZ;
					this.velocityChanged = true;
					this.thrower = (LivingEntity) result.entityHit;
					return;
				}
			}
		}
		this.world.createExplosion(this.thrower, this.posX, this.posY, this.posZ, 3.0F, CQRConfig.bosses.hotFireballsDestroyTerrain);
		this.setDead();
	}

}
