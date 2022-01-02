package team.cqr.cqrepoured.entity.ai.boss.piratecaptain;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import team.cqr.cqrepoured.entity.ai.AbstractCQREntityAI;
import team.cqr.cqrepoured.entity.boss.EntityCQRPirateCaptain;
import team.cqr.cqrepoured.init.CQRItems;

public class BossAIPirateTeleportBehindEnemy extends AbstractCQREntityAI<EntityCQRPirateCaptain> {

	private static final double MIN_ATTACK_DISTANCE = 8;
	private static final int MAX_COOLDOWN = 60;

	private int cooldown = MAX_COOLDOWN / 2;

	private int timer = 0;

	public BossAIPirateTeleportBehindEnemy(EntityCQRPirateCaptain entity) {
		super(entity);
	}

	@Override
	public boolean canUse() {
		this.cooldown--;
		return this.cooldown <= 0 && this.entity.getAttackTarget() != null && this.entity.getDistance(this.entity.getAttackTarget()) >= MIN_ATTACK_DISTANCE && (!this.entity.isInvisible() && !this.entity.isReintegrating() && !this.entity.isDisintegrating());
	}

	@Override
	public boolean canContinueToUse() {
		return this.timer < 120 && this.entity.getAttackTarget() != null;
	}

	@Override
	public void tick() {
		this.timer++;
		super.tick();
		if (this.timer == 10) {
			this.entity.setHeldItem(Hand.MAIN_HAND, new ItemStack(CQRItems.DAGGER_NINJA, 1));
		}

		if (this.timer == 100) {
			Vector3d v = this.entity.getAttackTarget().getLookVec().normalize().scale(2);
			Vector3d p = this.entity.getAttackTarget().getPositionVector().subtract(v).add(0, 0.5, 0);
			this.entity.attemptTeleport(p.x, p.y, p.z);
			this.entity.getLookHelper().setLookPositionWithEntity(this.entity.getAttackTarget(), 30, 30);
			this.entity.attackEntityAsMob(this.entity.getAttackTarget());

			this.cooldown = MAX_COOLDOWN;
		}
	}

	@Override
	public void stop() {
		super.stop();
		this.timer = 0;
		this.entity.setHeldItem(Hand.MAIN_HAND, new ItemStack(CQRItems.CAPTAIN_REVOLVER, 1));
	}

}
