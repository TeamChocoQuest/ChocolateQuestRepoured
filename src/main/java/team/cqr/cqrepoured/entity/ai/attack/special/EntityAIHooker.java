package team.cqr.cqrepoured.entity.ai.attack.special;

import net.minecraft.entity.MobEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import team.cqr.cqrepoured.entity.ai.AbstractCQREntityAI;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.entity.projectiles.ProjectileHookShotHook;
import team.cqr.cqrepoured.item.ItemHookshotBase;

import java.util.EnumSet;

public class EntityAIHooker extends AbstractCQREntityAI<AbstractEntityCQR> {

	protected int cooldown = 100;
	protected int MAX_COOLDOWN = 60;
	protected double MAX_RANGE = 256; // = 16 * 16
	protected double MIN_RANGE = 64;

	private ProjectileHookShotHook hook = null;

	protected STATE state = STATE.PREPARING;

	public enum STATE {
		PREPARING, PREPARING_LAUNCH, HOOK_FLYING, HOOK_RETURNED
	}

	public EntityAIHooker(AbstractEntityCQR entity) {
		super(entity);
		//this.setMutexBits(3);
		this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		if (this.hasHookShoot(this.entity)) {
			if (this.cooldown > 0) {
				this.cooldown--;
				return false;
			}
			return this.entity.hasAttackTarget() && this.entity.getSensing().hasLineOfSight(this.entity.getTarget());
		}

		return false;
	}

	@Override
	public void start() {
		super.start();
		this.state = STATE.PREPARING;
		if (this.entity.isPathFinding()) {
			this.entity.getNavigation().stop();
			double dist = this.entity.distanceToSqr(this.entity.getTarget());
			if (dist > this.MAX_RANGE) {
				this.entity.getNavigation().moveTo(this.entity.getTarget(), 1.1);
			} else if (dist >= this.MIN_RANGE) {
				this.entity.getNavigation().stop();
				this.state = STATE.PREPARING_LAUNCH;
			} else {
				// We are too close to our target
				Vec3 v = this.entity.position().subtract(this.entity.getTarget().position()).normalize().scale(6);
				v = v.add(this.entity.position());
				this.entity.getNavigation().moveTo(v.x, v.y, v.z, 1.3);
			}
		}
	}

	@Override
	public void tick() {
		super.tick();

		if (this.hook != null && this.hook.isAlive()) {
			this.entity.getLookControl().setLookAt(this.hook, 30, 30);
		} else if (this.entity.hasAttackTarget()) {
			this.entity.getLookControl().setLookAt(this.entity.getTarget(), 30, 30);
		}

		switch (this.state) {
		case HOOK_FLYING:
			if (this.entity.isPathFinding()) {
				this.entity.getNavigation().stop();
			}
			if (this.hook == null || !this.hook.isAlive()) {
				this.state = STATE.PREPARING;
				this.cooldown = this.MAX_COOLDOWN / 2;
			}
			break;
		case PREPARING:
			double dist = this.entity.distanceToSqr(this.entity.getTarget());
			if (dist > this.MAX_RANGE) {
				this.entity.getNavigation().moveTo(this.entity.getTarget(), 1.1);
			} else if (dist >= 64) {
				this.entity.getNavigation().stop();
				this.state = STATE.PREPARING_LAUNCH;
			} else {
				// We are too close to our target
				Vec3 v = this.entity.position().subtract(this.entity.getTarget().position()).normalize().scale(6);
				v = v.add(this.entity.position());
				this.entity.getNavigation().moveTo(v.x, v.y, v.z, 1.3);
			}
			break;
		case PREPARING_LAUNCH:
			if (this.entity.isPathFinding()) {
				this.entity.getNavigation().stop();
			}
			ItemStack hookItem = this.entity.getMainHandItem();
			if (hookItem.getItem() instanceof ItemHookshotBase) {
				this.hook = ((ItemHookshotBase) hookItem.getItem()).entityAIshoot(this.world, this.entity, this.entity.getTarget(), InteractionHand.OFF_HAND);
				this.state = STATE.HOOK_FLYING;
			}

			break;
		default:
			break;
		}
	}

	@Override
	public boolean canContinueToUse() {
		return super.canContinueToUse() && this.entity.hasAttackTarget() && this.entity.getSensing().hasLineOfSight(this.entity.getTarget()) && this.hasHookShoot(this.entity) && this.cooldown <= 0;
	}

	@Override
	public void stop() {
		super.stop();
		this.hook = null;
		this.state = STATE.PREPARING;
	}

	protected boolean hasHookShoot(MobEntity ent) {
		ItemStack item = ent.getMainHandItem();
		if (!item.isEmpty()) {
			return item.getItem() instanceof ItemHookshotBase;
		}
		return false;
	}

}
