package team.cqr.cqrepoured.entity.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.entity.ai.goal.BreakBlockGoal;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.level.block.FireBlock;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

public class EntityAIFireFighter extends /*AbstractCQREntityAI<AbstractEntityCQR>*/BreakBlockGoal {

	public EntityAIFireFighter(AbstractEntityCQR entity) {
		super(Blocks.FIRE, entity, 1.1, 16); //block to break, entity, speedModifier, search range
	}

	/*private static final int SEARCH_RADIUS_HORIZONTAL = 16;
	private static final int SEARCH_RADIUS_VERTICAL = 2;
	private static final double REACH_DISTANCE_SQ = 3.0D * 3.0D;
	private BlockPos nearestFire = null;
	private int lastTickStarted = Integer.MIN_VALUE;

	public EntityAIFireFighter(AbstractEntityCQR entity) {
		super(entity);
		this.setMutexBits(3);
	}

	@Override
	public boolean canUse() {
		if (!this.entity.canPutOutFire()) {
			return false;
		}

		if (this.random.nextInt(this.lastTickStarted + 60 >= this.entity.ticksExisted ? 5 : 20) == 0) {
			BlockPos pos = new BlockPos(this.entity);
			Vector3d vec = this.entity.getPositionEyes(1.0F);
			this.nearestFire = BlockPosUtil.getNearest(this.world, pos.getX(), pos.getY() + (MathHelper.ceil(this.entity.height) >> 1), pos.getZ(), SEARCH_RADIUS_HORIZONTAL, SEARCH_RADIUS_VERTICAL, true, true, Blocks.FIRE, (mutablePos, state) -> {
				mutablePos.setY(mutablePos.getY() - 1);
				if (this.world.getBlockState(mutablePos).getBlock().isFireSource(this.world, mutablePos, Direction.UP)) {
					return false;
				}
				mutablePos.setY(mutablePos.getY() + 1);
				RayTraceResult result = this.world.rayTraceBlocks(vec, new Vector3d(mutablePos.getX() + 0.5D, mutablePos.getY() + 0.5D, mutablePos.getZ() + 0.5D), false, true, false);
				return result == null || result.getBlockPos().equals(mutablePos);
			});
		}

		return this.nearestFire != null;
	}

	@Override
	public boolean canContinueToUse() {
		if (this.nearestFire == null) {
			return false;
		}
		if (this.entity.ticksExisted % 10 == 0 && this.entity.world.getBlockState(this.nearestFire).getBlock() != Blocks.FIRE) {
			return false;
		}
		return this.entity.hasPath();
	}

	@Override
	public void start() {
		if (this.entity.getDistanceSqToCenter(this.nearestFire) > REACH_DISTANCE_SQ) {
			this.entity.getNavigator().tryMoveToXYZ(this.nearestFire.getX(), this.nearestFire.getY(), this.nearestFire.getZ(), 1.0D);
		}
		this.lastTickStarted = this.entity.ticksExisted;
	}

	@Override
	public void stop() {
		this.nearestFire = null;
		this.entity.getNavigator().clearPath();
	}

	@Override
	public void tick() {
		if (this.entity.getDistanceSqToCenter(this.nearestFire) <= REACH_DISTANCE_SQ) {
			if (this.entity.world.getBlockState(this.nearestFire).getBlock() == Blocks.FIRE) {
				this.entity.world.setBlockToAir(this.nearestFire);
				((ServerWorld) this.entity.world).spawnParticle(ParticleTypes.SMOKE_NORMAL, this.nearestFire.getX() + 0.5D, this.nearestFire.getY() + 0.5D, this.nearestFire.getZ() + 0.5D, 4, 0.25D, 0.25D, 0.25D, 0.0D);
				this.entity.world.playSound(null, this.nearestFire.getX() + 0.5D, this.nearestFire.getY() + 0.5D, this.nearestFire.getZ() + 0.5D, SoundEvents.BLOCK_FIRE_EXTINGUISH, this.entity.getSoundSource(), 1.0F, 0.9F + this.entity.getRNG().nextFloat() * 0.2F);
			}
			this.nearestFire = null;
		}
	}*/
	
	@Override
	public boolean canUse() {
		return super.canUse() && (this.mob instanceof AbstractEntityCQR && ((AbstractEntityCQR) this.mob).canPutOutFire());
	}
	
	@Override
	public boolean canContinueToUse() {
		return super.canContinueToUse() && (this.mob instanceof AbstractEntityCQR && ((AbstractEntityCQR) this.mob).canPutOutFire());
	}
	
	@Override
	protected boolean isValidTarget(IWorldReader pLevel, BlockPos pPos) {
		Block block = pLevel.getBlockState(pPos).getBlock();
		return super.isValidTarget(pLevel, pPos) || block instanceof FireBlock;
	}

}
