package team.cqr.cqrepoured.client.gui;

import java.awt.TextComponent;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.CQRConstants;
import team.cqr.cqrepoured.inventory.ContainerBossBlock;

@OnlyIn(Dist.CLIENT)
public class ScreenBossBlock extends ContainerScreen<ContainerBossBlock> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(CQRConstants.MODID, "textures/gui/container/gui_boss_block.png");

	public ScreenBossBlock(ContainerBossBlock container, Inventory playerInv, TextComponent title) {
		super(container, playerInv, title);
		this.imageHeight = 132;
		this.inventoryLabelY = this.imageHeight - 94;
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
