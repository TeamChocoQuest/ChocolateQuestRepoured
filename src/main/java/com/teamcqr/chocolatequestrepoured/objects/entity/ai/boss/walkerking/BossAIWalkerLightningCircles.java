package com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.walkerking;

import com.teamcqr.chocolatequestrepoured.objects.entity.ai.AbstractCQREntityAI;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRWalkerKing;
import com.teamcqr.chocolatequestrepoured.objects.entity.misc.EntityColoredLightningBolt;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.VectorUtil;

import net.minecraft.util.math.Vec3d;

public class BossAIWalkerLightningCircles extends AbstractCQREntityAI<EntityCQRWalkerKing> {

	private static final int MIN_COOLDOWN = 200;
	private static final int MAX_COOLDOWN = 300;
	private static final int MAX_CIRCLE_RADIUS = 18;
	
	private int cooldown = 150;
	private int cooldown_circle = 5;
	private int circleRad = 4;
	
	public BossAIWalkerLightningCircles(EntityCQRWalkerKing entity) {
		super(entity);
	}

	@Override
	public boolean shouldExecute() {
		if(!entity.world.isRemote && entity != null && !entity.isDead && entity.getAttackTarget() != null) {
			cooldown--;
			return cooldown <= 0;
		}
		return false;
	}
	
	@Override
	public void startExecuting() {
		super.startExecuting();
		cooldown_circle = 1;
		circleRad = 4;
	}
	
	@Override
	public void updateTask() {
		super.updateTask();
		cooldown_circle--;
		if(cooldown_circle <= 0) {
			spawnLightnings();
			circleRad *= 1.75;
			cooldown_circle = 10;
		}
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return shouldExecute() && circleRad < MAX_CIRCLE_RADIUS;
	}
	
	private void spawnLightnings() {
		int count = 2* circleRad;
		int angle = 360 / count;
		Vec3d v = new Vec3d(circleRad, 0, 0);
		for(int i = 0; i < count; i++) {
			v = VectorUtil.rotateVectorAroundY(v, angle);
			EntityColoredLightningBolt lightning = new EntityColoredLightningBolt(entity.world, entity.posX + v.x, entity.posY + v.y, entity.posZ + v.z, true, false, 0.34F, 0.08F, 0.43F, 0.4F);
			lightning.setPosition(entity.posX + v.x, entity.posY + v.y, entity.posZ + v.z);
			entity.world.spawnEntity(lightning);
		}
	}

	@Override
	public void resetTask() {
		this.circleRad = 4;
		this.cooldown = DungeonGenUtils.randomBetween(MIN_COOLDOWN, MAX_COOLDOWN, this.entity.getRNG());
		super.resetTask();
	}
	
	@Override
	public boolean isInterruptible() {
		return false;
	}

}
