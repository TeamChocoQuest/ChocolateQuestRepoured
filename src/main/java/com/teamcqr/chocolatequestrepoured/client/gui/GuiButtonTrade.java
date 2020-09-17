package com.teamcqr.chocolatequestrepoured.client.gui;

import com.teamcqr.chocolatequestrepoured.client.util.GuiHelper;
import com.teamcqr.chocolatequestrepoured.objects.npc.trading.TradeInput;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

public class GuiButtonTrade extends GuiButton {

	private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/container/gui_button_trade.png");
	private int index;
	private NonNullList<TradeInput> input;
	private ItemStack output;
	private boolean outOfStock;
	private boolean lockedForPlayer;

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

	public void setInput(NonNullList<TradeInput> input) {
		this.input = input;
	}

	public void setOutput(ItemStack output) {
		this.output = output;
	}

	public void setOutOfStock(boolean outOfStock) {
		this.outOfStock = outOfStock;
	}

	public void setLockedForPlayer(boolean lockedForPlayer) {
		this.lockedForPlayer = lockedForPlayer;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		if (this.visible && (this.input != null && this.output != null)) {
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
			if (this.lockedForPlayer) {
				GuiHelper.drawTexture(this.x + 77.0D, this.y + 2.0D, 0.25D, 0.75D, 16.0D, 16.0D, 0.125D, 0.2D);
			} else if (this.outOfStock) {
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
			for (int j = 0; j < 4 && j < this.input.size(); j++) {
				itemRender.renderItemAndEffectIntoGUI(mc.player, this.input.get(j).getStack(), x + j * 18, y);
				itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, this.input.get(j).getStack(), x + j * 18, y, null);
			}
			itemRender.renderItemAndEffectIntoGUI(mc.player, this.output, x + 92, y);
			itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, this.output, x + 92, y, null);
			itemRender.zLevel = 0.0F;

			GlStateManager.disableDepth();
			GlStateManager.disableRescaleNormal();
			RenderHelper.disableStandardItemLighting();
		}
	}

	public void renderHoveredToolTip(GuiMerchant parent, int mouseX, int mouseY) {
		if (!this.visible || this.input == null || this.output == null) {
			return;
		}
		for (int i = 0; i < 4 && i < this.input.size(); i++) {
			int x = this.x + 4 + i * 18;
			int y = this.y + 2;
			if (mouseX >= x && mouseX <= x + 16 && mouseY >= y && mouseY <= y + 16) {
				parent.renderToolTip(this.input.get(i).getStack(), mouseX, mouseY);
			}
		}
		int x = this.x + 96;
		int y = this.y + 2;
		if (mouseX >= x && mouseX <= x + 16 && mouseY >= y && mouseY <= y + 16) {
			parent.renderToolTip(this.output, mouseX, mouseY);
		}
		x = this.x + 78;
		if (mouseX >= x && mouseX <= x + 16 && mouseY >= y && mouseY <= y + 16) {
			if (this.lockedForPlayer) {
				parent.drawHoveringText(I18n.format("description.gui_button_trade.locked"), mouseX, mouseY);
			} else if (this.outOfStock) {
				parent.drawHoveringText(I18n.format("description.gui_button_trade.out_of_stock"), mouseX, mouseY);
			}
		}
	}

}
