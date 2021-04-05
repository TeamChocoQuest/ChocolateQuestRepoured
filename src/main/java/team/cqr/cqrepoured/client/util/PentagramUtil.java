package team.cqr.cqrepoured.client.util;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.Vec3d;
import team.cqr.cqrepoured.util.VectorUtil;

public class PentagramUtil {
	
	protected static void setLightmapDisabled(boolean disabled) {
		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);

		if (disabled) {
			GlStateManager.disableTexture2D();
		} else {
			GlStateManager.enableTexture2D();
		}

		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}
	
	public static void preRenderPentagram(double x, double y, double z, int ticksExisted) {
		GlStateManager.translate((float) x, (float) y + 0.02, (float) z);
		GlStateManager.rotate((float) (4 * ticksExisted), 0F, 1F, 0F);
		GlStateManager.disableFog();
		GlStateManager.disableLighting();
		GlStateManager.disableCull();
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
	}
	
	public static void postRenderPentagram() {
		GlStateManager.glLineWidth(1.0F);
		GlStateManager.enableLighting();
		GlStateManager.enableTexture2D();
		GlStateManager.enableCull();
		GlStateManager.enableDepth();
		GlStateManager.depthMask(true);
		GlStateManager.enableFog();
	}
	
	public static void renderPentagram(int ticksExisted) {
		double corners = 5;
		int r = 55 + (int) Math.round(200 * 0.5 * (Math.sin(0.25 * ticksExisted) + 1));
		int g = 1;
		int b = 1;

		
		renderPentagram(ticksExisted, r, g, b, corners);
	}
	
	public static void renderPentagram(int ticksExisted, float rf, float gf, float bf, double corners) {
		int r = (int) Math.floor(Math.max(1, rf * 255));
		int g = (int) Math.floor(Math.max(1, gf * 255));
		int b = (int) Math.floor(Math.max(1, bf * 255));
		renderPentagram(ticksExisted, r, g, b, corners);
	}
	
	public static void renderPentagram(int ticksExisted, int r, int g, int b, double corners) {
		setLightmapDisabled(true);

		Tessellator tess = Tessellator.getInstance();
		BufferBuilder builder = tess.getBuffer();

		GlStateManager.glLineWidth(8.0F);
		builder.begin(3, DefaultVertexFormats.POSITION_COLOR);

		double radius = 0.75D;
		
		Vec3d vector = new Vec3d(radius, 0, 0);
		double alpha = 360D / corners;
		int skipCorners = Math.floorDiv((int)corners, 2);
		alpha *= (double) skipCorners;
		// First, draw the diagonal lines (get one point, and move to the over-next point
		for (int i = 0; i <= corners; i++) {

			builder.pos(vector.x, 0, vector.z).color(r, g, b, 255).endVertex();

			vector = VectorUtil.rotateVectorAroundY(vector, alpha);
		}
		vector = VectorUtil.rotateVectorAroundY(vector, -alpha);
		alpha /= (double) skipCorners;
		vector = VectorUtil.rotateVectorAroundY(vector, alpha);
		// after this, we need to render the outline
		for (int i = 0; i < corners; i++) {
			builder.pos(vector.x, 0, vector.z).color(r, g, b, 255).endVertex();
			vector = VectorUtil.rotateVectorAroundY(vector, alpha);
		}

		tess.draw();

		setLightmapDisabled(false);
	}

}
