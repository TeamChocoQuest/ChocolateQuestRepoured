package team.cqr.cqrepoured.client.gui.npceditor;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.gui.GuiButtonTextured;
import team.cqr.cqrepoured.client.gui.INumericIDButton;
import team.cqr.cqrepoured.client.gui.IUpdatableGui;
import team.cqr.cqrepoured.client.util.GuiHelper;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.entity.trade.Trade;
import team.cqr.cqrepoured.entity.trade.TraderOffer;
import team.cqr.cqrepoured.inventory.ContainerMerchant;
import team.cqr.cqrepoured.network.client.packet.CPacketContainerClickButton;

@OnlyIn(Dist.CLIENT)
public class GuiMerchant extends ContainerScreen<ContainerMerchant> implements IUpdatableGui {

	private static final ResourceLocation BG_TEXTURE = new ResourceLocation(CQRMain.MODID, "textures/gui/container/gui_merchant.png");
	private static final ResourceLocation BG_TEXTURE_CREATIVE = new ResourceLocation(CQRMain.MODID, "textures/gui/container/gui_merchant_creative.png");

	private final AbstractEntityCQR entity;
	private final TraderOffer trades;
	private final GuiButtonTrade[] tradeButtons = new GuiButtonTrade[7];
	private final Button[] pushUpButtons = new Button[this.tradeButtons.length - 1];
	private final Button[] pushDownButtons = new Button[this.tradeButtons.length - 1];
	private final Button[] deleteButtons = new Button[this.tradeButtons.length - 1];
	private final Button[] editButtons = new Button[this.tradeButtons.length - 1];
	private Button addNewTradeButton;
	private int buttonStartIndex = 0;
	private boolean scrollBarClicked;

	public GuiMerchant(ContainerMerchant container, PlayerInventory pPlayerInventory, ITextComponent pTitle) {
		super(container, pPlayerInventory, pTitle);
		this.entity = container.getMerchant();
		this.trades = entity.getTrades();
		this.imageWidth = 307;
		this.imageHeight = 166;
	}
	
	@Override
	protected void init() {
		super.init();

		for (int i = 0; i < this.tradeButtons.length; i++) {
			this.tradeButtons[i] = this.addButton(new GuiButtonTrade(this, 10 + i, this.leftPos + 8, this.topPos + 18 + i * 20, i));
			if (i < this.tradeButtons.length - 1) {
				this.pushUpButtons[i] = this.addButton(new GuiButtonTextured(20 + i, this.leftPos - 12, this.topPos + 18 + i * 20, 10, 10, 0, 0, 0,	CQRMain.prefix("textures/gui/container/gui_button_10px.png"), CQRMain.prefix("textures/gui/container/icon_up.png"), this::actionPerformed, new StringTextComponent("")));
				this.pushDownButtons[i] = this.addButton(new GuiButtonTextured(30 + i, this.leftPos - 12, this.topPos + 28 + i * 20, 10, 10, 0, 0, 0, CQRMain.prefix("textures/gui/container/gui_button_10px.png"), CQRMain.prefix("textures/gui/container/icon_down.png"), this::actionPerformed, new StringTextComponent("")));
				this.deleteButtons[i] = this.addButton(new GuiButtonTextured(40 + i, this.leftPos - 2, this.topPos + 18 + i * 20, 10, 10, 0, 0, 0, CQRMain.prefix("textures/gui/container/gui_button_10px.png"), CQRMain.prefix("textures/gui/container/icon_delete.png"), this::actionPerformed, new StringTextComponent("")));
				this.editButtons[i] = this.addButton(new GuiButtonTextured(50 + i, this.leftPos - 2, this.topPos + 28 + i * 20, 10, 10, 0, 0, 0, CQRMain.prefix("textures/gui/container/gui_button_10px.png"), CQRMain.prefix("textures/gui/container/icon_edit.png"), this::actionPerformed, new StringTextComponent("")));
			}
		}
		this.addNewTradeButton = this.addButton(new Button(/*0, */this.leftPos - 12, this.topPos + 138, 136, 20, new TranslationTextComponent("- Create Trade -"), this::actionPerformed));

		this.update();
	}

	@Override
	public void render(MatrixStack pMatrixStack, int mouseX, int mouseY, float partialTicks) {
		super.render(pMatrixStack,mouseX, mouseY, partialTicks);
		this.renderTooltip(pMatrixStack, mouseX, mouseY);
		for (GuiButtonTrade tradeButton : this.tradeButtons) {
			tradeButton.renderHoveredToolTip(this, pMatrixStack, mouseX, mouseY);
		}
	}

	@Override
	protected void renderBg(MatrixStack pMatrixStack, float partialTicks, int mouseX, int mouseY) {

		this.renderBackground(pMatrixStack);

		if (this.getMinecraft().player.isCreative()) {
			this.getMinecraft().getTextureManager().bind(BG_TEXTURE_CREATIVE);
			GuiHelper.drawTexture(this.leftPos - 20.0D, this.topPos, 0.0D, 0.0D, this.imageWidth + 20.0D, this.imageHeight, (this.imageWidth + 20) / 512.0D, this.imageHeight / 256.0D);
		} else {
			this.getMinecraft().getTextureManager().bind(BG_TEXTURE);
			GuiHelper.drawTexture(this.leftPos, this.topPos, 0.0D, 0.0D, this.imageWidth, this.imageHeight, this.imageWidth / 512.0D, this.imageHeight / 256.0D);
		}

		if (this.trades.size() > this.tradeButtons.length - (this.getMinecraft().player.isCreative() ? 1 : 0)) {
			int scrollOffsetY = (int) ((double) this.buttonStartIndex / (double) (this.trades.size() - (this.tradeButtons.length - (this.getMinecraft().player.isCreative() ? 1 : 0))) * 113.0D);
			GuiHelper.drawTexture(this.leftPos + 125.0D, this.topPos + 18.0D + scrollOffsetY, 0.0D, 166.0D / 256.0D, 6, 27, 6.0D / 512.0D, 27.0D / 256.0D);
		} else {
			GuiHelper.drawTexture(this.leftPos + 125.0D, this.topPos + 18.0D, 6.0D / 512.0D, 166.0D / 256.0D, 6, 27, 6.0D / 512.0D, 27.0D / 256.0D);
		}

		if (this.getMinecraft().player.isCreative()) {
			this.font.draw(pMatrixStack, this.entity.getDisplayName(), this.leftPos - 13, this.topPos + 7, 0x404040);
		} else {
			this.font.draw(pMatrixStack, this.entity.getDisplayName(), this.leftPos + 7, this.topPos + 7, 0x404040);
		}
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}
	
	protected void actionPerformed(Button button) {
		if (button instanceof GuiButtonTrade) {
			((ContainerMerchant) this.menu).setCurrentTradeIndex(((GuiButtonTrade) button).getIndex());
			((ContainerMerchant) this.menu).updateInputsForTrade(((GuiButtonTrade) button).getIndex());
		}
		if (button == addNewTradeButton) {
				CQRMain.NETWORK.sendToServer(new CPacketContainerClickButton(0 /* new Trade button */));
		} else if (button instanceof INumericIDButton) {
			INumericIDButton inib = (INumericIDButton) button;
			int index = this.buttonStartIndex + (inib.getId() % 10);
			CQRMain.NETWORK.sendToServer(new CPacketContainerClickButton(inib.getId(), buf -> buf.writeInt(index)));
		}
	}

	@Override
	public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
		if (pDelta != 0.0D) {
			int scrollAmount = (int) (pDelta / 60.0D);
			if (this.trades.size() > this.tradeButtons.length - (this.getMinecraft().player.isCreative() ? 1 : 0)) {
				this.buttonStartIndex = MathHelper.clamp(this.buttonStartIndex - scrollAmount, 0, this.trades.size() - (this.tradeButtons.length - (this.getMinecraft().player.isCreative() ? 1 : 0)));
				this.update();
			}
		}
		return true;
	}
	
	@Override
	public boolean mouseDragged(double pMouseX, double mouseY, int pButton, double pDragX, double pDragY) {
		if (this.scrollBarClicked) {
			int y1 = this.topPos + 18;
			int y2 = y1 + 139;
			int scrollLength = this.trades.size() - (this.tradeButtons.length - (this.getMinecraft().player.isCreative() ? 1 : 0));
			float f = ((float) mouseY - (float) y1 - 13.5F) / (y2 - y1 - 27.0F);
			f = f * scrollLength + 0.5F;
			this.buttonStartIndex = MathHelper.clamp((int) f, 0, scrollLength);
			this.update();
			return true;
		} else {
			return super.mouseDragged(pMouseX, mouseY, pButton, pDragX, pDragY);
		}
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		super.mouseClicked(mouseX, mouseY, mouseButton);

		this.scrollBarClicked = false;
		if (this.trades.size() > this.tradeButtons.length - (this.getMinecraft().player.isCreative() ? 1 : 0) && mouseButton == 0) {
			int x1 = this.leftPos + 125;
			int y1 = this.topPos + 18;
			int x2 = x1 + 5;
			int y2 = y1 + 139;
			if (x1 <= mouseX && mouseX <= x2 && y1 <= mouseY && mouseY <= y2) {
				this.scrollBarClicked = true;
			}
		}
		//Super class always returns true
		return true;
	}

	@Override
	public void update() {
		if (this.trades.size() > this.tradeButtons.length - (this.getMinecraft().player.isCreative() ? 1 : 0)) {
			this.buttonStartIndex = MathHelper.clamp(this.buttonStartIndex, 0, this.trades.size() - (this.tradeButtons.length - (this.getMinecraft().player.isCreative() ? 1 : 0)));
		} else {
			this.buttonStartIndex = 0;
		}

		for (int i = 0; i < this.tradeButtons.length; i++) {
			Trade trade = this.trades.get(this.buttonStartIndex + i);

			this.tradeButtons[i].visible = trade != null && (i < this.tradeButtons.length - 1 || !this.getMinecraft().player.isCreative());
			if (trade != null) {
				this.tradeButtons[i].setIndex(this.buttonStartIndex + i);
				this.tradeButtons[i].setTrade(trade);
			}

			if (i < this.tradeButtons.length - 1) {
				this.pushUpButtons[i].visible = trade != null && this.getMinecraft().player.isCreative();
				this.pushDownButtons[i].visible = trade != null && this.getMinecraft().player.isCreative();
				this.deleteButtons[i].visible = trade != null && this.getMinecraft().player.isCreative();
				this.editButtons[i].visible = trade != null && this.getMinecraft().player.isCreative();
			}
		}

		this.addNewTradeButton.visible = this.getMinecraft().player.isCreative();
		this.addNewTradeButton.x = this.leftPos - 12;
		this.addNewTradeButton.y = this.topPos + (this.trades.size() < this.tradeButtons.length - 1 ? 18 + this.trades.size() * 20 : 138);
	}

	// Overriding to set access modifier to public
	@Override
	public void renderTooltip(MatrixStack pMatrixStack, ItemStack pItemStack, int pMouseX, int pMouseY) {
		super.renderTooltip(pMatrixStack, pItemStack, pMouseX, pMouseY);
	}
	

	@Override
	public int getGuiLeft() {
		return super.getGuiLeft() - (this.getMinecraft().player.isCreative() ? 20 : 0);
	}

	@Override
	public int getXSize() {
		return super.getXSize() + (this.getMinecraft().player.isCreative() ? 20 : 0);
	}

}
