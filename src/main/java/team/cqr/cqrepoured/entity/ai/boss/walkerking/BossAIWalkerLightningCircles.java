package team.cqr.cqrepoured.entity.ai.boss.walkerking;

import net.minecraft.util.math.vector.Vector3d;
import team.cqr.cqrepoured.entity.ai.AbstractCQREntityAI;
import team.cqr.cqrepoured.entity.boss.EntityCQRWalkerKing;
import team.cqr.cqrepoured.entity.misc.EntityColoredLightningBolt;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.util.VectorUtil;

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
	public boolean canUse() {
		if (!this.entity.world.isRemote && !this.entity.isDead && this.entity.getAttackTarget() != null) {
			this.cooldown--;
			return this.cooldown <= 0;
		}
		return false;
	}

	@Override
	public void start() {
		super.start();
		this.cooldown_circle = 1;
		this.circleRad = 4;
	}

	@Override
	public void tick() {
		super.tick();
		this.cooldown_circle--;
		if (this.cooldown_circle <= 0) {
			this.spawnLightnings();
			this.circleRad *= 1.75;
			this.cooldown_circle = 10;
		}
	}

	@Override
	public boolean canContinueToUse() {
		return this.canUse() && this.circleRad < MAX_CIRCLE_RADIUS;
	}

	private void spawnLightnings() {
		int count = 2 * this.circleRad;
		int angle = 360 / count;
		Vector3d v = new Vector3d(this.circleRad, 0, 0);
		for (int i = 0; i < count; i++) {
			v = VectorUtil.rotateVectorAroundY(v, angle);
			EntityColoredLightningBolt lightning = new EntityColoredLightningBolt(this.entity.world, this.entity.posX + v.x, this.entity.posY + v.y, this.entity.posZ + v.z, true, false, 0.34F, 0.08F, 0.43F, 0.4F);
			lightning.setPosition(this.entity.posX + v.x, this.entity.posY + v.y, this.entity.posZ + v.z);
			this.entity.world.spawnEntity(lightning);
		}
	}

	@Override
	public void stop() {
		this.circleRad = 4;
		this.cooldown = DungeonGenUtils.randomBetween(MIN_COOLDOWN, MAX_COOLDOWN, this.entity.getRNG());
		super.stop();
	}

	@Override
	public boolean isInterruptable() {
		return false;
	}

}
