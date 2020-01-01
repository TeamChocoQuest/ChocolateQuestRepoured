package com.teamcqr.chocolatequestrepoured.client.gui;

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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiExporter extends GuiScreen {

	private String authorName;
	private boolean saveStructOnExit = false;
	private TileEntityExporter exporter;

	private GuiButtonExt btnExport;
	private GuiTextField edtName, edtEndX, edtEndY, edtEndZ, edtStartX, edtStartY, edtStartZ;
	private GuiCheckBox chbxPartsMode, chbxRelativeMode;

	public GuiExporter(TileEntityExporter exporter) {
		this.mc = Minecraft.getMinecraft();
		this.authorName = this.mc.player.getName();
		this.exporter = exporter;
		if (this.exporter != null) {
			this.exporter.setUser(this.mc.player);
		}
	}

	public void sync() {
		this.edtEndX.setText(String.valueOf(this.exporter.endX));
		this.edtEndY.setText(String.valueOf(this.exporter.endY));
		this.edtEndZ.setText(String.valueOf(this.exporter.endZ));
		this.edtStartX.setText(String.valueOf(this.exporter.startX));
		this.edtStartY.setText(String.valueOf(this.exporter.startY));
		this.edtStartZ.setText(String.valueOf(this.exporter.startZ));
		this.edtName.setText(this.exporter.structureName);
		this.chbxPartsMode.setIsChecked(this.exporter.partModeUsing);
		this.chbxRelativeMode.setIsChecked(this.exporter.relativeMode);
	}

	@Override
	public void initGui() {
		this.edtName = new GuiTextField(0, this.fontRenderer, this.width / 2 - 70, this.height / 2 - 70, 140, 20);
		this.edtName.setText(this.exporter.structureName);

		this.edtEndX = new GuiTextField(1, this.fontRenderer, this.width / 2 - 70, this.height / 2 + 10, 40, 20);
		this.edtEndX.setText(String.valueOf(this.exporter.endX));
		this.edtEndY = new GuiTextField(2, this.fontRenderer, this.width / 2 - 70 + 50, this.height / 2 + 10, 40, 20);
		this.edtEndY.setText(String.valueOf(this.exporter.endY));
		this.edtEndZ = new GuiTextField(3, this.fontRenderer, this.width / 2 - 70 + 50 + 50, this.height / 2 + 10, 40, 20);
		this.edtEndZ.setText(String.valueOf(this.exporter.endZ));

		this.edtStartX = new GuiTextField(1, this.fontRenderer, this.width / 2 - 70, this.height / 2 - 30, 40, 20);
		this.edtStartX.setText(String.valueOf(this.exporter.startX));
		this.edtStartY = new GuiTextField(2, this.fontRenderer, this.width / 2 - 70 + 50, this.height / 2 - 30, 40, 20);
		this.edtStartY.setText(String.valueOf(this.exporter.startY));
		this.edtStartZ = new GuiTextField(3, this.fontRenderer, this.width / 2 - 70 + 50 + 50, this.height / 2 - 30, 40, 20);
		this.edtStartZ.setText(String.valueOf(this.exporter.startZ));

		this.chbxPartsMode = new GuiCheckBox(5, this.width / 2 - 70, this.height / 2 + 40, "Use Part Mode", this.exporter.partModeUsing);
		this.chbxRelativeMode = new GuiCheckBox(5, this.width / 2 + 30, this.height / 2 + 40, "Use Relative Mode", this.exporter.relativeMode);

		this.btnExport = new GuiButtonExt(4, this.width / 2 - 70, this.height / 2 + 60, 140, 20, "Export");

		this.buttonList.add(this.chbxPartsMode);
		this.buttonList.add(this.chbxRelativeMode);
		this.buttonList.add(this.btnExport);
	}

	@Override
	public void onGuiClosed() {
		try {
			int eX = Integer.parseInt(this.edtEndX.getText());
			int sX = Integer.parseInt(this.edtStartX.getText());
			int eY = Integer.parseInt(this.edtEndY.getText());
			int sY = Integer.parseInt(this.edtStartY.getText());
			int eZ = Integer.parseInt(this.edtEndZ.getText());
			int sZ = Integer.parseInt(this.edtStartZ.getText());

			String structName = this.edtName.getText();
			structName = structName.replaceAll(" ", "_");

			if (structName.isEmpty() || structName.equalsIgnoreCase("")) {
				structName = "dungeon_export";
			}

			this.exporter.setValues(sX, sY, sZ, eX, eY, eZ, structName, this.chbxPartsMode.isChecked(), this.chbxRelativeMode.isChecked());

			CQRMain.NETWORK.sendToServer(new ExporterUpdatePacket(this.exporter));

			if (this.saveStructOnExit) {
				System.out.println("Saving structure...");
				this.exporter.saveStructure(this.mc.world, new BlockPos(sX, sY, sZ), new BlockPos(eX, eY, eZ), structName);
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

		this.edtName.textboxKeyTyped(typedChar, keyCode);

		if (!Character.isAlphabetic(typedChar)) {
			this.edtEndX.textboxKeyTyped(typedChar, keyCode);
			this.edtEndY.textboxKeyTyped(typedChar, keyCode);
			this.edtEndZ.textboxKeyTyped(typedChar, keyCode);

			this.edtStartX.textboxKeyTyped(typedChar, keyCode);
			this.edtStartY.textboxKeyTyped(typedChar, keyCode);
			this.edtStartZ.textboxKeyTyped(typedChar, keyCode);
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);

		this.edtName.mouseClicked(mouseX, mouseY, mouseButton);

		this.edtEndX.mouseClicked(mouseX, mouseY, mouseButton);
		this.edtEndY.mouseClicked(mouseX, mouseY, mouseButton);
		this.edtEndZ.mouseClicked(mouseX, mouseY, mouseButton);

		this.edtStartX.mouseClicked(mouseX, mouseY, mouseButton);
		this.edtStartY.mouseClicked(mouseX, mouseY, mouseButton);
		this.edtStartZ.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void updateScreen() {
		super.updateScreen();

		this.edtName.updateCursorCounter();

		this.edtEndX.updateCursorCounter();
		this.edtEndY.updateCursorCounter();
		this.edtEndZ.updateCursorCounter();

		this.edtStartX.updateCursorCounter();
		this.edtStartY.updateCursorCounter();
		this.edtStartZ.updateCursorCounter();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, I18n.format("tile.exporter.name"), this.width / 2, 20, 16777215);

		this.edtName.drawTextBox();

		this.edtEndX.drawTextBox();
		this.edtEndY.drawTextBox();
		this.edtEndZ.drawTextBox();

		this.edtStartX.drawTextBox();
		this.edtStartY.drawTextBox();
		this.edtStartZ.drawTextBox();

		this.drawString(this.fontRenderer, "Structure Name", this.width / 2 - 70, this.height / 2 - 80, 10526880);

		this.drawString(this.fontRenderer, "Start X", this.width / 2 - 70, this.height / 2 - 40, 10526880);
		this.drawString(this.fontRenderer, "Start Y", this.width / 2 - 20, this.height / 2 - 40, 10526880);
		this.drawString(this.fontRenderer, "Start Z", this.width / 2 + 30, this.height / 2 - 40, 10526880);

		this.drawString(this.fontRenderer, "End X", this.width / 2 - 70, this.height / 2, 10526880);
		this.drawString(this.fontRenderer, "End Y", this.width / 2 - 20, this.height / 2, 10526880);
		this.drawString(this.fontRenderer, "End Z", this.width / 2 + 30, this.height / 2, 10526880);

		if (this.chbxPartsMode.isMouseOver()) {
			this.drawHoveringText(I18n.format("description.gui_exporter_part_mode.name"), this.width / 2 - 170, this.height / 2 + 30);
		} else if (this.chbxRelativeMode.isMouseOver()) {
			this.drawHoveringText(I18n.format("description.gui_exporter_relative_mode.name"), this.width / 2 - 170, this.height / 2 + 30);
		}

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button == this.btnExport) {
			this.saveStructOnExit = true;
			this.mc.displayGuiScreen((GuiScreen) null);
		}
	}

}
