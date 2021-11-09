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
		//According to jabelar this makes it compatible with everything...
		setMutexBits(8);
	}

	@Override
	public boolean shouldExecute() {
		return this.entity.collidedHorizontally && this.entity.hasPath() && !this.entity.isSneaking();
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		final BlockPos pos = this.entity.getPosition().up();
		final IBlockState blockstate = this.world.getBlockState(pos);
		boolean preResult = this.entity.hasPath() && !(Blocks.AIR.isAir(blockstate, this.world, pos) || blockstate.getBlock() instanceof BlockAir);
		if(!preResult) {
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
