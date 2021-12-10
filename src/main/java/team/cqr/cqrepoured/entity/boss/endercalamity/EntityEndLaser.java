package team.cqr.cqrepoured.entity.boss.endercalamity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import team.cqr.cqrepoured.entity.boss.spectrelord.EntityRotatingLaser;

public class EntityEndLaser extends EntityRotatingLaser {

	public EntityEndLaser(World worldIn) {
		super(worldIn);
	}

	public EntityEndLaser(World worldIn, EntityLivingBase caster, float length, float deltaRotationYawPerTick, float deltaRotationPitchPerTick) {
		super(worldIn, caster, length, deltaRotationYawPerTick, deltaRotationPitchPerTick);
	}

	@Override
	public double laserEffectRadius() {
		return 0.5D;
	}

	@Override
	public float getColorR() {
		return 0.8F;
	}

	@Override
	public float getColorG() {
		return 0.01F;
	}

	@Override
	public float getColorB() {
		return 0.98F;
	}

	@Override
	public int getEntityHitRate() {
		return 5;
	}

	@Override
	public int blockBreakThreshhold() {
		return 1;
	}

	@Override
	public float blockBreakRevert() {
		return 0.025F * super.blockBreakRevert();
	}

	@Override
	public float onHitBlock(BlockPos pos, IBlockState state) {
		return 64 * super.onHitBlock(pos, state);
	}

	@Override
	public boolean canBreakBlocks() {
		return true;
	}

}
