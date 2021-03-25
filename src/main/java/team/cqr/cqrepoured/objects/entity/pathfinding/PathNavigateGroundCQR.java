package team.cqr.cqrepoured.objects.entity.pathfinding;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.World;
import team.cqr.cqrepoured.world.ChunkCacheCQR;

/**
 * Copied from {@link PathNavigateGround}
 */
public class PathNavigateGroundCQR extends PathNavigateGround {

	private int ticksAtLastPos;
	private Vec3d lastPosCheck = Vec3d.ZERO;
	private Vec3d timeoutCachedNode = Vec3d.ZERO;
	private long timeoutTimer;
	private long lastTimeoutCheck;
	private double timeoutLimit;
	private long lastTimeUpdated;
	private BlockPos targetPos;
	private PathFinder pathFinder;

	public PathNavigateGroundCQR(EntityLiving entitylivingIn, World worldIn) {
		super(entitylivingIn, worldIn);
	}

	@Override
	protected PathFinder getPathFinder() {
		PathFinder newPathFinder = super.getPathFinder();
		this.pathFinder = newPathFinder;
		return newPathFinder;
	}

	@Override
	public float getPathSearchRange() {
		return 256.0F;
	}

	@Override
	public void updatePath() {
		if(this.hasMount()) {
			this.getMount().getNavigator().updatePath();
		}
		if (this.world.getTotalWorldTime() - this.lastTimeUpdated > 20L) {
			if (this.targetPos != null) {
				this.currentPath = null;
				this.currentPath = this.getPathToPos(this.targetPos);
				this.lastTimeUpdated = this.world.getTotalWorldTime();
				this.tryUpdatePath = false;
			}
		} else {
			this.tryUpdatePath = true;
		}
	}
	
	@Override
	public void onUpdateNavigation() {
		super.onUpdateNavigation();
		if(!noPath() && this.hasMount()) {
			getMount().getNavigator().onUpdateNavigation();
		}
	}
	
	private boolean hasMount() {
		return entity.getRidingEntity() instanceof EntityLiving;
	}

	@Override
	public Path getPathToPos(BlockPos pos) {
		if (this.world.getBlockState(pos).getMaterial() == Material.AIR) {
			BlockPos blockpos;

			for (blockpos = pos.down(); blockpos.getY() > 0 && this.world.getBlockState(blockpos).getMaterial() == Material.AIR; blockpos = blockpos.down()) {

			}

			if (blockpos.getY() > 0) {
				return this.getPathToPosCQR(blockpos.up());
			}

			while (blockpos.getY() < this.world.getHeight() && this.world.getBlockState(blockpos).getMaterial() == Material.AIR) {
				blockpos = blockpos.up();
			}

			pos = blockpos;
		}

		if (!this.world.getBlockState(pos).getMaterial().isSolid()) {
			return this.getPathToPosCQR(pos);
		} else {
			BlockPos blockpos1;

			for (blockpos1 = pos.up(); blockpos1.getY() < this.world.getHeight() && this.world.getBlockState(blockpos1).getMaterial().isSolid(); blockpos1 = blockpos1.up()) {

			}

			return this.getPathToPosCQR(blockpos1);
		}
	}

	@Nullable
	private Path getPathToPosCQR(BlockPos pos) {
		if (!this.canNavigate()) {
			return null;
		} else if (this.currentPath != null && !this.currentPath.isFinished() && pos.equals(this.targetPos)) {
			return this.currentPath;
		} else {
			Entity ent = this.hasMount() ? this.getMount() : this.entity;
			float distance = MathHelper.sqrt(ent.getDistanceSqToCenter(pos));
			if (distance > this.getPathSearchRange()) {
				return null;
			}

			this.world.profiler.startSection("pathfind");
			BlockPos entityPos = new BlockPos(this.hasMount() ? this.getMount() : this.entity);
			ChunkCache chunkcache = new ChunkCacheCQR(this.world, entityPos, pos, entityPos, 32, false);
			Path path = this.pathFinder.findPath(chunkcache, this.hasMount() ? this.getMount() : this.entity, pos, MathHelper.ceil(distance + 32.0F));
			this.world.profiler.endSection();
			return path;
		}
	}

	@Override
	public boolean setPath(Path pathentityIn, double speedIn) {
		if (pathentityIn == null) {
			this.currentPath = null;
			this.targetPos = null;
			return false;
		} else {
			
			if(this.hasMount()) {
				this.getMount().getNavigator().setPath(pathentityIn, speedIn);
			}
			
			if (pathentityIn.isSamePath(this.currentPath)) {
				return true;
			}

			this.currentPath = pathentityIn;

			this.removeSunnyPath();

			if (this.currentPath.getCurrentPathLength() <= 0) {
				this.currentPath = null;
				this.targetPos = null;
				return false;
			} else {
				PathPoint finalPathPoint = pathentityIn.getFinalPathPoint();
				this.targetPos = new BlockPos(finalPathPoint.x, finalPathPoint.y, finalPathPoint.z);
				this.speed = speedIn;
				this.ticksAtLastPos = this.totalTicks;
				this.lastPosCheck = this.getEntityPosition();
				return true;
			}
		}
	}
	
	@Override
	protected boolean canNavigate() {
		return super.canNavigate() || this.hasMount();
	}

	@Override
	protected void checkForStuck(Vec3d positionVec3) {
		if (this.totalTicks - this.ticksAtLastPos >= 100) {
			double aiMoveSpeed = (double) (this.hasMount() ? this.getMount().getAIMoveSpeed() : this.entity.getAIMoveSpeed());
			aiMoveSpeed = aiMoveSpeed * aiMoveSpeed * 0.98D / 0.454D;
			if (positionVec3.distanceTo(this.lastPosCheck) / 100.0D < aiMoveSpeed * 0.5D) {
				this.clearPath();
			}

			this.ticksAtLastPos = this.totalTicks;
			this.lastPosCheck = positionVec3;
		}

		if (this.currentPath != null && !this.currentPath.isFinished()) {
			Vec3d vec3d = this.currentPath.getCurrentPos();

			if (!vec3d.equals(this.timeoutCachedNode)) {
				this.timeoutCachedNode = vec3d;
				this.timeoutTimer = this.totalTicks;
				double aiMoveSpeedOrig = (double) (this.hasMount() ? this.getMount().getAIMoveSpeed() : this.entity.getAIMoveSpeed());
				double aiMoveSpeed = aiMoveSpeedOrig;
				if (aiMoveSpeed > 0.0F) {
					aiMoveSpeed = aiMoveSpeed * aiMoveSpeed * 0.98D / 0.454D;
					double distance = positionVec3.distanceTo(this.timeoutCachedNode);
					this.timeoutLimit = aiMoveSpeedOrig > 0.0F ? MathHelper.ceil(distance / aiMoveSpeed) : 0.0D;
				} else {
					this.timeoutLimit = 0.0D;
				}
			}

			if (this.timeoutLimit > 0.0D && (double) (this.totalTicks - this.timeoutTimer) > this.timeoutLimit * 2.0D) {
				this.timeoutCachedNode = Vec3d.ZERO;
				this.timeoutTimer = 0L;
				this.timeoutLimit = 0.0D;
				this.clearPath();
			}
		}
	}
	
	@Nullable
	private EntityLiving getMount() {
		try {
			return (EntityLiving)entity.getRidingEntity();
		} catch(NullPointerException npe) {
			return null;
		}
	}

	@Override
	public void clearPath() {
		if(this.hasMount()) {
			getMount().getNavigator().clearPath();
		}
		this.currentPath = null;
		this.targetPos = null;
		super.clearPath();
	}

}
