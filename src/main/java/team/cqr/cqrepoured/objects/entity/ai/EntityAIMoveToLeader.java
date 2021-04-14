package team.cqr.cqrepoured.objects.entity.ai;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;

public class EntityAIMoveToLeader extends AbstractCQREntityAI<AbstractEntityCQR> {

	public EntityAIMoveToLeader(AbstractEntityCQR entity) {
		super(entity);
		this.setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		if (this.entity.hasLeader()) {
			EntityLivingBase leader = this.entity.getLeader();
			return this.entity.getDistanceSq(leader) > 64.0D;
		}
		return false;
	}

	@Override
	public boolean shouldContinueExecuting() {
		if (this.entity.hasLeader()) {
			EntityLivingBase leader = this.entity.getLeader();

			if (this.entity.getDistanceSq(leader) > 16.0D) {
				return this.entity.hasPath();
			}
		}
		return false;
	}

	@Override
	public void startExecuting() {
		EntityLivingBase leader = this.entity.getLeader();
		this.entity.getNavigator().tryMoveToEntityLiving(leader, 1.0D);
	}

	@Override
	public void updateTask() {
		if (this.entity.hasPath()) {
			EntityLivingBase leader = this.entity.getLeader();

			if (this.entity.getDistance(leader) > 24) {
				int i = MathHelper.floor(leader.posX) - 2;
				int j = MathHelper.floor(leader.posZ) - 2;
				int k = MathHelper.floor(leader.getEntityBoundingBox().minY);

				for (int l = 0; l <= 4; ++l) {
					for (int i1 = 0; i1 <= 4; ++i1) {
						if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && this.isTeleportFriendlyBlock(i, j, k, l, i1)) {
							this.entity.setLocationAndAngles((double) ((float) (i + l) + 0.5F), (double) k, (double) ((float) (j + i1) + 0.5F), this.entity.rotationYaw, this.entity.rotationPitch);
							this.entity.getNavigator().clearPath();
							return;
						}
					}
				}
			}

			PathPoint target = this.entity.getNavigator().getPath().getFinalPathPoint();

			if (leader.getDistanceSq(target.x + 0.5D, target.y, target.z + 0.5D) > 16.0D) {
				this.entity.getNavigator().tryMoveToEntityLiving(leader, 1.0D);
			}
		}
	}

	@Override
	public void resetTask() {
		this.entity.getNavigator().clearPath();
	}

	protected boolean isTeleportFriendlyBlock(int x, int z, int y, int xOffset, int zOffset) {
		BlockPos blockpos = new BlockPos(x + xOffset, y - 1, z + zOffset);
		IBlockState iblockstate = this.world.getBlockState(blockpos);
		return iblockstate.getBlockFaceShape(this.world, blockpos, EnumFacing.DOWN) == BlockFaceShape.SOLID && iblockstate.canEntitySpawn(this.entity) && this.world.isAirBlock(blockpos.up()) && this.world.isAirBlock(blockpos.up(2));
	}

}
