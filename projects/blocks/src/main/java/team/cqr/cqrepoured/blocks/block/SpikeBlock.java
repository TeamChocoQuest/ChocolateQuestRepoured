package team.cqr.cqrepoured.blocks.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import team.cqr.cqrepoured.blocks.init.CQRBlocksEntityTags;
import team.cqr.cqrepoured.blocks.init.CQRBlocksBlockTags;
import team.cqr.cqrepoured.blocks.init.CQRBlocksDamageTypes;

public class SpikeBlock extends DirectionalBlock implements Fallable, SimpleWaterloggedBlock {
	
	public static final BooleanProperty EXTENDED = BooleanProperty.create("extended");
	public static final Vec3 SPIKE_MOTION_PENALTY = new Vec3(0.8D, 0.75D, 0.8D);
	
	public static final VoxelShape COLLISSION_SHAPE_UP 		= Shapes.box(0, 0, 0, 16, 12, 16);
	public static final VoxelShape COLLISSION_SHAPE_DOWN 	= Shapes.box(0, 4, 0, 16, 16, 16);
	public static final VoxelShape COLLISSION_SHAPE_NORTH 	= Shapes.box(0, 0, 4, 16, 16, 16);
	public static final VoxelShape COLLISSION_SHAPE_EAST 	= Shapes.box(0, 0, 0, 12, 16, 16);
	public static final VoxelShape COLLISSION_SHAPE_SOUTH 	= Shapes.box(0, 0, 0, 16, 16, 12);
	public static final VoxelShape COLLISSION_SHAPE_WEST 	= Shapes.box(4, 0, 0, 16, 16, 16);
	
	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		Direction direction = pState.getValue(FACING);
		switch(direction) {
		case DOWN:
			return COLLISSION_SHAPE_DOWN;
		case EAST:
			return COLLISSION_SHAPE_EAST;
		case NORTH:
			return COLLISSION_SHAPE_NORTH;
		case SOUTH:
			return COLLISSION_SHAPE_SOUTH;
		case UP:
			return COLLISSION_SHAPE_UP;
		case WEST:
			return COLLISSION_SHAPE_WEST;
		default:
			return Shapes.empty();
		}
	}
	
	public SpikeBlock(Properties p_52591_) {
		super(p_52591_.noCollission().pushReaction(PushReaction.NORMAL));
		this.registerDefaultState(
			this.defaultBlockState()
			.setValue(EXTENDED, true)
			.setValue(FACING, Direction.UP)
		);
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> pBuilder) {
		super.createBlockStateDefinition(pBuilder);
		pBuilder.add(FACING);
		pBuilder.add(EXTENDED);
	}
	
	public static boolean canStateSupportSpike(final BlockState state) {
		if (state.isAir() || !state.is(CQRBlocksBlockTags.SPIKE_SUPPORTING)) {
			return false;
		}
		return true;
	}

	/*
	 * If we are on the floor or the block we're attached to supports us, we can survive
	 */
	@Override
	public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
		boolean hasFloor = true;
		boolean hasSupportOnAttachedSite = false;
		BlockState stateBelow = pLevel.getBlockState(pPos.below());
		hasFloor = canStateSupportSpike(stateBelow);
		
		if (pState.hasProperty(FACING)) {
			BlockState attachedTo = pLevel.getBlockState(pPos.relative(pState.getValue(FACING).getOpposite()));
			hasSupportOnAttachedSite = canStateSupportSpike(attachedTo);
		}
		return hasFloor || hasSupportOnAttachedSite;
	}
	
	@Override
	public void fallOn(Level pLevel, BlockState pState, BlockPos pPos, Entity pEntity, float pFallDistance) {
		if (pState.hasProperty(EXTENDED) && pState.hasProperty(FACING) && pState.getValue(EXTENDED) && pState.getValue(FACING) != Direction.DOWN) {
			// Falls into the spikes
			if (pEntity.getType().is(CQRBlocksEntityTags.SPIKE_DAMAGE_IMMUNE)) {
				// Just fall damage if you're immune
				super.fallOn(pLevel, pState, pPos, pEntity, pFallDistance);
			} else {
				pEntity.hurt(pLevel.damageSources().source(CQRBlocksDamageTypes.SPIKES), pFallDistance + 2.0F);
			}
		} else {
			super.fallOn(pLevel, pState, pPos, pEntity, pFallDistance);
		}
	}
	
	// TODO: Damage when entity flies into the block / wall
	
	@Override
	public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
		super.entityInside(pState, pLevel, pPos, pEntity);
		if (pState.hasProperty(EXTENDED) && pState.getValue(EXTENDED)) {
			if (!pEntity.getType().is(CQRBlocksEntityTags.SPIKE_WALK_PENALTY_IMMUNE)) {
				pEntity.makeStuckInBlock(pState, SPIKE_MOTION_PENALTY);
			}
			
			if (!pEntity.getType().is(CQRBlocksEntityTags.SPIKE_DAMAGE_IMMUNE) && !pLevel.isClientSide()) {
				// Did we move since last time?
				// we should only damage when the entity moved, otherwise that's a bit unfair...
				double deltaX = Math.abs(pEntity.getX() - pEntity.xOld);
				double deltaZ = Math.abs(pEntity.getZ() - pEntity.zOld);
				if (deltaX >= (double)0.006F || deltaZ >= (double)0.006F) {
					// TODO: Make the variable a constant or a config value => config value!
					pEntity.hurt(pLevel.damageSources().source(CQRBlocksDamageTypes.SPIKES), 2.0F);
				}
			}
		}
	}
	
	@Override
	public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pNeighborBlock, BlockPos pNeighborPos, boolean pMovedByPiston) {
		super.neighborChanged(pState, pLevel, pPos, pNeighborBlock, pNeighborPos, pMovedByPiston);
		if (!pLevel.isClientSide()) {
			boolean extended = pState.getValue(EXTENDED);
			boolean shouldBeExtended = !pLevel.hasNeighborSignal(pPos);
			if (extended != shouldBeExtended) {
				if (shouldBeExtended) {
					// Reacts after a delay!
					pLevel.scheduleTick(pPos, this, 2);
				} else {
					// Immediately retract!
					pLevel.setBlock(pPos, pState.cycle(EXTENDED), 2);
					// Play retract sound
				}
			}
		}
	}
	
	@Override
	public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
		super.tick(pState, pLevel, pPos, pRandom);
		boolean shouldBeExtended = !pLevel.hasNeighborSignal(pPos);
		pLevel.setBlock(pPos, pState.cycle(EXTENDED), 2);
		// Play extend sound
		
	}
	
	@Override
	public boolean isPathfindable(BlockState pState, BlockGetter pLevel, BlockPos pPos, PathComputationType pType) {
		return !pState.getValue(EXTENDED) && pType == PathComputationType.AIR;
	}
	
	@Override
	public boolean propagatesSkylightDown(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
		return pState.getFluidState().isEmpty();
	}
	
}
