package team.cqr.cqrepoured.entity.ai.navigator;

import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.level.pathfinder.Target;

public class DirectLineNodeProcessor extends NodeEvaluator {

	public DirectLineNodeProcessor() {
	}

	@Override
	public Node getStart() {
		return new Node(Mth.floor(this.mob.getBoundingBox().minX), Mth.floor(this.mob.getBoundingBox().minY + 0.5D), Mth.floor(this.mob.getBoundingBox().minZ));
	}
	
	@Override
	public Target getGoal(double x, double y, double z) {
		return new Target(Mth.floor(x), Mth.floor(y), Mth.floor(z));
	}
	
	@Override
	public int getNeighbors(Node[] pathOptions, Node currentPoint) {
		int i = 0;

		for (Direction enumfacing : Direction.values()) {
			Node pathpoint = new Node(currentPoint.x + enumfacing.getStepX(), currentPoint.y + enumfacing.getStepY(), currentPoint.z + enumfacing.getStepZ());

			//Is the last check truly necessary???
			if (pathpoint != null && pathpoint.closed /*&& pathpoint.distanceTo(targetPoint) < maxDistance*/) {
				pathOptions[i++] = pathpoint;
			}
		}

		return i;
	}

	@Override
	public BlockPathTypes getBlockPathType(BlockGetter pLevel, int pX, int pY, int pZ, Mob pMob) {
		return BlockPathTypes.OPEN;
	}

	@Override
	public BlockPathTypes getBlockPathType(BlockGetter blockaccessIn, int x, int y, int z) {
		return BlockPathTypes.OPEN;
	}

}
