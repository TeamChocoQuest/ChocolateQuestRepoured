package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.segments;

public class WallOptions
{

    private boolean hasWindow;
    private DoorPlacement door;

    public WallOptions(boolean withWindow)
    {
        this.door = null;
        this.hasWindow = withWindow;
    }

    public void addDoor(DoorPlacement door)
    {
        this.door = door;
        this.hasWindow = false; //doors override windows because they usually overlap
    }

    public boolean hasWindow()
    {
        return hasWindow;
    }

    public DoorPlacement getDoor()
    {
        return this.door;
    }

    public boolean hasDoor()
    {
        return this.door != null;
    }

}
