package team.cqr.cqrepoured.entity.projectiles;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ShieldItem;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.fml.network.NetworkHooks;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.init.CQREntityTypes;

public class ProjectileHotFireball extends ProjectileBase {

	protected LivingEntity shooter;
	/*public ProjectileHotFireball(World worldIn) {
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
	} */

	public ProjectileHotFireball(EntityType<? extends ProjectileBase> throwableEntity, World world)
	{
		super(throwableEntity, world);
	}

	public ProjectileHotFireball(double pX, double pY, double pZ, World world, LivingEntity shooter) {
		super(CQREntityTypes.PROJECTILE_HOT_FIREBALL.get(), world);
		this.shooter = shooter;
	}

	public ProjectileHotFireball(LivingEntity shooter, World world)
	{
		super(CQREntityTypes.PROJECTILE_HOT_FIREBALL.get(), shooter.getX(), shooter.getEyeY() - (double)0.1F, shooter.getZ(), world);
	}

/*	@Override
	public boolean isNo()
	{
		return true;
	} */

	@Override
	public void tick() {
		if (this.tickCount > 400) {
			this.level.explode(this.shooter, this.getX(), this.getY(), this.getZ(), 1.5F, Explosion.Mode.NONE);
			this.remove();
		}

		super.tick();
	}

/*	@Override
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
	} */

	@Override
	public void onHitEntity(EntityRayTraceResult entityResult)
	{
		Entity entity = entityResult.getEntity();

		if(entity == this.shooter) return;

		if(entity instanceof PartEntity && ((PartEntity)entity).getParent() == this.shooter) return;

		if(entity instanceof LivingEntity)
		{
			LivingEntity livingEntity = (LivingEntity)entity;

			if(livingEntity.isBlocking() && livingEntity.getUseItem().getItem() instanceof ShieldItem)
			{
				this.setDeltaMovement(getDeltaMovement().multiply(-1, -1, -1));
				this.shooter = livingEntity;
			}
		}
	}

	@Override
	protected void onHitBlock(BlockRayTraceResult result) {
		super.onHitBlock(result);
		this.level.explode(this.shooter, this.getX(), this.getY(), this.getZ(), 3.0F, CQRConfig.bosses.hotFireballsDestroyTerrain ? Explosion.Mode.DESTROY : Explosion.Mode.NONE);
		this.remove();
	}

	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected void defineSynchedData() {

	}
}
