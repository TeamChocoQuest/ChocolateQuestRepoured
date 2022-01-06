package team.cqr.cqrepoured.client.util;

import com.mojang.blaze3d.platform.GlStateManager;

public class EmissiveUtil {

	public static void preEmissiveTextureRendering() {
		GlStateManager._enableBlend();
		GlStateManager._blendFunc(GlStateManager.SourceFactor.SRC_ALPHA.value, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.value);
		GlStateManager._disableLighting();
		//Not needed anymore?
		//GlStateManager._setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GlStateManager._disableTexture();
		//GlStateManager._setActiveTexture(OpenGlHelper.defaultTexUnit);
	}

	public static void postEmissiveTextureRendering() {
		//Not needed anymore?
		//GlStateManager._setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GlStateManager._enableTexture();
		//GlStateManager._setActiveTexture(OpenGlHelper.defaultTexUnit);
		GlStateManager._enableLighting();
		GlStateManager._disableBlend();
	}

}
