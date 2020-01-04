package com.teamcqr.chocolatequestrepoured.objects.blocks;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.tileentity.TileEntityForceFieldNexus;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Copyright (c) 25.05.2019 Developed by MrMarnic GitHub:
 * https://github.com/MrMarnic
 */
public class BlockForceFieldNexus extends Block implements ITileEntityProvider {

	public BlockForceFieldNexus(Material materialIn) {
		super(materialIn);

		this.setSoundType(SoundType.METAL);
		this.setHardness(45.0F);
		this.setResistance(10.0F);
		this.setHarvestLevel("pickaxe", 3);
	}

	@Nullable
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		TileEntityForceFieldNexus tile = new TileEntityForceFieldNexus();
		return tile;
	}

	/*
	 * TODO implement nexus and determine if this is necessary
	 * 
	 * @Override
	 * public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
	 * super.onBlockHarvested(worldIn, pos, state, player);
	 * TileEntityForceFieldNexus nexus = (TileEntityForceFieldNexus) worldIn.getTileEntity(pos);
	 * if (nexus.hasData()) {
	 * nexus.getRegion().setEnabled(false);
	 * }
	 * }
	 */

}
