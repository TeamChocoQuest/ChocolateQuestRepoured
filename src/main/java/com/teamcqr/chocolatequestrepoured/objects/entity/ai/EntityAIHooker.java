package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileHookShotHook;
import com.teamcqr.chocolatequestrepoured.objects.items.ItemHookshotBase;

import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;

public class EntityAIHooker extends AbstractCQREntityAI<AbstractEntityCQR> {

	protected int cooldown = 100;
	protected static final int MAX_COOLDOWN = 60;
	protected static final double REQUIRED_RANGE = 256; // = 16 * 16

	private ProjectileHookShotHook hook = null;

	protected STATE state = STATE.PREPARING;

	enum STATE {
		PREPARING, PREPARING_LAUNCH, HOOK_FLYING, HOOK_RETURNED
	}

	public EntityAIHooker(AbstractEntityCQR entity) {
		super(entity);
		this.setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		if (this.hasHookShoot(this.entity)) {
			if (this.cooldown > 0) {
				this.cooldown--;
				return false;
			}
			return this.entity.hasAttackTarget() && this.entity.getEntitySenses().canSee(this.entity.getAttackTarget());
		}

		return false;
	}

	@Override
	public void startExecuting() {
		super.startExecuting();
		this.state = STATE.PREPARING;
		if (this.entity.hasPath()) {
			this.entity.getNavigator().clearPath();
			double dist = this.entity.getDistanceSq(this.entity.getAttackTarget());
			if (dist > REQUIRED_RANGE) {
				this.entity.getNavigator().tryMoveToEntityLiving(this.entity.getAttackTarget(), 1.1);
			} else if (dist >= 64) {
				this.entity.getNavigator().clearPath();
				this.state = STATE.PREPARING_LAUNCH;
			} else {
				// We are too close to our target
				Vec3d v = this.entity.getPositionVector().subtract(this.entity.getAttackTarget().getPositionVector()).normalize().scale(6);
				v = v.add(this.entity.getPositionVector());
				this.entity.getNavigator().tryMoveToXYZ(v.x, v.y, v.z, 1.3);
			}
		}
	}

	@Override
	public void updateTask() {
		super.updateTask();

		if (this.hook != null && !this.hook.isDead) {
			this.entity.getLookHelper().setLookPositionWithEntity(this.hook, 30, 30);
		} else if (this.entity.hasAttackTarget()) {
			this.entity.getLookHelper().setLookPositionWithEntity(this.entity.getAttackTarget(), 30, 30);
		}

		switch (this.state) {
		case HOOK_FLYING:
			if (this.entity.hasPath()) {
				this.entity.getNavigator().clearPath();
			}
			if (this.hook == null || this.hook.isDead) {
				this.state = STATE.PREPARING;
				this.cooldown = MAX_COOLDOWN / 2;
			}
			break;
		case PREPARING:
			double dist = this.entity.getDistanceSq(this.entity.getAttackTarget());
			if (dist > REQUIRED_RANGE) {
				this.entity.getNavigator().tryMoveToEntityLiving(this.entity.getAttackTarget(), 1.1);
			} else if (dist >= 64) {
				this.entity.getNavigator().clearPath();
				this.state = STATE.PREPARING_LAUNCH;
			} else {
				// We are too close to our target
				Vec3d v = this.entity.getPositionVector().subtract(this.entity.getAttackTarget().getPositionVector()).normalize().scale(6);
				v = v.add(this.entity.getPositionVector());
				this.entity.getNavigator().tryMoveToXYZ(v.x, v.y, v.z, 1.3);
			}
			break;
		case PREPARING_LAUNCH:
			if (this.entity.hasPath()) {
				this.entity.getNavigator().clearPath();
			}
			ItemStack hookItem = this.entity.getHeldItemOffhand();
			if (hookItem.getItem() instanceof ItemHookshotBase) {
				this.hook = ((ItemHookshotBase) hookItem.getItem()).entityAIshoot(this.world, this.entity, this.entity.getAttackTarget(), EnumHand.OFF_HAND);
				this.state = STATE.HOOK_FLYING;
			}

			break;
		default:
			break;
		}
	}

	@Override
	public boolean shouldContinueExecuting() {
		return super.shouldContinueExecuting() && this.entity.hasAttackTarget() && this.entity.getEntitySenses().canSee(this.entity.getAttackTarget()) && this.hasHookShoot(this.entity) && this.cooldown <= 0;
	}

	@Override
	public void resetTask() {
		super.resetTask();
		this.hook = null;
		this.state = STATE.PREPARING;
	}

	protected boolean hasHookShoot(EntityLiving ent) {
		ItemStack item = ent.getHeldItemOffhand();
		if (!item.isEmpty()) {
			return item.getItem() instanceof ItemHookshotBase;
		}
		return false;
	}

}
