package com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityEvokerFangs;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

/*
 * 20.12.2019
 * Made by: DerToaster98
 * Comment: This code is adapted minecraft vanilla code, so it is made by Mojang
 */
public class EntityAIFangAttack extends AbstractEntityAIUseSpell {

	public EntityAIFangAttack(AbstractEntityCQR entity) {
		super(entity);
	}

	@Override
	protected void castSpell() {
		EntityLivingBase entitylivingbase = this.entity.getAttackTarget();
		double d0 = Math.min(entitylivingbase.posY, this.entity.posY);
		double d1 = Math.max(entitylivingbase.posY, this.entity.posY) + 1.0D;
		float f = (float) MathHelper.atan2(entitylivingbase.posZ - this.entity.posZ, entitylivingbase.posX - this.entity.posX);

		if (this.entity.getDistanceSq(entitylivingbase) < 9.0D) {
			for (int i = 0; i < 5; ++i) {
				float f1 = f + (float) i * (float) Math.PI * 0.4F;
				this.spawnFangs(this.entity.posX + (double) MathHelper.cos(f1) * 1.5D, this.entity.posZ + (double) MathHelper.sin(f1) * 1.5D, d0, d1, f1, 0);
			}

			for (int k = 0; k < 8; ++k) {
				float f2 = f + (float) k * (float) Math.PI * 2.0F / 8.0F + ((float) Math.PI * 2F / 5F);
				this.spawnFangs(this.entity.posX + (double) MathHelper.cos(f2) * 2.5D, this.entity.posZ + (double) MathHelper.sin(f2) * 2.5D, d0, d1, f2, 3);
			}
		} else {
			for (int l = 0; l < 16; ++l) {
				double d2 = 1.25D * (double) (l + 1);
				int j = 1 * l;
				this.spawnFangs(this.entity.posX + (double) MathHelper.cos(f) * d2, this.entity.posZ + (double) MathHelper.sin(f) * d2, d0, d1, f, j);
			}
		}
	}

	private void spawnFangs(double p_190876_1_, double p_190876_3_, double p_190876_5_, double p_190876_7_, float p_190876_9_, int p_190876_10_) {
		BlockPos blockpos = new BlockPos(p_190876_1_, p_190876_7_, p_190876_3_);
		boolean flag = false;
		double d0 = 0.0D;

		while (true) {
			if (!this.entity.world.isBlockNormalCube(blockpos, true) && this.entity.world.isBlockNormalCube(blockpos.down(), true)) {
				if (!this.entity.world.isAirBlock(blockpos)) {
					IBlockState iblockstate = this.entity.world.getBlockState(blockpos);
					AxisAlignedBB axisalignedbb = iblockstate.getCollisionBoundingBox(this.entity.world, blockpos);

					if (axisalignedbb != null) {
						d0 = axisalignedbb.maxY;
					}
				}

				flag = true;
				break;
			}

			blockpos = blockpos.down();

			if (blockpos.getY() < MathHelper.floor(p_190876_5_) - 1) {
				break;
			}
		}

		if (flag) {
			EntityEvokerFangs entityevokerfangs = new EntityEvokerFangs(this.entity.world, p_190876_1_, (double) blockpos.getY() + d0, p_190876_3_, p_190876_9_, p_190876_10_, this.entity);
			this.entity.world.spawnEntity(entityevokerfangs);
		}
	}

	@Override
	protected int getCastingTime() {
		return 40;
	}

	@Override
	protected int getCastingInterval() {
		return 120;
	}

	@Override
	protected SoundEvent getSpellPrepareSound() {
		return SoundEvents.EVOCATION_ILLAGER_PREPARE_ATTACK;
	}

	@Override
	protected ESpellType getSpellType() {
		return ESpellType.SUMMON_FANGS;
	}

}
