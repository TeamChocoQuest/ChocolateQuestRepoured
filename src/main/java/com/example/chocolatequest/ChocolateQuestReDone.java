package com.example.chocolatequest;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import com.example.chocolatequest.registry.ModBlocks;
import com.example.chocolatequest.registry.ModCreativeTabs;
import com.example.chocolatequest.registry.ModItems;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(ChocolateQuestReDone.MODID)
public class ChocolateQuestReDone {
    public static final String MODID = "cqrepoured";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ChocolateQuestReDone(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        com.example.chocolatequest.registry.ModDataComponents.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
        com.example.chocolatequest.registry.ModMenuTypes.MENUS.register(modEventBus);
        com.example.chocolatequest.registry.ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        com.example.chocolatequest.registry.ModEntities.register(modEventBus);
        com.example.chocolatequest.registry.ModArmorMaterials.ARMOR_MATERIALS.register(modEventBus);
        com.example.chocolatequest.registry.ModSounds.register(modEventBus);
        com.example.chocolatequest.registry.ModAttachments.ATTACHMENTS.register(modEventBus);
        com.example.chocolatequest.registry.ModStructureProcessors.register(modEventBus);
        com.example.chocolatequest.registry.ModStructures.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("Chocolate Quest Repoured Common Setup");
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Chocolate Quest Repoured Server Starting");
    }
}
