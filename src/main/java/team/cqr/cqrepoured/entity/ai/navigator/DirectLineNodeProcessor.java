package team.cqr.cqrepoured.entity.ai.navigator;

import net.minecraft.entity.MobEntity;
import net.minecraft.pathfinding.FlaggedPathPoint;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.core.Direction;

public class DirectLineNodeProcessor extends NodeProcessor {

	public DirectLineNodeProcessor() {
	}

	@Override
	public PathPoint getStart() {
		return new PathPoint(Mth.floor(this.mob.getBoundingBox().minX), Mth.floor(this.mob.getBoundingBox().minY + 0.5D), Mth.floor(this.mob.getBoundingBox().minZ));
	}

	@Override
	public FlaggedPathPoint getGoal(double x, double y, double z) {
		return new FlaggedPathPoint(Mth.floor(x), Mth.floor(y), Mth.floor(z));
	}
	
	@Override
	public int getNeighbors(PathPoint[] pathOptions, PathPoint currentPoint) {
		int i = 0;

		for (Direction enumfacing : Direction.values()) {
			PathPoint pathpoint = new PathPoint(currentPoint.x + enumfacing.getStepX(), currentPoint.y + enumfacing.getStepY(), currentPoint.z + enumfacing.getStepZ());

			//Is the last check truly necessary???
			if (pathpoint != null && pathpoint.closed /*&& pathpoint.distanceTo(targetPoint) < maxDistance*/) {
				pathOptions[i++] = pathpoint;
			}
		}

		return i;
	}

	@Override
	public BlockPathTypes getBlockPathType(BlockGetter blockaccessIn, int x, int y, int z, MobEntity entitylivingIn, int xSize, int ySize, int zSize, boolean canBreakDoorsIn, boolean canEnterDoorsIn) {
		return BlockPathTypes.OPEN;
	}

	@Override
	public BlockPathTypes getBlockPathType(BlockGetter blockaccessIn, int x, int y, int z) {
		return BlockPathTypes.OPEN;
	}

}
