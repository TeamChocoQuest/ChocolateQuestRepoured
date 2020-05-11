package com.teamcqr.chocolatequestrepoured.objects.entity.ai.navigator;

import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PathNavigateNetherDragon extends PathNavigate {

	public PathNavigateNetherDragon(EntityLiving entity, World world) {
		super(entity, world);
	}

	@Override
	protected PathFinder getPathFinder() {
		this.nodeProcessor = new DirectLineNodeProcessor();
        this.nodeProcessor.setCanEnterDoors(true);
        this.nodeProcessor.setCanSwim(true);
        return new PathFinder(this.nodeProcessor);
	}
	
	public void onUpdateNavigation()
    {
        ++this.totalTicks;

        if (this.tryUpdatePath)
        {
            this.updatePath();
        }

        if (!this.noPath())
        {
            if (this.canNavigate())
            {
                this.pathFollow();
            }
            else if (this.currentPath != null && this.currentPath.getCurrentPathIndex() < this.currentPath.getCurrentPathLength())
            {
                Vec3d vec3d = this.currentPath.getVectorFromIndex(this.entity, this.currentPath.getCurrentPathIndex());

                if (MathHelper.floor(this.entity.posX) == MathHelper.floor(vec3d.x) && MathHelper.floor(this.entity.posY) == MathHelper.floor(vec3d.y) && MathHelper.floor(this.entity.posZ) == MathHelper.floor(vec3d.z))
                {
                    this.currentPath.setCurrentPathIndex(this.currentPath.getCurrentPathIndex() + 1);
                }
            }

            this.debugPathFinding();

            if (!this.noPath())
            {
                Vec3d vec3d1 = this.currentPath.getPosition(this.entity);
                this.entity.getMoveHelper().setMoveTo(vec3d1.x, vec3d1.y, vec3d1.z, this.speed);
            }
        }
    }

	@Override
	protected Vec3d getEntityPosition() {
		return entity.getPositionVector();
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
