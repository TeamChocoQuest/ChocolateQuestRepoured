package team.cqr.cqrepoured.objects.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.tileentity.TileEntityMap;
import team.cqr.cqrepoured.util.Reference;

public class BlockMapPlaceholder extends BlockHorizontal {

	protected static final AxisAlignedBB LADDER_EAST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0625D, 1.0D, 1.0D);
	protected static final AxisAlignedBB LADDER_WEST_AABB = new AxisAlignedBB(0.9375D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
	protected static final AxisAlignedBB LADDER_SOUTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.0625D);
	protected static final AxisAlignedBB LADDER_NORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.9375D, 1.0D, 1.0D, 1.0D);

	public BlockMapPlaceholder() {
		super(Material.WOOD);

		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
		this.setSoundType(SoundType.WOOD);
		this.setBlockUnbreakable();
		this.setResistance(Float.MAX_VALUE);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		if (worldIn.isRemote && (placer instanceof EntityPlayer) && !placer.isSneaking()) {
			EnumFacing facing1 = state.getValue(FACING);
			int x = pos.getX() - facing1.getXOffset();
			int y = (pos.getY() & 0xBFFFFFFF) | (facing1.getHorizontalIndex() << 29);
			int z = pos.getZ() - facing1.getZOffset();
			((EntityPlayer) placer).openGui(CQRMain.INSTANCE, Reference.MAP_GUI_SIMPLE_ID, worldIn, x, y, z);
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote) {
			playerIn.openGui(CQRMain.INSTANCE, Reference.MAP_GUI_ID, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}

		return true;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityMap();
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
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

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
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

	public boolean canAttachTo(World world, BlockPos pos, EnumFacing side) {
		IBlockState iblockstate = world.getBlockState(pos);
		boolean flag = isExceptBlockForAttachWithPiston(iblockstate.getBlock());
		return !flag && iblockstate.getBlockFaceShape(world, pos, side) == BlockFaceShape.SOLID && !iblockstate.canProvidePower();
	}

	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		if (facing.getAxis().isHorizontal() && this.canAttachTo(worldIn, pos.offset(facing.getOpposite()), facing)) {
			return this.getDefaultState().withProperty(FACING, facing);
		} else {
			for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
				if (this.canAttachTo(worldIn, pos.offset(enumfacing.getOpposite()), enumfacing)) {
					return this.getDefaultState().withProperty(FACING, enumfacing);
				}
			}

			return this.getDefaultState();
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		EnumFacing enumfacing = state.getValue(FACING);

		if (!this.canAttachTo(worldIn, pos.offset(enumfacing.getOpposite()), enumfacing)) {
			this.dropBlockAsItem(worldIn, pos, state, 0);
			worldIn.setBlockToAir(pos);
		}

		super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing enumfacing = EnumFacing.byIndex(meta);

		if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
			enumfacing = EnumFacing.NORTH;
		}

		return this.getDefaultState().withProperty(FACING, enumfacing);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getIndex();
	}

	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
	}

	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
		return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING);
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

}
