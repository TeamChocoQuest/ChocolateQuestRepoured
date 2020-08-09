package com.teamcqr.chocolatequestrepoured.client.gui;

import com.teamcqr.chocolatequestrepoured.objects.npc.trading.TradeInput;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class GuiButtonTrade extends GuiButton {

	private int index;
	private NonNullList<TradeInput> input;
	private boolean locked = false;
	private boolean outOfStock = false;
	private ItemStack output;
	private static final ItemStack OUT_OF_STOCK_ITEM_STACK = new ItemStack(Blocks.BARRIER);

	public GuiButtonTrade(int buttonId, int x, int y, int widthIn, int heightIn, int index) {
		super(buttonId, x, y, widthIn, heightIn, "");
		this.index = index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getIndex() {
		return this.index;
	}

	public void setInputAndOutput(NonNullList<TradeInput> input, ItemStack output) {
		this.input = input;
		this.output = output;
	}
	
	public void setOutOfStock(boolean value) {
		this.outOfStock = value;
	}
	
	public void setLocked(boolean value) {
		this.locked = value;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		super.drawButton(mc, mouseX, mouseY, partialTicks);

		if (this.visible) {
			RenderItem itemRender = mc.getRenderItem();
			RenderHelper.enableGUIStandardItemLighting();
			GlStateManager.enableDepth();
			itemRender.zLevel = 100.0F;
			int x = this.x + 4;
			int y = this.y + 2;
			for (int i = 0; i < 4 && i < this.input.size(); i++) {
				itemRender.renderItemAndEffectIntoGUI(mc.player, this.input.get(i).getStack(), x + i * 18, y);
				itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, this.input.get(i).getStack(), x + i * 18, y, null);
			}
			//DONE: Render a barrier icon between in and output when the trade is out of stock
			//TODO: Render a lock between in and output when the trade is not unlocked for the player
			if(this.outOfStock) {
				itemRender.renderItemAndEffectIntoGUI(mc.player, OUT_OF_STOCK_ITEM_STACK, x + 73, y);
			}
			else if(this.locked) {
				//TODO this.drawTexturedModalRect(82, y, net.minecraft.client.gui.GuiLockIconButton.Icon.LOCKED.getX(), net.minecraft.client.gui.GuiLockIconButton.Icon.LOCKED.getY(), 16, 16);
			}
			
			itemRender.renderItemAndEffectIntoGUI(mc.player, this.output, x + 92, y);
			itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, this.output, x + 92, y, null);
			itemRender.zLevel = 0.0F;
			RenderHelper.disableStandardItemLighting();
		}
	}

}
