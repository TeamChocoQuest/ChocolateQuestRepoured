package com.example.chocolatequest.item;

import com.example.chocolatequest.entity.bases.AbstractEntityCQR;
import com.example.chocolatequest.inventory.BadgeMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;

import java.util.List;

public class BadgeItem extends Item {
    public BadgeItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!player.isCreative()) return InteractionResultHolder.fail(stack);

        if (!level.isClientSide) {
            player.openMenu(new MenuProvider() {
                @Override
                public Component getDisplayName() {
                    return Component.translatable("item.cqrepoured.badge");
                }

                @Override
                public AbstractContainerMenu createMenu(int id, Inventory inventory, Player menuPlayer) {
                    return new BadgeMenu(id, inventory, hand);
                }
            }, buffer -> buffer.writeInt(hand == InteractionHand.MAIN_HAND ? 0 : 1));
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (!player.isCreative() || !(target instanceof AbstractEntityCQR cqr)) return InteractionResult.PASS;
        if (!player.level().isClientSide) {
            ItemStack attached = stack.copyWithCount(1);
            cqr.extraInventory.setItem(1, attached);
            cqr.extraInventory.setChanged();
            if (player.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.WITCH,
                        cqr.getX(), cqr.getY() + cqr.getBbHeight() * 0.65, cqr.getZ(),
                        12, 0.4, 0.5, 0.4, 0.04);
            }
            player.displayClientMessage(Component.translatable("message.cqrepoured.badge.attached", cqr.getDisplayName()), true);
        }
        return InteractionResult.sidedSuccess(player.level().isClientSide);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return stack.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).nonEmptyItems().iterator().hasNext();
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.cqrepoured.badge.tooltip").withStyle(ChatFormatting.BLUE));
        int shown = 0;
        for (ItemStack content : stack.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).nonEmptyItems()) {
            if (shown++ >= 5) {
                tooltip.add(Component.translatable("item.cqrepoured.badge.more").withStyle(ChatFormatting.DARK_GRAY));
                break;
            }
            tooltip.add(Component.literal("  ").append(content.getHoverName()).append(" x" + content.getCount()).withStyle(ChatFormatting.GRAY));
        }
    }
}
