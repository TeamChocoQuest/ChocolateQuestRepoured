package team.cqr.cqrepoured.client.gui;

import java.io.IOException;
import java.util.Arrays;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.config.ConfigGuiType;
import net.minecraftforge.fml.client.config.DummyConfigElement.DummyListElement;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiCheckBox;
import net.minecraftforge.fml.client.config.GuiEditArray;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;
import team.cqr.cqrepoured.client.util.GuiHelper;
import team.cqr.cqrepoured.tileentity.TileEntityExporter;

@Dist(OnlyIn.CLIENT)
public class GuiExporter extends Screen {

	private final TileEntityExporter exporter;
	private final DummyListElement unprotectedBlocksConfig;

	private GuiButtonExt btnExport;
	private GuiButtonExt btnUnprotectedBlocks;
	private TextFieldWidget edtName;
	private TextFieldWidget edtEndX;
	private TextFieldWidget edtEndY;
	private TextFieldWidget edtEndZ;
	private TextFieldWidget edtStartX;
	private TextFieldWidget edtStartY;
	private TextFieldWidget edtStartZ;
	private GuiCheckBox chbxRelativeMode;
	private GuiCheckBox chbxIgnoreEntities;

	private boolean saveStructOnExit = false;

	public GuiExporter(TileEntityExporter exporter) {
		this.mc = Minecraft.getMinecraft();
		this.exporter = exporter;
		String[] unprotectedBlocks = Arrays.stream(this.exporter.getUnprotectedBlocks()).map(p -> String.format("%d %d %d", p.getX(), p.getY(), p.getZ())).toArray(String[]::new);
		this.unprotectedBlocksConfig = new DummyListElement("test", unprotectedBlocks, ConfigGuiType.STRING, "test");
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
		String[] unprotectedBlocks = Arrays.stream(this.exporter.getUnprotectedBlocks()).map(p -> String.format("%d %d %d", p.getX(), p.getY(), p.getZ())).toArray(String[]::new);
		this.unprotectedBlocksConfig.set(unprotectedBlocks);
	}

	@Override
	public void initGui() {
		int index = 0;

		this.edtName = new TextFieldWidget(index++, this.fontRenderer, this.width / 2 - 200, this.height / 2 - 70, 400, 20);
		this.edtName.setMaxStringLength(1024);
		this.edtName.setText(this.exporter.getStructureName());

		this.edtEndX = new TextFieldWidget(index++, this.fontRenderer, this.width / 2 - 70, this.height / 2 + 10, 40, 20);
		this.edtEndX.setText(String.valueOf(this.exporter.getEndX()));
		this.edtEndY = new TextFieldWidget(index++, this.fontRenderer, this.width / 2 - 70 + 50, this.height / 2 + 10, 40, 20);
		this.edtEndY.setText(String.valueOf(this.exporter.getEndY()));
		this.edtEndZ = new TextFieldWidget(index++, this.fontRenderer, this.width / 2 - 70 + 50 + 50, this.height / 2 + 10, 40, 20);
		this.edtEndZ.setText(String.valueOf(this.exporter.getEndZ()));

		this.edtStartX = new TextFieldWidget(index++, this.fontRenderer, this.width / 2 - 70, this.height / 2 - 30, 40, 20);
		this.edtStartX.setText(String.valueOf(this.exporter.getStartX()));
		this.edtStartY = new TextFieldWidget(index++, this.fontRenderer, this.width / 2 - 70 + 50, this.height / 2 - 30, 40, 20);
		this.edtStartY.setText(String.valueOf(this.exporter.getStartY()));
		this.edtStartZ = new TextFieldWidget(index++, this.fontRenderer, this.width / 2 - 70 + 50 + 50, this.height / 2 - 30, 40, 20);
		this.edtStartZ.setText(String.valueOf(this.exporter.getStartZ()));

		this.chbxRelativeMode = new GuiCheckBox(index++, this.width / 2 + 30, this.height / 2 + 40, "Use Relative Mode", this.exporter.isRelativeMode());
		this.chbxIgnoreEntities = new GuiCheckBox(index++, this.width / 2 - 70, this.height / 2 + 40, "Ignore Entities", this.exporter.isIgnoreEntities());

		this.btnExport = new GuiButtonExt(index++, this.width / 2 - 70, this.height / 2 + 90, 140, 20, "Export");

		this.btnUnprotectedBlocks = new GuiButtonExt(index, this.width / 2 - 70, this.height / 2 + 60, 140, 20, "Unprotected Blocks");

		this.buttonList.add(this.chbxRelativeMode);
		this.buttonList.add(this.chbxIgnoreEntities);
		this.buttonList.add(this.btnExport);
		this.buttonList.add(this.btnUnprotectedBlocks);
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
			BlockPos[] unprotectedBlocks = Arrays.stream(this.unprotectedBlocksConfig.getDefaults()).map(obj -> {
				String[] arr = ((String) obj).split(" ");
				return new BlockPos(Integer.parseInt(arr[0]), Integer.parseInt(arr[1]), Integer.parseInt(arr[2]));
			}).toArray(BlockPos[]::new);

			this.exporter.setValues(structName, startX, startY, startZ, endX, endY, endZ, this.chbxRelativeMode.isChecked(), this.chbxIgnoreEntities.isChecked(), unprotectedBlocks);

			if (this.saveStructOnExit) {
				this.exporter.saveStructure(this.mc.player);
			}
		} catch (Exception e) {
			Minecraft.getMinecraft().player.sendMessage(new StringTextComponent("Invalid exporter arguments"));
		}

		super.onGuiClosed();

	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (this.edtName.isFocused() || this.edtStartX.isFocused() || this.edtStartY.isFocused() || this.edtStartZ.isFocused() || this.edtEndX.isFocused() || this.edtEndY.isFocused() || this.edtEndZ.isFocused()) {
			if (keyCode == 1) {
				this.edtName.setFocused(false);
				this.edtStartX.setFocused(false);
				this.edtStartY.setFocused(false);
				this.edtStartZ.setFocused(false);
				this.edtEndX.setFocused(false);
				this.edtEndY.setFocused(false);
				this.edtEndZ.setFocused(false);
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
		this.drawCenteredString(this.fontRenderer, I18n.format("tile.exporter.name"), this.width / 2, 20, 0xFFFFFF);

		this.edtName.drawTextBox();

		this.edtEndX.drawTextBox();
		this.edtEndY.drawTextBox();
		this.edtEndZ.drawTextBox();

		this.edtStartX.drawTextBox();
		this.edtStartY.drawTextBox();
		this.edtStartZ.drawTextBox();

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
	protected void actionPerformed(Button button) throws IOException {
		if (button == this.btnExport) {
			this.saveStructOnExit = true;
			this.mc.player.closeScreen();
		} else if (button == this.btnUnprotectedBlocks) {
			this.mc.displayGuiScreen(new GuiEditArray(this, this.unprotectedBlocksConfig, 0, this.unprotectedBlocksConfig.getDefaults(), true));
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
