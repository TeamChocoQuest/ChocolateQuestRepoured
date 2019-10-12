package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.util.BlockPlacement;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.*;
import java.lang.Double;

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

        for (int floor = 0; floor < usedFloors; floor++)
        {
            boolean narrowFloor = grid.floorIsNarrow(floor);

            if (!narrowFloor)
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
        }

        addStairCases();

        ArrayList<RoomGridCell> unTyped = grid.getAllCellsWhere(c -> c.isSelectedForBuilding() &&
                                                                     !c.isPopulated());
        for (RoomGridCell selection : unTyped)
        {
            selection.setRoom(new CastleRoomKitchen(getRoomStart(selection), roomSize, floorHeight));
        }

        determineWalls();
        placeOuterDoors();
        placeTowers();
        connectRooms();
        determineRoofs();

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
                        grid.selectCellForBuilding(floorIndex, xIndex, zIndex);
                        grid.setRoomAsMainStruct(floorIndex, xIndex, zIndex);

                        if (Math.min(mainRoomsX, mainRoomsZ) == 1)
                        {
                            grid.setRoomAsNarrow(floorIndex, xIndex, zIndex);
                        }

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
            int sideRoomsX, sideRoomsZ, startX, startZ;

            if (openCellsWest > 0)
            {
                sideRoomsX = random.nextInt(openCellsWest + 1);
                sideRoomsZ = random.nextInt(mainRoomsZ + 1);
                if (Math.min(sideRoomsX, sideRoomsZ) >= 1)
                {
                    startX = minX + offsetX - sideRoomsX;
                    startZ = random.nextBoolean() ? minZ + offsetZ : minZ + offsetZ + mainRoomsZ - sideRoomsZ;
                    grid.selectBlockOfCellsForBuilding((layer * floorsPerLayer), floorsPerLayer, startX, sideRoomsX, startZ, sideRoomsZ);
                }
            }

            if (openCellsNorth > 0)
            {
                sideRoomsX = random.nextInt(mainRoomsX + 1);
                sideRoomsZ = random.nextInt(openCellsNorth + 1);
                if (Math.min(sideRoomsX, sideRoomsZ) >= 1)
                {
                    startX = random.nextBoolean() ? minX + offsetX : minX + offsetX + mainRoomsX - sideRoomsX;
                    startZ = minZ + offsetZ - sideRoomsZ;
                    grid.selectBlockOfCellsForBuilding((layer * floorsPerLayer), floorsPerLayer, startX, sideRoomsX, startZ, sideRoomsZ);
                }
            }

            if (openCellsEast > 0)
            {
                sideRoomsX = random.nextInt(openCellsEast + 1);
                sideRoomsZ = random.nextInt(mainRoomsZ + 1);
                if (Math.min(sideRoomsX, sideRoomsZ) >= 1)
                {
                    startX = minX + offsetX + mainRoomsX;
                    startZ = random.nextBoolean() ? minZ + offsetZ : minZ + offsetZ + mainRoomsZ - sideRoomsZ;
                    grid.selectBlockOfCellsForBuilding((layer * floorsPerLayer), floorsPerLayer, startX, sideRoomsX, startZ, sideRoomsZ);
                }
            }

            if (openCellsSouth > 0)
            {
                sideRoomsX = random.nextInt(mainRoomsX + 1);
                sideRoomsZ = random.nextInt(openCellsSouth + 1);
                if (Math.min(sideRoomsX, sideRoomsZ) >= 1)
                {
                    startX = random.nextBoolean() ? minX + offsetX : minX + offsetX + mainRoomsX - sideRoomsX;
                    startZ = minZ + offsetZ + mainRoomsZ;
                    grid.selectBlockOfCellsForBuilding((layer * floorsPerLayer), floorsPerLayer, startX, sideRoomsX, startZ, sideRoomsZ);

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

    private int randomSubsectionLength(int mainLength)
    {
        int rounding = (mainLength % 2 == 0) ? 0 : 1;
        int halfLen = mainLength / 2;
        return halfLen + random.nextInt(halfLen + rounding);
    }

    private void placeTowers()
    {
        for (int floor = 0; floor < usedFloors; floor += floorsPerLayer)
        {
            HashSet<EnumFacing> sidesToCheck = new HashSet<>();
            sidesToCheck.addAll(Arrays.asList(EnumFacing.HORIZONTALS));

            final int f = floor;
            ArrayList<RoomGridCell> candidateCells = grid.getAllCellsWhere(c -> c.getFloor() == f &&
                    c.isPopulated());
            Collections.shuffle(candidateCells); //make the list more random

            CellLoop:
            for (RoomGridCell cell : candidateCells)
            {
                for (EnumFacing side : sidesToCheck)
                {
                    boolean canBuild;

                    if (floor == 0)
                    {
                        canBuild = grid.cellIsOuterEdge(cell, side) && grid.getAdjacentCell(cell, side) != null;
                    }
                    else
                    {
                        canBuild = grid.adjacentCellIsWalkableRoof(cell, side) && !cell.getRoom().hasDoorOnSide(side);
                    }

                    if (canBuild)
                    {
                        int maxHeight = (usedFloors - floor) + 1;
                        if (maxHeight - 3 > 0)
                        {
                            int height = 3 + random.nextInt(maxHeight - 3);
                            addTower(cell.getGridPosition().move(side), height, side.getOpposite());
                            sidesToCheck.remove(side);
                            break CellLoop;
                        }
                    }
                }
            }
        }
    }
    private void addTower(RoomGridPosition position, int height, EnumFacing alignment)
    {
        int x = position.getX();
        int z = position.getZ();
        int startFloor = position.getFloor();

        int towerSize = 5 + random.nextInt(roomSize - 5);
        System.out.format("Placing tower at %d,%d on floor %d facing %s, size = %d\n",
                x, z, startFloor, alignment.toString(), towerSize);

        CastleRoomTowerSquare tower = null;
        for (int floor = startFloor; floor < startFloor + height; floor++)
        {
            RoomGridCell cell = grid.getCellAt(floor, x, z);
            if (cell == null)
            {
                System.out.println("Tried to place a tower @ null");
            }
            else
            {
                tower = new CastleRoomTowerSquare(getRoomStart(cell), roomSize, floorHeight, alignment, towerSize, tower);
                cell.setRoom(tower);
                cell.setReachable();

                RoomGridCell adjacent = grid.getAdjacentCell(cell, alignment);
                if (adjacent.isPopulated())
                {
                    adjacent.getRoom().addDoorOnSideCentered(alignment.getOpposite());
                }
                else
                {
                    cell.getRoom().addWall(alignment, false);
                    if (grid.adjacentCellIsWalkableRoof(cell, alignment))
                    {
                        cell.getRoom().addDoorOnSideCentered(alignment);
                    }
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
                    CastleRoomHallway.Alignment.VERTICAL));
            selection.setReachable();
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
                    CastleRoomHallway.Alignment.HORIZONTAL));
            selection.setReachable();
        }
    }

    private void placeOuterDoors()
    {
        for (int floor = 0; floor < usedFloors; floor += floorsPerLayer)
        {
            HashSet<EnumFacing> doorDirections = new HashSet<>(); //Sides of this floor that already have exits

            ArrayList<RoomGridCell> floorRooms = grid.getAllCellsWhere(r -> r.getFloor() == 0 && r.isPopulated());
            Collections.shuffle(floorRooms);

            for (RoomGridCell cell : floorRooms)
            {
                for (EnumFacing side : EnumFacing.HORIZONTALS)
                {
                    if (!doorDirections.contains(side) && cell.getRoom().canBuildDoorOnSide(side))
                    {
                        boolean buildExit;

                        if (floor == 0)
                        {
                            buildExit = !grid.adjacentCellIsPopulated(cell, side);
                        }
                        else
                        {
                            buildExit = grid.adjacentCellIsWalkableRoof(cell, side);
                        }

                        if (buildExit)
                        {
                            if (floor == 0) cell.setReachable();
                            doorDirections.add(side);
                            cell.getRoom().addDoorOnSideRandom(side);
                            break;
                        }
                    }
                }
            }
        }
    }

    private void addStairCases()
    {
        for (int floor = 0; floor < usedFloors; floor++)
        {
            final int f = floor; //lambda requires a final
            ArrayList<RoomGridCell> candidateCells;

            if (!grid.floorIsNarrow(floor + 1))
            {
                candidateCells = grid.getAllCellsWhere(r -> r.getFloor() == f &&
                                                       r.isSelectedForBuilding() &&
                                                       !r.isPopulated());
                Iterator<RoomGridCell> iter =  candidateCells.iterator();
                boolean built = false;

                while (iter.hasNext() && !built)
                {
                    built = buildDirectedStairsIfPossible(iter.next());
                }
            }
            else
            {
                candidateCells = grid.getAllCellsWhere(r -> r.getFloor() == f &&
                        r.isSelectedForBuilding());

                for (RoomGridCell cell : candidateCells)
                {
                    RoomGridCell aboveCell = grid.getAdjacentCell(cell, EnumFacing.UP);
                    if (aboveCell.isSelectedForBuilding() && !aboveCell.isPopulated())
                    {
                        CastleRoomStaircaseSpiral stairs = new CastleRoomStaircaseSpiral(getRoomStart(cell), roomSize, floorHeight);
                        cell.setRoom(stairs);

                        CastleRoomLandingSpiral landing = new CastleRoomLandingSpiral(getRoomStart(aboveCell), roomSize, floorHeight, stairs);
                        aboveCell.setRoom(landing);
                        aboveCell.setReachable();
                        break;
                    }
                }
            }
        }
    }

    private boolean buildDirectedStairsIfPossible(RoomGridCell cell)
    {
        EnumFacing side = getValidStairDoorSide(cell);
        if (side != EnumFacing.DOWN)
        {
            RoomGridCell aboveCell = grid.getAdjacentCell(cell, EnumFacing.UP);

            CastleRoomStaircaseDirected stairs = new CastleRoomStaircaseDirected(getRoomStart(cell), roomSize, floorHeight,
                    side);
            cell.setRoom(stairs);
            cell.getRoom().addDoorOnSideRandom(side);

            aboveCell.setRoom(new CastleRoomLandingDirected(getRoomStart(aboveCell), roomSize, floorHeight,
                    stairs));
            aboveCell.setReachable();
            aboveCell.getRoom().addDoorOnSideRandom(side);

            return true;
        }

        return false;
    }

    //Returns direction that the door can face on directed stairs, or DOWN if no valid direction
    private EnumFacing getValidStairDoorSide(RoomGridCell cell)
    {
        RoomGridCell aboveCell = grid.getAdjacentCell(cell, EnumFacing.UP);
        if (aboveCell != null && !aboveCell.isPopulated())
        {
            for (EnumFacing side : EnumFacing.HORIZONTALS)
            {
                if (grid.adjacentCellIsSelected(cell, side) &&
                    !grid.adjacentCellIsPopulated(cell, side) &&
                    !grid.cellBordersRoomType(cell, CastleRoom.RoomType.LANDING_DIRECTED) &&
                    grid.adjacentCellIsSelected(aboveCell, side) &&
                    !grid.adjacentCellIsPopulated(aboveCell, side))
                {
                    return side;
                }
            }
        }
        return EnumFacing.DOWN;
    }

    private class PathNode
    {
        private PathNode parent;
        private EnumFacing parentDirection;
        private RoomGridCell cell;
        private double f;
        private double g;
        private double h;

        private PathNode(PathNode parent, EnumFacing parentDirection, RoomGridCell cell, double g, double h)
        {
            this.parent = parent;
            this.parentDirection = parentDirection;
            this.cell = cell;
            this.g = g;
            this.h = h;
            this.f = g + h;
        }

        public RoomGridCell getCell()
        {
            return cell;
        }

        public PathNode getParent()
        {
            return parent;
        }

        public double getF()
        {
            return f;
        }

        public double getG()
        {
            return g;
        }

        public EnumFacing getParentDirection()
        {
            return parentDirection;
        }

        public void updateParent(PathNode parent)
        {
            this.parent = parent;
        }

        public void updateG(double g)
        {
            this.g = g;
            this.f = g + h;
        }
    }

    private void connectRooms()
    {
        System.out.println("Connecting rooms");
        for (int floor = 0; floor < maxFloors; floor++)
        {
            final int f = floor;
            ArrayList<RoomGridCell> unreachable = grid.getAllCellsWhere(c -> c.getFloor() == f && !c.isReachable() && c.isPopulated());
            ArrayList<RoomGridCell> reachable = grid.getAllCellsWhere(c -> c.getFloor() == f && c.isReachable() && c.isPopulated());

            while (!unreachable.isEmpty() && !reachable.isEmpty())
            {
                RoomGridCell srcRoom = unreachable.get(random.nextInt(unreachable.size()));

                //going for the nearest doesnt always make the most interesting layout, may want to add noise
                RoomGridCell destRoom = findNearestReachableRoom(srcRoom, reachable);

                LinkedList<PathNode> destToSrcPath = findPathBetweenRooms(srcRoom, destRoom);

                for (PathNode node : destToSrcPath)
                {
                    RoomGridCell cell = grid.getCellAt(node.getCell().getGridPosition());
                    if (cell != null)
                    {
                        if (node.getParent() != null)
                        {
                            if (cell.getRoom().hasWallOnSide(node.getParentDirection()))
                            {
                                cell.getRoom().addDoorOnSideRandom(node.getParentDirection());
                            } else
                            {
                                RoomGridCell parentCell = grid.getAdjacentCell(cell, node.getParentDirection());
                                if (parentCell != null)
                                {
                                    parentCell.getRoom().addDoorOnSideRandom(node.getParentDirection().getOpposite());
                                }
                            }
                        }
                        cell.setReachable();
                        unreachable.remove(cell);
                        reachable.add(cell);
                    }
                }
            }
        }
    }

    private LinkedList<PathNode> findPathBetweenRooms(RoomGridCell startCell, RoomGridCell endCell)
    {
        LinkedList<PathNode> open = new LinkedList<>();
        LinkedList<PathNode> closed = new LinkedList<>();
        LinkedList<PathNode> path = new LinkedList<>();

        open.add(new PathNode(null, EnumFacing.DOWN, startCell, 0, startCell.distanceTo(endCell)));

        while (!open.isEmpty())
        {
            open.sort(Comparator.comparingDouble(PathNode::getF)); //would be more efficient to sort this as we add
            PathNode currentNode = open.removeFirst();
            if (currentNode.getCell() == endCell)
            {
                while (currentNode != null)
                {
                    path.add(currentNode);
                    currentNode = currentNode.getParent();
                }
                break;
            }

            closed.add(currentNode);

            double neighborG = currentNode.getG() + 1;
            //add each neighbor node to closed list if it connectable and not closed already
            for (EnumFacing direction : EnumFacing.HORIZONTALS)
            {
                RoomGridCell neighborCell = grid.getAdjacentCell(currentNode.getCell(), direction);
                if (neighborCell != null && neighborCell.isPopulated()/* && neighborCell.getRoom().getRoomType() != CastleRoom.RoomType.STAIRCASE_DIRECTED*/)
                {
                    PathNode neighborNode = new PathNode(currentNode, direction.getOpposite(), neighborCell, neighborG, neighborCell.distanceTo(endCell));

                    //should really do this with .contains() but I don't feel like doing the overrides
                    boolean cellAlreadyClosed = nodeListContainsCell(closed, neighborCell);

                    if (!cellAlreadyClosed)
                    {
                        boolean cellAlreadyOpen = nodeListContainsCell(open, neighborCell);
                        if (cellAlreadyOpen)
                        {
                            PathNode openNode = getNodeThatContainsCell(open, neighborCell);
                            if (openNode.getG() > neighborG)
                            {
                                openNode.updateParent(currentNode);
                                openNode.updateG(neighborG);
                            }
                        }
                        else
                        {
                            open.add(neighborNode);
                        }
                    }
                }
            }
        }

        return path;
    }

    private boolean nodeListContainsCell(LinkedList<PathNode> nodeList, RoomGridCell cell)
    {
        return (getNodeThatContainsCell(nodeList, cell) != null);
    }

    private PathNode getNodeThatContainsCell(LinkedList<PathNode> nodeList, RoomGridCell cell)
    {
        if (!nodeList.isEmpty())
        {
            for (PathNode n : nodeList)
            {
                if (n.getCell() == cell)
                {
                    return n;
                }
            }
        }

        return null;
    }

    private RoomGridCell findNearestReachableRoom(RoomGridCell origin, ArrayList<RoomGridCell> floorRooms)
    {
        if (!floorRooms.isEmpty())
        {
            floorRooms.sort((RoomGridCell c1, RoomGridCell c2) ->
                    Double.compare(grid.distanceBetweenCells2D(origin, c1), (grid.distanceBetweenCells2D(origin, c2))));

            return floorRooms.get(0);
        }
        else
        {
            return null;
        }
    }

    private void determineWalls()
    {
        ArrayList<RoomGridCell> cells = grid.getAllCellsWhere(c -> c.isPopulated() && !c.getRoom().isTower());
        for (RoomGridCell cell : cells)
        {
            //If we are at the edge cells, we force adding the walls. Otherwise we don't force
            //it so rooms like hallways don't add them by mistake.
            boolean forceSouth = !grid.adjacentCellIsPopulated(cell, EnumFacing.SOUTH);
            cell.getRoom().addWall(EnumFacing.SOUTH, forceSouth);

            boolean forceEast = !grid.adjacentCellIsPopulated(cell, EnumFacing.EAST);
            cell.getRoom().addWall(EnumFacing.EAST, forceEast);

            if (!grid.adjacentCellIsPopulated(cell, EnumFacing.NORTH))
            {
                cell.getRoom().addWall(EnumFacing.NORTH, true);
            }

            if (!grid.adjacentCellIsPopulated(cell, EnumFacing.WEST))
            {
                cell.getRoom().addWall(EnumFacing.WEST, true);
            }
        }

    }

    private void determineRoofs()
    {
        for (RoomGridCell cell : grid.getSelectionListCopy())
        {
            if (cell.isPopulated() && !grid.adjacentCellIsPopulated(cell, EnumFacing.UP))
            {
                for (EnumFacing side : EnumFacing.HORIZONTALS)
                {
                    addRoofEdgeIfRequired(cell, side);
                }
            }
        }
    }

    private void addRoofEdgeIfRequired(RoomGridCell cell, EnumFacing side)
    {
        RoomGridCell adjacent = grid.getAdjacentCell(cell, side);

        if (adjacent == null || !adjacent.isPopulated() || adjacent.getRoom().isTower())
        {
            cell.getRoom().addRoofEdge(side);
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
            rooms.get(random.nextInt(rooms.size())).getRoom().addDoorOnSideRandom(side);
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
        CastleRoom room = new CastleRoomHallway(getRoomStart(floor, x, z), roomSize, floorHeight, alignment);
        grid.addRoomAt(room, floor, x, z);
    }

    private int getLayerFromFloor(int floor)
    {
        return floor / floorsPerLayer;
    }
}