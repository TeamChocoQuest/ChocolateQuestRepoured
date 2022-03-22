package team.cqr.cqrepoured.block;

import java.util.Random;

import javax.annotation.Nullable;

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
import team.cqr.cqrepoured.tileentity.TileEntityForceFieldNexus;

/**
 * Copyright (c) 25.05.2019 Developed by MrMarnic GitHub: https://github.com/MrMarnic
 */
public class BlockForceFieldNexus extends Block implements ITileEntityProvider {

	private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0, 0, 0, 1, 1.75, 1);

	public BlockForceFieldNexus() {
		super(Material.ROCK);
		this.setSoundType(SoundType.STONE);
		this.setHardness(4.0F);
		this.setResistance(15.0F);
		this.setLightLevel(1.0F);
	}

	@Nullable
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityForceFieldNexus();
	}

	@Deprecated
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return BOUNDING_BOX;
	}

	@Deprecated
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Deprecated
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Items.AIR;
	}

	@Deprecated
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}

}
