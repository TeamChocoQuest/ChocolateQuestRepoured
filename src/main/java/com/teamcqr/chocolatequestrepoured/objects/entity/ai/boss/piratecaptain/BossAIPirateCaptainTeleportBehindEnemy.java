package com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.piratecaptain;

import com.teamcqr.chocolatequestrepoured.objects.entity.ai.AbstractCQREntityAI;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRPirateCaptain;

import net.minecraft.util.math.Vec3d;

public class BossAIPirateCaptainTeleportBehindEnemy extends AbstractCQREntityAI<EntityCQRPirateCaptain> {

	private static final double MIN_ATTACK_DISTANCE = 8;
	private static final int MAX_COOLDOWN = 60;
	
	private int cooldown = MAX_COOLDOWN / 2;
	
	public BossAIPirateCaptainTeleportBehindEnemy(EntityCQRPirateCaptain entity) {
		super(entity);
	}

	@Override
	public boolean shouldExecute() {
		cooldown--;
		return cooldown <= 0 && entity.hasAttackTarget() && entity.getDistance(entity.getAttackTarget()) >= MIN_ATTACK_DISTANCE; 
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return cooldown <= 0;
	}
	
	@Override
	public void updateTask() {
		super.updateTask();
		
		Vec3d v = entity.getAttackTarget().getPositionVector().subtract(entity.getPositionVector()).scale(3);
		Vec3d p = entity.getPositionVector().add(v);
		entity.attemptTeleport(p.x, p.y, p.z);
		entity.getLookHelper().setLookPositionWithEntity(entity.getAttackTarget(), 30, 30);
		entity.attackEntityAsMob(entity.getAttackTarget());
		
		cooldown = MAX_COOLDOWN;
	}

}
