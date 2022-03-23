package team.cqr.cqrepoured.entity.ai.boss.endercalamity;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import team.cqr.cqrepoured.entity.boss.endercalamity.EntityCQREnderCalamity;
import team.cqr.cqrepoured.entity.boss.endercalamity.phases.EEnderCalamityPhase;

import java.util.EnumSet;

public class BossAITeleportAroundHome extends AbstractBossAIEnderCalamity {

	private final int MAX_COOLDOWN;
	private int cooldown = 40;
	private final double HOVER_DISTANCE = 12D;

	public BossAITeleportAroundHome(EntityCQREnderCalamity entity, final int cooldownTime) {
		super(entity);
		this.MAX_COOLDOWN = cooldownTime;
		this.setFlags(EnumSet.noneOf(Flag.class));
	}

	/*@Override
	public int getMutexBits() {
		return 0;
	}*/

	@Override
	public boolean canUse() {
		if (super.canUse() && this.checkHome()) {
			this.cooldown--;
			return this.cooldown <= 0;
		}
		return false;
	}

	private boolean checkHome() {
		return (this.entity.getHomePositionCQR() != null && this.entity.hasHomePositionCQR());
	}

	@Override
	public void stop() {
		super.stop();
		this.cooldown = this.MAX_COOLDOWN;
	}

	@Override
	public void start() {
		float angle = this.entity.getRandom().nextFloat() * 360F;
		Vector3d vec = new Vector3d(EntityCQREnderCalamity.getArenaRadius(), 0, 0);
		vec = vec.yRot(angle);

		BlockPos home = this.entity.getHomePositionCQR();
		if (home == null) {
			return;
		}
		double x = home.getX() + vec.x;
		double y = home.getY() + this.HOVER_DISTANCE + vec.y;
		double z = home.getZ() + vec.z;

		this.entity.teleport(x, y, z);

		this.stop();
	}

	public void forceExecution() {
		if (this.world.isClientSide) {
			return;
		}
		this.start();
	}

	@Override
	public boolean canContinueToUse() {
		return false;
	}

	@Override
	protected boolean canExecuteDuringPhase(EEnderCalamityPhase currentPhase) {
		return currentPhase.getPhaseObject().canRandomTeleportDuringPhase();
	}

}
