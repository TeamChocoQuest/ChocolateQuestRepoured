package team.cqr.cqrepoured.objects.entity.ai.boss.endercalamity;

import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.network.server.packet.endercalamity.SPacketCalamityUpdateMainAnimation;
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
		//Animation length: 2,68s => 54 ticks
		//Animation warmup time: 0.72s => 21 ticks
		//Animation cooldown time: 0.28s => 6 ticks
		//2 ticks buffer
		//TODO: Make animation longer to make this longer
		//TODO: Mark AI to not change looking direction when in shooting state
		//TODO: Calculate new starting position of laser to match animation
		return 23;
	}

	@Override
	protected boolean canExecuteDuringPhase(EEnderCalamityPhase phase) {
		return phase == EEnderCalamityPhase.PHASE_TELEPORT_LASER;
	}

	@Override
	public int execPrepareShoot() {
		//40 is the transition time of the animation controller
		IMessage message = SPacketCalamityUpdateMainAnimation.builder(this.entity).animate(EntityCQREnderCalamity.ANIM_NAME_SHOOT_LASER).build();
		CQRMain.NETWORK.sendToAllTracking(message, this.entity);
		//40 is the transition time
		//animation warmup is 0.72s => 15 ticks
		//5 ticks is a little buffer
		return 25;
	}
	
	@Override
	public int execAfterShoot() {
		IMessage message = SPacketCalamityUpdateMainAnimation.builder(this.entity).animate(EntityCQREnderCalamity.ANIM_NAME_IDLE_BODY).build();
		CQRMain.NETWORK.sendToAllTracking(message, this.entity);
		//Animation cooldown time: 0.28s => 6 ticks
		//Transition time: 10 ticks
		return 16;
	}
	
	@Override
	public void resetTask() {
		IMessage message = SPacketCalamityUpdateMainAnimation.builder(this.entity).animate(EntityCQREnderCalamity.ANIM_NAME_IDLE_BODY).build();
		CQRMain.NETWORK.sendToAllTracking(message, this.entity);
		super.resetTask();
	}

}
