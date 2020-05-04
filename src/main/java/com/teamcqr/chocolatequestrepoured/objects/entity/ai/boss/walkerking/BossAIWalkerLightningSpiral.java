package com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.walkerking;

import com.teamcqr.chocolatequestrepoured.objects.entity.ai.AbstractCQREntityAI;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.objects.entity.misc.EntityColoredLightningBolt;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.VectorUtil;

import net.minecraft.util.math.Vec3d;

public class BossAIWalkerLightningSpiral extends AbstractCQREntityAI {
	
	private static final int MIN_COOLDOWN = 120;
	private static final int MAX_COOLDOWN = 260;
	private static final int ANGLE_INCREMENT = 40;
	private static final int RADIUS_INCREMENT = 1;
	private static final int MAX_LIGHTNINGS = 18;
	
	private int cooldown = 150;
	private int cooldown_circle = 5;
	private int r = 2;
	private int lightningCount = 0;
	private int angle = 0;
	
	public BossAIWalkerLightningSpiral(AbstractEntityCQR entity) {
		super(entity);
	}

	@Override
	public boolean shouldExecute() {
		if(!entity.world.isRemote && entity != null && !entity.isDead && entity.getAttackTarget() != null && lightningCount < MAX_LIGHTNINGS) {
			cooldown--;
			return cooldown <= 0;
		}
		return false;
	}
	@Override
	public boolean shouldContinueExecuting() {
		return shouldExecute() && lightningCount < MAX_LIGHTNINGS;
	}
	
	@Override
	public void startExecuting() {
		super.startExecuting();
		cooldown_circle = 1;
		lightningCount = 0;
		angle = 0;
		r = 2;
	}
	
	@Override
	public void updateTask() {
		super.updateTask();
		cooldown_circle--;
		if(cooldown_circle <= 0) {
			spawnLightning();
			lightningCount++;
			cooldown_circle = 5;
		}
	}

	private void spawnLightning() {
		Vec3d v = new Vec3d(r, 0, 0);
		v = VectorUtil.rotateVectorAroundY(v, angle);
		EntityColoredLightningBolt lightning = new EntityColoredLightningBolt(entity.world, entity.posX + v.x, entity.posY + v.y, entity.posZ + v.z, true, false, 0.34F, 0.08F, 0.43F, 0.4F);
		lightning.setPosition(entity.posX + v.x, entity.posY + v.y, entity.posZ + v.z);
		entity.world.spawnEntity(lightning);
		r += RADIUS_INCREMENT;
		angle += ANGLE_INCREMENT;
		if(angle >= 360) {
			angle -= 360;
		}
	}
	
	@Override
	public void resetTask() {
		this.r = 2;
		lightningCount = 0;
		angle = 0;
		this.cooldown = DungeonGenUtils.getIntBetweenBorders(MIN_COOLDOWN, MAX_COOLDOWN, entity.getRNG());
		super.resetTask();
	}
	
	@Override
	public boolean isInterruptible() {
		return false;
	}

}
