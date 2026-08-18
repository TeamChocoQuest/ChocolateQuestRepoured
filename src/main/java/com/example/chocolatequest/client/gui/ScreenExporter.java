package com.example.chocolatequest.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import com.example.chocolatequest.block.entity.ExporterBlockEntity;
import com.example.chocolatequest.network.packet.SaveStructurePayload;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;

@OnlyIn(Dist.CLIENT)
public class ScreenExporter extends Screen {

    private final ExporterBlockEntity tileEntity;

    private Button btnExport;
    private EditBox edtName;
    private EditBox edtStartX, edtStartY, edtStartZ;
    private EditBox edtEndX, edtEndY, edtEndZ;
    private Checkbox chbxRelativeMode;
    private Checkbox chbxIgnoreEntities;

    public ScreenExporter(ExporterBlockEntity tileEntity) {
        super(Component.translatable("tile.exporter.name"));
        this.tileEntity = tileEntity;
    }

    public static void open(ExporterBlockEntity tileEntity) {
        Minecraft.getInstance().setScreen(new ScreenExporter(tileEntity));
    }

    @Override
    protected void init() {
        super.init();

        this.edtName = new EditBox(this.font, this.width / 2 - 200, this.height / 2 - 70, 400, 20, Component.empty());
        this.edtName.setMaxLength(1024);
        this.edtName.setValue(this.tileEntity.getStructureName());
        this.addRenderableWidget(this.edtName);

        this.edtStartX = createNumberBox(this.width / 2 - 70, this.height / 2 - 30, this.tileEntity.getStartX());
        this.edtStartY = createNumberBox(this.width / 2 - 70 + 50, this.height / 2 - 30, this.tileEntity.getStartY());
        this.edtStartZ = createNumberBox(this.width / 2 - 70 + 100, this.height / 2 - 30, this.tileEntity.getStartZ());

        this.edtEndX = createNumberBox(this.width / 2 - 70, this.height / 2 + 10, this.tileEntity.getEndX());
        this.edtEndY = createNumberBox(this.width / 2 - 70 + 50, this.height / 2 + 10, this.tileEntity.getEndY());
        this.edtEndZ = createNumberBox(this.width / 2 - 70 + 100, this.height / 2 + 10, this.tileEntity.getEndZ());

        this.chbxRelativeMode = Checkbox.builder(Component.literal("Relative Mode"), this.font)
                .pos(this.width / 2 + 40, this.height / 2 + 40)
                .selected(this.tileEntity.isRelativeMode())
                .build();
        this.addRenderableWidget(this.chbxRelativeMode);

        this.chbxIgnoreEntities = Checkbox.builder(Component.literal("Ignore Entities"), this.font)
                .pos(this.width / 2 - 70, this.height / 2 + 40)
                .selected(this.tileEntity.isIgnoreEntities())
                .build();
        this.addRenderableWidget(this.chbxIgnoreEntities);

        this.btnExport = Button.builder(Component.literal("Export"), button -> {
            sendUpdateAndExport();
            this.minecraft.setScreen(null);
        }).pos(this.width / 2 - 70, this.height / 2 + 90).size(140, 20).build();
        this.addRenderableWidget(this.btnExport);
    }

    private EditBox createNumberBox(int x, int y, int value) {
        EditBox box = new EditBox(this.font, x, y, 40, 20, Component.empty());
        box.setValue(String.valueOf(value));
        this.addRenderableWidget(box);
        return box;
    }

    private int parseInt(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void sendUpdateAndExport() {
        String structName = this.edtName.getValue().isEmpty() ? "Unnamed" : this.edtName.getValue();
        int sx = parseInt(this.edtStartX.getValue());
        int sy = parseInt(this.edtStartY.getValue());
        int sz = parseInt(this.edtStartZ.getValue());
        int ex = parseInt(this.edtEndX.getValue());
        int ey = parseInt(this.edtEndY.getValue());
        int ez = parseInt(this.edtEndZ.getValue());
        boolean relative = this.chbxRelativeMode.selected();
        boolean ignoreEnt = this.chbxIgnoreEntities.selected();

        PacketDistributor.sendToServer(new SaveStructurePayload(
                this.tileEntity.getBlockPos(),
                structName,
                sx, sy, sz,
                ex, ey, ez,
                relative, ignoreEnt
        ));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        guiGraphics.drawCenteredString(this.font, "Start Coordinates", this.width / 2, this.height / 2 - 45, 0xFFFFFF);
        guiGraphics.drawCenteredString(this.font, "End Coordinates", this.width / 2, this.height / 2 - 5, 0xFFFFFF);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
