package team.cqr.cqrepoured.entity.ai.boss.giantspider;

import net.minecraft.util.math.vector.Vector3d;
import team.cqr.cqrepoured.entity.ai.AbstractCQREntityAI;
import team.cqr.cqrepoured.entity.boss.EntityCQRGiantSpider;
import team.cqr.cqrepoured.entity.projectiles.ProjectileBase;
import team.cqr.cqrepoured.entity.projectiles.ProjectilePoisonSpell;
import team.cqr.cqrepoured.entity.projectiles.ProjectileWeb;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.util.VectorUtil;

public class BossAISpiderWebshot extends AbstractCQREntityAI<EntityCQRGiantSpider> {

	private static final int MIN_WEBS = 3;
	private static final int MAX_WEBS = 7;
	private static final int MIN_COOLDOWN = 80;
	private static final int MAX_COOLDOWN = 120;

	private static final int MAX_DISTANCE_TO_TARGET = 20 * 20;
	protected static final double SPEED_MULTIPLIER = 1.3;

	private int cooldown = 100;

	public BossAISpiderWebshot(EntityCQRGiantSpider entity) {
		super(entity);
	}

	@Override
	public boolean canUse() {
		if (this.entity == null || this.entity.isDeadOrDying()) {
			return false;
		}
		if (this.cooldown > 0) {
			this.cooldown--;
			return false;
		}
		return this.entity.hasAttackTarget() && this.entity.distanceToSqr(this.entity.getTarget()) < MAX_DISTANCE_TO_TARGET;
	}

	@Override
	public void start() {
		if (this.entity == null) {
			return;
		}
		int projCount = DungeonGenUtils.randomBetween(MIN_WEBS, MAX_WEBS, this.entity.getRandom());
		double angle = 180 / projCount;
		Vector3d v = this.entity.getTarget().position().subtract(this.entity.position()).normalize();
		for (int i = -(projCount / 2); i <= (projCount / 2); i++) {
			Vector3d velo = VectorUtil.rotateVectorAroundY(v, i * angle);
			velo = velo.add(0, 0.1, 0);

			ProjectileBase web = this.entity.getRandom().nextDouble() > 0.8 ? new ProjectilePoisonSpell(this.entity.level, this.entity) : new ProjectileWeb(this.entity, this.entity.level);
			/*web.motionX = velo.x * SPEED_MULTIPLIER;
			web.motionY = velo.y * SPEED_MULTIPLIER;
			web.motionZ = velo.z * SPEED_MULTIPLIER;
			web.velocityChanged = true;*/
			web.setDeltaMovement(velo.scale(SPEED_MULTIPLIER));
			web.hasImpulse = true;
			this.entity.level.addFreshEntity(web);

		}
		this.cooldown = DungeonGenUtils.randomBetween(MIN_COOLDOWN, MAX_COOLDOWN, this.entity.getRandom());
	}

	@Override
	public boolean canContinueToUse() {
		return false;
	}

}
