package team.cqr.cqrepoured.entity.ai;

import java.util.EnumSet;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.pathfinder.Node;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

public class EntityAIMoveToLeader extends AbstractCQREntityAI<AbstractEntityCQR> {

	public EntityAIMoveToLeader(AbstractEntityCQR entity) {
		super(entity);
		//this.setMutexBits(3);
		this.setFlags(EnumSet.of(Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		if (this.entity.hasLeader()) {
			LivingEntity leader = this.entity.getLeader();
			return this.entity.distanceToSqr(leader) > 64.0D;
		}
		return false;
	}

	@Override
	public boolean canContinueToUse() {
		if (this.entity.hasLeader()) {
			LivingEntity leader = this.entity.getLeader();

			if (this.entity.distanceToSqr(leader) > 16.0D) {
				return this.entity.isPathFinding();
			}
		}
		return false;
	}

	@Override
	public void start() {
		LivingEntity leader = this.entity.getLeader();
		this.entity.getNavigation().moveTo(leader, 1.0D);
	}

	@Override
	public void tick() {
		if (this.entity.isPathFinding()) {
			LivingEntity leader = this.entity.getLeader();

			if (this.entity.distanceTo(leader) > 24) {
				int i = Mth.floor(leader.getX()) - 2;
				int j = Mth.floor(leader.getZ()) - 2;
				int k = Mth.floor(leader.getBoundingBox().minY);

				for (int l = 0; l <= 4; ++l) {
					for (int i1 = 0; i1 <= 4; ++i1) {
						if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && this.isTeleportFriendlyBlock(i, j, k, l, i1)) {
							this.entity.getLookControl().setLookAt(i + l + 0.5F, k, j + i1 + 0.5F, this.entity.getYRot(), this.entity.getXRot());
							this.entity.getNavigation().stop();
							return;
						}
					}
				}
			}

			Node target = this.entity.getNavigation().getPath().getEndNode();

			if (leader.distanceToSqr(target.x + 0.5D, target.y, target.z + 0.5D) > 16.0D) {
				this.entity.getNavigation().moveTo(leader, 1.0D);
			}
		}
	}

	@Override
	public void stop() {
		this.entity.getNavigation().stop();
	}

	protected boolean isTeleportFriendlyBlock(int x, int z, int y, int xOffset, int zOffset) {
		BlockPos blockpos = new BlockPos(x + xOffset, y - 1, z + zOffset);
		//Workaround for the thing below
		return this.entity.getNavigation().isStableDestination(blockpos);
		
		//BlockState iblockstate = this.world.getBlockState(blockpos);
		//return iblockstate.getBlockFaceShape(this.world, blockpos, Direction.DOWN) == BlockFaceShape.SOLID && iblockstate.canEntitySpawn(this.entity) && this.world.isAirBlock(blockpos.up()) && this.world.isAirBlock(blockpos.up(2));
	}

}
