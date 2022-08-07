package team.cqr.cqrepoured.client.gui.npceditor;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;
import net.minecraftforge.fml.client.gui.widget.Slider;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.inventory.ContainerCQREntity;
import team.cqr.cqrepoured.network.client.packet.CPacketOpenMerchantGui;
import team.cqr.cqrepoured.network.client.packet.CPacketSyncEntity;

@OnlyIn(Dist.CLIENT)
public class GuiCQREntity extends ContainerScreen<ContainerCQREntity> {

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

	public GuiCQREntity(ContainerCQREntity inventorySlotsIn, PlayerInventory pPlayerInventory, ITextComponent pTitle) {
		super(inventorySlotsIn, pPlayerInventory, pTitle);
		this.entity = inventorySlotsIn.getEntity();
	}

	@Override
	public void init() {
		super.init();
		// W := 107 -> steps are 10% steps
		// NEW: x, y, w, h
		// OLD: id, x, y, w, h
		this.sliderHealthScaling = new Slider(5, 5, 107, 16, new StringTextComponent("Health Scale "), new StringTextComponent(" %"), 10,  1000, this.entity.getHealthScale() * 100.0D, false, true, (x) -> {});
		this.sliderDropChanceHelm = new Slider(5, 25, 108, 16,  new StringTextComponent("Drop helm "), new StringTextComponent(" %"), 0, 100, this.entity.getDropChance(EquipmentSlotType.HEAD) * 100.0D, false, true, (x) -> {});
		this.sliderDropChanceChest = new Slider(5, 45, 108, 16,  new StringTextComponent("Drop chest "), new StringTextComponent(" %"), 0,  100, this.entity.getDropChance(EquipmentSlotType.CHEST) * 100.0D, false, true, (x) -> {});
		this.sliderDropChanceLegs = new Slider(5, 65, 108, 16,  new StringTextComponent("Drop legs "), new StringTextComponent(" %"), 0, 100, this.entity.getDropChance(EquipmentSlotType.LEGS) * 100.0D, false, true, (x) -> {});
		this.sliderDropChanceFeet = new Slider(5, 85, 108, 16,  new StringTextComponent("Drop feet "), new StringTextComponent(" %"), 0, 100, this.entity.getDropChance(EquipmentSlotType.FEET) * 100.0D, false, true, (x) -> {});
		this.sliderDropChanceMainhand = new Slider(5, 105, 108, 16,  new StringTextComponent("Drop mainhand "), new StringTextComponent(" %"), 0, 100, this.entity.getDropChance(EquipmentSlotType.MAINHAND) * 100.0D, false, true, (x) -> {});
		this.sliderDropChanceOffhand = new Slider(5, 125, 108, 16,  new StringTextComponent("Drop offhand "), new StringTextComponent(" %"), 0, 100,  this.entity.getDropChance(EquipmentSlotType.OFFHAND) * 100.0D, false, true, (x) -> {});
		this.sliderSizeScaling = new Slider(5, 145, 107, 16,  new StringTextComponent("Size Scale "), new StringTextComponent(" %"), 5, 500, this.entity.getSizeVariation() * 100.0D, false, true, (x) -> {});
		this.openTradeGUIButton = new ExtendedButton(5 + this.sliderHealthScaling.getWidth() + 40, 25, 54, 16, new StringTextComponent("Trades"), this::onPress);
		this.addButton(this.sliderHealthScaling);
		this.addButton(this.sliderDropChanceHelm);
		this.addButton(this.sliderDropChanceChest);
		this.addButton(this.sliderDropChanceLegs);
		this.addButton(this.sliderDropChanceFeet);
		this.addButton(this.sliderDropChanceMainhand);
		this.addButton(this.sliderDropChanceOffhand);
		this.addButton(this.openTradeGUIButton);
		if (!this.getMinecraft().player.isCreative()) {
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

	protected void renderBg(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
		this.fillGradient(matrixStack, 0, 0, this.width, this.height, 0xC010_1010, 0xD010_1010);
		GlStateManager._color4f(1, 1, 1, 1);
		this.getMinecraft().getTextureManager().bind(BG_TEXTURE);
		this.blit(matrixStack, this.getGuiLeft(), this.getGuiTop(), 0, 0, this.getXSize(), this.getYSize());
		this.drawEntity(this.getGuiLeft() + 225, this.getGuiTop() + 100, 30, mouseX, mouseY);
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

	public void onPress() {

	}

	protected void onPress(Button button) {
		ClientPlayerEntity player = this.getMinecraft().player;
		if (button == this.openTradeGUIButton && (player.isCreative() || (!this.entity.getTrades().isEmpty() && !this.entity.getFaction().isEnemy(player)))) {
			CQRMain.NETWORK.sendToServer(new CPacketOpenMerchantGui(this.entity.getId()));
		}
	}

}
