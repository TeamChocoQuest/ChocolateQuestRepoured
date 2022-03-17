package team.cqr.cqrepoured.block;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import team.cqr.cqrepoured.inventory.InventoryBlockEntity;
import team.cqr.cqrepoured.tileentity.TileEntityTable;

public class BlockTable extends Block implements IWaterLoggable {

	public static final BooleanProperty TOP = BooleanProperty.create("top");
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	private static final VoxelShape PLATE = Block.box(0.0D, 13.0D, 0.0D, 16.0D, 16.0D, 16.0D);
	private static final VoxelShape STAND = Block.box(6.0D, 0.0D, 6.0D, 10.0D, 13.0D, 10.0D);
	private static final VoxelShape PLATE_STAND = VoxelShapes.or(PLATE, STAND);

	public BlockTable(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(TOP, false).setValue(WATERLOGGED, false));
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> pBuilder) {
		pBuilder.add(TOP, WATERLOGGED);
	}

	@Override
	public VoxelShape getShape(BlockState pState, IBlockReader pLevel, BlockPos pPos, ISelectionContext pContext) {
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
	public BlockState getStateForPlacement(BlockItemUseContext pContext) {
		IWorld level = pContext.getLevel();
		BlockPos pos = pContext.getClickedPos();
		return super.getStateForPlacement(pContext)
				.setValue(WATERLOGGED, level.getFluidState(pos).getType() == Fluids.WATER)
				.setValue(TOP, shouldOnlyTopBePresent(level, pos));
	}

	@SuppressWarnings("deprecation")
	@Override
	public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, IWorld pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
		if (pState.getValue(WATERLOGGED)) {
			pLevel.getLiquidTicks().scheduleTick(pCurrentPos, Fluids.WATER, Fluids.WATER.getTickDelay(pLevel));
		}

		return super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos)
				.setValue(TOP, shouldOnlyTopBePresent(pLevel, pCurrentPos));
	}

	private boolean shouldOnlyTopBePresent(IWorld level, BlockPos pos) {
		return (level.getBlockState(pos.west()).getBlock() == this && level.getBlockState(pos.east()).getBlock() == this)
				|| (level.getBlockState(pos.north()).getBlock() == this && level.getBlockState(pos.south()).getBlock() == this);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileEntityTable();
	}
	
	@Override
	public ActionResultType use(BlockState pState, World pLevel, BlockPos pPos, PlayerEntity pPlayer, Hand pHand, BlockRayTraceResult pHit) {
		TileEntityTable tileEntity = (TileEntityTable) pLevel.getBlockEntity(pPos);
		InventoryBlockEntity inventory = tileEntity.getInventory();
		ItemStack heldItem = pPlayer.getItemInHand(pHand);
		ItemStack storedItem = tileEntity.getInventory().getItem(0);
		
		if (storedItem.isEmpty()) {
			if (heldItem.isEmpty()) {
				return ActionResultType.PASS;
			}

			ItemStack stack = heldItem.copy();
			inventory.setItem(0, stack);
			heldItem.shrink(stack.getCount());
			tileEntity.setRotation(Math.round(pPlayer.yRot / 22.5F));
			pLevel.playSound(pPlayer, pPos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundCategory.BLOCKS, 1.0F, 1.0F);
		} else if (!pPlayer.isCrouching()) {
			ItemStack stack = inventory.removeItem(0, 64);
			if (!pPlayer.addItem(stack)) {
				pPlayer.drop(stack, false);
			}
			pLevel.playSound(pPlayer, pPos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundCategory.BLOCKS, 1.0F, 1.0F);
		} else {
			tileEntity.setRotation(tileEntity.getRotation() + 1);
			pLevel.playSound(pPlayer, pPos, SoundEvents.ITEM_FRAME_ROTATE_ITEM, SoundCategory.BLOCKS, 1.0F, 1.0F);
		}

		return ActionResultType.SUCCESS;
	}

}
