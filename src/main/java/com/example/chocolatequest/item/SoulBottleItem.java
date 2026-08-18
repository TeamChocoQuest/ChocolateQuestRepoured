package com.example.chocolatequest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.List;

public class SoulBottleItem extends Item {

    public SoulBottleItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, net.minecraft.world.entity.LivingEntity interactionTarget, InteractionHand usedHand) {
        if (interactionTarget instanceof Player || stack.has(DataComponents.CUSTOM_DATA)) {
            return InteractionResult.PASS;
        }
        if (player.level().isClientSide) {
            return InteractionResult.SUCCESS;
        }

        CompoundTag entityTag = new CompoundTag();
        if (interactionTarget.save(entityTag)) {
            entityTag.remove("UUID");
            entityTag.remove("Pos");

            ItemStack filledBottle;
            if (stack.getCount() > 1) {
                stack.shrink(1);
                filledBottle = new ItemStack(this);
            } else {
                filledBottle = stack;
            }
            filledBottle.set(DataComponents.CUSTOM_DATA, CustomData.of(entityTag));
            if (filledBottle != stack && !player.getInventory().add(filledBottle)) {
                player.drop(filledBottle, false);
            }

            double effectX = interactionTarget.getX();
            double effectY = interactionTarget.getY() + interactionTarget.getBbHeight() * 0.5D;
            double effectZ = interactionTarget.getZ();
            interactionTarget.remove(Entity.RemovalReason.DISCARDED);
            spawnAdditions(player.level(), effectX, effectY, effectZ);
            return InteractionResult.SUCCESS;
        }
        return super.interactLivingEntity(stack, player, interactionTarget, usedHand);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        
        if (player != null && !player.isSpectator()) {
            ItemStack stack = context.getItemInHand();
            
            if (stack.has(DataComponents.CUSTOM_DATA)) {
                if (!level.isClientSide) {
                    CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
                    if (customData != null) {
                        CompoundTag entityTag = customData.copyTag();
                        
                        // Strip position and UUIDs just in case
                        entityTag.remove("UUID");
                        entityTag.remove("Pos");
                        
                        double x = pos.getX() + context.getClickedFace().getStepX() + 0.5D;
                        double y = pos.getY() + context.getClickedFace().getStepY();
                        double z = pos.getZ() + context.getClickedFace().getStepZ() + 0.5D;
                        
                        Entity entity = EntityType.loadEntityRecursive(entityTag, level, (e) -> {
                            e.moveTo(x, y, z, e.getYRot(), e.getXRot());
                            return e;
                        });
                        
                        if (entity != null) {
                            level.addFreshEntity(entity);
                            spawnAdditions(level, x, y, z);
                            
                            if (player.isCrouching() || !player.isCreative()) {
                                stack.remove(DataComponents.CUSTOM_DATA);
                            }
                            
                            if (!player.isCreative()) {
                                stack.shrink(1);
                            }
                        }
                    }
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    private void spawnAdditions(Level level, double x, double y, double z) {
        if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
            for (int i = 0; i < 4; i++) {
                serverLevel.sendParticles(ParticleTypes.CLOUD, x, y, z, 5, 0.25D, 0.25D, 0.25D, 0.1D);
            }
            level.playSound(null, x, y, z, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 1.0F, 0.6F + level.random.nextFloat() * 0.2F);
        }
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return stack.has(DataComponents.CUSTOM_DATA);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (stack.has(DataComponents.CUSTOM_DATA)) {
            CustomData data = stack.get(DataComponents.CUSTOM_DATA);
            if (data != null) {
                CompoundTag tag = data.copyTag();
                if (tag.contains("id")) {
                    String id = tag.getString("id");
                    tooltipComponents.add(Component.translatable("item.cqrepoured.tooltip.contains_entity").append(Component.literal(": " + id)).withStyle(ChatFormatting.BLUE));
                    return;
                }
            }
        }
        tooltipComponents.add(Component.translatable("item.cqrepoured.tooltip.contains_nothing").withStyle(ChatFormatting.BLUE));
    }
}
