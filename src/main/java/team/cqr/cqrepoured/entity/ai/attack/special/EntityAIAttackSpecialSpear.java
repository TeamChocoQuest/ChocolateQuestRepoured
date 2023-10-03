package team.cqr.cqrepoured.entity.ai.attack.special;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
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
		attacker.level().playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), SoundEvents.PLAYER_ATTACK_KNOCKBACK, attacker.getSoundSource(), 1.0F, 1.0F);
	}

	@Override
	public void continueAttack(AbstractEntityCQR attacker, LivingEntity target, int tick) {

	}

	@Override
	public void stopAttack(AbstractEntityCQR attacker, LivingEntity target) {
		Vec3 vec1 = attacker.getEyePosition(1.0F);
		double x = target.getX() - vec1.x;
		double y = Mth.clamp(vec1.y, target.getY(), target.getY() + target.getBbHeight()) - vec1.y;
		double z = target.getZ() - vec1.z;
		double dist = Mth.sqrt((float) (x * x + z * z));
		double yaw = Mth.atan2(-x, z);
		double pitch = Mth.atan2(-y, dist);
		Vec3 vec2 = Vec3.directionFromRotation((float) Math.toDegrees(pitch), (float) Math.toDegrees(yaw));
		ItemStack stack = attacker.getMainHandItem();
		ItemSpearBase item = ((ItemSpearBase) stack.getItem());
		double reachDistance = attacker.getBbWidth() + 0.85D + item.getReach() * 2.5D;
		BoundingBox bb = new BoundingBox(new Vec3(-0.25D, -0.25D, 0.0D), new Vec3(0.25D, 0.25D, reachDistance), yaw, pitch, vec1);

		for (LivingEntity entity : BoundingBox.getEntitiesInsideBB(attacker.level(), attacker, LivingEntity.class, bb)) {
			if (!attacker.getFaction().isAlly(entity)) {
				// TODO apply enchantments
				entity.hurt(attacker.damageSources().mobAttack(attacker), 1.0F + item.getDamage());
			}
		}

		Vec3 vec3 = vec1.add(new Vec3(-0.4D, -0.5D, 0.0D).xRot((float) -pitch).yRot((float) -yaw));
		for (double d = reachDistance; d >= 0.0D; d--) {
			Vec3 vec4 = vec3.add(vec2.scale(d));
			((ServerLevel) attacker.level()).sendParticles(ParticleTypes.SMOKE, vec4.x, vec4.y, vec4.z, 1, 0, 0, 0, 0.05);
		}

		attacker.level().playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), SoundEvents.PLAYER_ATTACK_KNOCKBACK, attacker.getSoundSource(), 1.0F, 1.0F);
		attacker.swing(InteractionHand.MAIN_HAND);
	}

	@Override
	public void resetAttack(AbstractEntityCQR attacker) {

	}

}
