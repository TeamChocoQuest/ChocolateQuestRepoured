package com.example.chocolatequest;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = ChocolateQuestReDone.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = ChocolateQuestReDone.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ChocolateQuestReDoneClient {
    public ChocolateQuestReDoneClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            net.minecraft.client.renderer.item.ItemProperties.register(com.example.chocolatequest.registry.ModItems.HOOKSHOT.get(), ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "hook_out"), (stack, level, entity, seed) -> {
                return entity != null && entity.getUseItem() == stack ? 1.0F : 0.0F;
            });
            net.minecraft.client.renderer.item.ItemProperties.register(com.example.chocolatequest.registry.ModItems.LONGSHOT.get(), ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "hook_out"), (stack, level, entity, seed) -> {
                return entity != null && entity.getUseItem() == stack ? 1.0F : 0.0F;
            });
            net.minecraft.client.renderer.item.ItemProperties.register(com.example.chocolatequest.registry.ModItems.SPIDER_HOOK.get(), ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "hook_out"), (stack, level, entity, seed) -> {
                return entity != null && entity.getUseItem() == stack ? 1.0F : 0.0F;
            });
        });
        ChocolateQuestReDone.LOGGER.info("HELLO FROM CLIENT SETUP");
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(net.neoforged.neoforge.client.event.EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(com.example.chocolatequest.client.render.entity.projectile.RenderProjectileCannonBall.LAYER_LOCATION, com.example.chocolatequest.client.model.entity.ModelCannonBall::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(com.example.chocolatequest.registry.ModEntities.CQR_DRAGON.get(), com.example.chocolatequest.client.render.boss.RenderCQRDragon::new);
        event.registerEntityRenderer(com.example.chocolatequest.registry.ModEntities.CQ_ZOMBIE.get(), com.example.chocolatequest.client.render.RenderCQRZombie::new);
        event.registerEntityRenderer(com.example.chocolatequest.registry.ModEntities.CQ_SKELETON.get(), com.example.chocolatequest.client.render.RenderCQRSkeleton::new);
        event.registerEntityRenderer(com.example.chocolatequest.registry.ModEntities.CQ_SPECTER.get(), com.example.chocolatequest.client.render.RenderCQRSpectre::new);
        event.registerEntityRenderer(com.example.chocolatequest.registry.ModEntities.CQ_ENDERMAN.get(), com.example.chocolatequest.client.render.RenderCQREnderman::new);
        event.registerEntityRenderer(com.example.chocolatequest.registry.ModEntities.CQ_PIRATE.get(), com.example.chocolatequest.client.render.RenderCQRPirate::new);
        
        event.registerEntityRenderer(com.example.chocolatequest.registry.ModEntities.CQ_PIRATE_CAPTAIN.get(), com.example.chocolatequest.client.render.RenderCQRPirateCaptain::new);
        event.registerEntityRenderer(com.example.chocolatequest.registry.ModEntities.CQ_PIRATE_PARROT.get(), net.minecraft.client.renderer.entity.ParrotRenderer::new);
        event.registerEntityRenderer(com.example.chocolatequest.registry.ModEntities.CQ_GREMLIN.get(), com.example.chocolatequest.client.render.RenderCQRGremlin::new);
        event.registerEntityRenderer(com.example.chocolatequest.registry.ModEntities.GREMLIN_SHAMAN.get(), com.example.chocolatequest.client.render.RenderCQRGremlin::new);
        
        event.registerEntityRenderer(com.example.chocolatequest.registry.ModEntities.CQ_BOARMAN.get(), com.example.chocolatequest.client.render.RenderCQRBoarman::new);
        event.registerEntityRenderer(com.example.chocolatequest.registry.ModEntities.CQ_DUMMY.get(), com.example.chocolatequest.client.render.RenderCQRDummy::new);
        event.registerEntityRenderer(com.example.chocolatequest.registry.ModEntities.CQ_DWARF.get(), com.example.chocolatequest.client.render.RenderCQRDwarf::new);
        event.registerEntityRenderer(com.example.chocolatequest.registry.ModEntities.CQ_GOBLIN.get(), com.example.chocolatequest.client.render.RenderCQRGoblin::new);
        event.registerEntityRenderer(com.example.chocolatequest.registry.ModEntities.CQ_GOLEM.get(), com.example.chocolatequest.client.render.RenderCQRGolem::new);
        event.registerEntityRenderer(com.example.chocolatequest.registry.ModEntities.CQ_HUMAN.get(), com.example.chocolatequest.client.render.RenderCQRHuman::new);
        event.registerEntityRenderer(com.example.chocolatequest.registry.ModEntities.CQ_ILLAGER.get(), com.example.chocolatequest.client.render.RenderCQRIllager::new);
        event.registerEntityRenderer(com.example.chocolatequest.registry.ModEntities.CQ_MANDRIL.get(), com.example.chocolatequest.client.render.RenderCQRMandril::new);
        event.registerEntityRenderer(com.example.chocolatequest.registry.ModEntities.CQ_MINOTAUR.get(), com.example.chocolatequest.client.render.RenderCQRMinotaur::new);
        event.registerEntityRenderer(com.example.chocolatequest.registry.ModEntities.CQ_MUMMY.get(), com.example.chocolatequest.client.render.RenderCQRMummy::new);
        event.registerEntityRenderer(com.example.chocolatequest.registry.ModEntities.CQ_NPC.get(), com.example.chocolatequest.client.render.RenderCQRNPC::new);
        event.registerEntityRenderer(com.example.chocolatequest.registry.ModEntities.CQ_OGRE.get(), com.example.chocolatequest.client.render.RenderCQROgre::new);
        event.registerEntityRenderer(com.example.chocolatequest.registry.ModEntities.CQ_ORC.get(), com.example.chocolatequest.client.render.RenderCQROrc::new);
        event.registerEntityRenderer(com.example.chocolatequest.registry.ModEntities.CQ_TRITON.get(), com.example.chocolatequest.client.render.RenderCQRTriton::new);
        event.registerEntityRenderer(com.example.chocolatequest.registry.ModEntities.CQ_WALKER.get(), com.example.chocolatequest.client.render.RenderCQRWalker::new);

        event.registerEntityRenderer(com.example.chocolatequest.registry.ModEntities.CQ_BULL.get(), com.example.chocolatequest.client.render.boss.CQBullRenderer::new);
        event.registerEntityRenderer(com.example.chocolatequest.registry.ModEntities.CQ_ICE_BULL.get(), com.example.chocolatequest.client.render.boss.CQIceBullRenderer::new);

        event.registerEntityRenderer(com.example.chocolatequest.registry.ModEntities.PROJECTILE_HOOKSHOT.get(), com.example.chocolatequest.client.render.entity.projectile.RenderHookShotHook::new);
        event.registerEntityRenderer(com.example.chocolatequest.registry.ModEntities.PROJECTILE_SPIDER_HOOK.get(), com.example.chocolatequest.client.render.entity.projectile.RenderSpiderHook::new);
        event.registerEntityRenderer(com.example.chocolatequest.registry.ModEntities.CQR_ARROW.get(), com.example.chocolatequest.client.renderer.CQRArrowRenderer::new);
        event.registerEntityRenderer(com.example.chocolatequest.registry.ModEntities.CQR_POTION.get(), net.minecraft.client.renderer.entity.ThrownItemRenderer::new);

        event.registerEntityRenderer(com.example.chocolatequest.registry.ModEntities.PROJECTILE_BULLET.get(), net.minecraft.client.renderer.entity.NoopRenderer::new);
        event.registerEntityRenderer(com.example.chocolatequest.registry.ModEntities.DARK_PROJECTILE.get(), net.minecraft.client.renderer.entity.ThrownItemRenderer::new);
        event.registerEntityRenderer(com.example.chocolatequest.registry.ModEntities.ENDER_CALAMITY_ORB.get(), context -> new net.minecraft.client.renderer.entity.ThrownItemRenderer<>(context, 1.8F, true));
        event.registerEntityRenderer(com.example.chocolatequest.registry.ModEntities.ENDER_BLOCK_PROJECTILE.get(), context -> new net.minecraft.client.renderer.entity.ThrownItemRenderer<>(context, 2.0F, true));
        event.registerEntityRenderer(com.example.chocolatequest.registry.ModEntities.POISON_PROJECTILE.get(), net.minecraft.client.renderer.entity.ThrownItemRenderer::new);
        event.registerEntityRenderer(com.example.chocolatequest.registry.ModEntities.WIND_PROJECTILE.get(), net.minecraft.client.renderer.entity.ThrownItemRenderer::new);
        event.registerEntityRenderer(com.example.chocolatequest.registry.ModEntities.FLYING_HEART.get(), net.minecraft.client.renderer.entity.ThrownItemRenderer::new);
        event.registerEntityRenderer(com.example.chocolatequest.registry.ModEntities.BUBBLE_PROJECTILE.get(), net.minecraft.client.renderer.entity.ThrownItemRenderer::new);
        event.registerEntityRenderer(com.example.chocolatequest.registry.ModEntities.BUBBLE_TRAP.get(), context -> new net.minecraft.client.renderer.entity.ThrownItemRenderer<>(context, 3.0F, true));

        event.registerEntityRenderer(com.example.chocolatequest.registry.ModEntities.EXTERMINATOR.get(), com.example.chocolatequest.client.render.boss.CQExterminatorRenderer::new);
        event.registerEntityRenderer(com.example.chocolatequest.registry.ModEntities.LICH.get(), com.example.chocolatequest.client.renderer.EntityCQRLichRenderer::new);
        event.registerEntityRenderer(com.example.chocolatequest.registry.ModEntities.NECROMANCER.get(), com.example.chocolatequest.client.renderer.EntityCQRNecromancerRenderer::new);
        event.registerEntityRenderer(com.example.chocolatequest.registry.ModEntities.BOARMAGE.get(), com.example.chocolatequest.client.renderer.EntityCQRBoarmageRenderer::new);
        event.registerEntityRenderer(com.example.chocolatequest.registry.ModEntities.MONKING.get(), com.example.chocolatequest.client.render.boss.RenderCQRMonking::new);
        event.registerEntityRenderer(com.example.chocolatequest.registry.ModEntities.WALKER_KING.get(), com.example.chocolatequest.client.render.boss.RenderCQRWalkerKing::new);
        event.registerEntityRenderer(com.example.chocolatequest.registry.ModEntities.GIANT_TORTOISE.get(), com.example.chocolatequest.client.render.boss.RenderCQRGiantTortoise::new);
        event.registerEntityRenderer(com.example.chocolatequest.registry.ModEntities.ENDERMENACE.get(), com.example.chocolatequest.client.render.boss.RenderCQREndermenace::new);
        event.registerEntityRenderer(com.example.chocolatequest.registry.ModEntities.SHULKER_GOLEM.get(), com.example.chocolatequest.client.render.boss.RenderCQRShulkerGolem::new);

        event.registerEntityRenderer(com.example.chocolatequest.registry.ModEntities.CQ_MONK.get(), com.example.chocolatequest.client.render.npc.RenderCQRMonk::new);
        event.registerEntityRenderer(com.example.chocolatequest.registry.ModEntities.CQ_MERCHANT.get(), com.example.chocolatequest.client.render.npc.RenderCQRMerchant::new);
        event.registerEntityRenderer(com.example.chocolatequest.registry.ModEntities.CQ_PRISONER_KNIGHT.get(), com.example.chocolatequest.client.render.npc.RenderCQRPrisonerKnight::new);
        
        event.registerEntityRenderer(com.example.chocolatequest.registry.ModEntities.EXTERMINATOR_HAND_LASER.get(), com.example.chocolatequest.client.render.entity.RenderLaser::new);
        event.registerEntityRenderer(com.example.chocolatequest.registry.ModEntities.ENDERMENACE_LASER.get(), com.example.chocolatequest.client.render.entity.RenderLaser::new);
        event.registerEntityRenderer(com.example.chocolatequest.registry.ModEntities.PROJECTILE_CANNON_BALL.get(), com.example.chocolatequest.client.render.entity.projectile.RenderProjectileCannonBall::new);

        event.registerBlockEntityRenderer(com.example.chocolatequest.registry.ModBlockEntities.FORCE_FIELD_NEXUS.get(), context -> new com.example.chocolatequest.client.render.ForceFieldNexusRenderer());
        event.registerBlockEntityRenderer(com.example.chocolatequest.registry.ModBlockEntities.NEXUS_CORE.get(), context -> new com.example.chocolatequest.client.render.NexusCoreRenderer());
    }

    @SubscribeEvent
    public static void registerScreens(net.neoforged.neoforge.client.event.RegisterMenuScreensEvent event) {
        event.register(com.example.chocolatequest.registry.ModMenuTypes.BADGE.get(), com.example.chocolatequest.client.gui.BadgeScreen::new);
        event.register(com.example.chocolatequest.registry.ModMenuTypes.CQR_ENTITY_EDITOR.get(), com.example.chocolatequest.client.gui.npceditor.GuiCQREntity::new);
        event.register(
            com.example.chocolatequest.registry.ModMenuTypes.CQR_MERCHANT.get(),
            (net.minecraft.world.inventory.MerchantMenu menu, net.minecraft.world.entity.player.Inventory inv, net.minecraft.network.chat.Component title) -> 
                new com.example.chocolatequest.client.gui.npceditor.GuiMerchant((com.example.chocolatequest.inventory.ContainerCQRMerchant)menu, inv, title)
        );
        event.register(com.example.chocolatequest.registry.ModMenuTypes.SPAWNER_MENU.get(), com.example.chocolatequest.client.gui.SpawnerScreen::new);
    }
}
