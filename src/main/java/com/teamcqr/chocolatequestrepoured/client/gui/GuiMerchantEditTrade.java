package com.teamcqr.chocolatequestrepoured.client.gui;

import java.io.IOException;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.client.util.GuiHelper;
import com.teamcqr.chocolatequestrepoured.network.packets.toServer.CPacketEditTrade;
import com.teamcqr.chocolatequestrepoured.network.packets.toServer.CPacketOpenMerchantGui;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.objects.npc.trading.Trade;
import com.teamcqr.chocolatequestrepoured.objects.npc.trading.TradeInput;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiCheckBox;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class GuiMerchantEditTrade extends GuiContainer {

	private static final ResourceLocation BG_TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/container/gui_merchant_edit_trade.png");
	private static final int GUI_WIDTH = 240;
	private static final int GUI_HEIGHT = 164;

	private final AbstractEntityCQR entity;
	private final int tradeIndex;
	@Nullable
	private final Trade trade;
	private final GuiCheckBox[] ignoreMetaCheckboxes = new GuiCheckBox[4];
	private final GuiCheckBox[] ignoreNBTCheckboxes = new GuiCheckBox[4];

	public GuiMerchantEditTrade(Container container, AbstractEntityCQR entity, int tradeIndex) {
		super(container);
		this.entity = entity;
		this.tradeIndex = tradeIndex;
		this.trade = entity.getTrades().get(tradeIndex);
	}

	@Override
	public void initGui() {
		super.initGui();

		this.guiLeft = (this.width - GUI_WIDTH) / 2;
		this.guiTop = (this.height - GUI_HEIGHT) / 2;

		this.addButton(new GuiButton(0, this.guiLeft + 122, this.guiTop + 137, 111, 20, "Cancel"));
		this.addButton(new GuiButton(1, this.guiLeft + 7, this.guiTop + 137, 111, 20, "Apply"));
		this.ignoreMetaCheckboxes[0] = this.addButton(new GuiCheckBox(2, this.guiLeft + 76, this.guiTop + 30, "", false));
		this.ignoreNBTCheckboxes[0] = this.addButton(new GuiCheckBox(3, this.guiLeft + 76, this.guiTop + 42, "", false));
		this.ignoreMetaCheckboxes[1] = this.addButton(new GuiCheckBox(4, this.guiLeft + 102, this.guiTop + 30, "", false));
		this.ignoreNBTCheckboxes[1] = this.addButton(new GuiCheckBox(5, this.guiLeft + 102, this.guiTop + 42, "", false));
		this.ignoreMetaCheckboxes[2] = this.addButton(new GuiCheckBox(6, this.guiLeft + 128, this.guiTop + 30, "", false));
		this.ignoreNBTCheckboxes[2] = this.addButton(new GuiCheckBox(7, this.guiLeft + 128, this.guiTop + 42, "", false));
		this.ignoreMetaCheckboxes[3] = this.addButton(new GuiCheckBox(8, this.guiLeft + 154, this.guiTop + 30, "", false));
		this.ignoreNBTCheckboxes[3] = this.addButton(new GuiCheckBox(9, this.guiLeft + 154, this.guiTop + 42, "", false));
		if (this.trade != null) {
			NonNullList<TradeInput> tradeInputs = this.trade.getInputItems();

			for (int i = 0; i < tradeInputs.size(); i++) {
				TradeInput tradeInput = tradeInputs.get(i);

				if (i < this.ignoreMetaCheckboxes.length) {
					this.ignoreMetaCheckboxes[i].setIsChecked(tradeInput.ignoreMeta());
				}
				if (i < this.ignoreNBTCheckboxes.length) {
					this.ignoreNBTCheckboxes[i].setIsChecked(tradeInput.ignoreNBT());
				}
			}
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		this.drawDefaultBackground();
		this.mc.getTextureManager().bindTexture(BG_TEXTURE);
		GuiHelper.drawTexture(this.guiLeft, this.guiTop, 0.0D, 0.0D, GUI_WIDTH, GUI_HEIGHT, GUI_WIDTH / 512.0D, GUI_HEIGHT / 256.0D);
		this.drawString(this.fontRenderer, "Ignore Meta", this.guiLeft + 7, this.guiTop + 32, 0xFFFFFF);
		this.drawString(this.fontRenderer, "Ignore NBT", this.guiLeft + 7, this.guiTop + 44, 0xFFFFFF);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0 || button.id == 1) {
			if (button.id == 1) {
				boolean[] ignoreMeta = new boolean[4];
				for (int i = 0; i < ignoreMeta.length; i++) {
					ignoreMeta[i] = this.ignoreMetaCheckboxes[i].isChecked();
				}
				boolean[] ignoreNBT = new boolean[4];
				for (int i = 0; i < ignoreMeta.length; i++) {
					ignoreNBT[i] = this.ignoreNBTCheckboxes[i].isChecked();
				}
				CQRMain.NETWORK.sendToServer(new CPacketEditTrade(this.entity.getEntityId(), this.tradeIndex, ignoreMeta, ignoreNBT));
			}
			CQRMain.NETWORK.sendToServer(new CPacketOpenMerchantGui(this.entity.getEntityId()));
		}
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == 1 || this.mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode)) {
			CQRMain.NETWORK.sendToServer(new CPacketOpenMerchantGui(this.entity.getEntityId()));
		} else {
			super.keyTyped(typedChar, keyCode);
		}
	}

}
