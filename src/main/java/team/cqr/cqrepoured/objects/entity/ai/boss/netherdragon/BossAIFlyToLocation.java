package team.cqr.cqrepoured.objects.entity.ai.boss.netherdragon;

import net.minecraft.util.math.Vec3d;
import team.cqr.cqrepoured.objects.entity.ai.AbstractCQREntityAI;
import team.cqr.cqrepoured.objects.entity.boss.EntityCQRNetherDragon;

public class BossAIFlyToLocation extends AbstractCQREntityAI<EntityCQRNetherDragon> {

	protected static final double MIN_DISTANCE_TO_REACH = 2;

	protected int cooldown = 0;

	public BossAIFlyToLocation(EntityCQRNetherDragon entity) {
		super(entity);
		this.setMutexBits(1 | 2 | 4);
	}

	@Override
	public boolean shouldExecute() {
		return this.getTargetLocation() != null && !this.entity.isFlyingUp();
	}

	@Override
	public boolean shouldContinueExecuting() {
		return super.shouldContinueExecuting() && this.entity.getPositionVector().distanceTo(this.getTargetLocation()) > MIN_DISTANCE_TO_REACH;
	}

	protected Vec3d getTargetLocation() {
		return this.entity.getTargetLocation();
	}

	@Override
	public void updateTask() {
		super.updateTask();
		if (this.cooldown <= 0) {
			this.cooldown = 10;
			this.entity.getNavigator().tryMoveToXYZ(this.getTargetLocation().x, this.getTargetLocation().y, this.getTargetLocation().z, this.getMovementSpeed());
		}
		this.cooldown--;
	}

	protected double getMovementSpeed() {
		return 0.15;
	}

	@Override
	public void resetTask() {
		super.resetTask();
		this.cooldown = 0;
		this.entity.setTargetLocation(null);
	}

}
