package com.teamcqr.chocolatequestrepoured.client.gui.npceditor;

import java.io.IOException;

import org.lwjgl.input.Mouse;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.client.gui.GuiButtonTextured;
import com.teamcqr.chocolatequestrepoured.client.gui.IUpdatableGui;
import com.teamcqr.chocolatequestrepoured.client.util.GuiHelper;
import com.teamcqr.chocolatequestrepoured.inventory.ContainerMerchant;
import com.teamcqr.chocolatequestrepoured.network.client.packet.CPacketDeleteTrade;
import com.teamcqr.chocolatequestrepoured.network.client.packet.CPacketOpenEditTradeGui;
import com.teamcqr.chocolatequestrepoured.network.client.packet.CPacketSyncSelectedTrade;
import com.teamcqr.chocolatequestrepoured.network.client.packet.CPacketUpdateTradeIndex;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.objects.npc.trading.Trade;
import com.teamcqr.chocolatequestrepoured.objects.npc.trading.TraderOffer;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiMerchant extends GuiContainer implements IUpdatableGui {

	private static final ResourceLocation BG_TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/container/gui_merchant.png");
	private static final ResourceLocation BG_TEXTURE_CREATIVE = new ResourceLocation(Reference.MODID, "textures/gui/container/gui_merchant_creative.png");

	private final AbstractEntityCQR entity;
	private final TraderOffer trades;
	private final GuiButtonTrade[] tradeButtons = new GuiButtonTrade[7];
	private final GuiButton[] pushUpButtons = new GuiButton[this.tradeButtons.length - 1];
	private final GuiButton[] pushDownButtons = new GuiButton[this.tradeButtons.length - 1];
	private final GuiButton[] deleteButtons = new GuiButton[this.tradeButtons.length - 1];
	private final GuiButton[] editButtons = new GuiButton[this.tradeButtons.length - 1];
	private GuiButton addNewTradeButton;
	private int buttonStartIndex = 0;
	private boolean scrollBarClicked;

	public GuiMerchant(Container container, AbstractEntityCQR entity) {
		super(container);
		this.entity = entity;
		this.trades = entity.getTrades();
		this.xSize = 307;
		this.ySize = 166;
	}

	@Override
	public void initGui() {
		super.initGui();

		for (int i = 0; i < this.tradeButtons.length; i++) {
			this.tradeButtons[i] = this.addButton(new GuiButtonTrade(10 + i, this.guiLeft + 8, this.guiTop + 18 + i * 20, i));
			if (i < this.tradeButtons.length - 1) {
				this.pushUpButtons[i] = this.addButton(new GuiButtonTextured(20 + i, this.guiLeft - 12, this.guiTop + 18 + i * 20, 10, 10, "", "container/gui_button_10px", "container/icon_up"));
				this.pushDownButtons[i] = this.addButton(new GuiButtonTextured(30 + i, this.guiLeft - 12, this.guiTop + 28 + i * 20, 10, 10, "", "container/gui_button_10px", "container/icon_down"));
				this.deleteButtons[i] = this.addButton(new GuiButtonTextured(40 + i, this.guiLeft - 2, this.guiTop + 18 + i * 20, 10, 10, "", "container/gui_button_10px", "container/icon_delete"));
				this.editButtons[i] = this.addButton(new GuiButtonTextured(50 + i, this.guiLeft - 2, this.guiTop + 28 + i * 20, 10, 10, "", "container/gui_button_10px", "container/icon_edit"));
			}
		}
		this.addNewTradeButton = this.addButton(new GuiButton(0, this.guiLeft - 12, this.guiTop + 138, 136, 20, "- Create Trade -"));

		this.update();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
		for (GuiButtonTrade tradeButton : this.tradeButtons) {
			tradeButton.renderHoveredToolTip(this, mouseX, mouseY);
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		this.drawDefaultBackground();

		if (this.mc.player.isCreative()) {
			this.mc.getTextureManager().bindTexture(BG_TEXTURE_CREATIVE);
			GuiHelper.drawTexture(this.guiLeft - 20.0D, this.guiTop, 0.0D, 0.0D, this.xSize + 20.0D, this.ySize, (this.xSize + 20) / 512.0D, this.ySize / 256.0D);
		} else {
			this.mc.getTextureManager().bindTexture(BG_TEXTURE);
			GuiHelper.drawTexture(this.guiLeft, this.guiTop, 0.0D, 0.0D, this.xSize, this.ySize, this.xSize / 512.0D, this.ySize / 256.0D);
		}

		if (this.trades.size() > this.tradeButtons.length - (this.mc.player.isCreative() ? 1 : 0)) {
			int scrollOffsetY = (int) ((double) this.buttonStartIndex / (double) (this.trades.size() - (this.tradeButtons.length - (this.mc.player.isCreative() ? 1 : 0))) * 113.0D);
			GuiHelper.drawTexture(this.guiLeft + 125.0D, this.guiTop + 18.0D + scrollOffsetY, 0.0D, 166.0D / 256.0D, 6, 27, 6.0D / 512.0D, 27.0D / 256.0D);
		} else {
			GuiHelper.drawTexture(this.guiLeft + 125.0D, this.guiTop + 18.0D, 6.0D / 512.0D, 166.0D / 256.0D, 6, 27, 6.0D / 512.0D, 27.0D / 256.0D);
		}

		if (this.mc.player.isCreative()) {
			this.fontRenderer.drawString(this.entity.getDisplayName().getFormattedText(), this.guiLeft - 13, this.guiTop + 7, 0x404040);
		} else {
			this.fontRenderer.drawString(this.entity.getDisplayName().getFormattedText(), this.guiLeft + 7, this.guiTop + 7, 0x404040);
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
		} else if (button == this.addNewTradeButton) {
			CQRMain.NETWORK.sendToServer(new CPacketOpenEditTradeGui(this.entity.getEntityId(), this.trades.size()));
		} else if (button.id >= 20 && button.id < 30) {
			CQRMain.NETWORK.sendToServer(new CPacketUpdateTradeIndex(this.entity.getEntityId(), this.buttonStartIndex + button.id - 20, this.buttonStartIndex + button.id - 20 - 1));
		} else if (button.id >= 30 && button.id < 40) {
			CQRMain.NETWORK.sendToServer(new CPacketUpdateTradeIndex(this.entity.getEntityId(), this.buttonStartIndex + button.id - 30, this.buttonStartIndex + button.id - 30 + 1));
		} else if (button.id >= 40 && button.id < 50) {
			CQRMain.NETWORK.sendToServer(new CPacketDeleteTrade(this.entity.getEntityId(), this.buttonStartIndex + button.id - 40));
		} else if (button.id >= 50 && button.id < 60) {
			CQRMain.NETWORK.sendToServer(new CPacketOpenEditTradeGui(this.entity.getEntityId(), this.buttonStartIndex + button.id - 50));
		}
	}

	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		double dWheel = Mouse.getEventDWheel();
		if (dWheel != 0.0D) {
			int scrollAmount = (int) (dWheel / 60.0D);
			if (this.trades.size() > this.tradeButtons.length - (this.mc.player.isCreative() ? 1 : 0)) {
				this.buttonStartIndex = MathHelper.clamp(this.buttonStartIndex - scrollAmount, 0, this.trades.size() - (this.tradeButtons.length - (this.mc.player.isCreative() ? 1 : 0)));
				this.update();
			}
		}
	}

	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		if (this.scrollBarClicked) {
			int y1 = this.guiTop + 18;
			int y2 = y1 + 139;
			int scrollLength = this.trades.size() - (this.tradeButtons.length - (this.mc.player.isCreative() ? 1 : 0));
			float f = ((float) mouseY - (float) y1 - 13.5F) / ((float) (y2 - y1) - 27.0F);
			f = f * (float) scrollLength + 0.5F;
			this.buttonStartIndex = MathHelper.clamp((int) f, 0, scrollLength);
			this.update();
		} else {
			super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);

		this.scrollBarClicked = false;
		if (this.trades.size() > this.tradeButtons.length - (this.mc.player.isCreative() ? 1 : 0) && mouseButton == 0) {
			int x1 = this.guiLeft + 125;
			int y1 = this.guiTop + 18;
			int x2 = x1 + 5;
			int y2 = y1 + 139;
			if (x1 <= mouseX && mouseX <= x2 && y1 <= mouseY && mouseY <= y2) {
				this.scrollBarClicked = true;
			}
		}
	}

	@Override
	public void update() {
		if (this.trades.size() > this.tradeButtons.length - (this.mc.player.isCreative() ? 1 : 0)) {
			this.buttonStartIndex = MathHelper.clamp(this.buttonStartIndex, 0, this.trades.size() - (this.tradeButtons.length - (this.mc.player.isCreative() ? 1 : 0)));
		} else {
			this.buttonStartIndex = 0;
		}

		for (int i = 0; i < this.tradeButtons.length; i++) {
			Trade trade = this.trades.get(this.buttonStartIndex + i);

			this.tradeButtons[i].visible = trade != null && (i < this.tradeButtons.length - 1 || !this.mc.player.isCreative());
			if (trade != null) {
				this.tradeButtons[i].setIndex(this.buttonStartIndex + i);
				this.tradeButtons[i].setTrade(trade);
			}

			if (i < this.tradeButtons.length - 1) {
				this.pushUpButtons[i].visible = trade != null && this.mc.player.isCreative();
				this.pushDownButtons[i].visible = trade != null && this.mc.player.isCreative();
				this.deleteButtons[i].visible = trade != null && this.mc.player.isCreative();
				this.editButtons[i].visible = trade != null && this.mc.player.isCreative();
			}
		}

		this.addNewTradeButton.visible = this.mc.player.isCreative();
		this.addNewTradeButton.x = this.guiLeft - 12;
		this.addNewTradeButton.y = this.guiTop + (this.trades.size() < this.tradeButtons.length - 1 ? 18 + this.trades.size() * 20 : 138);
	}

	// Overriding to set access modifier to public
	@Override
	public void renderToolTip(ItemStack stack, int x, int y) {
		super.renderToolTip(stack, x, y);
	}

	@Override
	public int getGuiLeft() {
		return super.getGuiLeft() - (this.mc.player.isCreative() ? 20 : 0);
	}

	@Override
	public int getXSize() {
		return super.getXSize() + (this.mc.player.isCreative() ? 20 : 0);
	}

}
