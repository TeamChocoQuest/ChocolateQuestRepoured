package team.cqr.cqrepoured.objects.entity.ai.boss.piratecaptain;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.objects.entity.ai.AbstractCQREntityAI;
import team.cqr.cqrepoured.objects.entity.boss.EntityCQRPirateCaptain;

public class BossAIPirateTeleportBehindEnemy extends AbstractCQREntityAI<EntityCQRPirateCaptain> {

	private static final double MIN_ATTACK_DISTANCE = 8;
	private static final int MAX_COOLDOWN = 60;

	private int cooldown = MAX_COOLDOWN / 2;

	private int timer = 0;

	public BossAIPirateTeleportBehindEnemy(EntityCQRPirateCaptain entity) {
		super(entity);
	}

	@Override
	public boolean shouldExecute() {
		this.cooldown--;
		return this.cooldown <= 0 && this.entity.getAttackTarget() != null && this.entity.getDistance(this.entity.getAttackTarget()) >= MIN_ATTACK_DISTANCE && !(this.entity.isInvisible() || this.entity.isReintegrating() || this.entity.isDisintegrating());
	}

	@Override
	public boolean shouldContinueExecuting() {
		return this.timer < 120 && this.entity.getAttackTarget() != null;
	}

	@Override
	public void updateTask() {
		this.timer++;
		super.updateTask();
		if (this.timer == 10) {
			this.entity.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(CQRItems.DAGGER_NINJA, 1));
		}

		if (this.timer == 100) {
			Vec3d v = this.entity.getAttackTarget().getLookVec().normalize().scale(2);
			Vec3d p = this.entity.getAttackTarget().getPositionVector().subtract(v).add(0, 0.5, 0);
			this.entity.attemptTeleport(p.x, p.y, p.z);
			this.entity.getLookHelper().setLookPositionWithEntity(this.entity.getAttackTarget(), 30, 30);
			this.entity.attackEntityAsMob(this.entity.getAttackTarget());

			this.cooldown = MAX_COOLDOWN;
		}
	}

	@Override
	public void resetTask() {
		super.resetTask();
		this.timer = 0;
		this.entity.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(CQRItems.CAPTAIN_REVOLVER, 1));
	}

}
