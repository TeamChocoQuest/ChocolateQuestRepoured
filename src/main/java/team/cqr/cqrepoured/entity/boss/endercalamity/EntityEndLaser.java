package team.cqr.cqrepoured.entity.boss.endercalamity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.IPacket;
import net.minecraft.world.World;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkHooks;
import team.cqr.cqrepoured.entity.boss.spectrelord.EntityRotatingLaser;
import team.cqr.cqrepoured.init.CQREntityTypes;

public class EntityEndLaser extends EntityRotatingLaser {

	public EntityEndLaser(World worldIn) {
		this(CQREntityTypes.END_LASER.get(), worldIn);
	}
	
	public EntityEndLaser(EntityType<? extends EntityEndLaser> type, World world) {
		super(type, world);
	}

	public EntityEndLaser(World worldIn, LivingEntity caster, float length, float deltaRotationYawPerTick, float deltaRotationPitchPerTick) {
		super(CQREntityTypes.END_LASER.get(), worldIn, caster, length, deltaRotationYawPerTick, deltaRotationPitchPerTick);
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
