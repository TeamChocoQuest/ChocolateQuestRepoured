package com.example.chocolatequest.client.gui.npceditor;

import com.example.chocolatequest.entity.bases.AbstractEntityCQR;
import com.example.chocolatequest.inventory.ContainerCQREntity;
import com.example.chocolatequest.network.packet.CPacketContainerClickButton;
import com.example.chocolatequest.network.packet.CPacketOpenMerchantGui;
import com.example.chocolatequest.network.packet.CPacketSyncEntity;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.client.gui.widget.ExtendedSlider;
import net.neoforged.neoforge.network.PacketDistributor;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/** Creative entity editor with explicit actions and real current values. */
public class GuiCQREntity extends AbstractContainerScreen<ContainerCQREntity> {

    private final AbstractEntityCQR entity;
    private ExtendedSlider healthScale;
    private ExtendedSlider dropHead;
    private ExtendedSlider dropChest;
    private ExtendedSlider dropLegs;
    private ExtendedSlider dropFeet;
    private ExtendedSlider dropMain;
    private ExtendedSlider dropOff;
    private ExtendedSlider sizeScale;
    private Button applyButton;
    private Button healButton;
    private Button sitButton;
    private Button clearTargetButton;
    private Button previewTradesButton;
    private Button addTradeButton;
    private Button removeTradeButton;

    public GuiCQREntity(ContainerCQREntity menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.entity = menu.getEntity();
    }

    @Override
    protected void init() {
        this.imageWidth = 356;
        this.imageHeight = 270;
        super.init();

        int x = this.leftPos + 10;
        int y = this.topPos + 34;
        int width = 150;
        this.healthScale = slider(x, y, width, "gui.cqrepoured.editor.health",
                10, 1000, Math.round(this.entity.getHealthScale() * 100.0F));
        this.dropHead = slider(x, y + 23, width, "gui.cqrepoured.editor.drop_head",
                0, 100, percent(EquipmentSlot.HEAD));
        this.dropChest = slider(x, y + 46, width, "gui.cqrepoured.editor.drop_chest",
                0, 100, percent(EquipmentSlot.CHEST));
        this.dropLegs = slider(x, y + 69, width, "gui.cqrepoured.editor.drop_legs",
                0, 100, percent(EquipmentSlot.LEGS));
        this.dropFeet = slider(x, y + 92, width, "gui.cqrepoured.editor.drop_feet",
                0, 100, percent(EquipmentSlot.FEET));
        this.dropMain = slider(x, y + 115, width, "gui.cqrepoured.editor.drop_main",
                0, 100, percent(EquipmentSlot.MAINHAND));
        this.dropOff = slider(x, y + 138, width, "gui.cqrepoured.editor.drop_off",
                0, 100, percent(EquipmentSlot.OFFHAND));
        this.sizeScale = slider(x, y + 161, width, "gui.cqrepoured.editor.size",
                25, 300, Math.round(this.entity.getSizeVariation() * 100.0F));

        int left = this.leftPos + 174;
        int right = this.leftPos + 264;
        this.applyButton = button(left, this.topPos + 101, 82,
                "gui.cqrepoured.editor.apply", b -> applyValues());
        this.healButton = button(right, this.topPos + 101, 82,
                "gui.cqrepoured.editor.heal", b -> action(1));
        this.sitButton = button(left, this.topPos + 124, 82,
                "gui.cqrepoured.editor.sit", b -> action(2));
        this.clearTargetButton = button(right, this.topPos + 124, 82,
                "gui.cqrepoured.editor.clear_target", b -> action(3));
        this.previewTradesButton = button(left, this.topPos + 147, 82,
                "gui.cqrepoured.editor.preview_trades", b -> previewTrades());
        this.addTradeButton = button(right, this.topPos + 147, 39,
                "gui.cqrepoured.editor.add_trade", b -> action(4));
        this.removeTradeButton = button(right + 43, this.topPos + 147, 39,
                "gui.cqrepoured.editor.remove_trade", b -> action(5));

        boolean creative = this.minecraft != null && this.minecraft.player != null
                && this.minecraft.player.isCreative();
        this.healthScale.active = creative;
        this.dropHead.active = creative;
        this.dropChest.active = creative;
        this.dropLegs.active = creative;
        this.dropFeet.active = creative;
        this.dropMain.active = creative;
        this.dropOff.active = creative;
        this.sizeScale.active = creative;
        this.applyButton.active = creative;
        this.healButton.active = creative;
        this.sitButton.active = creative;
        this.clearTargetButton.active = creative;

        boolean tavernMerchant = this.entity.isTavernNpc();
        this.previewTradesButton.visible = tavernMerchant;
        this.addTradeButton.visible = tavernMerchant && creative;
        this.removeTradeButton.visible = tavernMerchant && creative;
    }

    private ExtendedSlider slider(int x, int y, int width, String key,
                                  int min, int max, int current) {
        ExtendedSlider slider = new ExtendedSlider(x, y, width, 19,
                Component.translatable(key), Component.literal("%"),
                min, max, Math.max(min, Math.min(max, current)), true);
        this.addRenderableWidget(slider);
        return slider;
    }

    private Button button(int x, int y, int width, String key, Button.OnPress onPress) {
        Button button = Button.builder(Component.translatable(key), onPress)
                .bounds(x, y, width, 20).build();
        this.addRenderableWidget(button);
        return button;
    }

    private int percent(EquipmentSlot slot) {
        return Math.round(this.entity.getEditorDropChance(slot) * 100.0F);
    }

    private void applyValues() {
        PacketDistributor.sendToServer(new CPacketSyncEntity(this.entity.getId(),
                this.healthScale.getValueInt(), this.dropHead.getValueInt(),
                this.dropChest.getValueInt(), this.dropLegs.getValueInt(),
                this.dropFeet.getValueInt(), this.dropMain.getValueInt(),
                this.dropOff.getValueInt(), this.sizeScale.getValueInt()));
    }

    private void action(int action) {
        PacketDistributor.sendToServer(new CPacketContainerClickButton(action));
    }

    private void previewTrades() {
        PacketDistributor.sendToServer(new CPacketOpenMerchantGui(this.entity.getId()));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);
        this.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int x = this.leftPos;
        int y = this.topPos;
        // top-left edge and dark bottom-right edge.
        graphics.fill(x, y, x + this.imageWidth, y + this.imageHeight, 0xFF373737);
        graphics.fill(x + 1, y + 1, x + this.imageWidth - 1, y + this.imageHeight - 1, 0xFFC6C6C6);
        graphics.fill(x + 1, y + 1, x + this.imageWidth - 2, y + 2, 0xFFFFFFFF);
        graphics.fill(x + 1, y + 1, x + 2, y + this.imageHeight - 2, 0xFFFFFFFF);
        graphics.fill(x + 1, y + this.imageHeight - 2, x + this.imageWidth - 1, y + this.imageHeight - 1, 0xFF555555);
        graphics.fill(x + this.imageWidth - 2, y + 1, x + this.imageWidth - 1, y + this.imageHeight - 1, 0xFF555555);
        panel(graphics, x + 6, y + 29, x + 164, y + 226);
        panel(graphics, x + 170, y + 29, x + 350, y + 174);
        panel(graphics, x + 180, y + 184, x + 350, y + 266);

        for (net.minecraft.world.inventory.Slot slot : this.menu.slots) {
            int sx = x + slot.x;
            int sy = y + slot.y;
            graphics.fill(sx - 1, sy - 1, sx + 17, sy + 17, 0xFFFFFFFF);
            graphics.fill(sx - 1, sy - 1, sx + 17, sy, 0xFF373737);
            graphics.fill(sx - 1, sy - 1, sx, sy + 17, 0xFF373737);
            graphics.fill(sx, sy, sx + 16, sy + 16, 0xFF8B8B8B);
        }

        InventoryScreen.renderEntityInInventory(graphics, x + 307, y + 82, 28,
                new Vector3f(x + 307 - mouseX, y + 40 - mouseY, 0),
                new Quaternionf().rotationXYZ(0, 0, 0), null, this.entity);
    }

    private static void panel(GuiGraphics graphics, int x1, int y1, int x2, int y2) {
        graphics.fill(x1, y1, x2, y2, 0xFF555555);
        graphics.fill(x1 + 1, y1 + 1, x2 - 1, y2 - 1, 0xFFFFFFFF);
        graphics.fill(x1 + 2, y1 + 2, x2 - 1, y2 - 1, 0xFFB0B0B0);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(this.font, Component.translatable("gui.cqrepoured.editor.title"),
                8, 7, 0xFF404040, false);
        graphics.drawString(this.font, this.entity.getDisplayName(), 174, 7, 0xFF404040, false);
        graphics.drawString(this.font, Component.translatable("gui.cqrepoured.editor.attributes"),
                10, 19, 0xFF404040, false);
        graphics.drawString(this.font, Component.translatable("gui.cqrepoured.editor.equipment"),
                174, 19, 0xFF404040, false);
        graphics.drawString(this.font, this.playerInventoryTitle, 184, 174, 0xFF404040, false);
        if (this.entity.isTavernNpc()) {
            graphics.drawString(this.font,
                    Component.translatable("gui.cqrepoured.editor.trade_hint"),
                    174, 92, 0xFF306030, false);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
