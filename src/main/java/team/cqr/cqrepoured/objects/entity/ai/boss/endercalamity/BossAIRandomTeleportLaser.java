package team.cqr.cqrepoured.objects.entity.ai.boss.endercalamity;

import net.minecraft.util.math.Vec3d;
import team.cqr.cqrepoured.objects.entity.boss.AbstractEntityLaser;
import team.cqr.cqrepoured.objects.entity.boss.endercalamity.EntityCQREnderCalamity;
import team.cqr.cqrepoured.objects.entity.boss.endercalamity.EntityEndLaserTargeting;
import team.cqr.cqrepoured.objects.entity.boss.endercalamity.phases.EEnderCalamityPhase;

public class BossAIRandomTeleportLaser extends AbstractBossAIRandomShoot {

	public BossAIRandomTeleportLaser(EntityCQREnderCalamity entity) {
		super(entity);
	}

	@Override
	protected int execRandomShoot() {
		Vec3d eyePos = this.entity.getPositionEyes(1);
		AbstractEntityLaser laser = new EntityEndLaserTargeting(this.entity, this.entity.getAttackTarget());
		laser.setPosition(eyePos.x, eyePos.y, eyePos.z);
		this.world.spawnEntity(laser);
		this.projectile = laser;
		return 18;
	}

	@Override
	protected boolean canExecuteDuringPhase(EEnderCalamityPhase phase) {
		return phase == EEnderCalamityPhase.PHASE_TELEPORT_LASER;
	}

}
