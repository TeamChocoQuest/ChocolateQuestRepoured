package team.cqr.cqrepoured.entity.projectiles;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

public abstract class ProjectileBase extends ThrowableProjectile {

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

	protected ProjectileBase(EntityType<? extends ProjectileBase> throwableEntity, Level world) {
		super(throwableEntity, world);
	}

	protected ProjectileBase(EntityType<? extends ProjectileBase> throwableEntity, double pX, double pY, double pZ, Level world) {
		this(throwableEntity, world);
		this.setPos(pX, pY, pZ);
	}

	protected ProjectileBase(EntityType<? extends ProjectileBase> throwableEntity, LivingEntity shooter, Level world) {
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
	protected void onHitBlock(BlockHitResult result) {
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
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

}
