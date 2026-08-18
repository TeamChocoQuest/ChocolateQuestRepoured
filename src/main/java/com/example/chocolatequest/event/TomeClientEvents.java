package com.example.chocolatequest.event;

import com.example.chocolatequest.ChocolateQuestReDone;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(modid = ChocolateQuestReDone.MODID, value = Dist.CLIENT)
public class TomeClientEvents {

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
        if (mc.player != null && ModClientEvents.DODGE_KEY.consumeClick()) {
            net.neoforged.neoforge.network.PacketDistributor.sendToServer(new com.example.chocolatequest.network.packet.CPacketDodge());
        }
        if (mc.player != null && ModClientEvents.RELOAD_FIREARM_KEY.consumeClick()) {
            net.minecraft.world.item.ItemStack mainHand = mc.player.getMainHandItem();
            net.minecraft.world.item.ItemStack offHand = mc.player.getOffhandItem();
            if (mainHand.getItem() instanceof com.example.chocolatequest.item.ItemRevolver ||
                    offHand.getItem() instanceof com.example.chocolatequest.item.ItemRevolver) {
                net.neoforged.neoforge.network.PacketDistributor.sendToServer(new com.example.chocolatequest.network.packet.CPacketReloadFirearm());
            }
        }
    }
}
