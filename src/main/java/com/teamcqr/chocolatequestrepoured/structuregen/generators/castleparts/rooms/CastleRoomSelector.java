package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.util.BlockPlacement;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class CastleRoomSelector
{
    private static final int MAX_LAYERS = 5;

    private BlockPos startPos;
    private int floorHeight;
    private int roomSize;
    private int floorsPerLayer;
    private int maxFloors;
    private int usedFloors;
    private int numSlotsX;
    private int numSlotsZ;
    private Random random;
    private RoomGrid grid;

    public CastleRoomSelector(BlockPos startPos, int roomSize, int floorHeight, int floorsPerLayer, int numSlotsX, int numSlotsZ, Random random)
    {
        this.startPos = startPos;
        this.floorHeight = floorHeight;
        this.roomSize = roomSize;
        this.floorsPerLayer = floorsPerLayer;
        this.maxFloors = floorsPerLayer * MAX_LAYERS;
        this.numSlotsX = numSlotsX;
        this.numSlotsZ = numSlotsZ;
        this.random = random;

        this.grid = new RoomGrid(maxFloors, numSlotsX, numSlotsZ, random);
    }

    public void generateRooms(ArrayList<BlockPlacement> blocks)
    {
        for (CastleRoom room : grid.getRooms())
        {
            room.generate(blocks);
        }
    }

    public void fillRooms()
    {
        addMainBuilding();

        boolean vertical = random.nextBoolean();
        boolean largeBuilding = (numSlotsX >= 2 && numSlotsZ >= 2);

        if (largeBuilding)
        {
            for (int floor = 0; floor < usedFloors; floor++)
            {
                if (vertical)
                {
                    buildVerticalFloorHallway(floor);
                }
                else
                {
                    buildHorizontalFloorHallway(floor);
                }
                vertical = !vertical;
            }

            //addStairCases();
        }

        ArrayList<RoomGridCell> unTyped = grid.getCellsWithoutAType();
        for (RoomGridCell selection : unTyped)
        {
            selection.setRoom(new CastleRoomKitchen(getRoomStart(selection), roomSize,
                    floorHeight, getPositionFromIndex(selection.getGridX(), selection.getGridZ())));
        }

        //addEntrances();
        //connectRooms();
        //placeDoors();
        DetermineRoofs();

        //System.out.println(grid.printGrid());
    }

    private void addMainBuilding()
    {
        setFirstLayerBuildable();

        for (int layer = 0; layer < MAX_LAYERS; layer++)
        {
            int minX = grid.getMinBuildableXOnFloor(layer * floorsPerLayer);
            int maxX = grid.getMaxBuildableXOnFloor(layer * floorsPerLayer);
            int maxLenX = maxX - minX + 1;
            int minZ = grid.getMinBuildableZOnFloor(layer * floorsPerLayer);
            int maxZ = grid.getMaxBuildableZOnFloor(layer * floorsPerLayer);
            int maxLenZ = maxZ - minZ + 1;

            if (Math.min(maxLenX, maxLenZ) < 2)
            {
                break;
            }

            int mainRoomsX = randomSubsectionLength(maxLenX);
            int mainRoomsZ = randomSubsectionLength(maxLenZ);

            int offsetX = random.nextBoolean() ? 0 : maxLenX - mainRoomsX;
            int offsetZ = random.nextBoolean() ? 0 : maxLenZ - mainRoomsZ;

            for (int floor = 0; floor < floorsPerLayer; floor++)
            {
                usedFloors++;
                for (int x = 0; x < mainRoomsX; x++)
                {
                    for (int z = 0; z < mainRoomsZ; z++)
                    {
                        int xIndex = minX + offsetX + x;
                        int zIndex = minZ + offsetZ + z;
                        int floorIndex = floor + (layer * floorsPerLayer);
                        grid.selectRoomForBuilding(floorIndex, xIndex, zIndex);
                        grid.setRoomAsMainStruct(floorIndex, xIndex, zIndex);

                        int oneLayerUp = floorIndex + floorsPerLayer;
                        if (oneLayerUp < maxFloors)
                        {
                            grid.setCellBuilable(oneLayerUp, xIndex, zIndex);
                        }
                    }
                }
            }

            int openCellsWest = offsetX;
            int openCellsNorth = offsetZ;
            int openCellsEast = maxLenX - mainRoomsX - offsetX;
            int openCellsSouth = maxLenZ - mainRoomsZ - offsetZ;
        }
    }

    private void setFirstLayerBuildable()
    {
        for (int floor = 0; floor < floorsPerLayer; floor++)
        {
            for (int x = 0; x < numSlotsX; x++)
            {
                for (int z = 0; z < numSlotsZ; z++)
                {
                    grid.setCellBuilable(floor, x, z);
                }
            }
        }
    }

    private int randomSubsectionLength(int mainLength)
    {
        int rounding = (mainLength % 2 == 0) ? 0 : 1;
        int halfLen = mainLength / 2;
        return halfLen + random.nextInt(halfLen + rounding);
    }

    private void buildVerticalFloorHallway(int floor)
    {
        ArrayList<RoomGridCell> mainRooms = grid.getSelectedMainStructCells(floor);
        RoomGridCell rootRoom = mainRooms.get(random.nextInt(mainRooms.size()));
        ArrayList<RoomGridCell> hallwayRooms = grid.getSelectedCellsInColumn(floor, rootRoom.getGridX());

        for (RoomGridCell selection : hallwayRooms)
        {
            selection.setRoom(new CastleRoomHallway(getRoomStart(selection), roomSize, floorHeight,
                    getPositionFromIndex(selection.getGridX(), selection.getGridZ()), CastleRoomHallway.Alignment.VERTICAL));
        }
    }

    private void buildHorizontalFloorHallway(int floor)
    {
        ArrayList<RoomGridCell> mainRooms = grid.getSelectedMainStructCells(floor);
        RoomGridCell rootRoom = mainRooms.get(random.nextInt(mainRooms.size()));
        ArrayList<RoomGridCell> hallwayRooms = grid.getSelectedCellsInRow(floor, rootRoom.getGridZ());

        for (RoomGridCell selection : hallwayRooms)
        {
            selection.setRoom(new CastleRoomHallway(getRoomStart(selection), roomSize, floorHeight,
                    getPositionFromIndex(selection.getGridX(), selection.getGridZ()), CastleRoomHallway.Alignment.HORIZONTAL));
        }
    }

    private void placeDoors()
    {
        for (int floor = 0; (floor < maxFloors); floor++)
        {
            for (int z = 0; (z < numSlotsZ); z++)
            {
                for (int x = 0; (x < numSlotsX); x++)
                {
                    CastleRoom room = grid.getRoomAt(floor, x, z);
                    if (room != null && roomIsStaircaseOrLanding(room))
                    {
                        EnumFacing hallDirection = getAdjacentHallwayDirection(floor, x, z);
                        if (hallDirection == EnumFacing.SOUTH || hallDirection == EnumFacing.EAST)
                        {
                            room.addDoorOnSide(hallDirection);
                        }
                        else if (hallDirection == EnumFacing.WEST)
                        {
                            grid.getRoomAt(floor, x - 1, z).addDoorOnSide(EnumFacing.EAST);
                        }
                        else if (hallDirection == EnumFacing.NORTH)
                        {
                            grid.getRoomAt(floor, x, z - 1).addDoorOnSide(EnumFacing.SOUTH);
                        }
                    }
                }
            }
        }
    }

    private boolean roomIsStaircaseOrLanding(CastleRoom room)
    {
        boolean result = false;
        if (room != null)
        {
            CastleRoom.RoomType type = room.getRoomType();
            if (type == CastleRoom.RoomType.STAIRCASE || type == CastleRoom.RoomType.LANDING)
            {
                result = true;
            }
        }

        return result;
    }


    private void addStairCases()
    {
        boolean stairsPlaced = false;
        //only iterate through floors thar aren't the top floor
        for (int floor = 0; (floor < maxFloors - 1) && (!stairsPlaced); floor++)
        {
            for (int z = 0; (z < numSlotsZ) && (!stairsPlaced); z++)
            {
                for (int x = 0; (x < numSlotsX) && (!stairsPlaced); x++)
                {
                    if (!grid.isRoomFilled(floor, x, z) &&
                            !grid.isRoomFilled(floor + 1, x, z) &&
                            roomBordersHallway(floor, x, z) &&
                            roomBordersHallway(floor + 1, x, z))
                    {
                        addStairCaseAndLanding(floor, x, z);
                        stairsPlaced = true;
                    }
                }
            }
        }
    }

    private boolean roomBordersHallway(int floor, int x, int z)
    {
        return getAdjacentHallwayDirection(floor, x, z) != EnumFacing.DOWN;
    }

    private EnumFacing getAdjacentHallwayDirection(int floor, int x, int z)
    {
        CastleRoom neighborRoom;
        if (x != 0)
        {
            neighborRoom = grid.getRoomAt(floor, x - 1, z);
            if (neighborRoom != null && neighborRoom.roomType == CastleRoom.RoomType.HALLWAY)
            {
                return EnumFacing.WEST;
            }
        }
        if (z != 0)
        {
            neighborRoom = grid.getRoomAt(floor, x, z - 1);
            if (x != 0 && neighborRoom != null && neighborRoom.roomType == CastleRoom.RoomType.HALLWAY)
            {
                return EnumFacing.NORTH;
            }
        }
        if (x < numSlotsX - 1)
        {
            neighborRoom = grid.getRoomAt(floor, x + 1, z);
            if (neighborRoom != null && neighborRoom.roomType == CastleRoom.RoomType.HALLWAY)
            {
                return EnumFacing.EAST;
            }
        }
        if (z < numSlotsZ - 1)
        {
            neighborRoom = grid.getRoomAt(floor, x, z + 1);
            if (x != 0 && neighborRoom != null && neighborRoom.roomType == CastleRoom.RoomType.HALLWAY)
            {
                return EnumFacing.SOUTH;
            }
        }
        return EnumFacing.DOWN;
    }

    private void connectRooms()
    {
        System.out.println("Connecting rooms");
        for (int floor = 0; floor < maxFloors; floor++)
        {
            ArrayList<RoomGridCell> roomList = grid.getUnreachableRoomList(floor);
        }
    }

    private void DetermineRoofs()
    {
        for (RoomGridCell cell : grid.getSelectionListCopy())
        {
            if (cell.isPopulated() && !grid.adjacentCellIsPopulated(cell, EnumFacing.UP))
            {
                for (EnumFacing direction : EnumFacing.HORIZONTALS)
                {
                    if (!grid.adjacentCellIsPopulated(cell, direction))
                    {
                        cell.getRoom().addRoofEdge(direction);
                    }
                }
            }
        }
    }


    private void addEntranceToSide(EnumFacing side)
    {
        ArrayList<RoomGridCell> rooms = grid.getSelectionListCopy();
        rooms.removeIf(r -> r.getFloor() > 0);
        if (side == EnumFacing.NORTH)
        {
            rooms.removeIf(r -> r.getGridZ() > 0);
        }
        else if (side == EnumFacing.SOUTH)
        {
            rooms.removeIf(r -> r.getGridZ() < numSlotsZ - 1);
        }
        else if (side == EnumFacing.WEST)
        {
            rooms.removeIf(r -> r.getGridX() > 0);
        }
        else if (side == EnumFacing.EAST)
        {
            rooms.removeIf(r -> r.getGridX() < numSlotsX - 1);
        }

        if (!rooms.isEmpty())
        {
            rooms.get(random.nextInt(rooms.size())).getRoom().addDoorOnSide(side);
        }
    }

    //Translate the room's (x, z) position in the floor array to a RoomPosition enum
    private CastleRoom.RoomPosition getPositionFromIndex(int x, int z)
    {
        if (x == 0 && z == 0)
        {
            return CastleRoom.RoomPosition.TOP_LEFT;
        }
        else if (x == 0 && z < numSlotsZ - 1)
        {
            return CastleRoom.RoomPosition.MID_LEFT;
        }
        else if (x == 0)
        {
            return CastleRoom.RoomPosition.BOT_LEFT;
        }
        else if (x < numSlotsX - 1 && z == 0)
        {
            return CastleRoom.RoomPosition.TOP_MID;
        }
        else if (x < numSlotsX - 1  && z < numSlotsZ - 1)
        {
            return CastleRoom.RoomPosition.MID;
        }
        else if (x < numSlotsX - 1 )
        {
            return CastleRoom.RoomPosition.BOT_MID;
        }
        else if (z == 0)
        {
            return CastleRoom.RoomPosition.TOP_RIGHT;
        }
        else if (z < numSlotsZ - 1)
        {
            return CastleRoom.RoomPosition.MID_RIGHT;
        }
        else
        {
            return CastleRoom.RoomPosition.BOT_RIGHT;
        }
    }

    private BlockPos getRoomStart(int floor, int x, int z)
    {
        return startPos.add(x * roomSize, floor * floorHeight, z * roomSize);
    }

    private BlockPos getRoomStart(RoomGridCell selection)
    {
        int gridX = selection.getGridX();
        int floor = selection.getFloor();
        int gridZ = selection.getGridZ();
        return startPos.add(gridX * roomSize, floor * floorHeight, gridZ * roomSize);
    }

    private void addRoomHallway(int floor, int x, int z, CastleRoomHallway.Alignment alignment)
    {
        CastleRoom room = new CastleRoomHallway(getRoomStart(floor, x, z), roomSize, floorHeight, getPositionFromIndex(x, z), alignment);
        grid.addRoomAt(room, floor, x, z);
    }

    private void addStairCaseAndLanding(int stairFloor, int stairX, int stairZ)
    {
        addRoomStaircase(stairFloor, stairX, stairZ);
        addRoomLanding(stairFloor + 1, stairX, stairZ, (CastleRoomStaircase)grid.getRoomAt(stairFloor, stairX, stairZ));
    }

    private void addRoomStaircase(int floor, int x, int z)
    {
        EnumFacing doorSide = getAdjacentHallwayDirection(floor, x, z);
        CastleRoom room = new CastleRoomStaircase(getRoomStart(floor, x, z), roomSize, floorHeight, getPositionFromIndex(x, z), doorSide);
        grid.addRoomAt(room, floor, x, z);
        grid.setRoomReachable(floor, x, z);
    }

    private void addRoomLanding(int floor, int x, int z, CastleRoomStaircase stairsBelow)
    {
        CastleRoom room = new CastleRoomLanding(getRoomStart(floor, x, z), roomSize, floorHeight, getPositionFromIndex(x, z), stairsBelow);
        grid.addRoomAt(room, floor, x, z);
        grid.setRoomReachable(floor, x, z);
    }

    private int getLayerFromFloor(int floor)
    {
        return floor / floorsPerLayer;
    }
}