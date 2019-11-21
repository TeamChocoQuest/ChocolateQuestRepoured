package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.addons.CastleAddonRoof;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.segments.DoorPlacement;
import com.teamcqr.chocolatequestrepoured.util.WeightedRandom;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;
import java.lang.Double;

public class CastleRoomSelector
{
    private class RoofArea
    {
        int gridStartX;
        int lengthX;
        int gridStartZ;
        int lengthZ;
        int floor;

        private RoofArea(int gridStartX, int lengthX, int gridStartZ, int lengthZ, int floor)
        {
            this.gridStartX = gridStartX;
            this.lengthX = lengthX;
            this.gridStartZ = gridStartZ;
            this.lengthZ = lengthZ;
            this.floor = floor;
        }
    }

    private static final int MAX_LAYERS = 5;
    private static final int PADDING_FLOORS = 2;
    private static final int MIN_TOWER_FLOORS = 3;
    private static final int MIN_TOWER_SIZE = 7; //needs to have room for spiral stairs

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
    private List<RoofArea> roofAreas;
    private List<CastleAddonRoof> potentialRoofs;
    private WeightedRandom<EnumRoomType> roomRandomizer;

    public CastleRoomSelector(BlockPos startPos, int roomSize, int floorHeight, int floorsPerLayer,
                              int numSlotsX, int numSlotsZ, Random random)
    {
        this.startPos = startPos;
        this.floorHeight = floorHeight;
        this.roomSize = roomSize;
        this.floorsPerLayer = floorsPerLayer;
        this.maxFloors = floorsPerLayer * MAX_LAYERS;
        this.numSlotsX = numSlotsX;
        this.numSlotsZ = numSlotsZ;
        this.random = random;
        this.potentialRoofs = new ArrayList<>();
        roofAreas = new ArrayList<>();

        //Add padding floors so that we can build walkable roofs on top of the highest rooms
        this.grid = new RoomGrid(maxFloors + PADDING_FLOORS, numSlotsX, numSlotsZ, random);

        this.roomRandomizer = new WeightedRandom<EnumRoomType>(random);

        this.roomRandomizer.add(EnumRoomType.KITCHEN, 2);
        this.roomRandomizer.add(EnumRoomType.ALCHEMY_LAB, 2);
        this.roomRandomizer.add(EnumRoomType.ARMORY, 2);
        this.roomRandomizer.add(EnumRoomType.BEDROOM, 2);
    }

    public void generateRooms(World world, CastleDungeon dungeon)
    {
        ArrayList<RoomGridCell> populated = grid.getAllCellsWhere(RoomGridCell::isPopulated);

        for (CastleAddonRoof roof : potentialRoofs)
        {
            roof.generate(world, dungeon);
        }

        for (RoomGridCell cell : populated)
        {
            cell.getRoom().generate(world, dungeon);
        }

        //The rooms MUST be generated before they are decorated
        //Some decoration requires that neighboring rooms have their walls/doors
        for (RoomGridCell cell : populated)
        {
            cell.getRoom().decorate(world, dungeon);
        }
    }

    public void randomizeCastle()
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

        randomizeRooms();
        determineRoofs();

        determineWalls();
        placeOuterDoors();
        placeTowers();
        connectRooms();

        //System.out.println(grid.printGrid());
    }

    private void randomizeRooms()
    {
        ArrayList<RoomGridCell> unTyped = grid.getAllCellsWhere(RoomGridCell::needsRoomType);

        while (!unTyped.isEmpty())
        {
            RoomGridCell rootCell = unTyped.get(random.nextInt(unTyped.size()));
            int availableX = grid.getContiguousUntypedRoomsX(rootCell.getGridPosition());
            int availableZ = grid.getContiguousUntypedRoomsZ(rootCell.getGridPosition());

            EnumRoomType type = roomRandomizer.next();
            int maxX = Math.min(type.getMaxXCells(), availableX);
            int maxZ = Math.min(type.getMaxZCells(), availableZ);

            int sizeX = (maxX > 1) ? (1 + random.nextInt(maxX - 1)) : 1;
            int sizeZ = (maxZ > 1) ? (1 + random.nextInt(maxZ - 1)) : 1;

            for (int x = 0; x < sizeX; x++)
            {
                for (int z = 0; z < sizeZ; z++)
                {
                    RoomGridCell buildCell = grid.getCellAt(rootCell.getFloor(), rootCell.getGridX() + x, rootCell.getGridZ() + z);
                    buildCell.setRoom(RoomFactoryCastle.CreateGenericRoom(type, getRoomStart(buildCell), roomSize, floorHeight));
                }
            }

            unTyped = grid.getAllCellsWhere(RoomGridCell::needsRoomType);
        }
    }

    private void addMainBuilding()
    {
        setFirstLayerBuildable();

        //These are declared up here so after the for loop we retain the indices
        //and floor of the highest section
        int lastMainStartX = 0;
        int lastMainStartZ = 0;
        int mainRoomsX = 0;
        int mainRoomsZ = 0;
        int layer;

        for (layer = 0; layer < MAX_LAYERS; layer++)
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

            mainRoomsX = randomSubsectionLength(maxLenX);
            mainRoomsZ = randomSubsectionLength(maxLenZ);

            int offsetX = random.nextBoolean() ? 0 : maxLenX - mainRoomsX;
            int offsetZ = random.nextBoolean() ? 0 : maxLenZ - mainRoomsZ;

            lastMainStartX = minX + offsetX;
            lastMainStartZ = minZ + offsetZ;

            for (int floor = 0; floor < floorsPerLayer; floor++)
            {
                usedFloors++;
                for (int x = 0; x < mainRoomsX; x++)
                {
                    for (int z = 0; z < mainRoomsZ; z++)
                    {
                        int xIndex = lastMainStartX + x;
                        int zIndex = lastMainStartZ + z;
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
            int floorStart = layer * floorsPerLayer;
            int sideRoomsX, sideRoomsZ, startX, startZ;

            if (openCellsWest > 0)
            {
                sideRoomsX = random.nextInt(openCellsWest + 1);
                sideRoomsZ = random.nextInt(mainRoomsZ + 1);
                if (Math.min(sideRoomsX, sideRoomsZ) >= 1)
                {
                    startX = minX + offsetX - sideRoomsX;
                    startZ = random.nextBoolean() ? minZ + offsetZ : minZ + offsetZ + mainRoomsZ - sideRoomsZ;
                    grid.selectBlockOfCellsForBuilding(floorStart, floorsPerLayer, startX, sideRoomsX, startZ, sideRoomsZ);

                    roofAreas.add(new RoofArea(startX, sideRoomsX, startZ, sideRoomsZ, floorStart + floorsPerLayer));
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
                    grid.selectBlockOfCellsForBuilding(floorStart, floorsPerLayer, startX, sideRoomsX, startZ, sideRoomsZ);

                    roofAreas.add(new RoofArea(startX, sideRoomsX, startZ, sideRoomsZ, floorStart + floorsPerLayer));
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
                    grid.selectBlockOfCellsForBuilding(floorStart, floorsPerLayer, startX, sideRoomsX, startZ, sideRoomsZ);

                    roofAreas.add(new RoofArea(startX, sideRoomsX, startZ, sideRoomsZ, floorStart + floorsPerLayer));
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
                    grid.selectBlockOfCellsForBuilding(floorStart, floorsPerLayer, startX, sideRoomsX, startZ, sideRoomsZ);

                    roofAreas.add(new RoofArea(startX, sideRoomsX, startZ, sideRoomsZ, floorStart + floorsPerLayer));
                }
            }
        }

        //Make the highest main room section a potential roof position
        roofAreas.add(new RoofArea(lastMainStartX, mainRoomsX, lastMainStartZ, mainRoomsZ, layer * floorsPerLayer));
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
            HashSet<EnumFacing> sidesToCheck = new HashSet<>(Arrays.asList(EnumFacing.HORIZONTALS));

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
                        canBuild = grid.cellIsOuterEdge(cell, side) && grid.canAttachTower(cell, side);
                    }
                    else
                    {
                        canBuild = grid.adjacentCellIsWalkableRoof(cell, side) && grid.canAttachTower(cell, side);
                    }

                    if (canBuild)
                    {
                        //max height is one floor above the top floor of the castle
                        int maxHeight = (usedFloors - floor) + 1;

                        //if we can build at least the min tower height
                        if (maxHeight > MIN_TOWER_FLOORS)
                        {
                            int height = MIN_TOWER_FLOORS + random.nextInt(maxHeight - MIN_TOWER_FLOORS);
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

        //Randomize the size somewhere between the min tower size and the room size
        int towerSize = MIN_TOWER_SIZE + random.nextInt(roomSize - Math.min(MIN_TOWER_SIZE, roomSize));

        System.out.format("Placing tower at %d,%d on floor %d facing %s, size = %d\n",
                x, z, startFloor, alignment.toString(), towerSize);

        CastleRoomTowerSquare tower = null;
        RoomGridCell cell = null;

        for (int floor = startFloor; floor < startFloor + height; floor++)
        {
            cell = grid.getCellAt(floor, x, z);
            if (cell == null)
            {
                System.out.println("Tried to place a tower @ null");
            }
            else
            {
                tower = new CastleRoomTowerSquare(getRoomStart(cell), roomSize, floorHeight, alignment, towerSize, tower);
                cell.setRoom(tower);
                //cell.setReachable();

                RoomGridCell adjacent = grid.getAdjacentCell(cell, alignment);
                if (adjacent.isPopulated())
                {
                    adjacent.getRoom().addDoorOnSideCentered(alignment.getOpposite());

                    if (adjacent.getRoom() instanceof CastleRoomWalkableRoof)
                    {
                        cell.getRoom().addDoorOnSideCentered(alignment);
                    }
                    else
                    {
                        cell.getRoom().removeWall(alignment);
                    }
                }
                else
                {
                    if (grid.adjacentCellIsWalkableRoof(cell, alignment))
                    {
                        cell.getRoom().addDoorOnSideCentered(alignment);
                    }
                }
            }
        }

        //Build a walkable roof on top of the tower
        if (tower != null && grid.withinGridBounds(startFloor + height, x, z))
        {
            cell = grid.getCellAt(startFloor + height, x, z);
            cell.setRoom(new CastleRoomWalkableRoofTower(getRoomStart(cell), roomSize, floorHeight, tower));
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
        boolean firstRoomReachable = false;

        for (int floor = 0; floor < usedFloors; floor += floorsPerLayer)
        {
            HashSet<EnumFacing> doorDirections = new HashSet<>(); //Sides of this floor that already have exits

            final int f = floor;
            ArrayList<RoomGridCell> floorRooms = grid.getAllCellsWhere(r -> r.getFloor() == f && r.isPopulated()
                                    && !(r.getRoom() instanceof CastleRoomWalkableRoof) && !r.getRoom().isTower());
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
                            //At least one room needs to be reachable on the ground floor to start the pathing
                            if(!firstRoomReachable && floor == 0)
                            {
                                firstRoomReachable = true;
                                cell.setReachable();
                            }
                            doorDirections.add(side);
                            addDoorToRoomCentered(cell, side);
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

            /*
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
             */
            {
                candidateCells = grid.getAllCellsWhere(r -> r.getFloor() == f && r.needsRoomType());

                Collections.shuffle(candidateCells);

                for (RoomGridCell cell : candidateCells)
                {
                    RoomGridCell aboveCell = grid.getAdjacentCell(cell, EnumFacing.UP);
                    if (aboveCell != null && aboveCell.needsRoomType())
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
            addDoorToRoomCentered(cell, side);

            aboveCell.setRoom(new CastleRoomLandingDirected(getRoomStart(aboveCell), roomSize, floorHeight,
                    stairs));
            aboveCell.setReachable();

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
                RoomGridCell adjacent = grid.getAdjacentCell(cell, side);
                if (adjacent != null &&
                    adjacent.needsRoomType() &&
                    !grid.cellBordersRoomType(cell, EnumRoomType.LANDING_DIRECTED) &&
                    !grid.cellBordersRoomType(adjacent, EnumRoomType.LANDING_DIRECTED) &&
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
            ArrayList<RoomGridCell> unreachable = grid.getAllCellsWhere(c -> c.getFloor() == f && c.isValidPathStart());
            ArrayList<RoomGridCell> reachable = grid.getAllCellsWhere(c -> c.getFloor() == f && c.isValidPathDestination());

            while (!unreachable.isEmpty() && !reachable.isEmpty())
            {
                RoomGridCell srcRoom = unreachable.get(random.nextInt(unreachable.size()));

                //going for the nearest doesn't always make the most interesting layout, may want to add noise
                RoomGridCell destRoom = findNearestReachableRoom(srcRoom, reachable);

                LinkedList<PathNode> destToSrcPath = findPathBetweenRooms(srcRoom, destRoom);

                if (!destToSrcPath.isEmpty())
                {
                    for (PathNode node : destToSrcPath)
                    {
                        RoomGridCell cell = grid.getCellAt(node.getCell().getGridPosition());
                        if (cell != null)
                        {
                            if (node.getParent() != null)
                            {
                                addDoorToRoomRandom(cell, node.getParentDirection());
                            }
                            cell.setReachable();
                            unreachable.remove(cell);
                            reachable.add(cell);
                        }
                    }
                }
                else
                {
                    System.out.println("Failed to find path from " + srcRoom.getGridPosition().toString() + " to" + destRoom.getGridPosition().toString());
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
                if (neighborCell != null && neighborCell.isPopulated() && neighborCell.reachableFromSide(direction.getOpposite()))
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
            if (cell.getRoom() instanceof  CastleRoomWalkableRoof)
            {
                determineWalkableRoofWalls(cell);
            }
            else
            {
                determineNormalRoomWalls(cell);
            }
        }
    }

    private void determineWalkableRoofWalls(RoomGridCell cell)
    {
        for (EnumFacing side : EnumFacing.HORIZONTALS)
        {
            if (!grid.adjacentCellIsPopulated(cell, side))
            {
                cell.getRoom().addOuterWall(side);
            }
        }
    }

    private void determineNormalRoomWalls(RoomGridCell cell)
    {
        //If we are at the edge cells, we force adding the walls. Otherwise we don't force
        //it so rooms like hallways don't add them by mistake.
        boolean outerSouth = !grid.adjacentCellIsFullRoom(cell, EnumFacing.SOUTH);

        if (outerSouth)
        {
            cell.getRoom().addOuterWall(EnumFacing.SOUTH);
        }
        else
        {
            cell.getRoom().addInnerWall(EnumFacing.SOUTH);
        }

        boolean outerEast = !grid.adjacentCellIsFullRoom(cell, EnumFacing.EAST);

        if (outerEast)
        {
            cell.getRoom().addOuterWall(EnumFacing.EAST);
        }
        else
        {
            cell.getRoom().addInnerWall(EnumFacing.EAST);
        }

        if (!grid.adjacentCellIsFullRoom(cell, EnumFacing.NORTH))
        {
            cell.getRoom().addOuterWall(EnumFacing.NORTH);
        }

        if (!grid.adjacentCellIsFullRoom(cell, EnumFacing.WEST))
        {
            cell.getRoom().addOuterWall(EnumFacing.WEST);
        }
    }

    private void determineRoofs()
    {
        ArrayList<RoomGridCell> roofCells = grid.getAllCellsWhere(c -> !c.isSelectedForBuilding() &&
                                                                        grid.adjacentCellIsPopulated(c, EnumFacing.DOWN));

        for (RoofArea roofArea : roofAreas)
        {
            if (random.nextBoolean())
            {
                addRoofFromRoofArea(roofArea);
                for (int x = roofArea.gridStartX; x < roofArea.gridStartX + roofArea.lengthX; x++)
                {
                    for (int z = roofArea.gridStartZ; z < roofArea.gridStartZ + roofArea.lengthZ; z++)
                    {
                        final int xf = x;
                        final int zf = z;
                        roofCells.removeIf(c -> c.getGridX() == xf && c.getGridZ() == zf && c.getFloor() == roofArea.floor);
                    }
                }
            }
        }

        for (RoomGridCell cell : roofCells)
        {
            cell.setRoom(new CastleRoomWalkableRoof(getRoomStart(cell), roomSize, floorHeight));
        }
    }

    private void addRoofFromRoofArea(RoofArea roofArea)
    {
        BlockPos roofStart = getRoomStart(roofArea.floor, roofArea.gridStartX, roofArea.gridStartZ);

        potentialRoofs.add(new CastleAddonRoof(roofStart, roofArea.lengthX * roomSize, roofArea.lengthZ * roomSize));
    }

    private void addDoorToRoomCentered(RoomGridCell cell, EnumFacing side)
    {
        if (cell.getRoom().hasWallOnSide(side))
        {
            DoorPlacement placement = cell.getRoom().addDoorOnSideCentered(side);
            if (grid.adjacentCellIsPopulated(cell, side))
            {
                grid.getAdjacentCell(cell, side).getRoom().registerAdjacentRoomDoor(side.getOpposite(), placement);
            }
        }
        else
        {
            if (grid.adjacentCellIsPopulated(cell, side))
            {
                DoorPlacement placement = grid.getAdjacentCell(cell, side).getRoom().addDoorOnSideCentered(side.getOpposite());
                cell.getRoom().registerAdjacentRoomDoor(side, placement);
            }
        }
    }

    private void addDoorToRoomRandom(RoomGridCell cell, EnumFacing side)
    {
        if (cell.getRoom().hasWallOnSide(side))
        {
            DoorPlacement placement = cell.getRoom().addDoorOnSideRandom(random, side);
            if (grid.adjacentCellIsPopulated(cell, side))
            {
                grid.getAdjacentCell(cell, side).getRoom().registerAdjacentRoomDoor(side.getOpposite(), placement);
            }
        }
        else
        {
            if (grid.adjacentCellIsPopulated(cell, side))
            {
                DoorPlacement placement = grid.getAdjacentCell(cell, side).getRoom().addDoorOnSideRandom(random, side.getOpposite());
                cell.getRoom().registerAdjacentRoomDoor(side, placement);
            }
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

    private int getLayerFromFloor(int floor)
    {
        return floor / floorsPerLayer;
    }
}