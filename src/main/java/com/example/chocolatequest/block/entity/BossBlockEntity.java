package com.example.chocolatequest.block.entity;

import com.example.chocolatequest.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;

public class BossBlockEntity extends SpawnerBlockEntity {

    public BossBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BOSS_BLOCK.get(), pos, state);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.cqrepoured.boss_spawner");
    }
}
