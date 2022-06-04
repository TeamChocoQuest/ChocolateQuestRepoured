package team.cqr.cqrepoured.entity.projectiles;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.fml.network.NetworkHooks;
import team.cqr.cqrepoured.entity.mobs.EntityCQREnderman;
import team.cqr.cqrepoured.init.CQRBlockTags;
import team.cqr.cqrepoured.init.CQREntityTypes;

public class ProjectileHomingEnderEye extends ProjectileBase {

	private Entity target = null;

/*	public ProjectileHomingEnderEye(World worldIn) {
		super(worldIn);
	}

	public ProjectileHomingEnderEye(World worldIn, LivingEntity shooter, Entity target) {
		super(worldIn, shooter);
		this.shooter = shooter;
		this.target = target;
		this.isImmuneToFire = true;
	} */

	public ProjectileHomingEnderEye(EntityType<? extends ProjectileBase> throwableEntity, World world) {
		super(throwableEntity, world);
	}

	public ProjectileHomingEnderEye(double pX, double pY, double pZ, World world) {
		super(CQREntityTypes.PROJECTILE_HOMING_ENDER_EYE.get(), world);
	}

	public ProjectileHomingEnderEye(LivingEntity shooter, World world, Entity target) {
		super(CQREntityTypes.PROJECTILE_HOMING_ENDER_EYE.get(), shooter, world);
		//this.shooter = shooter;
		this.setOwner(shooter);
		this.target = target;
	}
	
	@Override
	public void tick() {
		double dx = this.getX() + (-0.25 + (0.5 * this.level.random.nextDouble()));
		double dy = 0.125 + this.getY() + (-0.25 + (0.5 * this.level.random.nextDouble()));
		double dz = this.getZ() + (-0.25 + (0.5 * this.level.random.nextDouble()));
		this.level.addParticle(ParticleTypes.DRAGON_BREATH, dx, dy, dz, 0, 0, 0);
		
		super.tick();
	}
	
	@Override
	protected boolean canHitEntity(Entity pTarget) {
		return super.canHitEntity(pTarget) && pTarget != this.getOwner();
	}

	@Override
	protected void onHit(RayTraceResult result)
	{
		AreaEffectCloudEntity entityareaeffectcloud = new AreaEffectCloudEntity(this.level, this.getX(), this.getY(), this.getZ());
		entityareaeffectcloud.setOwner(this.getOwner() instanceof LivingEntity ? (LivingEntity) this.getOwner() : null);
		entityareaeffectcloud.setParticle(ParticleTypes.DRAGON_BREATH);
		entityareaeffectcloud.setRadius(2F);
		entityareaeffectcloud.setDuration(200);
		entityareaeffectcloud.setRadiusOnUse(-0.25F);
		entityareaeffectcloud.setWaitTime(10);
		entityareaeffectcloud.setRadiusPerTick(-entityareaeffectcloud.getRadius() / entityareaeffectcloud.getDuration());
		entityareaeffectcloud.addEffect(new EffectInstance(Effects.HARM, 20, 1));

		this.level.addFreshEntity(entityareaeffectcloud);

		super.onHit(result);
	}

	@Override
	public void onHitEntity(EntityRayTraceResult entityResult)
	{
		if(entityResult.getEntity() != this.getOwner() && !(entityResult.getEntity() instanceof PartEntity))
		{
			this.push(entityResult.getEntity());
		}
		if(entityResult.getEntity() == this.getOwner()) {
			return;
		}
		super.onHitEntity(entityResult);
	}

	@Override
	protected void onHitBlock(BlockRayTraceResult result) {
		super.onHitBlock(result);
		this.level.explode(this.getOwner(), this.getX(), this.getY(), this.getZ(), 2, Explosion.Mode.NONE);
		this.remove();
		
		BlockPos blockPos = this.blockPosition();
		this.removeBlocksRecursively(blockPos.mutable(), 0, this.level.getDifficulty().ordinal() * 3);
	}
	
	protected void removeBlocksRecursively(BlockPos.Mutable blockPos, int recursionDepth, final int maxRecursion) {
		BlockState state = this.level.getBlockState(blockPos);
		Block block = state.getBlock();
		if(block.is(CQRBlockTags.HOMING_ENDER_EYE_DESTROYABLE)) {
			this.level.destroyBlock(blockPos, state.hasTileEntity() || this.level.getRandom().nextBoolean());
			
			this.level.addParticle(ParticleTypes.LARGE_SMOKE, this.getX(), this.getY(), this.getZ(), 0.0, 0.025, 0.0);
			this.level.addParticle(ParticleTypes.LARGE_SMOKE, this.getX(), this.getY(), this.getZ(), 0.025, 0.01, 0.025);
			this.level.addParticle(ParticleTypes.LARGE_SMOKE, this.getX(), this.getY(), this.getZ(), 0.025, 0.01, -0.025);
			this.level.addParticle(ParticleTypes.LARGE_SMOKE, this.getX(), this.getY(), this.getZ(), -0.025, 0.01, 0.025);
			this.level.addParticle(ParticleTypes.LARGE_SMOKE, this.getX(), this.getY(), this.getZ(), -0.025, 0.01, -0.025);
			
			recursionDepth++;
			if(recursionDepth < maxRecursion) {
				switch(this.level.getRandom().nextInt(6)) {
					case 0:
						this.removeBlocksRecursively(blockPos.above().mutable(), recursionDepth, maxRecursion);
						break;
					case 1:
						this.removeBlocksRecursively(blockPos.below().mutable(), recursionDepth, maxRecursion);
						break;
					case 2:
						this.removeBlocksRecursively(blockPos.north().mutable(), recursionDepth, maxRecursion);
						break;
					case 3:
						this.removeBlocksRecursively(blockPos.east().mutable(), recursionDepth, maxRecursion);
						break;
					case 4:
						this.removeBlocksRecursively(blockPos.south().mutable(), recursionDepth, maxRecursion);
						break;
					case 5:
						this.removeBlocksRecursively(blockPos.west().mutable(), recursionDepth, maxRecursion);
						break;
					default:
						break;
				}
			}
		}
	}

	@Override
	public void push(Entity entityIn) {
		if (entityIn == null) {
			return;
		}
		if (entityIn == this.getOwner()) {
			return;
		}
		if (entityIn instanceof ProjectileBase || entityIn instanceof EndermanEntity || entityIn instanceof EntityCQREnderman) {
			return;
		}
		boolean hitTarget = this.target != null && entityIn != this.getOwner();
		if (hitTarget) {
			this.level.explode(this.getOwner(), this.getX(), this.getY(), this.getZ(), 2, Explosion.Mode.NONE);
			this.remove();
		}
		if (this.getOwner() != null && this.getOwner() instanceof LivingEntity) {
			entityIn.hurt(DamageSource.indirectMobAttack(this, (LivingEntity) this.getOwner()), 2 + this.level.getDifficulty().getId());
		}
	}

	@Override
	protected void onUpdateInAir() {
		super.onUpdateInAir();
		if (this.tickCount > 400 && !this.level.isClientSide) {
			this.level.explode(this.getOwner(), this.getX(), this.getY(), this.getZ(), 2, Explosion.Mode.NONE);
			this.remove();
			return;
		}
		if (!this.level.isClientSide && this.target != null) {
			Vector3d v = this.target.position().subtract(this.position());
			v = v.normalize();
			v = v.scale(0.2);

			this.setDeltaMovement(v);
			//this.motionX = v.x;
			//this.motionY = v.y;
			//this.motionZ = v.z;
			//this.velocityChanged = true;
		}
	}

	@Override
	protected void defineSynchedData() {

	}

	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}