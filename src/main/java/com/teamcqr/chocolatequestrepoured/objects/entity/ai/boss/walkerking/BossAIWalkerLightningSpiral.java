package com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.walkerking;

import com.teamcqr.chocolatequestrepoured.objects.entity.ai.AbstractCQREntityAI;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRWalkerKing;
import com.teamcqr.chocolatequestrepoured.objects.entity.misc.EntityColoredLightningBolt;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.VectorUtil;

import net.minecraft.util.math.Vec3d;

public class BossAIWalkerLightningSpiral extends AbstractCQREntityAI<EntityCQRWalkerKing> {

	private static final int MIN_COOLDOWN = 120;
	private static final int MAX_COOLDOWN = 240;
	private static final int ANGLE_INCREMENT = 40;
	private static final int RADIUS_INCREMENT = 1;
	private static final int MAX_LIGHTNINGS = 18;

	private int cooldown = 100;
	private int cooldown_circle = 5;
	private int r = 2;
	private int lightningCount = 0;
	private int angle = 0;

	public BossAIWalkerLightningSpiral(EntityCQRWalkerKing entity) {
		super(entity);
	}

	@Override
	public boolean shouldExecute() {
		if (!this.entity.world.isRemote && this.entity != null && !this.entity.isDead && this.entity.getAttackTarget() != null && this.lightningCount < MAX_LIGHTNINGS) {
			this.cooldown--;
			return this.cooldown <= 0;
		}
		return false;
	}

	@Override
	public boolean shouldContinueExecuting() {
		return this.shouldExecute() && this.lightningCount < MAX_LIGHTNINGS;
	}

	@Override
	public void startExecuting() {
		super.startExecuting();
		this.cooldown_circle = 1;
		this.lightningCount = 0;
		this.angle = 0;
		this.r = 2;
	}

	@Override
	public void updateTask() {
		super.updateTask();
		this.cooldown_circle--;
		if (this.cooldown_circle <= 0) {
			this.spawnLightning();
			this.lightningCount++;
			this.cooldown_circle = 5;
		}
	}

	private void spawnLightning() {
		Vec3d v = new Vec3d(this.r, 0, 0);
		v = VectorUtil.rotateVectorAroundY(v, this.angle);
		EntityColoredLightningBolt lightning = new EntityColoredLightningBolt(this.entity.world, this.entity.posX + v.x, this.entity.posY + v.y, this.entity.posZ + v.z, true, false, 0.34F, 0.08F, 0.43F, 0.4F);
		lightning.setPosition(this.entity.posX + v.x, this.entity.posY + v.y, this.entity.posZ + v.z);
		this.entity.world.spawnEntity(lightning);
		this.r += RADIUS_INCREMENT;
		this.angle += ANGLE_INCREMENT;
		if (this.angle >= 360) {
			this.angle -= 360;
		}
	}

	@Override
	public void resetTask() {
		this.r = 2;
		this.lightningCount = 0;
		this.angle = 0;
		this.cooldown = DungeonGenUtils.randomBetween(MIN_COOLDOWN, MAX_COOLDOWN, this.entity.getRNG());
		super.resetTask();
	}

	@Override
	public boolean isInterruptible() {
		return false;
	}

}
