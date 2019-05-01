package com.teamcqr.chocolatequestrepoured.gui;

import com.teamcqr.chocolatequestrepoured.gui.container.ContainerAlchemyBag;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiAlchemyBag extends GuiContainer
{
	private static final ResourceLocation GUI_ALCHEMY_BAG = new ResourceLocation("textures/gui/container/hopper.png");
	private final InventoryPlayer playerInventory;
	private final IInventory inventory;
	
	public GuiAlchemyBag(InventoryPlayer playerInventory, IInventory inventory) 
	{
		super(new ContainerAlchemyBag(playerInventory, inventory));
		this.playerInventory = playerInventory;
		this.inventory = inventory;
		
		this.ySize = 133;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) 
	{
		this.fontRenderer.drawString(this.inventory.getDisplayName().getUnformattedText(), 8, 6, 4210752);
        this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 3, 4210752);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		this.mc.getTextureManager().bindTexture(GUI_ALCHEMY_BAG);
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
	}
}