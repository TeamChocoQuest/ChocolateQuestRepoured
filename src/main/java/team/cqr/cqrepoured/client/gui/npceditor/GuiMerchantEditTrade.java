package team.cqr.cqrepoured.client.gui.npceditor;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiCheckBox;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.util.GuiHelper;
import team.cqr.cqrepoured.network.client.packet.CPacketEditTrade;
import team.cqr.cqrepoured.network.client.packet.CPacketOpenMerchantGui;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.objects.npc.trading.Trade;
import team.cqr.cqrepoured.objects.npc.trading.TradeInput;
import team.cqr.cqrepoured.util.Reference;

@SideOnly(Side.CLIENT)
public class GuiMerchantEditTrade extends GuiContainer {

	private static final ResourceLocation BG_TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/container/gui_merchant_edit_trade.png");

	private final AbstractEntityCQR entity;
	private final int tradeIndex;
	@Nullable
	private final Trade trade;
	private final GuiCheckBox[] ignoreMetaCheckBoxes = new GuiCheckBox[4];
	private final GuiCheckBox[] ignoreNBTCheckBoxes = new GuiCheckBox[4];

	private GuiCheckBox stockCheckBox;
	private GuiTextField restockTextField;
	private GuiTextField inStockTextField;
	private GuiTextField maxStockTextField;

	private GuiButtonReputation reputationButton;
	private GuiTextField advancementTextField;

	public GuiMerchantEditTrade(Container container, AbstractEntityCQR entity, int tradeIndex) {
		super(container);
		this.entity = entity;
		this.tradeIndex = tradeIndex;
		this.trade = entity.getTrades().get(tradeIndex);
		this.xSize = 304;
		this.ySize = 166;
	}

	@Override
	public void initGui() {
		super.initGui();

		this.addButton(new GuiButton(0, this.guiLeft + 155, this.guiTop + 139, 142, 20, "Cancel"));
		this.addButton(new GuiButton(1, this.guiLeft + 7, this.guiTop + 139, 142, 20, "Apply"));

		List<TradeInput> tradeInputs = this.trade != null ? this.trade.getInputItems() : Collections.emptyList();
		for (int i = 0; i < this.ignoreMetaCheckBoxes.length; i++) {
			this.ignoreMetaCheckBoxes[i] = this.addButton(new GuiCheckBox(i * 2 + 2, this.guiLeft + i * 26 + 76, this.guiTop + 31, "", i < tradeInputs.size() && tradeInputs.get(i).ignoreMeta()));
			this.ignoreMetaCheckBoxes[i].width = 11;
			this.ignoreNBTCheckBoxes[i] = this.addButton(new GuiCheckBox(i * 2 + 3, this.guiLeft + i * 26 + 76, this.guiTop + 44, "", i < tradeInputs.size() && tradeInputs.get(i).ignoreNBT()));
			this.ignoreNBTCheckBoxes[i].width = 11;
		}

		this.reputationButton = this.addButton(new GuiButtonReputation(30, this.guiLeft + 7, this.guiTop + 72));
		this.reputationButton.setReputationIndex(this.trade != null ? this.trade.getRequiredReputation() : Integer.MIN_VALUE);
		this.advancementTextField = new GuiTextField(40, this.fontRenderer, this.guiLeft + 8, this.guiTop + 103, 58, 10);
		this.advancementTextField.setText(this.trade != null && this.trade.getRequiredAdvancement() != null ? this.trade.getRequiredAdvancement().toString() : "");

		this.stockCheckBox = this.addButton(new GuiCheckBox(20, this.guiLeft + 237, this.guiTop + 17, "", this.trade != null && this.trade.hasLimitedStock()));
		this.stockCheckBox.width = 11;
		this.restockTextField = new GuiTextField(21, this.fontRenderer, this.guiLeft + 238, this.guiTop + 43, 38, 10);
		this.restockTextField.setText(this.trade != null ? Integer.toString(this.trade.getRestockRate()) : "0");
		this.restockTextField.setEnabled(this.stockCheckBox.isChecked());
		this.inStockTextField = new GuiTextField(21, this.fontRenderer, this.guiLeft + 238, this.guiTop + 69, 38, 10);
		this.inStockTextField.setText(this.trade != null ? Integer.toString(this.trade.getInStock()) : "0");
		this.inStockTextField.setEnabled(this.stockCheckBox.isChecked());
		this.maxStockTextField = new GuiTextField(22, this.fontRenderer, this.guiLeft + 238, this.guiTop + 95, 38, 10);
		this.maxStockTextField.setText(this.trade != null ? Integer.toString(this.trade.getMaxStock()) : "0");
		this.maxStockTextField.setEnabled(this.stockCheckBox.isChecked());
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);

		this.advancementTextField.drawTextBox();

		this.restockTextField.drawTextBox();
		this.inStockTextField.drawTextBox();
		this.maxStockTextField.drawTextBox();

		this.renderHoveredToolTip(mouseX, mouseY);

		for (int i = 0; i < this.ignoreMetaCheckBoxes.length; i++) {
			if (this.ignoreMetaCheckBoxes[i].isMouseOver()) {
				this.drawHoveringText(I18n.format("description.gui_merchant_edit_trade.ignore_meta_check_box.name"), mouseX, mouseY);
			}
		}
		for (int i = 0; i < this.ignoreNBTCheckBoxes.length; i++) {
			if (this.ignoreNBTCheckBoxes[i].isMouseOver()) {
				this.drawHoveringText(I18n.format("description.gui_merchant_edit_trade.ignore_nbt_check_box.name"), mouseX, mouseY);
			}
		}
		if (this.reputationButton.isMouseOver()) {
			this.drawHoveringText(I18n.format("description.gui_merchant_edit_trade.reputation_button.name"), mouseX, mouseY);
		}
		if (GuiHelper.isMouseOver(mouseX, mouseY, this.advancementTextField)) {
			this.drawHoveringText(I18n.format("description.gui_merchant_edit_trade.advancement_text_field.name"), mouseX, mouseY);
		}
		if (this.stockCheckBox.isMouseOver()) {
			this.drawHoveringText(I18n.format("description.gui_merchant_edit_trade.stock_check_box.name"), mouseX, mouseY);
		}
		if (GuiHelper.isMouseOver(mouseX, mouseY, this.restockTextField)) {
			this.drawHoveringText(I18n.format("description.gui_merchant_edit_trade.restock_text_field.name"), mouseX, mouseY);
		}
		if (GuiHelper.isMouseOver(mouseX, mouseY, this.inStockTextField)) {
			this.drawHoveringText(I18n.format("description.gui_merchant_edit_trade.in_stock_text_field.name"), mouseX, mouseY);
		}
		if (GuiHelper.isMouseOver(mouseX, mouseY, this.maxStockTextField)) {
			this.drawHoveringText(I18n.format("description.gui_merchant_edit_trade.max_stock_text_field.name"), mouseX, mouseY);
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);

		this.advancementTextField.mouseClicked(mouseX, mouseY, mouseButton);

		if (this.stockCheckBox.isChecked()) {
			this.restockTextField.mouseClicked(mouseX, mouseY, mouseButton);
			this.inStockTextField.mouseClicked(mouseX, mouseY, mouseButton);
			this.maxStockTextField.mouseClicked(mouseX, mouseY, mouseButton);
		}

		if (mouseButton == 1 && this.reputationButton.isMouseOver()) {
			this.reputationButton.playPressSound(this.mc.getSoundHandler());
			this.reputationButton.updateReputationIndex(false);
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		this.drawDefaultBackground();
		this.mc.getTextureManager().bindTexture(BG_TEXTURE);
		GuiHelper.drawTexture(this.guiLeft, this.guiTop, 0.0D, 0.0D, this.xSize, this.ySize, this.xSize / 512.0D, this.ySize / 256.0D);

		this.fontRenderer.drawString("Ignore Meta", this.guiLeft + 7, this.guiTop + 33, 0x404040);
		this.fontRenderer.drawString("Ignore NBT", this.guiLeft + 7, this.guiTop + 46, 0x404040);

		this.fontRenderer.drawString("Reputation", this.guiLeft + 7, this.guiTop + 62, 0x404040);
		this.fontRenderer.drawString("Advancement", this.guiLeft + 7, this.guiTop + 92, 0x404040);

		this.fontRenderer.drawString("Stock", this.guiLeft + 237, this.guiTop + 7, 0x404040);
		this.fontRenderer.drawString("Restock", this.guiLeft + 237, this.guiTop + 32, 0x404040);
		this.fontRenderer.drawString("In Stock", this.guiLeft + 237, this.guiTop + 58, 0x404040);
		this.fontRenderer.drawString("Max Stock", this.guiLeft + 237, this.guiTop + 84, 0x404040);
	}

	@Override
	public void updateScreen() {
		super.updateScreen();

		this.advancementTextField.updateCursorCounter();

		if (this.stockCheckBox.isChecked()) {
			this.restockTextField.updateCursorCounter();
			this.inStockTextField.updateCursorCounter();
			this.maxStockTextField.updateCursorCounter();
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			CQRMain.NETWORK.sendToServer(new CPacketOpenMerchantGui(this.entity.getEntityId()));
		} else if (button.id == 1) {
			boolean[] ignoreMeta = new boolean[this.ignoreMetaCheckBoxes.length];
			for (int i = 0; i < ignoreMeta.length; i++) {
				ignoreMeta[i] = this.ignoreMetaCheckBoxes[i].isChecked();
			}
			boolean[] ignoreNBT = new boolean[this.ignoreNBTCheckBoxes.length];
			for (int i = 0; i < ignoreNBT.length; i++) {
				ignoreNBT[i] = this.ignoreNBTCheckBoxes[i].isChecked();
			}
			String reputation = this.reputationButton.displayString;
			String advancement = this.advancementTextField.getText();
			boolean stock = this.stockCheckBox.isChecked();
			int restock = 0;
			try {
				restock = Integer.parseInt(this.restockTextField.getText());
			} catch (Exception e) {
				// ignore
			}
			int inStock = 0;
			try {
				inStock = Integer.parseInt(this.inStockTextField.getText());
			} catch (Exception e) {
				// ignore
			}
			int maxStock = 0;
			try {
				maxStock = Integer.parseInt(this.maxStockTextField.getText());
			} catch (Exception e) {
				// ignore
			}

			CQRMain.NETWORK.sendToServer(new CPacketEditTrade(this.entity.getEntityId(), this.tradeIndex, ignoreMeta, ignoreNBT, reputation, advancement, stock, restock, inStock, maxStock));
			CQRMain.NETWORK.sendToServer(new CPacketOpenMerchantGui(this.entity.getEntityId()));
		} else if (button == this.stockCheckBox) {
			this.restockTextField.setEnabled(this.stockCheckBox.isChecked());
			this.restockTextField.setFocused(false);
			this.inStockTextField.setEnabled(this.stockCheckBox.isChecked());
			this.inStockTextField.setFocused(false);
			this.maxStockTextField.setEnabled(this.stockCheckBox.isChecked());
			this.maxStockTextField.setFocused(false);
		} else if (button == this.reputationButton) {
			this.reputationButton.updateReputationIndex(true);
		}
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (this.advancementTextField.isFocused() || (this.stockCheckBox.isChecked() && (this.restockTextField.isFocused() || this.inStockTextField.isFocused() || this.maxStockTextField.isFocused()))) {
			if (keyCode == 1) {
				this.advancementTextField.setFocused(false);
				this.restockTextField.setFocused(false);
				this.inStockTextField.setFocused(false);
				this.maxStockTextField.setFocused(false);
			} else {
				this.advancementTextField.textboxKeyTyped(typedChar, keyCode);
				if (Character.isDigit(typedChar) || keyCode == 14 || keyCode == 211 || keyCode == 203 || keyCode == 205 || keyCode == 199 || keyCode == 207) {
					this.restockTextField.textboxKeyTyped(typedChar, keyCode);
					this.inStockTextField.textboxKeyTyped(typedChar, keyCode);
					this.maxStockTextField.textboxKeyTyped(typedChar, keyCode);
				}
			}
		} else if (keyCode == 1 || this.mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode)) {
			CQRMain.NETWORK.sendToServer(new CPacketOpenMerchantGui(this.entity.getEntityId()));
		} else {
			super.keyTyped(typedChar, keyCode);
		}
	}

}
