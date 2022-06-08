package team.cqr.cqrepoured.client.gui.npceditor;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import javax.annotation.Nullable;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.CheckboxButton;
import net.minecraft.client.util.InputMappings;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.gui.INumericIDButton;
import team.cqr.cqrepoured.client.gui.IdentifiedButton;
import team.cqr.cqrepoured.client.util.GuiHelper;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.entity.trade.Trade;
import team.cqr.cqrepoured.entity.trade.TradeInput;
import team.cqr.cqrepoured.network.client.packet.CPacketContainerClickButton;
import team.cqr.cqrepoured.network.client.packet.CPacketOpenMerchantGui;

@OnlyIn(Dist.CLIENT)
public class GuiMerchantEditTrade extends ContainerScreen {

	private static final ResourceLocation BG_TEXTURE = new ResourceLocation(CQRMain.MODID, "textures/gui/container/gui_merchant_edit_trade.png");

	private final AbstractEntityCQR entity;
	private final int tradeIndex;
	@Nullable
	private final Trade trade;
	
	private final CheckboxButton[] ignoreMetaCheckBoxes = new CheckboxButton[4];
	private final CheckboxButton[] ignoreNBTCheckBoxes = new CheckboxButton[4];

	private CheckboxButton stockCheckBox;
	private TextFieldWidget restockTextField;
	private TextFieldWidget inStockTextField;
	private TextFieldWidget maxStockTextField;

	private GuiButtonReputation reputationButton;
	private TextFieldWidget advancementTextField;

	public GuiMerchantEditTrade(Container container, AbstractEntityCQR entity, int tradeIndex) {
		super(container);
		this.entity = entity;
		this.tradeIndex = tradeIndex;
		this.trade = entity.getTrades().get(tradeIndex);
		this.imageWidth = 304;
		this.imageHeight = 166;
	}

	@Override
	public void init() {
		super.init();

		this.addButton(new IdentifiedButton(0, this.leftPos + 155, this.topPos + 139, 142, 20, new TranslationTextComponent("description.gui_merchant_edit_trade.cancel"), this::actionPerformed));
		this.addButton(new IdentifiedButton(1, this.leftPos + 7, this.topPos + 139, 142, 20, new TranslationTextComponent("description.gui_merchant_edit_trade.apply"), this::actionPerformed));

		List<TradeInput> tradeInputs = this.trade != null ? this.trade.getInputItems() : Collections.emptyList();
		for (int i = 0; i < this.ignoreMetaCheckBoxes.length; i++) {
			this.ignoreMetaCheckBoxes[i] = this.addButton(
					new CheckboxButton(/*i * 2 + 2,*/ this.leftPos + i * 26 + 76, this.topPos + 31, 11, 11, new StringTextComponent(""), i < tradeInputs.size() && tradeInputs.get(i).ignoreMeta()));
			this.ignoreNBTCheckBoxes[i] = this.addButton(
					new CheckboxButton(/*i * 2 + 3,*/ this.leftPos + i * 26 + 76, this.topPos + 44, 11, 11, new StringTextComponent(""), i < tradeInputs.size() && tradeInputs.get(i).ignoreNBT()));
		}

		this.reputationButton = this.addButton(new GuiButtonReputation(30, this.leftPos + 7, this.topPos + 72, this::actionPerformed));
		this.reputationButton.setReputationIndex(this.trade != null ? this.trade.getRequiredReputation() : Integer.MIN_VALUE);
		this.advancementTextField = new TextFieldWidget(/*40, */this.font, this.leftPos + 8, this.topPos + 103, 58, 10, new StringTextComponent(""));
		this.advancementTextField.setValue(this.trade != null && this.trade.getRequiredAdvancement() != null ? this.trade.getRequiredAdvancement().toString() : "");

		this.stockCheckBox = this.addWidget(
				new CheckboxButton(/*20, */this.leftPos + 237, this.topPos + 17, 11, 11, new StringTextComponent(""), this.trade != null && this.trade.hasLimitedStock()) {
					public void onPress() {
						super.onPress();
						GuiMerchantEditTrade.this.actionPerformedAbstractButton(this);
					};
				}
		);
		this.restockTextField = new TextFieldWidget(/*21, */this.font, this.leftPos + 238, this.topPos + 43, 38, 10, new StringTextComponent(""));
		this.restockTextField.setValue(this.trade != null ? Integer.toString(this.trade.getRestockRate()) : "0");
		this.restockTextField.setEditable(this.stockCheckBox.selected());
		this.inStockTextField = new TextFieldWidget(/*21, */this.font, this.leftPos + 238, this.topPos + 69, 38, 10, new StringTextComponent(""));
		this.inStockTextField.setValue(this.trade != null ? Integer.toString(this.trade.getInStock()) : "0");
		this.inStockTextField.setEditable(this.stockCheckBox.selected());
		this.maxStockTextField = new TextFieldWidget(/*22, */this.font, this.leftPos + 238, this.topPos + 95, 38, 10, new StringTextComponent(""));
		this.maxStockTextField.setValue(this.trade != null ? Integer.toString(this.trade.getMaxStock()) : "0");
		this.maxStockTextField.setEditable(this.stockCheckBox.selected());
	}

	@Override
	public void render(MatrixStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
		super.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);

		this.advancementTextField.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);;

		this.restockTextField.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);
		this.inStockTextField.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);
		this.maxStockTextField.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);

		this.renderTooltip(pMatrixStack, pMouseX, pMouseY);

		for (int i = 0; i < this.ignoreMetaCheckBoxes.length; i++) {
			if (this.ignoreMetaCheckBoxes[i].isMouseOver(pMouseX, pMouseY)) {
				this.renderTooltip(pMatrixStack, new TranslationTextComponent("description.gui_merchant_edit_trade.ignore_meta_check_box.name"), pMouseX, pMouseY);
			}
		}
		for (int i = 0; i < this.ignoreNBTCheckBoxes.length; i++) {
			if (this.ignoreNBTCheckBoxes[i].isMouseOver(pMouseX, pMouseY)) {
				this.renderTooltip(pMatrixStack, new TranslationTextComponent("description.gui_merchant_edit_trade.ignore_nbt_check_box.name"), pMouseX, pMouseY);
			}
		}
		if (this.reputationButton.isMouseOver(pMouseX, pMouseY)) {
			this.renderTooltip(pMatrixStack, new TranslationTextComponent("description.gui_merchant_edit_trade.reputation_button.name"), pMouseX, pMouseY);
		}
		if (GuiHelper.isMouseOver(pMouseX, pMouseY, this.advancementTextField)) {
			this.renderTooltip(pMatrixStack, new TranslationTextComponent("description.gui_merchant_edit_trade.advancement_text_field.name"), pMouseX, pMouseY);
		}
		if (this.stockCheckBox.isMouseOver(pMouseX, pMouseY)) {
			this.renderTooltip(pMatrixStack, new TranslationTextComponent("description.gui_merchant_edit_trade.stock_check_box.name"), pMouseX, pMouseY);
		}
		if (GuiHelper.isMouseOver(pMouseX, pMouseY, this.restockTextField)) {
			this.renderTooltip(pMatrixStack, new TranslationTextComponent("description.gui_merchant_edit_trade.restock_text_field.name"), pMouseX, pMouseY);
		}
		if (GuiHelper.isMouseOver(pMouseX, pMouseY, this.inStockTextField)) {
			this.renderTooltip(pMatrixStack, new TranslationTextComponent("description.gui_merchant_edit_trade.in_stock_text_field.name"), pMouseX, pMouseY);
		}
		if (GuiHelper.isMouseOver(pMouseX, pMouseY, this.maxStockTextField)) {
			this.renderTooltip(pMatrixStack, new TranslationTextComponent("description.gui_merchant_edit_trade.max_stock_text_field.name"), pMouseX, pMouseY);
		}
	}
	
	@Override
	public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
		boolean sr = super.mouseClicked(pMouseX, pMouseY, pButton);
		

		this.advancementTextField.mouseClicked(pMouseX, pMouseY, pButton);

		if (this.stockCheckBox.selected()) {
			this.restockTextField.mouseClicked(pMouseX, pMouseY, pButton);
			this.inStockTextField.mouseClicked(pMouseX, pMouseY, pButton);
			this.maxStockTextField.mouseClicked(pMouseX, pMouseY, pButton);
		}

		if (pButton == 1 && this.reputationButton.isMouseOver(pMouseX, pMouseY)) { 
			this.reputationButton.playDownSound(this.minecraft.getSoundManager());
			this.reputationButton.updateReputationIndex(false);
		}
		
		return sr;
	}

	@Override
	public void tick() {
		super.tick();
		
		this.advancementTextField.tick();

		if (this.stockCheckBox.selected()) {
			this.restockTextField.tick();
			this.inStockTextField.tick();
			this.maxStockTextField.tick();
		}
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	protected void actionPerformed(Button button) {
		this.actionPerformedAbstractButton(button);
	}
	
	protected void actionPerformedAbstractButton(AbstractButton button) {
		if (button instanceof INumericIDButton) {
			INumericIDButton inidb = (INumericIDButton) button;
			if (inidb.getId() == 0) {
				CQRMain.NETWORK.sendToServer(new CPacketContainerClickButton(inidb.getId()));
			} else if (inidb.getId() == 1) {
				boolean[] ignoreMeta = new boolean[this.ignoreMetaCheckBoxes.length];
				for (int i = 0; i < ignoreMeta.length; i++) {
					ignoreMeta[i] = this.ignoreMetaCheckBoxes[i].selected();
				}
				boolean[] ignoreNBT = new boolean[this.ignoreNBTCheckBoxes.length];
				for (int i = 0; i < ignoreNBT.length; i++) {
					ignoreNBT[i] = this.ignoreNBTCheckBoxes[i].selected();
				}
				//This takes it granted that stringtextComponent is used
				String reputation = this.reputationButton.getMessage().getContents();
				String advancement = this.advancementTextField.getValue();
				boolean stock = this.stockCheckBox.selected();
				int restock = 0;
				try {
					restock = Integer.parseInt(this.restockTextField.getValue());
				} catch (Exception e) {
					// ignore
				}
				int inStock = 0;
				try {
					inStock = Integer.parseInt(this.inStockTextField.getValue());
				} catch (Exception e) {
					// ignore
				}
				int maxStock = 0;
				try {
					maxStock = Integer.parseInt(this.maxStockTextField.getValue());
				} catch (Exception e) {
					// ignore
				}
	
				CPacketContainerClickButton packet = new CPacketContainerClickButton(inidb.getId());
				PacketBuffer extraData = packet.getExtraData();
				extraData.writeInt(this.tradeIndex);
				IntStream.range(0, ignoreMeta.length).forEach(i -> extraData.writeBoolean(ignoreMeta[i]));
				IntStream.range(0, ignoreNBT.length).forEach(i -> extraData.writeBoolean(ignoreNBT[i]));
				//ByteBufUtils.writeUTF8String(extraData, reputation);
				extraData.writeUtf(reputation);
				//ByteBufUtils.writeUTF8String(extraData, advancement);
				extraData.writeUtf(advancement);
				extraData.writeBoolean(stock);
				extraData.writeInt(restock);
				extraData.writeInt(inStock);
				extraData.writeInt(maxStock);
	
				CQRMain.NETWORK.sendToServer(packet);
			}
		} else if (button == this.stockCheckBox) {
			this.restockTextField.setEditable(this.stockCheckBox.selected());
			this.restockTextField.setFocus(false);
			this.inStockTextField.setEditable(this.stockCheckBox.selected());
			this.inStockTextField.setFocus(false);
			this.maxStockTextField.setEditable(this.stockCheckBox.selected());
			this.maxStockTextField.setFocus(false);
		} else if (button == this.reputationButton) {
			this.reputationButton.updateReputationIndex(true);
		}
	}

	@Override
	public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
		 InputMappings.Input keyInput = InputMappings.getKey(pKeyCode, pScanCode);
		if (this.advancementTextField.isFocused()
				|| (this.stockCheckBox.selected()
						&& (this.restockTextField.isFocused() || this.inStockTextField.isFocused() || this.maxStockTextField.isFocused()))) {
			if (pKeyCode == 0) {//Escape key
				this.advancementTextField.setFocus(false);
				this.restockTextField.setFocus(false);
				this.inStockTextField.setFocus(false);
				this.maxStockTextField.setFocus(false);
			} else {
				this.advancementTextField.textboxKeyTyped(typedChar, pKeyCode);
				if (Character.isDigit(typedChar) || pKeyCode == 14 || pKeyCode == 211 || pKeyCode == 203 || pKeyCode == 205 || pKeyCode == 199 || pKeyCode == 207) {
					this.restockTextField.textboxKeyTyped(typedChar, pKeyCode);
					this.inStockTextField.textboxKeyTyped(typedChar, pKeyCode);
					this.maxStockTextField.textboxKeyTyped(typedChar, pKeyCode);
				}
			}
		} else if (pKeyCode == 1 || this.minecraft.options.keyInventory.isActiveAndMatches(pKeyCode)) {
			CQRMain.NETWORK.sendToServer(new CPacketOpenMerchantGui(this.entity.getId()));
		} else {
			super.keyPressed(pKeyCode, pScanCode, pModifiers);
		}
	}

	@Override
	protected void renderBg(MatrixStack pMatrixStack, float pPartialTicks, int pX, int pY) {
		//this.drawDefaultBackground();
		this.minecraft.getTextureManager().bind(BG_TEXTURE);
		GuiHelper.drawTexture(this.leftPos, this.topPos, 0.0D, 0.0D, this.imageWidth, this.imageHeight, this.imageWidth / 512.0D, this.imageHeight / 256.0D);

		this.font.draw(pMatrixStack, new TranslationTextComponent("gui.merchant.edit.ignoremeta"), this.leftPos + 7, this.topPos + 33, 0x404040);
		this.font.draw(pMatrixStack, new TranslationTextComponent("gui.merchant.edit.ignorenbt"), this.leftPos + 7, this.topPos + 46, 0x404040);

		this.font.draw(pMatrixStack, new TranslationTextComponent("gui.merchant.edit.reputation_label"), this.leftPos + 7, this.topPos + 62, 0x404040);
		this.font.draw(pMatrixStack, new TranslationTextComponent("gui.merchant.edit.advancement_label"), this.leftPos + 7, this.topPos + 92, 0x404040);

		this.font.draw(pMatrixStack, new TranslationTextComponent("gui.merchant.edit.stock.label"), this.leftPos + 237, this.topPos + 7, 0x404040);
		this.font.draw(pMatrixStack, new TranslationTextComponent("gui.merchant.edit.stock.restock"), this.leftPos + 237, this.topPos + 32, 0x404040);
		this.font.draw(pMatrixStack, new TranslationTextComponent("gui.merchant.edit.stock.in_stock"), this.leftPos + 237, this.topPos + 58, 0x404040);
		this.font.draw(pMatrixStack, new TranslationTextComponent("gui.merchant.edit.stock.max_stack"), this.leftPos + 237, this.topPos + 84, 0x404040);
	}

}
