package team.cqr.cqrepoured.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockNull extends Block {

	public static final BooleanProperty PASSABLE = BooleanProperty.create("passable");

	public BlockNull() {
		super(Properties
				.of(Material.GLASS)
				.sound(SoundType.GLASS)
				.noDrops()
				.noOcclusion()
				.isViewBlocking(Blocks::never)
				.strength(-1.0F, Float.MAX_VALUE)
				.isValidSpawn(Blocks::never)
		);
		this.registerDefaultState(this.stateDefinition.any().setValue(PASSABLE, false));
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
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand pHand, BlockRayTraceResult pHit) {
		
		if (playerIn.isCreative() && playerIn.getItemInHand(Hand.MAIN_HAND).isEmpty()) {
			if (state.getValue(PASSABLE)) {
				worldIn.setBlock(pos, state.setValue(PASSABLE, false), 3);
			} else {
				worldIn.setBlock(pos, state.setValue(PASSABLE, true), 3);
			}
			return ActionResultType.SUCCESS;
		} else {
			return ActionResultType.PASS;
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
	@OnlyIn(Dist.CLIENT)
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@SuppressWarnings("deprecation")
	@Override
	@OnlyIn(Dist.CLIENT)
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
