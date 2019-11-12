package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.addons;

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
        WALKABLE,
        TWOSIDED,
        FOURSIDED
    }

    private int xStart;
    private int yStart;
    private int zStart;
    private int sizeX;
    private int sizeZ;
    private EnumFacing structFacing;
    private RoofType type;

    public CastleAddonRoof(int x, int y, int z, int sizeX, int sizeZ, RoofType type)
    {
        this.xStart = x;
        this.yStart = y;
        this.zStart = z;
        this.sizeX = sizeX;
        this.sizeZ = sizeZ;
        this.type = type;
    }

    public CastleAddonRoof(int x, int y, int z, int sizeX, int sizeZ, RoofType type, EnumFacing facing)
    {
        this.xStart = x;
        this.yStart = y;
        this.zStart = z;
        this.sizeX = sizeX;
        this.sizeZ = sizeZ;
        this.type = type;
        this.structFacing = facing;
    }

    @Override
    public void generate(World world)
    {
        ArrayList<BlockPlacement> roofBlocks = new ArrayList<>();

        switch (type)
        {
            case WALKABLE:
            {
                generateWalkable(roofBlocks);
            }
            case TWOSIDED:
            {
                break;
            }
            case FOURSIDED:
            default:
            {
                generateFourSided(roofBlocks);
            }
        }

        for (BlockPlacement block : roofBlocks)
        {
            block.build(world);
        }
    }

    private void generateWalkable(ArrayList<BlockPlacement> roofBlocks)
    {
        IBlockState state = Blocks.STONEBRICK.getDefaultState();
        int x = xStart;
        int y = yStart;
        int z = zStart;

        for (int i = 0; i < sizeX - 1; i++)
        {
            if (this.structFacing != EnumFacing.SOUTH)
            {
                roofBlocks.add(new BlockPlacement(new BlockPos(x + i, y, z), state));
            }
            if (this.structFacing != EnumFacing.NORTH)
            {
                roofBlocks.add(new BlockPlacement(new BlockPos(x + i, y, z + sizeZ - 2), state));
            }
            if (i % 2 == 0 || i == sizeX - 1)
            {
                if (this.structFacing != EnumFacing.SOUTH)
                {
                    roofBlocks.add(new BlockPlacement(new BlockPos(x + i, y + 1, z), state));
                }
                if (this.structFacing != EnumFacing.NORTH)
                {
                    roofBlocks.add(new BlockPlacement(new BlockPos(x + i, y + 1, z + sizeZ - 2), state));
                }
            }
        }
        for (int i = 0; i < sizeZ - 1; i++)
        {
            if (this.structFacing != EnumFacing.EAST)
            {
                roofBlocks.add(new BlockPlacement(new BlockPos(x, y, z + i), state));
            }
            if (this.structFacing != EnumFacing.WEST)
            {
                roofBlocks.add(new BlockPlacement(new BlockPos(x + sizeX - 2, y, z + i), state));
            }
            if (i % 2 == 0 || i == sizeZ - 1)
            {
                if (this.structFacing != EnumFacing.EAST)
                {
                    roofBlocks.add(new BlockPlacement(new BlockPos(x, y + 1, z + i), state));
                }
                if (this.structFacing != EnumFacing.WEST)
                {
                    roofBlocks.add(new BlockPlacement(new BlockPos(x + sizeX - 2, y + 1, z + i), state));
                }
            }
        }
    }

    private void generateTwoSided(ArrayList<BlockPlacement> roofBlocks)
    {

    }

    private void generateFourSided(ArrayList<BlockPlacement> roofBlocks)
    {
        int roofX;
        int roofZ;
        int roofLenX;
        int roofLenZ;
        int underLenX = sizeX;
        int underLenZ = sizeZ;
        int x = xStart;
        int y = yStart;
        int z = zStart;

        do
        {
            // Add the foundation under the roof
            IBlockState state = Blocks.STONEBRICK.getDefaultState();
            for (int i = 0; i < underLenX; i++)
            {
                roofBlocks.add(new BlockPlacement(new BlockPos(x + i, y, z), state));
                roofBlocks.add(new BlockPlacement((new BlockPos(x + i, y, z + underLenZ - 1)), state));
            }
            for (int j = 0; j < underLenZ; j++)
            {
                roofBlocks.add(new BlockPlacement(new BlockPos(x, y, z + j), state));
                roofBlocks.add(new BlockPlacement(new BlockPos(x + underLenX - 1, y, z + j), state));
            }

            roofX = x - 1;
            roofZ = z - 1;
            roofLenX = underLenX + 2;
            roofLenZ = underLenZ + 2;

            //add the north row
            for (int i = 0; i < roofLenX; i++)
            {
                BlockPlacement bInfo = new BlockPlacement(new BlockPos(roofX + i, y, roofZ), Blocks.SPRUCE_STAIRS.getDefaultState());
                bInfo.applyProperty(BlockStairs.FACING, EnumFacing.SOUTH);

                //Apply properties to corner pieces
                if (i == 0)
                {
                    bInfo.applyProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.INNER_LEFT);
                }
                else if (i == roofLenX - 1)
                {
                    bInfo.applyProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.INNER_RIGHT);
                }

                roofBlocks.add(bInfo);
            }
            //add the south row
            for (int i = 0; i < roofLenX; i++)
            {
                BlockPlacement bInfo = new BlockPlacement(new BlockPos(roofX + i, y, roofZ + roofLenZ - 1), Blocks.SPRUCE_STAIRS.getDefaultState());
                bInfo.applyProperty(BlockStairs.FACING, EnumFacing.NORTH);

                //Apply properties to corner pieces
                if (i == 0)
                {
                    bInfo.applyProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.INNER_RIGHT);
                }
                else if (i == roofLenX - 1)
                {
                    bInfo.applyProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.INNER_LEFT);
                }
                roofBlocks.add(bInfo);
            }

            for (int i = 0; i < roofLenZ; i++)
            {
                BlockPlacement bInfo = new BlockPlacement(new BlockPos(roofX, y, roofZ + i), Blocks.SPRUCE_STAIRS.getDefaultState());
                bInfo.applyProperty(BlockStairs.FACING, EnumFacing.EAST);

                roofBlocks.add(bInfo);

                bInfo = new BlockPlacement(new BlockPos(roofX + roofLenX - 1, y, roofZ + i), Blocks.SPRUCE_STAIRS.getDefaultState());
                bInfo.applyProperty(BlockStairs.FACING, EnumFacing.WEST);

                roofBlocks.add(bInfo);
            }

            x++;
            y++;
            z++;
            underLenX -= 2;
            underLenZ -= 2;
        } while (underLenX >= 0 && underLenZ >= 0);
    }
}
