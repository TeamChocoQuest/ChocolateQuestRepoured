package com.example.chocolatequest.client.gui.npceditor;

import com.example.chocolatequest.inventory.ContainerCQRMerchant;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.MerchantScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiMerchant extends MerchantScreen {

    public GuiMerchant(ContainerCQRMerchant menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        super.renderBg(guiGraphics, partialTicks, mouseX, mouseY);
        
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        int rep = 0;
        net.minecraft.world.entity.player.Player player = this.minecraft.player;
        if (player != null) {
            try {
                com.example.chocolatequest.faction.EDefaultFaction faction = com.example.chocolatequest.faction.EDefaultFaction.valueOf(((com.example.chocolatequest.inventory.ContainerCQRMerchant)this.getMenu()).factionName);
                rep = player.getData(com.example.chocolatequest.registry.ModAttachments.PLAYER_REPUTATION).getReputation(faction);
            } catch (IllegalArgumentException e) {
                // Ignore
            }
        }
        
        int barX = x + 80;
        int barY = y - 10;
        int barWidth = 110;
        int center = barX + barWidth / 2;
        int clampedRep = Math.max(-100, Math.min(100, rep));
        int reputationWidth = Math.round(Math.abs(clampedRep) * (barWidth / 2.0F) / 100.0F);

        guiGraphics.fill(barX - 1, barY - 1, barX + barWidth + 1, barY + 9, 0xFF202020);
        guiGraphics.fill(barX, barY, barX + barWidth, barY + 8, 0xFF555555);
        if (clampedRep < 0) {
            guiGraphics.fill(center - reputationWidth, barY, center, barY + 8, 0xFFFF5555);
        } else if (clampedRep > 0) {
            guiGraphics.fill(center, barY, center + reputationWidth, barY + 8, 0xFF55DD55);
        }
        guiGraphics.fill(center, barY - 1, center + 1, barY + 9, 0xFFFFFFFF);

        Component standing = getStanding(rep);
        guiGraphics.drawCenteredString(this.font,
                Component.translatable("gui.cqrepoured.merchant.reputation", rep, standing),
                center, y - 22, getStandingColor(rep));

        String tradeProfile = ((ContainerCQRMerchant) this.getMenu()).tradeProfile;
        int nextUnlock = getNextUnlock(rep, tradeProfile);
        Component unlockText = "tavern".equals(tradeProfile)
                ? Component.translatable("gui.cqrepoured.merchant.tavern_stock")
                : nextUnlock > 0
                    ? Component.translatable("gui.cqrepoured.merchant.next_unlock", nextUnlock)
                    : Component.translatable("gui.cqrepoured.merchant.all_unlocked");
        guiGraphics.drawCenteredString(this.font, unlockText, center, y + 2, 0xFFB0B0B0);
    }

    private Component getStanding(int reputation) {
        String standing;
        if (reputation <= -50) standing = "hostile";
        else if (reputation < 0) standing = "distrusted";
        else if (reputation < 20) standing = "neutral";
        else if (reputation < 60) standing = "friendly";
        else standing = "honored";
        return Component.translatable("gui.cqrepoured.merchant.standing." + standing);
    }

    private int getStandingColor(int reputation) {
        if (reputation <= -50) return 0xFFFF5555;
        if (reputation < 0) return 0xFFFFAA55;
        if (reputation < 20) return 0xFFFFFF55;
        if (reputation < 60) return 0xFF55FF55;
        return 0xFF55FFFF;
    }

    private int getNextUnlock(int reputation, String profile) {
        int[] thresholds = "monk".equals(profile) ? new int[]{15, 35, 60} : new int[]{10, 30, 60};
        for (int threshold : thresholds) {
            if (reputation < threshold) {
                return threshold;
            }
        }
        return -1;
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        // Leave empty to hide default "Trades", "Inventory" and entity name which overlap with our UI
    }
}
