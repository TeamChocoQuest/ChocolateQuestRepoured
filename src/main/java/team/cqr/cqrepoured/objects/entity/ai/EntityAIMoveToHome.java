package team.cqr.cqrepoured.objects.entity.ai;

import net.minecraft.util.math.BlockPos;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;

public class EntityAIMoveToHome extends AbstractCQREntityAI<AbstractEntityCQR> {

	public EntityAIMoveToHome(AbstractEntityCQR entity) {
		super(entity);
		this.setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		if (this.entity.hasHomePositionCQR()) {
			BlockPos pos = this.entity.getHomePositionCQR();
			return this.entity.getDistanceSqToCenter(pos) > 16.0D;
		}
		return false;
	}

	@Override
	public boolean shouldContinueExecuting() {
		return this.entity.hasPath();
	}

	@Override
	public void startExecuting() {
		BlockPos pos = this.entity.getHomePositionCQR();
		this.entity.getNavigator().tryMoveToXYZ(pos.getX(), pos.getY(), pos.getZ(), 1.0D);
	}

	@Override
	public void resetTask() {
		this.entity.getNavigator().clearPath();
	}

}
