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
		matrixStack.translate(-0.25F, -0.25F, -0.25F);
		matrixStack.scale(0.5F, 0.5F, 0.5F);

		Minecraft.getInstance().getBlockRenderer().renderBlock(iBlockState, matrixStack, rtb, packedLightIn, OverlayTexture.NO_OVERLAY, null);
		matrixStack.popPose();
	}

}
