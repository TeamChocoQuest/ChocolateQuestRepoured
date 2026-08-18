package com.example.chocolatequest.client.gui;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.item.EBulletType;
import com.example.chocolatequest.item.ItemRevolver;
import com.example.chocolatequest.registry.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

@EventBusSubscriber(modid = ChocolateQuestReDone.MODID, value = Dist.CLIENT)
public final class FirearmHudOverlay {

    private static final int WIDTH = 116;
    private static final int HEIGHT = 31;

    private FirearmHudOverlay() {
    }

    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.level == null || minecraft.options.hideGui) {
            return;
        }

        ItemStack firearmStack = minecraft.player.getMainHandItem();
        ItemRevolver firearm;
        if (firearmStack.getItem() instanceof ItemRevolver mainHandFirearm) {
            firearm = mainHandFirearm;
        } else {
            firearmStack = minecraft.player.getOffhandItem();
            if (!(firearmStack.getItem() instanceof ItemRevolver offhandFirearm)) {
                return;
            }
            firearm = offhandFirearm;
        }

        float partialTick = event.getPartialTick().getGameTimeDeltaPartialTick(true);
        render(event.getGuiGraphics(), minecraft, firearm, firearmStack, partialTick);
    }

    private static void render(GuiGraphics graphics, Minecraft minecraft, ItemRevolver firearm,
                               ItemStack firearmStack, float partialTick) {
        Font font = minecraft.font;
        int screenWidth = graphics.guiWidth();
        int screenHeight = graphics.guiHeight();
        int x = screenWidth / 2 + 101;
        if (x + WIDTH > screenWidth - 6) {
            x = screenWidth - WIDTH - 6;
        }
        int y = screenHeight - HEIGHT - 7;

        int loaded = firearm.getLoadedAmmo(firearmStack);
        int capacity = firearm.getCapacity();
        EBulletType ammoType = firearm.getLoadedAmmoType(firearmStack);
        float reload = firearm.getReloadProgress(firearmStack, minecraft.level, partialTick);
        graphics.fill(x - 2, y - 2, x + WIDTH + 2, y + HEIGHT + 2, 0xD0000000);
        graphics.fill(x - 1, y - 1, x + WIDTH + 1, y + HEIGHT + 1, 0xFF555555);
        graphics.fill(x, y, x + WIDTH, y + HEIGHT, 0xD0101010);

        ItemStack bullet = bulletStack(ammoType);
        graphics.renderItem(bullet, x + 7, y + 7);

        String ammo = loaded + " / " + capacity;
        Component reloading = Component.translatable("hud.cqrepoured.reloading");
        if (reload > 0.0F) {
            graphics.drawString(font, reloading, x + 27, y + 5, 0xFFFFFF55, true);
        } else {
            graphics.drawString(font, ammo, x + 27, y + 5,
                    loaded == 0 ? 0xFFFF5555 : 0xFFFFFFFF, true);
        }
        Component type = Component.translatable("hud.cqrepoured.ammo_type." + ammoType.name().toLowerCase());
        graphics.drawString(font, type, x + 27, y + 17, 0xFFAAAAAA, true);

        if (reload > 0.0F) {
            int barX = x + 2;
            int barY = y + HEIGHT - 2;
            int barWidth = WIDTH - 4;
            graphics.fill(barX, barY, barX + barWidth, barY + 1, 0xFF555555);
            graphics.fill(barX, barY, barX + Mth.floor(barWidth * reload), barY + 1, 0xFFFFFFFF);
        } else if (loaded == 0) {
            Component hint = Component.literal("[R]");
            graphics.drawString(font, hint, x + WIDTH - font.width(hint) - 4,
                    y + 5, 0xFFFFFF55, true);
        }
    }

    private static ItemStack bulletStack(EBulletType type) {
        return switch (type) {
            case IRON -> new ItemStack(ModItems.BULLET_IRON.get());
            case GOLD -> new ItemStack(ModItems.BULLET_GOLD.get());
            case DIAMOND -> new ItemStack(ModItems.BULLET_DIAMOND.get());
            case FIRE -> new ItemStack(ModItems.BULLET_FIRE.get());
        };
    }
}
