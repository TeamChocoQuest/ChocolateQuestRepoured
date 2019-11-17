package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.addons;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
import com.teamcqr.chocolatequestrepoured.util.BlockPlacement;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

public class CastleAddonRoof implements ICastleAddon
{
    public enum RoofType
    {
        TWOSIDED,
        FOURSIDED
    }

    private BlockPos startPos;
    private int sizeX;
    private int sizeZ;

    public CastleAddonRoof(BlockPos startPos, int sizeX, int sizeZ)
    {
        this.startPos = startPos;
        this.sizeX = sizeX;
        this.sizeZ = sizeZ;
    }

    @Override
    public void generate(World world, CastleDungeon dungeon)
    {
        RoofType type = RoofType.FOURSIDED;
        switch (type)
        {
            case TWOSIDED:
            {
                break;
            }
            case FOURSIDED:
            default:
            {
                generateFourSided(world, dungeon);
            }
        }
    }


    private void generateTwoSided(ArrayList<BlockPlacement> roofBlocks)
    {

    }

    private void generateFourSided(World world, CastleDungeon dungeon)
    {
        int roofX;
        int roofZ;
        int roofLenX;
        int roofLenZ;
        int underLenX = sizeX;
        int underLenZ = sizeZ;
        int x = startPos.getX();
        int y = startPos.getY();
        int z = startPos.getZ();

        do
        {
            // Add the foundation under the roof
            IBlockState state = Blocks.STONEBRICK.getDefaultState();
            for (int i = 0; i < underLenX; i++)
            {
                world.setBlockState(new BlockPos(x + i, y, z), state);
                world.setBlockState((new BlockPos(x + i, y, z + underLenZ - 1)), state);
            }
            for (int j = 0; j < underLenZ; j++)
            {
                world.setBlockState(new BlockPos(x, y, z + j), state);
                world.setBlockState(new BlockPos(x + underLenX - 1, y, z + j), state);
            }

            roofX = x - 1;
            roofZ = z - 1;
            roofLenX = underLenX + 2;
            roofLenZ = underLenZ + 2;

            //add the north row
            for (int i = 0; i < roofLenX; i++)
            {
                IBlockState blockState = Blocks.SPRUCE_STAIRS.getDefaultState();
                blockState = blockState.withProperty(BlockStairs.FACING, EnumFacing.SOUTH);

                //Apply properties to corner pieces
                if (i == 0)
                {
                    blockState = blockState.withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.INNER_LEFT);
                }
                else if (i == roofLenX - 1)
                {
                    blockState = blockState.withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.INNER_RIGHT);
                }

                world.setBlockState(new BlockPos(roofX + i, y, roofZ), blockState);
            }
            //add the south row
            for (int i = 0; i < roofLenX; i++)
            {
                IBlockState blockState = Blocks.SPRUCE_STAIRS.getDefaultState();
                blockState = blockState.withProperty(BlockStairs.FACING, EnumFacing.NORTH);

                //Apply properties to corner pieces
                if (i == 0)
                {
                    blockState = blockState.withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.INNER_RIGHT);
                }
                else if (i == roofLenX - 1)
                {
                    blockState = blockState.withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.INNER_LEFT);
                }

                world.setBlockState(new BlockPos(roofX + i, y, roofZ + roofLenZ - 1), blockState);
            }

            for (int i = 0; i < roofLenZ; i++)
            {
                IBlockState blockState = Blocks.SPRUCE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.EAST);
                world.setBlockState(new BlockPos(roofX, y, roofZ + i), blockState);

                blockState = Blocks.SPRUCE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST);

                world.setBlockState(new BlockPos(roofX + roofLenX - 1, y, roofZ + i), blockState);
            }

            x++;
            y++;
            z++;
            underLenX -= 2;
            underLenZ -= 2;
        } while (underLenX >= 0 && underLenZ >= 0);
    }
}
