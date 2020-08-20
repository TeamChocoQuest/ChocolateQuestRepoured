package com.teamcqr.chocolatequestrepoured.objects.entity.pathfinding;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.World;

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
	public Path getPathToPos(BlockPos pos) {
		if (this.world.getBlockState(pos).getMaterial() == Material.AIR) {
			BlockPos blockpos;

			for (blockpos = pos.down(); blockpos.getY() > 0 && this.world.getBlockState(blockpos).getMaterial() == Material.AIR; blockpos = blockpos.down()) {
				;
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
				;
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
			float f = this.getPathSearchRange();
			this.world.profiler.startSection("pathfind");
			BlockPos blockpos = new BlockPos(this.entity);
			int i = (int) (f + 8.0F);
			ChunkCache chunkcache = new ChunkCache(this.world, blockpos.add(-i, -i, -i), blockpos.add(i, i, i), 0);
			Path path = this.pathFinder.findPath(chunkcache, this.entity, pos, f);
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
	protected void checkForStuck(Vec3d positionVec3) {
		if (this.totalTicks - this.ticksAtLastPos > 100) {
			if (positionVec3.squareDistanceTo(this.lastPosCheck) < 2.25D) {
				this.clearPath();
			}

			this.ticksAtLastPos = this.totalTicks;
			this.lastPosCheck = positionVec3;
		}

		if (this.currentPath != null && !this.currentPath.isFinished()) {
			Vec3d vec3d = this.currentPath.getCurrentPos();

			if (vec3d.equals(this.timeoutCachedNode)) {
				this.timeoutTimer += System.currentTimeMillis() - this.lastTimeoutCheck;
			} else {
				this.timeoutCachedNode = vec3d;
				double d0 = positionVec3.distanceTo(this.timeoutCachedNode);
				this.timeoutLimit = this.entity.getAIMoveSpeed() > 0.0F ? d0 / (double) this.entity.getAIMoveSpeed() * 1000.0D : 0.0D;
			}

			if (this.timeoutLimit > 0.0D && (double) this.timeoutTimer > this.timeoutLimit * 3.0D) {
				this.timeoutCachedNode = Vec3d.ZERO;
				this.timeoutTimer = 0L;
				this.timeoutLimit = 0.0D;
				this.clearPath();
			}

			this.lastTimeoutCheck = System.currentTimeMillis();
		}
	}

	@Override
	public void clearPath() {
		this.currentPath = null;
		this.targetPos = null;
	}

}
