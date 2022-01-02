package team.cqr.cqrepoured.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

public class BlockNull extends Block {

	public static final PropertyBool PASSABLE = PropertyBool.create("passable");

	public BlockNull() {
		super(Material.GLASS);

		this.setSoundType(SoundType.GLASS);
		this.setBlockUnbreakable();
		this.setResistance(Float.MAX_VALUE);
		this.setHarvestLevel("hand", 0);
		this.setDefaultState(this.blockState.getBaseState().withProperty(PASSABLE, false));
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
	public boolean onBlockActivated(World worldIn, BlockPos pos, BlockState state, PlayerEntity playerIn, Hand hand, Direction facing, float hitX, float hitY, float hitZ) {
		if (playerIn.capabilities.isCreativeMode && playerIn.getHeldItem(Hand.MAIN_HAND).isEmpty()) {
			if (state.getValue(PASSABLE)) {
				worldIn.setBlockState(pos, state.withProperty(PASSABLE, false), 3);
			} else {
				worldIn.setBlockState(pos, state.withProperty(PASSABLE, true), 3);
			}
			return true;
		} else {
			return false;
		}
	}

	@Deprecated
	@Override
	public AxisAlignedBB getCollisionBoundingBox(BlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return blockState.getValue(PASSABLE) ? null : blockState.getBoundingBox(worldIn, pos);
	}

	@Deprecated
	@Override
	public BlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(PASSABLE, (meta & 1) != 0);
	}

	@Override
	public int getMetaFromState(BlockState state) {
		return state.getValue(PASSABLE) ? 1 : 0;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, PASSABLE);
	}

	@Override
	@Dist(OnlyIn.CLIENT)
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@SuppressWarnings("deprecation")
	@Override
	@Dist(OnlyIn.CLIENT)
	public boolean shouldSideBeRendered(BlockState blockState, IBlockAccess blockAccess, BlockPos pos, Direction side) {
		BlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
		Block block = iblockstate.getBlock();

		if (blockState != iblockstate) {
			return true;
		}

		if (block == this) {
			return false;
		}

		return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
	}

}
