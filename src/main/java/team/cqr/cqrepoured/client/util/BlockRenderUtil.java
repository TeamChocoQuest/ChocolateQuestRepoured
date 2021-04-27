package team.cqr.cqrepoured.client.util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;

public class BlockRenderUtil {

	public static void renderBlockAtEntity(IBlockState iBlockState, Entity currentEntity, Render<? extends Entity> renderer) {
		BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
		GlStateManager.enableRescaleNormal();
		GlStateManager.pushMatrix();

		GlStateManager.translate(-0.25F, -0.25F, 0.25F);
		GlStateManager.scale(0.5F, 0.5F, 0.5F);

		int i = currentEntity.getBrightnessForRender();
		int j = i % 65536;
		int k = i / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j, (float) k);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		renderer.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		blockrendererdispatcher.renderBlockBrightness(iBlockState, 1.0F);

		GlStateManager.popMatrix();
		GlStateManager.disableRescaleNormal();
	}
	
}
