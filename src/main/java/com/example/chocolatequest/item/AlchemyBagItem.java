package com.example.chocolatequest.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleContainer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.alchemy.PotionContents;
import com.example.chocolatequest.inventory.AlchemyBagMenu;

import java.util.List;

public class AlchemyBagItem extends Item {

    public AlchemyBagItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            if (player.isCrouching()) {
                player.openMenu(new MenuProvider() {
                    @Override
                    public Component getDisplayName() {
                        return Component.translatable("item.cqrepoured.alchemy_bag");
                    }

                    @Override
                    public AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player player) {
                        return new AlchemyBagMenu(windowId, inventory, hand);
                    }
                }, buf -> buf.writeInt(hand == InteractionHand.MAIN_HAND ? 0 : 1));
                return InteractionResultHolder.success(stack);
            }

            // Throw potion
            ItemContainerContents contents = stack.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY);
            SimpleContainer container = new SimpleContainer(5);
            contents.copyInto(container.getItems());

            boolean thrown = false;
            for (int i = 0; i < container.getContainerSize(); i++) {
                ItemStack potion = container.getItem(i);
                if (!potion.isEmpty() && (potion.is(net.minecraft.world.item.Items.SPLASH_POTION) || potion.is(net.minecraft.world.item.Items.LINGERING_POTION))) {
                    ItemStack thrownPotion = potion.split(1);
                    container.setItem(i, potion);
                    
                    ThrownPotion entitypotion = new ThrownPotion(level, player);
                    entitypotion.setItem(thrownPotion);
                    entitypotion.shootFromRotation(player, player.getXRot(), player.getYRot(), -20.0F, 0.5F, 1.0F);
                    level.addFreshEntity(entitypotion);

                    level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SPLASH_POTION_THROW, SoundSource.PLAYERS, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
                    thrown = true;
                    break;
                }
            }

            if (thrown) {
                stack.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(container.getItems()));
                return InteractionResultHolder.success(stack);
            }
        }
        return InteractionResultHolder.fail(stack);
    }
}
