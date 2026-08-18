package com.example.chocolatequest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WebBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class PoisonousWebBlock extends WebBlock {
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 3);

    public PoisonousWebBlock() {
        super(Properties.ofFullCopy(Blocks.COBWEB).randomTicks());
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(AGE);
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        this.tick(pState, pLevel, pPos, pRandom);
    }

    @Override
    protected void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        super.onPlace(pState, pLevel, pPos, pOldState, pIsMoving);
        if (!pOldState.is(pState.getBlock()) && !pLevel.isClientSide()) {
            pLevel.scheduleTick(pPos, this, Mth.nextInt(pLevel.random, 10, 30));
        }
    }

    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pRandom.nextInt(2) == 0 || this.fewerNeigboursThan(pLevel, pPos, 4)) {
            this.slightlyDecay(pState, pLevel, pPos);
        } else {
            pLevel.scheduleTick(pPos, this, Mth.nextInt(pRandom, 10, 30));
        }
    }

    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        if (pBlock == this && this.fewerNeigboursThan(pLevel, pPos, 2)) {
            this.decay(pState, pLevel, pPos);
        }
        super.neighborChanged(pState, pLevel, pPos, pBlock, pFromPos, pIsMoving);
    }

    private boolean fewerNeigboursThan(BlockGetter pLevel, BlockPos pPos, int pCount) {
        int i = 0;
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();

        for (Direction direction : Direction.values()) {
            mutableBlockPos.setWithOffset(pPos, direction);
            if (pLevel.getBlockState(mutableBlockPos).is(this)) {
                ++i;
                if (i >= pCount) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean slightlyDecay(BlockState pState, Level pLevel, BlockPos pPos) {
        int i = pState.getValue(AGE);
        if (i < 3) {
            pLevel.setBlock(pPos, pState.setValue(AGE, i + 1), 2);
            pLevel.scheduleTick(pPos, this, Mth.nextInt(pLevel.random, 20, 40));
            return false;
        } else {
            this.decay(pState, pLevel, pPos);
            return true;
        }
    }

    private void decay(BlockState pState, Level pLevel, BlockPos pPos) {
        pLevel.removeBlock(pPos, false);
    }

    @Override
    public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
        super.entityInside(pState, pLevel, pPos, pEntity);
        if (pEntity instanceof LivingEntity livingEntity && pLevel.random.nextInt(100) < 25) {
            livingEntity.addEffect(new MobEffectInstance(MobEffects.POISON, 60));
        }
    }
}
