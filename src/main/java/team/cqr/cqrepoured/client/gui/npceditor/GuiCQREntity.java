package team.cqr.cqrepoured.client.gui.npceditor;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiSlider;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;
import net.minecraftforge.fml.client.gui.widget.Slider;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.network.client.packet.CPacketOpenMerchantGui;
import team.cqr.cqrepoured.network.client.packet.CPacketSyncEntity;

import java.io.IOException;

@OnlyIn(Dist.CLIENT)
public class GuiCQREntity extends ContainerScreen {

	private static final ResourceLocation BG_TEXTURE = new ResourceLocation(CQRMain.MODID, "textures/gui/container/gui_cqr_entity.png");

	private AbstractEntityCQR entity;

	private ExtendedButton openTradeGUIButton;
	private Slider sliderHealthScaling;
	private Slider sliderDropChanceHelm;
	private Slider sliderDropChanceChest;
	private Slider sliderDropChanceLegs;
	private Slider sliderDropChanceFeet;
	private Slider sliderDropChanceMainhand;
	private Slider sliderDropChanceOffhand;
	private Slider sliderSizeScaling;

	public GuiCQREntity(Container inventorySlotsIn, AbstractEntityCQR entity) {
		super(inventorySlotsIn);
		this.entity = entity;
	}

	@Override
	public void init() {
		super.init();
		// W := 107 -> steps are 10% steps
		this.sliderHealthScaling = new Slider(0, 5, 5, 107, 16, "Health Scale ", " %", 10, 1000, this.entity.getHealthScale() * 100.0D, false, true);
		this.sliderDropChanceHelm = new Slider(1, 5, 25, 108, 16, "Drop helm ", " %", 0, 100, this.entity.getDropChance(EquipmentSlotType.HEAD) * 100.0D, false, true);
		this.sliderDropChanceChest = new Slider(2, 5, 45, 108, 16, "Drop chest ", " %", 0, 100, this.entity.getDropChance(EquipmentSlotType.CHEST) * 100.0D, false, true);
		this.sliderDropChanceLegs = new Slider(3, 5, 65, 108, 16, "Drop legs ", " %", 0, 100, this.entity.getDropChance(EquipmentSlotType.LEGS) * 100.0D, false, true);
		this.sliderDropChanceFeet = new Slider(4, 5, 85, 108, 16, "Drop feet ", " %", 0, 100, this.entity.getDropChance(EquipmentSlotType.FEET) * 100.0D, false, true);
		this.sliderDropChanceMainhand = new Slider(5, 5, 105, 108, 16, "Drop mainhand ", " %", 0, 100, this.entity.getDropChance(EquipmentSlotType.MAINHAND) * 100.0D, false, true);
		this.sliderDropChanceOffhand = new Slider(6, 5, 125, 108, 16, "Drop offhand ", " %", 0, 100, this.entity.getDropChance(EquipmentSlotType.OFFHAND) * 100.0D, false, true);
		this.sliderSizeScaling = new Slider(7, 5, 145, 107, 16, "Size Scale ", " %", 5, 500, this.entity.getSizeVariation() * 100.0D, false, true);
		this.openTradeGUIButton = new ExtendedButton(8, 5 + this.sliderHealthScaling.getWidth() + 40, 25, 54, "Trades", );
		this.addButton(this.sliderHealthScaling);
		this.addButton(this.sliderDropChanceHelm);
		this.addButton(this.sliderDropChanceChest);
		this.addButton(this.sliderDropChanceLegs);
		this.addButton(this.sliderDropChanceFeet);
		this.addButton(this.sliderDropChanceMainhand);
		this.addButton(this.sliderDropChanceOffhand);
		this.addButton(this.openTradeGUIButton);
		if (!Minecraft.getInstance().player.isCreative()) {
			this.sliderHealthScaling.active = false;
			this.sliderHealthScaling.visible = false;

			this.sliderDropChanceHelm.active = false;
			this.sliderDropChanceHelm.visible = false;

			this.sliderDropChanceChest.active = false;
			this.sliderDropChanceChest.visible = false;

			this.sliderDropChanceLegs.active = false;
			this.sliderDropChanceLegs.visible = false;

			this.sliderDropChanceFeet.active = false;
			this.sliderDropChanceFeet.visible = false;

			this.sliderDropChanceMainhand.active = false;
			this.sliderDropChanceMainhand.visible = false;

			this.sliderDropChanceOffhand.active = false;
			this.sliderDropChanceOffhand.visible = false;

			this.sliderSizeScaling.active = false;
			this.sliderSizeScaling.visible = false;
		}
		this.addButton(this.sliderSizeScaling);
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderComponentHoverEffect( matrixStack, null ,mouseX, mouseY);
	}

	protected void renderBG(float partialTicks, int mouseX, int mouseY) {
		this.drawGradientRect(0, 0, this.width, this.height, 0xC010_1010, 0xD010_1010);
		GlStateManager.color(1, 1, 1, 1);
		this.mc.getTextureManager().bindTexture(BG_TEXTURE);
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
		this.drawEntity(this.guiLeft + 225, this.guiTop + 100, 30, mouseX, mouseY);
	}

	protected void drawEntity(int x, int y, int scale, float mouseX, float mouseY) {
		InventoryScreen.renderEntityInInventory(x, y, scale, x - mouseX, y - scale * this.entity.getEyeHeight() - mouseY, this.entity);
	}

	@Override
	public void onClose() {
		super.onClose();
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

		CQRMain.NETWORK.sendToServer(new CPacketSyncEntity(this.entity.getId(), healthScaling, dropChanceHelm, dropChanceChest, dropChanceLegs, dropChanceFeet, dropChanceMainhand, dropChanceOffhand, sizeScaling));
	}

	@Override
	public boolean isPauseScreen() {
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
