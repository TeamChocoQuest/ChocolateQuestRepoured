package team.cqr.cqrepoured.entity.ai.spells;

import net.minecraft.entity.CreatureAttribute;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.phys.Vec3;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.entity.projectiles.ProjectilePoisonSpell;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.util.VectorUtil;

public class EntityAIShootPoisonProjectiles extends AbstractEntityAISpell<AbstractEntityCQR> implements IEntityAISpellAnimatedVanilla {

	protected static final int MAX_PROJECTILES = 10;
	protected static final int MIN_PROJECTILES = 4;
	protected static final double SPEED_MULTIPLIER = 1.5;

	public EntityAIShootPoisonProjectiles(AbstractEntityCQR entity, int cooldown, int chargingTicks) {
		super(entity, cooldown, chargingTicks, 1);
		this.setup(true, true, true, false);
	}

	@Override
	public void startCastingSpell() {
		int projectiles = DungeonGenUtils.randomBetween(MIN_PROJECTILES, MAX_PROJECTILES, this.entity.getRandom());

		Vec3 vector = this.entity.getTarget().position().subtract(this.entity.position()).normalize();
		double angle = 180D / projectiles;
		vector = VectorUtil.rotateVectorAroundY(vector, 270 + (angle / 2));
		Vec3[] velocities = new Vec3[projectiles];
		for (int i = 0; i < projectiles; i++) {
			velocities[i] = VectorUtil.rotateVectorAroundY(vector, angle * i);
		}

		for (Vec3 v : velocities) {
			ProjectilePoisonSpell proj = new ProjectilePoisonSpell(this.entity, this.entity.level);
			// proj.setVelocity(v.x * SPEED_MULTIPLIER, v.y * SPEED_MULTIPLIER, v.z * SPEED_MULTIPLIER);

			if (this.entity.getMobType() == CreatureAttribute.ARTHROPOD) {
				proj.enableAuraPlacement();
			}

			/*proj.motionX = v.x * SPEED_MULTIPLIER;
			proj.motionY = v.y * SPEED_MULTIPLIER;
			proj.motionZ = v.z * SPEED_MULTIPLIER;
			proj.velocityChanged = true;*/
			proj.setDeltaMovement(v.scale(SPEED_MULTIPLIER));
			proj.hasImpulse = true;
			this.entity.level.addFreshEntity(proj);
		}
	}

	@Override
	protected SoundEvent getStartChargingSound() {
		return SoundEvents.ZOMBIE_VILLAGER_CURE;
	}

	@Override
	protected SoundEvent getStartCastingSound() {
		return SoundEvents.EVOKER_CAST_SPELL;
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
		return 0.16F;
	}

	@Override
	public float getGreen() {
		return 0.48F;
	}

	@Override
	public float getBlue() {
		return 0.12F;
	}

}
