package com.teamcqr.chocolatequestrepoured.gui;

import com.teamcqr.chocolatequestrepoured.gui.container.ContainerBadge;
import com.teamcqr.chocolatequestrepoured.gui.container.ContainerSpawner;
import com.teamcqr.chocolatequestrepoured.tileentity.TileEntitySpawner;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiBadge extends GuiContainer
{
	private static final ResourceLocation GUI_BADGE = new ResourceLocation("textures/gui/container/dispenser.png");
	private final InventoryPlayer playerInventory;
	
	public GuiBadge(InventoryPlayer playerInventory, IInventory inventory) 
	{
		super(new ContainerBadge(playerInventory, inventory));
		this.playerInventory = playerInventory;
		
		this.xSize = 176;
		this.ySize = 166;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) 
	{
		String s = I18n.format("gui.drops.name");
        this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
        this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		this.mc.getTextureManager().bindTexture(GUI_BADGE);
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
	}
}
