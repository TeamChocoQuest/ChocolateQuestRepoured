package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import net.minecraft.util.EnumFacing;

import java.util.*;
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
        ArrayList<RoomGridCell> rooms = getCellListCopy();
        ArrayList<CastleRoom> result = new ArrayList<>();

        for (RoomGridCell rs: rooms)
        {
            result.add(rs.getRoom());
        }

        return result;
    }

    public ArrayList<RoomGridCell> getCellListCopy()
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
        return getCellListCopy().stream().filter(p).collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<RoomGridCell> getSelectedCellsInColumn(int floor, int columnIndex)
    {
        ArrayList<RoomGridCell> result = getCellListCopy();
        result.removeIf(r -> r.getFloor() != floor);
        result.removeIf(r -> r.getGridX() != columnIndex);
        result.removeIf(r -> !r.isSelectedForBuilding());
        result.removeIf(r -> !r.isPopulated());
        return result;
    }

    public ArrayList<RoomGridCell> getSelectedCellsInRow(int floor, int rowIndex)
    {
        ArrayList<RoomGridCell> result = getCellListCopy();
        result.removeIf(r -> r.getFloor() != floor);
        result.removeIf(r -> r.getGridZ() != rowIndex);
        result.removeIf(r -> !r.isSelectedForBuilding());
        result.removeIf(r -> !r.isPopulated());
        return result;
    }

    public ArrayList<RoomGridCell> getSelectedMainStructCells(int floor)
    {
        ArrayList<RoomGridCell> result = getCellListCopy();
        result.removeIf(r -> r.getFloor() != floor);
        result.removeIf(r -> !r.isSelectedForBuilding());
        result.removeIf(r -> !r.isMainStruct());
        return result;
    }

    public ArrayList<RoomGridCell> getCellsWithoutAType()
    {
        ArrayList<RoomGridCell> result = getCellListCopy();
        result.removeIf(r -> !r.needsRoomType());
        return result;
    }

    public ArrayList<CastleRoom> getRooms()
    {
        ArrayList<RoomGridCell> populatedRooms = getCellListCopy();
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

    public int getMinBuildableXOnFloor(int floor)
    {
        Optional<RoomGridCell> result = getCellListCopy().stream()
                .filter(r -> r.getFloor() == floor)
                .filter(r -> r.isBuildable())
                .min(Comparator.comparingInt(RoomGridCell::getGridX));
        if (result.isPresent())
        {
            return result.get().getGridX();
        }
        else
        {
            return 0;
        }
    }

    public int getMaxBuildableXOnFloor(int floor)
    {
        Optional<RoomGridCell> result = getCellListCopy().stream()
                .filter(r -> r.getFloor() == floor)
                .filter(r -> r.isBuildable())
                .max(Comparator.comparingInt(RoomGridCell::getGridX));
        if (result.isPresent())
        {
            return result.get().getGridX();
        }
        else
        {
            return 0;
        }
    }

    public int getMinBuildableZOnFloor(int floor)
    {
        Optional<RoomGridCell> result = getCellListCopy().stream()
                .filter(r -> r.getFloor() == floor)
                .filter(r -> r.isBuildable())
                .min(Comparator.comparingInt(RoomGridCell::getGridZ));
        if (result.isPresent())
        {
            return result.get().getGridZ();
        }
        else
        {
            return 0;
        }
    }

    public int getMaxBuildableZOnFloor(int floor)
    {
        Optional<RoomGridCell> result = getCellListCopy().stream()
                .filter(r -> r.getFloor() == floor)
                .filter(r -> r.isBuildable())
                .max(Comparator.comparingInt(RoomGridCell::getGridZ));
        if (result.isPresent())
        {
            return result.get().getGridZ();
        }
        else
        {
            return 0;
        }
    }

    public void setCellBuilable(int floor, int x, int z)
    {
        roomArray[floor][x][z].setBuildable();
    }

    public void selectCellForBuilding(int floor, int x, int z)
    {
        roomArray[floor][x][z].selectForBuilding();
    }

    public void setRoomAsMainStruct(int floor, int x, int z)
    {
        roomArray[floor][x][z].setAsMainStruct();
    }

    public void setRoomAsNarrow(int floor, int x, int z)
    {
        roomArray[floor][x][z].setNarrow();
    }

    public boolean floorIsNarrow(final int floor)
    {
        return getAllCellsWhere(c -> c.isSelectedForBuilding() &&
                c.getFloor() == floor &&
                c.isNarrow()).size() > 0;
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

    public RoomGridCell getCellAt(int floor, int x, int z)
    {
        if (withinGridBounds(floor, x, z))
        {
            return roomArray[floor][x][z];
        }
        else
        {
            return null;
        }
    }

    public RoomGridCell getCellAt(RoomGridPosition position)
    {
        if (withinGridBounds(position.getFloor(), position.getX(), position.getZ()))
        {
            return roomArray[position.getFloor()][position.getX()][position.getZ()];
        }
        else
        {
            return null;
        }
    }

    public void selectBlockOfCellsForBuilding(int startFloor, int floors, int startX, int lenX, int startZ, int lenZ)
    {
        for (int floor = startFloor; floor < startFloor + floors; floor++)
        {
            for (int x = startX; x < startX + lenX; x++)
            {
                for (int z = startZ; z < startZ + lenZ; z++)
                {
                    if (withinGridBounds(floor, x, z))
                    {
                        selectCellForBuilding(floor, x, z);
                    }
                }
            }
        }
    }

    public boolean adjacentCellIsPopulated(RoomGridCell startCell, EnumFacing direction)
    {
        RoomGridCell adjacent = getAdjacentCell(startCell, direction);
        return (adjacent != null && adjacent.isPopulated());
    }

    public boolean adjacentCellIsFullRoom(RoomGridCell startCell, EnumFacing direction)
    {
        RoomGridCell adjacent = getAdjacentCell(startCell, direction);
        return (adjacent != null && adjacent.isPopulated() && !(adjacent.getRoom() instanceof CastleRoomWalkableRoof));
    }

    public boolean adjacentCellIsSelected(RoomGridCell startCell, EnumFacing direction)
    {
        RoomGridCell adjacent = getAdjacentCell(startCell, direction);
        return (adjacent != null && adjacent.isSelectedForBuilding());
    }

    public boolean adjacentCellIsWalkableRoof(RoomGridCell startCell, EnumFacing direction)
    {
        RoomGridCell adjacent = getAdjacentCell(startCell, direction);
        return (adjacent != null && adjacent.isPopulated() && adjacent.getRoom() instanceof CastleRoomWalkableRoof);
    }

    public boolean cellIsOuterEdge(RoomGridCell cell, EnumFacing direction)
    {
        RoomGridPosition coords = cell.getGridPosition();

        coords = coords.move(direction);
        while(withinGridBounds(coords))
        {
            if (getCellAt(coords).isPopulated())
            {
                return false;
            }
            coords = coords.move(direction);
        }

        return true;
    }

    /*
    * Determine if a tower can be attached next to the given cell
     */
    public boolean canAttachTower(RoomGridCell cell, EnumFacing side)
    {
        RoomGridCell adjacent = getAdjacentCell(cell, side);

        return (!cell.getRoom().isTower() &&
                !cell.getRoom().hasDoorOnSide(side) &&
                adjacent != null &&
                !(adjacent.isPopulated() &&
                 !cell.getRoom().isStairsOrLanding()));
    }

    public double distanceBetweenCells2D(RoomGridCell c1, RoomGridCell c2)
    {
        int distX = Math.abs(c1.getGridX() - c2.getGridX());
        int distZ = Math.abs(c1.getGridZ() - c2.getGridZ());
        return (Math.hypot(distX, distZ));
    }

    public boolean cellBordersRoomType(RoomGridCell cell, EnumRoomType type)
    {
        for (EnumFacing side : EnumFacing.HORIZONTALS)
        {
            RoomGridCell adjacent = getAdjacentCell(cell, side);
            if (adjacent != null && adjacent.isPopulated() && adjacent.getRoom().getRoomType() == type)
            {
                return true;
            }
        }

        return false;
    }

    public RoomGridCell getAdjacentCell(RoomGridCell startCell, EnumFacing direction)
    {
        RoomGridPosition startPosition = startCell.getGridPosition();
        int floor = startPosition.getFloor();
        int x = startPosition.getX();
        int z = startPosition.getZ();

        switch (direction)
        {
            case UP:
                floor += 1;
                break;
            case DOWN:
                floor -= 1;
                break;
            case NORTH:
                z -= 1;
                break;
            case SOUTH:
                z += 1;
                break;
            case WEST:
                x -= 1;
                break;
            case EAST:
                x += 1;
                break;
            default:
                break;
        }

        if (withinGridBounds(floor, x, z))
        {
            return roomArray[floor][x][z];
        }
        else
        {
            return null;
        }
    }

    public boolean withinGridBounds(int floor, int x, int z)
    {
        return (floor >= 0 && floor < floors && withinFloorBounds(x, z));
    }

    public boolean withinGridBounds(RoomGridPosition position)
    {
        return (position.getFloor() >= 0 && position.getFloor() < floors &&
                withinFloorBounds(position.getX(), position.getZ()));
    }

    public boolean withinFloorBounds(int x, int z)
    {
        return (x >= 0 && x < roomsX && z >= 0 && z < roomsZ);
    }

}
