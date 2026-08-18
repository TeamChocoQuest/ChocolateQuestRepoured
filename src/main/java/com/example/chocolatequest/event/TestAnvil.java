package com.example.chocolatequest.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AnvilUpdateEvent;

@EventBusSubscriber
public class TestAnvil {
    @SubscribeEvent
    public static void onAnvil(AnvilUpdateEvent event) {
        event.getPlayer();
    }
}
