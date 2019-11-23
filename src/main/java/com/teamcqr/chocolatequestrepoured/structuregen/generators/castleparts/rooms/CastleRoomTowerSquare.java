package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
import com.teamcqr.chocolatequestrepoured.util.SpiralStaircaseBuilder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CastleRoomTowerSquare extends CastleRoom
{
    private static final int MIN_SIZE = 5;
    private EnumFacing connectedSide;
    private int stairYOffset;
    private BlockPos pillarStart;
    private EnumFacing firstStairSide;

    public CastleRoomTowerSquare(BlockPos startPos, int sideLength, int height,
                                 EnumFacing connectedSide, int towerSize, CastleRoomTowerSquare towerBelow)
    {
        super(startPos, sideLength, height);
        this.roomType = EnumRoomType.TOWER_SQUARE;
        this.connectedSide = connectedSide;
        this.buildLength = towerSize;
        this.defaultFloor = false;
        this.defaultCeiling = false;
        this.pathable = false;
        this.isTower = true;

        if (connectedSide == EnumFacing.NORTH || connectedSide == EnumFacing.SOUTH)
        {
            offsetX += (sideLength - buildLength) / 2;
            if (connectedSide == EnumFacing.SOUTH)
            {
                offsetZ += sideLength - buildLength;
            }
        }
        if (connectedSide == EnumFacing.WEST || connectedSide == EnumFacing.EAST)
        {
            offsetZ += (sideLength - buildLength) / 2;
            if (connectedSide == EnumFacing.EAST)
            {
                offsetX += sideLength - buildLength;
            }
        }

        if (towerBelow != null)
        {
            this.firstStairSide = towerBelow.getLastStairSide().rotateY();
            stairYOffset = 0; //stairs must continue from room below so start building in the floor
        }
        else
        {
            this.firstStairSide = this.connectedSide.rotateY(); //makes stairs face door
            stairYOffset = 1; //account for 1 layer of floor
        }

        this.pillarStart = startPos.add((offsetX + buildLength / 2), stairYOffset, (offsetZ + buildLength / 2));

        for (EnumFacing side: EnumFacing.HORIZONTALS)
        {
            this.walls.addOuter(side);
        }
    }

    @Override
    public void generateRoom(World world, CastleDungeon dungeon)
    {
        SpiralStaircaseBuilder stairs = new SpiralStaircaseBuilder(pillarStart, firstStairSide, dungeon.getWallBlock(), dungeon.getStairBlock());

        BlockPos pos;
        IBlockState blockToBuild;

        for (int x = 0; x < buildLength; x++)
        {
            for (int z = 0; z < buildLength; z++)
            {
                for (int y = 0; y < height; y++)
                {
                    blockToBuild = Blocks.AIR.getDefaultState();
                    pos = startPos.add(x + offsetX, y, z + offsetZ);

                    if (stairs.isPartOfStairs(pos))
                    {
                        blockToBuild = stairs.getBlock(pos);
                    }
                    else if (y == 0)
                    {
                        blockToBuild = dungeon.getFloorBlock().getDefaultState();
                    }
                    else if (y == height - 1)
                    {
                        blockToBuild = dungeon.getWallBlock().getDefaultState();
                    }

                    world.setBlockState(pos, blockToBuild);
                }
            }
        }
    }

    public EnumFacing getLastStairSide()
    {
        EnumFacing result = firstStairSide;
        for (int i = stairYOffset; i < height - 1; i++)
        {
            result = result.rotateY();
        }
        return result;
    }

    @Override
    public boolean isTower()
    {
        return true;
    }
}
