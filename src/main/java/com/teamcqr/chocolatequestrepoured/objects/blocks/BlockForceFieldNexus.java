package com.teamcqr.chocolatequestrepoured.objects.blocks;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.dungeongen.protection.ProtectionHandler;
import com.teamcqr.chocolatequestrepoured.init.ModBlocks;
import com.teamcqr.chocolatequestrepoured.init.ModItems;
import com.teamcqr.chocolatequestrepoured.objects.base.BlockBase;
import com.teamcqr.chocolatequestrepoured.tileentity.TileEntityForceFieldNexus;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Copyright (c) 25.05.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class BlockForceFieldNexus extends BlockContainer {
    public BlockForceFieldNexus(String name,Material materialIn) {
        super(materialIn);
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(CQRMain.CQRBlocksTab);

        setBlockUnbreakable();
        setResistance(100);
        setHardness(5);
        setSoundType(SoundType.METAL);

        ModBlocks.BLOCKS.add(this);
        ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        TileEntityForceFieldNexus tile = new TileEntityForceFieldNexus();
        return tile;
    }
}
