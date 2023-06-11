package team.cqr.cqrepoured.client.model.entity;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import net.minecraft.world.phys.Vec3;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.renderer.RenderType;
import com.mojang.math.Vector3f;

public class ModelPentagram {

	private static boolean initialized;
	private static int vertexBuffer;
	private static int indexBuffer;

	private static void init() {
		if (!initialized) {
			FloatBuffer vertexByteBuffer = ByteBuffer.allocateDirect(96).order(ByteOrder.nativeOrder()).asFloatBuffer();
			vertexByteBuffer.put(new float[] { 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 1, 1, 1, 0, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1 });
			vertexByteBuffer.rewind();
			ByteBuffer indexByteBuffer = ByteBuffer.allocateDirect(14).order(ByteOrder.nativeOrder());
			indexByteBuffer.put(new byte[] { 0, 4, 1, 5, 7, 4, 6, 0, 2, 1, 3, 7, 2, 6 });
			indexByteBuffer.rewind();

			vertexBuffer = GL15.glGenBuffers();
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBuffer);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexByteBuffer, GL15.GL_STATIC_DRAW);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

			indexBuffer = GL15.glGenBuffers();
			GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
			GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexByteBuffer, GL15.GL_STATIC_DRAW);
			GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

			initialized = true;
		}
	}

	@SuppressWarnings("deprecation")
	public static void render(PoseStack matrix, RenderType renderType, int corners, float radius, float lineWidth, float lineHeight) {
		if (!initialized) {
			init();
		}

		renderType.setupRenderState();

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBuffer);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		GL20.glEnableVertexAttribArray(0);
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);

		float f1 = 360.0F / corners;
		Vec3 v1 = new Vec3(0.0D, 0.0D, radius + lineWidth * 0.5F);
		Vec3 v2 = v1.yRot((float) Math.toRadians(f1));
		float f2 = (float) v1.distanceTo(v2);

		float f3 = f1 * (corners / 2);
		Vec3 v3 = new Vec3(0.0D, 0.0D, radius);
		Vec3 v4 = v3.yRot((float) Math.toRadians(f3));
		float f4 = (float) v3.distanceTo(v4);

		for (int i = 0; i < corners; i++) {
			matrix.pushPose();
			matrix.mulPose(Vector3f.YP.rotationDegrees(f1 * i));
			matrix.translate(0F, 0F, radius);

			matrix.pushPose();
			matrix.translate(0.0F, 0.0F, lineWidth * 0.5F);
			matrix.mulPose(Vector3f.YP.rotationDegrees(90.0F + f1 * 0.5F));
			matrix.translate(0.0F, -lineHeight * 0.5F, 0.0F);
			matrix.scale(lineWidth, lineHeight, f2);

			GL11.glPushMatrix();
			GL11.glLoadIdentity();
			RenderSystem.multMatrix(matrix.last().pose());
			GL11.glDrawElements(GL11.GL_TRIANGLE_STRIP, 14, GL11.GL_UNSIGNED_BYTE, 0);
			GL11.glPopMatrix();

			matrix.popPose();

			matrix.pushPose();
			matrix.mulPose(Vector3f.YP.rotationDegrees(90.0F + f3 * 0.5F));
			matrix.translate(-lineWidth * 0.5F, -lineHeight * 0.5F, 0.0F);
			matrix.scale(lineWidth, lineHeight, f4);

			GL11.glPushMatrix();
			GL11.glLoadIdentity();
			RenderSystem.multMatrix(matrix.last().pose());
			GL11.glDrawElements(GL11.GL_TRIANGLE_STRIP, 14, GL11.GL_UNSIGNED_BYTE, 0);
			GL11.glPopMatrix();

			matrix.popPose();

			matrix.popPose();
		}

		GL20.glDisableVertexAttribArray(0);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		renderType.clearRenderState();
	}

}
