package team.cqr.cqrepoured.objects.entity.projectiles;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemShield;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import team.cqr.cqrepoured.util.CQRConfig;

public class ProjectileHotFireball extends EntityThrowable {

	public ProjectileHotFireball(World worldIn) {
		super(worldIn);
		this.setSize(0.5F, 0.5F);
	}

	public ProjectileHotFireball(World worldIn, EntityLivingBase shooter, double x, double y, double z) {
		super(worldIn, x, y, z);
		this.thrower = shooter;
		this.setSize(0.5F, 0.5F);
	}

	public ProjectileHotFireball(World worldIn, EntityLivingBase shooter) {
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

		super.onUpdate();
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

			if (result.entityHit instanceof MultiPartEntityPart && ((MultiPartEntityPart) result.entityHit).parent == this.thrower) {
				return;
			}

			if (result.entityHit instanceof EntityLivingBase) {
				if (((EntityLivingBase) result.entityHit).isActiveItemStackBlocking() && ((EntityLivingBase) result.entityHit).getActiveItemStack().getItem() instanceof ItemShield) {
					this.motionX = -this.motionX;
					this.motionY = -this.motionY;
					this.motionZ = -this.motionZ;
					this.velocityChanged = true;
					this.thrower = (EntityLivingBase) result.entityHit;
					return;
				}
			}
		}
		this.world.createExplosion(this.thrower, this.posX, this.posY, this.posZ, 3.0F, CQRConfig.bosses.hotFireballsDestroyTerrain);
		this.setDead();
	}

}
