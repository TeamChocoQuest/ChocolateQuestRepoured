package com.teamcqr.chocolatequestrepoured.client.render.entity.layers;

import org.lwjgl.opengl.GL11;

import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQREntity;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class LayerCQRSpeechbubble extends AbstractLayerCQR {

	public static final int CHANGE_BUBBLE_INTERVAL = 80;

	public LayerCQRSpeechbubble(RenderCQREntity<?> livingEntityRendererIn) {
		super(livingEntityRendererIn);
	}

	@Override
	public void doRenderLayer(AbstractEntityCQR entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		if (CQRConfig.general.enableSpeechBubbles && entity.isChatting()) {
			Tessellator tessellator = Tessellator.getInstance();
			Minecraft minecraft = Minecraft.getMinecraft();

			GlStateManager.pushMatrix();
			GlStateManager.rotate(netHeadYaw, 0.0F, 1.0F, 0.0F);
			// DONE : This does not really line up with scaling mob size
			if (this.entityRenderer.heightScale != 1.0D || this.entityRenderer.widthScale != 1.0D) {
				GlStateManager.translate(0.0D, 1.5D, 0.0D);
				GlStateManager.scale(1.0D / this.entityRenderer.widthScale, 1.0D / this.entityRenderer.heightScale, 1.0D / this.entityRenderer.widthScale);
				GlStateManager.translate(0.0D, -1.5D, 0.0D);
			}
			GlStateManager.translate(-0.5D, (-1.15D * (double) entity.height) / entity.getSizeVariation(), 0.0D);

			minecraft.getTextureManager().bindTexture(entity.getCurrentSpeechBubble().getResourceLocation());

			BufferBuilder buffer = tessellator.getBuffer();
			buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			buffer.pos(0, 1, 0).tex(0, 1).endVertex();
			buffer.pos(1, 1, 0).tex(1, 1).endVertex();
			buffer.pos(1, 0, 0).tex(1, 0).endVertex();
			buffer.pos(0, 0, 0).tex(0, 0).endVertex();

			tessellator.draw();

			GlStateManager.popMatrix();

		}

	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}

}
