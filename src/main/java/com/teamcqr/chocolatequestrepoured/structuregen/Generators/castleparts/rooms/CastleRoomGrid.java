package com.teamcqr.chocolatequestrepoured.structuregen.Generators.castleparts.rooms;

import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class CastleRoomGrid
{
    private RoomSelection[][][] roomArray;
    private int floors;
    private int sizeX;
    private int sizeZ;
    private Random random;

    public class RoomSelection
    {
        private GridPosition gridLocation;
        private boolean populated;
        private boolean reachable;
        private CastleRoom room;

        public RoomSelection(GridPosition gridLocation, CastleRoom room)
        {
            this.gridLocation = gridLocation;
            this.populated = true;
            this.reachable = false;
            this.room = room;
        }

        public RoomSelection(GridPosition gridLocation)
        {
            this.gridLocation = gridLocation;
            this.populated = false;
            this.reachable = false;
            this.room = null;
        }
    }

    public class GridPosition
    {
        private int floor;
        private int x;
        private int z;

        private GridPosition(int floor, int x, int z)
        {
            this.floor = floor;
            this.x = x;
            this.z = z;
        }
    }

    public CastleRoomGrid(int floors, int roomsX, int roomsZ, Random random)
    {
        this.roomArray = new RoomSelection[floors][roomsX][roomsZ];
        this.floors = floors;
        this.sizeX = roomsX;
        this.sizeZ = roomsZ;
        this.random = random;
    }

    public void connectRoomToNearestReachable(RoomSelection roomSelection)
    {
        GridPosition gridPos = roomSelection.gridLocation;
        int floor = gridPos.floor;
        int x = gridPos.x;
        int z = gridPos.z;
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

    private EnumFacing getDirectionOfNearestReachable(GridPosition gridLocation)
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
            distance = distToReachableRoom(gridLocation.floor, gridLocation.x, gridLocation.z, direction);
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
            while (z < sizeZ - 1)
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
            while (x < sizeX - 1)
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

    public void addEntranceToSide(EnumFacing side)
    {
        ArrayList<RoomSelection> rooms = getRoomSelectionList();
        rooms.removeIf(r -> r.gridLocation.floor > 0);
        if (side == EnumFacing.NORTH)
        {
            rooms.removeIf(r -> r.gridLocation.z > 0);
        }
        else if (side == EnumFacing.SOUTH)
        {
            rooms.removeIf(r -> r.gridLocation.z < sizeZ - 1);
        }
        else if (side == EnumFacing.WEST)
        {
            rooms.removeIf(r -> r.gridLocation.x > 0);
        }
        else if (side == EnumFacing.EAST)
        {
            rooms.removeIf(r -> r.gridLocation.x < sizeX - 1);
        }

        if (!rooms.isEmpty())
        {
            rooms.get(random.nextInt(rooms.size())).room.addDoorOnSide(side);
        }
    }

    public void removeWallsFromRoomsOnSide(EnumFacing side)
    {
        Stream<CastleRoom> roomStream = getRoomList().stream().filter(r -> r.position.isOnSide(side));;
        roomStream.forEach(r -> r.disableWallOnSide(side));
    }

    public void addRoomAt(CastleRoom room, int floor, int x, int z)
    {
        roomArray[floor][x][z] = new RoomSelection(new GridPosition(floor, x, z), room);
    }

    public void addRoomAt(CastleRoom room, GridPosition gridPos)
    {
        roomArray[gridPos.floor][gridPos.x][gridPos.z] = new RoomSelection(gridPos, room);
    }

    public CastleRoom getRoomAt(int floor, int x, int z)
    {
        if (roomArray[floor][x][z] != null)
        {
            return (roomArray[floor][x][z].room);
        }
        else
        {
            return null;
        }
    }

    public void addDoorToRoomAllFloors(int x, int z, EnumFacing side)
    {
        for (int floor = 0; floor < floors; floor++)
        {
            roomArray[floor][x][z].room.addDoorOnSide(side);
        }
    }

    public CastleRoom getRoomAt(GridPosition gridPos)
    {
        if (roomArray[gridPos.floor][gridPos.x][gridPos.z] != null)
        {
            return (roomArray[gridPos.floor][gridPos.x][gridPos.z].room);
        }
        else
        {
            return null;
        }
    }

    public boolean isRoomFilled(int floor, int x, int z)
    {
        return roomArray[floor][x][z] != null && roomArray[floor][x][z].room != null;
    }

    public boolean isRoomFilled(GridPosition gridPos)
    {
        return roomArray[gridPos.floor][gridPos.x][gridPos.z] != null && roomArray[gridPos.floor][gridPos.x][gridPos.z].room != null;
    }

    public boolean isRoomReachable(int floor, int x, int z)
    {
        return roomArray[floor][x][z].reachable;
    }

    public void setRoomReachableAllFloors(int x, int z)
    {
        for (int floor = 0; floor < floors; floor++)
        {
            setRoomReachable(floor, x, z);
        }
    }

    public void setRoomReachable(int floor, int x, int z)
    {
        roomArray[floor][x][z].reachable = true;
    }

    public ArrayList<RoomSelection> getUnreachableRoomList(int floor)
    {
        ArrayList<RoomSelection> result = new ArrayList<>();
        for (int x = 0; x < roomArray[0].length; x++)
        {
            for (int z = 0; z < roomArray[0][0].length; z++)
            {
                if (roomArray[floor][x][z] != null && !roomArray[floor][x][z].reachable)
                        result.add(roomArray[floor][x][z]);
            }
        }

        return result;
    }

    public ArrayList<CastleRoom> getRoomList()
    {
        ArrayList<RoomSelection> rooms = getRoomSelectionList();
        ArrayList<CastleRoom> result = new ArrayList<>();

        for (RoomSelection rs: rooms)
        {
            result.add(rs.room);
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

    public ArrayList<CastleRoom> getAllRoomsOfType(CastleRoom.RoomType type, int floor)
    {
        ArrayList<CastleRoom> result = new ArrayList<>();
        for (int x = 0; x < roomArray[0].length; x++)
        {
            for (int z = 0; z < roomArray[0][0].length; z++)
            {
                if (roomArray[floor][x][z] != null && roomArray[floor][x][z].room.roomType == type)
                {
                    result.add(roomArray[floor][x][z].room);
                }
            }
        }

        return result;
    }

    private RoomSelection selectionFromGridPosition(GridPosition gridPos)
    {
        return roomArray[gridPos.floor][gridPos.x][gridPos.z];
    }

    public boolean withinGridBounds(int x, int z)
    {
        return (x >= 0 && x < sizeX && z >= 0 && z < sizeZ);
    }
}
