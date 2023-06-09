package team.cqr.cqrepoured.block;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.material.Material;
import team.cqr.cqrepoured.tileentity.TileEntityMap;

public class BlockMapPlaceholder extends DirectionalBlock {

	private static final VoxelShape SHAPE_UP = Shapes.box(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D);
	private static final VoxelShape SHAPE_DOWN = Shapes.box(0.0D, 0.9375D, 0.0D, 1.0D, 1.0D, 1.0D);
	private static final VoxelShape SHAPE_NORTH = Shapes.box(0.0D, 0.0D, 0.9375D, 1.0D, 1.0D, 1.0D);
	private static final VoxelShape SHAPE_SOUTH = Shapes.box(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.0625D);
	private static final VoxelShape SHAPE_WEST = Shapes.box(0.9375D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
	private static final VoxelShape SHAPE_EAST = Shapes.box(0.0D, 0.0D, 0.0D, 0.0625D, 1.0D, 1.0D);

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
	public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
		return new TileEntityMap();
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
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
	public BlockState getStateForPlacement(BlockPlaceContext pContext) {
		return super.getStateForPlacement(pContext).setValue(FACING, pContext.getClickedFace());
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {
		if (entity instanceof Player && ((Player) entity).isCreative()) {
			// TODO open gui
		}
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		if (player.isCreative()) {
			// TODO open gui
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

}
