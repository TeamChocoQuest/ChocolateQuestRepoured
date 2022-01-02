package team.cqr.cqrepoured.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.tileentity.TileEntityMap;
import team.cqr.cqrepoured.util.GuiHandler;

public class BlockMapPlaceholder extends HorizontalBlock {

	protected static final AxisAlignedBB LADDER_EAST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0625D, 1.0D, 1.0D);
	protected static final AxisAlignedBB LADDER_WEST_AABB = new AxisAlignedBB(0.9375D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
	protected static final AxisAlignedBB LADDER_SOUTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.0625D);
	protected static final AxisAlignedBB LADDER_NORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.9375D, 1.0D, 1.0D, 1.0D);

	public BlockMapPlaceholder() {
		super(Material.WOOD);

		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, Direction.NORTH));
		this.setSoundType(SoundType.WOOD);
		this.setBlockUnbreakable();
		this.setResistance(Float.MAX_VALUE);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		if (worldIn.isRemote && (placer instanceof PlayerEntity) && !placer.isSneaking()) {
			Direction facing1 = state.getValue(FACING);
			int x = pos.getX() - facing1.getXOffset();
			int y = (pos.getY() & 0x9FFFFFFF) | (facing1.getHorizontalIndex() << 29);
			int z = pos.getZ() - facing1.getZOffset();
			((PlayerEntity) placer).openGui(CQRMain.INSTANCE, GuiHandler.MAP_GUI_SIMPLE_ID, worldIn, x, y, z);
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, BlockState state, PlayerEntity playerIn, Hand hand, Direction facing, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote) {
			playerIn.openGui(CQRMain.INSTANCE, GuiHandler.MAP_GUI_ID, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}

		return true;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, BlockState state) {
		return new TileEntityMap();
	}

	@Deprecated
	@Override
	public AxisAlignedBB getBoundingBox(BlockState state, IBlockAccess source, BlockPos pos) {
		switch (state.getValue(FACING)) {
		case NORTH:
			return LADDER_NORTH_AABB;
		case SOUTH:
			return LADDER_SOUTH_AABB;
		case WEST:
			return LADDER_WEST_AABB;
		case EAST:
		default:
			return LADDER_EAST_AABB;
		}
	}

	@Deprecated
	@Override
	public boolean isOpaqueCube(BlockState state) {
		return false;
	}

	@Deprecated
	@Override
	public boolean isFullCube(BlockState state) {
		return false;
	}

	@Override
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, Direction side) {
		if (this.canAttachTo(worldIn, pos.west(), side)) {
			return true;
		} else if (this.canAttachTo(worldIn, pos.east(), side)) {
			return true;
		} else if (this.canAttachTo(worldIn, pos.north(), side)) {
			return true;
		} else {
			return this.canAttachTo(worldIn, pos.south(), side);
		}
	}

	public boolean canAttachTo(World world, BlockPos pos, Direction side) {
		BlockState iblockstate = world.getBlockState(pos);
		boolean flag = isExceptBlockForAttachWithPiston(iblockstate.getBlock());
		return !flag && iblockstate.getBlockFaceShape(world, pos, side) == BlockFaceShape.SOLID && !iblockstate.canProvidePower();
	}

	@Override
	public BlockState getStateForPlacement(World worldIn, BlockPos pos, Direction facing, float hitX, float hitY, float hitZ, int meta, LivingEntity placer, Hand hand) {
		if (facing.getAxis().isHorizontal() && this.canAttachTo(worldIn, pos.offset(facing.getOpposite()), facing)) {
			return this.getDefaultState().withProperty(FACING, facing);
		} else {
			for (Direction enumfacing : Direction.Plane.HORIZONTAL) {
				if (this.canAttachTo(worldIn, pos.offset(enumfacing.getOpposite()), enumfacing)) {
					return this.getDefaultState().withProperty(FACING, enumfacing);
				}
			}

			return this.getDefaultState();
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		Direction enumfacing = state.getValue(FACING);

		if (!this.canAttachTo(worldIn, pos.offset(enumfacing.getOpposite()), enumfacing)) {
			this.dropBlockAsItem(worldIn, pos, state, 0);
			worldIn.setBlockToAir(pos);
		}

		super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
	}

	@Deprecated
	@Override
	public BlockState getStateFromMeta(int meta) {
		Direction enumfacing = Direction.byIndex(meta);

		if (enumfacing.getAxis() == Direction.Axis.Y) {
			enumfacing = Direction.NORTH;
		}

		return this.getDefaultState().withProperty(FACING, enumfacing);
	}

	@Dist(OnlyIn.CLIENT)
	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public int getMetaFromState(BlockState state) {
		return state.getValue(FACING).getIndex();
	}

	@Deprecated
	@Override
	public BlockState withRotation(BlockState state, Rotation rot) {
		return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
	}

	@Deprecated
	@Override
	public BlockState withMirror(BlockState state, Mirror mirrorIn) {
		return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING);
	}

	@Deprecated
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, BlockState state, BlockPos pos, Direction face) {
		return BlockFaceShape.UNDEFINED;
	}

}
