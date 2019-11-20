package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

public enum EnumRoomType
{
        NONE("None", false),

        KITCHEN("Kitchen", false),
        BEDROOM("Bedroom", false),
        ARMORY("Armory", false),
        ALCHEMY_LAB("Alchemy Lab", false),

        WALKABLE_ROOF("Walkable Roof", false),
        HALLWAY("Hallway", false),
        STAIRCASE_DIRECTED("Directed Stairs", true),
        STAIRCASE_SPIRAL("Spiral Stairs", true),
        LANDING_DIRECTED("Directed Landing", true),
        LANDING_SPIRAL("Spiral Landing", true),
        TOWER_SQUARE("Square Tower", false);



        private final String name;
        private final boolean partOfStairs;

        EnumRoomType(String nameIn, boolean partOfStairsIn)
        {
            this.name = nameIn;
            this.partOfStairs = partOfStairsIn;
        }

        public boolean isPartOfStairs()
        {
            return this.partOfStairs;
        }

        @Override
        public String toString()
        {
            return name;
        }
}
