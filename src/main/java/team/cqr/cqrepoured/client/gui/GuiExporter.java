package team.cqr.cqrepoured.client.gui;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiCheckBox;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.cqr.cqrepoured.client.util.GuiHelper;
import team.cqr.cqrepoured.tileentity.TileEntityExporter;

@SideOnly(Side.CLIENT)
public class GuiExporter extends GuiScreen {

	private boolean saveStructOnExit = false;
	private TileEntityExporter exporter;

	private GuiButtonExt btnExport;
	private GuiTextField edtName, edtEndX, edtEndY, edtEndZ, edtStartX, edtStartY, edtStartZ, edtUnprotectedBlocks;
	private GuiCheckBox chbxRelativeMode, chbxIgnoreEntities;

	public GuiExporter(TileEntityExporter exporter) {
		this.mc = Minecraft.getMinecraft();
		this.exporter = exporter;
	}

	public void sync() {
		this.edtEndX.setText(String.valueOf(this.exporter.getEndX()));
		this.edtEndY.setText(String.valueOf(this.exporter.getEndY()));
		this.edtEndZ.setText(String.valueOf(this.exporter.getEndZ()));
		this.edtStartX.setText(String.valueOf(this.exporter.getStartX()));
		this.edtStartY.setText(String.valueOf(this.exporter.getStartY()));
		this.edtStartZ.setText(String.valueOf(this.exporter.getStartZ()));
		this.edtName.setText(this.exporter.getStructureName());
		this.chbxRelativeMode.setIsChecked(this.exporter.isRelativeMode());
		this.chbxIgnoreEntities.setIsChecked(this.exporter.isIgnoreEntities());
		StringBuilder sb = new StringBuilder();
		BlockPos[] arr = this.exporter.getUnprotectedBlocks();
		if (arr.length > 0) {
			sb.append(arr[0].getX());
			sb.append(',');
			sb.append(arr[0].getY());
			sb.append(',');
			sb.append(arr[0].getZ());
			for (int i = 1; i < arr.length; i++) {
				sb.append(';');
				sb.append(' ');
				sb.append(arr[i].getX());
				sb.append(',');
				sb.append(arr[i].getY());
				sb.append(',');
				sb.append(arr[i].getZ());
			}
		}
		this.edtUnprotectedBlocks.setText(sb.toString());
	}

	@Override
	public void initGui() {
		int index = 0;

		this.edtName = new GuiTextField(index++, this.fontRenderer, this.width / 2 - 70, this.height / 2 - 70, 140, 20);
		this.edtName.setText(this.exporter.getStructureName());

		this.edtEndX = new GuiTextField(index++, this.fontRenderer, this.width / 2 - 70, this.height / 2 + 10, 40, 20);
		this.edtEndX.setText(String.valueOf(this.exporter.getEndX()));
		this.edtEndY = new GuiTextField(index++, this.fontRenderer, this.width / 2 - 70 + 50, this.height / 2 + 10, 40, 20);
		this.edtEndY.setText(String.valueOf(this.exporter.getEndY()));
		this.edtEndZ = new GuiTextField(index++, this.fontRenderer, this.width / 2 - 70 + 50 + 50, this.height / 2 + 10, 40, 20);
		this.edtEndZ.setText(String.valueOf(this.exporter.getEndZ()));

		this.edtStartX = new GuiTextField(index++, this.fontRenderer, this.width / 2 - 70, this.height / 2 - 30, 40, 20);
		this.edtStartX.setText(String.valueOf(this.exporter.getStartX()));
		this.edtStartY = new GuiTextField(index++, this.fontRenderer, this.width / 2 - 70 + 50, this.height / 2 - 30, 40, 20);
		this.edtStartY.setText(String.valueOf(this.exporter.getStartY()));
		this.edtStartZ = new GuiTextField(index++, this.fontRenderer, this.width / 2 - 70 + 50 + 50, this.height / 2 - 30, 40, 20);
		this.edtStartZ.setText(String.valueOf(this.exporter.getStartZ()));

		this.chbxRelativeMode = new GuiCheckBox(index++, this.width / 2 + 30, this.height / 2 + 40, "Use Relative Mode", this.exporter.isRelativeMode());
		this.chbxIgnoreEntities = new GuiCheckBox(index++, this.width / 2 - 70, this.height / 2 + 40, "Ignore Entities", this.exporter.isIgnoreEntities());

		this.edtUnprotectedBlocks = new GuiTextField(index++, this.fontRenderer, this.width / 2 - 70, this.height / 2 + 60, 140, 20);
		StringBuilder sb = new StringBuilder();
		BlockPos[] arr = this.exporter.getUnprotectedBlocks();
		if (arr.length > 0) {
			sb.append(arr[0].getX());
			sb.append(',');
			sb.append(arr[0].getY());
			sb.append(',');
			sb.append(arr[0].getZ());
			for (int i = 1; i < arr.length; i++) {
				sb.append(';');
				sb.append(' ');
				sb.append(arr[i].getX());
				sb.append(',');
				sb.append(arr[i].getY());
				sb.append(',');
				sb.append(arr[i].getZ());
			}
		}
		this.edtUnprotectedBlocks.setText(sb.toString());

		this.btnExport = new GuiButtonExt(index++, this.width / 2 - 70, this.height / 2 + 90, 140, 20, "Export");

		this.buttonList.add(this.chbxRelativeMode);
		this.buttonList.add(this.chbxIgnoreEntities);
		this.buttonList.add(this.btnExport);
	}

	@Override
	public void onGuiClosed() {
		try {
			String structName = this.edtName.getText();
			if (structName.isEmpty()) {
				throw new IllegalArgumentException();
			}
			int startX = Integer.parseInt(this.edtStartX.getText());
			int startY = Integer.parseInt(this.edtStartY.getText());
			int startZ = Integer.parseInt(this.edtStartZ.getText());
			int endX = Integer.parseInt(this.edtEndX.getText());
			int endY = Integer.parseInt(this.edtEndY.getText());
			int endZ = Integer.parseInt(this.edtEndZ.getText());
			BlockPos[] unprotectedBlocks;
			if (!this.edtUnprotectedBlocks.getText().isEmpty()) {
				String[] arr = this.edtUnprotectedBlocks.getText().split(";");
				unprotectedBlocks = new BlockPos[arr.length];
				for (int i = 0; i < arr.length; i++) {
					String[] arr1 = arr[i].split(",");
					unprotectedBlocks[i] = new BlockPos(Integer.parseInt(arr1[0].trim()), Integer.parseInt(arr1[1].trim()), Integer.parseInt(arr1[2].trim()));
				}
			} else {
				unprotectedBlocks = new BlockPos[0];
			}

			this.exporter.setValues(structName, startX, startY, startZ, endX, endY, endZ, this.chbxRelativeMode.isChecked(), this.chbxIgnoreEntities.isChecked(), unprotectedBlocks);

			if (this.saveStructOnExit) {
				this.exporter.saveStructure(this.mc.player);
			}
		} catch (Exception e) {
			Minecraft.getMinecraft().player.sendMessage(new TextComponentString("Invalid exporter arguments"));
		}

		super.onGuiClosed();

	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (this.edtName.isFocused() || this.edtStartX.isFocused() || this.edtStartY.isFocused() || this.edtStartZ.isFocused() || this.edtEndX.isFocused() || this.edtEndY.isFocused() || this.edtEndZ.isFocused() || this.edtUnprotectedBlocks.isFocused()) {
			if (keyCode == 1) {
				this.edtName.setFocused(false);
				this.edtStartX.setFocused(false);
				this.edtStartY.setFocused(false);
				this.edtStartZ.setFocused(false);
				this.edtEndX.setFocused(false);
				this.edtEndY.setFocused(false);
				this.edtEndZ.setFocused(false);
				this.edtUnprotectedBlocks.setFocused(false);
			} else {
				this.edtName.textboxKeyTyped(typedChar, keyCode);
				if (GuiHelper.isValidCharForNumberTextField(typedChar, keyCode, true, false)) {
					this.edtStartX.textboxKeyTyped(typedChar, keyCode);
					this.edtStartY.textboxKeyTyped(typedChar, keyCode);
					this.edtStartZ.textboxKeyTyped(typedChar, keyCode);
					this.edtEndX.textboxKeyTyped(typedChar, keyCode);
					this.edtEndY.textboxKeyTyped(typedChar, keyCode);
					this.edtEndZ.textboxKeyTyped(typedChar, keyCode);
				}
				this.edtUnprotectedBlocks.textboxKeyTyped(typedChar, keyCode);
			}
		} else if (keyCode == 1 || this.mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode)) {
			this.mc.player.closeScreen();
		} else {
			super.keyTyped(typedChar, keyCode);
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

		this.edtUnprotectedBlocks.mouseClicked(mouseX, mouseY, mouseButton);
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

		this.edtUnprotectedBlocks.updateCursorCounter();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, I18n.format("tile.exporter.name"), this.width / 2, 20, 0xFFFFFF);

		this.edtName.drawTextBox();

		this.edtEndX.drawTextBox();
		this.edtEndY.drawTextBox();
		this.edtEndZ.drawTextBox();

		this.edtStartX.drawTextBox();
		this.edtStartY.drawTextBox();
		this.edtStartZ.drawTextBox();

		this.edtUnprotectedBlocks.drawTextBox();

		this.drawString(this.fontRenderer, "Structure Name", this.width / 2 - 70, this.height / 2 - 80, 0xA0A0A0);

		this.drawString(this.fontRenderer, "Start X", this.width / 2 - 70, this.height / 2 - 40, 0xA0A0A0);
		this.drawString(this.fontRenderer, "Start Y", this.width / 2 - 20, this.height / 2 - 40, 0xA0A0A0);
		this.drawString(this.fontRenderer, "Start Z", this.width / 2 + 30, this.height / 2 - 40, 0xA0A0A0);

		this.drawString(this.fontRenderer, "End X", this.width / 2 - 70, this.height / 2, 0xA0A0A0);
		this.drawString(this.fontRenderer, "End Y", this.width / 2 - 20, this.height / 2, 0xA0A0A0);
		this.drawString(this.fontRenderer, "End Z", this.width / 2 + 30, this.height / 2, 0xA0A0A0);

		super.drawScreen(mouseX, mouseY, partialTicks);

		if (this.chbxRelativeMode.isMouseOver()) {
			this.drawHoveringText(I18n.format("description.gui_exporter_relative_mode.name"), mouseX, mouseY);
		} else if (this.chbxIgnoreEntities.isMouseOver()) {
			this.drawHoveringText(I18n.format("description.gui_exporter_ignore_entities.name"), mouseX, mouseY);
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button == this.btnExport) {
			this.saveStructOnExit = true;
			this.mc.player.closeScreen();
		} else {
			super.actionPerformed(button);
			if (button == this.chbxRelativeMode) {
				boolean flag = this.chbxRelativeMode.isChecked();
				BlockPos pos = this.exporter.getPos();
				try {
					this.edtStartX.setText(Integer.toString(Integer.parseInt(this.edtStartX.getText()) + (flag ? -pos.getX() : pos.getX())));
				} catch (NumberFormatException e) {
					// ignore
				}
				try {
					this.edtStartY.setText(Integer.toString(Integer.parseInt(this.edtStartY.getText()) + (flag ? -pos.getY() : pos.getY())));
				} catch (NumberFormatException e) {
					// ignore
				}
				try {
					this.edtStartZ.setText(Integer.toString(Integer.parseInt(this.edtStartZ.getText()) + (flag ? -pos.getZ() : pos.getZ())));
				} catch (NumberFormatException e) {
					// ignore
				}
				try {
					this.edtEndX.setText(Integer.toString(Integer.parseInt(this.edtEndX.getText()) + (flag ? -pos.getX() : pos.getX())));
				} catch (NumberFormatException e) {
					// ignore
				}
				try {
					this.edtEndY.setText(Integer.toString(Integer.parseInt(this.edtEndY.getText()) + (flag ? -pos.getY() : pos.getY())));
				} catch (NumberFormatException e) {
					// ignore
				}
				try {
					this.edtEndZ.setText(Integer.toString(Integer.parseInt(this.edtEndZ.getText()) + (flag ? -pos.getZ() : pos.getZ())));
				} catch (NumberFormatException e) {
					// ignore
				}
			}
		}
	}

}
