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

/**
 * This code is adapted minecraft vanilla code, so it is made by Mojang
 */
public class EntityAIFangAttack extends AbstractEntityAISpell<AbstractEntityCQR> implements IEntityAISpellAnimatedVanilla {

	public EntityAIFangAttack(AbstractEntityCQR entity, int cooldown, int chargingTicks) {
		super(entity, cooldown, chargingTicks, 1);
		this.setup(true, true, true, false);
	}

	@Override
	public void startCastingSpell() {
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
			Vec3d v = new Vec3d((double) MathHelper.cos(entityAngle), 0, (double) MathHelper.sin(entityAngle));
			v = v.normalize().scale(1.25D);
			int rows = DungeonGenUtils.randomBetween(this.minRows, this.maxRows, this.entity.getRNG());
			double angle = rows > 0 ? 120 / rows : 0;
			if (angle != 0) {
				v = VectorUtil.rotateVectorAroundY(v, -60);
			}
			for (int rowCount = 0; rowCount < rows; rowCount++) {
				for (int fangcount = 0; fangcount < 24; ++fangcount) {
					double d2 = 1.25D * (double) (fangcount + 1);
					v = v.normalize();
					v = v.scale(d2);
					this.spawnFangs(this.entity.posX + v.x, this.entity.posZ + v.z, d0, d1, entityAngle, fangcount);
				}
				if (angle != 0) {
					v = v.normalize();
					v = VectorUtil.rotateVectorAroundY(v, angle);
				}
			}
		}
	}

	private void spawnFangs(double x, double z, double minY, double maxY, float rotationYawRadians, int warmupDelayTicks) {
		BlockPos blockpos = new BlockPos(x, maxY, z);
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

			if (blockpos.getY() < MathHelper.floor(minY) - 1) {
				break;
			}
		}

		if (flag) {
			EntityEvokerFangs entityevokerfangs = new EntityEvokerFangs(this.entity.world, x, (double) blockpos.getY() + d0, z, rotationYawRadians, warmupDelayTicks, this.entity);
			this.entity.world.spawnEntity(entityevokerfangs);
		}
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
		return 0.4F;
	}

	@Override
	public float getGreen() {
		return 0.3F;
	}

	@Override
	public float getBlue() {
		return 0.35F;
	}

}
