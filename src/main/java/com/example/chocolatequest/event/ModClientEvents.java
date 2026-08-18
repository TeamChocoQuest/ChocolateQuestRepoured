package com.example.chocolatequest.event;

import com.example.chocolatequest.client.render.tileentity.TileEntityExporterChestRenderer;
import com.example.chocolatequest.registry.ModBlockEntities;
import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.registry.ModEntities;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import com.example.chocolatequest.registry.ModMenuTypes;
import com.example.chocolatequest.client.gui.ScreenAlchemyBag;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = ChocolateQuestReDone.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModClientEvents {

    public static final net.minecraft.client.KeyMapping DODGE_KEY = new net.minecraft.client.KeyMapping(
        "key.cqrepoured.dodge", 
        com.mojang.blaze3d.platform.InputConstants.Type.KEYSYM, 
        org.lwjgl.glfw.GLFW.GLFW_KEY_V, 
        "category.cqrepoured.keys"
    );

    public static final net.minecraft.client.KeyMapping RELOAD_FIREARM_KEY = new net.minecraft.client.KeyMapping(
        "key.cqrepoured.reload_firearm",
        com.mojang.blaze3d.platform.InputConstants.Type.KEYSYM,
        org.lwjgl.glfw.GLFW.GLFW_KEY_R,
        "category.cqrepoured.keys"
    );

    @SubscribeEvent
    public static void registerKeyMappings(net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent event) {
        event.register(DODGE_KEY);
        event.register(RELOAD_FIREARM_KEY);
    }

    public static final net.minecraft.client.model.geom.ModelLayerLocation HEAVY_ARMOR_OUTER = new net.minecraft.client.model.geom.ModelLayerLocation(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "heavy_armor"), "outer");
    public static final net.minecraft.client.model.geom.ModelLayerLocation HEAVY_ARMOR_INNER = new net.minecraft.client.model.geom.ModelLayerLocation(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "heavy_armor"), "inner");

    public static final net.minecraft.client.model.geom.ModelLayerLocation BULL_ARMOR_OUTER = new net.minecraft.client.model.geom.ModelLayerLocation(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "bull_armor"), "outer");
    public static final net.minecraft.client.model.geom.ModelLayerLocation BULL_ARMOR_INNER = new net.minecraft.client.model.geom.ModelLayerLocation(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "bull_armor"), "inner");

    public static final net.minecraft.client.model.geom.ModelLayerLocation TURTLE_ARMOR_OUTER = new net.minecraft.client.model.geom.ModelLayerLocation(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "turtle_armor"), "outer");
    public static final net.minecraft.client.model.geom.ModelLayerLocation TURTLE_ARMOR_INNER = new net.minecraft.client.model.geom.ModelLayerLocation(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "turtle_armor"), "inner");

    public static final net.minecraft.client.model.geom.ModelLayerLocation SPIDER_ARMOR_OUTER = new net.minecraft.client.model.geom.ModelLayerLocation(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "spider_armor"), "outer");
    public static final net.minecraft.client.model.geom.ModelLayerLocation SPIDER_ARMOR_INNER = new net.minecraft.client.model.geom.ModelLayerLocation(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "spider_armor"), "inner");

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(HEAVY_ARMOR_OUTER, () -> com.example.chocolatequest.client.model.armor.CQHeavyArmorModel.createArmorLayer(new net.minecraft.client.model.geom.builders.CubeDeformation(1.0F)));
        event.registerLayerDefinition(HEAVY_ARMOR_INNER, () -> com.example.chocolatequest.client.model.armor.CQHeavyArmorModel.createArmorLayer(new net.minecraft.client.model.geom.builders.CubeDeformation(0.5F)));
        event.registerLayerDefinition(BULL_ARMOR_OUTER, () -> com.example.chocolatequest.client.model.armor.CQBullArmorModel.createArmorLayer(new net.minecraft.client.model.geom.builders.CubeDeformation(1.0F)));
        event.registerLayerDefinition(BULL_ARMOR_INNER, () -> com.example.chocolatequest.client.model.armor.CQBullArmorModel.createArmorLayer(new net.minecraft.client.model.geom.builders.CubeDeformation(0.5F)));
        event.registerLayerDefinition(TURTLE_ARMOR_OUTER, () -> com.example.chocolatequest.client.model.armor.CQTurtleArmorModel.createArmorLayer(new net.minecraft.client.model.geom.builders.CubeDeformation(1.0F)));
        event.registerLayerDefinition(TURTLE_ARMOR_INNER, () -> com.example.chocolatequest.client.model.armor.CQTurtleArmorModel.createArmorLayer(new net.minecraft.client.model.geom.builders.CubeDeformation(0.5F)));
        event.registerLayerDefinition(SPIDER_ARMOR_OUTER, () -> com.example.chocolatequest.client.model.armor.SpiderArmorModel.createBodyLayer(new net.minecraft.client.model.geom.builders.CubeDeformation(1.02F)));
        event.registerLayerDefinition(SPIDER_ARMOR_INNER, () -> com.example.chocolatequest.client.model.armor.SpiderArmorModel.createBodyLayer(new net.minecraft.client.model.geom.builders.CubeDeformation(0.5F)));
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            io.wispforest.accessories.api.client.AccessoriesRendererRegistry.registerNoRenderer(com.example.chocolatequest.registry.ModItems.CAPE_GOLEM.get());
            io.wispforest.accessories.api.client.AccessoriesRendererRegistry.registerNoRenderer(com.example.chocolatequest.registry.ModItems.CAPE_ILLAGER.get());
            io.wispforest.accessories.api.client.AccessoriesRendererRegistry.registerNoRenderer(com.example.chocolatequest.registry.ModItems.CAPE_PIRATE.get());
            io.wispforest.accessories.api.client.AccessoriesRendererRegistry.registerNoRenderer(com.example.chocolatequest.registry.ModItems.CAPE_SKELETON.get());
            io.wispforest.accessories.api.client.AccessoriesRendererRegistry.registerNoRenderer(com.example.chocolatequest.registry.ModItems.CAPE_WALKER.get());
            io.wispforest.accessories.api.client.AccessoriesRendererRegistry.registerNoRenderer(com.example.chocolatequest.registry.ModItems.CAPE_ICEBULL.get());
            io.wispforest.accessories.api.client.AccessoriesRendererRegistry.registerNoRenderer(com.example.chocolatequest.registry.ModItems.CAPE_ENDERMAN.get());
            io.wispforest.accessories.api.client.AccessoriesRendererRegistry.registerNoRenderer(com.example.chocolatequest.registry.ModItems.CAPE_GOBLINSHAMAN.get());
            io.wispforest.accessories.api.client.AccessoriesRendererRegistry.registerNoRenderer(com.example.chocolatequest.registry.ModItems.CAPE_SPECTERLORD.get());

            net.minecraft.client.renderer.item.ItemProperties.register(com.example.chocolatequest.registry.ModItems.SHIELD_BULL.get(), net.minecraft.resources.ResourceLocation.withDefaultNamespace("blocking"), (stack, level, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
            net.minecraft.client.renderer.item.ItemProperties.register(com.example.chocolatequest.registry.ModItems.SHIELD_CARL.get(), net.minecraft.resources.ResourceLocation.withDefaultNamespace("blocking"), (stack, level, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
            net.minecraft.client.renderer.item.ItemProperties.register(com.example.chocolatequest.registry.ModItems.SHIELD_DRAGONSLAYER.get(), net.minecraft.resources.ResourceLocation.withDefaultNamespace("blocking"), (stack, level, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
            net.minecraft.client.renderer.item.ItemProperties.register(com.example.chocolatequest.registry.ModItems.SHIELD_FIRE.get(), net.minecraft.resources.ResourceLocation.withDefaultNamespace("blocking"), (stack, level, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
            net.minecraft.client.renderer.item.ItemProperties.register(com.example.chocolatequest.registry.ModItems.SHIELD_GOBLIN.get(), net.minecraft.resources.ResourceLocation.withDefaultNamespace("blocking"), (stack, level, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
            net.minecraft.client.renderer.item.ItemProperties.register(com.example.chocolatequest.registry.ModItems.SHIELD_MONKING.get(), net.minecraft.resources.ResourceLocation.withDefaultNamespace("blocking"), (stack, level, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
            net.minecraft.client.renderer.item.ItemProperties.register(com.example.chocolatequest.registry.ModItems.SHIELD_MOON.get(), net.minecraft.resources.ResourceLocation.withDefaultNamespace("blocking"), (stack, level, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
            net.minecraft.client.renderer.item.ItemProperties.register(com.example.chocolatequest.registry.ModItems.SHIELD_MUMMY.get(), net.minecraft.resources.ResourceLocation.withDefaultNamespace("blocking"), (stack, level, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
            net.minecraft.client.renderer.item.ItemProperties.register(com.example.chocolatequest.registry.ModItems.SHIELD_PIGMAN.get(), net.minecraft.resources.ResourceLocation.withDefaultNamespace("blocking"), (stack, level, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
            net.minecraft.client.renderer.item.ItemProperties.register(com.example.chocolatequest.registry.ModItems.SHIELD_PIRATE.get(), net.minecraft.resources.ResourceLocation.withDefaultNamespace("blocking"), (stack, level, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
            net.minecraft.client.renderer.item.ItemProperties.register(com.example.chocolatequest.registry.ModItems.SHIELD_PIRATE2.get(), net.minecraft.resources.ResourceLocation.withDefaultNamespace("blocking"), (stack, level, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
            net.minecraft.client.renderer.item.ItemProperties.register(com.example.chocolatequest.registry.ModItems.SHIELD_RAINBOW.get(), net.minecraft.resources.ResourceLocation.withDefaultNamespace("blocking"), (stack, level, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
            net.minecraft.client.renderer.item.ItemProperties.register(com.example.chocolatequest.registry.ModItems.SHIELD_REFLECTIVE.get(), net.minecraft.resources.ResourceLocation.withDefaultNamespace("blocking"), (stack, level, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
            net.minecraft.client.renderer.item.ItemProperties.register(com.example.chocolatequest.registry.ModItems.SHIELD_RUSTED.get(), net.minecraft.resources.ResourceLocation.withDefaultNamespace("blocking"), (stack, level, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
            net.minecraft.client.renderer.item.ItemProperties.register(com.example.chocolatequest.registry.ModItems.SHIELD_SKELETON_FRIENDS.get(), net.minecraft.resources.ResourceLocation.withDefaultNamespace("blocking"), (stack, level, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
            net.minecraft.client.renderer.item.ItemProperties.register(com.example.chocolatequest.registry.ModItems.SHIELD_SPECTER.get(), net.minecraft.resources.ResourceLocation.withDefaultNamespace("blocking"), (stack, level, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
            net.minecraft.client.renderer.item.ItemProperties.register(com.example.chocolatequest.registry.ModItems.SHIELD_SPIDER.get(), net.minecraft.resources.ResourceLocation.withDefaultNamespace("blocking"), (stack, level, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
            net.minecraft.client.renderer.item.ItemProperties.register(com.example.chocolatequest.registry.ModItems.SHIELD_SUN.get(), net.minecraft.resources.ResourceLocation.withDefaultNamespace("blocking"), (stack, level, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
            net.minecraft.client.renderer.item.ItemProperties.register(com.example.chocolatequest.registry.ModItems.SHIELD_TOMB.get(), net.minecraft.resources.ResourceLocation.withDefaultNamespace("blocking"), (stack, level, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
            net.minecraft.client.renderer.item.ItemProperties.register(com.example.chocolatequest.registry.ModItems.SHIELD_TRITON.get(), net.minecraft.resources.ResourceLocation.withDefaultNamespace("blocking"), (stack, level, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
            net.minecraft.client.renderer.item.ItemProperties.register(com.example.chocolatequest.registry.ModItems.SHIELD_TURTLE.get(), net.minecraft.resources.ResourceLocation.withDefaultNamespace("blocking"), (stack, level, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
            net.minecraft.client.renderer.item.ItemProperties.register(com.example.chocolatequest.registry.ModItems.SHIELD_WALKER.get(), net.minecraft.resources.ResourceLocation.withDefaultNamespace("blocking"), (stack, level, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
            net.minecraft.client.renderer.item.ItemProperties.register(com.example.chocolatequest.registry.ModItems.SHIELD_WARPED.get(), net.minecraft.resources.ResourceLocation.withDefaultNamespace("blocking"), (stack, level, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
            net.minecraft.client.renderer.item.ItemProperties.register(com.example.chocolatequest.registry.ModItems.SHIELD_ZOMBIE.get(), net.minecraft.resources.ResourceLocation.withDefaultNamespace("blocking"), (stack, level, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
            net.minecraft.client.renderer.item.ItemProperties.register(com.example.chocolatequest.registry.ModItems.SHIELD_WALKER_KING.get(), net.minecraft.resources.ResourceLocation.withDefaultNamespace("blocking"), (stack, level, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
        });
    }

    @SubscribeEvent
    public static void addLayers(EntityRenderersEvent.AddLayers event) {
        for (net.minecraft.client.resources.PlayerSkin.Model skin : event.getSkins()) {
            net.minecraft.client.renderer.entity.player.PlayerRenderer renderer = event.getSkin(skin);
            if (renderer != null) {
                renderer.addLayer(new com.example.chocolatequest.client.render.layer.CustomCapeLayer(renderer));
            }
        }
    }

    @SubscribeEvent
    public static void registerItemColors(net.neoforged.neoforge.client.event.RegisterColorHandlersEvent.Item event) {
        net.minecraft.client.color.item.ItemColor rainbowColor = (stack, tintIndex) -> {
            if (tintIndex > 0) return -1;
            
            int color = -1;
            net.minecraft.world.item.component.DyedItemColor component = stack.get(net.minecraft.core.component.DataComponents.DYED_COLOR);
            if (component != null) {
                int rawColor = component.rgb();
                net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
                if (mc.level != null) {
                    if ((rawColor >> 28 & 1) == 1) {
                        float j = 1530.0F / (float) Math.max(1, (rawColor >> 16 & 255));
                        float s = (rawColor >> 8 & 255) / 255.0F;
                        float b = (rawColor & 255) / 255.0F;
                        int hsb = java.awt.Color.HSBtoRGB((mc.level.getGameTime() + mc.getTimer().getGameTimeDeltaPartialTick(true)) % j / j, s, b);
                        return hsb & 0x00FFFFFF | (rawColor & 0xFF000000);
                    } else if ((rawColor >> 24 & 15) > 0) {
                        float f = 0.5F + 0.5F * net.minecraft.util.Mth.sin((mc.level.getGameTime() + mc.getTimer().getGameTimeDeltaPartialTick(true)) / 15.0F * (rawColor >> 25 & 15));
                        int r = Math.round((rawColor >> 16 & 255) * f);
                        int g = Math.round((rawColor >> 8 & 255) * f);
                        int b = Math.round((rawColor & 255) * f);
                        return r << 16 | g << 8 | b | (rawColor & 0xFF000000);
                    }
                }
                return rawColor | 0xFF000000;
            }
            
            if (stack.is(com.example.chocolatequest.registry.ModItems.DIAMOND_DYEABLE_HELMET.get()) ||
                stack.is(com.example.chocolatequest.registry.ModItems.DIAMOND_DYEABLE_CHESTPLATE.get()) ||
                stack.is(com.example.chocolatequest.registry.ModItems.DIAMOND_DYEABLE_LEGGINGS.get()) ||
                stack.is(com.example.chocolatequest.registry.ModItems.DIAMOND_DYEABLE_BOOTS.get())) {
                return 0xFFA06540;
            } else {
                return 0xFFA06540;
            }
        };

        event.register(rainbowColor, 
            com.example.chocolatequest.registry.ModItems.IRON_DYEABLE_HELMET.get(),
            com.example.chocolatequest.registry.ModItems.IRON_DYEABLE_CHESTPLATE.get(),
            com.example.chocolatequest.registry.ModItems.IRON_DYEABLE_LEGGINGS.get(),
            com.example.chocolatequest.registry.ModItems.IRON_DYEABLE_BOOTS.get(),
            com.example.chocolatequest.registry.ModItems.DIAMOND_DYEABLE_HELMET.get(),
            com.example.chocolatequest.registry.ModItems.DIAMOND_DYEABLE_CHESTPLATE.get(),
            com.example.chocolatequest.registry.ModItems.DIAMOND_DYEABLE_LEGGINGS.get(),
            com.example.chocolatequest.registry.ModItems.DIAMOND_DYEABLE_BOOTS.get()
        );

        // Those portraits must stay untinted, while real boss eggs need both
        // SpawnEggItem colors and an untouched crown layer (tint index 2).
        net.minecraft.client.color.item.ItemColor untintedColor = (stack, tintIndex) -> -1;
        event.register(untintedColor,
                com.example.chocolatequest.registry.ModItems.CQ_ZOMBIE_SPAWN_EGG.get(),
                com.example.chocolatequest.registry.ModItems.CQ_SKELETON_SPAWN_EGG.get(),
                com.example.chocolatequest.registry.ModItems.CQ_SPECTER_SPAWN_EGG.get(),
                com.example.chocolatequest.registry.ModItems.CQ_ENDERMAN_SPAWN_EGG.get(),
                com.example.chocolatequest.registry.ModItems.CQ_PIRATE_SPAWN_EGG.get(),
                com.example.chocolatequest.registry.ModItems.CQ_GREMLIN_SPAWN_EGG.get(),
                com.example.chocolatequest.registry.ModItems.CQ_BOARMAN_SPAWN_EGG.get(),
                com.example.chocolatequest.registry.ModItems.CQ_DUMMY_SPAWN_EGG.get(),
                com.example.chocolatequest.registry.ModItems.CQ_DWARF_SPAWN_EGG.get(),
                com.example.chocolatequest.registry.ModItems.CQ_GOBLIN_SPAWN_EGG.get(),
                com.example.chocolatequest.registry.ModItems.CQ_GOLEM_SPAWN_EGG.get(),
                com.example.chocolatequest.registry.ModItems.CQ_HUMAN_SPAWN_EGG.get(),
                com.example.chocolatequest.registry.ModItems.CQ_ILLAGER_SPAWN_EGG.get(),
                com.example.chocolatequest.registry.ModItems.CQ_MANDRIL_SPAWN_EGG.get(),
                com.example.chocolatequest.registry.ModItems.CQ_MINOTAUR_SPAWN_EGG.get(),
                com.example.chocolatequest.registry.ModItems.CQ_MUMMY_SPAWN_EGG.get(),
                com.example.chocolatequest.registry.ModItems.CQ_NPC_SPAWN_EGG.get(),
                com.example.chocolatequest.registry.ModItems.CQ_OGRE_SPAWN_EGG.get(),
                com.example.chocolatequest.registry.ModItems.CQ_ORC_SPAWN_EGG.get(),
                com.example.chocolatequest.registry.ModItems.CQ_TRITON_SPAWN_EGG.get(),
                com.example.chocolatequest.registry.ModItems.CQ_WALKER_SPAWN_EGG.get());

        net.minecraft.client.color.item.ItemColor bossEggColor = (stack, tintIndex) -> {
            if (tintIndex < 0 || tintIndex > 1
                    || !(stack.getItem() instanceof net.minecraft.world.item.SpawnEggItem egg)) return -1;
            // item renderer expects ARGB. Without an explicit alpha channel
            // both egg layers are fully transparent and only the crown shows.
            return 0xFF000000 | egg.getColor(tintIndex);
        };
        event.register(bossEggColor,
                com.example.chocolatequest.registry.ModItems.SPECTER_LORD_SPAWN_EGG.get(),
                com.example.chocolatequest.registry.ModItems.SHELOB_SPAWN_EGG.get(),
                com.example.chocolatequest.registry.ModItems.EXTERMINATOR_SPAWN_EGG.get(),
                com.example.chocolatequest.registry.ModItems.CQ_BULL_SPAWN_EGG.get(),
                com.example.chocolatequest.registry.ModItems.CQ_ICE_BULL_SPAWN_EGG.get(),
                com.example.chocolatequest.registry.ModItems.CQ_PIRATE_CAPTAIN_SPAWN_EGG.get(),
                com.example.chocolatequest.registry.ModItems.CQ_PIRATE_PARROT_SPAWN_EGG.get(),
                com.example.chocolatequest.registry.ModItems.BOARMAGE_SPAWN_EGG.get(),
                com.example.chocolatequest.registry.ModItems.LICH_SPAWN_EGG.get(),
                com.example.chocolatequest.registry.ModItems.NECROMANCER_SPAWN_EGG.get(),
                com.example.chocolatequest.registry.ModItems.GREMLIN_SHAMAN_SPAWN_EGG.get(),
                com.example.chocolatequest.registry.ModItems.CQR_DRAGON_SPAWN_EGG.get(),
                com.example.chocolatequest.registry.ModItems.MONKING_SPAWN_EGG.get(),
                com.example.chocolatequest.registry.ModItems.WALKER_KING_SPAWN_EGG.get(),
                com.example.chocolatequest.registry.ModItems.GIANT_TORTOISE_SPAWN_EGG.get(),
                com.example.chocolatequest.registry.ModItems.ENDERMENACE_SPAWN_EGG.get(),
                com.example.chocolatequest.registry.ModItems.SHULKER_GOLEM_SPAWN_EGG.get());
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {

        event.registerEntityRenderer(ModEntities.GIANT_SILVERFISH_NORMAL.get(), com.example.chocolatequest.client.render.entity.mounts.RenderGiantSilverfishNormal::new);
        event.registerEntityRenderer(ModEntities.GIANT_SILVERFISH_RED.get(), com.example.chocolatequest.client.render.entity.mounts.RenderGiantSilverfishRed::new);
        event.registerEntityRenderer(ModEntities.GIANT_SILVERFISH_GREEN.get(), com.example.chocolatequest.client.render.entity.mounts.RenderGiantSilverfishGreen::new);

        event.registerEntityRenderer(ModEntities.BUBBLE_PROJECTILE.get(), net.minecraft.client.renderer.entity.ThrownItemRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.EXPORTER_CHEST_FIXED.get(), TileEntityExporterChestRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.EXPORTER_CHEST_CUSTOM.get(), TileEntityExporterChestRenderer::new);
    }

    @SubscribeEvent
    public static void registerMenuScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.ALCHEMY_BAG.get(), ScreenAlchemyBag::new);
    }
}
