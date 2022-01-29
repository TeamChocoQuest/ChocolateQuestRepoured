package team.cqr.cqrepoured.client.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Random;
import java.util.stream.IntStream;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import team.cqr.cqrepoured.util.VectorUtil;

public class ElectricFieldRenderUtil {

	@SuppressWarnings("serial")
	private static final Random RANDOM = new Random() {
		private static final long MULTIPLIER = 0x5_DEEC_E66DL;
		private static final long ADDEND = 0xBL;
		private static final long MASK = (1L << 48) - 1;
		private long seed = 0L;

		@Override
		public void setSeed(long seed) {
			this.seed = (seed ^ MULTIPLIER) & MASK;
		}

		@Override
		protected int next(int bits) {
			this.seed = (this.seed * MULTIPLIER + ADDEND) & MASK;
			return (int) (this.seed >>> (48 - bits));
		}
	};
	private static final Tessellator TESSELATOR = Tessellator.getInstance();
	private static final BufferBuilder VERTEX_BUFFER = TESSELATOR.getBuilder();
	private static final IntBuffer FIRST_BUFFER = ByteBuffer.allocateDirect(64).order(ByteOrder.nativeOrder()).asIntBuffer();
	private static final IntBuffer COUNT_BUFFER = ByteBuffer.allocateDirect(64).order(ByteOrder.nativeOrder()).asIntBuffer();
	private static int vertexCount;

	private static void startLineStripBatch(VertexFormat format) {
		VERTEX_BUFFER.begin(GL11.GL_LINE_STRIP, format);
	}

	private static void addVertex(double x, double y, double z, boolean endLineStrip) {
		VERTEX_BUFFER.vertex(x, y, z).endVertex();
		vertexCount++;

		if (endLineStrip) {
			FIRST_BUFFER.put(VERTEX_BUFFER.vertexCounts.size() - vertexCount);
			COUNT_BUFFER.put(vertexCount);
			vertexCount = 0;
		}
	}

	private static void endLineStripBatch() {
		if (vertexCount > 0) {
			throw new IllegalStateException("Last line strip not finished!");
		}

		VERTEX_BUFFER.end();
		FIRST_BUFFER.flip();
		COUNT_BUFFER.flip();

		VertexFormat format = VERTEX_BUFFER.getVertexFormat();
		IntStream.range(0, format.getElements().size()).forEach((int i) -> format.getElements().get(i).getUsage().preDraw(format, i, format.getVertexSize(), VERTEX_BUFFER.getByteBuffer()));

		GL14.glMultiDrawArrays(GL11.GL_LINE_STRIP, FIRST_BUFFER, COUNT_BUFFER);

		IntStream.range(0, format.getElements().size()).forEach((int i) -> format.getElements().get(i).getUsage().preDraw(format, i, format.getVertexSize(), VERTEX_BUFFER.getByteBuffer()));

		VERTEX_BUFFER.clear();
		FIRST_BUFFER.clear();
		COUNT_BUFFER.clear();
	}

	public static void renderElectricField(double fieldRadius, double fieldHeight, double x, double y, double z, int bolts, long seed) {
		RANDOM.setSeed(seed);
		
		EmissiveUtil.preEmissiveTextureRendering();

		// First disable tex2d and lighting, we do not use a texture and don't want to be affected by lighting
		GlStateManager._disableTexture();
		GlStateManager._disableLighting();

		// Since we use a blend function, enable it and apply the blend function
		GlStateManager._enableBlend();
		GlStateManager._blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);

		// Grab some random instance and set the line width
		GlStateManager._lineWidth(3.0F);

		// Since we're using vertex.position we need to set the color beforehand
		GlStateManager._color4f(0.5F, 0.64F, 1.0F, 0.6F);

		// Initialize the drawing process, our vertices consist of positions and color information
		startLineStripBatch(DefaultVertexFormats.POSITION);

		// Now we want to draw <boltCount> lightnings
		for (int boltCount = 0; boltCount < bolts; ++boltCount) {
			// The steps defines the "segments" of the lightning
			int steps = RANDOM.nextInt(26) + 5;

			// now, actually calculate the position of each "segment"
			for (int i = 0; i <= steps; ++i) {

				// Apply some "noise" so it looks "electric" (it will jitter when rendering)
				double vX = (RANDOM.nextFloat() * 2 - 1) * fieldRadius /* + (rng.nextFloat() - 0.5D) * (fieldRadius / 2) */;
				double vZ = (RANDOM.nextFloat() * 2 - 1) * fieldRadius /* + (rng.nextFloat() - 0.5D) * (fieldRadius / 2) */;
				double vY = (RANDOM.nextFloat() * 2 - 1) * fieldHeight /* + (rng.nextFloat() - 0.5D) * (fieldHeight / 2) */;

				// Finally, create the vertex
				addVertex(x + vX, y + vY, z + vZ, i == steps);
			}
		}

		// Draw the lightning
		endLineStripBatch();

		// Finally re-enable tex2d and lightning and disable blending
		GlStateManager._disableBlend();
		GlStateManager._enableTexture();
		GlStateManager._enableLighting();
		
		EmissiveUtil.postEmissiveTextureRendering();
	}

	/*
	 * X, Y, Z are the weird xyz from the rendering stuff in the entities
	 */
	public static void renderElectricLineBetween(Vector3d startOffset, Vector3d endOffset, double maxOffset, double posX, double posY, double posZ, int boltCount, long seed) {
		startOffset = startOffset.add(posX, posY, posZ);
		endOffset = endOffset.add(posX, posY, posZ);
		RANDOM.setSeed(seed);

		EmissiveUtil.preEmissiveTextureRendering();
		
		// First disable tex2d and lighting, we do not use a texture and don't want to be affected by lighting
		GlStateManager._disableTexture();
		GlStateManager._disableLighting();

		// Since we use a blend function, enable it and apply the blend function
		GlStateManager._enableBlend();
		GlStateManager._blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);

		// Grab some random instance and set the line width
		GlStateManager._lineWidth(3.0F);

		// Since we're using vertex.position we need to set the color beforehand
		GlStateManager._color4f(0.5F, 0.64F, 1.0F, 0.6F);

		startLineStripBatch(DefaultVertexFormats.POSITION);

		Vector3d direction = endOffset.subtract(startOffset);
		double distance = direction.length();
		direction = direction.scale(1.0D / distance);
		Vector3d directionOffset;
		if (direction.x < 0.5D) {
			directionOffset = direction.cross(new Vector3d(1.0D, 0.0D, 0.0D)).normalize();
		} else {
			directionOffset = direction.cross(new Vector3d(0.0D, 0.0D, 1.0D)).normalize();
		}
		int steps = Math.max(MathHelper.floor(distance / (maxOffset * 2.0D)), 4);
		double lineLength = distance / steps;

		// Now we want to draw <boltCount> lightnings
		for (int i = 0; i < boltCount; i++) {
			renderSingleElectricLineBetween(startOffset, direction, directionOffset, lineLength, steps, maxOffset);
		}

		// Draw the lightning
		endLineStripBatch();

		// Finally re-enable tex2d and lightning and disable blending
		GlStateManager._disableBlend();
		GlStateManager._enableTexture();
		GlStateManager._enableLighting();
		
		EmissiveUtil.postEmissiveTextureRendering();
	}

	private static void renderSingleElectricLineBetween(Vector3d start, Vector3d direction, Vector3d directionOffset, double lineLength, int steps, double offset) {
		addVertex(start.x, start.y, start.z, false);
		for (int i = 1; i < steps; i++) {
			Vector3d offsetVector = generateOffsetVector(direction, directionOffset);
			double offsetScale = RANDOM.nextFloat() * offset;
			double vX = start.x + (direction.x * i * lineLength) + (offsetVector.x * offsetScale);
			double vY = start.y + (direction.y * i * lineLength) + (offsetVector.y * offsetScale);
			double vZ = start.z + (direction.z * i * lineLength) + (offsetVector.z * offsetScale);
			addVertex(vX, vY, vZ, false);
		}
		addVertex(start.x + direction.x * steps * lineLength, start.y + direction.y * steps * lineLength, start.z + direction.z * steps * lineLength, true);
	}

	private static Vector3d generateOffsetVector(Vector3d direction, Vector3d directionOffset) {
		return VectorUtil.rotateAroundAnyAxis(direction, directionOffset, RANDOM.nextFloat() * 360.0D);
	}

	/*
	 * private static void renderSingleElectricLine(BufferBuilder builder, Tessellator tess, Vec3d start, Vec3d direction,
	 * double lineLength, Random rng, double pointVariation) {
	 * Vec3d end = start.add(direction.normalize().scale(lineLength));
	 * renderSingleElectricLineBetween(builder, tess, start, end, pointVariation, rng);
	 * }
	 */

	public static void renderElectricFieldWithSizeOfEntityAt(Entity entity, double x, double y, double z, int bolts, long seed) {
		renderElectricField(entity.getBbWidth() / 2, entity.getBbHeight() / 2, x, y + entity.getBbHeight() / 2, z, bolts, seed);
	}

}
