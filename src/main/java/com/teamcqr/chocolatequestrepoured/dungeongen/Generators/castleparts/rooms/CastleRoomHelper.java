package com.teamcqr.chocolatequestrepoured.dungeongen.Generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.util.BlockPlacement;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.structure.StructureMineshaftPieces;

import java.util.ArrayList;
import java.util.Random;

public class CastleRoomHelper
{
    private BlockPos startPos;
    private int floorHeight;
    private int roomSize;
    private int numFloors;
    private int numRoomsX;
    private int numRoomsZ;
    private RoomGrid roomGrid;
    private Random random;

    //helper class to track the rooms in a 3D grid
    private class RoomGrid
    {
        private CastleRoom[][][] roomArray; //array to track which room goes where
        private boolean [][][] filled; //array to track if a room has been assigned

        private RoomGrid(int numFloors, int numRoomsX, int numRoomsZ)
        {
            this.roomArray = new CastleRoom[numFloors][numRoomsX][numRoomsZ];
            this.filled = new boolean[numFloors][numRoomsX][numRoomsZ];
        }

        private void addRoomAt(CastleRoom room, int floor, int x, int z)
        {
            roomArray[floor][x][z] = room;
            filled[floor][x][z] = true;
        }

        private CastleRoom getRoomAt(int floor, int x, int z)
        {
            if (filled[floor][x][z])
            {
                return (roomArray[floor][x][z]);
            }
            else
            {
                return null;
            }
        }

        private boolean isRoomFilled(int floor, int x, int z)
        {
            return filled[floor][x][z] && (roomArray[floor][x][z] != null);
        }
    }

    public CastleRoomHelper(BlockPos startPos, int roomSize, int floorHeight, int numFloors, int numRoomsX, int numRoomsZ, Random random)
    {
        this.startPos = startPos;
        this.floorHeight = floorHeight;
        this.roomSize = roomSize;
        this.numFloors = numFloors;
        this.numRoomsX = numRoomsX;
        this.numRoomsZ = numRoomsZ;
        this.random = random;
        this.roomGrid = new RoomGrid(numFloors, numRoomsX, numRoomsZ);
    }

    public void fillRooms()
    {
        for (int floor = 0; floor < numFloors; floor++)
        {
            buildFloorHallway(floor);
        }

        addStairCases();

        for (int floor = 0; floor < numFloors; floor++)
        {
            for (int z = 0; z < numRoomsZ; z++)
            {
                for (int x = 0; x < numRoomsX; x++)
                {
                    if (!roomGrid.isRoomFilled(floor, x, z))
                    {
                        roomGrid.addRoomAt(new CastleRoomKitchen(getRoomStart(floor, x, z), roomSize, floorHeight, getPositionFromIndex(x, z)), floor, x, z);
                    }
                }
            }
        }

    }

    public void generateRooms(ArrayList<BlockPlacement> blocks)
    {
        for (CastleRoom room : getRoomList())
        {
            room.generate(blocks);
        }
    }

    private void buildFloorHallway(int floor)
    {
        //fill the hallway - each floor must have at least 1 hallway
        boolean vertical = random.nextBoolean();

        //start at a random z index that isn't already filled
        if (vertical)
        {
            int hallStartX = 0;
            //TODO: Try the other side if this side filled already
            do
            {
                hallStartX = random.nextInt(numRoomsX);
            } while (roomGrid.isRoomFilled(floor, hallStartX,0));

            for (int z = 0; z < numRoomsZ; z++)
            {
                addRoomHallway(floor, hallStartX, z, true);
            }
        } else
        {
            int hallStartZ = 0;
            //TODO: Try the other side if this side filled already
            do
            {
                hallStartZ = random.nextInt(numRoomsZ);
            } while (roomGrid.isRoomFilled(floor, 0, hallStartZ));

            for (int x = 0; x < numRoomsX; x++)
            {
                addRoomHallway(floor, x, hallStartZ, false);
            }
        }
    }

    private void addStairCases()
    {
        //only iterate through floors thar aren't the top floor
        for (int floor = 0; floor < numFloors - 1; floor++)
        {
            for (int z = 0; z < numRoomsZ; z++)
            {
                for (int x = 0; x < numRoomsX; x++)
                {
                    if (!roomGrid.isRoomFilled(floor, x, z) &&
                            roomIsNextToHallway(floor, x, z) &&
                            roomIsNextToHallway(floor + 1, x, z))
                    {
                        addRoomStaircase(floor, x, z);
                        addRoomLanding(floor, x, z);
                    }
                }
            }
        }
    }

    private boolean roomIsNextToHallway(int floor, int x, int z)
    {
        CastleRoom neighborRoom;
        if (x != 0)
        {
            neighborRoom = roomGrid.getRoomAt(floor, x - 1, z);
            if (neighborRoom != null && neighborRoom.roomType == CastleRoom.RoomType.HALLWAY)
            {
                return true;
            }
        }
        if (z != 0)
        {
            neighborRoom = roomGrid.getRoomAt(floor, x, z - 1);
            if (x != 0 && neighborRoom != null && neighborRoom.roomType == CastleRoom.RoomType.HALLWAY)
            {
                return true;
            }
        }
        if (x < numRoomsX - 1)
        {
            neighborRoom = roomGrid.getRoomAt(floor, x + 1, z);
            if (neighborRoom != null && neighborRoom.roomType == CastleRoom.RoomType.HALLWAY)
            {
                return true;
            }
        }
        if (z < numRoomsZ - 1)
        {
            neighborRoom = roomGrid.getRoomAt(floor, x, z + 1);
            if (x != 0 && neighborRoom != null && neighborRoom.roomType == CastleRoom.RoomType.HALLWAY)
            {
                return true;
            }
        }
        return false;
    }

    public int getFloorCount()
    {
        return numFloors;
    }

    public int getRoomCountX()
    {
        return numRoomsX;
    }

    public int getRoomCountZ()
    {
        return numRoomsZ;
    }

    //Translate the room's (x, z) position in the floor array to a RoomPosition enum
    private CastleRoom.RoomPosition getPositionFromIndex(int x, int z)
    {
        if (x == 0 && z == 0)
        {
            return CastleRoom.RoomPosition.TOP_LEFT;
        }
        else if (x == 0 && z < numRoomsZ - 1)
        {
            return CastleRoom.RoomPosition.MID_LEFT;
        }
        else if (x == 0)
        {
            return CastleRoom.RoomPosition.BOT_LEFT;
        }
        else if (x < numRoomsX - 1 && z == 0)
        {
            return CastleRoom.RoomPosition.TOP_MID;
        }
        else if (x < numRoomsX - 1  && z < numRoomsZ - 1)
        {
            return CastleRoom.RoomPosition.MID;
        }
        else if (x < numRoomsX - 1 )
        {
            return CastleRoom.RoomPosition.BOT_MID;
        }
        else if (z == 0)
        {
            return CastleRoom.RoomPosition.TOP_RIGHT;
        }
        else if (z < numRoomsZ - 1)
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
        roomGrid.addRoomAt(room, floor, x, z);
    }

    private void addRoomStaircase(int floor, int x, int z)
    {
        CastleRoom room = new CastleRoomStaircase(getRoomStart(floor, x, z), roomSize, floorHeight, getPositionFromIndex(x, z));
        roomGrid.addRoomAt(room, floor, x, z);
    }

    private void addRoomLanding(int floor, int x, int z)
    {
        CastleRoom room = new CastleRoomLanding(getRoomStart(floor, x, z), roomSize, floorHeight, getPositionFromIndex(x, z));
        roomGrid.addRoomAt(room, floor, x, z);
    }

    private void addRoomUndirected(CastleRoom.RoomType type, int floor, int x, int z)
    {
        CastleRoom room;
        switch (type)
        {
            case KITCHEN:
                room = new CastleRoomKitchen(getRoomStart(floor, x, z), roomSize, floorHeight, getPositionFromIndex(x, z));
                roomGrid.addRoomAt(room, floor, x, z);
                break;
            default:
                break;
        }
    }

    private ArrayList<CastleRoom> getRoomList()
    {
        ArrayList<CastleRoom> result = new ArrayList<>();
        for (int floor = 0; floor < numFloors; floor++)
        {
            for (int z = 0; z < numRoomsZ; z++)
            {
                for (int x = 0; x < numRoomsX; x++)
                {
                    result.add(roomGrid.getRoomAt(floor,x, z));
                }
            }
        }

        return result;
    }

    //print the room array in a grid, floor by floor
    @Override
    public String toString()
    {
        String result = "";
        for (int floor = 0; floor < numFloors; floor++)
        {
            result += "\nFloor " + floor + "\n";
            for (int z = 0; z < numRoomsZ; z++)
            {
                for (int x = 0; x < numRoomsX; x++)
                {
                    if (roomGrid.isRoomFilled(floor, x, z))
                    {
                        result += "[" + roomGrid.getRoomAt(floor, x, z).getNameShortened() + "|" + roomGrid.getRoomAt(floor, x, z).getPositionString() + "] ";
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
