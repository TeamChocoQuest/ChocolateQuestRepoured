package team.cqr.cqrepoured.objects.entity.ai.navigator;

import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;

public class DirectLineNodeProcessor extends NodeProcessor {

	public DirectLineNodeProcessor() {
	}

	@Override
	public PathPoint getStart() {
		return this.openPoint(MathHelper.floor(this.entity.getEntityBoundingBox().minX), MathHelper.floor(this.entity.getEntityBoundingBox().minY + 0.5D), MathHelper.floor(this.entity.getEntityBoundingBox().minZ));
	}

	@Override
	public PathPoint getPathPointToCoords(double x, double y, double z) {
		return super.openPoint(MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z));
	}

	@Override
	public int findPathOptions(PathPoint[] pathOptions, PathPoint currentPoint, PathPoint targetPoint, float maxDistance) {
		int i = 0;

		for (EnumFacing enumfacing : EnumFacing.values()) {
			PathPoint pathpoint = this.openPoint(currentPoint.x + enumfacing.getXOffset(), currentPoint.y + enumfacing.getYOffset(), currentPoint.z + enumfacing.getZOffset());

			if (pathpoint != null && !pathpoint.visited && pathpoint.distanceTo(targetPoint) < maxDistance) {
				pathOptions[i++] = pathpoint;
			}
		}

		return i;
	}

	@Override
	public PathNodeType getPathNodeType(IBlockAccess blockaccessIn, int x, int y, int z, EntityLiving entitylivingIn, int xSize, int ySize, int zSize, boolean canBreakDoorsIn, boolean canEnterDoorsIn) {
		return PathNodeType.OPEN;
	}

	@Override
	public PathNodeType getPathNodeType(IBlockAccess blockaccessIn, int x, int y, int z) {
		return PathNodeType.OPEN;
	}

}
