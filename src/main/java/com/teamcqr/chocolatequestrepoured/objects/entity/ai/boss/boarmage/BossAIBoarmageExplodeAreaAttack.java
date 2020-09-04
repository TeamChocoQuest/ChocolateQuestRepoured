package com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.boarmage;

import java.util.ArrayList;
import java.util.List;

import com.teamcqr.chocolatequestrepoured.objects.entity.ai.AbstractCQREntityAI;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRBoarmage;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;

import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;

public class BossAIBoarmageExplodeAreaAttack extends AbstractCQREntityAI<EntityCQRBoarmage> {

	private static final int MIN_EXPLOSIONS = 6;
	private static final int MAX_EXPLOSIONS = 12;

	private static final long TIMEDIV = 20;
	private long lastExplodeTechTick = 0;

	private int explosionCount = 0;
	private List<BlockPos> explosions = new ArrayList<>();

	private static final int MIN_COOLDOWN = 20;
	private static final int MAX_COOLDOWN = 60;
	private int cooldown = 40;

	public BossAIBoarmageExplodeAreaAttack(EntityCQRBoarmage entity) {
		super(entity);
	}

	@Override
	public boolean shouldExecute() {
		if (this.cooldown > 0) {
			this.cooldown--;
			return false;
		}
		return this.entity != null && this.entity.isEntityAlive() /*&& this.entity.isExecutingExplodeAreaAttack()*/;
	}

	@Override
	public boolean shouldContinueExecuting() {
		return this.entity != null && this.entity.isEntityAlive() /*&& this.entity.isExecutingExplodeAreaAttack()*/;
	}

	@Override
	public void startExecuting() {
		this.explosionCount = DungeonGenUtils.randomBetween(MIN_EXPLOSIONS, MAX_EXPLOSIONS, this.entity.getRNG());
		this.lastExplodeTechTick = this.entity.ticksExisted;
		this.addExplosionLoc();
	}

	private void addExplosionLoc() {
		if (this.entity.getAttackTarget() != null) {
			this.explosions.add(this.entity.getAttackTarget().getPosition());
		}
	}

	@Override
	public void updateTask() {
		super.updateTask();
		// Particles on positions
		if (this.entity.ticksExisted % 5 == 0) {
			for (BlockPos p : this.explosions) {
				this.world.spawnParticle(EnumParticleTypes.FLAME, p.getX(), p.getY(), p.getZ(), -0.125, 0.125, -0.125, 1);
				this.world.spawnParticle(EnumParticleTypes.FLAME, p.getX(), p.getY(), p.getZ(), -0.125, 0.125, 0.125, 1);
				this.world.spawnParticle(EnumParticleTypes.FLAME, p.getX(), p.getY(), p.getZ(), 0.125, 0.125, -0.125, 1);
				this.world.spawnParticle(EnumParticleTypes.FLAME, p.getX(), p.getY(), p.getZ(), 0.125, 0.125, 0.125, 1);
			}
		}

		if (this.explosions.size() >= this.explosionCount) {
			// EXPLOSION!!!!

			for (BlockPos p : this.explosions) {
				this.world.newExplosion(this.entity, p.getX(), p.getY(), p.getZ(), 3, this.entity.getRNG().nextBoolean(), CQRConfig.bosses.boarmageExplosionAreaDestroysTerrain);
			}

			this.resetTask();
		} else if (Math.abs(this.entity.ticksExisted - this.lastExplodeTechTick) > TIMEDIV) {
			this.entity.swingArm(EnumHand.OFF_HAND);
			this.lastExplodeTechTick = this.entity.ticksExisted;
			this.addExplosionLoc();
		}
	}

	@Override
	public void resetTask() {
		super.resetTask();
		this.explosionCount = 0;
		this.explosions.clear();
		this.cooldown = DungeonGenUtils.randomBetween(MIN_COOLDOWN, MAX_COOLDOWN, this.entity.getRNG());
		//this.entity.stopExplodeAreaAttack();
	}

}
