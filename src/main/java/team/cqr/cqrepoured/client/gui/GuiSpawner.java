package team.cqr.cqrepoured.client.gui;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiCheckBox;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.cqr.cqrepoured.client.util.GuiHelper;
import team.cqr.cqrepoured.tileentity.TileEntitySpawner;

@SideOnly(Side.CLIENT)
public class GuiSpawner extends GuiContainer {

	private static final ResourceLocation GUI_SPAWNER = new ResourceLocation("textures/gui/container/dispenser.png");

	private final TileEntitySpawner tileEntity;
	private GuiCheckBox vanillaSpawnerCheckBox;
	private GuiTextField minSpawnDelayTextField;
	private GuiTextField maxSpawnDelayTextField;
	private GuiTextField spawnCountTextField;
	private GuiTextField maxNearbyEntitiesTextField;
	private GuiTextField activatingRangeFromPlayerTextField;
	private GuiTextField spawnRangeTextField;

	public GuiSpawner(TileEntitySpawner tileEntity, Container inventorySlotsIn) {
		super(inventorySlotsIn);
		this.tileEntity = tileEntity;
	}

	@Override
	public void initGui() {
		super.initGui();
		this.vanillaSpawnerCheckBox = this.addButton(new GuiCheckBox(0, this.width / 2 + 90, this.height / 2 - 80, "Vanilla Spawner", this.tileEntity.isVanillaSpawner()));
		this.minSpawnDelayTextField = new GuiTextField(1, this.fontRenderer, this.width / 2 + 91, this.height / 2 - 66, 32, 10);
		this.maxSpawnDelayTextField = new GuiTextField(2, this.fontRenderer, this.width / 2 + 91, this.height / 2 - 52, 32, 10);
		this.spawnCountTextField = new GuiTextField(3, this.fontRenderer, this.width / 2 + 91, this.height / 2 - 38, 32, 10);
		this.maxNearbyEntitiesTextField = new GuiTextField(4, this.fontRenderer, this.width / 2 + 91, this.height / 2 - 24, 32, 10);
		this.activatingRangeFromPlayerTextField = new GuiTextField(5, this.fontRenderer, this.width / 2 + 91, this.height / 2 - 10, 32, 10);
		this.spawnRangeTextField = new GuiTextField(6, this.fontRenderer, this.width / 2 + 91, this.height / 2 + 4, 32, 10);

		this.minSpawnDelayTextField.setText(Integer.toString(this.tileEntity.getMinSpawnDelay()));
		this.maxSpawnDelayTextField.setText(Integer.toString(this.tileEntity.getMaxSpawnDelay()));
		this.spawnCountTextField.setText(Integer.toString(this.tileEntity.getSpawnCount()));
		this.maxNearbyEntitiesTextField.setText(Integer.toString(this.tileEntity.getMaxNearbyEntities()));
		this.activatingRangeFromPlayerTextField.setText(Integer.toString(this.tileEntity.getActivatingRangeFromPlayer()));
		this.spawnRangeTextField.setText(Integer.toString(this.tileEntity.getSpawnRange()));

		if (!this.vanillaSpawnerCheckBox.isChecked()) {
			this.minSpawnDelayTextField.setEnabled(false);
			this.maxSpawnDelayTextField.setEnabled(false);
			this.spawnCountTextField.setEnabled(false);
			this.maxNearbyEntitiesTextField.setEnabled(false);
			this.activatingRangeFromPlayerTextField.setEnabled(false);
			this.spawnRangeTextField.setEnabled(false);
		}
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();

		if (this.vanillaSpawnerCheckBox.isChecked()) {
			int minSpawnDelay = 200;
			try {
				minSpawnDelay = Integer.parseInt(this.minSpawnDelayTextField.getText());
			} catch (NumberFormatException e) {
				// ignore
			}
			int maxSpawnDelay = 800;
			try {
				maxSpawnDelay = Integer.parseInt(this.maxSpawnDelayTextField.getText());
			} catch (NumberFormatException e) {
				// ignore
			}
			int spawnCount = 4;
			try {
				spawnCount = Integer.parseInt(this.spawnCountTextField.getText());
			} catch (NumberFormatException e) {
				// ignore
			}
			int maxNearbyEntities = 6;
			try {
				maxNearbyEntities = Integer.parseInt(this.maxNearbyEntitiesTextField.getText());
			} catch (NumberFormatException e) {
				// ignore
			}
			int activatingRangeFromPlayer = 16;
			try {
				activatingRangeFromPlayer = Integer.parseInt(this.activatingRangeFromPlayerTextField.getText());
			} catch (NumberFormatException e) {
				// ignore
			}
			int spawnRange = 4;
			try {
				spawnRange = Integer.parseInt(this.spawnRangeTextField.getText());
			} catch (NumberFormatException e) {
				// ignore
			}

			this.tileEntity.setVanillaSpawner(minSpawnDelay, maxSpawnDelay, spawnCount, maxNearbyEntities, activatingRangeFromPlayer, spawnRange);
		} else {
			this.tileEntity.setCQRSpawner();
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String s = "Spawner";
		this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 0x404040);
		this.fontRenderer.drawString("Inventory", 8, 74, 0x404040);

		this.fontRenderer.drawStringWithShadow("Min Spawn Delay", 214, 18, 0xE0E0E0);
		this.fontRenderer.drawStringWithShadow("Max Spawn Delay", 214, 32, 0xE0E0E0);
		this.fontRenderer.drawStringWithShadow("Spawn Count", 214, 46, 0xE0E0E0);
		this.fontRenderer.drawStringWithShadow("Max Nearby Entities", 214, 60, 0xE0E0E0);
		this.fontRenderer.drawStringWithShadow("Activation Range", 214, 74, 0xE0E0E0);
		this.fontRenderer.drawStringWithShadow("Spawn Range", 214, 88, 0xE0E0E0);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);

		GlStateManager.disableLighting();
		this.minSpawnDelayTextField.drawTextBox();
		this.maxSpawnDelayTextField.drawTextBox();
		this.spawnCountTextField.drawTextBox();
		this.maxNearbyEntitiesTextField.drawTextBox();
		this.activatingRangeFromPlayerTextField.drawTextBox();
		this.spawnRangeTextField.drawTextBox();

		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(GUI_SPAWNER);
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);

		if (this.vanillaSpawnerCheckBox.isChecked()) {
			this.minSpawnDelayTextField.mouseClicked(mouseX, mouseY, mouseButton);
			this.maxSpawnDelayTextField.mouseClicked(mouseX, mouseY, mouseButton);
			this.spawnCountTextField.mouseClicked(mouseX, mouseY, mouseButton);
			this.maxNearbyEntitiesTextField.mouseClicked(mouseX, mouseY, mouseButton);
			this.activatingRangeFromPlayerTextField.mouseClicked(mouseX, mouseY, mouseButton);
			this.spawnRangeTextField.mouseClicked(mouseX, mouseY, mouseButton);
		}
	}

	@Override
	public void updateScreen() {
		super.updateScreen();

		if (this.vanillaSpawnerCheckBox.isChecked()) {
			this.minSpawnDelayTextField.updateCursorCounter();
			this.maxSpawnDelayTextField.updateCursorCounter();
			this.spawnCountTextField.updateCursorCounter();
			this.maxNearbyEntitiesTextField.updateCursorCounter();
			this.activatingRangeFromPlayerTextField.updateCursorCounter();
			this.spawnRangeTextField.updateCursorCounter();
		}
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button == this.vanillaSpawnerCheckBox) {
			boolean flag = this.vanillaSpawnerCheckBox.isChecked();
			this.minSpawnDelayTextField.setEnabled(flag);
			this.minSpawnDelayTextField.setFocused(false);
			this.maxSpawnDelayTextField.setEnabled(flag);
			this.maxSpawnDelayTextField.setFocused(false);
			this.spawnCountTextField.setEnabled(flag);
			this.spawnCountTextField.setFocused(false);
			this.maxNearbyEntitiesTextField.setEnabled(flag);
			this.maxNearbyEntitiesTextField.setFocused(false);
			this.activatingRangeFromPlayerTextField.setEnabled(flag);
			this.activatingRangeFromPlayerTextField.setFocused(false);
			this.spawnRangeTextField.setEnabled(flag);
			this.spawnRangeTextField.setFocused(false);
		}
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (this.minSpawnDelayTextField.isFocused() || this.maxSpawnDelayTextField.isFocused() || this.spawnCountTextField.isFocused() || this.maxNearbyEntitiesTextField.isFocused() || this.activatingRangeFromPlayerTextField.isFocused() || this.spawnRangeTextField.isFocused()) {
			if (keyCode == 1) {
				this.minSpawnDelayTextField.setFocused(false);
				this.maxSpawnDelayTextField.setFocused(false);
				this.spawnCountTextField.setFocused(false);
				this.maxNearbyEntitiesTextField.setFocused(false);
				this.activatingRangeFromPlayerTextField.setFocused(false);
				this.spawnRangeTextField.setFocused(false);
			} else {
				if (GuiHelper.isValidCharForNumberTextField(typedChar, keyCode, false, false)) {
					this.minSpawnDelayTextField.textboxKeyTyped(typedChar, keyCode);
					this.maxSpawnDelayTextField.textboxKeyTyped(typedChar, keyCode);
					this.spawnCountTextField.textboxKeyTyped(typedChar, keyCode);
					this.maxNearbyEntitiesTextField.textboxKeyTyped(typedChar, keyCode);
					this.activatingRangeFromPlayerTextField.textboxKeyTyped(typedChar, keyCode);
					this.spawnRangeTextField.textboxKeyTyped(typedChar, keyCode);
				}
			}
		} else {
			super.keyTyped(typedChar, keyCode);
		}
	}

}
