package com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileVampiricSpell;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.VectorUtil;

import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;

public class EntityAIVampiricSpell extends AbstractEntityAIUseSpell {

	protected static final int MIN_PROJECTILES = 1;
	protected static final int MAX_PROJECTILES = 5;

	public EntityAIVampiricSpell(AbstractEntityCQR entity) {
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
			ProjectileVampiricSpell proj = new ProjectileVampiricSpell(this.entity.world, this.entity);
			proj.setVelocity(v.x * 0.5, v.y * 0.5, v.z * 0.5);
			this.entity.world.spawnEntity(proj);
		}
	}

	@Override
	protected int getCastingTime() {
		return 20;
	}

	@Override
	protected int getCastingInterval() {
		return 160;
	}

	@Override
	protected SoundEvent getSpellPrepareSound() {
		return SoundEvents.BLOCK_PORTAL_TRAVEL;
	}

	@Override
	protected ESpellType getSpellType() {
		return ESpellType.STEAL_HEALTH;
	}

}
