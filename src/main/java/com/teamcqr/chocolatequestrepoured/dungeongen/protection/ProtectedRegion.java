package com.teamcqr.chocolatequestrepoured.dungeongen.protection;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.world.BlockEvent;

/**
 * Copyright (c) 29.04.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ProtectedRegion {
    private AxisAlignedBB boundingBox;
    private BlockPos center;
    private BlockPos min;
    private BlockPos max;
    private boolean enabled;

    public ProtectedRegion(BlockPos center, double width, double height, double depth) {
        this.center = center;
        this.boundingBox = new AxisAlignedBB(new BlockPos(center.getX()-width/2,center.getY()-height/2,center.getZ()-depth/2));
        this.min = new BlockPos(boundingBox.minX,boundingBox.minY,boundingBox.minZ);
        this.max = new BlockPos(boundingBox.maxX,boundingBox.maxY,boundingBox.maxZ);
    }

    public ProtectedRegion(BlockPos min, BlockPos max) {
        this.boundingBox = new AxisAlignedBB(min,max);
        this.center = new BlockPos(boundingBox.getCenter());
        this.min = new BlockPos(boundingBox.minX,boundingBox.minY,boundingBox.minZ);
        this.max = new BlockPos(boundingBox.maxX,boundingBox.maxY,boundingBox.maxZ);
    }

    public ProtectedRegion(double width, double height, double depth, BlockPos min) {
        this.boundingBox = new AxisAlignedBB(min,new BlockPos(min.getX()+width,min.getY()+height,min.getZ()+depth));
        this.center = new BlockPos(boundingBox.getCenter());
        this.min = new BlockPos(boundingBox.minX,boundingBox.minY,boundingBox.minZ);
        this.max = new BlockPos(boundingBox.maxX,boundingBox.maxY,boundingBox.maxZ);
    }

    public AxisAlignedBB getBoundingBox() {
        return boundingBox;
    }

    public BlockPos getCenter() {
        return center;
    }

    public boolean isBlockInRegion(BlockPos blockPos) {
        return boundingBox.intersects(blockPos.getX(),blockPos.getY(),blockPos.getZ(),blockPos.getX()+1,blockPos.getY()+1,blockPos.getZ()+1);
    }

    public BlockPos getMax() {
        return max;
    }

    public BlockPos getMin() {
        return min;
    }

    public boolean isEnabled() {
        return enabled;
    }


    //Checks if block is in area and is breakable
    public void checkBreakEvent(BlockEvent.BreakEvent e) {
        if(enabled) {
            if(isBlockInRegion(e.getPos())) {
                e.setCanceled(true);
            }
        }
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
