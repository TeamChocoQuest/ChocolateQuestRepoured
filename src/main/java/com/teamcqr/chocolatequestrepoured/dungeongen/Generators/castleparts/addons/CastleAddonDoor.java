package com.teamcqr.chocolatequestrepoured.dungeongen.Generators.castleparts.addons;

import com.teamcqr.chocolatequestrepoured.util.BlockInfo;
import net.minecraft.init.Blocks;

import java.rmi.activation.ActivationGroup_Stub;
import java.util.ArrayList;

public class CastleAddonDoor implements ICastleAddon
{
    public enum Orientation
    {
        HORIZONTAL,
        VERTICAL
    }

    public enum DoorType
    {
        EMPTY,
        FENCE_BORDER
    }

    private int startX;
    private int startY;
    private int startZ;
    private int width;
    private int height;
    private Orientation orientation;
    private DoorType type;

    public CastleAddonDoor(int x, int y, int z, int width, int height, DoorType type)
    {
        this.startX = x;
        this.startY = y;
        this.startZ = z;
        this.width = width;
        this.height = height;
        this.type = type;
    }

    @Override
    public void generate(ArrayList<BlockInfo> blocks)
    {
        switch (type)
        {
            case FENCE_BORDER:
            {
                break;
            }
            case EMPTY:
            default:
            {
                generateEmpty(blocks);
                break;
            }
        }
    }

    private void generateEmpty(ArrayList<BlockInfo> blocks)
    {
        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                if (orientation == Orientation.HORIZONTAL)
                {
                    blocks.add(new BlockInfo(startX + i, startY + j, startZ, Blocks.AIR.getDefaultState()));
                }
                else //vertical
                {
                    blocks.add(new BlockInfo(startX, startY + j, startZ + i, Blocks.AIR.getDefaultState()));
                }
            }
        }
    }
}
