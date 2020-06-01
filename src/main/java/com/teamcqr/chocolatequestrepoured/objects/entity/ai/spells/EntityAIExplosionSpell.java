package com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.util.VectorUtil;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class EntityAIExplosionSpell extends AbstractEntityAISpell<AbstractEntityCQR> implements IEntityAISpellAnimatedVanilla {

	public EntityAIExplosionSpell(AbstractEntityCQR entity, int cooldown, int chargeUpTicks) {
		super(entity, true, true, cooldown, chargeUpTicks, 1);
	}

	@Override
	public void startCastingSpell() {
		EntityLivingBase attackTarget = this.entity.getAttackTarget();
		Vec3d centeredPos = new Vec3d(this.entity.getPosition());
		{
			Vec3d v = attackTarget.getPositionVector().subtract(this.entity.getPositionVector());
			v = new Vec3d(v.x / 2, v.y / 2, v.z / 2);
			centeredPos = centeredPos.add(v);
		}
		int rdmAngle = this.entity.getRNG().nextInt(360);
		Vec3d v = attackTarget.getPositionVector().subtract(centeredPos);
		v = VectorUtil.rotateVectorAroundY(v, rdmAngle);
		BlockPos explosionPos = attackTarget.getPosition().add(v.x, v.y, v.z);
		this.entity.world.createExplosion(this.entity, explosionPos.getX(), explosionPos.getY(), explosionPos.getZ(), 3.0F, true);
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
		return 0.0F;
	}

	@Override
	public float getGreen() {
		return 0.6F;
	}

	@Override
	public float getBlue() {
		return 0.0F;
	}

}
