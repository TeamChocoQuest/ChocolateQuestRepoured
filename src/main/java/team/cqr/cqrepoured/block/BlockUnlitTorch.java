package team.cqr.cqrepoured.block;

import net.minecraft.block.*;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import team.cqr.cqrepoured.init.CQRItemTags;

import java.util.Random;

public class BlockUnlitTorch extends TorchBlock implements IWaterLoggable {

	public static final BooleanProperty LIT = BlockStateProperties.LIT;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public BlockUnlitTorch() {
		super(Properties.copy(Blocks.TORCH).lightLevel(state -> 0), null);
		this.registerDefaultState(this.stateDefinition.any().setValue(LIT, false).setValue(WATERLOGGED, false));
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(LIT, WATERLOGGED);
	}

	@SuppressWarnings("deprecation")
	@Override
	public FluidState getFluidState(BlockState pState) {
		return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		BlockState state = super.getStateForPlacement(context);
		if (state == null) {
			return null;
		}
		return state.setValue(WATERLOGGED, level.getFluidState(pos).getType() == Fluids.WATER);
	}

	@Override
	public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, Level pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
		if (pState.getValue(WATERLOGGED)) {
			pLevel.getLiquidTicks().scheduleTick(pCurrentPos, Fluids.WATER, Fluids.WATER.getTickDelay(pLevel));
		}

		return super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
	}

	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean p_220082_5_) {
		if (state.getValue(LIT)) {
			level.setBlock(pos, Blocks.TORCH.defaultBlockState(), 11);
			this.spawnIgniteParticles(level, pos, state);
		}
	}

	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		if (!level.isClientSide && entity.isOnFire()) {
			level.setBlock(pos, Blocks.TORCH.defaultBlockState(), 11);
			level.playSound(null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
			this.spawnIgniteParticles(level, pos, state);
		}
	}

	private void spawnIgniteParticles(Level level, BlockPos pos, BlockState state) {
		if (!level.isClientSide) {
			double x = pos.getX() + 0.5D;
			double y = pos.getY() + 0.7D;
			double z = pos.getZ() + 0.5D;
			((ServerLevel) level).sendParticles(ParticleTypes.FLAME, x, y, z, 4, 0.0625D, 0.0625D, 0.0625D, 0.0078125D);
		}
	}

	@Override
	public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, Random pRand) {

	}

	@Override
	public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
		ItemStack stack = pPlayer.getItemInHand(pHand);

		if (!stack.isEmpty() && stack.getItem().is(CQRItemTags.TORCH_IGNITERS)) {
			if (!pLevel.isClientSide) {
				pLevel.setBlock(pHit.getBlockPos(), Blocks.TORCH.defaultBlockState(), 11);
				pLevel.playSound(null, pHit.getBlockPos(), SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
				this.spawnIgniteParticles(pLevel, pHit.getBlockPos(), pState);
			}
			return InteractionResult.SUCCESS;
		}
		return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
	}

}
