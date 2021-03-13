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
		System.out.println("original eyepos: " + eyePos.toString());
		//DONE: Calculate new starting position of laser to match animation
		//Head distance with scale = 100%: 0.75 blocks
		//TODO: Add sub entity for the laser itself!
		//TODO: Or bug meldex to change the laser to use a position for it's start rather than an entity or create a special sub-laser entity for exactly that
		Vec3d v = this.entity.getLookVec().normalize();
		System.out.println("vector v: " + v.toString());
		System.out.println("entity scale: " + entity.getSizeVariation());
		v = v.scale(0.75);
		v = v.scale(this.entity.getSizeVariation());
		System.out.println("V after transformation: " + v.toString());
		eyePos = eyePos.add(v);
		System.out.println("Eyepos: " + eyePos.toString());
		AbstractEntityLaser laser = new EntityEndLaserTargeting(this.entity, this.entity.getAttackTarget());
		laser.setPosition(eyePos.x, eyePos.y, eyePos.z);
		this.world.spawnEntity(laser);
		this.projectile = laser;
		//Animation total length: 5s => 100 ticks
		//Animation warmup time: 0.72s => 21 ticks
		//Animation cooldown time: 0.28s => 6 ticks
		//Animation lasering time: 3.94s => 79 ticks
		//5 ticks buffer
		//DONE: Make animation longer to make this longer
		//TODO: Mark AI to not change looking direction when in shooting state
		return 70;
	}

	@Override
	protected boolean canExecuteDuringPhase(EEnderCalamityPhase phase) {
		return phase == EEnderCalamityPhase.PHASE_TELEPORT_LASER;
	}

	@Override
	public int execPrepareShoot() {
		//10 is the transition time of the animation controller
		IMessage message = SPacketCalamityUpdateMainAnimation.builder(this.entity).animate(EntityCQREnderCalamity.ANIM_NAME_SHOOT_LASER).build();
		CQRMain.NETWORK.sendToAllTracking(message, this.entity);
		//10 is the transition time
		//animation warmup is 0.72s => 15 ticks
		//5 ticks is a little buffer
		return 40;
	}
	
	@Override
	public int execAfterShoot() {
		IMessage message = SPacketCalamityUpdateMainAnimation.builder(this.entity).animate(EntityCQREnderCalamity.ANIM_NAME_IDLE_BODY).build();
		CQRMain.NETWORK.sendToAllTracking(message, this.entity);
		//Animation cooldown time: 0.28s => 6 ticks
		//Transition time: 10 ticks
		return 30;
	}
	
	@Override
	public void resetTask() {
		IMessage message = SPacketCalamityUpdateMainAnimation.builder(this.entity).animate(EntityCQREnderCalamity.ANIM_NAME_IDLE_BODY).build();
		CQRMain.NETWORK.sendToAllTracking(message, this.entity);
		super.resetTask();
	}

}
