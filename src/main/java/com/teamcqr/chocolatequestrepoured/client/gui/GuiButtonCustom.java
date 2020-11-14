package com.teamcqr.chocolatequestrepoured.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;

public class GuiButtonCustom extends GuiButton {

	public GuiButtonCustom(int buttonId, int x, int y, int width, int height, String buttonText) {
		super(buttonId, x, y, MathHelper.clamp(width, 4, 500), MathHelper.clamp(height, 4, 80), buttonText);
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		if (this.visible) {
			FontRenderer fontrenderer = mc.fontRenderer;
			mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
			int i = this.getHoverState(this.hovered);
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			int width1 = this.width / 2;
			int width2 = this.width - width1;
			int height1 = this.height / 2;
			int height2 = this.height - height1;
			this.drawTexturedModalRect(this.x, this.y, 0, 46 + i * 20, width1, height1);
			this.drawTexturedModalRect(this.x + width1, this.y, 200 - width2, 46 + i * 20, width2, height1);
			this.drawTexturedModalRect(this.x, this.y + height1, 0, 46 + i * 20 + 20 - height1, width1, height2);
			this.drawTexturedModalRect(this.x + width1, this.y + height1, 200 - width2, 46 + i * 20 + 20 - height1, width2, height2);
			this.mouseDragged(mc, mouseX, mouseY);
			int j = 14737632;

			if (packedFGColour != 0) {
				j = packedFGColour;
			} else if (!this.enabled) {
				j = 10526880;
			} else if (this.hovered) {
				j = 16777120;
			}

			this.drawCenteredString(fontrenderer, this.displayString, this.x + this.width / 2, this.y + (this.height - 8) / 2, j);
		}
	}

}
