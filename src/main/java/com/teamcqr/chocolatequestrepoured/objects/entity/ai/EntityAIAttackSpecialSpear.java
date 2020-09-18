package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.objects.items.spears.ItemSpearBase;
import com.teamcqr.chocolatequestrepoured.util.math.BoundingBox;
import com.teamcqr.chocolatequestrepoured.util.math.BoundingBoxHelper;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;

public class EntityAIAttackSpecialSpear extends AbstractEntityAIAttackSpecial {

	public EntityAIAttackSpecialSpear() {
		super(true, false, 10, 100);
	}

	@Override
	public boolean shouldStartAttack(AbstractEntityCQR attacker, EntityLivingBase target) {
		ItemStack stack = attacker.getHeldItemMainhand();
		return stack.getItem() instanceof ItemSpearBase;
	}

	@Override
	public boolean shouldContinueAttack(AbstractEntityCQR attacker, EntityLivingBase target) {
		ItemStack stack = attacker.getHeldItemMainhand();
		return stack.getItem() instanceof ItemSpearBase;
	}

	@Override
	public boolean isInterruptible(AbstractEntityCQR entity) {
		return false;
	}

	@Override
	public void startAttack(AbstractEntityCQR attacker, EntityLivingBase target) {
		attacker.world.playSound(null, attacker.posX, attacker.posY, attacker.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK, attacker.getSoundCategory(), 1.0F, 1.0F);
	}

	@Override
	public void continueAttack(AbstractEntityCQR attacker, EntityLivingBase target, int tick) {

	}

	@Override
	public void stopAttack(AbstractEntityCQR attacker, EntityLivingBase target) {
		Vec3d vec1 = attacker.getPositionEyes(1.0F);
		double x = target.posX - vec1.x;
		double y = MathHelper.clamp(vec1.y, target.posY, target.posY + target.height) - vec1.y;
		double z = target.posZ - vec1.z;
		double dist = MathHelper.sqrt(x * x + z * z);
		double yaw = MathHelper.atan2(-x, z);
		double pitch = MathHelper.atan2(-y, dist);
		Vec3d vec2 = Vec3d.fromPitchYaw((float) Math.toDegrees(pitch), (float) Math.toDegrees(yaw));
		ItemStack stack = attacker.getHeldItemMainhand();
		ItemSpearBase item = ((ItemSpearBase) stack.getItem());
		double reachDistance = attacker.width + 0.85D + item.getReach() * 2.5D;
		BoundingBox bb = new BoundingBox(new Vec3d(-0.25D, -0.25D, 0.0D), new Vec3d(0.25D, 0.25D, reachDistance), yaw, pitch, vec1);

		for (EntityLivingBase entity : BoundingBoxHelper.getEntitiesInsideBB(attacker.world, attacker, EntityLivingBase.class, bb)) {
			if (!attacker.getFaction().isAlly(entity)) {
				// TODO apply enchantments
				entity.attackEntityFrom(DamageSource.causeMobDamage(attacker), 1.0F + item.getAttackDamage());
			}
		}

		Vec3d vec3 = vec1.add(new Vec3d(-0.4D, -0.5D, 0.0D).rotatePitch((float) -pitch).rotateYaw((float) -yaw));
		for (double d = reachDistance; d >= 0.0D; d--) {
			Vec3d vec4 = vec3.add(vec2.scale(d));
			((WorldServer) attacker.world).spawnParticle(EnumParticleTypes.SMOKE_NORMAL, vec4.x, vec4.y, vec4.z, 1, 0.05D, 0.05D, 0.05D, 0.0D);
		}

		attacker.world.playSound(null, attacker.posX, attacker.posY, attacker.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK, attacker.getSoundCategory(), 1.0F, 1.0F);
		attacker.swingArm(EnumHand.MAIN_HAND);
	}

	@Override
	public void resetAttack(AbstractEntityCQR attacker) {

	}

}
