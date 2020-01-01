package com.teamcqr.chocolatequestrepoured.objects.blocks;

import com.teamcqr.chocolatequestrepoured.tileentity.TileEntityTable;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class BlockTable extends Block implements ITileEntityProvider {

	public static final PropertyBool TOP = PropertyBool.create("top");

	public static final AxisAlignedBB TABLE_AABB = new AxisAlignedBB(0.0D, 0.8125D, 0.0D, 1.0D, 1.0D, 1.0D);
	public static final AxisAlignedBB TABLE_TOP_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

	public BlockTable() {
		super(Material.WOOD);

		setSoundType(SoundType.WOOD);
		setHardness(2.0F);
		setResistance(15.0F);
		setHarvestLevel("axe", 0);
		setDefaultState(blockState.getBaseState().withProperty(TOP, false));
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
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		if (state.getValue(TOP) == true) {
			return TABLE_AABB;
		} else {
			return TABLE_TOP_AABB;
		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(TOP, ((meta & 1) != 0));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		if (state.getValue(TOP) == true) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { TOP });
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		@SuppressWarnings("unused")
		IBlockState blockstate = worldIn.getBlockState(pos);
		state = this.getDefaultState();

		if (worldIn.getBlockState(pos.west()).getBlock() == this
				&& worldIn.getBlockState(pos.east()).getBlock() == this) {
			worldIn.setBlockState(pos, state.withProperty(TOP, true));
		}

		else if (worldIn.getBlockState(pos.north()).getBlock() == this
				&& worldIn.getBlockState(pos.south()).getBlock() == this) {
			worldIn.setBlockState(pos, state.withProperty(TOP, true));
		}

		else {
			worldIn.setBlockState(pos, state.withProperty(TOP, false));
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		TileEntityTable tile = getTileEntity(worldIn, pos);
		ItemStack helditem = playerIn.getHeldItem(EnumHand.MAIN_HAND);
		IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
		ItemStack table = itemHandler.getStackInSlot(0);

		if (table.isEmpty() && !helditem.isEmpty()) {
			worldIn.playSound(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, SoundEvents.ENTITY_ITEM_PICKUP,
					SoundCategory.BLOCKS, 0.5F, worldIn.rand.nextFloat() * 0.4F + 0.8F, false);
			if (!worldIn.isRemote) {
				playerIn.setHeldItem(hand, itemHandler.insertItem(0, helditem, false));
				tile.setRotation(playerIn);
			}
		}

		if (!table.isEmpty()) {
			worldIn.playSound(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, SoundEvents.ENTITY_ITEM_PICKUP,
					SoundCategory.BLOCKS, 0.5F, worldIn.rand.nextFloat() * 0.4F + 0.8F, false);
			if (!worldIn.isRemote) {
				playerIn.inventory.addItemStackToInventory(itemHandler.extractItem(0, 64, false));
			}
		}
		return true;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntityTable tile = getTileEntity(world, pos);
		IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
		ItemStack stack = itemHandler.getStackInSlot(0);

		if (!stack.isEmpty()) {
			EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack);
			world.spawnEntity(item);
		}
		super.breakBlock(world, pos, state);
	}

	public TileEntityTable getTileEntity(IBlockAccess world, BlockPos pos) {
		return (TileEntityTable) world.getTileEntity(pos);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityTable();
	}

}