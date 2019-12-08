package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CastleRoomBossFoyer extends CastleRoom
{
    public CastleRoomBossFoyer(BlockPos startPos, int sideLength, int height)
    {
        super(startPos, sideLength, height);
        this.roomType = EnumRoomType.ARMORY;
        this.maxSlotsUsed = 2;
        this.defaultCeiling = true;
        this.defaultFloor = true;
    }

    @Override
    protected void generateRoom(World world, CastleDungeon dungeon)
    {

    }

    @Override
    protected IBlockState getFloorBlock(CastleDungeon dungeon)
    {
        return Blocks.QUARTZ_BLOCK.getDefaultState();
    }
}
