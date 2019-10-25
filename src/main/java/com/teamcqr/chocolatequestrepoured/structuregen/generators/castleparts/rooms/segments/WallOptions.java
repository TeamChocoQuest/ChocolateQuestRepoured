package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.segments;

public class WallOptions
{
    private enum DoorOptions
    {
        NONE,
        CENTERED,
        RANDOM
    }

    private boolean hasWindow;
    private DoorOptions door;

    public WallOptions(boolean withWindow)
    {
        this.hasWindow = withWindow;
    }

    public void addDoorCentered()
    {
        this.door = DoorOptions.CENTERED;
    }

    public void addDoorRandom()
    {
        this.door = DoorOptions.RANDOM;
    }

    public boolean hasWindow()
    {
        return hasWindow;
    }

    public DoorOptions getDoor()
    {
        return this.door;
    }
}
