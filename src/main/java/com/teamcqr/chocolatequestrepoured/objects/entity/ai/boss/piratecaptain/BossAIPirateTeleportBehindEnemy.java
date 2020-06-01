package com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.piratecaptain;

import com.teamcqr.chocolatequestrepoured.init.ModItems;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.AbstractCQREntityAI;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRPirateCaptain;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;

public class BossAIPirateTeleportBehindEnemy extends AbstractCQREntityAI<EntityCQRPirateCaptain> {

	private static final double MIN_ATTACK_DISTANCE = 8;
	private static final int MAX_COOLDOWN = 60;
	
	private int cooldown = MAX_COOLDOWN / 2;
	
	private int timer = 0;
	
	public BossAIPirateTeleportBehindEnemy(EntityCQRPirateCaptain entity) {
		super(entity);
	}

	@Override
	public boolean shouldExecute() {
		cooldown--;
		return cooldown <= 0 && entity.getAttackTarget() != null && entity.getDistance(entity.getAttackTarget()) >= MIN_ATTACK_DISTANCE; 
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return timer < 45;
	}
	
	@Override
	public void updateTask() {
		timer++;
		super.updateTask();
		if(timer == 10) {
			entity.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(ModItems.DAGGER_NINJA, 1));
		}
		
		if(timer == 40) {
			Vec3d v = entity.getAttackTarget().getPositionVector().subtract(entity.getPositionVector()).normalize().scale(3);
			Vec3d p = entity.getPositionVector().add(v).addVector(0,0.5,0);
			entity.attemptTeleport(p.x, p.y, p.z);
			entity.getLookHelper().setLookPositionWithEntity(entity.getAttackTarget(), 30, 30);
			entity.attackEntityAsMob(entity.getAttackTarget());
			
			cooldown = MAX_COOLDOWN;
		}
	}
	
	@Override
	public void resetTask() {
		super.resetTask();
		timer = 0;
		this.entity.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(ModItems.CAPTAIN_REVOLVER, 1));
	}

}
