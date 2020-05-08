package com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.netherdragon;

import java.util.Random;

import com.teamcqr.chocolatequestrepoured.objects.entity.ai.AbstractCQREntityAI;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRNetherDragon;

import net.minecraft.entity.ai.EntityMoveHelper;

public class BossAIFlyRandomly extends AbstractCQREntityAI<EntityCQRNetherDragon> {

	public BossAIFlyRandomly(EntityCQRNetherDragon entity) {
		super(entity);
		setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		EntityMoveHelper entitymovehelper = this.entity.getMoveHelper();

		if (entity.getAttackTarget() != null /*|| entity.getLastAttackedEntityTime() <= 400*/) {
			return false;
		}
		if (!entitymovehelper.isUpdating()) {
			return true;
		} else {
			double d0 = entitymovehelper.getX() - this.entity.posX;
			double d1 = entitymovehelper.getY() - this.entity.posY;
			double d2 = entitymovehelper.getZ() - this.entity.posZ;
			double d3 = d0 * d0 + d1 * d1 + d2 * d2;
			return d3 < 1.0D || d3 > 3600.0D;
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean shouldContinueExecuting() {
		return false;
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		Random random = this.entity.getRNG();
		double d0 = this.entity.posX + (double) ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
		double d1 = this.entity.posY + (double) ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
		double d2 = this.entity.posZ + (double) ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
		this.entity.getMoveHelper().setMoveTo(d0, d1, d2, 1.0D);
	}

}
