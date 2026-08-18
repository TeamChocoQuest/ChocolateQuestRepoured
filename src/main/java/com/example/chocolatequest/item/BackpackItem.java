package com.example.chocolatequest.item;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class BackpackItem extends ArmorItem implements GeoItem {
    private static final int STORAGE_SIZE = 27;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public BackpackItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack backpack = player.getItemInHand(hand);

        // Normal use opens the storage while the backpack is held.
        if (player.isShiftKeyDown()) return super.use(level, player, hand);

        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            serverPlayer.openMenu(new SimpleMenuProvider(
                    (containerId, inventory, menuPlayer) -> createStorageMenu(containerId, inventory, backpack),
                    Component.translatable("container.cqrepoured.backpack")));
        }
        return InteractionResultHolder.sidedSuccess(backpack, level.isClientSide);
    }

    private ChestMenu createStorageMenu(int containerId, Inventory inventory, ItemStack backpack) {
        SimpleContainer storage = new SimpleContainer(STORAGE_SIZE) {
            @Override
            public boolean canPlaceItem(int slot, ItemStack candidate) {
                // Prevent recursive container data and backpack-in-backpack exploits.
                return !(candidate.getItem() instanceof BackpackItem);
            }

            @Override
            public void setChanged() {
                super.setChanged();
                backpack.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(this.getItems()));
            }
        };
        backpack.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY)
                .copyInto(storage.getItems());
        return ChestMenu.threeRows(containerId, inventory, storage);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, state -> PlayState.CONTINUE));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void initializeClient(java.util.function.Consumer<net.neoforged.neoforge.client.extensions.common.IClientItemExtensions> consumer) {
        consumer.accept(new net.neoforged.neoforge.client.extensions.common.IClientItemExtensions() {
            private software.bernie.geckolib.renderer.GeoArmorRenderer<?> renderer;

            @Override
            public net.minecraft.client.model.HumanoidModel<?> getHumanoidArmorModel(net.minecraft.world.entity.LivingEntity livingEntity, net.minecraft.world.item.ItemStack itemStack, net.minecraft.world.entity.EquipmentSlot equipmentSlot, net.minecraft.client.model.HumanoidModel<?> original) {
                if (this.renderer == null) {
                    this.renderer = new com.example.chocolatequest.entity.client.BackpackRenderer();
                }
                this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
                return this.renderer;
            }
        });
    }
}
