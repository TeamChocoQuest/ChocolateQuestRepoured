package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.segments;

public class WallOptions
{
    public enum DoorPlacement
    {
        NONE,
        CENTERED,
        RANDOM
    }

    private boolean hasWindow;
    private DoorPlacement door;

    public WallOptions(boolean withWindow)
    {
        this.door = DoorPlacement.NONE;
        this.hasWindow = withWindow;
    }

    public void addDoorCentered()
    {
        this.door = DoorPlacement.CENTERED;
        this.hasWindow = false;
    }

    public void addDoorRandom()
    {
        this.door = DoorPlacement.RANDOM;
        this.hasWindow = false;
    }

    public boolean hasWindow()
    {
        return hasWindow;
    }

    public DoorPlacement getDoor()
    {
        return this.door;
    }

    public boolean hasDoor() { return this.door != DoorPlacement.NONE; }

}
