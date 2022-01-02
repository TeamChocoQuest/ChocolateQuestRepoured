package team.cqr.cqrepoured.entity.ai;

import net.minecraft.util.math.vector.Vector3d;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

public class EntityAIFollowAttackTarget extends AbstractCQREntityAI<AbstractEntityCQR> {

	private int ticksWaiting;

	public EntityAIFollowAttackTarget(AbstractEntityCQR entity) {
		super(entity);
		this.setMutexBits(3);
	}

	@Override
	public boolean canUse() {
		return this.entity.getAttackTarget() != null;
	}

	@Override
	public boolean canContinueToUse() {
		if (this.entity.getAttackTarget() == null) {
			return false;
		}
		if (this.entity.hasPath()) {
			return true;
		}
		if (this.ticksWaiting < 100) {
			return true;
		}
		this.entity.setTarget(null);
		return false;
	}

	@Override
	public void start() {
		Vector3d v = this.entity.getLastPosAttackTarget();
		this.entity.getNavigator().tryMoveToXYZ(v.x, v.y, v.z, 1.0D);
	}

	@Override
	public void stop() {
		this.entity.getNavigator().clearPath();
	}

	@Override
	public void tick() {
		if (this.entity.getLastTimeSeenAttackTarget() + 100 >= this.entity.ticksExisted) {
			Vector3d v = this.entity.getLastPosAttackTarget();
			this.entity.getNavigator().tryMoveToXYZ(v.x, v.y, v.z, 1.0D);
		}
		if (!this.entity.hasPath()) {
			this.ticksWaiting++;
		} else {
			this.ticksWaiting = 0;
		}
	}

}
