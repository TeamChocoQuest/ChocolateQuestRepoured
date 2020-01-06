package com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.util.VectorUtil;

import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class EntityAIExplosionSpell extends AbstractEntityAIUseSpell {

	public EntityAIExplosionSpell(AbstractEntityCQR entity) {
		super(entity);
	}

	@Override
	protected void castSpell() {
		Vec3d centeredPos = new Vec3d(this.entity.getPosition());
		if (this.entity.getAttackTarget() != null && !this.entity.getAttackTarget().isDead) {
			Vec3d v = this.entity.getAttackTarget().getPositionVector().subtract(this.entity.getPositionVector());
			v = new Vec3d(v.x / 2, v.y / 2, v.z / 2);
			centeredPos = centeredPos.add(v);
		}
		int rdmAngle = this.entity.getRNG().nextInt(360);
		Vec3d v = this.entity.getAttackTarget().getPositionVector().subtract(centeredPos);
		v = VectorUtil.rotateVectorAroundY(v, rdmAngle);
		BlockPos explosionPos = this.entity.getAttackTarget().getPosition().add(v.x, v.y, v.z);
		this.entity.world.createExplosion(this.entity, explosionPos.getX(), explosionPos.getY(), explosionPos.getZ(), 3.0F, true);
	}

	@Override
	protected int getCastingTime() {
		return 100;
	}

	@Override
	protected int getCastingInterval() {
		return 300;
	}

	@Override
	protected int getCastWarmupTime() {
		return 120;
	}

	@Override
	protected SoundEvent getSpellPrepareSound() {
		return null;
	}

	@Override
	protected ESpellType getSpellType() {
		return ESpellType.CAST_EXPLOSION;
	}

}
