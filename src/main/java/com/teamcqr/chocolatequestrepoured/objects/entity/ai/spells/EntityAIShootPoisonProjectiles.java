package com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectilePoisonSpell;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.VectorUtil;

import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;

/*
 * 20.12.2019
 * Author: DerToaster98
 * Comment: A simple AI that launches a bunch of poison balls at the enemy
 */
public class EntityAIShootPoisonProjectiles extends AbstractEntityAIUseSpell {

	protected static final int MAX_PROJECTILES = 10;
	protected static final int MIN_PROJECTILES = 4;
	protected static final double SPEED_MULTIPLIER = 0.18;

	public EntityAIShootPoisonProjectiles(AbstractEntityCQR entity) {
		super(entity);
	}

	@Override
	protected void castSpell() {
		int projectiles = DungeonGenUtils.getIntBetweenBorders(MIN_PROJECTILES, MAX_PROJECTILES, this.entity.getRNG());

		Vec3d vector = new Vec3d(this.entity.getAttackTarget().getPosition().subtract(this.entity.getPosition())).normalize();
		vector = vector.add(vector).add(vector).add(vector);
		double angle = 180D / (double) projectiles;
		vector = VectorUtil.rotateVectorAroundY(vector, 270 + (angle / 2));
		Vec3d velocities[] = new Vec3d[projectiles];
		for (int i = 0; i < projectiles; i++) {
			velocities[i] = VectorUtil.rotateVectorAroundY(vector, angle * i);
		}

		for (Vec3d v : velocities) {
			ProjectilePoisonSpell proj = new ProjectilePoisonSpell(this.entity.world, this.entity);
			//proj.setVelocity(v.x * SPEED_MULTIPLIER, v.y * SPEED_MULTIPLIER, v.z * SPEED_MULTIPLIER);
			proj.motionX = v.x * SPEED_MULTIPLIER;
			proj.motionY = v.y * SPEED_MULTIPLIER;
			proj.motionZ = v.z * SPEED_MULTIPLIER;
			proj.velocityChanged = true;
			this.entity.world.spawnEntity(proj);
		}
	}

	@Override
	protected int getCastingTime() {
		return 40;
	}

	@Override
	protected int getCastingInterval() {
		return 60;
	}

	@Override
	protected SoundEvent getSpellPrepareSound() {
		return SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE;
	}

	@Override
	protected ESpellType getSpellType() {
		return ESpellType.POISON_TARGET;
	}

}
