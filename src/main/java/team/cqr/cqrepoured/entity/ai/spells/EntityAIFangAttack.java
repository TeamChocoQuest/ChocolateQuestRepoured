package team.cqr.cqrepoured.entity.ai.spells;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.EvokerFangsEntity;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.util.VectorUtil;

/**
 * This code is adapted minecraft vanilla code, so it is made by Mojang
 */
public class EntityAIFangAttack extends AbstractEntityAISpell<AbstractEntityCQR> implements IEntityAISpellAnimatedVanilla {

	private int minRows = 1;
	private int maxRows = 1;

	public EntityAIFangAttack(AbstractEntityCQR entity, int cooldown, int chargingTicks) {
		super(entity, cooldown, chargingTicks, 1);
		this.setup(true, true, true, false);
	}

	public EntityAIFangAttack(AbstractEntityCQR entity, int cooldown, int chargingTicks, int minRows, int maxRows) {
		this(entity, cooldown, chargingTicks);
		this.minRows = minRows;
		this.maxRows = maxRows;
	}

	@Override
	public void startCastingSpell() {
		LivingEntity entitylivingbase = this.entity.getTarget();
		double d0 = Math.min(entitylivingbase.getY(), this.entity.getY());
		double d1 = Math.max(entitylivingbase.getY(), this.entity.getY()) + 1.0D;
		float entityAngle = (float) MathHelper.atan2(entitylivingbase.getZ() - this.entity.getZ(), entitylivingbase.getX() - this.entity.getX());

		if (this.entity.distanceToSqr(entitylivingbase) < 9.0D) {
			for (int i = 0; i < 5; ++i) {
				float f1 = entityAngle + i * (float) Math.PI * 0.4F;
				this.spawnFangs(this.entity.getX() + MathHelper.cos(f1) * 1.5D, this.entity.getZ() + MathHelper.sin(f1) * 1.5D, d0, d1, f1, 0);
			}

			for (int k = 0; k < 8; ++k) {
				float f2 = entityAngle + k * (float) Math.PI * 2.0F / 8.0F + ((float) Math.PI * 2F / 5F);
				this.spawnFangs(this.entity.getX() + MathHelper.cos(f2) * 2.5D, this.entity.getZ() + MathHelper.sin(f2) * 2.5D, d0, d1, f2, 3);
			}
		} else {
			Vector3d v = new Vector3d(MathHelper.cos(entityAngle), 0, MathHelper.sin(entityAngle));
			v = v.normalize().scale(1.25D);
			int rows = DungeonGenUtils.randomBetween(this.minRows, this.maxRows, this.entity.getRandom());
			double angle = rows > 0 ? 120 / rows : 0;
			if (angle != 0) {
				v = VectorUtil.rotateVectorAroundY(v, -60);
			}
			for (int rowCount = 0; rowCount < rows; rowCount++) {
				for (int fangcount = 0; fangcount < 24; ++fangcount) {
					double d2 = 1.25D * (fangcount + 1);
					v = v.normalize();
					v = v.scale(d2);
					this.spawnFangs(this.entity.getX() + v.x, this.entity.getZ() + v.z, d0, d1, entityAngle, fangcount);
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
			if (this.entity.level.getBlockState(blockpos).isFaceSturdy(this.entity.level, blockpos.below(), Direction.UP)/*!this.entity.level.isBlockNormalCube(blockpos, true) && this.entity.level.isBlockNormalCube(blockpos.below(), true)*/) {
				if (!this.entity.level.isEmptyBlock(blockpos)) {
					BlockState iblockstate = this.entity.level.getBlockState(blockpos);
					AxisAlignedBB axisalignedbb = iblockstate.getShape(this.entity.level, blockpos).bounds();

					if (axisalignedbb != null) {
						d0 = axisalignedbb.maxY;
					}
				}

				flag = true;
				break;
			}

			blockpos = blockpos.below();

			if (blockpos.getY() < MathHelper.floor(minY) - 1) {
				break;
			}
		}

		if (flag) {
			EvokerFangsEntity entityevokerfangs = new EvokerFangsEntity(this.entity.level, x, blockpos.getY() + d0, z, rotationYawRadians, warmupDelayTicks, this.entity);
			this.entity.level.addFreshEntity(entityevokerfangs);
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
