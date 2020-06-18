package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.segments;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonCastle;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.RandomCastleConfigOptions;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.RoomGridCell;
import com.teamcqr.chocolatequestrepoured.util.BlockStateGenArray;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Optional;
import java.util.Random;

public class CastleMainStructWall {
    private final int length;
    private final int height;
    private RandomCastleConfigOptions.WindowType windowType = RandomCastleConfigOptions.WindowType.BASIC_GLASS;

    public enum WallOrientation {
        HORIZONTAL,
        VERTICAL
    }

    private boolean enabled = false;
    private boolean isOuterWall;
    private int doorStartOffset = 0;
    private EnumCastleDoorType doorType = EnumCastleDoorType.NONE;
    private BlockPos origin;
    private WallOrientation orientation;
    private HashMap<EnumFacing, RoomGridCell> adjacentCells = new HashMap<>();

    public CastleMainStructWall(BlockPos origin, WallOrientation orientation, int length, int height) {
        this.origin = origin;
        this.orientation = orientation;
        this.length = length;
        this.height = height;
    }

    public void registerAdjacentCell(RoomGridCell cell, EnumFacing directionOfCell) {
        adjacentCells.put(directionOfCell, cell);
    }

    public Optional<RoomGridCell> getAdjacentCell(EnumFacing direction) {
        if (adjacentCells.containsKey(direction)) {
            return Optional.of(adjacentCells.get(direction));
        } else {
            return Optional.empty();
        }
    }

    public void enable()
    {
        this.enabled = true;
    }

    public void disable()
    {
        this.enabled = false;
    }

    public void setAsOuterWall() {
        this.isOuterWall = true;
    }

    public void setAsInnerWall() {
        this.isOuterWall = false;
    }

    public boolean hasDoor() {
        return (this.doorType != EnumCastleDoorType.NONE);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void generate(BlockStateGenArray genArray, DungeonCastle dungeon) {
        BlockPos pos;
        IBlockState blockToBuild;

        EnumFacing iterDirection;
        this.windowType = dungeon.getRandomWindowType();

        if (this.orientation == WallOrientation.VERTICAL) {
            iterDirection = EnumFacing.SOUTH;
        } else {
            iterDirection = EnumFacing.EAST;
        }

        for (int i = 0; i < this.length; i++) {
            for (int y = 0; y < this.height; y++) {
                pos = this.origin.offset(iterDirection, i).offset(EnumFacing.UP, y);
                blockToBuild = this.getBlockToBuild(pos, dungeon);
                genArray.forceAddBlockState(pos, blockToBuild, BlockStateGenArray.GenerationPhase.MAIN);
            }
        }
    }

    protected IBlockState getBlockToBuild(BlockPos pos, DungeonCastle dungeon) {
        if (this.isOuterWall) {
            return this.getWindowBlock(pos, dungeon);
        } else if (this.hasDoor()) {
            return this.getDoorBlock(pos, dungeon);
        } else {
            return dungeon.getMainBlockState();
        }
    }

    protected IBlockState getDoorBlock(BlockPos pos, DungeonCastle dungeon) {
        switch (this.doorType) {
            case AIR:
                return this.getBlockDoorAir(pos, dungeon);

            case STANDARD:
                return this.getBlockDoorStandard(pos, dungeon);

            case FENCE_BORDER:
                return this.getBlockDoorFenceBorder(pos, dungeon);

            case STAIR_BORDER:
                return this.getBlockDoorStairBorder(pos, dungeon);

            case GRAND_ENTRY:
                return this.getBlockGrandEntry(pos, dungeon);

            default:
                return dungeon.getMainBlockState();
        }
    }

    private IBlockState getBlockDoorAir(BlockPos pos, DungeonCastle dungeon) {
        IBlockState blockToBuild = dungeon.getMainBlockState();
        int y = pos.getY() - this.origin.getY();
        int dist = this.getLengthPoint(pos);

        if (this.withinDoorWidth(dist)) {
            if (y == 0) {
                blockToBuild = dungeon.getMainBlockState();
            } else if (y < this.doorType.getHeight()) {
                blockToBuild = Blocks.AIR.getDefaultState();
            }
        }

        return blockToBuild;
    }

    private IBlockState getBlockDoorStairBorder(BlockPos pos, DungeonCastle dungeon) {
        IBlockState blockToBuild = dungeon.getMainBlockState();
        final int y = pos.getY() - this.origin.getY();
        final int dist = this.getLengthPoint(pos);
        final int halfPoint = this.doorStartOffset + (this.doorType.getWidth() / 2);

        if (this.withinDoorWidth(dist)) {
            if (y == 0) {
                blockToBuild = dungeon.getMainBlockState();
            } else if (dist == halfPoint || dist == halfPoint - 1) {
                if (y >= 1 && y <= 3) {
                    blockToBuild = Blocks.AIR.getDefaultState();
                } else if (y == 4) {
                    blockToBuild = dungeon.getSlabBlockState().withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP);
                }
            } else if (dist == halfPoint + 1 || dist == halfPoint - 2) {
                EnumFacing stairFacing;

                if (this.orientation == WallOrientation.HORIZONTAL) {
                    stairFacing = (dist == halfPoint - 2) ? EnumFacing.WEST : EnumFacing.EAST;
                } else {
                    stairFacing = (dist == halfPoint - 2) ? EnumFacing.SOUTH : EnumFacing.NORTH;
                }

                IBlockState stairBase = dungeon.getStairBlockState().withProperty(BlockStairs.FACING, stairFacing);

                if (y == 1) {
                    blockToBuild = stairBase;
                } else if (y == 2 || y == 3) {
                    blockToBuild = Blocks.AIR.getDefaultState();
                } else if (y == 4) {
                    blockToBuild = stairBase.withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP);
                }
            }
        }

        return blockToBuild;
    }

    private IBlockState getBlockDoorFenceBorder(BlockPos pos, DungeonCastle dungeon) {
        IBlockState blockToBuild = dungeon.getMainBlockState();
        final int y = pos.getY() - this.origin.getY();
        final int dist = this.getLengthPoint(pos);
        final int halfPoint = this.doorStartOffset + (this.doorType.getWidth() / 2);

        if (this.withinDoorWidth(dist)) {
            if (y == 0) {
                blockToBuild = dungeon.getMainBlockState();
            } else if (dist == halfPoint || dist == halfPoint - 1) {
                if (y == 1 || y == 2) {
                    blockToBuild = Blocks.AIR.getDefaultState();
                } else if (y == 3) {
                    blockToBuild = dungeon.getFenceBlockState();
                }
            } else if (((dist == halfPoint + 1) || (dist == halfPoint - 2)) && (y < this.doorType.getHeight())) {
                blockToBuild = dungeon.getFenceBlockState();
            }
        }

        return blockToBuild;
    }

    private IBlockState getBlockDoorStandard(BlockPos pos, DungeonCastle dungeon) {
        IBlockState blockToBuild = dungeon.getMainBlockState();
        final int y = pos.getY() - this.origin.getY();
        final int dist = this.getLengthPoint(pos);
        final int halfPoint = this.doorStartOffset + (this.doorType.getWidth() / 2);

        if (this.withinDoorWidth(dist)) {
            if (y == 0) {
                blockToBuild = dungeon.getFloorBlockState();
            } else if ((dist == halfPoint || dist == halfPoint - 1)) {
                if (y == 1 || y == 2) {
                    BlockDoor.EnumDoorHalf half = (y == 1) ? BlockDoor.EnumDoorHalf.LOWER : BlockDoor.EnumDoorHalf.UPPER;
                    BlockDoor.EnumHingePosition hinge = (dist == halfPoint) ? BlockDoor.EnumHingePosition.LEFT : BlockDoor.EnumHingePosition.RIGHT;
                    EnumFacing facing = (orientation == WallOrientation.HORIZONTAL) ? EnumFacing.NORTH : EnumFacing.WEST;

                    blockToBuild = dungeon.getDoorBlockState().withProperty(BlockDoor.HALF, half).withProperty(BlockDoor.FACING, facing).withProperty(BlockDoor.HINGE, hinge);
                } else if (y == 3) {
                    blockToBuild = dungeon.getPlankBlockState();
                }

            } else if (((dist == halfPoint + 1) || (dist == halfPoint - 2)) && (y < this.doorType.getHeight())) {
                blockToBuild = dungeon.getPlankBlockState();
            }
        }

        return blockToBuild;
    }

    private IBlockState getBlockGrandEntry(BlockPos pos, DungeonCastle dungeon) {
        IBlockState blockToBuild = dungeon.getMainBlockState();

        final int y = pos.getY() - this.origin.getY();
        final int dist = this.getLengthPoint(pos);
        final int halfPoint = this.doorStartOffset + (this.doorType.getWidth() / 2);
        final int distFromHalf = Math.abs(dist - halfPoint);

        final IBlockState outlineBlock = dungeon.getFancyBlockState();

        if (this.withinDoorWidth(dist)) {
            if (y == 0) {
                blockToBuild = dungeon.getMainBlockState();
            } else if (distFromHalf == 0) {
                if (y <= 3) {
                    return Blocks.AIR.getDefaultState();
                } else if (y == 4) {
                    return dungeon.getFenceBlockState();
                } else if (y == 5) {
                    return outlineBlock;
                }
            } else if (distFromHalf == 1) {
                if (y <= 2) {
                    return Blocks.AIR.getDefaultState();
                } else if (y == 3 || y == 4) {
                    return dungeon.getFenceBlockState();
                } else if (y == 5) {
                    return outlineBlock;
                }
            } else if (Math.abs(dist - halfPoint) == 2) {
                if (y <= 3) {
                    return dungeon.getFenceBlockState();
                } else if (y == 4 || y == 5) {
                    return outlineBlock;
                }
            } else if (Math.abs(dist - halfPoint) == 3) {
                if (y <= 4) {
                    return outlineBlock;
                }
            }
        }

        return blockToBuild;
    }

    protected IBlockState getWindowBlock(BlockPos pos, DungeonCastle dungeon) {
        switch (this.windowType) {
            case BASIC_GLASS:
                return getBlockWindowBasicGlass(pos, dungeon);
            case CROSS_GLASS:
                return getBlockWindowCrossGlass(pos, dungeon);
            case SQUARE_BARS:
                return getBlockWindowSquareBars(pos, dungeon);
            case OPEN_SLIT:
            default:
                return getBlockWindowOpenSlit(pos, dungeon);
        }
    }

    private IBlockState getBlockWindowBasicGlass(BlockPos pos, DungeonCastle dungeon) {
        int y = pos.getY() - this.origin.getY();
        int dist = this.getLengthPoint(pos);

        if ((y == 2 || y == 3) && (dist == this.length / 2)) {
            return Blocks.GLASS_PANE.getDefaultState();
        } else {
            return dungeon.getMainBlockState();
        }
    }

    private IBlockState getBlockWindowCrossGlass(BlockPos pos, DungeonCastle dungeon) {
        int y = pos.getY() - this.origin.getY();
        int dist = this.getLengthPoint(pos);
        int halfDist = this.length / 2;

        if ((dist == halfDist - 1 && y == 3) ||
                (dist == halfDist && y >= 2 && y <= 4) ||
                (dist == halfDist + 1 && y == 3)){
            return Blocks.GLASS_PANE.getDefaultState();
        } else {
            return dungeon.getMainBlockState();
        }
    }

    private IBlockState getBlockWindowSquareBars(BlockPos pos, DungeonCastle dungeon) {
        int y = pos.getY() - this.origin.getY();
        int dist = this.getLengthPoint(pos);
        int halfDist = length / 2;

        if (((y == 2) || (y == 3)) &&
                ((dist == halfDist) || (dist == halfDist + 1)))  {
            return Blocks.IRON_BARS.getDefaultState();
        } else {
            return dungeon.getMainBlockState();
        }
    }

    private IBlockState getBlockWindowOpenSlit(BlockPos pos, DungeonCastle dungeon) {
        int y = pos.getY() - this.origin.getY();
        int dist = this.getLengthPoint(pos);
        int halfDist = length / 2;

        if ((y == 2) && (dist >= halfDist - 1) && (dist <= halfDist + 1))  {
            return Blocks.AIR.getDefaultState();
        } else {
            return dungeon.getMainBlockState();
        }
    }

    public void addDoorCentered(EnumCastleDoorType type, Random random) {
        if (type == EnumCastleDoorType.RANDOM) {
            type = EnumCastleDoorType.getRandomRegularType(random);
        }
        this.doorType = type;
        this.doorStartOffset = (length - type.getWidth()) / 2;
    }

    public void addDoorRandom(EnumCastleDoorType type, Random random) {
        if (type == EnumCastleDoorType.RANDOM) {
            type = EnumCastleDoorType.getRandomRegularType(random);
        }
        this.doorType = type;
        this.doorStartOffset = 1 + random.nextInt(length - type.getWidth() - 1);
    }

    /*
     * Whether to build a door or window is usually determined by how far along the wall we are.
     * This function gets the relevant length along the wall based on if we are a horizontal
     * wall or a vertical wall.
     */
    protected int getLengthPoint(BlockPos pos) {
        if (this.orientation == WallOrientation.VERTICAL) {
            return pos.getZ() - this.origin.getZ();
        } else {
            return pos.getX() - this.origin.getX();
        }
    }

    protected boolean withinDoorWidth(int value) {
        int relativeToDoor = value - this.doorStartOffset;
        return (relativeToDoor >= 0 && relativeToDoor < this.doorType.getWidth());
    }
}

