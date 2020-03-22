package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.objects.factories.CastleGearedMobFactory;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
import com.teamcqr.chocolatequestrepoured.util.BlockStateGenArray;
import com.teamcqr.chocolatequestrepoured.util.CQRWeightedRandom;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Collections;

public class CastleRoomLibrary extends CastleRoomDecoratedBase
{
    private enum ShelfPattern {
        LONG_VERTICAL,
        LONG_HORIZONTAL
    }

    private ShelfPattern pattern = null;
    private BlockPos shelfStart = null;
    private int shelfXLen = 0;
    private int shelfZLen = 0;
    private int shelfHeight = 0;

    public CastleRoomLibrary(BlockPos startOffset, int sideLength, int height, int floor) {
        super(startOffset, sideLength, height, floor);
        this.roomType = EnumRoomType.LIBRARY;
        this.maxSlotsUsed = 2;
        this.defaultCeiling = true;
        this.defaultFloor = true;

        CQRWeightedRandom<ShelfPattern> randomPattern = new CQRWeightedRandom<>(random);
        randomPattern.add(ShelfPattern.LONG_VERTICAL, 1);
        randomPattern.add(ShelfPattern.LONG_HORIZONTAL, 1);

        this.pattern = randomPattern.next();
    }

    @Override
    protected void generateRoom(World world, BlockStateGenArray genArray, CastleDungeon dungeon) {
        //allow 1 space from the wall to start
        shelfStart = this.getDecorationStartPos().south().east();
        shelfXLen = this.getDecorationLengthX() - 2;
        shelfZLen = this.getDecorationLengthZ() - 2;
        shelfHeight = this.getDecorationLengthY() - 2; //leave some room to the ceiling

        if (this.hasDoorOnSide(EnumFacing.WEST)) {
            shelfStart = shelfStart.east();
            --shelfXLen;
        }
        if (this.hasDoorOnSide(EnumFacing.EAST)) {
            --shelfXLen;
        }
        if (this.hasDoorOnSide(EnumFacing.NORTH)) {
            shelfStart = shelfStart.south();
            --shelfZLen;
        }
        if (this.hasDoorOnSide(EnumFacing.SOUTH)) {
            --shelfZLen;
        }

        switch (pattern) {
            case LONG_VERTICAL:
                generateVertical(genArray);
                break;
            case LONG_HORIZONTAL:
                generateHorizontal(genArray);
                break;
            default:
                break;
        }


    }

    @Override
    public void decorate(World world, BlockStateGenArray genArray, CastleDungeon dungeon, CastleGearedMobFactory mobFactory)
    {
        setupDecoration(genArray);
        addWallDecoration(world, genArray, dungeon);
        addSpawners(world, genArray, dungeon, mobFactory);
        fillEmptySpaceWithAir(genArray);
    }

    private void generateVertical(BlockStateGenArray genArray) {
        for (int x = 0; x < shelfXLen; x++) {
            for (int y = 0; y < shelfHeight; y++) {
                for (int z = 0; z < shelfZLen; z++) {
                    if ((x % 2 == 0) && (z != (shelfZLen / 2))) {
                        BlockPos pos = shelfStart.add(x, y, z);
                        genArray.addBlockState(pos, Blocks.BOOKSHELF.getDefaultState(), BlockStateGenArray.GenerationPhase.MAIN);
                        this.usedDecoPositions.add(pos);
                    }
                }
            }
        }
    }

    private void generateHorizontal(BlockStateGenArray genArray) {
        for (int x = 0; x < shelfXLen; x++) {
            for (int y = 0; y < shelfHeight; y++) {
                for (int z = 0; z < shelfZLen; z++) {
                    if ((z % 2 == 0) && (x != (shelfXLen / 2))) {
                        BlockPos pos = shelfStart.add(x, y, z);
                        genArray.addBlockState(pos, Blocks.BOOKSHELF.getDefaultState(), BlockStateGenArray.GenerationPhase.MAIN);
                        this.usedDecoPositions.add(pos);
                    }
                }
            }
        }
    }
}
