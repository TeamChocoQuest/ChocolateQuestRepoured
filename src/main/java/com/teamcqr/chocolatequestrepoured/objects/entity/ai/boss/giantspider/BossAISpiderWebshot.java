package com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.giantspider;

import com.teamcqr.chocolatequestrepoured.objects.entity.ai.AbstractCQREntityAI;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRGiantSpider;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileBase;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectilePoisonSpell;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileWeb;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.VectorUtil;

import net.minecraft.util.math.Vec3d;

public class BossAISpiderWebshot extends AbstractCQREntityAI<EntityCQRGiantSpider> {

	private static final int MIN_WEBS = 3;
	private static final int MAX_WEBS = 7;
	private static final int MIN_COOLDOWN = 160;
	private static final int MAX_COOLDOWN = 260;
	
	private static final int MAX_DISTANCE_TO_TARGET = 20 * 20;
	protected static final double SPEED_MULTIPLIER = 1.3;
	
	private int cooldown = 100;


	public BossAISpiderWebshot(EntityCQRGiantSpider entity) {
		super(entity);
	}

	@Override
	public boolean shouldExecute() {
		if (this.entity == null || this.entity.isDead) {
			return false;
		}
		if(cooldown > 0) {
			cooldown--;
			return false;
		}
		return entity.hasAttackTarget() && entity.getDistanceSq(entity.getAttackTarget()) < MAX_DISTANCE_TO_TARGET;
	}
	
	@Override
	public void startExecuting() {
		if (this.entity == null) {
			return;
		}
		int projCount = DungeonGenUtils.getIntBetweenBorders(MIN_WEBS, MAX_WEBS, entity.getRNG());
		double angle = 180 / projCount;
		Vec3d v = entity.getAttackTarget().getPositionVector().subtract(entity.getPositionVector()).normalize();
		for(int i = - (projCount /2); i <= (projCount /2); i++) {
			Vec3d velo = VectorUtil.rotateVectorAroundY(v, i* angle);
			velo = velo.add(0, 0.1, 0);
			
			ProjectileBase web = entity.getRNG().nextDouble() > 0.8 ? new ProjectilePoisonSpell(entity.world, entity) : new ProjectileWeb(entity.world, entity);
			web.motionX = velo.x * SPEED_MULTIPLIER;
			web.motionY = velo.y * SPEED_MULTIPLIER;
			web.motionZ = velo.z * SPEED_MULTIPLIER;
			web.velocityChanged = true;
			this.entity.world.spawnEntity(web);
			
		}
		this.cooldown = DungeonGenUtils.getIntBetweenBorders(MIN_COOLDOWN, MAX_COOLDOWN, entity.getRNG());
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return false;
	}

}
