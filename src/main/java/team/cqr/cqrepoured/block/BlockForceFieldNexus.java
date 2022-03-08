package team.cqr.cqrepoured.block;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.block.BlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.tileentity.TileEntityForceFieldNexus;

/**
 * Copyright (c) 25.05.2019 Developed by MrMarnic GitHub: https://github.com/MrMarnic
 */
public class BlockForceFieldNexus extends Block implements ITileEntityProvider {

	private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0, 0, 0, 1, 1.75, 1);

	public BlockForceFieldNexus() {
		super(Material.IRON);
		this.setSoundType(SoundType.METAL);
		this.setHardness(45.0F);
		this.setResistance(10.0F);
		this.setLightLevel(1F);
		this.setLightOpacity(10);
		this.setHarvestLevel("pickaxe", 3);
	}

	@Nullable
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityForceFieldNexus();
	}

	@Deprecated
	@Override
	public AxisAlignedBB getBoundingBox(BlockState state, IBlockAccess source, BlockPos pos) {
		return BOUNDING_BOX;
	}

	@Deprecated
	@Override
	public boolean isFullCube(BlockState state) {
		return false;
	}

	@Deprecated
	@Override
	public boolean isOpaqueCube(BlockState state) {
		return false;
	}

	@Override
	public Item getItemDropped(BlockState state, Random rand, int fortune) {
		return Items.AIR;
	}

	@Deprecated
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}

}
