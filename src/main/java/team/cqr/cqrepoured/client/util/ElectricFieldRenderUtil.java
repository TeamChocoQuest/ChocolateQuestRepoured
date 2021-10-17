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

		// First disable tex2d and lighting, we do not use a texture and don't want to be affected by lighting
		GlStateManager.disableTexture2D();
		GlStateManager.disableLighting();

		// Grab instance of tessellator and bufferbuilder
		Tessellator tess = Tessellator.getInstance();
		BufferBuilder builder = tess.getBuffer();

		// Since we use a blend function, enable it and apply the blend function
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, 1);

		// Grab some random instance and set the line width
		GlStateManager.glLineWidth(3.0F);
		
		//Since we're using vertex.position we need to set the color beforehand
		GlStateManager.color(0.5F, 0.64F, 1.0F, 0.6F);

		// Now we want to draw <boltCount> lightnings
		for (int boltCount = 0; boltCount < bolts; ++boltCount) {
			// The steps defines the "segments" of the lightning
			int steps = rng.nextInt(26) + 5;
			// Initialize the drawing process, our vertices consist of positions and color information
			builder.begin(3, DefaultVertexFormats.POSITION);

			// now, actually calculate the position of each "segment"
			for (int i = 0; i <= steps; ++i) {

				// Apply some "noise" so it looks "electric" (it will jitter when rendering)
				double vX = (rng.nextFloat() * 2 -1) * fieldRadius /*+ (rng.nextFloat() - 0.5D) * (fieldRadius / 2)*/;
				double vZ = (rng.nextFloat() * 2 -1) * fieldRadius /*+ (rng.nextFloat() - 0.5D) * (fieldRadius / 2)*/;
				double vY = (rng.nextFloat() * 2 -1) * fieldHeight /*+ (rng.nextFloat() - 0.5D) * (fieldHeight / 2)*/;

				// Finally, create the vertex
				builder.pos(x + vX, y + vY, z + vZ).endVertex();
			}
			// Draw the lightning
			tess.draw();
		}

		// Finally re-enable tex2d and lightning and disable blending
		GlStateManager.disableBlend();
		GlStateManager.enableTexture2D();
		GlStateManager.enableLighting();

		GlStateManager.popMatrix();
	}

	/*
	 * X, Y, Z are the weird xyz from the rendering stuff in the entities
	 */
	public static void renderElectricLineBetween(Vec3d startOffset, Vec3d endOffset, Random rng, final double maxOffset, double posX, double posY, double posZ, int boltCount) {
		startOffset = startOffset.add(posX, posY, posZ);
		endOffset = endOffset.add(posX, posY, posZ);
		GlStateManager.pushMatrix();

		// First disable tex2d and lighting, we do not use a texture and don't want to be affected by lighting
		GlStateManager.disableTexture2D();
		GlStateManager.disableLighting();

		// Grab instance of tessellator and bufferbuilder
		Tessellator tess = Tessellator.getInstance();
		BufferBuilder builder = tess.getBuffer();

		// Since we use a blend function, enable it and apply the blend function
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, 1);

		// Grab some random instance and set the line width
		GlStateManager.glLineWidth(3.0F);

		// Now we want to draw <boltCount> lightnings
		for (int i = 0; i < boltCount; i++) {
			renderSingleElectricLineBetween(builder, tess, startOffset, endOffset, maxOffset, rng);
		}

		// Finally re-enable tex2d and lightning and disable blending
		GlStateManager.disableBlend();
		GlStateManager.enableTexture2D();
		GlStateManager.enableLighting();

		GlStateManager.popMatrix();
	}
	
	static Vec3d generateOffsetVector(final Vec3d direction, final Vec3d directionSecondary, Random rng, final double pointVariation) {
		Vec3d offsetVector = direction.crossProduct(directionSecondary);
		offsetVector = offsetVector.normalize();
		offsetVector = offsetVector.scale(rng.nextDouble() * pointVariation);

		offsetVector = VectorUtil.rotateAroundAnyAxis(direction, offsetVector, DungeonGenUtils.randomBetween(0, 360, rng));
		
		return offsetVector;
	}

	private static void renderSingleElectricLineBetween(BufferBuilder builder, Tessellator tess, Vec3d start, Vec3d end, final double pointVariation, Random rng) {
		// Initialize the drawing process, our vertices consist of positions and color information
		builder.begin(3, DefaultVertexFormats.POSITION_COLOR);

		final Vec3d direction = end.subtract(start).normalize();
		final Vec3d directionSecondary = VectorUtil.rotateVectorAroundY(direction, 90);
		
		final double lineLength = pointVariation * 3;
		final int steps = (int) Math.floor(end.subtract(start).length()  / lineLength) -1;
		
		Vec3d lastPos = start;
		for(int i = 0; i < steps; i++) {
			Vec3d offsetVector = generateOffsetVector(direction, directionSecondary, rng, pointVariation);

			builder.pos(lastPos.x + offsetVector.x, lastPos.y + offsetVector.y, lastPos.z  + offsetVector.z).color(0.5F, 0.64F, 1.0F, 0.6F).endVertex();
			
			lastPos = lastPos.add(direction.scale(lineLength));
		}
		Vec3d offsetVector = generateOffsetVector(direction, directionSecondary, rng, pointVariation);
		builder.pos(end.x + offsetVector.x, end.y + offsetVector.y, end.z  + offsetVector.z).color(0.5F, 0.64F, 1.0F, 0.6F).endVertex();
		
		tess.draw();
	}
	
	private static void renderSingleElectricLine(BufferBuilder builder, Tessellator tess, Vec3d start, Vec3d direction, final double lineLength, Random rng, final double pointVariation) {
		final Vec3d end = start.add(direction.normalize().scale(lineLength));
		renderSingleElectricLineBetween(builder, tess, start, end, pointVariation, rng);
	}

	public static void renderElectricFieldWithSizeOfEntityAt(Entity entity, double x, double y, double z) {
		renderElectricField(entity.ticksExisted, entity.world.rand, entity.width / 2, entity.height / 2, x, y + entity.height / 2, z, 5, 40);
	}

}
