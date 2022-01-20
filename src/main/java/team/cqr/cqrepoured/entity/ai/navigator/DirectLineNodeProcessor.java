package team.cqr.cqrepoured.entity.ai.navigator;

import net.minecraft.entity.MobEntity;
import net.minecraft.pathfinding.FlaggedPathPoint;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockReader;

public class DirectLineNodeProcessor extends NodeProcessor {

	public DirectLineNodeProcessor() {
	}

	@Override
	public PathPoint getStart() {
		return new PathPoint(MathHelper.floor(this.mob.getBoundingBox().minX), MathHelper.floor(this.mob.getBoundingBox().minY + 0.5D), MathHelper.floor(this.mob.getBoundingBox().minZ));
	}

	@Override
	public FlaggedPathPoint getGoal(double x, double y, double z) {
		return new FlaggedPathPoint(MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z));
	}
	
	@Override
	public int getNeighbors(PathPoint[] pathOptions, PathPoint currentPoint) {
		int i = 0;

		for (Direction enumfacing : Direction.values()) {
			PathPoint pathpoint = new PathPoint(currentPoint.x + enumfacing.getStepX(), currentPoint.y + enumfacing.getStepY(), currentPoint.z + enumfacing.getStepZ());

			if (pathpoint != null && pathpoint.closed && pathpoint.distanceTo(targetPoint) < maxDistance) {
				pathOptions[i++] = pathpoint;
			}
		}

		return i;
	}

	@Override
	public PathNodeType getBlockPathType(IBlockReader blockaccessIn, int x, int y, int z, MobEntity entitylivingIn, int xSize, int ySize, int zSize, boolean canBreakDoorsIn, boolean canEnterDoorsIn) {
		return PathNodeType.OPEN;
	}

	@Override
	public PathNodeType getBlockPathType(IBlockReader blockaccessIn, int x, int y, int z) {
		return PathNodeType.OPEN;
	}

}
