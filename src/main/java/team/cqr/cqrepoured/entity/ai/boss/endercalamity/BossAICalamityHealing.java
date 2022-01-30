package team.cqr.cqrepoured.entity.ai.boss.endercalamity;

import java.util.EnumSet;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import team.cqr.cqrepoured.entity.boss.endercalamity.EntityCQREnderCalamity;
import team.cqr.cqrepoured.entity.boss.endercalamity.EntityCalamityCrystal;
import team.cqr.cqrepoured.entity.boss.endercalamity.phases.EEnderCalamityPhase;
import team.cqr.cqrepoured.util.VectorUtil;

public class BossAICalamityHealing extends AbstractBossAIEnderCalamity {

	protected static final float MAX_HEALTH_PERCENT_TO_START = 0.5F;
	private int cooldown = 0;

	public BossAICalamityHealing(EntityCQREnderCalamity entity) {
		super(entity);
		//this.setMutexBits(0);
		//Correct??
		this.setFlags(EnumSet.allOf(Flag.class));
	}

	/*@Override
	public int getMutexBits() {
		return 0;
	}*/

	@Override
	public boolean canUse() {
		if (this.cooldown > 0) {
			this.cooldown--;
			return false;
		}
		return this.internalCheck();
	}

	protected boolean internalCheck() {
		if (this.entity.isAlive()) {
			if (this.entity.getHealth() / this.entity.getMaxHealth() <= MAX_HEALTH_PERCENT_TO_START) {
				return super.canUse();
			}

		}
		return false;
	}

	@Override
	public boolean canContinueToUse() {
		return false;
	}

	protected int getCrystalCount() {
		float healthPercent = 1 - this.entity.getHealth() / this.entity.getMaxHealth();
		int crystalCount = 1 + Math.round(4F * healthPercent);
		return crystalCount;
	}

	@Override
	public void start() {
		// Spawn the crystals
		final int crystalCount = this.getCrystalCount();
		BlockPos centralPosition = this.entity.getCirclingCenter().offset(0, 8, 0);
		if (crystalCount > 1) {
			Vector3d direction = this.entity.getLookAngle();
			direction = direction.normalize().scale(16);
			double angle = 360 / crystalCount;
			for (int i = 0; i < crystalCount; i++) {
				this.spawnCrystal(centralPosition.offset(direction.x, 0, direction.z));

				direction = VectorUtil.rotateVectorAroundY(direction, angle);
			}
		} else {
			this.spawnCrystal(centralPosition);
		}
		super.start();
	}

	protected void spawnCrystal(BlockPos position) {
		EntityCalamityCrystal crystal = new EntityCalamityCrystal(this.world, this.entity, position.getX(), position.getY(), position.getZ());
		this.world.addFreshEntity(crystal);
	}

	@Override
	public void stop() {
		this.cooldown = 100;
	}

	@Override
	protected boolean canExecuteDuringPhase(EEnderCalamityPhase currentPhase) {
		return currentPhase.getPhaseObject().canSummonAlliesDuringPhase();
	}

}
