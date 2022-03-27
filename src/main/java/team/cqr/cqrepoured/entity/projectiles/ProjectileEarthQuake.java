package team.cqr.cqrepoured.entity.projectiles;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.network.IPacket;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import team.cqr.cqrepoured.init.CQREntityTypes;

import java.util.List;

public class ProjectileEarthQuake extends ProjectileEntity {
	private int lifeTime = 60;
	@SuppressWarnings("unused")
	private LivingEntity thrower;
	private double throwY = 0.3D;

	public void setThrowHeight(double amount) {
		this.throwY = amount;
	}

	public ProjectileEarthQuake(EntityType<? extends ProjectileEntity> throwableEntity, World worldIn) {
		super(throwableEntity, worldIn);
	}

	public ProjectileEarthQuake(World worldIn, double x, double y, double z) 
	{
		this(CQREntityTypes.PROJECTILE_EARTH_QUAKE.get(), worldIn);
	    this.setPos(x, y - 1.5D, z);
	}

	public ProjectileEarthQuake(World worldIn, LivingEntity throwerIn) {
		this(worldIn, throwerIn.getX(), throwerIn.getEyeY() - (double)0.1F, throwerIn.getZ());
	    this.setOwner(throwerIn);
		this.thrower = throwerIn;

		//this.posY -= 1.2D;
		//this.setPos(this.getX(), this.getY() - 1.2D, this.getZ());
		this.setDeltaMovement(0.1D, 0D, 0.1D);
		//this.setNoGravity(false);
		//this.motionX = 0.1D;
		//this.motionY = -2.0D;
		//this.motionZ = 0.1D;
	}

	public LivingEntity getThrower()
	{
		return this.thrower;
	}
	
	/*@Override
	protected void onHit(RayTraceResult pResult) {
	      RayTraceResult.Type raytraceresult$type = pResult.getType();
	      if (raytraceresult$type == RayTraceResult.Type.ENTITY) {
	         this.onHitEntity((EntityRayTraceResult)pResult);
	      } 

	   } */

	@Override
	public void shoot(double pX, double pY, double pZ, float pVelocity, float pInaccuracy) {
	      Vector3d vector3d = (new Vector3d(pX, pY, pZ)).normalize().add(this.random.nextGaussian() * (double)0.0075F * (double)pInaccuracy, this.random.nextGaussian() * (double)0.0075F * (double)pInaccuracy, this.random.nextGaussian() * (double)0.0075F * (double)pInaccuracy).scale((double)pVelocity);
	      this.setDeltaMovement(vector3d);
	      float f = MathHelper.sqrt(getHorizontalDistanceSqr(vector3d));
	      this.yRot = (float)(MathHelper.atan2(vector3d.x, vector3d.z) * (double)(180F / (float)Math.PI));
	      //this.xRot = (float)(MathHelper.atan2(vector3d.y, (double)f) * (double)(180F / (float)Math.PI));
	      this.yRotO = this.yRot;
	      this.xRotO = this.xRot;
	   }

	@Override
	   public void shootFromRotation(Entity pShooter, float pX, float pY, float pZ, float pVelocity, float pInaccuracy) {
	      float f = -MathHelper.sin(pY * ((float)Math.PI / 180F)) * MathHelper.cos(pX * ((float)Math.PI / 180F));
	      float f1 = - 1.0F;//-MathHelper.sin((pX + pZ) * ((float)Math.PI / 180F));
	      float f2 = MathHelper.cos(pY * ((float)Math.PI / 180F)) * MathHelper.cos(pX * ((float)Math.PI / 180F));
	      this.shoot((double)f, (double)f1, (double)f2, pVelocity, pInaccuracy);
	     // Vector3d vector3d = pShooter.getDeltaMovement();
	     // this.setDeltaMovement(this.getDeltaMovement().add(vector3d.x, pShooter.isOnGround() ? 0.0D : vector3d.y, vector3d.z));
	   }
	   
	@Override
	public void onHitEntity(EntityRayTraceResult entityResult)
	{
		if (!this.level.isClientSide) {
			if (!(entityResult.getEntity() instanceof MobEntity)) {
				//this.motionY = 0.0D;
				//setDeltaMovement(getDeltaMovement().x, 0, getDeltaMovement().z);
			}
		}
	}
	
	//@Override
	///protected void onHitBlock(BlockRayTraceResult pResult) 
	//{
	//	if(this.level.isEmptyBlock(pResult.getBlockPos()) && pResult.isInside())
	//	{
	//		this.setPos(getX(), getY() - 1.0D, getZ());
	//	}
	//  }
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
		//this.setDeltaMovement(getDeltaMovement().multiply(1.01D, 1.01D, 1.01D));
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
			
			//this.updateMovement();
			this.move(MoverType.SELF, getDeltaMovement());
			this.onUpdateInAir();
			super.tick();
		}
	}
	
 /*   public void updateMovement()
    {
    	this.checkInsideBlocks();
    	Vector3d vector3d = this.getDeltaMovement();
        double d2 = this.getX() + vector3d.x;
        double d0 = this.getY();
        double d1 = this.getZ() + vector3d.z;
        this.updateRotation();
        float f;
        if (this.isInWater()) {
           for(int i = 0; i < 4; ++i) {
              float f1 = 0.25F;
              this.level.addParticle(ParticleTypes.BUBBLE, d2 - vector3d.x * 0.25D, d0 - vector3d.y * 0.25D, d1 - vector3d.z * 0.25D, vector3d.x, vector3d.y, vector3d.z);
           }

           f = 0.8F;
        } else {
           f = 0.99F;
        }

        this.setDeltaMovement(vector3d.scale((double)f));
        if (!this.isNoGravity()) {
           Vector3d vector3d1 = this.getDeltaMovement();
           this.setDeltaMovement(vector3d1.x, vector3d1.y - 5, vector3d1.z);
        }

        this.setPos(d2, d0, d1);
    } */

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
