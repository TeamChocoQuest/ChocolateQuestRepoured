package com.teamcqr.chocolatequestrepoured.objects.blocks;

import java.util.Random;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.tileentity.TileEntityForceFieldNexus;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Copyright (c) 25.05.2019 Developed by MrMarnic GitHub:
 * https://github.com/MrMarnic
 */
public class BlockForceFieldNexus extends Block implements ITileEntityProvider {
	
	protected final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0, 0, 0, 1, 1.75, 1);

	public BlockForceFieldNexus(Material materialIn) {
		super(materialIn);

		this.setSoundType(SoundType.METAL);
		this.setHardness(45.0F);
		this.setResistance(10.0F);
		this.setLightLevel(7F);
		this.setLightOpacity(10);
		this.setHarvestLevel("pickaxe", 3);
	}

	@Nullable
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		TileEntityForceFieldNexus tile = new TileEntityForceFieldNexus();
		return tile;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return BOUNDING_BOX;
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
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Items.AIR;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

}
