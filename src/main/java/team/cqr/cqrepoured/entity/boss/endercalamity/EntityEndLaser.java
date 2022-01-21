package team.cqr.cqrepoured.entity.boss.endercalamity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import team.cqr.cqrepoured.entity.boss.spectrelord.EntityRotatingLaser;

public class EntityEndLaser extends EntityRotatingLaser {

	public EntityEndLaser(World worldIn) {
		super(worldIn);
	}

	public EntityEndLaser(World worldIn, LivingEntity caster, float length, float deltaRotationYawPerTick, float deltaRotationPitchPerTick) {
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
	public float onHitBlock(BlockPos pos, BlockState state) {
		return 64 * super.onHitBlock(pos, state);
	}

	@Override
	public boolean canBreakBlocks() {
		return true;
	}
	
	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

}
