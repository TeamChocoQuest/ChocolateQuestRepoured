package com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.piratecaptain;

import com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells.AbstractEntityAISpell;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells.IEntityAISpellAnimatedVanilla;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRPirateCaptain;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRPirateParrot;
import com.teamcqr.chocolatequestrepoured.util.VectorUtil;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class BossAIPirateSummonParrot extends AbstractEntityAISpell<EntityCQRPirateCaptain> implements IEntityAISpellAnimatedVanilla {

	public BossAIPirateSummonParrot(EntityCQRPirateCaptain entity, int cooldown, int chargingTicks, int castingTicks) {
		super(entity, cooldown, chargingTicks, castingTicks);
		this.setup(true, true, true, false);
	}
	
	@Override
	public boolean shouldExecute() {
		return !entity.hasSpawnedParrot() && super.shouldExecute();
	}
	
	@Override
	public void startCastingSpell() {
		Vec3d v = this.entity.getLookVec().normalize().scale(3);
		v = VectorUtil.rotateVectorAroundY(v, 90);
		if(entity.world.getBlockState(new BlockPos(entity.getPositionVector().add(v).addVector(0,1,0))).getBlock() != Blocks.AIR) {
			v = new Vec3d(0,1,0);
		}
		EntityCQRPirateParrot parrot = new EntityCQRPirateParrot(world);
		parrot.setOwnerId(entity.getUniqueID());
		parrot.setTamed(true);
		parrot.setOwnerId(entity.getUniqueID());
		Vec3d pos = entity.getPositionVector().add(v);
		parrot.setPosition(pos.x, pos.y, pos.z);
		this.entity.world.spawnEntity(parrot);
		this.entity.setSpawnedParrot(true);
	}

	@Override
	public int getWeight() {
		return 1;
	}

	@Override
	public boolean ignoreWeight() {
		return true;
	}

	@Override
	public float getRed() {
		return 1;
	}

	@Override
	public float getGreen() {
		return 0;
	}

	@Override
	public float getBlue() {
		return 0;
	}

}
