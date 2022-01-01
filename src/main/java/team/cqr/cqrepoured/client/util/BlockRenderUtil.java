package team.cqr.cqrepoured.client.util;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.entity.Entity;

public class BlockRenderUtil {

	public static void renderBlockAtEntity(BlockState iBlockState, Entity currentEntity, EntityRenderer<? extends Entity> renderer) {
		BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
		GlStateManager.enableRescaleNormal();
		GlStateManager.pushMatrix();

		GlStateManager.translate(-0.25F, -0.25F, 0.25F);
		GlStateManager.scale(0.5F, 0.5F, 0.5F);

		int i = currentEntity.getBrightnessForRender();
		int j = i % 65536;
		int k = i / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j, k);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		renderer.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
		blockrendererdispatcher.renderBlockBrightness(iBlockState, 1.0F);

		GlStateManager.popMatrix();
		GlStateManager.disableRescaleNormal();
	}

}
