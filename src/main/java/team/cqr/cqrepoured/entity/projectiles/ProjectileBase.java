package team.cqr.cqrepoured.entity.projectiles;

import net.minecraft.block.BlockState;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public abstract class ProjectileBase extends ThrowableEntity {

	public ProjectileBase(World worldIn) {
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

		if (!state.getBlock().isPassable(this.level, result.getBlockPos())) {
			this.remove();
		}
	}

	protected void onUpdateInAir() {

	}

}
