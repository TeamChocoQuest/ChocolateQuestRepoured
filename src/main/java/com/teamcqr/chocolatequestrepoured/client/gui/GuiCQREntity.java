package com.teamcqr.chocolatequestrepoured.client.gui;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.network.SyncEntityPacket;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiSlider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiCQREntity extends GuiContainer {

	private static final ResourceLocation BG_TEXTURE = new ResourceLocation(Reference.MODID,
			"textures/gui/container/gui_cqr_entity.png");

	private AbstractEntityCQR entity;

	private GuiSlider sliderHealthScaling;
	private GuiSlider sliderDropChanceHelm;
	private GuiSlider sliderDropChanceChest;
	private GuiSlider sliderDropChanceLegs;
	private GuiSlider sliderDropChanceFeet;
	private GuiSlider sliderDropChanceMainhand;
	private GuiSlider sliderDropChanceOffhand;

	public GuiCQREntity(Container inventorySlotsIn, AbstractEntityCQR entity) {
		super(inventorySlotsIn);
		this.entity = entity;
	}

	@Override
	public void initGui() {
		super.initGui();
		this.sliderHealthScaling = new GuiSlider(0, 5, 5, 108, 16, "Health ", " %", 1, 200, this.entity.getHealthScale() * 100.0D, false, true);
		this.sliderDropChanceHelm = new GuiSlider(0, 5, 25, 108, 16, "Drop helm ", " %", 0, 100, this.entity.getDropChance(EntityEquipmentSlot.HEAD) * 100.0D, false, true);
		this.sliderDropChanceChest = new GuiSlider(0, 5, 45, 108, 16, "Drop chest ", " %", 0, 100, this.entity.getDropChance(EntityEquipmentSlot.CHEST) * 100.0D, false, true);
		this.sliderDropChanceLegs = new GuiSlider(0, 5, 65, 108, 16, "Drop legs ", " %", 0, 100, this.entity.getDropChance(EntityEquipmentSlot.LEGS) * 100.0D, false, true);
		this.sliderDropChanceFeet = new GuiSlider(0, 5, 85, 108, 16, "Drop feet ", " %", 0, 100, this.entity.getDropChance(EntityEquipmentSlot.FEET) * 100.0D, false, true);
		this.sliderDropChanceMainhand = new GuiSlider(0, 5, 105, 108, 16, "Drop mainhand ", " %", 0, 100, this.entity.getDropChance(EntityEquipmentSlot.MAINHAND) * 100.0D, false, true);
		this.sliderDropChanceOffhand = new GuiSlider(0, 5, 125, 108, 16, "Drop offhand ", " %", 0, 100, this.entity.getDropChance(EntityEquipmentSlot.OFFHAND) * 100.0D, false, true);
		this.buttonList.add(this.sliderHealthScaling);
		this.buttonList.add(this.sliderDropChanceHelm);
		this.buttonList.add(this.sliderDropChanceChest);
		this.buttonList.add(this.sliderDropChanceLegs);
		this.buttonList.add(this.sliderDropChanceFeet);
		this.buttonList.add(this.sliderDropChanceMainhand);
		this.buttonList.add(this.sliderDropChanceOffhand);
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
		this.drawEntity(this.guiLeft + 50, this.guiTop + 100, 30, mouseX, mouseY);
	}

	protected void drawEntity(int x, int y, int scale, float mouseX, float mouseY) {
		GuiInventory.drawEntityOnScreen(x, y, scale, (float) x - mouseX,
				(float) y - (float) scale * this.entity.getEyeHeight() - mouseY, this.entity);
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		System.out.println("UPDATE");
		int healthScaling = this.sliderHealthScaling.getValueInt();
		int dropChanceHelm = this.sliderDropChanceHelm.getValueInt();
		int dropChanceChest = this.sliderDropChanceChest.getValueInt();
		int dropChanceLegs = this.sliderDropChanceLegs.getValueInt();
		int dropChanceFeet = this.sliderDropChanceFeet.getValueInt();
		int dropChanceMainhand = this.sliderDropChanceMainhand.getValueInt();
		int dropChanceOffhand = this.sliderDropChanceOffhand.getValueInt();

		this.entity.setHealthScale((double) healthScaling / 100.0D);
		this.entity.setDropChance(EntityEquipmentSlot.HEAD, (float) dropChanceHelm / 100.0F);
		this.entity.setDropChance(EntityEquipmentSlot.CHEST, (float) dropChanceChest / 100.0F);
		this.entity.setDropChance(EntityEquipmentSlot.LEGS, (float) dropChanceLegs / 100.0F);
		this.entity.setDropChance(EntityEquipmentSlot.FEET, (float) dropChanceFeet / 100.0F);
		this.entity.setDropChance(EntityEquipmentSlot.MAINHAND, (float) dropChanceMainhand / 100.0F);
		this.entity.setDropChance(EntityEquipmentSlot.OFFHAND, (float) dropChanceOffhand / 100.0F);

		CQRMain.NETWORK.sendToServer(new SyncEntityPacket(this.entity.getEntityId(), healthScaling, dropChanceHelm,
				dropChanceChest, dropChanceLegs, dropChanceFeet, dropChanceMainhand, dropChanceOffhand));
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

}
