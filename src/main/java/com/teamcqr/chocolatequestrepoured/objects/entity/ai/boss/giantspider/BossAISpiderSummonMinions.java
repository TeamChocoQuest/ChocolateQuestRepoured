package com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.giantspider;

import com.teamcqr.chocolatequestrepoured.objects.entity.ai.AbstractCQREntityAI;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.ISummoner;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRGiantSpider;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.VectorUtil;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

public class BossAISpiderSummonMinions extends AbstractCQREntityAI<EntityCQRGiantSpider> {

	protected ISummoner summoner = null;
	protected int MAX_MINIONS = 6;
	protected int MAX_MINIONS_AT_A_TIME = 3;
	protected ResourceLocation minionOverride = new ResourceLocation("minecraft", "cave_spider");

	private int cooldown = 0;
	private static final int MAX_COOLDOWN = 200;
	private static final int MIN_COOLDOWN = 100;

	public BossAISpiderSummonMinions(EntityCQRGiantSpider spiderqueen) {
		super(spiderqueen);
		this.summoner = spiderqueen;
	}

	@Override
	public boolean shouldExecute() {
		if (this.summoner == null || this.entity == null) {
			return false;
		}
		if (cooldown > 0) {
			cooldown--;
		}
		if (!entity.hasAttackTarget()) {
			return false;
		}
		if (getAliveMinionCount() < MAX_MINIONS) {
			if (entity.getHealth() / entity.getMaxHealth() <= 0.75) {
				return cooldown <= 0;
			}
		}
		return false;
	}

	protected int getAliveMinionCount() {
		int aliveMinions = 0;
		for (Entity minio : this.summoner.getSummonedEntities()) {
			if (minio != null && !minio.isDead) {
				aliveMinions++;
			}
		}
		return aliveMinions;
	}

	@Override
	public void startExecuting() {
		if (this.summoner == null || this.entity == null) {
			return;
		}
		int minionCount = Math.min(MAX_MINIONS_AT_A_TIME, MAX_MINIONS - getAliveMinionCount());
		double angle = 360 / minionCount;
		Vec3d v = new Vec3d(1, 0, 0);
		for (int i = 0; i < minionCount; i++) {
			Vec3d pos = entity.getPositionVector().add(v);
			v = VectorUtil.rotateVectorAroundY(v, angle);

			Entity minion = EntityList.createEntityByIDFromName(minionOverride, entity.world);
			minion.setPosition(pos.x, pos.y, pos.z);
			entity.world.spawnEntity(minion);
			if (this.summoner != null && !this.summoner.getSummoner().isDead) {
				this.summoner.setSummonedEntityFaction(minion);
				this.summoner.addSummonedEntityToList(minion);
			}
		}
		this.cooldown = DungeonGenUtils.getIntBetweenBorders(MIN_COOLDOWN, MAX_COOLDOWN, entity.getRNG());
	}

	@Override
	public boolean shouldContinueExecuting() {
		return false;
	}

}
