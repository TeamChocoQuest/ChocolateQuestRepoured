package com.teamcqr.chocolatequestrepoured.objects.blocks;

import java.util.Random;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.init.ModItems;
import com.teamcqr.chocolatequestrepoured.tileentity.TileEntitySpawner;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class BlockSpawner extends BlockContainer {

	public BlockSpawner() {
		super(Material.IRON);

		setSoundType(SoundType.METAL);
		setBlockUnbreakable();
		setResistance(Float.MAX_VALUE);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote && playerIn.isCreative()) {
			if(playerIn.getHeldItem(hand).getItem() == ModItems.SPAWNER_CONVERTER) {
				return false;
			} else {
				playerIn.openGui(CQRMain.INSTANCE, Reference.SPAWNER_GUI_ID, worldIn, pos.getX(), pos.getY(), pos.getZ());
			}
			
		}
		return true;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntitySpawner tile = (TileEntitySpawner) world.getTileEntity(pos);
		IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);

		for (int i = 0; i < itemHandler.getSlots(); i++) {
			ItemStack stack = itemHandler.getStackInSlot(i);

			if (!stack.isEmpty()) {
				EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack);
				world.spawnEntity(item);
			}
		}
		super.breakBlock(world, pos, state);
	}

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
	public int quantityDropped(Random random) {
		return 0;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntitySpawner();
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return ItemStack.EMPTY;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

}
