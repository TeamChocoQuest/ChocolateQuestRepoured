package team.cqr.cqrepoured.client.util;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;

public class ElectricFieldRenderUtil {
	
	public static void renderElectricField(Entity entity, double x, double y, double z, int bolts, double boltSize) {
		GlStateManager.pushMatrix();

		GlStateManager.disableTexture2D();
		GlStateManager.disableLighting();

		Tessellator tess = Tessellator.getInstance();
		BufferBuilder builder = tess.getBuffer();

		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, 1);

		Random rng = entity.world.rand;
		GlStateManager.glLineWidth(3.0F);

		for (int boltCount = 0; boltCount < bolts; ++boltCount) {
			int steps = rng.nextInt(26) + 5;
			builder.begin(3, DefaultVertexFormats.POSITION_COLOR);

			for (int i = 0; i <= steps; ++i) {
				double radius = entity.width;

				double vX = Math.sin((i + rng.nextInt()) / boltSize * Math.PI * 2.0D) * radius + (rng.nextDouble() - 0.5D) * (entity.width / 2);
				double vZ = Math.cos((i + rng.nextInt()) / boltSize * Math.PI * 2.0D) * radius + (rng.nextDouble() - 0.5D) * (entity.width / 2);
				double vY = Math.sin((entity.ticksExisted + i + rng.nextInt()) / boltSize * Math.PI) * (entity.height / 2) + rng.nextDouble() + entity.height / 2;

				builder.pos(x + vX, y + vY, z + vZ).color(0.5F, 0.64F, 1.0F, 0.6F).endVertex();
			}

			tess.draw();
		}

		GlStateManager.disableBlend();
		GlStateManager.enableTexture2D();
		GlStateManager.enableLighting();

		GlStateManager.popMatrix();
	}

	public static void renderElectricField(Entity entity, double x, double y, double z) {
		renderElectricField(entity, x, y, z, 5, 80);
	}

}
