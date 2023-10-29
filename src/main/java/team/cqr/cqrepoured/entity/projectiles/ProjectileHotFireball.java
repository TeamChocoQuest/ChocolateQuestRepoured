package team.cqr.cqrepoured.entity.projectiles;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.network.NetworkHooks;
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

	public ProjectileHotFireball(EntityType<? extends ProjectileBase> throwableEntity, Level world)
	{
		super(throwableEntity, world);
	}

	public ProjectileHotFireball(double pX, double pY, double pZ, Level world, LivingEntity shooter) {
		super(CQREntityTypes.PROJECTILE_HOT_FIREBALL.get(), world);
		this.shooter = shooter;
	}

	public ProjectileHotFireball(LivingEntity shooter, Level world)
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
		if(this.level().isClientSide()) {
			double dx = this.getX() + (-0.5 + (this.level().getRandom().nextDouble()));
			double dy = 0.25 + this.getY() + (-0.5 + (this.level().getRandom().nextDouble()));
			double dz = this.getZ() + (-0.5 + (this.level().getRandom().nextDouble()));
			this.level().addParticle(ParticleTypes.FLAME, dx, dy, dz, 0, 0, 0);
		}
		
		if (this.tickCount > 400) {
			this.level().explode(this.shooter, this.getX(), this.getY(), this.getZ(), 1.5F, ExplosionInteraction.NONE);
			this.discard();
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
	public void onHitEntity(EntityHitResult entityResult)
	{
		Entity entity = entityResult.getEntity();

		if(entity == this.shooter) return;

		if(entity instanceof PartEntity && ((PartEntity<?>)entity).getParent() == this.shooter) return;

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
	protected void onHitBlock(BlockHitResult result) {
		super.onHitBlock(result);
		this.level().explode(this.shooter, this.getX(), this.getY(), this.getZ(), 3.0F, CQRConfig.SERVER_CONFIG.bosses.hotFireballsDestroyTerrain.get() ? ExplosionInteraction.MOB : ExplosionInteraction.NONE);
		this.discard();
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected void defineSynchedData() {

	}
}
