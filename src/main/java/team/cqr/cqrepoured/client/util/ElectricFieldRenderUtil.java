package team.cqr.cqrepoured.client.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Random;

import org.joml.Matrix4f;
import org.joml.Vector3d;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.system.MemoryUtil;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferBuilder.DrawState;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.datafixers.util.Pair;

import meldexun.randomutil.FastRandom;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.entity.PartEntity;
import software.bernie.shadowed.eliotlash.mclib.utils.MathHelper;
import team.cqr.cqrepoured.client.init.CQRRenderTypes;
import team.cqr.cqrepoured.util.VectorUtil;

public class ElectricFieldRenderUtil {

	private static final Random RANDOM = new FastRandom();
	private static final Tessellator TESSELATOR = Tessellator.getInstance();
	private static final BufferBuilder VERTEX_BUFFER = TESSELATOR.getBuilder();
	private static final IntBuffer FIRST_BUFFER = ByteBuffer.allocateDirect(64).order(ByteOrder.nativeOrder()).asIntBuffer();
	private static final IntBuffer COUNT_BUFFER = ByteBuffer.allocateDirect(64).order(ByteOrder.nativeOrder()).asIntBuffer();
	private static int vertexCount;

	private static void startLineStripBatch(VertexFormat format) {
		VERTEX_BUFFER.begin(GL11.GL_LINE_STRIP, format);
	}

	private static void addVertex(Matrix4f matrix, float x, float y, float z, boolean endLineStrip) {
		VERTEX_BUFFER.vertex(matrix, x, y, z).endVertex();
		vertexCount++;

		if (endLineStrip) {
			FIRST_BUFFER.put(VERTEX_BUFFER.vertices - vertexCount);
			COUNT_BUFFER.put(vertexCount);
			vertexCount = 0;
		}
	}

	private static void endLineStripBatch(RenderType renderType) {
		if (vertexCount > 0) {
			throw new IllegalStateException("Last line strip not finished!");
		}

		VERTEX_BUFFER.end();
		FIRST_BUFFER.flip();
		COUNT_BUFFER.flip();

		renderType.setupRenderState();
		Pair<DrawState, ByteBuffer> drawInfo = VERTEX_BUFFER.popNextBuffer();
		VertexFormat format = VERTEX_BUFFER.getVertexFormat();
		format.setupBufferState(MemoryUtil.memAddress(drawInfo.getSecond()));

		GL14.glMultiDrawArrays(GL11.GL_LINE_STRIP, FIRST_BUFFER, COUNT_BUFFER);

		format.clearBufferState();
		renderType.clearRenderState();

		VERTEX_BUFFER.clear();
		FIRST_BUFFER.clear();
		COUNT_BUFFER.clear();
	}

	public static void renderElectricField(MatrixStack matrix, IRenderTypeBuffer buffer, double fieldRadius, double fieldHeight, int bolts, long seed) {
		RANDOM.setSeed(seed);

		// Initialize the drawing process, our vertices consist of positions and color information
		startLineStripBatch(DefaultVertexFormats.POSITION);

		// Now we want to draw <boltCount> lightnings
		Matrix4f matrix4f = matrix.last().pose();
		for (int boltCount = 0; boltCount < bolts; ++boltCount) {
			// The steps defines the "segments" of the lightning
			int steps = RANDOM.nextInt(26) + 5;

			// now, actually calculate the position of each "segment"
			for (int i = 0; i <= steps; ++i) {

				// Apply some "noise" so it looks "electric" (it will jitter when rendering)
				float vX = (RANDOM.nextFloat() * 2F - 1F) * (float)fieldRadius /* + (rng.nextFloat() - 0.5D) * (fieldRadius / 2) */;
				float vZ = (RANDOM.nextFloat() * 2F - 1F) * (float)fieldRadius /* + (rng.nextFloat() - 0.5D) * (fieldRadius / 2) */;
				float vY = (RANDOM.nextFloat() * 2F - 1F) * (float)fieldHeight /* + (rng.nextFloat() - 0.5D) * (fieldHeight / 2) */;

				// Finally, create the vertex
				addVertex(matrix4f, vX, vY, vZ, i == steps);
			}
		}

		// Draw the lightning
		endLineStripBatch(CQRRenderTypes.lineStrip(3.0D));
	}

	/*
	 * X, Y, Z are the weird xyz from the rendering stuff in the entities
	 */
	public static void renderElectricLineBetween(MatrixStack matrix, IRenderTypeBuffer buffer, Vector3d startOffset, Vector3d endOffset, double maxOffset, int boltCount, long seed) {
		RANDOM.setSeed(seed);

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
			renderSingleElectricLineBetween(matrix, startOffset, direction, directionOffset, lineLength, steps, maxOffset);
		}
		
		// Draw the lightning
		endLineStripBatch(CQRRenderTypes.lineStrip(3.0D));
	}

	private static void renderSingleElectricLineBetween(MatrixStack matrix, Vector3d start, Vector3d direction, Vector3d directionOffset, double lineLength, int steps, double offset) {
		Matrix4f matrix4f = matrix.last().pose();
		addVertex(matrix4f, (float) start.x, (float) start.y, (float) start.z, false);
		for (int i = 1; i < steps; i++) {
			Vector3d offsetVector = generateOffsetVector(direction, directionOffset);
			double offsetScale = RANDOM.nextFloat() * offset;
			double vX = start.x + (direction.x * i * lineLength) + (offsetVector.x * offsetScale);
			double vY = start.y + (direction.y * i * lineLength) + (offsetVector.y * offsetScale);
			double vZ = start.z + (direction.z * i * lineLength) + (offsetVector.z * offsetScale);
			addVertex(matrix4f, (float) vX, (float) vY, (float) vZ, false);
		}
		double eX = start.x + direction.x * steps * lineLength;
		double eY = start.y + direction.y * steps * lineLength;
		double eZ = start.z + direction.z * steps * lineLength;
		addVertex(matrix4f, (float) eX, (float) eY, (float) eZ, true);
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

	public static void renderElectricFieldWithSizeOfEntityAt(MatrixStack matrix, IRenderTypeBuffer buffer, Entity entity, int bolts, long seed) {
		matrix.pushPose();
		//TODO: Fix offset on y axis
		if(entity instanceof PartEntity<?>) {
			
		} else {
			matrix.translate(0, entity.getBbHeight() / 2, 0);
		}
		renderElectricField(matrix, buffer, entity.getBbWidth() / 2, entity.getBbHeight() / 2, bolts, seed);
		matrix.popPose();
	}

}
