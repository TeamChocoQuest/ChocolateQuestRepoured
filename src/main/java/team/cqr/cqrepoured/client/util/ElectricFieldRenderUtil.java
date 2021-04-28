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
		
		//First disable tex2d and lighting, we do not use a texture and don't want to be affected by lighting
		GlStateManager.disableTexture2D();
		GlStateManager.disableLighting();

		//Grab instance of tessellator and bufferbuilder
		Tessellator tess = Tessellator.getInstance();
		BufferBuilder builder = tess.getBuffer();

		//Since we use a blend function, enable it and apply the blend function
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, 1);

		//Grab some random instance and set the line width
		Random rng = entity.world.rand;
		GlStateManager.glLineWidth(3.0F);

		//Now we want to draw <boltCount> lightnings
		for (int boltCount = 0; boltCount < bolts; ++boltCount) {
			//The steps defines the "segments" of the lightning
			int steps = rng.nextInt(26) + 5;
			//Initialize the drawing process, our vertices consist of positions and color information
			builder.begin(3, DefaultVertexFormats.POSITION_COLOR);

			//now, actually calculate the position of each "segment"
			for (int i = 0; i <= steps; ++i) {
				double radius = entity.width;

				//Apply some "noise" so it looks "electric" (it will jitter when rendering)
				double vX = Math.sin((i + rng.nextInt()) / boltSize * Math.PI * 2.0D) * radius + (rng.nextDouble() - 0.5D) * (entity.width / 2);
				double vZ = Math.cos((i + rng.nextInt()) / boltSize * Math.PI * 2.0D) * radius + (rng.nextDouble() - 0.5D) * (entity.width / 2);
				double vY = Math.sin((entity.ticksExisted + i + rng.nextInt()) / boltSize * Math.PI) * (entity.height / 2) + rng.nextDouble() + entity.height / 2;

				//Finally, create the vertex
				builder.pos(x + vX, y + vY, z + vZ).color(0.5F, 0.64F, 1.0F, 0.6F).endVertex();
			}
			//Draw the lightning
			tess.draw();
		}

		//Finally re-enable tex2d and lightning and disable blending
		GlStateManager.disableBlend();
		GlStateManager.enableTexture2D();
		GlStateManager.enableLighting();

		GlStateManager.popMatrix();
	}

	public static void renderElectricField(Entity entity, double x, double y, double z) {
		renderElectricField(entity, x, y, z, 5, 80);
	}

}
