package com.example.chocolatequest.client.gui;

import com.example.chocolatequest.ChocolateQuestReDone;
import com.example.chocolatequest.registry.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

@EventBusSubscriber(modid = ChocolateQuestReDone.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public class ScouterHudOverlay {

    @SubscribeEvent
    public static void onRenderGuiPost(RenderGuiEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null || mc.level == null) return;

        ItemStack headStack = player.getItemBySlot(EquipmentSlot.HEAD);
        if (headStack.isEmpty() || !headStack.is(ModItems.SCOUTER_HELMET.get())) {
            return;
        }

        HitResult crosshairTarget = mc.hitResult;
        if (crosshairTarget != null && crosshairTarget.getType() == HitResult.Type.ENTITY) {
            EntityHitResult entityTarget = (EntityHitResult) crosshairTarget;
            if (entityTarget.getEntity() instanceof LivingEntity living) {
                renderScouterHUD(event.getGuiGraphics(), mc, living);
            }
        }
    }

    private static void renderScouterHUD(GuiGraphics graphics, Minecraft mc, LivingEntity target) {
        Font font = mc.font;
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        String name = target.getDisplayName().getString();
        float health = target.getHealth();
        float maxHealth = target.getMaxHealth();
        int armor = target.getArmorValue();

        int boxWidth = 150;
        int boxHeight = 36;

        int hotbarLeft = screenWidth / 2 - 91;
        int x = hotbarLeft - boxWidth - 10;
        if (x < 10) {
            x = 10;
        }
        int y = screenHeight - boxHeight - 12;

        graphics.fill(x - 2, y - 2, x + boxWidth + 2, y + boxHeight + 2, 0xAA004444);
        graphics.fill(x, y, x + boxWidth, y + boxHeight, 0xDD001217);

        graphics.drawString(font, "SCOUTER", x + 5, y + 4, 0x00FFCC, false);
        
        String displayName = font.plainSubstrByWidth(name, boxWidth - 60);
        graphics.drawString(font, displayName, x + 55, y + 4, 0xFFFFFF, true);

        int barX = x + 5;
        int barY = y + 16;
        int barWidth = boxWidth - 10;
        int barHeight = 8;
        float hpPercent = Math.max(0.0f, Math.min(1.0f, health / maxHealth));
        int filledWidth = (int) (barWidth * hpPercent);

        graphics.fill(barX, barY, barX + barWidth, barY + barHeight, 0xFF222222);
        int barColor = hpPercent > 0.5f ? 0xFF00FF55 : (hpPercent > 0.25f ? 0xFFFFAA00 : 0xFFFF2222);
        graphics.fill(barX, barY, barX + filledWidth, barY + barHeight, barColor);

        String hpStr = String.format("%.1f / %.1f HP", health, maxHealth);
        graphics.drawString(font, hpStr, barX + 2, barY, 0xFFFFFFFF, true);

        // Armor info at bottom right of box
        if (armor > 0) {
            String armorStr = "Armor: " + armor;
            graphics.drawString(font, armorStr, x + boxWidth - font.width(armorStr) - 5, y + 26, 0xFFFFAA00, false);
        }
    }
}
