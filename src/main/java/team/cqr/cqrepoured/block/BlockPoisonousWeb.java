package team.cqr.cqrepoured.block;

import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WebBlock;
import net.minecraft.state.IntegerProperty;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import team.cqr.cqrepoured.util.DungeonGenUtils;

import java.util.Random;

public class BlockPoisonousWeb extends WebBlock {

	public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 3);

	public BlockPoisonousWeb() {
		super(Properties.copy(Blocks.COBWEB).randomTicks());
		this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> pBuilder) {
		pBuilder.add(AGE);
	}

	@Override
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random rand) {
		this.tick(state, level, pos, rand);
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, Random rand) {
		if (rand.nextInt(2) == 0 || this.fewerNeigboursThan(level, pos, 4)) {
			this.slightlyDecay(state, level, pos);
		} else {
			level.getBlockTicks().scheduleTick(pos, this, Mth.nextInt(rand, 10, 30));
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos neighborPos, boolean p_220069_6_) {
		if (block == this && this.fewerNeigboursThan(level, pos, 2)) {
			this.decay(state, level, pos);
		}

		super.neighborChanged(state, level, pos, block, neighborPos, p_220069_6_);
	}

	private boolean fewerNeigboursThan(BlockGetter level, BlockPos pos, int count) {
		int i = 0;
		BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

		for (Direction direction : Direction.values()) {
			blockpos$mutable.setWithOffset(pos, direction);
			if (level.getBlockState(blockpos$mutable).is(this)) {
				++i;
				if (i >= count) {
					return false;
				}
			}
		}

		return true;
	}

	private boolean slightlyDecay(BlockState state, Level level, BlockPos pos) {
		int i = state.getValue(AGE);
		if (i < 3) {
			level.setBlock(pos, state.setValue(AGE, Integer.valueOf(i + 1)), 2);
			level.getBlockTicks().scheduleTick(pos, this, Mth.nextInt(level.random, 20, 40));
			return false;
		} else {
			this.decay(state, level, pos);
			return true;
		}
	}

	private void decay(BlockState state, Level level, BlockPos pos) {
		level.removeBlock(pos, false);
	}
	
	@Override
	public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
		super.entityInside(pState, pLevel, pPos, pEntity);
		if(pEntity instanceof LivingEntity && DungeonGenUtils.percentageRandom(25, RANDOM)) {
			((LivingEntity)pEntity).addEffect(new MobEffectInstance(MobEffects.POISON	, 60));
		}
	}

}
