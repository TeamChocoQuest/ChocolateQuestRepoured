package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.objects.factories.CastleGearedMobFactory;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
import com.teamcqr.chocolatequestrepoured.util.BlockStateGenArray;
import com.teamcqr.chocolatequestrepoured.util.CQRWeightedRandom;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class CastleRoomPool extends CastleRoomDecoratedBase
{
    public CastleRoomPool(BlockPos startOffset, int sideLength, int height, int floor) {
        super(startOffset, sideLength, height, floor);
        this.roomType = EnumRoomType.POOL;
        this.maxSlotsUsed = 1;
        this.defaultCeiling = true;
        this.defaultFloor = true;
    }

    @Override
    protected void generateRoom(BlockStateGenArray genArray, CastleDungeon dungeon) {
        int endX = getDecorationLengthX() - 1;
        int endZ = getDecorationLengthZ() - 1;
        Predicate<Vec3i> northRow = (vec3i -> ((vec3i.getY() == 0) && (vec3i.getZ() == 1) && ((vec3i.getX() >= 1) && (vec3i.getX() <= endX - 1))));
        Predicate<Vec3i> southRow = (vec3i -> ((vec3i.getY() == 0) && (vec3i.getZ() == endZ - 1) && ((vec3i.getX() >= 1) && (vec3i.getX() <= endX - 1))));
        Predicate<Vec3i> westRow = (vec3i -> ((vec3i.getY() == 0) && (vec3i.getX() == 1) && ((vec3i.getZ() >= 1) && (vec3i.getZ() <= endZ - 1))));
        Predicate<Vec3i> eastRow = (vec3i -> ((vec3i.getY() == 0) && (vec3i.getX() == endZ - 1) && ((vec3i.getZ() >= 1) && (vec3i.getZ() <= endZ - 1))));
        Predicate<Vec3i> water = (vec3i -> ((vec3i.getY() == 0) && (vec3i.getX() > 1) && (vec3i.getX() < endX - 1) && (vec3i.getZ() > 1) && (vec3i.getZ() < endZ - 1)));

        for (int x = 0; x < this.getDecorationLengthX(); x++) {
            for (int z = 0; z < this.getDecorationLengthZ(); z++) {
                for (int y = 0; y < this.getDecorationLengthY(); y++) {
                    IBlockState blockToBuild;

                    Vec3i offset = new Vec3i(x, y, z);
                    if (northRow.test(offset)) {
                        blockToBuild = Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH);
                    } else if (southRow.test(offset)) {
                        blockToBuild = Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH);
                    } else if (westRow.test(offset)) {
                        blockToBuild = Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.EAST);
                    } else if (eastRow.test(offset)) {
                        blockToBuild = Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST);
                    } else if (water.test(offset)) {
                        blockToBuild = Blocks.WATER.getDefaultState();
                    } else {
                        blockToBuild = Blocks.AIR.getDefaultState();
                    }

                    genArray.addBlockState(this.getDecorationStartPos().add(offset), blockToBuild, BlockStateGenArray.GenerationPhase.MAIN);
                    if (blockToBuild.getBlock() != Blocks.AIR) {
                        this.usedDecoPositions.add(this.getDecorationStartPos().add(offset));
                    }
                }
            }
        }

    }

    @Override
    protected IBlockState getFloorBlock(CastleDungeon dungeon) {
        return dungeon.getWallBlock().getDefaultState();
    }

    @Override
    public void decorate(World world, BlockStateGenArray genArray, CastleDungeon dungeon, CastleGearedMobFactory mobFactory)
    {
        setupDecoration(genArray);
        addWallDecoration(world, genArray, dungeon);
        addSpawners(world, genArray, dungeon, mobFactory);
        fillEmptySpaceWithAir(genArray);
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
        return true;
    }

    @Override
    boolean shouldAddChests() {
        return false;
    }
}
