package com.teamcqr.chocolatequestrepoured.dungeongen.Generators.castleparts.rooms;

import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class CastleRoomGrid
{
    private RoomSelection[][][] roomArray;
    private int floors;
    private int sizeX;
    private int sizeZ;
    private Random random;

    public class RoomSelection
    {
        private CastleRoom room;
        GridPosition gridLocation;
        private boolean reachable;

        private RoomSelection(CastleRoom room, GridPosition gridLocation)
        {
            this.room = room;
            this.gridLocation = gridLocation;
            this.reachable = false;
        }
    }

    public class GridPosition
    {
        int floor;
        int x;
        int z;

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

    private EnumFacing getDirectionOfNearestReachable(GridPosition gridLocation)
    {
        EnumFacing result = EnumFacing.DOWN;

        class DirectionDistance
        {
            EnumFacing direction;
            int distance;

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

    public void addRoomAt(CastleRoom room, int floor, int x, int z)
    {
        roomArray[floor][x][z] = new RoomSelection(room, new GridPosition(floor, x, z));
    }

    public void addRoomAt(CastleRoom room, GridPosition gridPos)
    {
        roomArray[gridPos.floor][gridPos.x][gridPos.z] = new RoomSelection(room, gridPos);
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

    public void setRoomReachable(int floor, int x, int z)
    {
        roomArray[floor][x][z].reachable = true;
    }

    private ArrayList<RoomSelection> getUnreachableRoomList()
    {
        ArrayList<RoomSelection> result = new ArrayList<>();
        for (int floor = 0; floor < roomArray.length; floor++)
        {
            for (int x = 0; x < roomArray[0].length; x++)
            {
                for (int z = 0; z < roomArray[0][0].length; z++)
                {
                    if (roomArray[floor][x][z] != null && !roomArray[floor][x][z].reachable)
                        result.add(roomArray[floor][x][z]);
                }
            }
        }

        return result;
    }

    public ArrayList<CastleRoom> getRoomList()
    {
        ArrayList<CastleRoom> result = new ArrayList<>();
        for (int floor = 0; floor < roomArray.length; floor++)
        {
            for (int x = 0; x < roomArray[0].length; x++)
            {
                for (int z = 0; z < roomArray[0][0].length; z++)
                {
                    if (roomArray[floor][x][z] != null)
                    {
                        result.add(roomArray[floor][x][z].room);
                    }
                }
            }
        }

        return result;
    }

    private RoomSelection selectionFromGridPosition(GridPosition gridPos)
    {
        return roomArray[gridPos.floor][gridPos.x][gridPos.z];
    }

}
