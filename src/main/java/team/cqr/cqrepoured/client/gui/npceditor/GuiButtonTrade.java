package team.cqr.cqrepoured.client.gui.npceditor;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Advancement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.gui.INumericIDButton;
import team.cqr.cqrepoured.client.util.GuiHelper;
import team.cqr.cqrepoured.entity.trade.Trade;
import team.cqr.cqrepoured.faction.FactionRegistry;

public class GuiButtonTrade extends Button implements INumericIDButton {

	private static final ResourceLocation TEXTURE = new ResourceLocation(CQRMain.MODID, "textures/gui/container/gui_button_trade.png");
	private int index;
	private final int id;
	private Trade trade;
	private GuiMerchant parent;

	public GuiButtonTrade(GuiMerchant parent, final int id, int x, int y, int index) {
		super(x, y, 116, 20, new TextComponent(""), Button::onPress);
		this.index = index;
		this.id = id;
		this.parent = parent;
	}
	
	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public void onPress() {
		super.onPress();
		this.parent.actionPerformed(this);
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getIndex() {
		return this.index;
	}

	public void setTrade(Trade trade) {
		this.trade = trade;
	}

	@Override
	public void renderButton(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		// Referenced from net.minecraftforge.fml.client.gui.widget.ExtendedButton:renderButton
		if (this.visible && this.trade != null) {
			Minecraft mc = Minecraft.getInstance();
			mc.getTextureManager().bind(TEXTURE);
			RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
			int i = this.getYImage(this.isHovered());
			RenderSystem.enableBlend();
			RenderSystem.defaultBlendFunc();
			RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			GuiHelper.drawTexture(this.x, this.y, 0.0D, i / 4.0D, this.width, this.height, 0.90625D, 0.25D);
			this.mouseMoved(mouseX, mouseY);

			GuiHelper.drawTexture(this.x + 77.0D, this.y + 2.0D, 0.0D, 0.75D, 16.0D, 16.0D, 0.125D, 0.2D);
			if (!this.trade.isUnlockedFor(mc.player)) {
				GuiHelper.drawTexture(this.x + 77.0D, this.y + 2.0D, 0.25D, 0.75D, 16.0D, 16.0D, 0.125D, 0.2D);
			} else if (!this.trade.isInStock()) {
				GuiHelper.drawTexture(this.x + 77.0D, this.y + 2.0D, 0.125D, 0.75D, 16.0D, 16.0D, 0.125D, 0.2D);
			}

			ItemRenderer itemRender = mc.getItemRenderer();
			RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			// TODO Unknown moved reference:
			// OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
			RenderHelper.setupForFlatItems();
			RenderSystem.enableRescaleNormal();
			RenderSystem.enableDepthTest();

			itemRender.blitOffset = 100.0F;
			int x = this.x + 4;
			int y = this.y + 2;
			for (int j = 0; j < 4 && j < this.trade.getInputItemsClient().size(); j++) {
				itemRender.renderAndDecorateItem(this.trade.getInputItemsClient().get(j).getStack(), x + j * 18, y);
				itemRender.renderGuiItemDecorations(mc.font, this.trade.getInputItemsClient().get(j).getStack(), x + j * 18, y, null);
			}
			itemRender.renderAndDecorateItem( this.trade.getOutputClient(), x + 92, y);
			itemRender.renderGuiItemDecorations(mc.font, this.trade.getOutputClient(), x + 92, y, null);
			itemRender.blitOffset = 0.0F;

			RenderSystem.disableDepthTest();
			RenderSystem.disableRescaleNormal();
		}
	}

	@SuppressWarnings("resource")
	public void renderHoveredToolTip(GuiMerchant parent, PoseStack matrixStack, int mouseX, int mouseY) {
		if (!this.visible || this.trade == null) {
			return;
		}
		for (int i = 0; i < 4 && i < this.trade.getInputItemsClient().size(); i++) {
			int x = this.x + 4 + i * 18;
			int y = this.y + 2;
			if (mouseX >= x && mouseX <= x + 16 && mouseY >= y && mouseY <= y + 16) {
				parent.renderTooltip(matrixStack, this.trade.getInputItemsClient().get(i).getStack(), mouseX, mouseY);
			}
		}
		int x = this.x + 96;
		int y = this.y + 2;
		if (mouseX >= x && mouseX <= x + 16 && mouseY >= y && mouseY <= y + 16) {
			parent.renderTooltip(matrixStack, this.trade.getOutputClient(), mouseX, mouseY);
		}
		x = this.x + 78;
		if(this.parent.getMinecraft() == null) {
			return;
		}
		if (mouseX >= x && mouseX <= x + 16 && mouseY >= y && mouseY <= y + 16) {
			List<TextComponent> tooltip = new ArrayList<>();
			boolean isUnlocked = this.trade.isUnlockedFor(parent.getMinecraft().player);
			boolean inStock = this.trade.isInStock();
			if (!isUnlocked) {
				tooltip.add(new TranslationTextComponent("description.gui_button_trade.locked.name"));
				if (this.trade.getRequiredAdvancement() != null) {
					ChatFormatting formatting = CQRMain.PROXY.hasAdvancement(parent.getMinecraft().player, this.trade.getRequiredAdvancement()) ? ChatFormatting.GREEN : ChatFormatting.RED;
					Advancement advancement = CQRMain.PROXY.getAdvancement(parent.getMinecraft().player, this.trade.getRequiredAdvancement());
					IFormattableTextComponent advancementName =
							advancement != null ? advancement.getDisplay().getTitle().plainCopy().withStyle(formatting)
									: Advancement.Builder.advancement().build(this.trade.getRequiredAdvancement()).getDisplay().getTitle().plainCopy().withStyle(formatting);
					tooltip.add(advancementName);
				}
				if (this.trade.getRequiredReputation() != Integer.MIN_VALUE) {
					int i = FactionRegistry.instance(parent.getMinecraft().player).getExactReputationOf(parent.getMinecraft().player.getUUID(), this.trade.getHolder().getTraderFaction());
					ChatFormatting formatting = i >= this.trade.getRequiredReputation() ? ChatFormatting.GREEN : ChatFormatting.RED;
					tooltip.add(new TextComponent("" + this.trade.getHolder().getTraderFaction().getName() + " " + i + "/" + this.trade.getRequiredReputation()).withStyle(formatting));
				}
			} else if (!inStock) {
				tooltip.add(new TranslationTextComponent("description.gui_button_trade.out_of_stock.name"));
			} else {
				tooltip.add(new TranslationTextComponent("description.gui_button_trade.unlocked.name"));
				if (this.trade.getRequiredAdvancement() != null) {
					Advancement advancement = CQRMain.PROXY.getAdvancement(parent.getMinecraft().player, this.trade.getRequiredAdvancement());
					IFormattableTextComponent advancementName =
							advancement != null ? advancement.getDisplay().getTitle().plainCopy()
									: Advancement.Builder.advancement().build(this.trade.getRequiredAdvancement()).getDisplay().getTitle().plainCopy().withStyle(ChatFormatting.GREEN);
					tooltip.add(advancementName);
				}
				if (this.trade.getRequiredReputation() != Integer.MIN_VALUE) {
					int i = this.trade.getRequiredReputation();
					tooltip.add(new TextComponent("" + this.trade.getHolder().getTraderFaction().getName() + " " + i + "/" + i).withStyle(ChatFormatting.GREEN));
				}
			}
			parent.renderComponentTooltip(matrixStack, tooltip, mouseX, mouseY);
		}
	}

}
