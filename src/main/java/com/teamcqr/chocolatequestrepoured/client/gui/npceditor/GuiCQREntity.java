package com.teamcqr.chocolatequestrepoured.client.gui.npceditor;

import java.io.IOException;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.network.client.packet.CPacketSyncEntity;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiSlider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiCQREntity extends GuiContainer {

	private static final ResourceLocation BG_TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/container/gui_cqr_entity.png");

	private AbstractEntityCQR entity;

	private GuiButtonExt openTradeGUIButton;

	private GuiSlider sliderHealthScaling;
	private GuiSlider sliderDropChanceHelm;
	private GuiSlider sliderDropChanceChest;
	private GuiSlider sliderDropChanceLegs;
	private GuiSlider sliderDropChanceFeet;
	private GuiSlider sliderDropChanceMainhand;
	private GuiSlider sliderDropChanceOffhand;
	private GuiSlider sliderSizeScaling;

	public GuiCQREntity(Container inventorySlotsIn, AbstractEntityCQR entity) {
		super(inventorySlotsIn);
		this.entity = entity;
	}

	@Override
	public void initGui() {
		super.initGui();
		// W := 107 -> steps are 10% steps
		this.sliderHealthScaling = new GuiSlider(0, 5, 5, 108, 16, "Health Scale ", " %", 10, 1000, this.entity.getHealthScale() * 100.0D, false, true);
		this.sliderDropChanceHelm = new GuiSlider(1, 5, 25, 108, 16, "Drop helm ", " %", 0, 100, this.entity.getDropChance(EntityEquipmentSlot.HEAD) * 100.0D, false, true);
		this.sliderDropChanceChest = new GuiSlider(2, 5, 45, 108, 16, "Drop chest ", " %", 0, 100, this.entity.getDropChance(EntityEquipmentSlot.CHEST) * 100.0D, false, true);
		this.sliderDropChanceLegs = new GuiSlider(3, 5, 65, 108, 16, "Drop legs ", " %", 0, 100, this.entity.getDropChance(EntityEquipmentSlot.LEGS) * 100.0D, false, true);
		this.sliderDropChanceFeet = new GuiSlider(4, 5, 85, 108, 16, "Drop feet ", " %", 0, 100, this.entity.getDropChance(EntityEquipmentSlot.FEET) * 100.0D, false, true);
		this.sliderDropChanceMainhand = new GuiSlider(5, 5, 105, 108, 16, "Drop mainhand ", " %", 0, 100, this.entity.getDropChance(EntityEquipmentSlot.MAINHAND) * 100.0D, false, true);
		this.sliderDropChanceOffhand = new GuiSlider(6, 5, 125, 108, 16, "Drop offhand ", " %", 0, 100, this.entity.getDropChance(EntityEquipmentSlot.OFFHAND) * 100.0D, false, true);
		this.sliderSizeScaling = new GuiSlider(7, 5, 145, 108, 16, "Size Scale ", " %", 5, 500, this.entity.getSizeVariation() * 100.0D, false, true);
		this.openTradeGUIButton = new GuiButtonExt(8, 5 + this.sliderHealthScaling.width + 40, 25, 54, 16, "Trades");
		this.buttonList.add(this.sliderHealthScaling);
		this.buttonList.add(this.sliderDropChanceHelm);
		this.buttonList.add(this.sliderDropChanceChest);
		this.buttonList.add(this.sliderDropChanceLegs);
		this.buttonList.add(this.sliderDropChanceFeet);
		this.buttonList.add(this.sliderDropChanceMainhand);
		this.buttonList.add(this.sliderDropChanceOffhand);
		this.buttonList.add(this.openTradeGUIButton);
		if (!this.mc.player.isCreative()) {
			this.sliderHealthScaling.enabled = false;
			this.sliderHealthScaling.visible = false;

			this.sliderDropChanceHelm.enabled = false;
			this.sliderDropChanceHelm.visible = false;

			this.sliderDropChanceChest.enabled = false;
			this.sliderDropChanceChest.visible = false;

			this.sliderDropChanceLegs.enabled = false;
			this.sliderDropChanceLegs.visible = false;

			this.sliderDropChanceFeet.enabled = false;
			this.sliderDropChanceFeet.visible = false;

			this.sliderDropChanceMainhand.enabled = false;
			this.sliderDropChanceMainhand.visible = false;

			this.sliderDropChanceOffhand.enabled = false;
			this.sliderDropChanceOffhand.visible = false;

			this.sliderSizeScaling.enabled = false;
			this.sliderSizeScaling.visible = false;
		}
		this.buttonList.add(this.sliderSizeScaling);
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
		this.mc.getTextureManager().bindTexture(BG_TEXTURE);
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
		this.drawEntity(this.guiLeft + 225, this.guiTop + 100, 30, mouseX, mouseY);
	}

	protected void drawEntity(int x, int y, int scale, float mouseX, float mouseY) {
		GuiInventory.drawEntityOnScreen(x, y, scale, (float) x - mouseX, (float) y - (float) scale * this.entity.getEyeHeight() - mouseY, this.entity);
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		int healthScaling = this.sliderHealthScaling.getValueInt();
		int dropChanceHelm = this.sliderDropChanceHelm.getValueInt();
		int dropChanceChest = this.sliderDropChanceChest.getValueInt();
		int dropChanceLegs = this.sliderDropChanceLegs.getValueInt();
		int dropChanceFeet = this.sliderDropChanceFeet.getValueInt();
		int dropChanceMainhand = this.sliderDropChanceMainhand.getValueInt();
		int dropChanceOffhand = this.sliderDropChanceOffhand.getValueInt();
		int sizeScaling = this.sliderSizeScaling.getValueInt();

		this.entity.setHealthScale((double) healthScaling / 100.0D);
		this.entity.setDropChance(EntityEquipmentSlot.HEAD, (float) dropChanceHelm / 100.0F);
		this.entity.setDropChance(EntityEquipmentSlot.CHEST, (float) dropChanceChest / 100.0F);
		this.entity.setDropChance(EntityEquipmentSlot.LEGS, (float) dropChanceLegs / 100.0F);
		this.entity.setDropChance(EntityEquipmentSlot.FEET, (float) dropChanceFeet / 100.0F);
		this.entity.setDropChance(EntityEquipmentSlot.MAINHAND, (float) dropChanceMainhand / 100.0F);
		this.entity.setDropChance(EntityEquipmentSlot.OFFHAND, (float) dropChanceOffhand / 100.0F);
		this.entity.setSizeVariation((float) sizeScaling / 100.0F);

		CQRMain.NETWORK.sendToServer(new CPacketSyncEntity(this.entity.getEntityId(), healthScaling, dropChanceHelm, dropChanceChest, dropChanceLegs, dropChanceFeet, dropChanceMainhand, dropChanceOffhand, sizeScaling));
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		EntityPlayerSP player = Minecraft.getMinecraft().player;
		if (button == this.openTradeGUIButton && (player.isCreative() || (this.entity.getTrades() != null && !this.entity.getTrades().isEmpty() && !this.entity.getFaction().isEnemy(player)))) {
			player.openGui(CQRMain.INSTANCE, Reference.MERCHANT_GUI_ID, this.entity.world, this.entity.getEntityId(), 0, 0);
		}
	}

}
