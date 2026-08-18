package com.example.chocolatequest.item;

import com.example.chocolatequest.block.entity.SpawnerBlockEntity;
import com.example.chocolatequest.registry.ModBlocks;
import com.example.chocolatequest.registry.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public class SpawnerConverterItem extends Item {
    public SpawnerConverterItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getPlayer() == null || !context.getPlayer().isCreative()) return InteractionResult.PASS;
        if (context.getLevel().isClientSide) return InteractionResult.SUCCESS;

        ServerLevel level = (ServerLevel) context.getLevel();
        BlockPos pos = context.getClickedPos();
        if (level.getBlockState(pos).is(Blocks.SPAWNER)) {
            convertVanillaToCqr(level, pos);
            return InteractionResult.SUCCESS;
        }
        if (level.getBlockState(pos).is(ModBlocks.SPAWNER.get())) {
            convertCqrToVanilla(level, pos);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    private static void convertVanillaToCqr(ServerLevel level, BlockPos pos) {
        net.minecraft.world.level.block.entity.BlockEntity old = level.getBlockEntity(pos);
        if (!(old instanceof net.minecraft.world.level.block.entity.SpawnerBlockEntity)) return;

        CompoundTag blockTag = old.saveWithFullMetadata(level.registryAccess());
        CompoundTag spawnData = blockTag.getCompound("SpawnData");
        CompoundTag entityTag = spawnData.contains("entity") ? spawnData.getCompound("entity").copy() : spawnData.copy();
        if (!entityTag.contains("id")) return;

        level.setBlock(pos, ModBlocks.SPAWNER.get().defaultBlockState(), 3);
        if (level.getBlockEntity(pos) instanceof SpawnerBlockEntity cqrSpawner) {
            ItemStack bottle = new ItemStack(ModItems.SOUL_BOTTLE.get());
            bottle.set(DataComponents.CUSTOM_DATA, CustomData.of(entityTag));
            cqrSpawner.getInventory().setStackInSlot(0, bottle);
            cqrSpawner.setChanged();
            playEffect(level, pos);
        }
    }

    private static void convertCqrToVanilla(ServerLevel level, BlockPos pos) {
        if (!(level.getBlockEntity(pos) instanceof SpawnerBlockEntity cqrSpawner)) return;
        ItemStack bottle = ItemStack.EMPTY;
        for (int i = 0; i < cqrSpawner.getInventory().getSlots(); i++) {
            if (cqrSpawner.getInventory().getStackInSlot(i).has(DataComponents.CUSTOM_DATA)) {
                bottle = cqrSpawner.getInventory().getStackInSlot(i).copy();
                break;
            }
        }
        if (bottle.isEmpty()) return;

        CompoundTag entityTag = bottle.get(DataComponents.CUSTOM_DATA).copyTag();
        ResourceLocation id = ResourceLocation.tryParse(entityTag.getString("id"));
        if (id == null || !BuiltInRegistries.ENTITY_TYPE.containsKey(id)) return;

        level.setBlock(pos, Blocks.SPAWNER.defaultBlockState(), 3);
        if (level.getBlockEntity(pos) instanceof net.minecraft.world.level.block.entity.SpawnerBlockEntity vanillaSpawner) {
            vanillaSpawner.getSpawner().setEntityId(BuiltInRegistries.ENTITY_TYPE.get(id), level, level.random, pos);
            vanillaSpawner.setChanged();
            playEffect(level, pos);
        }
    }

    private static void playEffect(ServerLevel level, BlockPos pos) {
        level.sendParticles(net.minecraft.core.particles.ParticleTypes.SMOKE,
                pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 12, 0.35, 0.35, 0.35, 0.02);
        level.playSound(null, pos, net.minecraft.sounds.SoundEvents.ZOMBIE_VILLAGER_CURE,
                net.minecraft.sounds.SoundSource.BLOCKS, 0.7F, 1.2F);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.cqrepoured.spawner_converter.tooltip").withStyle(ChatFormatting.BLUE));
    }
}
