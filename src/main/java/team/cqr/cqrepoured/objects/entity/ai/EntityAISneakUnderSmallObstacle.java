package team.cqr.cqrepoured.objects.entity.ai;

import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;

public class EntityAISneakUnderSmallObstacle<T extends AbstractEntityCQR> extends AbstractCQREntityAI<T> {

	private int crouchTimerCooldown = 30;

	public EntityAISneakUnderSmallObstacle(T entity) {
		super(entity);
		// According to jabelar this makes it compatible with everything...
		this.setMutexBits(8);
	}

	@Override
	public boolean shouldExecute() {
		if (this.entity.isSneaking()) {
			return false;
		}
		return this.areBlocksAtHeadLevelNotAir();
	}

	private boolean areBlocksAtHeadLevelNotAir() {
		final BlockPos pos = this.entity.getPosition().up((int) Math.ceil(this.entity.height) - 1);
		final BlockPos posBeforeMe = new BlockPos(this.entity.getLookVec().normalize().scale(0.25).add(pos.getX(), pos.getY(), pos.getZ()));

		return this.isNotAir(pos) || (this.entity.hasPath() && this.isNotAir(posBeforeMe));
	}

	private boolean isNotAir(final BlockPos pos) {
		final IBlockState blockstate = this.world.getBlockState(pos);
		if ((!Blocks.AIR.isAir(blockstate, this.world, pos) && !(blockstate.getBlock() instanceof BlockAir))) {
			return true;
		}
		return false;
	}

	@Override
	public boolean shouldContinueExecuting() {
		boolean preResult = this.areBlocksAtHeadLevelNotAir();
		if (!preResult) {
			this.crouchTimerCooldown--;
			return this.crouchTimerCooldown > 0;
		} else {
			this.crouchTimerCooldown = 30;
		}
		return true;
	}

	@Override
	public void startExecuting() {
		this.entity.setSneaking(true);
	}

	@Override
	public void resetTask() {
		this.entity.setSneaking(false);
		this.crouchTimerCooldown = 30;
	}

}
