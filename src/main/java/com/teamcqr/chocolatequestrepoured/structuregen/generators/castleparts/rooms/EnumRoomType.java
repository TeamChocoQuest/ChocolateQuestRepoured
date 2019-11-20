package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

public enum EnumRoomType
{
        NONE("None", false, false, true),

        KITCHEN("Kitchen", false, false, true),
        BEDROOM("Bedroom", false, false, true),
        ARMORY("Armory", false, false, true),
        ALCHEMY_LAB("Alchemy Lab", false, false, true),

        WALKABLE_ROOF("Walkable Roof", false, false, false),
        WALKABLE_TOWER_ROOF("Walkable Tower Roof", false, true, false),

        HALLWAY("Hallway", false, false, true),
        STAIRCASE_DIRECTED("Directed Stairs", true, false, true),
        STAIRCASE_SPIRAL("Spiral Stairs", true, false, true),
        LANDING_DIRECTED("Directed Landing", true, false, true),
        LANDING_SPIRAL("Spiral Landing", true, false, true),
        TOWER_SQUARE("Square Tower", false, true, false);

        private final String name;
        private final boolean partOfStairs;
        private final boolean partOfTower;
        private final boolean pathable;

        EnumRoomType(String nameIn, boolean partOfStairsIn, boolean partOfTower, boolean pathable)
        {
            this.name = nameIn;
            this.partOfStairs = partOfStairsIn;
            this.partOfTower = partOfTower;
            this.pathable = pathable;
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

        @Override
        public String toString()
        {
            return name;
        }
}
