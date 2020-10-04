package com.teamcqr.chocolatequestrepoured.client.gui.npceditor;

import java.util.ArrayList;
import java.util.List;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.client.util.GuiHelper;
import com.teamcqr.chocolatequestrepoured.factions.FactionRegistry;
import com.teamcqr.chocolatequestrepoured.objects.npc.trading.Trade;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

public class GuiButtonTrade extends GuiButton {

	private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/container/gui_button_trade.png");
	private int index;
	private Trade trade;

	public GuiButtonTrade(int buttonId, int x, int y, int index) {
		super(buttonId, x, y, 116, 20, "");
		this.index = index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getIndex() {
		return this.index;
	}

	public void setTrade(Trade trade) {
		this.trade = trade;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		if (this.visible && this.trade != null) {
			mc.getTextureManager().bindTexture(TEXTURE);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
			int i = this.getHoverState(this.hovered);
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			GuiHelper.drawTexture(this.x, this.y, 0.0D, i / 4.0D, this.width, this.height, 0.90625D, 0.25D);
			this.mouseDragged(mc, mouseX, mouseY);

			GuiHelper.drawTexture(this.x + 77.0D, this.y + 2.0D, 0.0D, 0.75D, 16.0D, 16.0D, 0.125D, 0.2D);
			if (!this.trade.isUnlockedFor(mc.player)) {
				GuiHelper.drawTexture(this.x + 77.0D, this.y + 2.0D, 0.25D, 0.75D, 16.0D, 16.0D, 0.125D, 0.2D);
			} else if (!this.trade.isInStock()) {
				GuiHelper.drawTexture(this.x + 77.0D, this.y + 2.0D, 0.125D, 0.75D, 16.0D, 16.0D, 0.125D, 0.2D);
			}

			RenderItem itemRender = mc.getRenderItem();
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
			RenderHelper.enableGUIStandardItemLighting();
			GlStateManager.enableRescaleNormal();
			GlStateManager.enableDepth();

			itemRender.zLevel = 100.0F;
			int x = this.x + 4;
			int y = this.y + 2;
			for (int j = 0; j < 4 && j < this.trade.getInputItemsClient().size(); j++) {
				itemRender.renderItemAndEffectIntoGUI(mc.player, this.trade.getInputItemsClient().get(j).getStack(), x + j * 18, y);
				itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, this.trade.getInputItemsClient().get(j).getStack(), x + j * 18, y, null);
			}
			itemRender.renderItemAndEffectIntoGUI(mc.player, this.trade.getOutputClient(), x + 92, y);
			itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, this.trade.getOutputClient(), x + 92, y, null);
			itemRender.zLevel = 0.0F;

			GlStateManager.disableDepth();
			GlStateManager.disableRescaleNormal();
			RenderHelper.disableStandardItemLighting();
		}
	}

	public void renderHoveredToolTip(GuiMerchant parent, int mouseX, int mouseY) {
		if (!this.visible || this.trade == null) {
			return;
		}
		for (int i = 0; i < 4 && i < this.trade.getInputItemsClient().size(); i++) {
			int x = this.x + 4 + i * 18;
			int y = this.y + 2;
			if (mouseX >= x && mouseX <= x + 16 && mouseY >= y && mouseY <= y + 16) {
				parent.renderToolTip(this.trade.getInputItemsClient().get(i).getStack(), mouseX, mouseY);
			}
		}
		int x = this.x + 96;
		int y = this.y + 2;
		if (mouseX >= x && mouseX <= x + 16 && mouseY >= y && mouseY <= y + 16) {
			parent.renderToolTip(this.trade.getOutputClient(), mouseX, mouseY);
		}
		x = this.x + 78;
		if (mouseX >= x && mouseX <= x + 16 && mouseY >= y && mouseY <= y + 16) {
			List<String> tooltip = new ArrayList<>();
			boolean isUnlocked = this.trade.isUnlockedFor(parent.mc.player);
			boolean inStock = this.trade.isInStock();
			if (!isUnlocked) {
				tooltip.add(I18n.format("description.gui_button_trade.locked.name"));
				if (this.trade.getRequiredAdvancement() != null) {
					tooltip.add("" + (CQRMain.proxy.hasAdvancement(parent.mc.player, this.trade.getRequiredAdvancement()) ? TextFormatting.GREEN : TextFormatting.RED) + CQRMain.proxy.getAdvancement(parent.mc.player, this.trade.getRequiredAdvancement()).getDisplay().getTitle().getFormattedText());
				}
				if (this.trade.getRequiredReputation() != Integer.MIN_VALUE) {
					int i = FactionRegistry.instance().getExactReputationOf(parent.mc.player.getUniqueID(), this.trade.getHolder().getTraderFaction());
					tooltip.add("" + (i >= this.trade.getRequiredReputation() ? TextFormatting.GREEN : TextFormatting.RED) + this.trade.getHolder().getTraderFaction().getName() + " " + i + "/" + this.trade.getRequiredReputation());
				}
			} else if (!inStock) {
				tooltip.add(I18n.format("description.gui_button_trade.out_of_stock.name"));
			} else {
				tooltip.add(I18n.format("description.gui_button_trade.unlocked.name"));
				if (this.trade.getRequiredAdvancement() != null) {
					tooltip.add("" + TextFormatting.GREEN + CQRMain.proxy.getAdvancement(parent.mc.player, this.trade.getRequiredAdvancement()).getDisplay().getTitle().getFormattedText());
				}
				if (this.trade.getRequiredReputation() != Integer.MIN_VALUE) {
					int i = this.trade.getRequiredReputation();
					tooltip.add("" + TextFormatting.GREEN + this.trade.getHolder().getTraderFaction().getName() + " " + i + "/" + i);
				}
			}
			parent.drawHoveringText(tooltip, mouseX, mouseY);
		}
	}

}
