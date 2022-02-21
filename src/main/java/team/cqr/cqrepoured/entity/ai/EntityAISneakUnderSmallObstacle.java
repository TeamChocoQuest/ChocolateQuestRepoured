package team.cqr.cqrepoured.entity.ai;

import java.util.EnumSet;

import net.minecraft.block.AirBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Pose;
import net.minecraft.util.math.BlockPos;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

public class EntityAISneakUnderSmallObstacle<T extends AbstractEntityCQR> extends AbstractCQREntityAI<T> {

	private int crouchTimerCooldown = 30;

	public EntityAISneakUnderSmallObstacle(T entity) {
		super(entity);
		// According to jabelar this makes it compatible with everything...
		//this.setMutexBits(8);
		this.setFlags(EnumSet.noneOf(Flag.class));
	}

	@Override
	public boolean canUse() {
		if (this.entity.isCrouching()) {
			return false;
		}
		return this.areBlocksAtHeadLevelNotAir();
	}

	private boolean areBlocksAtHeadLevelNotAir() {
		final BlockPos pos = this.entity.blockPosition().above((int) Math.ceil(this.entity.getBbHeight()) - 1);
		final BlockPos posBeforeMe = new BlockPos(this.entity.getLookAngle().normalize().scale(0.25).add(pos.getX(), pos.getY(), pos.getZ()));

		return this.isNotAir(pos) || (this.entity.isPathFinding() && this.isNotAir(posBeforeMe));
	}

	private boolean isNotAir(final BlockPos pos) {
		final BlockState blockstate = this.world.getBlockState(pos);
		if ((!Blocks.AIR.isAir(blockstate, this.world, pos) && !(blockstate.getBlock() instanceof AirBlock))) {
			return true;
		}
		return false;
	}

	@Override
	public boolean canContinueToUse() {
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
	public void start() {
		this.entity.setPose(Pose.CROUCHING);
	}

	@Override
	public void stop() {
		this.entity.setPose(Pose.STANDING);
		this.crouchTimerCooldown = 30;
	}

}
