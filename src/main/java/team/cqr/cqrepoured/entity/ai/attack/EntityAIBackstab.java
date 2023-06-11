package team.cqr.cqrepoured.entity.ai.attack;

import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.world.entity.LivingEntity;
import software.bernie.shadowed.eliotlash.mclib.utils.MathHelper;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.item.sword.ItemDagger;

public class EntityAIBackstab extends EntityAIAttack {

	public EntityAIBackstab(AbstractEntityCQR entity) {
		super(entity);
	}

	@Override
	public boolean canUse() {
		return this.entity.getMainHandItem().getItem() instanceof ItemDagger && super.canUse();
	}

	@Override
	public boolean canContinueToUse() {
		return this.entity.getMainHandItem().getItem() instanceof ItemDagger && super.canContinueToUse();
	}

	@Override
	public void stop() {
		super.stop();
		this.entity.setPose(Pose.STANDING);
	}

	@Override
	public void tick() {
		super.tick();

		LivingEntity attackTarget = this.entity.getTarget();

		if (attackTarget instanceof AbstractEntityCQR) {
			AbstractEntityCQR target = (AbstractEntityCQR) attackTarget;
			boolean flag = this.entity.distanceToSqr(target) < 20.0D * 20.0D && target.getSensing().canSee(this.entity) && !target.isEntityInFieldOfView(this.entity);
			if(flag) {
				this.entity.setPose(Pose.CROUCHING);
			} else {
				this.entity.setPose(Pose.STANDING);
			}
		}
	}

	@Override
	protected void updatePath(LivingEntity target) {
		double distance = Math.min(4.0D, this.entity.distanceTo(target) * 0.5D);
		double rad = Math.toRadians(target.yRot);
		double sin = MathHelper.sin((float) rad);
		double cos = MathHelper.cos((float) rad);
		PathNavigator navigator = this.entity.getNavigation();
		Path path = null;
		for (int i = 4; path == null && i >= 0; i--) {
			double d = distance * i / 4.0D;
			path = navigator.createPath(target.getX() + sin * d, target.getY(), target.getZ() - cos * d, 1);
		}
		navigator.moveTo(path, 1.0D);//Correct?!
	}

	@Override
	protected void checkAndPerformBlock() {

	}

}
