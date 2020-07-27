package com.teamcqr.chocolatequestrepoured.client.gui;

import java.io.IOException;

import org.lwjgl.input.Mouse;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.client.util.GuiHelper;
import com.teamcqr.chocolatequestrepoured.inventory.ContainerMerchant;
import com.teamcqr.chocolatequestrepoured.network.packets.toServer.CPacketDeleteTrade;
import com.teamcqr.chocolatequestrepoured.network.packets.toServer.CPacketOpenEditTradeGui;
import com.teamcqr.chocolatequestrepoured.network.packets.toServer.CPacketSyncSelectedTrade;
import com.teamcqr.chocolatequestrepoured.network.packets.toServer.CPacketUpdateTradeIndex;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.objects.npc.trading.Trade;
import com.teamcqr.chocolatequestrepoured.objects.npc.trading.TraderOffer;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class GuiMerchant extends GuiContainer implements IUpdatableGui {

	private static final ResourceLocation BG_TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/container/gui_merchant.png");
	private static final ResourceLocation BG_TEXTURE_CREATIVE = new ResourceLocation(Reference.MODID, "textures/gui/container/gui_merchant_creative.png");

	private static final int GUI_WIDTH = 304;
	private static final int GUI_HEIGHT = 166;
	private final AbstractEntityCQR entity;
	private final TraderOffer trades;
	private final GuiButtonTrade[] tradeButtons = new GuiButtonTrade[7];
	private int buttonStartIndex = 0;
	private boolean scrollBarClicked;
	private final GuiButton[] creativeButtons = new GuiButton[this.tradeButtons.length * 4];

	public GuiMerchant(Container container, AbstractEntityCQR entity) {
		super(container);
		this.entity = entity;
		this.trades = entity.getTrades();
	}

	@Override
	public void initGui() {
		super.initGui();

		this.guiLeft = (this.width - GUI_WIDTH) / 2;
		this.guiTop = (this.height - GUI_HEIGHT) / 2;

		for (int i = 0; i < this.tradeButtons.length; i++) {
			this.tradeButtons[i] = this.addButton(new GuiButtonTrade(i, this.guiLeft + 5, this.guiTop + 18 + i * 20, 116, 20, i));
			int j = this.creativeButtons.length / this.tradeButtons.length;
			int k = Math.max(this.tradeButtons.length, j);
			this.creativeButtons[i * j] = this.addButton(new GuiButtonTextured((i + 1) * k, this.guiLeft - 15, this.guiTop + 18 + i * 20, 10, 10, "container/gui_button_10px", "container/icon_up"));
			this.creativeButtons[i * j + 1] = this.addButton(new GuiButtonTextured((i + 1) * k + 1, this.guiLeft - 15, this.guiTop + 28 + i * 20, 10, 10, "container/gui_button_10px", "container/icon_down"));
			this.creativeButtons[i * j + 2] = this.addButton(new GuiButtonTextured((i + 1) * k + 2, this.guiLeft - 5, this.guiTop + 18 + i * 20, 10, 10, "container/gui_button_10px", "container/icon_delete"));
			this.creativeButtons[i * j + 3] = this.addButton(new GuiButtonTextured((i + 1) * k + 3, this.guiLeft - 5, this.guiTop + 28 + i * 20, 10, 10, "container/gui_button_10px", "container/icon_edit"));
		}
		this.updateButtons();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		boolean flag = this.mc.player.isCreative();
		for (GuiButton button : this.creativeButtons) {
			button.visible = flag;
		}
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		this.drawDefaultBackground();

		if (this.mc.player.isCreative()) {
			this.mc.getTextureManager().bindTexture(BG_TEXTURE_CREATIVE);
			GuiHelper.drawTexture(this.guiLeft - 20.0D, this.guiTop, 0.0D, 0.0D, GUI_WIDTH + 20.0D, GUI_HEIGHT, (GUI_WIDTH + 20) / 512.0D, GUI_HEIGHT / 256.0D);
		} else {
			this.mc.getTextureManager().bindTexture(BG_TEXTURE);
			GuiHelper.drawTexture(this.guiLeft, this.guiTop, 0.0D, 0.0D, GUI_WIDTH, GUI_HEIGHT, GUI_WIDTH / 512.0D, GUI_HEIGHT / 256.0D);
		}

		if (this.trades.size() > this.tradeButtons.length) {
			int scrollOffsetY = (int) ((double) this.buttonStartIndex / (double) (this.trades.size() - this.tradeButtons.length) * 113.0D);
			GuiHelper.drawTexture(this.guiLeft + 122.0D, this.guiTop + 18.0D + scrollOffsetY, 0.0D, 199.0D / 256.0D, 6, 27, 6.0D / 512.0D, 27.0D / 256.0D);
		} else {
			GuiHelper.drawTexture(this.guiLeft + 122.0D, this.guiTop + 18.0D, 6.0D / 512.0D, 199.0D / 256.0D, 6, 27, 6.0D / 512.0D, 27.0D / 256.0D);
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button instanceof GuiButtonTrade) {
			((ContainerMerchant) this.inventorySlots).setCurrentTradeIndex(((GuiButtonTrade) button).getIndex());
			((ContainerMerchant) this.inventorySlots).updateInputsForTrade(((GuiButtonTrade) button).getIndex());
			CQRMain.NETWORK.sendToServer(new CPacketSyncSelectedTrade(((GuiButtonTrade) button).getIndex()));
		} else {
			int i = Math.max(this.tradeButtons.length, this.creativeButtons.length / this.tradeButtons.length);
			int j = button.id % i;
			int k = button.id / i - 1;
			if (j == 0) {
				CQRMain.NETWORK.sendToServer(new CPacketUpdateTradeIndex(this.entity.getEntityId(), k, k - 1));
			} else if (j == 1) {
				CQRMain.NETWORK.sendToServer(new CPacketUpdateTradeIndex(this.entity.getEntityId(), k, k + 1));
			} else if (j == 2) {
				CQRMain.NETWORK.sendToServer(new CPacketDeleteTrade(this.entity.getEntityId(), k));
			} else if (j == 3) {
				CQRMain.NETWORK.sendToServer(new CPacketOpenEditTradeGui(this.entity.getEntityId(), k));
			}
		}
	}

	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		double dWheel = Mouse.getEventDWheel();
		if (dWheel != 0.0D) {
			this.scroll((int) (dWheel / 60.0D));
		}
	}

	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		if (this.scrollBarClicked) {
			int y1 = this.guiTop + 18;
			int y2 = y1 + 139;
			int scrollLength = this.trades.size() - this.tradeButtons.length;
			float f = ((float) mouseY - (float) y1 - 13.5F) / ((float) (y2 - y1) - 27.0F);
			f = f * (float) scrollLength + 0.5F;
			this.buttonStartIndex = MathHelper.clamp((int) f, 0, scrollLength);
			this.updateButtons();
		} else {
			super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);

		this.scrollBarClicked = false;
		if (this.tradeButtons.length < this.trades.size() && mouseButton == 0) {
			int x1 = this.guiLeft + 122;
			int y1 = this.guiTop + 18;
			int x2 = x1 + 5;
			int y2 = y1 + 139;
			if (x1 <= mouseX && mouseX <= x2 && y1 <= mouseY && mouseY <= y2) {
				this.scrollBarClicked = true;
			}
		}
	}

	private void scroll(int amount) {
		if (this.trades.size() > this.tradeButtons.length) {
			this.buttonStartIndex = MathHelper.clamp(this.buttonStartIndex - amount, 0, this.trades.size() - this.tradeButtons.length);
			this.updateButtons();
		}
	}

	public void updateButtons() {
		for (int i = 0; i < this.tradeButtons.length; i++) {
			Trade trade = this.trades.get(this.buttonStartIndex + i);
			boolean flag = trade != null;
			if (flag) {
				this.tradeButtons[i].setIndex(this.buttonStartIndex + i);
				this.tradeButtons[i].setInputAndOutput(trade.getInputItems(), trade.getOutput());
			}
			this.tradeButtons[i].visible = flag;

			int j = this.creativeButtons.length / this.tradeButtons.length;
			for (int k = 0; k < j; k++) {
				this.creativeButtons[i * j + k].visible = this.mc.player.isCreative() && flag;
			}
		}
	}

	@Override
	public void update() {
		this.updateButtons();
	}

}
