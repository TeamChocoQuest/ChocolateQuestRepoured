package com.example.chocolatequest.item;

import com.example.chocolatequest.world.structure.generators.VegetatedCaveGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public class CaveSpawnerItem extends Item {

    public CaveSpawnerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (!context.getLevel().isClientSide && context.getLevel() instanceof ServerLevel serverLevel) {
            BlockPos clickedPos = context.getClickedPos().relative(context.getClickedFace());

            if (context.getPlayer() != null) {
                context.getPlayer().sendSystemMessage(Component.literal("§f[CQR] Spawning Vegetated Cave at " + clickedPos.toShortString() + "..."));
            }

            VegetatedCaveGenerator caveGen = new VegetatedCaveGenerator(clickedPos, serverLevel.random.nextLong());
            caveGen.generateMap();

            for (Map.Entry<BlockPos, BlockState> entry : caveGen.blocks.entrySet()) {
                BlockPos pos = entry.getKey();
                BlockState state = entry.getValue();
                if (serverLevel.getBlockState(pos) != state) {
                    serverLevel.setBlock(pos, state, 2);
                }
            }

            for (Map.Entry<BlockPos, CompoundTag> entry : caveGen.blockEntityTags.entrySet()) {
                BlockEntity te = serverLevel.getBlockEntity(entry.getKey());
                if (te != null) {
                    CompoundTag tag = entry.getValue();
                    if (te instanceof net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity container) {
                        String lt = tag.getString("LootTable");
                        if (lt.isEmpty()) lt = "cqrepoured:chests/treasure";
                        if (lt.startsWith("cqrepoured:")) lt = lt.replace("cqrepoured:", "cqrepoured:");
                        try {
                            net.minecraft.resources.ResourceLocation rl = net.minecraft.resources.ResourceLocation.parse(lt);
                            net.minecraft.resources.ResourceKey<net.minecraft.world.level.storage.loot.LootTable> key = 
                                net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.LOOT_TABLE, rl);
                            container.setLootTable(key, serverLevel.random.nextLong());
                        } catch (Exception ignored) {}
                    }
                    te.loadWithComponents(tag, serverLevel.registryAccess());
                    te.setChanged();
                }
            }

            if (context.getPlayer() != null) {
                context.getPlayer().sendSystemMessage(Component.literal("§f[CQR] Successfully spawned Vegetated Cave!"));
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.SUCCESS;
    }
}
