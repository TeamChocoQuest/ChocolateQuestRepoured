package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import net.minecraft.util.math.Vec3i;

public enum EnumRoomType
{
        NONE(false, false, true, new Vec3i(999, 999, 999)),

        KITCHEN(false, false, true, new Vec3i(3, 1, 3)),
        BEDROOM(false, false, true, new Vec3i(3, 1, 3)),
        ARMORY(false, false, true, new Vec3i(3, 1, 3)),
        ALCHEMY_LAB(false, false, true, new Vec3i(2, 1, 2)),

        WALKABLE_ROOF(false, false, false, new Vec3i(1, 1, 1)),
        WALKABLE_TOWER_ROOF(false, true, false, new Vec3i(1, 1, 1)),

        HALLWAY(false, false, true, new Vec3i(999, 999, 999)),
        STAIRCASE_DIRECTED(true, false, true, new Vec3i(1, 1, 1)),
        STAIRCASE_SPIRAL(true, false, true, new Vec3i(1, 1, 1)),
        LANDING_DIRECTED(true, false, true, new Vec3i(1, 1, 1)),
        LANDING_SPIRAL(true, false, true, new Vec3i(1, 1, 1)),
        TOWER_SQUARE(false, true, false, new Vec3i(1, 1, 1));

        private final boolean partOfStairs;
        private final boolean partOfTower;
        private final boolean pathable;
        private final Vec3i maxSize;

        EnumRoomType(boolean stairs, boolean tower, boolean pathable, Vec3i maxSize)
        {
            this.partOfStairs = stairs;
            this.partOfTower = tower;
            this.pathable = pathable;
            this.maxSize = maxSize;
        }

        public boolean isStairRoom()
        {
            return this.partOfStairs;
        }

        public boolean isTowerRoom()
        {
            return this.partOfTower;
        }

        public boolean isPathable()
        {
            return this.pathable;
        }

        public Vec3i getMaxDimensions() { return this.maxSize; }

        public int getMaxXCells() { return this.maxSize.getX(); }
        public int getMaxYCells() { return this.maxSize.getY(); }
        public int getMaxZCells() { return this.maxSize.getZ(); }

}
