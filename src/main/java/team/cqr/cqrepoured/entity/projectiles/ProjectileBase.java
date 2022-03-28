package team.cqr.cqrepoured.entity.projectiles;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public abstract class ProjectileBase extends ThrowableEntity {

	/*public ProjectileBase(World worldIn) {
		super(worldIn);
		this.isImmuneToFire = true;
	}

	public ProjectileBase(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
		this.isImmuneToFire = true;
	}

	public ProjectileBase(World worldIn, EntityLivingBase shooter) {
		super(worldIn, shooter);
		this.thrower = shooter;
		this.isImmuneToFire = true;
	} */

	protected ProjectileBase(EntityType<? extends ProjectileBase> throwableEntity, World world) {
		super(throwableEntity, world);
	}

	protected ProjectileBase(EntityType<? extends ProjectileBase> throwableEntity, double pX, double pY, double pZ, World world) {
		this(throwableEntity, world);
		this.setPos(pX, pY, pZ);
	}

	protected ProjectileBase(EntityType<? extends ProjectileBase> throwableEntity, LivingEntity shooter, World world) {
		this(throwableEntity, shooter.getX(), shooter.getEyeY() - (double)0.1F, shooter.getZ(), world);
		this.setOwner(shooter);
	}

	@Override
	public boolean isNoGravity() {
		return true;
	}

	@Override
	public void tick() {
		if (this.tickCount > 400) {
			this.remove();
		}

		super.tick();
		this.onUpdateInAir();
	}

	@Override
	protected void onHitBlock(BlockRayTraceResult result) {
		BlockState state = this.level.getBlockState(result.getBlockPos());
		state.onProjectileHit(this.level, state, result, this);
		if (state.getMaterial().blocksMotion()) {
			this.onDestroyedByBlockImpact();
			this.remove();
		}
		
		super.onHitBlock(result);
	}

	protected void onDestroyedByBlockImpact() {
		
	}

	protected void onUpdateInAir() {

	}
	
	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

}
