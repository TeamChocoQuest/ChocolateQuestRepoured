package team.cqr.cqrepoured.block;

import net.minecraft.core.BlockPos;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import team.cqr.cqrepoured.tileentity.TileEntityMap;

public class BlockMapPlaceholder extends DirectionalBlock {

	private static final VoxelShape SHAPE_UP = VoxelShapes.box(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D);
	private static final VoxelShape SHAPE_DOWN = VoxelShapes.box(0.0D, 0.9375D, 0.0D, 1.0D, 1.0D, 1.0D);
	private static final VoxelShape SHAPE_NORTH = VoxelShapes.box(0.0D, 0.0D, 0.9375D, 1.0D, 1.0D, 1.0D);
	private static final VoxelShape SHAPE_SOUTH = VoxelShapes.box(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.0625D);
	private static final VoxelShape SHAPE_WEST = VoxelShapes.box(0.9375D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
	private static final VoxelShape SHAPE_EAST = VoxelShapes.box(0.0D, 0.0D, 0.0D, 0.0625D, 1.0D, 1.0D);

	public BlockMapPlaceholder() {
		super(Properties.of(Material.WOOD)
				.sound(SoundType.WOOD)
				.strength(-1.0F, 3600000.0F)
				.noDrops()
				.noOcclusion());
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> pBuilder) {
		pBuilder.add(FACING);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileEntityMap();
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
		switch (state.getValue(FACING)) {
		case DOWN:
			return SHAPE_DOWN;
		case UP:
			return SHAPE_UP;
		case NORTH:
			return SHAPE_NORTH;
		case SOUTH:
			return SHAPE_SOUTH;
		case WEST:
			return SHAPE_WEST;
		case EAST:
			return SHAPE_EAST;
		default:
			throw new IllegalStateException();
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext pContext) {
		return super.getStateForPlacement(pContext).setValue(FACING, pContext.getClickedFace());
	}

	@Override
	public void setPlacedBy(World level, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {
		if (entity instanceof PlayerEntity && ((PlayerEntity) entity).isCreative()) {
			// TODO open gui
		}
	}

	@Override
	public ActionResultType use(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
		if (player.isCreative()) {
			// TODO open gui
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}

}
