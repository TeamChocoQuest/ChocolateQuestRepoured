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
		return this.cooldown <= 0 && this.entity.getTarget() != null && this.entity.distanceTo(this.entity.getTarget()) >= MIN_ATTACK_DISTANCE && (!this.entity.isInvisible() && !this.entity.isReintegrating() && !this.entity.isDisintegrating());
	}

	@Override
	public boolean canContinueToUse() {
		return this.timer < 120 && this.entity.getTarget() != null;
	}

	@Override
	public void tick() {
		this.timer++;
		super.tick();
		if (this.timer == 10) {
			this.entity.setItemInHand(Hand.MAIN_HAND, new ItemStack(CQRItems.DAGGER_NINJA.get(), 1));
		}

		if (this.timer == 100) {
			Vector3d v = this.entity.getTarget().getLookAngle().normalize().scale(2);
			Vector3d p = this.entity.getTarget().position().subtract(v).add(0, 0.5, 0);
			this.entity.randomTeleport(p.x, p.y, p.z, true); //OLD: attemptTeleport
			this.entity.getLookControl().setLookAt(this.entity.getTarget(), 30, 30);
			this.entity.canAttack(this.entity.getTarget());

			this.cooldown = MAX_COOLDOWN;
		}
	}

	@Override
	public void stop() {
		super.stop();
		this.timer = 0;
		this.entity.setItemInHand(Hand.MAIN_HAND, new ItemStack(CQRItems.CAPTAIN_REVOLVER.get(), 1));
	}

}
