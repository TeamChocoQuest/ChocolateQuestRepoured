package team.cqr.cqrepoured.client.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.client.render.shader.ResourceSupplier;
import team.cqr.cqrepoured.client.render.shader.ShaderProgram;
import team.cqr.cqrepoured.client.render.texture.CubemapTexture;
import team.cqr.cqrepoured.util.Reference;

public class SphereRenderer {

	private static final List<Quad> CUBE;
	static {
		ArrayList<Quad> list = new ArrayList<>();
		double d = Math.sqrt(1.0D / 3.0D);
		Vertex v0 = new Vertex(-d, -d, -d);
		Vertex v1 = new Vertex(-d, -d, d);
		Vertex v2 = new Vertex(-d, d, -d);
		Vertex v3 = new Vertex(-d, d, d);
		Vertex v4 = new Vertex(d, -d, -d);
		Vertex v5 = new Vertex(d, -d, d);
		Vertex v6 = new Vertex(d, d, -d);
		Vertex v7 = new Vertex(d, d, d);

		// top
		list.add(new Quad(v3, v7, v6, v2));
		// bottom
		list.add(new Quad(v0, v4, v5, v1));
		// left
		list.add(new Quad(v0, v1, v3, v2));
		// front
		list.add(new Quad(v1, v5, v7, v3));
		// right
		list.add(new Quad(v5, v4, v6, v7));
		// back
		list.add(new Quad(v4, v0, v2, v6));

		list.trimToSize();
		CUBE = Collections.unmodifiableList(list);
	}
	private static final List<Triangle> ICOSAHEDRON;
	static {
		ArrayList<Triangle> list = new ArrayList<>();
		double goldenRatio = (1.0D + Math.sqrt(5.0D)) * 0.5D;
		double d1 = Math.sqrt(1.0D / (goldenRatio * goldenRatio + 1.0D));
		double d2 = goldenRatio * d1;
		Vertex v0 = new Vertex(-d1, d2, 0);
		Vertex v1 = new Vertex(d1, d2, 0);
		Vertex v2 = new Vertex(-d1, -d2, 0);
		Vertex v3 = new Vertex(d1, -d2, 0);
		Vertex v4 = new Vertex(0, -d1, d2);
		Vertex v5 = new Vertex(0, d1, d2);
		Vertex v6 = new Vertex(0, -d1, -d2);
		Vertex v7 = new Vertex(0, d1, -d2);
		Vertex v8 = new Vertex(d2, 0, -d1);
		Vertex v9 = new Vertex(d2, 0, d1);
		Vertex v10 = new Vertex(-d2, 0, -d1);
		Vertex v11 = new Vertex(-d2, 0, d1);

		list.add(new Triangle(v0, v11, v5));
		list.add(new Triangle(v0, v5, v1));
		list.add(new Triangle(v0, v1, v7));
		list.add(new Triangle(v0, v7, v10));
		list.add(new Triangle(v0, v10, v11));

		list.add(new Triangle(v1, v5, v9));
		list.add(new Triangle(v5, v11, v4));
		list.add(new Triangle(v11, v10, v2));
		list.add(new Triangle(v10, v7, v6));
		list.add(new Triangle(v7, v1, v8));

		list.add(new Triangle(v3, v9, v4));
		list.add(new Triangle(v3, v4, v2));
		list.add(new Triangle(v3, v2, v6));
		list.add(new Triangle(v3, v6, v8));
		list.add(new Triangle(v3, v8, v9));

		list.add(new Triangle(v4, v9, v5));
		list.add(new Triangle(v2, v4, v11));
		list.add(new Triangle(v6, v2, v10));
		list.add(new Triangle(v8, v6, v7));
		list.add(new Triangle(v9, v8, v1));

		list.trimToSize();
		ICOSAHEDRON = Collections.unmodifiableList(list);
	}
	private static final List<Triangle> OCTAHEDRON;
	static {
		ArrayList<Triangle> list = new ArrayList<>();
		Vertex v0 = new Vertex(-1, 0, 0);
		Vertex v1 = new Vertex(1, 0, 0);
		Vertex v2 = new Vertex(0, -1, 0);
		Vertex v3 = new Vertex(0, 1, 0);
		Vertex v4 = new Vertex(0, 0, -1);
		Vertex v5 = new Vertex(0, 0, 1);

		list.add(new Triangle(v5, v0, v2));
		list.add(new Triangle(v0, v4, v2));
		list.add(new Triangle(v4, v1, v2));
		list.add(new Triangle(v1, v5, v2));

		list.add(new Triangle(v5, v1, v3));
		list.add(new Triangle(v1, v4, v3));
		list.add(new Triangle(v4, v0, v3));
		list.add(new Triangle(v0, v5, v3));

		list.trimToSize();
		OCTAHEDRON = Collections.unmodifiableList(list);
	}
	private static final FloatBuffer BUFFER = ByteBuffer.allocateDirect(64).order(ByteOrder.nativeOrder()).asFloatBuffer();
	private static ShaderProgram shader;
	private static int uniformProjectionMatrix;
	private static int uniformModelViewMatrix;
	private static int uniformColor;
	private static int uniformTexture;
	private static int prevProgram;

	public static void init() {
		delete();
		shader = new ShaderProgram.Builder()
				.addShader(GL20.GL_VERTEX_SHADER, new ResourceSupplier(new ResourceLocation(Reference.MODID, "shaders/sphere/vertex.glsl")))
				.addShader(GL20.GL_FRAGMENT_SHADER, new ResourceSupplier(new ResourceLocation(Reference.MODID, "shaders/sphere/fragment.glsl"))).build();
		GL20.glUseProgram(shader.getShaderProgram());
		uniformProjectionMatrix = GL20.glGetUniformLocation(shader.getShaderProgram(), "projection");
		uniformModelViewMatrix = GL20.glGetUniformLocation(shader.getShaderProgram(), "modelView");
		uniformColor = GL20.glGetUniformLocation(shader.getShaderProgram(), "color");
		uniformTexture = GL20.glGetUniformLocation(shader.getShaderProgram(), "useTexture");
		GL20.glUseProgram(0);
	}

	private static void delete() {
		if (shader != null) {
			shader.delete();
			shader = null;
		}
	}

	public static Stream<Quad> getCubeSphere() {
		return CUBE.stream();
	}

	public static Stream<Triangle> getIcoSphere() {
		return ICOSAHEDRON.stream();
	}

	public static Stream<Triangle> getOctaSphere() {
		return OCTAHEDRON.stream();
	}

	/**
	 * Splits shapes n times into smaller shapes. Use normalize to create a sphere.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Shape> Function<T, Stream<T>> splitter(int splitCount, boolean normalize) {
		return shape -> {
			Stream<T> stream = Stream.of(shape);
			for (int i = 0; i < splitCount; i++) {
				stream = stream.flatMap(shape1 -> (Stream<T>) shape1.split(normalize));
			}
			return stream;
		};
	}

	/**
	 * Buffers all vertices of a shape.
	 */
	public static <T extends Shape> Consumer<T> defaultBufferer(BufferBuilder buffer) {
		return shape -> shape.vertices().forEach(vertex -> buffer.pos(vertex.x, vertex.y, vertex.z).endVertex());
	}

	/**
	 * Buffers lines of shape with the specified width. Use lod to create a smooth sphere.<br>
	 * Lines are buffered as quads!
	 */
	public static <T extends Shape> Consumer<T> lineBufferer(BufferBuilder buffer, double width, int lod) {
		if (lod < 0) {
			throw new IllegalArgumentException();
		}
		return shape -> {
			int lod1 = (int) Math.pow(4, lod);
			Vertex[] outer = shape.vertices().toArray(Vertex[]::new);
			Vertex[] outerDir = fill(new Vertex[outer.length], i -> next(outer, i).subtract(outer[i]));
			Vertex[] inner = fill(new Vertex[outer.length], i -> {
				Vertex v = outerDir[i].normalize().subtract(prev(outerDir, i).normalize()).normalize();
				double d = outerDir[i].normalize().dot(v);
				return outer[i].add(v.scale(width / Math.sqrt(1.0D - d * d))).normalize();
			});
			Vertex[] innerDir = fill(new Vertex[outer.length], i -> next(inner, i).subtract(inner[i]));
			Vertex[] outer1 = fill(new Vertex[outer.length * lod1], i -> {
				if (i % lod1 == 0) {
					return outer[i / lod1];
				}
				return outer[i / lod1].add(outerDir[i / lod1].scale((double) (i % lod1) / lod1)).normalize();
			});
			Vertex[] inner1 = fill(new Vertex[outer.length * lod1], i -> {
				if (i % lod1 == 0) {
					return inner[i / lod1];
				}
				return inner[i / lod1].add(innerDir[i / lod1].scale((double) (i % lod1) / lod1)).normalize();
			});
			for (int i = 0; i < outer.length; i++) {
				for (int j = 0; j < lod1; j++) {
					Vertex v0 = inner1[i * lod1 + j];
					Vertex v1 = outer1[i * lod1 + j];
					Vertex v2 = next(outer1, i * lod1 + j);
					Vertex v3 = next(inner1, i * lod1 + j);
					buffer.pos(v0.x, v0.y, v0.z).endVertex();
					buffer.pos(v1.x, v1.y, v1.z).endVertex();
					buffer.pos(v2.x, v2.y, v2.z).endVertex();
					buffer.pos(v3.x, v3.y, v3.z).endVertex();
				}
			}
		};
	}

	private static <T> T[] fill(T[] arr, IntFunction<T> func) {
		IntStream.range(0, arr.length).forEach(i -> arr[i] = func.apply(i));
		return arr;
	}

	private static <T> T next(T[] arr, int index) {
		return arr[(index + 1) % arr.length];
	}

	private static <T> T prev(T[] arr, int index) {
		return arr[(index + arr.length - 1) % arr.length];
	}

	public static void renderSphere(VertexBuffer buffer, int mode, @Nullable ResourceLocation textureLocation, boolean drawFront, boolean drawBack) {
		if (!drawFront && !drawBack) {
			return;
		}
		preDraw(textureLocation);

		buffer.bindBuffer();
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
		GL20.glEnableVertexAttribArray(0);

		if (drawBack) {
			GL11.glCullFace(GL11.GL_FRONT);
			buffer.drawArrays(mode);
			GL11.glCullFace(GL11.GL_BACK);
		}
		if (drawFront) {
			buffer.drawArrays(mode);
		}

		GL20.glDisableVertexAttribArray(0);
		buffer.unbindBuffer();

		postDraw();
	}

	public static void renderSphere(BufferBuilder buffer, int mode, @Nullable ResourceLocation textureLocation, boolean drawFront, boolean drawBack) {
		if (!drawFront && !drawBack) {
			return;
		}
		preDraw(textureLocation);

		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, buffer.getByteBuffer());
		GL20.glEnableVertexAttribArray(0);

		if (drawBack) {
			GL11.glCullFace(GL11.GL_FRONT);
			GL11.glDrawArrays(mode, 0, buffer.getVertexCount());
			GL11.glCullFace(GL11.GL_BACK);
		}
		if (drawFront) {
			GL11.glDrawArrays(mode, 0, buffer.getVertexCount());
		}

		GL20.glDisableVertexAttribArray(0);
		buffer.reset();

		postDraw();
	}

	private static void preDraw(@Nullable ResourceLocation textureLocation) {
		prevProgram = GL11.glGetInteger(GL20.GL_CURRENT_PROGRAM);
		GL20.glUseProgram(shader.getShaderProgram());

		GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, BUFFER);
		GL20.glUniformMatrix4(uniformProjectionMatrix, false, BUFFER);
		GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, BUFFER);
		GL20.glUniformMatrix4(uniformModelViewMatrix, false, BUFFER);
		GL11.glGetFloat(GL11.GL_CURRENT_COLOR, BUFFER);
		GL20.glUniform4f(uniformColor, BUFFER.get(0), BUFFER.get(1), BUFFER.get(2), BUFFER.get(3));

		if (textureLocation != null) {
			GL20.glUniform1i(uniformTexture, 1);
			Minecraft mc = Minecraft.getMinecraft();
			TextureManager textureManager = mc.getTextureManager();
			ITextureObject texture = textureManager.getTexture(CubemapTexture.get(textureLocation));
			GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture.getGlTextureId());
		} else {
			GL20.glUniform1i(uniformTexture, 0);
		}
	}

	private static void postDraw() {
		GL20.glUseProgram(prevProgram);
	}

	public static class Vertex {

		public final double x;
		public final double y;
		public final double z;

		public Vertex(double x, double y, double z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		public Vertex normalize() {
			double d = 1.0D / this.length();
			return new Vertex(this.x * d, this.y * d, this.z * d);
		}

		public Vertex add(Vertex vertex) {
			return new Vertex(this.x + vertex.x, this.y + vertex.y, this.z + vertex.z);
		}

		public Vertex subtract(Vertex vertex) {
			return new Vertex(this.x - vertex.x, this.y - vertex.y, this.z - vertex.z);
		}

		public Vertex scale(double d) {
			return new Vertex(this.x * d, this.y * d, this.z * d);
		}

		public double dot(Vertex vertex) {
			return this.x * vertex.x + this.y * vertex.y + this.z * vertex.z;
		}

		public double length() {
			return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
		}

		public Vertex cross(Vertex vertex) {
			return new Vertex(this.y * vertex.z - this.z * vertex.y, this.z * vertex.x - this.x * vertex.z, this.x * vertex.y - this.y * vertex.x);
		}

		public Vertex negate() {
			return new Vertex(-this.x, -this.y, -this.z);
		}

		public Vertex rotate(Vertex axis, double radian) {
			// setup quaternion
			double d = Math.sin(radian * 0.5D);
			double i = d * axis.x;
			double j = d * axis.y;
			double k = d * axis.z;
			double r = Math.cos(radian * 0.5D);

			// setup rotation matrix
			double d0 = 2.0D * i * i;
			double d1 = 2.0D * j * j;
			double d2 = 2.0D * k * k;
			double d3 = 2.0D * i * j;
			double d4 = 2.0D * j * k;
			double d5 = 2.0D * i * k;
			double d6 = 2.0D * r * i;
			double d7 = 2.0D * r * j;
			double d8 = 2.0D * r * k;

			double d00 = 1 - (d1 + d2);
			double d01 = (d3 - d8);
			double d02 = (d5 + d7);

			double d10 = (d3 + d8);
			double d11 = 1 - (d2 + d0);
			double d12 = (d4 - d6);

			double d20 = (d5 - d7);
			double d21 = (d4 + d6);
			double d22 = 1 - (d0 + d1);

			// rotate vertex
			return new Vertex(this.x * d00 + this.y * d01 + this.z * d02, this.x * d10 + this.y * d11 + this.z * d12,
					this.x * d20 + this.y * d21 + this.z * d22);
		}

	}

	public interface Shape {

		Stream<? extends Shape> split(boolean normalize);

		Stream<Vertex> vertices();

	}

	public static class Triangle implements Shape {

		public final Vertex v0;
		public final Vertex v1;
		public final Vertex v2;

		public Triangle(Vertex v0, Vertex v1, Vertex v2) {
			this.v0 = v0;
			this.v1 = v1;
			this.v2 = v2;
		}

		@Override
		public Stream<Triangle> split(boolean normalize) {
			Vertex v01 = this.v0.add(this.v1);
			Vertex v12 = this.v1.add(this.v2);
			Vertex v20 = this.v2.add(this.v0);
			if (normalize) {
				v01 = v01.normalize();
				v12 = v12.normalize();
				v20 = v20.normalize();
			} else {
				v01 = v01.scale(0.5D);
				v12 = v12.scale(0.5D);
				v20 = v20.scale(0.5D);
			}
			return Stream.of(new Triangle(v01, this.v1, v12), new Triangle(v12, this.v2, v20), new Triangle(v20, this.v0, v01), new Triangle(v20, v01, v12));
		}

		@Override
		public Stream<Vertex> vertices() {
			return Stream.of(this.v0, this.v1, this.v2);
		}

	}

	public static class Quad implements Shape {

		public final Vertex v0;
		public final Vertex v1;
		public final Vertex v2;
		public final Vertex v3;

		public Quad(Vertex v0, Vertex v1, Vertex v2, Vertex v3) {
			this.v0 = v0;
			this.v1 = v1;
			this.v2 = v2;
			this.v3 = v3;
		}

		@Override
		public Stream<Quad> split(boolean normalize) {
			Vertex v01 = this.v0.add(this.v1);
			Vertex v12 = this.v1.add(this.v2);
			Vertex v23 = this.v2.add(this.v3);
			Vertex v30 = this.v3.add(this.v0);
			Vertex v02 = this.v0.add(this.v2);
			if (normalize) {
				v01 = v01.normalize();
				v12 = v12.normalize();
				v23 = v23.normalize();
				v30 = v30.normalize();
				v02 = v02.normalize();
			} else {
				v01 = v01.scale(0.5D);
				v12 = v12.scale(0.5D);
				v23 = v23.scale(0.5D);
				v30 = v30.scale(0.5D);
				v02 = v02.scale(0.5D);
			}
			return Stream.of(new Quad(v30, this.v0, v01, v02), new Quad(v01, this.v1, v12, v02), new Quad(v12, this.v2, v23, v02),
					new Quad(v23, this.v3, v30, v02));
		}

		@Override
		public Stream<Vertex> vertices() {
			return Stream.of(this.v0, this.v1, this.v2, this.v3);
		}

	}

}
