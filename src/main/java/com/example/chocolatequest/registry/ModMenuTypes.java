package com.example.chocolatequest.registry;

import com.example.chocolatequest.ChocolateQuestReDone;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;

import com.example.chocolatequest.inventory.AlchemyBagMenu;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(BuiltInRegistries.MENU, ChocolateQuestReDone.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<AlchemyBagMenu>> ALCHEMY_BAG = MENUS.register("alchemy_bag", () -> IMenuTypeExtension.create(AlchemyBagMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<com.example.chocolatequest.inventory.BadgeMenu>> BADGE = MENUS.register("badge", () -> IMenuTypeExtension.create(com.example.chocolatequest.inventory.BadgeMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<com.example.chocolatequest.inventory.ContainerCQREntity>> CQR_ENTITY_EDITOR = MENUS.register("cqr_entity_editor", () -> IMenuTypeExtension.create(com.example.chocolatequest.inventory.ContainerCQREntity::new));
    @SuppressWarnings("unchecked")
    public static final DeferredHolder<MenuType<?>, MenuType<net.minecraft.world.inventory.MerchantMenu>> CQR_MERCHANT = MENUS.register("cqr_merchant", () -> (MenuType) IMenuTypeExtension.create(com.example.chocolatequest.inventory.ContainerCQRMerchant::new));
    public static final DeferredHolder<MenuType<?>, MenuType<com.example.chocolatequest.inventory.SpawnerMenu>> SPAWNER_MENU = MENUS.register("spawner_menu", () -> IMenuTypeExtension.create((windowId, inv, data) -> new com.example.chocolatequest.inventory.SpawnerMenu(windowId, inv, data)));

}
