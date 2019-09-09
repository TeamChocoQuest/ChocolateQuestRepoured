package com.teamcqr.chocolatequestrepoured.client.gui;

import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiCQREntity extends GuiContainer {

	private static final ResourceLocation BG_TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/container/gui_cqr_entity.png");

	private AbstractEntityCQR entity;

	public GuiCQREntity(Container inventorySlotsIn, AbstractEntityCQR entity) {
		super(inventorySlotsIn);
		this.entity = entity;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		this.drawGradientRect(0, 0, this.width, this.height, -1072689136, -804253680);
		GlStateManager.color(1, 1, 1, 1);
		mc.getTextureManager().bindTexture(BG_TEXTURE);
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, xSize, ySize);
		this.drawEntity(this.guiLeft - 50, this.guiTop + 100, 30, mouseX, mouseY);
	}

	protected void drawEntity(int x, int y, int scale, float mouseX, float mouseY) {
		GuiInventory.drawEntityOnScreen(x, y, scale, (float) x - mouseX, (float) y - (float) scale * this.entity.getEyeHeight() - mouseY, this.entity);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

}
