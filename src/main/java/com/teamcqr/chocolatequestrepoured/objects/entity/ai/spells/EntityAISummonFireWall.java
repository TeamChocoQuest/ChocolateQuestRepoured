package com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileFireWallPart;
import com.teamcqr.chocolatequestrepoured.util.VectorUtil;

import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;

public class EntityAISummonFireWall extends AbstractEntityAIUseSpell {

	private static final int WALL_LENGTH = 10;
	
	public EntityAISummonFireWall(AbstractEntityCQR entity) {
		super(entity);
	}

	@Override
	protected void castSpell() {
		Vec3d v = new Vec3d(entity.getAttackTarget().getPosition().subtract(entity.getPosition()));
		v = v.normalize();
		Vec3d vR = VectorUtil.rotateVectorAroundY(v, 90);
		Vec3d vL = VectorUtil.rotateVectorAroundY(v, 270);
		Vec3d[] positions = new Vec3d[WALL_LENGTH +2];
		Vec3d startPos = entity.getPositionVector().add(new Vec3d(v.x /2, v.y /2, v.z /2));
		int arrayIndex = 0;
		positions[arrayIndex] = startPos;
		arrayIndex++;
		for(int i = 1; i <= WALL_LENGTH / 2; i++) {
			positions[arrayIndex] = startPos.add(new Vec3d(i* vR.x, i*vR.y, i*vR.z));
			arrayIndex++;
			positions[arrayIndex] = startPos.add(new Vec3d(i* vL.x, i*vL.y, i*vL.z));
			arrayIndex++;
		}
		
		for(Vec3d p : positions) {
			ProjectileFireWallPart wallPart = new ProjectileFireWallPart(entity.world, entity);
			wallPart.setVelocity(p.x, p.y, p.z);
			entity.world.spawnEntity(wallPart);
		}
	}

	@Override
	protected int getCastingTime() {
		return 40;
	}

	@Override
	protected int getCastingInterval() {
		return 80;
	}

	@Override
	protected SoundEvent getSpellPrepareSound() {
		return null;
	}

	@Override
	protected ESpellType getSpellType() {
		return ESpellType.SUMMON_FIRE_WALL;
	}

}
