package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonCastle;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.RoomDecorTypes;
import com.teamcqr.chocolatequestrepoured.util.BlockStateGenArray;
import com.teamcqr.chocolatequestrepoured.util.GenerationTemplate;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CastleRoomJailCell extends CastleRoomDecoratedBase {
    private EnumFacing doorSide;

    public CastleRoomJailCell(int sideLength, int height, int floor) {
        super(sideLength, height, floor);
        this.roomType = EnumRoomType.JAIL;
        this.defaultCeiling = true;
        this.defaultFloor = true;
        this.doorSide = EnumFacing.HORIZONTALS[random.nextInt(EnumFacing.HORIZONTALS.length)];
    }

    @Override
    boolean shouldBuildEdgeDecoration() {
        return false;
    }

    @Override
    boolean shouldBuildWallDecoration() {
        return true;
    }

    @Override
    boolean shouldBuildMidDecoration() {
        return false;
    }

    @Override
    boolean shouldAddSpawners() {
        return false;
    }

    @Override
    boolean shouldAddChests() {
        return false;
    }

    @Override
    protected void generateRoom(BlockPos castleOrigin, BlockStateGenArray genArray, DungeonCastle dungeon) {
        int endX = getDecorationLengthX() - 1;
        int endZ = getDecorationLengthZ() - 1;

        Predicate<Vec3i> northRow = (v -> ((v.getZ() == 1) && ((v.getX() >= 1) && (v.getX() <= endX - 1))));
        Predicate<Vec3i> southRow = (v -> ((v.getZ() == endZ - 1) && ((v.getX() >= 1) && (v.getX() <= endX - 1))));
        Predicate<Vec3i> westRow = (v -> ((v.getX() == 1) && ((v.getZ() >= 1) && (v.getZ() <= endZ - 1))));
        Predicate<Vec3i> eastRow = (v -> ((v.getX() == endZ - 1) && ((v.getZ() >= 1) && (v.getZ() <= endZ - 1))));

        GenerationTemplate template = new GenerationTemplate(getDecorationLengthX(), getDecorationLengthY(), getDecorationLengthZ());
        //here we take advantage of the fact that rules added to the template earlier will take priority
        //so we add in the order of door -> frame -> cell

        if (doorSide == EnumFacing.NORTH) {
            int half = getDecorationLengthX() / 2;
            Predicate<Vec3i> doorFrame = (v -> ((v.getY() >= 0) && (v.getY() <= 2)  && (v.getZ() == 1) && (v.getX() >= half - 1) && (v.getX() <= half + 2)));
            Predicate<Vec3i> doorLower1 = (v -> ((v.getY() == 0) && (v.getZ() == 1) && (v.getX() == half)));
            Predicate<Vec3i> doorLower2 = (v -> ((v.getY() == 0) && (v.getZ() == 1) && (v.getX() == half + 1)));
            Predicate<Vec3i> doorUpper1 = (v -> ((v.getY() == 1) && (v.getZ() == 1) && (v.getX() == half)));
            Predicate<Vec3i> doorUpper2 = (v -> ((v.getY() == 1) && (v.getZ() == 1) && (v.getX() == half + 1)));

            template.addRule(doorLower1, Blocks.IRON_DOOR.getDefaultState().
                    withProperty(BlockDoor.FACING, EnumFacing.SOUTH).
                    withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.LOWER).
                    withProperty(BlockDoor.HINGE, BlockDoor.EnumHingePosition.LEFT));
            template.addRule(doorLower2, Blocks.IRON_DOOR.getDefaultState().
                    withProperty(BlockDoor.FACING, EnumFacing.SOUTH).
                    withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.LOWER).
                    withProperty(BlockDoor.HINGE, BlockDoor.EnumHingePosition.RIGHT));
            template.addRule(doorUpper1, Blocks.IRON_DOOR.getDefaultState().
                    withProperty(BlockDoor.FACING, EnumFacing.SOUTH).
                    withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER).
                    withProperty(BlockDoor.HINGE, BlockDoor.EnumHingePosition.LEFT));
            template.addRule(doorUpper2, Blocks.IRON_DOOR.getDefaultState().
                    withProperty(BlockDoor.FACING, EnumFacing.SOUTH).
                    withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER).
                    withProperty(BlockDoor.HINGE, BlockDoor.EnumHingePosition.RIGHT));
            template.addRule(doorFrame, Blocks.IRON_BLOCK.getDefaultState());
        } else if (doorSide == EnumFacing.SOUTH) {
            int half = getDecorationLengthX() / 2;
            Predicate<Vec3i> doorFrame = (v -> ((v.getY() >= 0) && (v.getY() <= 2)  && (v.getZ() == endZ - 1) && (v.getX() >= half - 1) && (v.getX() <= half + 2)));
            Predicate<Vec3i> doorLower1 = (v -> ((v.getY() == 0) && (v.getZ() == endZ - 1) && (v.getX() == half)));
            Predicate<Vec3i> doorLower2 = (v -> ((v.getY() == 0) && (v.getZ() == endZ - 1) && (v.getX() == half + 1)));
            Predicate<Vec3i> doorUpper1 = (v -> ((v.getY() == 1) && (v.getZ() == endZ - 1) && (v.getX() == half)));
            Predicate<Vec3i> doorUpper2 = (v -> ((v.getY() == 1) && (v.getZ() == endZ - 1) && (v.getX() == half + 1)));

            template.addRule(doorLower1, Blocks.IRON_DOOR.getDefaultState().
                    withProperty(BlockDoor.FACING, EnumFacing.NORTH).
                    withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.LOWER).
                    withProperty(BlockDoor.HINGE, BlockDoor.EnumHingePosition.RIGHT));
            template.addRule(doorLower2, Blocks.IRON_DOOR.getDefaultState().
                    withProperty(BlockDoor.FACING, EnumFacing.NORTH).
                    withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.LOWER).
                    withProperty(BlockDoor.HINGE, BlockDoor.EnumHingePosition.LEFT));
            template.addRule(doorUpper1, Blocks.IRON_DOOR.getDefaultState().
                    withProperty(BlockDoor.FACING, EnumFacing.NORTH).
                    withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER).
                    withProperty(BlockDoor.HINGE, BlockDoor.EnumHingePosition.RIGHT));
            template.addRule(doorUpper2, Blocks.IRON_DOOR.getDefaultState().
                    withProperty(BlockDoor.FACING, EnumFacing.NORTH).
                    withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER).
                    withProperty(BlockDoor.HINGE, BlockDoor.EnumHingePosition.LEFT));
            template.addRule(doorFrame, Blocks.IRON_BLOCK.getDefaultState());
        } else if (doorSide == EnumFacing.WEST) {
            int half = getDecorationLengthZ() / 2;
            Predicate<Vec3i> doorFrame = (v -> ((v.getY() >= 0) && (v.getY() <= 2)  && (v.getX() == 1) && (v.getZ() >= half - 1) && (v.getZ() <= half + 2)));
            Predicate<Vec3i> doorLower1 = (v -> ((v.getY() == 0) && (v.getX() == 1) && (v.getZ() == half)));
            Predicate<Vec3i> doorLower2 = (v -> ((v.getY() == 0) && (v.getX() == 1) && (v.getZ() == half + 1)));
            Predicate<Vec3i> doorUpper1 = (v -> ((v.getY() == 1) && (v.getX() == 1) && (v.getZ() == half)));
            Predicate<Vec3i> doorUpper2 = (v -> ((v.getY() == 1) && (v.getX() == 1) && (v.getZ() == half + 1)));

            template.addRule(doorLower1, Blocks.IRON_DOOR.getDefaultState().
                    withProperty(BlockDoor.FACING, EnumFacing.EAST).
                    withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.LOWER).
                    withProperty(BlockDoor.HINGE, BlockDoor.EnumHingePosition.RIGHT));
            template.addRule(doorLower2, Blocks.IRON_DOOR.getDefaultState().
                    withProperty(BlockDoor.FACING, EnumFacing.EAST).
                    withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.LOWER).
                    withProperty(BlockDoor.HINGE, BlockDoor.EnumHingePosition.LEFT));
            template.addRule(doorUpper1, Blocks.IRON_DOOR.getDefaultState().
                    withProperty(BlockDoor.FACING, EnumFacing.EAST).
                    withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER).
                    withProperty(BlockDoor.HINGE, BlockDoor.EnumHingePosition.RIGHT));
            template.addRule(doorUpper2, Blocks.IRON_DOOR.getDefaultState().
                    withProperty(BlockDoor.FACING, EnumFacing.EAST).
                    withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER).
                    withProperty(BlockDoor.HINGE, BlockDoor.EnumHingePosition.LEFT));
            template.addRule(doorFrame, Blocks.IRON_BLOCK.getDefaultState());
        } else if (doorSide == EnumFacing.EAST) {
            int half = getDecorationLengthZ() / 2;
            Predicate<Vec3i> doorFrame = (v -> ((v.getY() >= 0) && (v.getY() <= 2)  && (v.getX() == endX - 1) && (v.getZ() >= half - 1) && (v.getZ() <= half + 2)));
            Predicate<Vec3i> doorLower1 = (v -> ((v.getY() == 0) && (v.getX() == endX - 1) && (v.getZ() == half)));
            Predicate<Vec3i> doorLower2 = (v -> ((v.getY() == 0) && (v.getX() == endX - 1) && (v.getZ() == half + 1)));
            Predicate<Vec3i> doorUpper1 = (v -> ((v.getY() == 1) && (v.getX() == endX - 1) && (v.getZ() == half)));
            Predicate<Vec3i> doorUpper2 = (v -> ((v.getY() == 1) && (v.getX() == endX - 1) && (v.getZ() == half + 1)));

            template.addRule(doorLower1, Blocks.IRON_DOOR.getDefaultState().
                    withProperty(BlockDoor.FACING, EnumFacing.WEST).
                    withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.LOWER).
                    withProperty(BlockDoor.HINGE, BlockDoor.EnumHingePosition.LEFT));
            template.addRule(doorLower2, Blocks.IRON_DOOR.getDefaultState().
                    withProperty(BlockDoor.FACING, EnumFacing.WEST).
                    withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.LOWER).
                    withProperty(BlockDoor.HINGE, BlockDoor.EnumHingePosition.RIGHT));
            template.addRule(doorUpper1, Blocks.IRON_DOOR.getDefaultState().
                    withProperty(BlockDoor.FACING, EnumFacing.WEST).
                    withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER).
                    withProperty(BlockDoor.HINGE, BlockDoor.EnumHingePosition.LEFT));
            template.addRule(doorUpper2, Blocks.IRON_DOOR.getDefaultState().
                    withProperty(BlockDoor.FACING, EnumFacing.WEST).
                    withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER).
                    withProperty(BlockDoor.HINGE, BlockDoor.EnumHingePosition.RIGHT));
            template.addRule(doorFrame, Blocks.IRON_BLOCK.getDefaultState());
        }

        template.addRule(northRow, Blocks.IRON_BARS.getDefaultState());
        template.addRule(southRow, Blocks.IRON_BARS.getDefaultState());
        template.addRule(westRow, Blocks.IRON_BARS.getDefaultState());
        template.addRule(eastRow, Blocks.IRON_BARS.getDefaultState());

        HashMap<BlockPos, IBlockState> genMap = template.GetGenerationMap(getDecorationStartPos(), true);
        genArray.addBlockStateMap(genMap, BlockStateGenArray.GenerationPhase.MAIN, BlockStateGenArray.EnumPriority.MEDIUM);
        for (Map.Entry<BlockPos, IBlockState> entry : genMap.entrySet()) {
            if (entry.getValue().getBlock() != Blocks.AIR) {
                usedDecoPositions.add(entry.getKey());
            }
        }
    }
}
