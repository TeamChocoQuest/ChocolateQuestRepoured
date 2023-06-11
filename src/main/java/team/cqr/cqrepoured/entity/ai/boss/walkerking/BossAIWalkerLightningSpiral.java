package team.cqr.cqrepoured.entity.ai.boss.walkerking;

import org.joml.Vector3d;

import team.cqr.cqrepoured.entity.ai.AbstractCQREntityAI;
import team.cqr.cqrepoured.entity.boss.EntityCQRWalkerKing;
import team.cqr.cqrepoured.entity.misc.EntityColoredLightningBolt;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.util.VectorUtil;

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
	public boolean canUse() {
		if (!this.entity.level.isClientSide && !this.entity.isDeadOrDying() && this.entity.getTarget() != null && this.lightningCount < MAX_LIGHTNINGS) {
			this.cooldown--;
			return this.cooldown <= 0;
		}
		return false;
	}

	@Override
	public boolean canContinueToUse() {
		return this.canUse() && this.lightningCount < MAX_LIGHTNINGS;
	}

	@Override
	public void start() {
		super.start();
		this.cooldown_circle = 1;
		this.lightningCount = 0;
		this.angle = 0;
		this.r = 2;
	}

	@Override
	public void tick() {
		super.tick();
		this.cooldown_circle--;
		if (this.cooldown_circle <= 0) {
			this.spawnLightning();
			this.lightningCount++;
			this.cooldown_circle = 5;
		}
	}

	private void spawnLightning() {
		Vector3d v = new Vector3d(this.r, 0, 0);
		v = VectorUtil.rotateVectorAroundY(v, this.angle);
		EntityColoredLightningBolt lightning = new EntityColoredLightningBolt(this.entity.level, this.entity.getX() + v.x, this.entity.getY() + v.y, this.entity.getZ() + v.z, true, false, 0.34F, 0.08F, 0.43F, 0.4F);
		lightning.setPos(this.entity.getX() + v.x, this.entity.getY() + v.y, this.entity.getZ() + v.z);
		this.entity.level.addFreshEntity(lightning);
		this.r += RADIUS_INCREMENT;
		this.angle += ANGLE_INCREMENT;
		if (this.angle >= 360) {
			this.angle -= 360;
		}
	}

	@Override
	public void stop() {
		this.r = 2;
		this.lightningCount = 0;
		this.angle = 0;
		this.cooldown = DungeonGenUtils.randomBetween(MIN_COOLDOWN, MAX_COOLDOWN, this.entity.getRandom());
		super.stop();
	}

	@Override
	public boolean isInterruptable() {
		return false;
	}

}
