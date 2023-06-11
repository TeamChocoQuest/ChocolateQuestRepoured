package team.cqr.cqrepoured.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.inventory.ContainerBackpack;

@OnlyIn(Dist.CLIENT)
public class ScreenBackpack extends ContainerScreen<ContainerBackpack> {

	private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/container/shulker_box.png");

	public ScreenBackpack(ContainerBackpack container, Inventory playerInv, TextComponent title) {
		super(container, playerInv, title);
		++this.imageHeight;
	}

	@Override
	public void render(PoseStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
		this.renderBackground(pMatrixStack);
		super.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);
		this.renderTooltip(pMatrixStack, pMouseX, pMouseY);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void renderBg(PoseStack pMatrixStack, float pPartialTicks, int pX, int pY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bind(TEXTURE);
		this.blit(pMatrixStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
	}

}
