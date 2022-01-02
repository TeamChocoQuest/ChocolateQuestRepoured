package team.cqr.cqrepoured.client.gui.npceditor;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiSlider;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.network.client.packet.CPacketOpenMerchantGui;
import team.cqr.cqrepoured.network.client.packet.CPacketSyncEntity;

@OnlyIn(Dist.CLIENT)
public class GuiCQREntity extends ContainerScreen {

	private static final ResourceLocation BG_TEXTURE = new ResourceLocation(CQRMain.MODID, "textures/gui/container/gui_cqr_entity.png");

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
		this.sliderHealthScaling = new GuiSlider(0, 5, 5, 107, 16, "Health Scale ", " %", 10, 1000, this.entity.getHealthScale() * 100.0D, false, true);
		this.sliderDropChanceHelm = new GuiSlider(1, 5, 25, 108, 16, "Drop helm ", " %", 0, 100, this.entity.getDropChance(EquipmentSlotType.HEAD) * 100.0D, false, true);
		this.sliderDropChanceChest = new GuiSlider(2, 5, 45, 108, 16, "Drop chest ", " %", 0, 100, this.entity.getDropChance(EquipmentSlotType.CHEST) * 100.0D, false, true);
		this.sliderDropChanceLegs = new GuiSlider(3, 5, 65, 108, 16, "Drop legs ", " %", 0, 100, this.entity.getDropChance(EquipmentSlotType.LEGS) * 100.0D, false, true);
		this.sliderDropChanceFeet = new GuiSlider(4, 5, 85, 108, 16, "Drop feet ", " %", 0, 100, this.entity.getDropChance(EquipmentSlotType.FEET) * 100.0D, false, true);
		this.sliderDropChanceMainhand = new GuiSlider(5, 5, 105, 108, 16, "Drop mainhand ", " %", 0, 100, this.entity.getDropChance(EquipmentSlotType.MAINHAND) * 100.0D, false, true);
		this.sliderDropChanceOffhand = new GuiSlider(6, 5, 125, 108, 16, "Drop offhand ", " %", 0, 100, this.entity.getDropChance(EquipmentSlotType.OFFHAND) * 100.0D, false, true);
		this.sliderSizeScaling = new GuiSlider(7, 5, 145, 107, 16, "Size Scale ", " %", 5, 500, this.entity.getSizeVariation() * 100.0D, false, true);
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
		this.drawGradientRect(0, 0, this.width, this.height, 0xC010_1010, 0xD010_1010);
		GlStateManager.color(1, 1, 1, 1);
		this.mc.getTextureManager().bindTexture(BG_TEXTURE);
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
		this.drawEntity(this.guiLeft + 225, this.guiTop + 100, 30, mouseX, mouseY);
	}

	protected void drawEntity(int x, int y, int scale, float mouseX, float mouseY) {
		InventoryScreen.drawEntityOnScreen(x, y, scale, x - mouseX, y - scale * this.entity.getEyeHeight() - mouseY, this.entity);
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

		this.entity.setHealthScale(healthScaling / 100.0D);
		this.entity.setDropChance(EquipmentSlotType.HEAD, dropChanceHelm / 100.0F);
		this.entity.setDropChance(EquipmentSlotType.CHEST, dropChanceChest / 100.0F);
		this.entity.setDropChance(EquipmentSlotType.LEGS, dropChanceLegs / 100.0F);
		this.entity.setDropChance(EquipmentSlotType.FEET, dropChanceFeet / 100.0F);
		this.entity.setDropChance(EquipmentSlotType.MAINHAND, dropChanceMainhand / 100.0F);
		this.entity.setDropChance(EquipmentSlotType.OFFHAND, dropChanceOffhand / 100.0F);
		this.entity.setSizeVariation(sizeScaling / 100.0F);

		CQRMain.NETWORK.sendToServer(new CPacketSyncEntity(this.entity.getEntityId(), healthScaling, dropChanceHelm, dropChanceChest, dropChanceLegs, dropChanceFeet, dropChanceMainhand, dropChanceOffhand, sizeScaling));
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	protected void actionPerformed(Button button) throws IOException {
		super.actionPerformed(button);
		ClientPlayerEntity player = Minecraft.getMinecraft().player;
		if (button == this.openTradeGUIButton && (player.isCreative() || (!this.entity.getTrades().isEmpty() && !this.entity.getFaction().isEnemy(player)))) {
			CQRMain.NETWORK.sendToServer(new CPacketOpenMerchantGui(this.entity.getEntityId()));
		}
	}

}
