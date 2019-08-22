package com.teamcqr.chocolatequestrepoured.dungeongen.Generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.util.BlockPlacement;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class CastleRoomSelector
{
    private BlockPos startPos;
    private int floorHeight;
    private int roomSize;
    private int numFloors;
    private int numSlotsX;
    private int numSlotsZ;
    private Random random;
    private RoomSelection[][][] roomArray;

    private boolean isMainStruct;

    public CastleRoomSelector(BlockPos startPos, int roomSize, int floorHeight, int numFloors, int numSlotsX, int numSlotsZ, Random random)
    {
        this.startPos = startPos;
        this.floorHeight = floorHeight;
        this.roomSize = roomSize;
        this.numFloors = numFloors;
        this.numSlotsX = numSlotsX;
        this.numSlotsZ = numSlotsZ;
        this.random = random;
        this.roomArray = new RoomSelection[numFloors][numSlotsX][numSlotsZ];
    }

    public void generateRooms(ArrayList<BlockPlacement> blocks)
    {
        for (CastleRoom room : getRoomList())
        {
            room.generate(blocks);
        }
    }

    public void fillRooms()
    {
        boolean vertical = random.nextBoolean();
        boolean largeBuilding = (numSlotsX >= 2 && numSlotsZ >= 2);

        if (largeBuilding)
        {
            for (int floor = 0; floor < numFloors; floor++)
            {
                buildFloorHallway(floor, vertical);
                vertical = !vertical;
            }

            addStairCases();
        }

        for (int floor = 0; floor < numFloors; floor++)
        {
            for (int z = 0; z < numSlotsZ; z++)
            {
                for (int x = 0; x < numSlotsX; x++)
                {
                    if (!isRoomFilled(floor, x, z))
                    {
                        addRoomAt(new CastleRoomKitchen(getRoomStart(floor, x, z), roomSize, floorHeight, getPositionFromIndex(x, z)), floor, x, z);
                    }
                }
            }
        }

        addEntrances();
        connectRooms();
        placeDoors();
    }

    private void addMainBuilding()
    {
        int halfX = numSlotsX / 2;
        int halfZ = numSlotsZ / 2;
        int mainRoomsX = halfX + random.nextInt(halfX);
        int mainRoomsZ = halfZ + random.nextInt(halfZ);
        int offsetX = getRandomXOffsetForRooms(mainRoomsX);
        int offsetZ = getRandomZOffsetForRooms(mainRoomsZ);

        for (int floor = 0; floor < numFloors; floor++)
        {
            for (int x = 0; x < mainRoomsX; x++)
            {
                for (int z = 0; z < mainRoomsZ; z++)
                {
                    int xIndex = offsetX + x;
                    int zIndex = offsetZ + z;
                    setRoomPopulated(floor, xIndex, zIndex);
                }
            }
        }
    }

    private void buildFloorHallway(int floor, boolean vertical)
    {
        //fill the hallway - each floor must have at least 1 hallway

        //start at a random z index that isn't already filled
        if (vertical)
        {
            int hallStartX = 0;
            //TODO: Try the other side if this side filled already
            do
            {
                hallStartX = random.nextInt(numSlotsX);
            } while (isRoomFilled(floor, hallStartX,0));

            for (int z = 0; z < numSlotsZ; z++)
            {
                addRoomHallway(floor, hallStartX, z, true);
            }
        } else
        {
            int hallStartZ = 0;
            //TODO: Try the other side if this side filled already
            do
            {
                hallStartZ = random.nextInt(numSlotsZ);
            } while (isRoomFilled(floor, 0, hallStartZ));

            for (int x = 0; x < numSlotsX; x++)
            {
                addRoomHallway(floor, x, hallStartZ, false);
            }
        }
    }

    private void placeDoors()
    {
        for (int floor = 0; (floor < numFloors); floor++)
        {
            for (int z = 0; (z < numSlotsZ); z++)
            {
                for (int x = 0; (x < numSlotsX); x++)
                {
                    CastleRoom room = getRoomAt(floor, x, z);
                    if (room != null && roomIsStaircaseOrLanding(room))
                    {
                        EnumFacing hallDirection = getAdjacentHallwayDirection(floor, x, z);
                        if (hallDirection == EnumFacing.SOUTH || hallDirection == EnumFacing.EAST)
                        {
                            room.addDoorOnSide(hallDirection);
                        }
                        else if (hallDirection == EnumFacing.WEST)
                        {
                            getRoomAt(floor, x - 1, z).addDoorOnSide(EnumFacing.EAST);
                        }
                        else if (hallDirection == EnumFacing.NORTH)
                        {
                            getRoomAt(floor, x, z - 1).addDoorOnSide(EnumFacing.SOUTH);
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
        for (int floor = 0; (floor < numFloors - 1) && (!stairsPlaced); floor++)
        {
            for (int z = 0; (z < numSlotsZ) && (!stairsPlaced); z++)
            {
                for (int x = 0; (x < numSlotsX) && (!stairsPlaced); x++)
                {
                    if (!isRoomFilled(floor, x, z) &&
                            !isRoomFilled(floor + 1, x, z) &&
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
            neighborRoom = getRoomAt(floor, x - 1, z);
            if (neighborRoom != null && neighborRoom.roomType == CastleRoom.RoomType.HALLWAY)
            {
                return EnumFacing.WEST;
            }
        }
        if (z != 0)
        {
            neighborRoom = getRoomAt(floor, x, z - 1);
            if (x != 0 && neighborRoom != null && neighborRoom.roomType == CastleRoom.RoomType.HALLWAY)
            {
                return EnumFacing.NORTH;
            }
        }
        if (x < numSlotsX - 1)
        {
            neighborRoom = getRoomAt(floor, x + 1, z);
            if (neighborRoom != null && neighborRoom.roomType == CastleRoom.RoomType.HALLWAY)
            {
                return EnumFacing.EAST;
            }
        }
        if (z < numSlotsZ - 1)
        {
            neighborRoom = getRoomAt(floor, x, z + 1);
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
        for (int floor = 0; floor < numFloors; floor++)
        {
            ArrayList<RoomSelection> roomList = getUnreachableRoomList(floor);
            while (!roomList.isEmpty())
            {
                RoomSelection currentRoom;

                currentRoom = roomList.get(random.nextInt(roomList.size()));
                connectRoomToNearestReachable(currentRoom);

                roomList = getUnreachableRoomList(floor);
            }
        }
    }

    public void connectRoomToNearestReachable(RoomSelection roomSelection)
    {
        RoomGridPosition gridPos = roomSelection.getGridPosition();
        int floor = gridPos.getFloor();
        int x = gridPos.getX();
        int z = gridPos.getZ();
        EnumFacing buildDirection = getDirectionOfNearestReachable(gridPos);

        if (buildDirection != EnumFacing.DOWN)
        {
            while (withinGridBounds(x, z) && !isRoomReachable(floor, x, z))
            {
                CastleRoom room = getRoomAt(floor, x, z);
                setRoomReachable(floor, x, z);

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
                        CastleRoom roomNorth = getRoomAt(floor, x, z);
                        roomNorth.addDoorOnSide(buildDirection.getOpposite());
                    }
                } else if (buildDirection == EnumFacing.WEST)
                {
                    if (x != 0)
                    {
                        x--;
                        CastleRoom roomWest = getRoomAt(floor, x, z);
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
                if (isRoomReachable(floor, x, z))
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
                if (isRoomReachable(floor, x, z))
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
                if (isRoomReachable(floor, x, z))
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
                if (isRoomReachable(floor, x, z))
                {
                    result = currentDistance;
                    break;
                }
            }
        }
        return result;
    }

    private ArrayList<RoomSelection> getUnreachableRoomList(int floor)
    {
        ArrayList<RoomSelection> result = new ArrayList<>();
        for (int x = 0; x < roomArray[0].length; x++)
        {
            for (int z = 0; z < roomArray[0][0].length; z++)
            {
                if (roomArray[floor][x][z] != null && !roomArray[floor][x][z].isReachable())
                    result.add(roomArray[floor][x][z]);
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
        ArrayList<RoomSelection> rooms = getRoomSelectionList();
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

    private void addRoomHallway(int floor, int x, int z, boolean vertical)
    {
        CastleRoom room = new CastleRoomHallway(getRoomStart(floor, x, z), roomSize, floorHeight, getPositionFromIndex(x, z), vertical);
        addRoomAt(room, floor, x, z);
    }

    private void addStairCaseAndLanding(int stairFloor, int stairX, int stairZ)
    {
        addRoomStaircase(stairFloor, stairX, stairZ);
        addRoomLanding(stairFloor + 1, stairX, stairZ, (CastleRoomStaircase) getRoomAt(stairFloor, stairX, stairZ));
    }

    private void addRoomStaircase(int floor, int x, int z)
    {
        EnumFacing doorSide = getAdjacentHallwayDirection(floor, x, z);
        CastleRoom room = new CastleRoomStaircase(getRoomStart(floor, x, z), roomSize, floorHeight, getPositionFromIndex(x, z), doorSide);
        addRoomAt(room, floor, x, z);
        setRoomReachable(floor, x, z);
    }

    private void addRoomLanding(int floor, int x, int z, CastleRoomStaircase stairsBelow)
    {
        CastleRoom room = new CastleRoomLanding(getRoomStart(floor, x, z), roomSize, floorHeight, getPositionFromIndex(x, z), stairsBelow);
        addRoomAt(room, floor, x, z);
        setRoomReachable(floor, x, z);
    }

    private int getRandomXOffsetForRooms(int numRooms)
    {
        if (numRooms <= numSlotsX)
        {
            int maxOffset = numSlotsX - numRooms;
            return random.nextInt(maxOffset + 1);
        }
        return -1;
    }

    private int getRandomZOffsetForRooms(int numRooms)
    {
        if (numRooms <= numSlotsZ)
        {
            int maxOffset = numSlotsZ - numRooms;
            return random.nextInt(maxOffset + 1);
        }
        return -1;
    }

    private void setRoomReachable(int floor, int x, int z)
    {
        roomArray[floor][x][z].setReachable();
    }

    private ArrayList<CastleRoom> getRoomList()
    {
        ArrayList<RoomSelection> rooms = getRoomSelectionList();
        ArrayList<CastleRoom> result = new ArrayList<>();

        for (RoomSelection rs: rooms)
        {
            result.add(rs.getRoom());
        }

        return result;
    }

    private ArrayList<RoomSelection> getRoomSelectionList()
    {
        ArrayList<RoomSelection> result = new ArrayList<>();
        for (int floor = 0; floor < roomArray.length; floor++)
        {
            for (int x = 0; x < roomArray[0].length; x++)
            {
                for (int z = 0; z < roomArray[0][0].length; z++)
                {
                    if (roomArray[floor][x][z] != null)
                    {
                        result.add(roomArray[floor][x][z]);
                    }
                }
            }
        }

        return result;
    }

    public void setRoomPopulated(int floor, int x, int z)
    {
        roomArray[floor][x][z].setPopulated();
    }

    private boolean isRoomFilled(int floor, int x, int z)
    {
        return roomArray[floor][x][z] != null && roomArray[floor][x][z].getRoom() != null;
    }

    private boolean isRoomReachable(int floor, int x, int z)
    {
        return roomArray[floor][x][z].isReachable();
    }

    private void addRoomAt(CastleRoom room, int floor, int x, int z)
    {
        roomArray[floor][x][z] = new RoomSelection(floor, x, z, room);
    }


    private CastleRoom getRoomAt(int floor, int x, int z)
    {
        if (roomArray[floor][x][z] != null)
        {
            return (roomArray[floor][x][z].getRoom());
        }
        else
        {
            return null;
        }
    }

    private boolean withinGridBounds(int x, int z)
    {
        return (x >= 0 && x < numSlotsX && z >= 0 && z < numSlotsZ);
    }

    //print the room array in a grid, floor by floor
    public String printGrid()
    {
        String result = "";
        for (int floor = 0; floor < numFloors; floor++)
        {
            result += "\nFloor " + floor + "\n";
            for (int z = 0; z < numSlotsZ; z++)
            {
                for (int x = 0; x < numSlotsX; x++)
                {
                    if (isRoomFilled(floor, x, z))
                    {
                        result += "[" + getRoomAt(floor, x, z).getNameShortened() + "|" + getRoomAt(floor, x, z).getPositionString() + "] ";
                    } else
                    {
                        result += "[NUL|--]";
                    }
                }
                result += "\n";
            }
        }
        result += "----------------\n";
        return result;
    }

}
