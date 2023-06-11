package team.cqr.cqrepoured.entity.projectiles;

import net.minecraft.network.IPacket;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkHooks;
import team.cqr.cqrepoured.init.CQREntityTypes;

public class ProjectileFireWallPart extends ProjectileBase {

	public ProjectileFireWallPart(EntityType<? extends ProjectileBase> throwableEntity, World world) {
		super(throwableEntity, world);
	}

	public ProjectileFireWallPart(double pX, double pY, double pZ, World world) {
		super(CQREntityTypes.PROJECTILE_FIRE_WALL_PART.get(), world);
	}

	public ProjectileFireWallPart(LivingEntity shooter, World world) {
		super(CQREntityTypes.PROJECTILE_FIRE_WALL_PART.get(), shooter, world);
	}

	@Override
	public void push(Entity entityIn) {
		super.push(entityIn);
		if ((!(entityIn instanceof LivingEntity) || !((LivingEntity) entityIn).isBlocking())) {
			entityIn.setSecondsOnFire(4);
		}
	}

	@Override
	protected void onUpdateInAir() {
		super.onUpdateInAir();
		if (!this.level.isClientSide && this.level.getBlockState(this.blockPosition().relative(Direction.DOWN)).isCollisionShapeFullBlock(this.level, this.blockPosition().relative(Direction.DOWN)) && this.random.nextInt(15) == 8) {
			this.level.setBlockAndUpdate(this.blockPosition(), Blocks.FIRE.defaultBlockState());
		}
	}
	
	@Override
	public boolean displayFireAnimation() {
		return true;
	}

/*	@Override
	protected void onHit(RayTraceResult result) {
		if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
			BlockState state = this.world.getBlockState(result.getBlockPos());

			if (!state.getBlock().isPassable(this.world, result.getBlockPos())) {
				if (this.world.isRemote) {
					this.world.newExplosion(this.thrower, result.getBlockPos().getX(), result.getBlockPos().getY(), result.getBlockPos().getZ(), 0.5F, true, false);
				}
				this.setDead();
			}
		}
	} */

	@Override
	protected void onHitBlock(BlockRayTraceResult result)
	{
		BlockState state = this.level.getBlockState(result.getBlockPos());

		if(!state.getMaterial().blocksMotion())
		{
			this.level.explode(this.getOwner(), result.getBlockPos().getX(), result.getBlockPos().getY(), result.getBlockPos().getZ(), 0.5F, true, Explosion.Mode.NONE);
			this.remove();
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
