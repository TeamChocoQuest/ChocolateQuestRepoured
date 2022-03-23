package team.cqr.cqrepoured.entity.projectiles;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.network.IPacket;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import team.cqr.cqrepoured.init.CQREntityTypes;

import java.util.List;

public class ProjectileEarthQuake extends ThrowableEntity {
	private int lifeTime = 60;
	@SuppressWarnings("unused")
	private LivingEntity thrower;
	private double throwY = 0.3D;

	public void setThrowHeight(double amount) {
		this.throwY = amount;
	}

	public ProjectileEarthQuake(EntityType<? extends ThrowableEntity> throwableEntity, World worldIn) {
		super(throwableEntity, worldIn);
	}

	public ProjectileEarthQuake(World worldIn, double x, double y, double z) {
		super(CQREntityTypes.PROJECTILE_EARTH_QUAKE.get(), x, y, z, worldIn);
	}

	public ProjectileEarthQuake(World worldIn, LivingEntity throwerIn) {
		super(CQREntityTypes.PROJECTILE_EARTH_QUAKE.get(), throwerIn, worldIn);
		this.thrower = throwerIn;

		//this.posY -= 1.2D;
		this.setPos(this.getX(), this.getY() - 1.2D, this.getZ());
		this.setDeltaMovement(0.1D, -2.0D, 0.1D);
		//this.motionX = 0.1D;
		//this.motionY = -2.0D;
		//this.motionZ = 0.1D;
	}

	public LivingEntity getThrower()
	{
		return this.thrower;
	}

	@Override
	public void onHitEntity(EntityRayTraceResult entityResult)
	{
		if (!this.level.isClientSide) {
			if (!(entityResult.getEntity() instanceof MobEntity)) {
				//this.motionY = 0.0D;
				setDeltaMovement(getDeltaMovement().x, 0.0D, getDeltaMovement().z);
			}
		}
	}
	/*@Override
	protected void onImpact(RayTraceResult result) {
		if (!this.level.isClientSide) {
			if (!(result.entityHit instanceof MobEntity)) {
				this.motionY = 0.0D;
			}
		}
	} */

	@Override
	public void tick() {
		this.setDeltaMovement(getDeltaMovement().multiply(1.01D, 1.01D, 1.01D));
		//this.motionX *= 1.01D;
		//this.motionY *= 1.01D;
		//this.motionZ *= 1.01D;

		if (this.getThrower() != null && this.getThrower().isDeadOrDying()) {
			this.remove();
		}

		else {
			if (this.tickCount++ > 300) {
				this.remove();
			}

			this.onUpdateInAir();
			super.tick();
		}
	}

	private void onUpdateInAir() {
		this.lifeTime -= 1;

		if (this.lifeTime <= 0) {
			this.remove();
		}

		BlockPos pos = new BlockPos(this.position().x, this.position().y - 1, this.position().z);
		BlockState iblockstate = this.level.getBlockState(pos);

		if (iblockstate.getBlock() == null || iblockstate.getBlock().isAir(iblockstate, this.level, pos)) {
			iblockstate = Blocks.GLASS.defaultBlockState();
		}

		double dist = 1.0D;
		AxisAlignedBB var3 = this.getBoundingBox().expandTowards(dist, 2.0D, dist);
		List<Entity> list = this.level.getEntitiesOfClass(LivingEntity.class, var3);

		for (Entity entity : list) {
			if (entity instanceof LivingEntity && entity != this.getThrower() && !this.level.isClientSide && entity.isOnGround()) {
				entity.setDeltaMovement(entity.getDeltaMovement().x, entity.getDeltaMovement().y + this.getEntityThrowDistance(), entity.getDeltaMovement().z);
				//entity.motionY += this.getEntityThrowDistance();
				entity.hurt(DamageSource.indirectMagic(this, this.getThrower()), 1.0F);
			}
		}

		if (this.level.isClientSide) {
			for (int i = 0; i < 10; i++) {
				this.level.addParticle(new BlockParticleData(ParticleTypes.BLOCK, iblockstate), this.position().x + this.random.nextFloat() - 0.5D, this.position().y + this.random.nextFloat() - 0.5D, this.position().z + this.random.nextFloat() - 0.5D, this.random.nextFloat() - 0.5F, this.random.nextFloat(), this.random.nextFloat() - 0.5F);
			}
		}
	}

	public double getEntityThrowDistance() {
		return this.throwY;
	}

	@Override
	protected void defineSynchedData() {

	}
	
	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
