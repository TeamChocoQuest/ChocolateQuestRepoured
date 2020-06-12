package com.teamcqr.chocolatequestrepoured.objects.entity.pathfinding;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.util.reflection.ReflectionField;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.World;

public class PathNavigateGroundCQR extends PathNavigateGround {

	public PathNavigateGroundCQR(EntityLiving entitylivingIn, World worldIn) {
		super(entitylivingIn, worldIn);
	}

	@Override
	public Path getPathToPos(BlockPos pos) {
		if (this.world.getBlockState(pos).getMaterial() == Material.AIR) {
			BlockPos blockpos;

			for (blockpos = pos.down(); blockpos.getY() > 0 && this.world.getBlockState(blockpos).getMaterial() == Material.AIR; blockpos = blockpos.down()) {
				;
			}

			if (blockpos.getY() > 0) {
				return this.getPathToPos2(blockpos.up());
			}

			while (blockpos.getY() < this.world.getHeight() && this.world.getBlockState(blockpos).getMaterial() == Material.AIR) {
				blockpos = blockpos.up();
			}

			pos = blockpos;
		}

		if (!this.world.getBlockState(pos).getMaterial().isSolid()) {
			return this.getPathToPos2(pos);
		} else {
			BlockPos blockpos1;

			for (blockpos1 = pos.up(); blockpos1.getY() < this.world.getHeight() && this.world.getBlockState(blockpos1).getMaterial().isSolid(); blockpos1 = blockpos1.up()) {
				;
			}

			return this.getPathToPos2(blockpos1);
		}
	}

	private static final ReflectionField<PathNavigate, Integer> fieldTicksAtLastPos = new ReflectionField(PathNavigate.class, "abc", "ticksAtLastPos");
	private static final ReflectionField<PathNavigate, Vec3d> fieldLastPosCheck = new ReflectionField(PathNavigate.class, "abc", "lastPosCheck");
	private static final ReflectionField<PathNavigate, BlockPos> fieldTargetPos = new ReflectionField(PathNavigate.class, "abc", "targetPos");
	private static final ReflectionField<PathNavigate, PathFinder> fieldPathFinder = new ReflectionField(PathNavigate.class, "abc", "pathFinder");

	@Nullable
	private Path getPathToPos2(BlockPos pos) {
		if (!this.canNavigate()) {
			return null;
		//} else if (this.currentPath != null && !this.currentPath.isFinished() && pos.equals(this.targetPos)) {
		} else if (this.currentPath != null && !this.currentPath.isFinished() && pos.equals(fieldTargetPos.get(this))) {
			return this.currentPath;
		} else {
			// this.targetPos = pos;
			float f = this.getPathSearchRange();
			this.world.profiler.startSection("pathfind");
			BlockPos blockpos = new BlockPos(this.entity);
			int i = (int) (f + 8.0F);
			ChunkCache chunkcache = new ChunkCache(this.world, blockpos.add(-i, -i, -i), blockpos.add(i, i, i), 0);
			//Path path = this.pathFinder.findPath(chunkcache, this.entity, this.targetPos, f);
			Path path = fieldPathFinder.get(this).findPath(chunkcache, this.entity, pos, f);
			this.world.profiler.endSection();
			return path;
		}
	}

	@Override
	public boolean setPath(Path pathentityIn, double speedIn) {
		if (pathentityIn == null) {
			this.currentPath = null;
			fieldTargetPos.set(this, null);
			//this.targetPos = null;
			return false;
		} else {
			if (pathentityIn.isSamePath(this.currentPath)) {
				return true;
			}

			this.currentPath = pathentityIn;
			PathPoint finalPathPoint = pathentityIn.getFinalPathPoint();
			fieldTargetPos.set(this, new BlockPos(finalPathPoint.x, finalPathPoint.y, finalPathPoint.z));
			//this.targetPos = new BlockPos(finalPathPoint.x, finalPathPoint.y, finalPathPoint.z);

			this.removeSunnyPath();

			if (this.currentPath.getCurrentPathLength() <= 0) {
				this.currentPath = null;
				fieldTargetPos.set(this, null);
				//this.targetPos = null;
				return false;
			} else {
				this.speed = speedIn;
				Vec3d vec3d = this.getEntityPosition();
				fieldTicksAtLastPos.set(this, this.totalTicks);
				fieldLastPosCheck.set(this, vec3d);
				//this.ticksAtLastPos = this.totalTicks;
				//this.lastPosCheck = vec3d;
				return true;
			}
		}
	}

}
