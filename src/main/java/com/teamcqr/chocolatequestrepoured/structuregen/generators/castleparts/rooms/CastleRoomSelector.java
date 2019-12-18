package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.addons.CastleAddonRoof;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.segments.DoorPlacement;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.WeightedRandom;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.apache.commons.lang3.ObjectUtils;

import javax.annotation.Nullable;
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

    public class SupportArea
    {
        private BlockPos nwCorner;
        private int blocksX;
        private int blocksZ;
        private int PADDING_PER_SIDE = 2;

        private SupportArea(BlockPos nwCorner, int xCells, int zCells)
        {
            this.blocksX = (xCells * roomSize) + (PADDING_PER_SIDE * 2);
            this.blocksZ = (zCells * roomSize) + (PADDING_PER_SIDE * 2);
            this.nwCorner = nwCorner.north(PADDING_PER_SIDE).west(PADDING_PER_SIDE);
        }

        public BlockPos getNwCorner()
        {
            return nwCorner;
        }

        public int getBlocksX()
        {
            return blocksX;
        }

        public int getBlocksZ()
        {
            return blocksZ;
        }
    }

    private static final int MAX_LAYERS = 5;
    private static final int PADDING_FLOORS = 2;
    private static final int MIN_TOWER_FLOORS = 3;
    private static final int MIN_TOWER_SIZE = 7; //needs to have room for spiral stairs

    private static final int MIN_BOSS_ROOM_SIZE = 15;

    private BlockPos startPos;
    private int floorHeight;
    private int roomSize;
    private int minRoomsForBoss;
    private int floorsPerLayer;
    private int maxFloors;
    private int usedFloors;
    private Random random;
    private RoomGrid grid;
    private List<SupportArea> supportAreas;
    private List<CastleAddonRoof> castleRoofs;
    private WeightedRandom<EnumRoomType> roomRandomizer;

    public CastleRoomSelector(BlockPos startPos, int roomSize, int floorHeight, int floorsPerLayer,
                              int numSlotsX, int numSlotsZ, Random random)
    {
        this.startPos = startPos;
        this.floorHeight = floorHeight;
        this.roomSize = roomSize;
        this.floorsPerLayer = floorsPerLayer;
        this.maxFloors = floorsPerLayer * MAX_LAYERS;
        this.minRoomsForBoss = (int)(Math.ceil((double) MIN_BOSS_ROOM_SIZE / (roomSize - 1)));
        this.random = random;
        this.castleRoofs = new ArrayList<>();
        this.supportAreas = new ArrayList<>();

        //Add padding floors so that we can build walkable roofs on top of the highest rooms
        this.grid = new RoomGrid(maxFloors + PADDING_FLOORS, numSlotsX, numSlotsZ, random);

        this.roomRandomizer = new WeightedRandom<EnumRoomType>(random);

        this.roomRandomizer.add(EnumRoomType.KITCHEN, 2);
        this.roomRandomizer.add(EnumRoomType.ALCHEMY_LAB, 2);
        this.roomRandomizer.add(EnumRoomType.ARMORY, 2);
        this.roomRandomizer.add(EnumRoomType.BEDROOM, 2);
    }

    public void generate(World world, CastleDungeon dungeon)
    {
        //Roofs come first so rooms overwrite roof blocks
        generateRoofs(world, dungeon);

        generateAndDecorateRooms(world, dungeon);

    }

    private void generateAndDecorateRooms(World world, CastleDungeon dungeon)
    {
        //Start with the entire list of populated cells
        ArrayList<RoomGridCell> populated = grid.getAllCellsWhere(RoomGridCell::isPopulated);
        ArrayList<RoomGridCell> toGenerate = new ArrayList<>(populated);

        //Generate walkable roofs first since they are lowest priority, other rooms may occupy same BlockPos
        for (RoomGridCell cell : grid.getAllCellsWhere(c -> c.isPopulated() && c.getRoom() instanceof CastleRoomWalkableRoof))
        {
            toGenerate.remove(cell);
            cell.getRoom().generate(world, dungeon);
        }

        //Generate all other cells
        for (RoomGridCell cell : toGenerate)
        {
            cell.getRoom().generate(world, dungeon);
        }

        //The rooms MUST be generated before they are decorated
        //Some decoration requires that neighboring rooms have their walls/doors
        for (RoomGridCell cell : grid.getAllCellsWhere(RoomGridCell::isPopulated))
        {
            cell.getRoom().decorate(world, dungeon);
        }
    }

    private void generateRoofs(World world, CastleDungeon dungeon)
    {
        for (CastleAddonRoof roof : castleRoofs)
        {
            roof.generate(world, dungeon);
        }
    }

    public void randomizeCastle()
    {
        addMainBuilding();

        addBossRooms();
        addHallways();
        addStairCases();

        randomizeRooms();
        linkCells();

        determineRoofs();
        determineWalls();

        placeOuterDoors();
        placeTowers();
        pathBetweenRooms();

    }

    private void addMainBuilding()
    {
        setFirstLayerBuildable();

        boolean lastFloor = false;

        //These are declared up here so after the for loop we retain the indices
        //and floor of the highest section
        int layer;

        for (layer = 0; ((!lastFloor) && (layer < MAX_LAYERS)); layer++)
        {
            int firstFloorInLayer = layer * floorsPerLayer;

            ArrayList<RoomGrid.Area2D> buildableAreas = grid.getAllGridAreasWhere(firstFloorInLayer, RoomGridCell::isBuildable, 2, 2);
            System.out.println(buildableAreas.toString());

            if (!buildableAreas.isEmpty())
            {
                for (RoomGrid.Area2D buildArea : buildableAreas)
                {
                    //The first area in the list is the largest area
                    if (buildableAreas.get(0) == buildArea)
                    {
                        if (buildArea.dimensionsAreAtLeast(minRoomsForBoss, minRoomsForBoss + 1))
                        {
                            if (buildArea.dimensionsAre(minRoomsForBoss, minRoomsForBoss + 1))
                            {
                                //if largest area is exact size for boss room, have to make boss area here
                                grid.setBossArea(buildArea);
                                lastFloor = true;
                            }
                            else
                            {
                                //area is at least big enough for boss area
                                if (layer >= 3)
                                {
                                    RoomGrid.Area2D bossArea = buildArea.getRandomSubArea(random, minRoomsForBoss, minRoomsForBoss + 1, true);
                                    //grid.selectBlockOfCellsForBuilding(bossArea, floorsPerLayer);
                                    System.out.println("At minimum boss area so setting boss area to: " + bossArea.toString());
                                    grid.setBossArea(bossArea);
                                    lastFloor = true;

                                    //TODO: Make use of any remaining space
                                }
                                else
                                {
                                    RoomGrid.Area2D structArea = buildArea.getRandomSubArea(random, minRoomsForBoss, minRoomsForBoss + 1, false);
                                    System.out.println("Added central struct to largest area: " + structArea.toString());
                                    grid.selectBlockOfCellsForBuilding(structArea, floorsPerLayer);
                                    addSupportIfFirstLayer(layer, structArea);

                                    addSideStructures(structArea, buildArea);
                                }
                            }
                        }
                    }
                    else //all other build areas that aren't the largest
                    {
                        RoomGrid.Area2D structArea = buildArea.getRandomSubArea(random, 2, 1, true);
                        System.out.println("Added central struct to NOT largest area: " + structArea.toString());
                        grid.selectBlockOfCellsForBuilding(structArea, floorsPerLayer);
                        addSupportIfFirstLayer(layer, structArea);

                        addSideStructures(structArea, buildArea);
                    }
                }
            }
            else
            {
                System.out.println("Buildable areas was empty (break here).");
            }

            usedFloors += floorsPerLayer;
        }

        //Make the highest main room section a potential roof position
        /*
        if (!lastFloor)
        {
            roofAreas.add(new RoofArea(lastMainStartX, mainRoomsX, lastMainStartZ, mainRoomsZ, layer * floorsPerLayer));
        }
        */
    }

    private void addSideStructures(RoomGrid.Area2D structArea, RoomGrid.Area2D buildArea)
    {
        for (EnumFacing side : EnumFacing.HORIZONTALS)
        {
            RoomGrid.Area2D sideAllowedArea = buildArea.sliceToSideOfArea(structArea, side);
            RoomGrid.Area2D lastBuiltArea = structArea;
            RoomGrid.Area2D sideSelectedArea;

            //While there is still room to build in this direction, 75% chance to keep going
            while (sideAllowedArea != null && DungeonGenUtils.PercentageRandom(75, random))
            {
                sideSelectedArea = sideAllowedArea.getRandomSubArea(random, 1, 1, false);
                sideSelectedArea.alignToSide(random, lastBuiltArea, side, buildArea);

                grid.selectBlockOfCellsForBuilding(sideSelectedArea, floorsPerLayer);
                addSupportIfFirstLayer(structArea.start.getFloor(), sideSelectedArea);
                System.out.println("Added " + side.toString() + " side struct: " + sideSelectedArea.toString());

                lastBuiltArea = sideSelectedArea;

                sideAllowedArea = buildArea.sliceToSideOfArea(lastBuiltArea, side);
            }
        }
    }

    private void setFirstLayerBuildable()
    {
        ArrayList<RoomGridCell> firstLayer = grid.getAllCellsWhere(c -> c.getFloor() < floorsPerLayer);

        for (RoomGridCell cell : firstLayer)
        {
            cell.setBuildable();
        }
    }

    private void addSupportIfFirstLayer(int layer, RoomGrid.Area2D area)
    {
        addSupportIfFirstLayer(layer, area.start.getX(), area.start.getZ(), area.sizeX, area.sizeZ);
    }

    private void addSupportIfFirstLayer(int layer, int gridIndexX, int gridIndexZ, int roomsX, int roomsZ)
    {
        if (layer == 0)
        {
            //get NW corner of the area and move it NW 1 square because of the extra N and W walls on the sides
            BlockPos startCorner = getRoomStart(0, gridIndexX, gridIndexZ).north().west();

            this.supportAreas.add(new SupportArea(startCorner, roomsX, roomsZ));
        }
    }

    public List<SupportArea> getSupportAreas()
    {
        return supportAreas;
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

                            //First floor is the same as first layer in this case
                            addSupportIfFirstLayer(floor, cell.getGridX(), cell.getGridZ(), 1, 1);

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
                //cell.setAllLinkedReachable();

                RoomGridCell adjacent = grid.getAdjacentCell(cell, alignment);
                if (adjacent.isPopulated() && !adjacent.getRoom().getRoomType().isBossRoom())
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
            }
        }

        //Build a walkable roof on top of the tower
        if (tower != null && grid.withinGridBounds(startFloor + height, x, z))
        {
            cell = grid.getCellAt(startFloor + height, x, z);
            cell.setRoom(new CastleRoomWalkableRoofTower(getRoomStart(cell), roomSize, floorHeight, tower));
        }
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

    private void addBossRooms()
    {
        CastleRoomRoofBossMain rootRoom;
        RoomGridPosition rootPos;
        RoomGrid.Area2D bossArea = grid.getBossArea();

        if (bossArea != null && bossArea.dimensionsAreAtLeast(minRoomsForBoss, minRoomsForBoss + 1))
        {
            boolean horizontal = bossArea.sizeX > bossArea.sizeZ;
            int longSideLen = horizontal ? bossArea.sizeX : bossArea.sizeZ;
            int shortSideLen = horizontal ? bossArea.sizeZ : bossArea.sizeX;
            boolean dualStairs = (shortSideLen % 2 == 0);

            HashMap<RoomGridPosition, EnumFacing> possibleStairs = new HashMap<>();

            EnumFacing alongLongSide = horizontal ? EnumFacing.EAST : EnumFacing.SOUTH;
            EnumFacing alongShortSide = horizontal ? EnumFacing.SOUTH : EnumFacing.EAST;

            final int shortSideOffset = dualStairs ? ((shortSideLen / 2) - 1) : (shortSideLen / 2);
            RoomGridPosition closePos = bossArea.start.move(alongShortSide, shortSideOffset);
            possibleStairs.put(closePos, alongLongSide);
            possibleStairs.put(closePos.move(alongLongSide, longSideLen - 1), alongLongSide.getOpposite());

            if (!dualStairs)
            {
                Iterator<RoomGridPosition> iter = new ArrayList<>(possibleStairs.keySet()).iterator();
                while (iter.hasNext())
                {
                    RoomGridPosition gridPos = iter.next();
                    if (!cellValidForDirectedStairs(gridPos, possibleStairs.get(gridPos)))
                    {
                        possibleStairs.remove(gridPos);
                    }
                }
            }

            if (!possibleStairs.isEmpty())
            {
                ArrayList<RoomGridPosition> stairPosList = new ArrayList<>(possibleStairs.keySet());
                RoomGridPosition topOfBossStairs = stairPosList.remove(random.nextInt(stairPosList.size()));
                RoomGridPosition bottomOfBossStairs = topOfBossStairs.move(EnumFacing.DOWN);
                EnumFacing stairDoorSide = possibleStairs.get(topOfBossStairs);

                if (dualStairs)
                {
                    CastleRoomBossStairMain stairMain = new CastleRoomBossStairMain(getRoomStart(bottomOfBossStairs), roomSize, floorHeight, stairDoorSide);
                    grid.getCellAt(bottomOfBossStairs).setRoom(stairMain);
                    //grid.initPathingForSingleCell(bottomOfBossStairs);

                    CastleRoomBossStairEmpty stairEmpty = new CastleRoomBossStairEmpty(getRoomStart(bottomOfBossStairs.move(alongShortSide)), roomSize, floorHeight, stairDoorSide);
                    grid.getCellAt(bottomOfBossStairs.move(alongShortSide)).setRoom(stairEmpty);
                    //grid.initPathingForSingleCell(bottomOfBossStairs.move(alongShortSide));

                    CastleRoomBossLandingMain landingMain = new CastleRoomBossLandingMain(getRoomStart(topOfBossStairs), roomSize, floorHeight, stairDoorSide);
                    grid.getCellAt(topOfBossStairs).setRoom(landingMain);

                    CastleRoomBossLandingEmpty landingEmpty = new CastleRoomBossLandingEmpty(getRoomStart(topOfBossStairs.move(alongShortSide)), roomSize, floorHeight, stairDoorSide);
                    grid.getCellAt(topOfBossStairs.move(alongShortSide)).setRoom(landingEmpty);
                }
                else
                {
                    //TODO: Single-wide boss stairs (just use directed stairs)
                }

                rootPos = bossArea.start;

                if (stairDoorSide == EnumFacing.SOUTH)
                {
                    rootPos = rootPos.move(EnumFacing.SOUTH);
                }
                else if (stairDoorSide == EnumFacing.EAST)
                {
                    rootPos = rootPos.move(EnumFacing.EAST);
                }

                rootRoom = new CastleRoomRoofBossMain(getRoomStart(rootPos), roomSize, floorHeight);

                grid.getCellAt(rootPos).setRoom(rootRoom);

                for (int x = 0; x < minRoomsForBoss; x++)
                {
                    for (int z = 0; z < minRoomsForBoss; z++)
                    {
                        if (x == 0 && z == 0)
                        {
                            continue;
                        }

                        RoomGridPosition emptyRoomPos = rootPos.move(EnumFacing.EAST, x).move(EnumFacing.SOUTH, z);
                        CastleRoomRoofBossEmpty emptyRoom = new CastleRoomRoofBossEmpty(getRoomStart(emptyRoomPos), roomSize, floorHeight);
                        grid.getCellAt(emptyRoomPos).setRoom(emptyRoom);
                    }
                }


                //Move the boss room area a few squares to align it with the stairs
                EnumFacing snapToSide = stairDoorSide.getOpposite();
                if (snapToSide == EnumFacing.NORTH)
                {
                    int distFromEdge = (bossArea.sizeX * roomSize) - rootRoom.getStaticSize();
                    int x = (bossArea.cellsFromStartX(topOfBossStairs) / (bossArea.sizeX - 1)) * distFromEdge;
                    rootRoom.setBossBuildOffset(new Vec3i(x, 0, 0));
                } else if (snapToSide == EnumFacing.WEST)
                {
                    int distFromEdge = (bossArea.sizeZ * roomSize) - rootRoom.getStaticSize();
                    int z = (bossArea.cellsFromStartZ(topOfBossStairs) / (bossArea.sizeZ - 1)) * distFromEdge;
                    rootRoom.setBossBuildOffset(new Vec3i(0, 0, z));
                } else if (snapToSide == EnumFacing.SOUTH)
                {
                    int distFromEdge = (bossArea.sizeX * roomSize) - rootRoom.getStaticSize();
                    int x = (bossArea.cellsFromStartX(topOfBossStairs) / (bossArea.sizeX - 1)) * distFromEdge;
                    int z = ((bossArea.sizeZ - 1) * roomSize) - rootRoom.getStaticSize();
                    rootRoom.setBossBuildOffset(new Vec3i(x, 0, z));
                } else //east
                {
                    int distFromEdge = (bossArea.sizeZ * roomSize) - rootRoom.getStaticSize();
                    int z = (bossArea.cellsFromStartZ(topOfBossStairs) / (bossArea.sizeZ - 1)) * distFromEdge;
                    int x = ((bossArea.sizeX - 1) * roomSize) - rootRoom.getStaticSize();
                    rootRoom.setBossBuildOffset(new Vec3i(x, 0, z));
                }
            }
        }
        else
        {
            System.out.println("Error adding boss rooms: boss area was never set during castle shaping.");
        }

    }


    public boolean cellValidForDirectedStairs(RoomGridPosition position, EnumFacing direction)
    {
        RoomGridCell stairCell = grid.getCellAt(position);
        RoomGridCell roomToStairs = grid.getCellAt(position.move(direction));

        //First check to see if this cell and the room it will open to are available
        if(stairCell != null &&
                stairCell.isSelectedForBuilding() &&
                roomToStairs != null &&
                roomToStairs.isSelectedForBuilding())
        {
            //Then check the other sides to make sure that we don't block pathing
            List<EnumFacing> outerSides = new ArrayList<>(Arrays.asList(EnumFacing.HORIZONTALS));
            outerSides.remove(direction);

            for (EnumFacing side : outerSides)
            {
                RoomGridCell checkCell = grid.getAdjacentCell(stairCell, side);
                if (checkCell != null && checkCell.isSelectedForBuilding())
                {
                    HashSet<RoomGridCell> invalid = new HashSet<>();
                    invalid.add(stairCell);
                    LinkedList<PathNode> destToSrcPath = findPathBetweenRooms(checkCell, roomToStairs, invalid);
                    if (destToSrcPath.isEmpty())
                    {
                        return false;
                    }
                }
            }

            //At this point we have checked all sides for blocked pathing so it should be OK
            return true;
        }

        return false;
    }

    private void linkCells()
    {
        for (int floor = 0; floor < usedFloors; floor++)
        {
            linkCellsOnFloor(floor);
        }
    }

    private void linkCellsOnFloor(int floor)
    {
        ArrayList<RoomGridCell> floorCells = grid.getAllCellsWhere(c -> c.isPopulated() && c.getFloor() == floor && !c.getRoom().isWalkableRoof());

        for (RoomGridCell cell : floorCells)
        {
            linkCellToAdjacentCells(cell);
        }
    }

    private void linkCellToAdjacentCells(RoomGridCell cell)
    {
        cell.linkToCell(cell); //link the cell to itself first

        for (EnumFacing direction : EnumFacing.HORIZONTALS)
        {
            RoomGridCell adjacent = grid.getAdjacentCell(cell, direction);
            if (adjacent != null && adjacent.isPopulated() && cell.getRoom().getRoomType() == adjacent.getRoom().getRoomType())
            {
                //if we are already on the adjacent cell's list then it likely means
                //that cell was linked to us already and nothing else needs to be done
                if (!adjacent.isLinkedToCell(cell))
                {
                    //link all of this cell's linked cells (including me) to the adjacent cell
                    for (RoomGridCell linkedCell : cell.getLinkedCellsCopy())
                    {
                        linkedCell.linkToCell(adjacent);
                    }

                    //copy current cell's links to neighbor
                    adjacent.setLinkedCells(cell.getLinkedCellsCopy());
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
        //Start at first floor since ground floor gets the grand entrance
        for (int floor = 1; floor < usedFloors; floor += floorsPerLayer)
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
                            doorDirections.add(side);
                            addDoorToRoomCentered(cell, side);
                            break;
                        }
                    }
                }
            }
        }
    }

    private void addHallways()
    {
        for (int floor = 0; floor < (grid.getBossArea().start.getFloor() - 1); floor++)
        {
            List<RoomGrid.Area2D> largestAreas = grid.getAllGridAreasWhere(floor, RoomGridCell::isValidHallwayRoom, 2, 2);
            if (!largestAreas.isEmpty())
            {
                RoomGrid.Area2D hallwayArea = largestAreas.get(0);
                boolean horizontal = hallwayArea.sizeX == hallwayArea.sizeZ ? random.nextBoolean() : hallwayArea.sizeX > hallwayArea.sizeZ;

                if (horizontal)
                {
                    int zIndex = DungeonGenUtils.randomBetweenGaussian(random, hallwayArea.getStartZ(), hallwayArea.getEndZ());

                    RoomGridPosition hallStartGridPos = new RoomGridPosition(floor, hallwayArea.getStartX(), zIndex);
                    ArrayList<RoomGridCell> hallwayCells = grid.getAdjacentSelectedCellsInRow(hallStartGridPos);

                    for (RoomGridCell hallwayCell : hallwayCells)
                    {
                        hallwayCell.setRoom(new CastleRoomHallway(getRoomStart(hallwayCell.getGridPosition()), roomSize, floorHeight, CastleRoomHallway.Alignment.HORIZONTAL));
                    }

                    if (floor == 0)
                    {
                        if (random.nextBoolean())
                        {
                            hallwayCells.get(0).getRoom().addOuterWall(EnumFacing.WEST);
                            hallwayCells.get(0).getRoom().addGrandEntrance(EnumFacing.WEST);
                            hallwayCells.get(0).setReachable();
                        }
                        else
                        {
                            hallwayCells.get(hallwayCells.size() - 1).getRoom().addOuterWall(EnumFacing.EAST);
                            hallwayCells.get(hallwayCells.size() - 1).getRoom().addGrandEntrance(EnumFacing.EAST);
                            hallwayCells.get(hallwayCells.size() - 1).setReachable();
                        }
                    }
                }
                else
                {
                    int xIndex = DungeonGenUtils.randomBetweenGaussian(random, hallwayArea.getStartX(), hallwayArea.getEndX());

                    RoomGridPosition hallStartGridPos = new RoomGridPosition(floor, xIndex, hallwayArea.getStartZ());
                    ArrayList<RoomGridCell> hallwayCells = grid.getAdjacentSelectedCellsInRow(hallStartGridPos);

                    for (RoomGridCell hallwayCell : hallwayCells)
                    {
                        hallwayCell.setRoom(new CastleRoomHallway(getRoomStart(hallwayCell.getGridPosition()), roomSize, floorHeight, CastleRoomHallway.Alignment.VERTICAL));
                    }

                    if (floor == 0)
                    {
                        if (random.nextBoolean())
                        {
                            hallwayCells.get(0).getRoom().addOuterWall(EnumFacing.NORTH);
                            hallwayCells.get(0).getRoom().addGrandEntrance(EnumFacing.NORTH);
                            hallwayCells.get(0).setReachable();
                        }
                        else
                        {
                            hallwayCells.get(hallwayCells.size() - 1).getRoom().addOuterWall(EnumFacing.SOUTH);
                            hallwayCells.get(hallwayCells.size() - 1).getRoom().addGrandEntrance(EnumFacing.SOUTH);
                            hallwayCells.get(hallwayCells.size() - 1).setReachable();
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

            candidateCells = grid.getAllCellsWhere(r -> r.getFloor() == f && r.needsRoomType());

            Collections.shuffle(candidateCells);

            for (RoomGridCell cell : candidateCells)
            {
                RoomGridCell aboveCell = grid.getAdjacentCell(cell, EnumFacing.UP);
                if (aboveCell != null && aboveCell.needsRoomType() && !aboveCell.isOnFloorWithLanding())
                {
                    CastleRoomStaircaseSpiral stairs = new CastleRoomStaircaseSpiral(getRoomStart(cell), roomSize, floorHeight);
                    cell.setRoom(stairs);

                    CastleRoomLandingSpiral landing = new CastleRoomLandingSpiral(getRoomStart(aboveCell), roomSize, floorHeight, stairs);
                    aboveCell.setRoom(landing);
                    aboveCell.setReachable();
                    aboveCell.setLandingForAllPathableCells();
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

    private void pathBetweenRooms()
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
                HashSet<RoomGridCell> pathableFromSrc = srcRoom.getPathableCellsCopy();

                pathableFromSrc.remove(srcRoom); //Don't want to path to myself
                pathableFromSrc.removeIf(c -> !c.isReachable());

                RoomGridCell destRoom = findNearestReachableRoom(srcRoom, pathableFromSrc);

                if (destRoom != null)
                {
                    LinkedList<PathNode> destToSrcPath = findPathBetweenRooms(srcRoom, destRoom, null);

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
                                cell.setAllLinkedReachable(unreachable, reachable);
                            }
                        }
                    } else
                    {
                        System.out.println("Failed to find path from " + srcRoom.getGridPosition().toString() + " to" + destRoom.getGridPosition().toString());
                    }
                }
                else
                {
                    System.out.println(srcRoom + " had no pathable rooms!");
                }
            }
        }
    }

    private LinkedList<PathNode> findPathBetweenRooms(RoomGridCell startCell, RoomGridCell endCell, @Nullable HashSet<RoomGridCell> invalidCells)
    {
        LinkedList<PathNode> open = new LinkedList<>();
        LinkedList<PathNode> closed = new LinkedList<>();
        LinkedList<PathNode> path = new LinkedList<>();

        if (invalidCells == null)
        {
            invalidCells = new HashSet<>();
        }

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
                if (neighborCell != null &&
                        neighborCell.isSelectedForBuilding() &&
                        neighborCell.reachableFromSide(direction.getOpposite()) &&
                        !invalidCells.contains(neighborCell))
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

    @Nullable
    private RoomGridCell findNearestReachableRoom(RoomGridCell origin, HashSet<RoomGridCell> pathableRooms)
    {
        ArrayList<RoomGridCell> sorted = new ArrayList<>(pathableRooms);

        if (!sorted.isEmpty())
        {
            sorted.sort((RoomGridCell c1, RoomGridCell c2) ->
                    Double.compare(grid.distanceBetweenCells2D(origin, c1), (grid.distanceBetweenCells2D(origin, c2))));

            return sorted.get(0);
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
            if (!cell.isLinkedToCell(grid.getAdjacentCell(cell, EnumFacing.SOUTH)))
            {
                cell.getRoom().addInnerWall(EnumFacing.SOUTH);
            }
        }

        boolean outerEast = !grid.adjacentCellIsFullRoom(cell, EnumFacing.EAST);

        if (outerEast)
        {
            cell.getRoom().addOuterWall(EnumFacing.EAST);
        }
        else
        {
            if (!cell.isLinkedToCell(grid.getAdjacentCell(cell, EnumFacing.EAST)))
            {
                cell.getRoom().addInnerWall(EnumFacing.EAST);
            }
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
        List<RoomGrid.Area2D> roofAreas = new ArrayList<>();
        ArrayList<RoomGridCell> roofCells = grid.getAllCellsWhere(c -> grid.cellIsValidForRoof(c));

        //For each "roof" floor
        for (int floor = floorsPerLayer; floor < usedFloors; floor += floorsPerLayer)
        {
            roofAreas.addAll(grid.getAllGridAreasWhere(floor, c -> grid.cellIsValidForRoof(c), 1, 2));
        }

        for (RoomGrid.Area2D roofArea : roofAreas)
        {
            if (random.nextBoolean())
            {
                addRoofFromRoofArea(roofArea);
                for (RoomGridPosition areaPos : roofArea.getPositionList())
                {
                    roofCells.remove(grid.getCellAt(areaPos));
                }
            }
        }

        for (RoomGridCell cell : roofCells)
        {
            cell.setRoom(new CastleRoomWalkableRoof(getRoomStart(cell), roomSize, floorHeight));
        }
    }

    private void addRoofFromRoofArea(RoomGrid.Area2D roofArea)
    {
        BlockPos roofStart = getRoomStart(roofArea.start.getFloor(), roofArea.start.getX(), roofArea.start.getZ());

        //Account for extra wall on N and W sides
        roofStart = roofStart.north().west();
        final int sizeX = (roofArea.sizeX * roomSize) + 1;
        final int sizeZ = (roofArea.sizeZ * roomSize) + 1;

        castleRoofs.add(new CastleAddonRoof(roofStart, sizeX, sizeZ));
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

    private BlockPos getRoomStart(RoomGridPosition position)
    {
        int gridX = position.getX();
        int floor = position.getFloor();
        int gridZ = position.getZ();
        return startPos.add(gridX * roomSize, floor * floorHeight, gridZ * roomSize);
    }

    private int getLayerFromFloor(int floor)
    {
        return floor / floorsPerLayer;
    }
}