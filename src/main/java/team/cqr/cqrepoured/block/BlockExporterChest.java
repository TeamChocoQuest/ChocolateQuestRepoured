package team.cqr.cqrepoured.block;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import team.cqr.cqrepoured.tileentity.TileEntityExporterChest;

public abstract class BlockExporterChest extends BlockHorizontal {

	private static final Set<BlockExporterChest> EXPORTER_CHESTS = new HashSet<>();

	protected static final AxisAlignedBB NORTH_CHEST_AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0D, 0.9375D, 0.875D, 0.9375D);
	protected static final AxisAlignedBB SOUTH_CHEST_AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.875D, 1.0D);
	protected static final AxisAlignedBB WEST_CHEST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0625D, 0.9375D, 0.875D, 0.9375D);
	protected static final AxisAlignedBB EAST_CHEST_AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 1.0D, 0.875D, 0.9375D);
	protected static final AxisAlignedBB NOT_CONNECTED_AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.875D, 0.9375D);

	private final ResourceLocation overlayTexture;

	protected BlockExporterChest(String resourceName) {
		this(new ResourceLocation(resourceName));
	}

	protected BlockExporterChest(String resourceDomain, String resourcePath) {
		this(new ResourceLocation(resourceDomain, resourcePath));
	}

	protected BlockExporterChest(ResourceLocation overlayTexture) {
		super(Material.WOOD);
		this.setBlockUnbreakable();
		this.setResistance(Float.MAX_VALUE);
		this.overlayTexture = overlayTexture;
		EXPORTER_CHESTS.add(this);
	}

	public static Set<BlockExporterChest> getExporterChests() {
		return Collections.unmodifiableSet(EXPORTER_CHESTS);
	}

	public ResourceLocation getOverlayTexture() {
		return this.overlayTexture;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getHorizontalIndex();
	}

	@Deprecated
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta));
	}

	@Deprecated
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Deprecated
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Deprecated
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	@Deprecated
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		if (BlockExporterChest.isChest(source.getBlockState(pos.north()).getBlock())) {
			return NORTH_CHEST_AABB;
		}

		if (BlockExporterChest.isChest(source.getBlockState(pos.south()).getBlock())) {
			return SOUTH_CHEST_AABB;
		}

		if (BlockExporterChest.isChest(source.getBlockState(pos.west()).getBlock())) {
			return WEST_CHEST_AABB;
		}

		if (BlockExporterChest.isChest(source.getBlockState(pos.east()).getBlock())) {
			return EAST_CHEST_AABB;
		}

		return NOT_CONNECTED_AABB;
	}

	@Deprecated
	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta,
			EntityLivingBase placer) {
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		EnumFacing connectedChestDirection = null;
		for (EnumFacing facing : EnumFacing.HORIZONTALS) {
			if (BlockExporterChest.isChest(worldIn.getBlockState(pos.offset(facing)).getBlock())) {
				connectedChestDirection = facing;
				break;
			}
		}

		if (connectedChestDirection != null) {
			IBlockState connectedChestState = worldIn.getBlockState(pos.offset(connectedChestDirection));
			EnumFacing facing = state.getValue(FACING);
			EnumFacing otherFacing = connectedChestState.getValue(FACING);

			if (facing != otherFacing || facing == connectedChestDirection || facing.getOpposite() == connectedChestDirection) {
				if (facing.rotateYCCW() == connectedChestDirection || facing.rotateY() == connectedChestDirection) {
					worldIn.setBlockState(pos.offset(connectedChestDirection), connectedChestState.withProperty(FACING, facing), 3);
				} else if (otherFacing.rotateYCCW() == connectedChestDirection.getOpposite()
						|| otherFacing.rotateY() == connectedChestDirection.getOpposite()) {
					worldIn.setBlockState(pos, state.withProperty(FACING, otherFacing), 3);
				} else {
					worldIn.setBlockState(pos.offset(connectedChestDirection), state.withProperty(FACING, facing.rotateY()), 3);
					worldIn.setBlockState(pos, state.withProperty(FACING, facing.rotateY()), 3);
				}
			}
		}
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		return canPlaceChestAt(worldIn, pos);
	}

	public static boolean canPlaceChestAt(World worldIn, BlockPos pos) {
		int i = 0;

		for (EnumFacing facing : EnumFacing.HORIZONTALS) {
			if (isChest(worldIn.getBlockState(pos.offset(facing)).getBlock())) {
				if (isDoubleChest(worldIn, pos, facing)) {
					return false;
				}

				++i;
			}
		}

		return i <= 1;
	}

	private static boolean isChest(Block block) {
		return block instanceof BlockExporterChest || block == Blocks.CHEST;
	}

	private static boolean isDoubleChest(World worldIn, BlockPos pos, EnumFacing facing) {
		if (isChest(worldIn.getBlockState(pos.offset(facing)).getBlock())) {
			BlockPos blockpos = pos.offset(facing).offset(facing.rotateYCCW());
			BlockPos blockpos1 = pos.offset(facing).offset(facing);
			BlockPos blockpos2 = pos.offset(facing).offset(facing.rotateY());

			if (isChest(worldIn.getBlockState(blockpos).getBlock())) {
				return true;
			}

			if (isChest(worldIn.getBlockState(blockpos1).getBlock())) {
				return true;
			}

			if (isChest(worldIn.getBlockState(blockpos2).getBlock())) {
				return true;
			}
		}

		return false;
	}

	@Override
	public abstract TileEntityExporterChest createTileEntity(World world, IBlockState state);

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Deprecated
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

}
