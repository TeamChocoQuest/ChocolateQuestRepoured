package com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileFireWallPart;
import com.teamcqr.chocolatequestrepoured.util.VectorUtil;

import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;

public class EntityAISummonFireWall extends AbstractEntityAISpell<AbstractEntityCQR> implements IEntityAISpellAnimatedVanilla {

	private static final int WALL_LENGTH = 10;

	public EntityAISummonFireWall(AbstractEntityCQR entity, int cooldown, int chargeUpTicks) {
		super(entity, true, true, cooldown, chargeUpTicks, 1);
	}

	@Override
	public void startCastingSpell() {
		Vec3d v = new Vec3d(this.entity.getAttackTarget().getPosition().subtract(this.entity.getPosition()));
		v = new Vec3d(v.x, 0, v.z);
		v = v.normalize();
		Vec3d vR = VectorUtil.rotateVectorAroundY(v, 90);
		Vec3d vL = VectorUtil.rotateVectorAroundY(v, 270);
		Vec3d[] positions = new Vec3d[WALL_LENGTH + 2];
		Vec3d startPos = this.entity.getPositionVector().add(new Vec3d(v.x / 2, 0, v.z / 2));
		int arrayIndex = 0;
		positions[arrayIndex] = startPos;
		arrayIndex++;
		for (int i = 1; i <= WALL_LENGTH / 2; i++) {
			positions[arrayIndex] = startPos.add(new Vec3d(i * vR.x, 0, i * vR.z));
			arrayIndex++;
			positions[arrayIndex] = startPos.add(new Vec3d(i * vL.x, 0, i * vL.z));
			arrayIndex++;
		}

		for (Vec3d p : positions) {
			if (p != null) {
				ProjectileFireWallPart wallPart = new ProjectileFireWallPart(this.entity.world, this.entity);
				wallPart.setPosition(p.x, p.y, p.z);
				// wallPart.setVelocity(v.x / 2, 0, v.z / 2);
				wallPart.motionX = v.x / 2D;
				wallPart.motionY = 0;
				wallPart.motionZ = v.z / 2D;
				wallPart.velocityChanged = true;
				this.entity.world.spawnEntity(wallPart);
			}
		}
	}

	@Override
	protected SoundEvent getStartChargingSound() {
		return SoundEvents.EVOCATION_ILLAGER_PREPARE_ATTACK;
	}

	@Override
	protected SoundEvent getStartCastingSound() {
		return SoundEvents.ENTITY_ILLAGER_CAST_SPELL;
	}

	@Override
	public int getWeight() {
		return 10;
	}

	@Override
	public boolean ignoreWeight() {
		return false;
	}

	@Override
	public float getRed() {
		return 0.5F;
	}

	@Override
	public float getGreen() {
		return 0.0F;
	}

	@Override
	public float getBlue() {
		return 0.0F;
	}

}
