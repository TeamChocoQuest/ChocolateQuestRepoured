package com.teamcqr.chocolatequestrepoured.objects.blocks;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.objects.base.BlockBase;
import com.teamcqr.chocolatequestrepoured.tileentity.TileEntityForceFieldNexus;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Copyright (c) 25.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class BlockForceFieldNexus extends BlockBase implements ITileEntityProvider {
    public BlockForceFieldNexus(String name,Material materialIn) {
        super(name,materialIn);

        setHardness(45);
        setHarvestLevel("pickaxe",3);
        setSoundType(SoundType.METAL);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        TileEntityForceFieldNexus tile = new TileEntityForceFieldNexus();
        return tile;
    }
/* TODO implement nexus and determine if this is necessary
    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        super.onBlockHarvested(worldIn, pos, state, player);
        TileEntityForceFieldNexus nexus = (TileEntityForceFieldNexus)worldIn.getTileEntity(pos);
        if(nexus.hasData()) {
            nexus.getRegion().setEnabled(false);
        }
    }
*/
    @Override
    public void registerModels() {
        CQRMain.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }
}
