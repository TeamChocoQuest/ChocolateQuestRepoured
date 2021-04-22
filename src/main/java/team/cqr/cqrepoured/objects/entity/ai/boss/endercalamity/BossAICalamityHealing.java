package team.cqr.cqrepoured.objects.entity.ai.boss.endercalamity;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import team.cqr.cqrepoured.objects.entity.boss.endercalamity.EntityCQREnderCalamity;
import team.cqr.cqrepoured.objects.entity.boss.endercalamity.EntityCalamityCrystal;
import team.cqr.cqrepoured.objects.entity.boss.endercalamity.phases.EEnderCalamityPhase;
import team.cqr.cqrepoured.util.VectorUtil;

public class BossAICalamityHealing extends AbstractBossAIEnderCalamity {
	
	protected static final float MAX_HEALTH_PERCENT_TO_START = 0.5F;
	private int cooldown = 0;

	public BossAICalamityHealing(EntityCQREnderCalamity entity) {
		super(entity);
		this.setMutexBits(0);
	}

	@Override
	public int getMutexBits() {
		return 0;
	}

	@Override
	public boolean shouldExecute() {
		if(this.cooldown > 0) {
			this.cooldown--;
			return false;
		}
		return this.internalCheck();
	}
	
	protected boolean internalCheck() {
		if(this.entity.isEntityAlive()) {
			if(this.entity.getHealth() / this.entity.getMaxHealth() <= MAX_HEALTH_PERCENT_TO_START) {
				return super.shouldExecute();
			}
			
		}
		return false;
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return false;
	}

	protected int getCrystalCount() {
		float healthPercent = 1 - this.entity.getHealth() / this.entity.getMaxHealth();
		int crystalCount = 1 + Math.round(4F * healthPercent);
		return crystalCount;
	}
	
	@Override
	public void startExecuting() {
		//Spawn the crystals
		final int crystalCount = this.getCrystalCount();
		BlockPos centralPosition = this.entity.getCirclingCenter().add(0,8,0);
		if(crystalCount > 1) {
			Vec3d direction = this.entity.getLookVec();
			direction = direction.normalize().scale(16);
			double angle = 360 / crystalCount;
			for(int i = 0; i < crystalCount; i++) {
				this.spawnCrystal(centralPosition.add(direction.x, 0, direction.z));
				
				direction = VectorUtil.rotateVectorAroundY(direction, angle);
			}
		} else {
			this.spawnCrystal(centralPosition);
		}
		super.startExecuting();
	}
	
	protected void spawnCrystal(BlockPos position) {
		EntityCalamityCrystal crystal = new EntityCalamityCrystal(this.world, this.entity, position.getX(), position.getY(), position.getZ());
		this.world.spawnEntity(crystal);
	}
	
	@Override
	public void resetTask() {
		this.cooldown = 400;
	}

	@Override
	protected boolean canExecuteDuringPhase(EEnderCalamityPhase currentPhase) {
		return currentPhase.getPhaseObject().canSummonAlliesDuringPhase();
	}
	
}
