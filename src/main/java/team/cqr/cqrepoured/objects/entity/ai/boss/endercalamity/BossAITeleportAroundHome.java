package team.cqr.cqrepoured.objects.entity.ai.boss.endercalamity;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import team.cqr.cqrepoured.objects.entity.ai.AbstractCQREntityAI;
import team.cqr.cqrepoured.objects.entity.boss.endercalamity.EntityCQREnderCalamity;

public class BossAITeleportAroundHome extends AbstractCQREntityAI<EntityCQREnderCalamity> {

	private final int MAX_COOLDOWN;
	private int cooldown = 40;
	private final double HOVER_DISTANCE = 8D;

	public BossAITeleportAroundHome(EntityCQREnderCalamity entity, final int cooldownTime) {
		super(entity);
		this.MAX_COOLDOWN = cooldownTime;
	}

	@Override
	public int getMutexBits() {
		return 0;
	}

	@Override
	public boolean shouldExecute() {
		if (!this.entity.isDowned() && checkHome()) {
			this.cooldown--;
			return this.cooldown <= 0;
		}
		return false;
	}

	private boolean checkHome() {
		return (this.entity.getHomePositionCQR() != null && this.entity.hasHomePositionCQR());
	}

	@Override
	public void resetTask() {
		super.resetTask();
		this.cooldown = MAX_COOLDOWN;
	}

	@Override
	public void startExecuting() {
		float angle = this.entity.getRNG().nextFloat() * 360F;
		Vec3d vec = new Vec3d(EntityCQREnderCalamity.getArenaRadius(), 0, 0);
		vec = vec.rotateYaw(angle);

		BlockPos home = this.entity.getHomePositionCQR();
		if (home == null) {
			return;
		}
		double x = home.getX() + vec.x;
		double y = home.getY() + HOVER_DISTANCE + vec.y;
		double z = home.getZ() + vec.z;

		this.entity.teleport(x, y, z);

		this.resetTask();
	}

	public void forceExecution() {
		if (this.world.isRemote) {
			return;
		}
		this.startExecuting();
	}

	@Override
	public boolean shouldContinueExecuting() {
		return false;
	}

}
