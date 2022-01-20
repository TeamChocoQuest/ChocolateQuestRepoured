package team.cqr.cqrepoured.entity.ai.navigator;

import net.minecraft.entity.MobEntity;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class PathNavigateDirectLine extends PathNavigator {

	public PathNavigateDirectLine(MobEntity entity, World world) {
		super(entity, world);
	}

	@Override
	protected PathFinder createPathFinder(int arg) {
		this.nodeEvaluator = new DirectLineNodeProcessor();
		this.nodeEvaluator.setCanOpenDoors(true);
		this.nodeEvaluator.setCanFloat(true);
		return new PathFinder(this.nodeEvaluator, this.getPathSearchRange());
	}

	@Override
	protected void followThePath() {
		Vector3d vec3d = this.getTempMobPos();
		float f = this.mob.getBbWidth() * this.mob.getBbWidth();
		// int i = 6;

		if (vec3d.distanceToSqr(this.path.getEntityPosAtNode(this.mob, this.path.getNextNodeIndex())) < f) {
			this.path.advance();
		}

		for (int j = Math.min(this.path.getNextNodeIndex() + 6, this.path.getNodeCount() - 1); j > this.path.getNextNodeIndex(); --j) {
			Vector3d vec3d1 = this.path.getEntityPosAtNode(this.mob, j);

			if (vec3d1.distanceToSqr(vec3d) <= 36.0D && this.canMoveDirectly(vec3d, vec3d1, 0, 0, 0)) {
				this.path.setNextNodeIndex(j);
				break;
			}
		}
	}

	@Override
	protected Vector3d getTempMobPos() {
		return this.mob.position();
	}

	@Override
	protected boolean canUpdatePath() {
		return !this.mob.isPassenger();
	}

	@Override
	protected boolean canMoveDirectly(Vector3d posVec31, Vector3d posVec32, int sizeX, int sizeY, int sizeZ) {
		return true;
	}
	
	@Override
	public boolean isStableDestination(BlockPos pPos) {
		return true;
	}

	public int getPathSearchRange() {
		return 64;
	}


}
