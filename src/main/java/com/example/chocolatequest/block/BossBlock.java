package com.example.chocolatequest.block;

import com.example.chocolatequest.block.entity.BossBlockEntity;
import com.example.chocolatequest.block.entity.SpawnerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.BaseEntityBlock;
import com.mojang.serialization.MapCodec;

import javax.annotation.Nullable;

public class BossBlock extends SpawnerBlock {

    public static final MapCodec<BossBlock> CODEC = simpleCodec(BossBlock::new);

    public BossBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BossBlockEntity(pos, state);
    }

    @Override
    protected BlockEntityType<? extends SpawnerBlockEntity> getExpectedBlockEntityType() {
        return com.example.chocolatequest.registry.ModBlockEntities.BOSS_BLOCK.get();
    }
}
