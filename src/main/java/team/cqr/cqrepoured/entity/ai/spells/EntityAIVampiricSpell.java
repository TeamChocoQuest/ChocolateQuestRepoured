package team.cqr.cqrepoured.entity.ai.spells;

import net.minecraft.util.SoundEvents;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.entity.projectiles.ProjectileVampiricSpell;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.util.VectorUtil;

public class EntityAIVampiricSpell extends AbstractEntityAISpell<AbstractEntityCQR> implements IEntityAISpellAnimatedVanilla {

	protected static final int MIN_PROJECTILES = 1;
	protected static final int MAX_PROJECTILES = 5;

	public EntityAIVampiricSpell(AbstractEntityCQR entity, int cooldown, int chargingTicks) {
		super(entity, cooldown, chargingTicks, 1);
		this.setup(true, true, true, false);
	}

	@Override
	public void startCastingSpell() {
		int projectiles = DungeonGenUtils.randomBetween(MIN_PROJECTILES, MAX_PROJECTILES, this.entity.getRandom());

		Vector3d vector = this.entity.getTarget().position().subtract(this.entity.position()).normalize();
		vector = vector.add(vector).add(vector).add(vector);
		double angle = 180D / projectiles;
		vector = VectorUtil.rotateVectorAroundY(vector, 270 + (angle / 2));
		Vector3d velocities[] = new Vector3d[projectiles];
		for (int i = 0; i < projectiles; i++) {
			velocities[i] = VectorUtil.rotateVectorAroundY(vector, angle * i);
		}

		for (Vector3d v : velocities) {
			ProjectileVampiricSpell proj = new ProjectileVampiricSpell(this.entity.level, this.entity);
			// proj.setVelocity(v.x * 0.5, v.y * 0.5, v.z * 0.5);
			/*proj.motionX = v.x * 0.5D;
			proj.motionY = v.y * 0.5D;
			proj.motionZ = v.z * 0.5D;
			proj.velocityChanged = true;*/
			proj.setDeltaMovement(v.scale(0.5D));
			proj.hasImpulse = true;
			this.entity.level.addFreshEntity(proj);
		}
	}

	@Override
	protected SoundEvent getStartChargingSound() {
		return SoundEvents.EVOKER_PREPARE_ATTACK;
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
