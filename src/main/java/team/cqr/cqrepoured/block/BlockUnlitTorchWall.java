package team.cqr.cqrepoured.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import team.cqr.cqrepoured.init.CQRItemTags;

public class BlockUnlitTorchWall extends WallTorchBlock implements IWaterLoggable {

	public static final BooleanProperty LIT = BlockStateProperties.LIT;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public BlockUnlitTorchWall() {
		super(Properties.copy(Blocks.WALL_TORCH).lightLevel(state -> 0), null);
		this.registerDefaultState(this.stateDefinition.any().setValue(LIT, false).setValue(WATERLOGGED, false));
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(LIT, WATERLOGGED);
	}

	@SuppressWarnings("deprecation")
	@Override
	public FluidState getFluidState(BlockState pState) {
		return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		IWorld level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		BlockState state = super.getStateForPlacement(context);
		if (state == null) {
			return null;
		}
		return state.setValue(WATERLOGGED, level.getFluidState(pos).getType() == Fluids.WATER);
	}

	@Override
	public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, IWorld pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
		if (pState.getValue(WATERLOGGED)) {
			pLevel.getLiquidTicks().scheduleTick(pCurrentPos, Fluids.WATER, Fluids.WATER.getTickDelay(pLevel));
		}

		return super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
	}

	@Override
	public void onPlace(BlockState state, World level, BlockPos pos, BlockState oldState, boolean p_220082_5_) {
		if (state.getValue(LIT)) {
			level.setBlock(pos, Blocks.WALL_TORCH.defaultBlockState().setValue(FACING, state.getValue(FACING)), 11);
			this.spawnIgniteParticles(level, pos, state);
		}
	}

	@Override
	public void entityInside(BlockState state, World level, BlockPos pos, Entity entity) {
		if (!level.isClientSide && entity.isOnFire()) {
			level.setBlock(pos, Blocks.WALL_TORCH.defaultBlockState().setValue(FACING, state.getValue(FACING)), 11);
			level.playSound(null, pos, SoundEvents.FIRECHARGE_USE, SoundCategory.BLOCKS, 1.0F, 1.0F);
			this.spawnIgniteParticles(level, pos, state);
		}
	}

	private void spawnIgniteParticles(World level, BlockPos pos, BlockState state) {
		if (!level.isClientSide) {
			double x = pos.getX() + 0.5D;
			double y = pos.getY() + 0.7D;
			double z = pos.getZ() + 0.5D;
			Direction dir = state.getValue(FACING).getOpposite();
			x += dir.getStepX() * 0.25D;
			y += 0.22D;
			z += dir.getStepZ() * 0.25D;
			((ServerWorld) level).sendParticles(ParticleTypes.FLAME, x, y, z, 4, 0.0625D, 0.0625D, 0.0625D, 0.0078125D);
		}
	}

	@Override
	public void animateTick(BlockState pState, World pLevel, BlockPos pPos, Random pRand) {

	}
	
	@Override
	public ActionResultType use(BlockState pState, World pLevel, BlockPos pPos, PlayerEntity pPlayer, Hand pHand, BlockRayTraceResult pHit) {
		ItemStack stack = pPlayer.getItemInHand(pHand);
		Block block = Block.byItem(stack.getItem());

		if ((stack.getItem() != null &&stack.getItem().is(CQRItemTags.TORCH_IGNITERS)) || block.getLightValue(block.defaultBlockState(), pLevel, pHit.getBlockPos()) > 0.0F) {
			if (!pLevel.isClientSide) {
				if(stack.getItem().is(CQRItemTags.SOUL_FIRE_EMITTERS)) {
					pLevel.setBlock(pHit.getBlockPos(), Blocks.SOUL_WALL_TORCH.defaultBlockState().setValue(FACING, pState.getValue(FACING)), 11);
				} else {
					pLevel.setBlock(pHit.getBlockPos(), Blocks.WALL_TORCH.defaultBlockState().setValue(FACING, pState.getValue(FACING)), 11);
				}
				pLevel.playSound(null, pHit.getBlockPos(), SoundEvents.FIRECHARGE_USE, SoundCategory.BLOCKS, 1.0F, 1.0F);
				this.spawnIgniteParticles(pLevel, pHit.getBlockPos(), pState);
			}
			return ActionResultType.SUCCESS;
		}
		return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
	}
	
	@Override
	public void onProjectileHit(World pLevel, BlockState pState, BlockRayTraceResult pHit, ProjectileEntity pProjectile) {
		if(pProjectile.isOnFire()) {
			pProjectile.kill();
			pLevel.setBlock(pHit.getBlockPos(), Blocks.WALL_TORCH.defaultBlockState().setValue(FACING, pState.getValue(FACING)), 11);
			pLevel.playSound(null, pHit.getBlockPos(), SoundEvents.FIRECHARGE_USE, SoundCategory.BLOCKS, 1.0F, 1.0F);
			this.spawnIgniteParticles(pLevel, pHit.getBlockPos(), pState);
		}
	}

}
