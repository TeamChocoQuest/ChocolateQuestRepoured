package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.segments;

import net.minecraft.util.EnumFacing;

public class DoorPlacement
{

    private int offset;
    private EnumCastleDoorType type;

    public DoorPlacement(int offset, EnumCastleDoorType type)
    {
        this.offset = offset;
        this.type = type;
    }

    public int getOffset()
    {
        return offset;
    }

    public EnumCastleDoorType getType()
    {
        return type;
    }

    public int getWidth()
    {
        return type.getWidth();
    }

    public int getHeight()
    {
        return type.getWidth();
    }
}
