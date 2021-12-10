package team.cqr.cqrepoured.client.gui;

import java.io.IOException;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.tileentity.TileEntityExporterChestCustom;

public class GuiExporterChestCustom extends GuiScreen {

	private final TileEntityExporterChestCustom tileEntity;
	private GuiTextField lootTableTextField;

	public GuiExporterChestCustom(TileEntityExporterChestCustom tileEntity) {
		this.tileEntity = tileEntity;
	}

	@Override
	public void initGui() {
		this.lootTableTextField = new GuiTextField(0, this.fontRenderer, this.width / 2 - 70, this.height / 2 - 70, 140, 20);
		this.lootTableTextField.setText(this.tileEntity.getLootTable().toString());
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		this.tileEntity.setLootTable(new ResourceLocation(this.lootTableTextField.getText()));
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);

		this.lootTableTextField.textboxKeyTyped(typedChar, keyCode);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);

		this.lootTableTextField.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void updateScreen() {
		super.updateScreen();

		this.lootTableTextField.updateCursorCounter();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, I18n.format("tile.exporter_chest_custom.name"), this.width / 2, 20, 0xFFFFFF);

		this.drawString(this.fontRenderer, "Loot Table", this.width / 2 - 70, this.height / 2 - 80, 0xA0A0A0);
		this.lootTableTextField.drawTextBox();

		super.drawScreen(mouseX, mouseY, partialTicks);

	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

}
