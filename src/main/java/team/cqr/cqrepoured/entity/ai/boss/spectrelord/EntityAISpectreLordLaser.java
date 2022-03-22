package team.cqr.cqrepoured.entity.ai.boss.spectrelord;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import team.cqr.cqrepoured.entity.ai.spells.AbstractEntityAISpell;
import team.cqr.cqrepoured.entity.ai.spells.IEntityAISpellAnimatedVanilla;
import team.cqr.cqrepoured.entity.boss.AbstractEntityLaser;
import team.cqr.cqrepoured.entity.boss.spectrelord.EntityCQRSpectreLord;
import team.cqr.cqrepoured.entity.boss.spectrelord.EntityRotatingLaser;

import java.util.ArrayList;
import java.util.List;

public class EntityAISpectreLordLaser extends AbstractEntityAISpell<EntityCQRSpectreLord> implements IEntityAISpellAnimatedVanilla {

	private LivingEntity target;
	private final List<AbstractEntityLaser> lasers = new ArrayList<>();

	public EntityAISpectreLordLaser(EntityCQRSpectreLord entity, int cooldown, int chargingTicks, int castingTicks) {
		super(entity, cooldown, chargingTicks, castingTicks);
		this.setup(true, true, false, false);
	}

	@Override
	public void resetTask() {
		super.resetTask();
		for (AbstractEntityLaser laser : this.lasers) {
			laser.setDead();
		}
		this.lasers.clear();
	}

	@Override
	public void startChargingSpell() {
		super.startChargingSpell();
		this.target = this.entity.getAttackTarget();
	}

	@Override
	public void startCastingSpell() {
		super.startCastingSpell();
		float yaw = (float) Math.toDegrees(Math.atan2(-(this.target.posX - this.entity.posX), this.target.posZ - this.entity.posZ));
		AbstractEntityLaser laser1 = new EntityRotatingLaser(this.world, this.entity, 32.0F, 1.0F, 0.0F);
		laser1.rotationYawCQR = yaw - 90.0F;
		Vector3d vec1 = Vector3d.fromPitchYaw(0.0F, laser1.rotationYawCQR);
		laser1.setPosition(this.entity.posX + vec1.x * 0.25D, this.entity.posY + this.entity.height * 0.6D + vec1.y * 0.25D, this.entity.posZ + vec1.z * 0.25D);
		this.world.spawnEntity(laser1);
		this.lasers.add(laser1);
		AbstractEntityLaser laser2 = new EntityRotatingLaser(this.world, this.entity, 32.0F, -2.0F, 0.0F);
		laser2.rotationYawCQR = yaw + 90.0F;
		Vector3d vec2 = Vector3d.fromPitchYaw(0.0F, laser2.rotationYawCQR);
		laser2.setPosition(this.entity.posX + vec2.x * 0.25D, this.entity.posY + this.entity.height * 0.6D + vec2.y * 0.25D, this.entity.posZ + vec2.z * 0.25D);
		this.world.spawnEntity(laser2);
		this.lasers.add(laser2);
	}

	@Override
	protected SoundEvent getStartChargingSound() {
		return SoundEvents.EVOCATION_ILLAGER_PREPARE_ATTACK;
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
		return 0.1F;
	}

	@Override
	public float getGreen() {
		return 0.8F;
	}

	@Override
	public float getBlue() {
		return 0.6F;
	}

}
