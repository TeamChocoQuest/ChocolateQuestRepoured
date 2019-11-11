package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.segments;

import net.minecraft.util.EnumFacing;

public class DoorPlacement
{
    public static int DEFAULT_WIDTH = 3;
    public static int DEFAULT_HEIGHT = 4;

    private int offset;
    private int width;
    private int height;

    public DoorPlacement(int offset, int width, int height)
    {
        this.offset = offset;
        this.width = width;
        this.height = height;
    }

    public int getOffset()
    {
        return offset;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }
}
