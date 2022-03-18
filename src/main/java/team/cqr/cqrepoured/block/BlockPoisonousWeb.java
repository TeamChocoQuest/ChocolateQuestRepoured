package team.cqr.cqrepoured.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.WebBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import team.cqr.cqrepoured.util.DungeonGenUtils;

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
	public void randomTick(BlockState state, ServerWorld level, BlockPos pos, Random rand) {
		this.tick(state, level, pos, rand);
	}

	@Override
	public void tick(BlockState state, ServerWorld level, BlockPos pos, Random rand) {
		if (rand.nextInt(2) == 0 || this.fewerNeigboursThan(level, pos, 4)) {
			this.slightlyDecay(state, level, pos);
		} else {
			level.getBlockTicks().scheduleTick(pos, this, MathHelper.nextInt(rand, 10, 30));
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void neighborChanged(BlockState state, World level, BlockPos pos, Block block, BlockPos neighborPos, boolean p_220069_6_) {
		if (block == this && this.fewerNeigboursThan(level, pos, 2)) {
			this.decay(state, level, pos);
		}

		super.neighborChanged(state, level, pos, block, neighborPos, p_220069_6_);
	}

	private boolean fewerNeigboursThan(IBlockReader level, BlockPos pos, int count) {
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

	private boolean slightlyDecay(BlockState state, World level, BlockPos pos) {
		int i = state.getValue(AGE);
		if (i < 3) {
			level.setBlock(pos, state.setValue(AGE, Integer.valueOf(i + 1)), 2);
			level.getBlockTicks().scheduleTick(pos, this, MathHelper.nextInt(level.random, 20, 40));
			return false;
		} else {
			this.decay(state, level, pos);
			return true;
		}
	}

	private void decay(BlockState state, World level, BlockPos pos) {
		level.removeBlock(pos, false);
	}
	
	@Override
	public void entityInside(BlockState pState, World pLevel, BlockPos pPos, Entity pEntity) {
		super.entityInside(pState, pLevel, pPos, pEntity);
		if(pEntity instanceof LivingEntity && DungeonGenUtils.percentageRandom(25, RANDOM)) {
			((LivingEntity)pEntity).addEffect(new EffectInstance(Effects.POISON	, 60));
		}
	}

}
