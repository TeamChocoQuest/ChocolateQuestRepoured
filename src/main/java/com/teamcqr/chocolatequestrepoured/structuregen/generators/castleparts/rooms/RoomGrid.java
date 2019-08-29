package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class RoomGrid
{
    private static final int MAX_lAYERS = 5;

    private int floors;
    private int roomsX;
    private int roomsZ;
    private Random random;
    private RoomGridCell[][][] roomArray;
    private List<RoomGridCell> roomList;

    public RoomGrid(int floors, int roomsX, int roomsZ, Random random)
    {
        this.floors = floors;
        this.roomsX = roomsX;
        this.roomsZ = roomsZ;
        this.random = random;
        this.roomArray = new RoomGridCell[floors][roomsX][roomsZ];
        this.roomList = new ArrayList<>();

        //initialize the room grid
        for (int floor = 0; floor < floors; floor++)
        {
            for (int x = 0; x < roomsX; x++)
            {
                for (int z = 0; z < roomsZ; z++)
                {
                    this.roomArray[floor][x][z] = new RoomGridCell(floor, x, z, null);
                    roomList.add(this.roomArray[floor][x][z]);
                }
            }
        }
    }

    public int getRandomXOffsetForRooms(int numRooms)
    {
        if (numRooms <= roomsX)
        {
            int maxOffset = roomsX - numRooms;
            return random.nextInt(maxOffset + 1);
        }
        return -1;
    }

    public int getRandomZOffsetForRooms(int numRooms)
    {
        if (numRooms <= roomsZ)
        {
            int maxOffset = roomsZ - numRooms;
            return random.nextInt(maxOffset + 1);
        }
        return -1;
    }

    public void setRoomReachable(int floor, int x, int z)
    {
        roomArray[floor][x][z].setReachable();
    }

    public ArrayList<CastleRoom> getRoomList()
    {
        ArrayList<RoomGridCell> rooms = getSelectionListCopy();
        ArrayList<CastleRoom> result = new ArrayList<>();

        for (RoomGridCell rs: rooms)
        {
            result.add(rs.getRoom());
        }

        return result;
    }

    public ArrayList<RoomGridCell> getSelectionListCopy()
    {
        return new ArrayList<RoomGridCell>(roomList);
    }

    public ArrayList<RoomGridCell> getUnreachableRoomList(int floor)
    {
        ArrayList<RoomGridCell> result = new ArrayList<>();
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

    //TODO: All of these list getters could (maybe?) be done faster as stream filters

    public ArrayList<RoomGridCell> getAllCellsWhere(Predicate<RoomGridCell> p)
    {
        return getSelectionListCopy().stream().filter(p).collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<RoomGridCell> getSelectedCellsInColumn(int floor, int columnIndex)
    {
        ArrayList<RoomGridCell> result = getSelectionListCopy();
        result.removeIf(r -> r.getFloor() != floor);
        result.removeIf(r -> r.getGridX() != columnIndex);
        result.removeIf(r -> !r.isSelectedForBuilding());
        return result;
    }

    public ArrayList<RoomGridCell> getSelectedCellsInRow(int floor, int rowIndex)
    {
        ArrayList<RoomGridCell> result = getSelectionListCopy();
        result.removeIf(r -> r.getFloor() != floor);
        result.removeIf(r -> r.getGridZ() != rowIndex);
        result.removeIf(r -> !r.isSelectedForBuilding());
        return result;
    }

    public ArrayList<RoomGridCell> getSelectedMainStructCells(int floor)
    {
        ArrayList<RoomGridCell> result = getSelectionListCopy();
        result.removeIf(r -> r.getFloor() != floor);
        result.removeIf(r -> !r.isSelectedForBuilding());
        result.removeIf(r -> !r.isMainStruct());
        return result;
    }

    public ArrayList<RoomGridCell> getCellsWithoutAType()
    {
        ArrayList<RoomGridCell> result = getSelectionListCopy();
        result.removeIf(r -> !r.needsRoomType());
        return result;
    }

    public ArrayList<CastleRoom> getRooms()
    {
        ArrayList<RoomGridCell> populatedRooms = getSelectionListCopy();
        ArrayList<CastleRoom> result = new ArrayList<>();
        for (RoomGridCell cell : populatedRooms)
        {
            if (cell.isPopulated())
            {
                result.add(cell.getRoom());
            }
        }
        return result;
    }

    public void setCellBuilable(int floor, int x, int z)
    {
        roomArray[floor][x][z].setBuildable();
    }

    public void selectRoomForBuilding(int floor, int x, int z)
    {
        roomArray[floor][x][z].selectForBuilding();
    }

    public void setRoomAsMainStruct(int floor, int x, int z)
    {
        roomArray[floor][x][z].setAsMainStruct();
    }

    public boolean isRoomFilled(int floor, int x, int z)
    {
        return roomArray[floor][x][z] != null && roomArray[floor][x][z].getRoom() != null;
    }

    public boolean isRoomReachable(int floor, int x, int z)
    {
        return roomArray[floor][x][z].isReachable();
    }

    public boolean isRoomPopulated(int floor, int x, int z)
    {
        return roomArray[floor][x][z].isPopulated();
    }

    public void addRoomAt(CastleRoom room, int floor, int x, int z)
    {
        roomArray[floor][x][z].setRoom(room);
    }


    public CastleRoom getRoomAt(int floor, int x, int z)
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

    public boolean withinGridBounds(int x, int z)
    {
        return (x >= 0 && x < roomsX && z >= 0 && z < roomsZ);
    }

    //print the room array in a grid, floor by floor
    public String printGrid()
    {
        String result = "";
        for (int floor = 0; floor < floors; floor++)
        {
            result += "\nFloor " + floor + "\n";
            for (int z = 0; z < roomsZ; z++)
            {
                for (int x = 0; x < roomsX; x++)
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
