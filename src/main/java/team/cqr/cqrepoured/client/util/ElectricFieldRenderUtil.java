package team.cqr.cqrepoured.client.util;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.util.VectorUtil;

public class ElectricFieldRenderUtil {
	
	public static void renderElectricField(int ticksExisted, Random rng, double fieldRadius, double fieldHeight, double x, double y, double z, int bolts, double boltSize) {
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
		GlStateManager.glLineWidth(3.0F);

		//Now we want to draw <boltCount> lightnings
		for (int boltCount = 0; boltCount < bolts; ++boltCount) {
			//The steps defines the "segments" of the lightning
			int steps = rng.nextInt(26) + 5;
			//Initialize the drawing process, our vertices consist of positions and color information
			builder.begin(3, DefaultVertexFormats.POSITION_COLOR);

			//now, actually calculate the position of each "segment"
			for (int i = 0; i <= steps; ++i) {

				//Apply some "noise" so it looks "electric" (it will jitter when rendering)
				double vX = Math.sin((i + rng.nextInt()) / boltSize * Math.PI * 2.0D) * fieldRadius + (rng.nextDouble() - 0.5D) * (fieldRadius / 2);
				double vZ = Math.cos((i + rng.nextInt()) / boltSize * Math.PI * 2.0D) * fieldRadius + (rng.nextDouble() - 0.5D) * (fieldRadius / 2);
				double vY = Math.sin((ticksExisted + i + rng.nextInt()) / boltSize * Math.PI) * (fieldHeight) + rng.nextDouble() + fieldHeight;

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
	
	/*
	 * X, Y, Z are the weird xyz from the rendering stuff in the entities
	 */
	public static void renderElectricLineBetween(Vec3d start, Vec3d end, Random rng, final double maxOffset, double renderX, double renderY, double renderZ, int boltCount) {
		//start = start.add(renderX, renderY, renderZ);
		//end = end.add(renderX, renderY, renderZ);
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
		GlStateManager.glLineWidth(3.0F);
		
		//Now we want to draw <boltCount> lightnings
		Vec3d direction = end.subtract(start);
		final double lineLength = direction.length();
		direction = direction.normalize();
		
		for(int i = 0; i < boltCount; i++) {
			renderSingleElectricLine(builder, tess, direction, direction, direction, lineLength, rng, maxOffset);
		}
		
		//Finally re-enable tex2d and lightning and disable blending
		GlStateManager.disableBlend();
		GlStateManager.enableTexture2D();
		GlStateManager.enableLighting();

		GlStateManager.popMatrix();
	}

	private static void renderSingleElectricLine(BufferBuilder builder, Tessellator tess, Vec3d start, Vec3d end, Vec3d direction, final double lineLength, Random rng, final double maxOffset) {
		//Initialize the drawing process, our vertices consist of positions and color information
		builder.begin(3, DefaultVertexFormats.POSITION_COLOR);
		
		//Put the first pos at start
		builder.pos(start.x, start.y, start.z).color(0.5F, 0.64F, 1.0F, 0.6F).endVertex();
		Vec3d pos = new Vec3d(start.x, start.y, start.z);
		
		double processedLength = 0;
		final Vec3d dirSecondary = VectorUtil.rotateVectorAroundY(direction, 90);
		while(processedLength < lineLength) {
			Vec3d increment = direction.normalize().scale(0.5D + rng.nextDouble());
			
			pos = pos.add(increment);
			
			Vec3d offsetVector = direction.crossProduct(dirSecondary);
			offsetVector = offsetVector.normalize();
			offsetVector = offsetVector.scale(rng.nextDouble() * maxOffset);
			
			offsetVector = VectorUtil.rotateAroundAnyAxis(direction, offsetVector, DungeonGenUtils.randomBetween(0,360,rng));
			
			
			builder.pos(pos.x + offsetVector.x, pos.y + offsetVector.y, pos.z + offsetVector.z).color(0.5F, 0.64F, 1.0F, 0.6F).endVertex();
			
			processedLength += increment.length();
		}
		
		//Put the last pos at end
		// builder.pos(end.x, end.y, end.z).color(0.5F, 0.64F, 1.0F, 0.6F).endVertex();
		
		tess.draw();
	}

	public static void renderElectricFieldWithSizeOfEntityAt(Entity entity, double x, double y, double z) {
		renderElectricField(entity.ticksExisted, entity.world.rand, entity.width, entity.height / 2, x, y, z, 5, 80);
	}

}
