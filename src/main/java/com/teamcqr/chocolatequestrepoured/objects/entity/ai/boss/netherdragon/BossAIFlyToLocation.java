package com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.netherdragon;

import com.teamcqr.chocolatequestrepoured.objects.entity.ai.AbstractCQREntityAI;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRNetherDragon;

import net.minecraft.util.math.Vec3d;

public class BossAIFlyToLocation extends AbstractCQREntityAI<EntityCQRNetherDragon> {

	protected static final double MIN_DISTANCE_TO_REACH = 2;

	protected int cooldown = 0;

	public BossAIFlyToLocation(EntityCQRNetherDragon entity) {
		super(entity);
		setMutexBits(1 | 2 | 4);
	}

	@Override
	public boolean shouldExecute() {
		return getTargetLocation() != null && !this.entity.isFlyingUp();
	}

	@Override
	public boolean shouldContinueExecuting() {
		return super.shouldContinueExecuting() && entity.getPositionVector().distanceTo(getTargetLocation()) > MIN_DISTANCE_TO_REACH;
	}

	protected Vec3d getTargetLocation() {
		return this.entity.getTargetLocation();
	}

	@Override
	public void updateTask() {
		super.updateTask();
		if (cooldown <= 0) {
			cooldown = 10;
			this.entity.getNavigator().tryMoveToXYZ(getTargetLocation().x, getTargetLocation().y, getTargetLocation().z, getMovementSpeed());
		}
		cooldown--;
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
