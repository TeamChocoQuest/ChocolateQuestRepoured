package com.teamcqr.chocolatequestrepoured.gui;

import java.io.IOException;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.network.ExporterUpdatePacket;
import com.teamcqr.chocolatequestrepoured.tileentity.TileEntityExporter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiCheckBox;

public class GuiExporter extends GuiScreen {

	private String authorName;
	private boolean saveStructOnExit = false;
	private TileEntityExporter exporter;

	private GuiButtonExt btnExport;
	private GuiTextField edtName, edtEndX, edtEndY, edtEndZ, edtStartX, edtStartY, edtStartZ;
	private GuiCheckBox chbxPartsMode, chbxRelativeMode;

	public GuiExporter(TileEntityExporter exporter) {
		this.mc = Minecraft.getMinecraft();
		this.authorName = mc.player.getName();
		this.exporter = exporter;
		if (this.exporter != null) {
			this.exporter.setUser(mc.player);
		}
	}

	public void sync() {
		edtEndX.setText(String.valueOf(exporter.endX));
		edtEndY.setText(String.valueOf(exporter.endY));
		edtEndZ.setText(String.valueOf(exporter.endZ));
		edtStartX.setText(String.valueOf(exporter.startX));
		edtStartY.setText(String.valueOf(exporter.startY));
		edtStartZ.setText(String.valueOf(exporter.startZ));
		edtName.setText(exporter.structureName);
		chbxPartsMode.setIsChecked(exporter.partModeUsing);
		chbxRelativeMode.setIsChecked(exporter.relativeMode);
	}

	@Override
	public void initGui() {
		edtName = new GuiTextField(0, this.fontRenderer, width / 2 - 70, height / 2 - 70, 140, 20);
		edtName.setText(exporter.structureName);

		edtEndX = new GuiTextField(1, this.fontRenderer, width / 2 - 70, height / 2 + 10, 40, 20);
		edtEndX.setText(String.valueOf(exporter.endX));
		edtEndY = new GuiTextField(2, this.fontRenderer, width / 2 - 70 + 50, height / 2 + 10, 40, 20);
		edtEndY.setText(String.valueOf(exporter.endY));
		edtEndZ = new GuiTextField(3, this.fontRenderer, width / 2 - 70 + 50 + 50, height / 2 + 10, 40, 20);
		edtEndZ.setText(String.valueOf(exporter.endZ));

		edtStartX = new GuiTextField(1, this.fontRenderer, width / 2 - 70, height / 2 - 30, 40, 20);
		edtStartX.setText(String.valueOf(exporter.startX));
		edtStartY = new GuiTextField(2, this.fontRenderer, width / 2 - 70 + 50, height / 2 - 30, 40, 20);
		edtStartY.setText(String.valueOf(exporter.startY));
		edtStartZ = new GuiTextField(3, this.fontRenderer, width / 2 - 70 + 50 + 50, height / 2 - 30, 40, 20);
		edtStartZ.setText(String.valueOf(exporter.startZ));

		chbxPartsMode = new GuiCheckBox(5, width / 2 - 70, height / 2 + 40, "Use Part Mode", exporter.partModeUsing);
		chbxRelativeMode = new GuiCheckBox(5, width / 2 + 30, height / 2 + 40, "Use Relative Mode", exporter.relativeMode);

		btnExport = new GuiButtonExt(4, width / 2 - 70, height / 2 + 60, 140, 20, "Export");

		buttonList.add(chbxPartsMode);
		buttonList.add(chbxRelativeMode);
		buttonList.add(btnExport);
	}

	@Override
	public void onGuiClosed() {
		try {
			int eX = Integer.parseInt(edtEndX.getText());
			int sX = Integer.parseInt(edtStartX.getText());
			int eY = Integer.parseInt(edtEndY.getText());
			int sY = Integer.parseInt(edtStartY.getText());
			int eZ = Integer.parseInt(edtEndZ.getText());
			int sZ = Integer.parseInt(edtStartZ.getText());

			String structName = edtName.getText();
			structName = structName.replaceAll(" ", "_");

			if (structName.isEmpty() || structName.equalsIgnoreCase("")) {
				structName = "dungeon_export";
			}

			exporter.setValues(sX, sY, sZ, eX, eY, eZ, structName, chbxPartsMode.isChecked(), chbxRelativeMode.isChecked());

			CQRMain.NETWORK.sendToServer(new ExporterUpdatePacket(exporter));

			if (this.saveStructOnExit) {
				System.out.println("Saving structure...");
				exporter.saveStructure(mc.world, new BlockPos(sX, sY, sZ), new BlockPos(eX, eY, eZ), structName);
			}
		} catch (NumberFormatException ex) {
			System.out.println(ex);
		}

		super.onGuiClosed();

	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == 1) {
			this.mc.player.closeScreen();
		}

		edtName.textboxKeyTyped(typedChar, keyCode);

		if (!Character.isAlphabetic(typedChar)) {
			edtEndX.textboxKeyTyped(typedChar, keyCode);
			edtEndY.textboxKeyTyped(typedChar, keyCode);
			edtEndZ.textboxKeyTyped(typedChar, keyCode);

			edtStartX.textboxKeyTyped(typedChar, keyCode);
			edtStartY.textboxKeyTyped(typedChar, keyCode);
			edtStartZ.textboxKeyTyped(typedChar, keyCode);
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);

		edtName.mouseClicked(mouseX, mouseY, mouseButton);

		edtEndX.mouseClicked(mouseX, mouseY, mouseButton);
		edtEndY.mouseClicked(mouseX, mouseY, mouseButton);
		edtEndZ.mouseClicked(mouseX, mouseY, mouseButton);

		edtStartX.mouseClicked(mouseX, mouseY, mouseButton);
		edtStartY.mouseClicked(mouseX, mouseY, mouseButton);
		edtStartZ.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void updateScreen() {
		super.updateScreen();

		edtName.updateCursorCounter();

		edtEndX.updateCursorCounter();
		edtEndY.updateCursorCounter();
		edtEndZ.updateCursorCounter();

		edtStartX.updateCursorCounter();
		edtStartY.updateCursorCounter();
		edtStartZ.updateCursorCounter();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, I18n.format("tile.exporter.name"), this.width / 2, 20, 16777215);

		edtName.drawTextBox();

		edtEndX.drawTextBox();
		edtEndY.drawTextBox();
		edtEndZ.drawTextBox();

		edtStartX.drawTextBox();
		edtStartY.drawTextBox();
		edtStartZ.drawTextBox();

		drawString(this.fontRenderer, "Structure Name", width / 2 - 70, height / 2 - 80, 10526880);

		drawString(this.fontRenderer, "Start X", width / 2 - 70, height / 2 - 40, 10526880);
		drawString(this.fontRenderer, "Start Y", width / 2 - 20, height / 2 - 40, 10526880);
		drawString(this.fontRenderer, "Start Z", width / 2 + 30, height / 2 - 40, 10526880);

		drawString(this.fontRenderer, "End X", width / 2 - 70, height / 2, 10526880);
		drawString(this.fontRenderer, "End Y", width / 2 - 20, height / 2, 10526880);
		drawString(this.fontRenderer, "End Z", width / 2 + 30, height / 2, 10526880);

		if (this.chbxPartsMode.isMouseOver()) {
			this.drawHoveringText(I18n.format("description.gui_exporter_part_mode.name"), width / 2 - 170,
					height / 2 + 30);
		} else if (this.chbxRelativeMode.isMouseOver()) {
			this.drawHoveringText(I18n.format("description.gui_exporter_relative_mode.name"), width / 2 - 170,
					height / 2 + 30);
		}

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button == btnExport) {
			this.saveStructOnExit = true;
			this.mc.displayGuiScreen((GuiScreen) null);
		}
	}

}
