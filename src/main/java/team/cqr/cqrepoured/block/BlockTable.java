package team.cqr.cqrepoured.block;

import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import team.cqr.cqrepoured.inventory.InventoryBlockEntity;
import team.cqr.cqrepoured.tileentity.TileEntityTable;

import javax.annotation.Nullable;

public class BlockTable extends Block implements IWaterLoggable {

	public static final BooleanProperty TOP = BooleanProperty.create("top");
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	private static final VoxelShape PLATE = Block.box(0.0D, 13.0D, 0.0D, 16.0D, 16.0D, 16.0D);
	private static final VoxelShape STAND = Block.box(6.0D, 0.0D, 6.0D, 10.0D, 13.0D, 10.0D);
	private static final VoxelShape PLATE_STAND = Shapes.or(PLATE, STAND);

	public BlockTable(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(TOP, false).setValue(WATERLOGGED, false));
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> pBuilder) {
		pBuilder.add(TOP, WATERLOGGED);
	}

	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		if (pState.getValue(TOP)) {
			return PLATE;
		}
		return PLATE_STAND;
	}

	@Override
	public boolean useShapeForLightOcclusion(BlockState pState) {
		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public FluidState getFluidState(BlockState pState) {
		return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
	}

	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext pContext) {
		Level level = pContext.getLevel();
		BlockPos pos = pContext.getClickedPos();
		return super.getStateForPlacement(pContext)
				.setValue(WATERLOGGED, level.getFluidState(pos).getType() == Fluids.WATER)
				.setValue(TOP, shouldOnlyTopBePresent(level, pos));
	}

	@SuppressWarnings("deprecation")
	@Override
	public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, Level pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
		if (pState.getValue(WATERLOGGED)) {
			pLevel.getLiquidTicks().scheduleTick(pCurrentPos, Fluids.WATER, Fluids.WATER.getTickDelay(pLevel));
		}

		return super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos)
				.setValue(TOP, shouldOnlyTopBePresent(pLevel, pCurrentPos));
	}

	private boolean shouldOnlyTopBePresent(Level level, BlockPos pos) {
		return (level.getBlockState(pos.west()).getBlock() == this && level.getBlockState(pos.east()).getBlock() == this)
				|| (level.getBlockState(pos.north()).getBlock() == this && level.getBlockState(pos.south()).getBlock() == this);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}
	
	@Override
	public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
		return new TileEntityTable();
	}
	
	@Override
	public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
		TileEntityTable tileEntity = (TileEntityTable) pLevel.getBlockEntity(pPos);
		InventoryBlockEntity inventory = tileEntity.getInventory();
		ItemStack heldItem = pPlayer.getItemInHand(pHand);
		ItemStack storedItem = tileEntity.getInventory().getItem(0);
		
		if (storedItem.isEmpty()) {
			if (heldItem.isEmpty()) {
				return InteractionResult.PASS;
			}

			ItemStack stack = heldItem.copy();
			inventory.setItem(0, stack);
			heldItem.shrink(stack.getCount());
			tileEntity.setRotation(Math.round(pPlayer.yRot / 22.5F));
			pLevel.playSound(pPlayer, pPos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
		} else if (!pPlayer.isCrouching()) {
			ItemStack stack = inventory.removeItem(0, 64);
			if (!pPlayer.addItem(stack)) {
				pPlayer.drop(stack, false);
			}
			pLevel.playSound(pPlayer, pPos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
		} else {
			tileEntity.setRotation(tileEntity.getRotation() + 1);
			pLevel.playSound(pPlayer, pPos, SoundEvents.ITEM_FRAME_ROTATE_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
		}

		return InteractionResult.SUCCESS;
	}

}
