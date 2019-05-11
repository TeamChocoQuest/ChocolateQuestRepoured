package com.teamcqr.chocolatequestrepoured.gui;

import com.teamcqr.chocolatequestrepoured.gui.container.ContainerBackpack;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiBackpack extends GuiContainer
{
	private static final ResourceLocation GUI_BACKPACK = new ResourceLocation("textures/gui/container/shulker_box.png");
	private final InventoryPlayer playerInventory;
	private final IInventory inventory;
	
	public GuiBackpack(InventoryPlayer playerInventory, IInventory inventory) 
	{
		super(new ContainerBackpack(playerInventory, inventory));
		this.playerInventory = playerInventory;
		this.inventory = inventory;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) 
	{
		this.fontRenderer.drawString(this.inventory.getDisplayName().getUnformattedText(), 8, 6, 4210752);
        this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 3, 4210752);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		this.mc.getTextureManager().bindTexture(GUI_BACKPACK);
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
	}
}