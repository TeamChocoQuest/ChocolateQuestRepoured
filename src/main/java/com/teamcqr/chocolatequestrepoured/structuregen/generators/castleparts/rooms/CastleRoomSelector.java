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
            for (int floor = 0; floor < maxFloors; floor++)
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

        //System.out.println(grid.printGrid());
    }

    private void addMainBuilding()
    {
        int halfX = numSlotsX / 2;
        int halfZ = numSlotsZ / 2;
        int mainRoomsX = halfX + random.nextInt(halfX);
        int mainRoomsZ = halfZ + random.nextInt(halfZ);
        int offsetX = grid.getRandomXOffsetForRooms(mainRoomsX);
        int offsetZ = grid.getRandomZOffsetForRooms(mainRoomsZ);

        setFirstLayerBuildable();

        for (int floor = 0; floor < maxFloors; floor++)
        {
            for (int x = 0; x < mainRoomsX; x++)
            {
                for (int z = 0; z < mainRoomsZ; z++)
                {
                    int xIndex = offsetX + x;
                    int zIndex = offsetZ + z;
                    grid.selectRoomForBuilding(floor, xIndex, zIndex);
                    grid.setRoomAsMainStruct(floor, xIndex, zIndex);
                }
            }
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
            while (!roomList.isEmpty())
            {
                RoomGridCell currentRoom;

                currentRoom = roomList.get(random.nextInt(roomList.size()));
                connectRoomToNearestReachable(currentRoom);

                roomList = grid.getUnreachableRoomList(floor);
            }
        }
    }

    public void connectRoomToNearestReachable(RoomGridCell roomGridCell)
    {
        RoomGridPosition gridPos = roomGridCell.getGridPosition();
        int floor = gridPos.getFloor();
        int x = gridPos.getX();
        int z = gridPos.getZ();
        EnumFacing buildDirection = getDirectionOfNearestReachable(gridPos);

        if (buildDirection != EnumFacing.DOWN)
        {
            while (grid.withinGridBounds(x, z) && !grid.isRoomReachable(floor, x, z))
            {
                CastleRoom room = grid.getRoomAt(floor, x, z);
                grid.setRoomReachable(floor, x, z);

                if (buildDirection == EnumFacing.SOUTH)
                {
                    z++;
                    room.addDoorOnSide(buildDirection);
                }
                else if (buildDirection == EnumFacing.EAST)
                {
                    x++;
                    room.addDoorOnSide(buildDirection);
                }
                else if (buildDirection == EnumFacing.NORTH)
                {
                    if (z != 0)
                    {
                        z--;
                        CastleRoom roomNorth = grid.getRoomAt(floor, x, z);
                        roomNorth.addDoorOnSide(buildDirection.getOpposite());
                    }
                } else if (buildDirection == EnumFacing.WEST)
                {
                    if (x != 0)
                    {
                        x--;
                        CastleRoom roomWest = grid.getRoomAt(floor, x, z);
                        roomWest.addDoorOnSide(buildDirection.getOpposite());
                    }
                }
            }
        }
    }

    private EnumFacing getDirectionOfNearestReachable(RoomGridPosition gridLocation)
    {
        EnumFacing result = EnumFacing.DOWN;

        class DirectionDistance
        {
            private EnumFacing direction;
            private int distance;

            private DirectionDistance(EnumFacing direction, int distance)
            {
                this.direction = direction;
                this.distance = distance;
            }
        }

        int distance;
        int shortest = Integer.MAX_VALUE;
        ArrayList<DirectionDistance> possibleDirections = new ArrayList<>();

        for (EnumFacing direction : EnumFacing.HORIZONTALS)
        {
            distance = distToReachableRoom(gridLocation.getFloor(), gridLocation.getX(), gridLocation.getZ(), direction);
            possibleDirections.add(new DirectionDistance(direction, distance));
            shortest = Math.min(shortest, distance);
        }
        Iterator<DirectionDistance> it = possibleDirections.iterator();
        while (it.hasNext())
        {
            DirectionDistance dd = it.next();
            if (dd.distance > shortest)
            {
                it.remove();
            }
        }
        if (!possibleDirections.isEmpty())
        {
            result =  possibleDirections.get(random.nextInt(possibleDirections.size())).direction;
        }

        return result;
    }

    private int distToReachableRoom(int floor, int x, int z, EnumFacing direction)
    {
        int result = Integer.MAX_VALUE;
        int currentDistance = 0;
        if (direction == EnumFacing.NORTH)
        {
            while (z > 0)
            {
                z--;
                currentDistance++;
                if (grid.isRoomReachable(floor, x, z))
                {
                    result = currentDistance;
                    break;
                }
            }
        }
        else if (direction == EnumFacing.SOUTH)
        {
            while (z < numSlotsZ - 1)
            {
                z++;
                currentDistance++;
                if (grid.isRoomReachable(floor, x, z))
                {
                    result = currentDistance;
                    break;
                }
            }
        }
        else if (direction == EnumFacing.WEST)
        {
            while (x > 0)
            {
                x--;
                currentDistance++;
                if (grid.isRoomReachable(floor, x, z))
                {
                    result = currentDistance;
                    break;
                }
            }
        }
        else if (direction == EnumFacing.EAST)
        {
            while (x < numSlotsX - 1)
            {
                x++;
                currentDistance++;
                if (grid.isRoomReachable(floor, x, z))
                {
                    result = currentDistance;
                    break;
                }
            }
        }
        return result;
    }

    private void addEntrances()
    {
        for (EnumFacing side : EnumFacing.values())
        {
            addEntranceToSide(side);
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