package team.cqr.cqrepoured.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import team.cqr.cqrepoured.tileentity.TileEntityExporterChestCustom;

public class GuiExporterChestCustom extends Screen {

	private final TileEntityExporterChestCustom tileEntity;
	private TextFieldWidget lootTableTextField;

	public GuiExporterChestCustom(TileEntityExporterChestCustom tileEntity) {
		super(new TranslationTextComponent("gui.cqrepoured.map_placeholder_simple.title"));
		this.tileEntity = tileEntity;
	}

	@Override
	public void init() {
		this.lootTableTextField = new TextFieldWidget(this.font, this.width / 2 - 70, this.height / 2 - 70, 140, 20, new TranslationTextComponent("gui.cqrepoured.exporter_chest_custom.loot_table"));
		this.lootTableTextField.setValue(this.tileEntity.getLootTable().toString());
		this.children.add(this.lootTableTextField);
	}

	@Override
	public void onClose() {
		super.onClose();
		this.tileEntity.setLootTable(new ResourceLocation(this.lootTableTextField.getValue()));
	}

	public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
		if (super.keyPressed(pKeyCode, pScanCode, pModifiers)) {
			return true;
		} else if (pKeyCode != 257 && pKeyCode != 335) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		super.mouseClicked(mouseX, mouseY, mouseButton);

		this.lootTableTextField.mouseClicked(mouseX, mouseY, mouseButton);
		return true;
	}

	@Override
	public void tick() {
		super.tick();

		this.lootTableTextField.tick();
	}

	@Override
	public void render(PoseStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
		this.renderBackground(pMatrixStack);
		this.drawCenteredString(pMatrixStack, this.font, new TranslationTextComponent("gui.cqrepoured.exporter_chest_custom.name"), this.width / 2, 20, 0xFFFFFF);

		this.drawString(pMatrixStack, this.font, "Loot Table", this.width / 2 - 70, this.height / 2 - 80, 0xA0A0A0);
		this.lootTableTextField.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);;

		super.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);

	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}
}
