package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells.AbstractEntityAISpell;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells.IEntityAISpellAnimatedVanilla;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.objects.entity.misc.EntityColoredLightningBolt;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;

public class EntityAISpellWalker extends AbstractEntityAISpell<AbstractEntityCQR> implements IEntityAISpellAnimatedVanilla {

	public EntityAISpellWalker(AbstractEntityCQR entity) {
		super(entity, 600, 100, 1);
		this.setup(true, true, true, true);
	}

	@Override
	public boolean shouldExecute() {
		if (!super.shouldExecute()) {
			return false;
		}
		EntityLivingBase attackTarget = this.entity.getAttackTarget();
		if (attackTarget.isRiding()) {
			Entity entity = attackTarget.getLowestRidingEntity();
			if (entity instanceof EntityLivingBase) {
				attackTarget = (EntityLivingBase) entity;
			}
		}
		return this.isEntityFlying(attackTarget);
	}

	@Override
	public void startCastingSpell() {
		super.startCastingSpell();
		EntityLivingBase attackTarget = this.entity.getAttackTarget();
		EntityColoredLightningBolt coloredLightningBolt = new EntityColoredLightningBolt(this.world, attackTarget.posX, attackTarget.posY, attackTarget.posZ, true, false, 0.8F, 0.35F, 0.1F, 0.3F);
		this.world.spawnEntity(coloredLightningBolt);
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
		return 0.9F;
	}

	@Override
	public float getBlue() {
		return 0.8F;
	}

	private boolean isEntityFlying(EntityLivingBase entity) {
		if (entity.onGround) {
			return false;
		}
		if (entity.collided) {
			return false;
		}
		if (entity.motionY < -0.1D) {
			return false;
		}
		BlockPos pos = new BlockPos(entity);
		int y = 0;
		int count = 0;
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos(pos.getX() + i, 255, pos.getZ() + j);
				if (!this.world.isBlockLoaded(mutablePos)) {
					continue;
				}
				while (mutablePos.getY() > 0 && this.world.getBlockState(mutablePos).getCollisionBoundingBox(this.world, mutablePos) == Block.NULL_AABB) {
					mutablePos.setY(mutablePos.getY() - 1);
				}
				y += mutablePos.getY();
				count++;
			}
		}
		y = count > 0 ? y / count : (int) this.entity.posY;
		if (entity.posY < y + 8) {
			return false;
		}
		return !this.world.checkBlockCollision(entity.getEntityBoundingBox());
	}
}
