package team.cqr.cqrepoured.client.util;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;

public class BlockRenderUtil {

	public static void renderBlockAtEntity(MatrixStack matrixStack, IRenderTypeBuffer rtb, int packedLightIn, BlockState iBlockState, Entity currentEntity, EntityRenderer<? extends Entity> renderer) {
		if(iBlockState.getRenderShape() != BlockRenderType.MODEL) {
			return;
		}
		matrixStack.pushPose();
		matrixStack.translate(-0.25F, -0.25F, 0.25F);
		matrixStack.scale(0.5F, 0.5F, 0.5F);

		//TODO: Recreate in 1.16... find how i made it in the first place...
		Minecraft.getInstance().getBlockRenderer().renderSingleBlock(iBlockState, matrixStack, rtb, packedLightIn, OverlayTexture.NO_OVERLAY);
		matrixStack.popPose();
		
		/*GlStateManager.enableRescaleNormal();
		GlStateManager.pushMatrix();

		GlStateManager.translate(-0.25F, -0.25F, 0.25F);
		GlStateManager.scale(0.5F, 0.5F, 0.5F);

		int i = currentEntity.getBrightness();
		int j = i % 65536;
		int k = i / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j, k);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		renderer.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
		blockrendererdispatcher.renderBlockBrightness(iBlockState, 1.0F);

		GlStateManager.popMatrix();
		GlStateManager.disableRescaleNormal();*/
	}

}
