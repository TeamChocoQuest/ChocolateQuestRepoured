package team.cqr.cqrepoured.entity.ai.spells;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.phys.Vec3;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.entity.projectiles.ProjectileFireWallPart;
import team.cqr.cqrepoured.util.VectorUtil;

public class EntityAISummonFireWall extends AbstractEntityAISpell<AbstractEntityCQR> implements IEntityAISpellAnimatedVanilla {

	private static final int WALL_LENGTH = 10;

	public EntityAISummonFireWall(AbstractEntityCQR entity, int cooldown, int chargingTicks) {
		super(entity, cooldown, chargingTicks, 1);
		this.setup(true, true, true, false);
	}

	@Override
	public void startCastingSpell() {
		Vec3 v = this.entity.getTarget().position().subtract(this.entity.position());
		v = new Vec3(v.x, 0, v.z);
		v = v.normalize();
		Vec3 vR = VectorUtil.rotateVectorAroundY(v, 90);
		Vec3 vL = VectorUtil.rotateVectorAroundY(v, 270);
		Vec3[] positions = new Vec3[WALL_LENGTH + 2];
		Vec3 startPos = this.entity.position().add(new Vec3(v.x / 2, 0, v.z / 2));
		int arrayIndex = 0;
		positions[arrayIndex] = startPos;
		arrayIndex++;
		for (int i = 1; i <= WALL_LENGTH / 2; i++) {
			positions[arrayIndex] = startPos.add(new Vec3(i * vR.x, 0, i * vR.z));
			arrayIndex++;
			positions[arrayIndex] = startPos.add(new Vec3(i * vL.x, 0, i * vL.z));
			arrayIndex++;
		}

		for (Vec3 p : positions) {
			if (p != null) {
				ProjectileFireWallPart wallPart = new ProjectileFireWallPart(this.entity, this.entity.level());
				wallPart.setPos(p.x, p.y, p.z);
				// wallPart.setVelocity(v.x / 2, 0, v.z / 2);
				/*wallPart.motionX = v.x / 2D;
				wallPart.motionY = 0;
				wallPart.motionZ = v.z / 2D;
				wallPart.velocityChanged = true;*/
				wallPart.setDeltaMovement(v.x / 2D, 0, v.z / 2D);
				wallPart.hasImpulse = true;
				this.entity.level().addFreshEntity(wallPart);
			}
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
