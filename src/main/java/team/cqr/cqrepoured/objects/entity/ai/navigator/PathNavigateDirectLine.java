package team.cqr.cqrepoured.objects.entity.ai.navigator;

import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PathNavigateDirectLine extends PathNavigate {

	public PathNavigateDirectLine(EntityLiving entity, World world) {
		super(entity, world);
	}

	@Override
	protected PathFinder getPathFinder() {
		this.nodeProcessor = new DirectLineNodeProcessor();
		this.nodeProcessor.setCanEnterDoors(true);
		this.nodeProcessor.setCanSwim(true);
		return new PathFinder(this.nodeProcessor);
	}

	@Override
	protected void pathFollow() {
		Vec3d vec3d = this.getEntityPosition();
		float f = this.entity.width * this.entity.width;
		// int i = 6;

		if (vec3d.squareDistanceTo(this.currentPath.getVectorFromIndex(this.entity, this.currentPath.getCurrentPathIndex())) < (double) f) {
			this.currentPath.incrementPathIndex();
		}

		for (int j = Math.min(this.currentPath.getCurrentPathIndex() + 6, this.currentPath.getCurrentPathLength() - 1); j > this.currentPath.getCurrentPathIndex(); --j) {
			Vec3d vec3d1 = this.currentPath.getVectorFromIndex(this.entity, j);

			if (vec3d1.squareDistanceTo(vec3d) <= 36.0D && this.isDirectPathBetweenPoints(vec3d, vec3d1, 0, 0, 0)) {
				this.currentPath.setCurrentPathIndex(j);
				break;
			}
		}

	}

	@Override
	protected Vec3d getEntityPosition() {
		return new Vec3d(this.entity.posX, this.entity.posY + (double) this.entity.height * 0.5D, this.entity.posZ);
	}

	@Override
	protected boolean canNavigate() {
		return !this.entity.isRiding();
	}

	@Override
	protected boolean isDirectPathBetweenPoints(Vec3d posVec31, Vec3d posVec32, int sizeX, int sizeY, int sizeZ) {
		return true;
	}

	@Override
	public boolean canEntityStandOnPos(BlockPos pos) {
		return true;
	}

}
