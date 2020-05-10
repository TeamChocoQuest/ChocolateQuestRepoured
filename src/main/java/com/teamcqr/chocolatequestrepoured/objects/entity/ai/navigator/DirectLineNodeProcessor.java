package com.teamcqr.chocolatequestrepoured.objects.entity.ai.navigator;

import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;

public class DirectLineNodeProcessor extends NodeProcessor {

	public DirectLineNodeProcessor() {
	}

	@Override
	public PathPoint getStart() {
		int i;

        if (this.getCanSwim() && this.entity.isInWater())
        {
            i = (int)this.entity.getEntityBoundingBox().minY;
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(MathHelper.floor(this.entity.posX), i, MathHelper.floor(this.entity.posZ));

            for (Block block = this.blockaccess.getBlockState(blockpos$mutableblockpos).getBlock(); block == Blocks.FLOWING_WATER || block == Blocks.WATER; block = this.blockaccess.getBlockState(blockpos$mutableblockpos).getBlock())
            {
                ++i;
                blockpos$mutableblockpos.setPos(MathHelper.floor(this.entity.posX), i, MathHelper.floor(this.entity.posZ));
            }
        }
        else
        {
            i = MathHelper.floor(this.entity.getEntityBoundingBox().minY + 0.5D);
        }

        BlockPos blockpos1 = new BlockPos(this.entity);
        PathNodeType pathnodetype1 = PathNodeType.OPEN;

        if (this.entity.getPathPriority(pathnodetype1) < 0.0F)
        {
            Set<BlockPos> set = Sets.<BlockPos>newHashSet();
            set.add(new BlockPos(this.entity.getEntityBoundingBox().minX, (double)i, this.entity.getEntityBoundingBox().minZ));
            set.add(new BlockPos(this.entity.getEntityBoundingBox().minX, (double)i, this.entity.getEntityBoundingBox().maxZ));
            set.add(new BlockPos(this.entity.getEntityBoundingBox().maxX, (double)i, this.entity.getEntityBoundingBox().minZ));
            set.add(new BlockPos(this.entity.getEntityBoundingBox().maxX, (double)i, this.entity.getEntityBoundingBox().maxZ));

            for (BlockPos blockpos : set)
            {
                PathNodeType pathnodetype = PathNodeType.OPEN;

                if (this.entity.getPathPriority(pathnodetype) >= 0.0F)
                {
                    return super.openPoint(blockpos.getX(), blockpos.getY(), blockpos.getZ());
                }
            }
        }

        return super.openPoint(blockpos1.getX(), i, blockpos1.getZ());
	}

	@Override
	public PathPoint getPathPointToCoords(double x, double y, double z)
    {
        return super.openPoint(MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z));
    }

	@Override
	public int findPathOptions(PathPoint[] pathOptions, PathPoint currentPoint, PathPoint targetPoint, float maxDistance) {
		Vec3d v = new Vec3d(targetPoint.x - currentPoint.x, targetPoint.y - currentPoint.y, targetPoint.z - currentPoint.z);
		v = v.normalize().scale( currentPoint.distanceToTarget / pathOptions.length);
		for(int i = 0; i < pathOptions.length; i++) {
			pathOptions[i] = openPoint((int)(currentPoint.x + v.x * (i+1)), (int)(currentPoint.y + v.y * (i+1)), (int)(currentPoint.z + v.z * (i+1)));
		}
		return pathOptions.length;
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
