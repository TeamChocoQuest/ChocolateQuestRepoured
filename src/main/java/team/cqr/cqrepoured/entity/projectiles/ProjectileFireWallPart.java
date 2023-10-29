package team.cqr.cqrepoured.entity.projectiles;

import net.minecraft.core.Direction;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import team.cqr.cqrepoured.init.CQREntityTypes;

public class ProjectileFireWallPart extends ProjectileBase {

	public ProjectileFireWallPart(EntityType<? extends ProjectileBase> throwableEntity, Level world) {
		super(throwableEntity, world);
	}

	public ProjectileFireWallPart(double pX, double pY, double pZ, Level world) {
		super(CQREntityTypes.PROJECTILE_FIRE_WALL_PART.get(), world);
	}

	public ProjectileFireWallPart(LivingEntity shooter, Level world) {
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
		if (!this.level().isClientSide() && this.level().getBlockState(this.blockPosition().relative(Direction.DOWN)).isCollisionShapeFullBlock(this.level(), this.blockPosition().relative(Direction.DOWN)) && this.random.nextInt(15) == 8) {
			this.level().setBlockAndUpdate(this.blockPosition(), Blocks.FIRE.defaultBlockState());
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
	protected void onHitBlock(BlockHitResult result)
	{
		BlockState state = this.level().getBlockState(result.getBlockPos());

		if(!state.blocksMotion())
		{
			this.level().explode(this.getOwner(), result.getBlockPos().getX(), result.getBlockPos().getY(), result.getBlockPos().getZ(), 0.5F, true, ExplosionInteraction.NONE);
			this.discard();
		}
	}

	@Override
	protected void defineSynchedData() {

	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

}
