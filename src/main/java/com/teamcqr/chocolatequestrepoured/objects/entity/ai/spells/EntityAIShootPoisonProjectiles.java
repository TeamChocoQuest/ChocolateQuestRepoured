package com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectilePoisonSpell;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.VectorUtil;

import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;

public class EntityAIShootPoisonProjectiles extends AbstractEntityAIUseSpell {

	protected static final int MAX_PROJECTILES = 10;
	protected static final int MIN_PROJECTILES = 4;
	protected static final double SPEED_MULTIPLIER = 0.125;
	
	public EntityAIShootPoisonProjectiles(AbstractEntityCQR entity) {
		super(entity);
	}

	@Override
	protected void castSpell() {
		int projectiles = DungeonGenUtils.getIntBetweenBorders(MIN_PROJECTILES, MAX_PROJECTILES, entity.getRNG());
		
		Vec3d vector = new Vec3d(entity.getAttackTarget().getPosition().subtract(entity.getPosition())).normalize();
		vector = vector.add(vector).add(vector).add(vector);
		double angle = 180D / (double)projectiles;
		vector = VectorUtil.rotateVectorAroundY(vector, 270 + (angle /2));
		Vec3d velocities[] = new Vec3d[projectiles];
		for(int i = 0; i < projectiles; i++) {
			velocities[i] = VectorUtil.rotateVectorAroundY(vector, angle*i);
		}
		
		for(Vec3d v : velocities) {
			ProjectilePoisonSpell proj = new ProjectilePoisonSpell(entity.world, entity);
			proj.setVelocity(v.x * SPEED_MULTIPLIER, v.y * SPEED_MULTIPLIER, v.z * SPEED_MULTIPLIER);
			entity.world.spawnEntity(proj);
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
		return ESpellType.POISON_PLAYER;
	}

}
