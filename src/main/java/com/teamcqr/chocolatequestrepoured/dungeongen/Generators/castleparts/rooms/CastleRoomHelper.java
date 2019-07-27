package com.teamcqr.chocolatequestrepoured.dungeongen.Generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.util.BlockPlacement;
import net.minecraft.util.math.BlockPos;

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
    private CastleRoom[][][] roomArray; //array to track which room goes where
    private boolean[][][] filled; //array to track if a room has been assigned
    private Random random;

    public enum CastleRoomType
    {
        HALLWAY,
        KITCHEN,
        BEDROOM,
        LIBRARY
    }

    public CastleRoomHelper(BlockPos startPos, int roomSize, int floorHeight, int numFloors, int numRoomsX, int numRoomsZ, Random random)
    {
        this.startPos = startPos;
        this.floorHeight = floorHeight;
        this.roomSize = roomSize;
        this.numFloors = numFloors;
        this.numRoomsX = numRoomsX;
        this.numRoomsZ = numRoomsZ;
        this.roomArray = new CastleRoom[numFloors][numRoomsX][numRoomsZ];
        this.filled = new boolean[numFloors][numRoomsX][numRoomsZ];
        this.random = random;
    }

    public void fillRooms()
    {
        for (int floor = 0; floor < numFloors; floor++)
        {
            buildFloorHallway(floor);
        }

        for (int floor = 0; floor < numFloors; floor++)
        {
            for (int z = 0; z < numRoomsZ; z++)
            {
                for (int x = 0; x < numRoomsX; x++)
                {
                    if (!filled[floor][x][z])
                    {
                        roomArray[floor][x][z] = new CastleRoomKitchen(getRoomStart(floor, x, z), roomSize, floorHeight, getPositionFromIndex(x, z));
                        filled[floor][x][z] = true;
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
            } while (filled[floor][hallStartX][0]);

            for (int z = 0; z < numRoomsZ; z++)
            {
                roomArray[floor][hallStartX][z] = new CastleRoomHallway(getRoomStart(floor, hallStartX, z), roomSize, floorHeight, getPositionFromIndex(hallStartX, z), true);
                filled[floor][hallStartX][z] = true;
            }
        } else
        {
            int hallStartZ = 0;
            //TODO: Try the other side if this side filled already
            do
            {
                hallStartZ = random.nextInt(numRoomsZ);
            } while (filled[floor][0][hallStartZ]);

            for (int x = 0; x < numRoomsX; x++)
            {
                roomArray[floor][x][hallStartZ] = new CastleRoomHallway(getRoomStart(floor, x, hallStartZ), roomSize, floorHeight, getPositionFromIndex(x, hallStartZ), false);
                filled[floor][x][hallStartZ] = true;
            }
        }

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

    private ArrayList<CastleRoom> getRoomList()
    {
        ArrayList<CastleRoom> result = new ArrayList<>();
        for (int floor = 0; floor < numFloors; floor++)
        {
            for (int z = 0; z < numRoomsZ; z++)
            {
                for (int x = 0; x < numRoomsX; x++)
                {
                    result.add(roomArray[floor][x][z]);
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
                    if (filled[floor][x][z] && roomArray[floor][x][z] != null)
                    {
                        result += "[" + roomArray[floor][x][z].getNameShortened() + "|" + roomArray[floor][x][z].getPositionString() + "] ";
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
