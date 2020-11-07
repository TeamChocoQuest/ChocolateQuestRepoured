package com.teamcqr.chocolatequestrepoured.client.gui;

import com.teamcqr.chocolatequestrepoured.client.util.GuiHelper;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiBossBlock extends GuiContainer {

	private static final ResourceLocation GUI_BOSS_BLOCK = new ResourceLocation(Reference.MODID, "textures/gui/container/gui_boss_block.png");
	private static final int GUI_WIDTH = 176;
	private static final int GUI_HEIGHT = 132;

	public GuiBossBlock(Container inventorySlotsIn) {
		super(inventorySlotsIn);
	}

	@Override
	public void initGui() {
		super.initGui();

		this.guiLeft = (this.width - GUI_WIDTH) / 2;
		this.guiTop = (this.height - GUI_HEIGHT) / 2;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		GuiHelper.drawString(this.fontRenderer, I18n.format("Boss Block"), this.xSize / 2, 7, 0x404040, true, false);
		GuiHelper.drawString(this.fontRenderer, I18n.format("container.inventory"), 8, 39, 0x404040, false, false);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(GUI_BOSS_BLOCK);
		GuiHelper.drawTexture(this.guiLeft, this.guiTop, 0.0D, 0.0D, GUI_WIDTH, GUI_HEIGHT, GUI_WIDTH / 256.0D, GUI_HEIGHT / 256.0D);
	}

}
