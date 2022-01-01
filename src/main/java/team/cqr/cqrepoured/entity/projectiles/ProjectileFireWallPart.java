package team.cqr.cqrepoured.entity.projectiles;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ProjectileFireWallPart extends ProjectileBase {

	private Random rdm = new Random();

	public ProjectileFireWallPart(World worldIn) {
		super(worldIn);
		this.setSize(1F, 2.5F);
	}

	public ProjectileFireWallPart(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
		this.setSize(1F, 2.5F);
	}

	public ProjectileFireWallPart(World worldIn, LivingEntity shooter) {
		super(worldIn, shooter);
		this.setSize(1F, 2.5F);
	}

	@Override
	public void applyEntityCollision(Entity entityIn) {
		super.applyEntityCollision(entityIn);
		if ((!(entityIn instanceof LivingEntity) || !((LivingEntity) entityIn).isActiveItemStackBlocking())) {
			entityIn.setFire(4);
		}
	}

	@Override
	public boolean hasNoGravity() {
		return true;
	}

	@Override
	protected void onUpdateInAir() {
		super.onUpdateInAir();
		if (!this.world.isRemote && this.world.getBlockState(this.getPosition().offset(Direction.DOWN)).isFullBlock() && this.rdm.nextInt(15) == 8) {
			this.world.setBlockState(this.getPosition(), Blocks.FIRE.getDefaultState());
		}
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
			BlockState state = this.world.getBlockState(result.getBlockPos());

			if (!state.getBlock().isPassable(this.world, result.getBlockPos())) {
				if (this.world.isRemote) {
					this.world.newExplosion(this.thrower, result.getBlockPos().getX(), result.getBlockPos().getY(), result.getBlockPos().getZ(), 0.5F, true, false);
				}
				this.setDead();
			}
		}
	}

}
