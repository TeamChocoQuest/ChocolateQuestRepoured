package team.cqr.cqrepoured.objects.entity.ai.boss.endercalamity;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import team.cqr.cqrepoured.objects.entity.boss.AbstractEntityLaser;
import team.cqr.cqrepoured.objects.entity.boss.endercalamity.EntityCQREnderCalamity;
import team.cqr.cqrepoured.objects.entity.boss.endercalamity.EntityEndLaser;
import team.cqr.cqrepoured.objects.entity.boss.endercalamity.phases.EEnderCalamityPhase;

public class BossAIEndLaser extends AbstractBossAIEnderCalamity {

	private AbstractEntityLaser endlaser;
	private int executionTime = 0;
	
	public BossAIEndLaser(EntityCQREnderCalamity entity) {
		super(entity);
		this.setMutexBits(2);
	}

	@Override
	protected boolean canExecuteDuringPhase(EEnderCalamityPhase currentPhase) {
		return currentPhase == EEnderCalamityPhase.PHASE_LASERING;
	}
	
	@Override
	public void startExecuting() {
		this.executionTime = 0;
		if (this.entity.hasHomePositionCQR()) {
			BlockPos home = this.entity.getHomePositionCQR();
			this.entity.teleport(home.getX(), home.getY(), home.getZ());
		}
	}
	
	@Override
	public void updateTask() {
		super.updateTask();
		
		this.executionTime++;
		
		//Summon laser
		if(this.executionTime == 1) {
			this.createLaser();
		}
		if(this.endlaser != null) {
			//TODO: Fix buggy rotation
			this.entity.rotationPitch = (float) -this.endlaser.rotationPitchCQR;
			this.entity.rotationYaw = (float) -this.endlaser.rotationYawCQR - 90.0F;
			this.entity.rotationYawHead = this.entity.rotationYaw;
		}
	}
	
	private void createLaser() {
		Vec3d laserPosition = this.entity.getPositionVector();
		laserPosition = laserPosition.add(0, this.entity.height / 2, 0);
		//System.out.println("original eyepos: " + eyePos.toString());
		//DONE: Calculate new starting position of laser to match animation
		//Head distance with scale = 100%: 0.75 blocks
		float yaw = this.entity.rotationYaw;
		if(this.entity.hasAttackTarget()) {
			 yaw = (float) Math.toDegrees(Math.atan2(-(this.entity.getAttackTarget().posX - this.entity.posX), this.entity.getAttackTarget().posZ - this.entity.posZ));
		}
		
		AbstractEntityLaser laser = new EntityEndLaser(this.entity.getEntityWorld(), this.entity, 64.0F, 4.0F, -0.01F);
		laser.rotationYawCQR = yaw;
		laser.rotationPitchCQR = 11.25F;
		laser.setPosition(laserPosition.x, laserPosition.y, laserPosition.z);
		this.world.spawnEntity(laser);
		this.endlaser = laser;
	}

	@Override
	public void resetTask() {
		if(this.endlaser != null) {
			this.endlaser.setDead();
			this.endlaser = null;
		}
		this.entity.setCantUpdatePhase(false);
		this.entity.forceTeleport();
		super.resetTask();
	}

}
