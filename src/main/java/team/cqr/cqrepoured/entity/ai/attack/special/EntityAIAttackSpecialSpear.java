package team.cqr.cqrepoured.entity.ai.attack.special;

import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.item.spear.ItemSpearBase;
import team.cqr.cqrepoured.util.math.BoundingBox;

public class EntityAIAttackSpecialSpear extends AbstractEntityAIAttackSpecial {

	public EntityAIAttackSpecialSpear() {
		super(true, false, 10, 100);
	}

	@Override
	public boolean shouldStartAttack(AbstractEntityCQR attacker, LivingEntity target) {
		ItemStack stack = attacker.getMainHandItem();
		return stack.getItem() instanceof ItemSpearBase;
	}

	@Override
	public boolean shouldContinueAttack(AbstractEntityCQR attacker, LivingEntity target) {
		ItemStack stack = attacker.getMainHandItem();
		return stack.getItem() instanceof ItemSpearBase;
	}

	@Override
	public boolean isInterruptible(AbstractEntityCQR entity) {
		return false;
	}

	@Override
	public void startAttack(AbstractEntityCQR attacker, LivingEntity target) {
		attacker.level.playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), SoundEvents.PLAYER_ATTACK_KNOCKBACK, attacker.getSoundSource(), 1.0F, 1.0F);
	}

	@Override
	public void continueAttack(AbstractEntityCQR attacker, LivingEntity target, int tick) {

	}

	@Override
	public void stopAttack(AbstractEntityCQR attacker, LivingEntity target) {
		Vector3d vec1 = attacker.getEyePosition(1.0F);
		double x = target.getX() - vec1.x;
		double y = MathHelper.clamp(vec1.y, target.getY(), target.getY() + target.getBbHeight()) - vec1.y;
		double z = target.getZ() - vec1.z;
		double dist = MathHelper.sqrt(x * x + z * z);
		double yaw = MathHelper.atan2(-x, z);
		double pitch = MathHelper.atan2(-y, dist);
		Vector3d vec2 = Vector3d.directionFromRotation((float) Math.toDegrees(pitch), (float) Math.toDegrees(yaw));
		ItemStack stack = attacker.getMainHandItem();
		ItemSpearBase item = ((ItemSpearBase) stack.getItem());
		double reachDistance = attacker.getBbWidth() + 0.85D + item.getReach() * 2.5D;
		BoundingBox bb = new BoundingBox(new Vector3d(-0.25D, -0.25D, 0.0D), new Vector3d(0.25D, 0.25D, reachDistance), yaw, pitch, vec1);

		for (LivingEntity entity : BoundingBox.getEntitiesInsideBB(attacker.level, attacker, LivingEntity.class, bb)) {
			if (!attacker.getFaction().isAlly(entity)) {
				// TODO apply enchantments
				entity.hurt(DamageSource.mobAttack(attacker), 1.0F + item.getDamage());
			}
		}

		Vector3d vec3 = vec1.add(new Vector3d(-0.4D, -0.5D, 0.0D).xRot((float) -pitch).yRot((float) -yaw));
		for (double d = reachDistance; d >= 0.0D; d--) {
			Vector3d vec4 = vec3.add(vec2.scale(d));
			((ServerWorld) attacker.level).addParticle(ParticleTypes.SMOKE, vec4.x, vec4.y, vec4.z, 0.05D, 0.05D, 0.05D);
		}

		attacker.level.playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), SoundEvents.PLAYER_ATTACK_KNOCKBACK, attacker.getSoundSource(), 1.0F, 1.0F);
		attacker.swing(Hand.MAIN_HAND);
	}

	@Override
	public void resetAttack(AbstractEntityCQR attacker) {

	}

}
