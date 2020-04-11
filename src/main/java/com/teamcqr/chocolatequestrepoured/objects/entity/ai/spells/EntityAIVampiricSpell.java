package com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileVampiricSpell;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.VectorUtil;

import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.Vec3d;

public class EntityAIVampiricSpell extends AbstractEntityAISpell implements IEntityAISpellAnimatedVanilla {

	protected static final int MIN_PROJECTILES = 1;
	protected static final int MAX_PROJECTILES = 5;

	public EntityAIVampiricSpell(AbstractEntityCQR entity, int cooldown, int chargeUpTicks) {
		super(entity, true, cooldown, chargeUpTicks, 1);
	}

	@Override
	protected void chargeUpSpell() {
		if (this.tick == 0) {
			this.entity.playSound(SoundEvents.BLOCK_PORTAL_TRAVEL, 1.0F, 1.0F);
		}
	}

	@Override
	protected void castSpell() {
		if (this.tick == this.chargeUpTicks) {
			this.entity.playSound(SoundEvents.ENTITY_ILLAGER_CAST_SPELL, 1.0F, 1.0F);
		}
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
			//proj.setVelocity(v.x * 0.5, v.y * 0.5, v.z * 0.5);
			proj.motionX = v.x * 0.5D;
			proj.motionY = v.y * 0.5D;
			proj.motionZ = v.z * 0.5D;
			proj.velocityChanged = true;
			this.entity.world.spawnEntity(proj);
		}
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
		return 1.0F;
	}

	@Override
	public float getGreen() {
		return 0.0F;
	}

	@Override
	public float getBlue() {
		return 1.0F;
	}

}
