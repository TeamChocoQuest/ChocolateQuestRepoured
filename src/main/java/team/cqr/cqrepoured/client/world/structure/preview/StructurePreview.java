package team.cqr.cqrepoured.client.world.structure.preview;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.vecmath.Matrix4f;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import it.unimi.dsi.fastutil.ints.AbstractIntComparator;
import it.unimi.dsi.fastutil.ints.IntArrays;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraftforge.client.ForgeHooksClient;
import team.cqr.cqrepoured.world.structure.generation.structurefile.CQStructure;

public class StructurePreview {

	private static final Map<Path, StructurePreview> INSTANCES = new HashMap<>();

	private final Path file;
	private final StructureBoundingBox boundingBox;
	private final VertexFormat format;
	private final Map<BlockRenderLayer, VertexBuffer> vertexBuffers;

	private final IndexType translucentIndexType;
	private final float[] translucentQuadPositions;
	private final float[] translucentQuadDistances;
	private final int[] translucentQuadIndices;
	private final int[] translucentQuadIndicesSort;
	private final ByteBuffer translucentIndexBuffer;

	private static class VertexBuffer {

		private int vbo;
		private int vertices;

		private VertexBuffer(int vbo, int vertices) {
			this.vbo = vbo;
			this.vertices = vertices;
		}

		public static VertexBuffer of(BufferBuilder data) {
			int vbo = GL15.glGenBuffers();
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data.getByteBuffer(), GL15.GL_STATIC_DRAW);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
			return new VertexBuffer(vbo, data.getVertexCount());
		}

		public int vbo() {
			return this.vbo;
		}

		public int vertices() {
			return this.vertices;
		}

		public void dispose() {
			GL15.glDeleteBuffers(this.vbo);
			this.vbo = -1;
			this.vertices = 0;
		}

	}

	public StructurePreview(Path file, StructureBoundingBox boundingBox, VertexFormat format, Map<BlockRenderLayer, BufferBuilder> vertexData) {
		this.file = file;
		this.boundingBox = boundingBox;
		this.format = format;
		this.vertexBuffers = vertexData.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> VertexBuffer.of(e.getValue()), (v1, v2) -> { throw new UnsupportedOperationException(); }, () -> new EnumMap<>(BlockRenderLayer.class)));

		if (vertexData.containsKey(BlockRenderLayer.TRANSLUCENT)) {
			BufferBuilder translucentBuffer = vertexData.get(BlockRenderLayer.TRANSLUCENT);
			ByteBuffer translucentVertexData = translucentBuffer.getByteBuffer();
			int translucentQuads = translucentBuffer.getVertexCount() / 4;
			this.translucentIndexType = IndexType.forIndexCount(translucentBuffer.getVertexCount());
			this.translucentQuadPositions = new float[translucentQuads * 3];
			this.translucentQuadDistances = new float[translucentQuads];
			this.translucentQuadIndices = IntStream.range(0, translucentQuads).toArray();
			this.translucentQuadIndicesSort = new int[translucentQuads];
			this.translucentIndexBuffer = ByteBuffer.allocateDirect(translucentBuffer.getVertexCount() * this.translucentIndexType.bytes()).order(ByteOrder.nativeOrder());

			for (int i = 0; i < translucentQuads; i++) {
				for (int j = 0; j < 3; j++) {
					this.translucentQuadPositions[i * 3 + j] = (translucentVertexData.getFloat((i * 4 + 0) * format.getSize() + j * 4)
							+ translucentVertexData.getFloat((i * 4 + 1) * format.getSize() + j * 4)
							+ translucentVertexData.getFloat((i * 4 + 2) * format.getSize() + j * 4)
							+ translucentVertexData.getFloat((i * 4 + 3) * format.getSize() + j * 4)) * 0.25F;
				}
			}
		} else {
			this.translucentIndexType = null;
			this.translucentQuadPositions = null;
			this.translucentQuadDistances = null;
			this.translucentQuadIndices = null;
			this.translucentQuadIndicesSort = null;
			this.translucentIndexBuffer = null;
		}
	}

	public static StructurePreview of(Path file) {
		return INSTANCES.computeIfAbsent(file.toAbsolutePath(), k -> {
			StructureBoundingBox boundingBox = StructureBoundingBox.getNewBoundingBox();
			Map<BlockRenderLayer, BufferBuilder> meshBuilders = new EnumMap<>(BlockRenderLayer.class);

			CQStructure structure = CQStructure.createFromFile(k.toFile());
			StructureBlockAccess structureBlockAccess = new StructureBlockAccess(structure);
			structure.forEachBlock((posInfo, pos) -> {
				IBlockState state = posInfo.getRenderState();
				if (state.getBlock() == Blocks.AIR) {
					return;
				}

				for (BlockRenderLayer layer : BlockRenderLayer.values()) {
					if (!state.getBlock().canRenderInLayer(state, layer)) continue;
					BufferBuilder meshBuilder = meshBuilders.computeIfAbsent(layer, k1 -> {
						BufferBuilder v = new BufferBuilder(1 << 16);
						v.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
						return v;
					});
					ForgeHooksClient.setRenderLayer(layer);
					if (Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlock(state, pos, structureBlockAccess, meshBuilder)) {
						boundingBox.minX = Math.min(boundingBox.minX, pos.getX());
						boundingBox.minY = Math.min(boundingBox.minY, pos.getY());
						boundingBox.minZ = Math.min(boundingBox.minZ, pos.getZ());
						boundingBox.maxX = Math.max(boundingBox.maxX, pos.getX());
						boundingBox.maxY = Math.max(boundingBox.maxY, pos.getY());
						boundingBox.maxZ = Math.max(boundingBox.maxZ, pos.getZ());
					}
					ForgeHooksClient.setRenderLayer(null);
				}
			});

			meshBuilders.values().forEach(BufferBuilder::finishDrawing);

			return new StructurePreview(k, boundingBox, DefaultVertexFormats.BLOCK, meshBuilders);
		});
	}

	public void sortTranslucent(Matrix4f modelMatrix) {
		if (!this.vertexBuffers.containsKey(BlockRenderLayer.TRANSLUCENT)) {
			return;
		}

		// update quad distances
		for (int i = 0; i < this.translucentQuadDistances.length; i++) {
			this.translucentQuadDistances[i] = modelMatrix.m02 * this.translucentQuadPositions[i * 3 + 0]
					+ modelMatrix.m12 * this.translucentQuadPositions[i * 3 + 1]
					+ modelMatrix.m22 * this.translucentQuadPositions[i * 3 + 2]
					+ modelMatrix.m32;
		}

		// sort quad indices
		System.arraycopy(this.translucentQuadIndices, 0, this.translucentQuadIndicesSort, 0, this.translucentQuadIndices.length);
		IntArrays.mergeSort(this.translucentQuadIndices, 0, this.translucentQuadIndices.length, new AbstractIntComparator() {
			@Override
			public int compare(int k1, int k2) {
				return Float.compare(StructurePreview.this.translucentQuadDistances[k2], StructurePreview.this.translucentQuadDistances[k1]);
			}
		}, this.translucentQuadIndicesSort);

		// update index buffer
		switch (this.translucentIndexType) {
		case BYTE:
			for (int i = 0; i < this.translucentQuadIndices.length; i++) {
				this.translucentIndexBuffer.put((i * 4 + 0) * this.translucentIndexType.bytes(), (byte) (this.translucentQuadIndices[i] * 4 + 0));
				this.translucentIndexBuffer.put((i * 4 + 1) * this.translucentIndexType.bytes(), (byte) (this.translucentQuadIndices[i] * 4 + 1));
				this.translucentIndexBuffer.put((i * 4 + 2) * this.translucentIndexType.bytes(), (byte) (this.translucentQuadIndices[i] * 4 + 2));
				this.translucentIndexBuffer.put((i * 4 + 3) * this.translucentIndexType.bytes(), (byte) (this.translucentQuadIndices[i] * 4 + 3));
			}
			break;
		case SHORT:
			for (int i = 0; i < this.translucentQuadIndices.length; i++) {
				this.translucentIndexBuffer.putShort((i * 4 + 0) * this.translucentIndexType.bytes(), (short) (this.translucentQuadIndices[i] * 4 + 0));
				this.translucentIndexBuffer.putShort((i * 4 + 1) * this.translucentIndexType.bytes(), (short) (this.translucentQuadIndices[i] * 4 + 1));
				this.translucentIndexBuffer.putShort((i * 4 + 2) * this.translucentIndexType.bytes(), (short) (this.translucentQuadIndices[i] * 4 + 2));
				this.translucentIndexBuffer.putShort((i * 4 + 3) * this.translucentIndexType.bytes(), (short) (this.translucentQuadIndices[i] * 4 + 3));
			}
			break;
		case INT:
			for (int i = 0; i < this.translucentQuadIndices.length; i++) {
				this.translucentIndexBuffer.putInt((i * 4 + 0) * this.translucentIndexType.bytes(), this.translucentQuadIndices[i] * 4 + 0);
				this.translucentIndexBuffer.putInt((i * 4 + 0) * this.translucentIndexType.bytes(), this.translucentQuadIndices[i] * 4 + 1);
				this.translucentIndexBuffer.putInt((i * 4 + 0) * this.translucentIndexType.bytes(), this.translucentQuadIndices[i] * 4 + 2);
				this.translucentIndexBuffer.putInt((i * 4 + 0) * this.translucentIndexType.bytes(), this.translucentQuadIndices[i] * 4 + 3);
			}
			break;
		}
	}

	public void draw() {
		TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
		textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

		GlStateManager.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GlStateManager.glEnableClientState(GL11.GL_COLOR_ARRAY);
		GlStateManager.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);

		this.vertexBuffers.forEach((layer, vertexBuffer) -> {
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBuffer.vbo());
			GlStateManager.glVertexPointer(3, GL11.GL_FLOAT, this.format.getSize(), 0);
			GlStateManager.glColorPointer(4, GL11.GL_UNSIGNED_BYTE, this.format.getSize(), 12);
			GlStateManager.glTexCoordPointer(2, GL11.GL_FLOAT, this.format.getSize(), 16);

			switch (layer) {
			case SOLID:
				GlStateManager.disableAlpha();
				GL11.glDrawArrays(GL11.GL_QUADS, 0, vertexBuffer.vertices());
				GlStateManager.enableAlpha();
				break;
			case CUTOUT_MIPPED:
				GL11.glDrawArrays(GL11.GL_QUADS, 0, vertexBuffer.vertices());
				break;
			case CUTOUT:
				textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
				GL11.glDrawArrays(GL11.GL_QUADS, 0, vertexBuffer.vertices());
				textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, true);
				break;
			case TRANSLUCENT:
				GlStateManager.enableBlend();
				GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
				GL11.glDrawElements(GL11.GL_QUADS, vertexBuffer.vertices(), this.translucentIndexType.value(), this.translucentIndexBuffer);
				GlStateManager.disableBlend();
				break;
			default:
				this.drawCustomLayer(layer, vertexBuffer);
				break;
			}
		});

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		GlStateManager.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		GlStateManager.glDisableClientState(GL11.GL_COLOR_ARRAY);
		GlStateManager.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
	}

	private void drawCustomLayer(BlockRenderLayer layer, VertexBuffer vertexBuffer) {
		GL11.glDrawArrays(GL11.GL_QUADS, 0, vertexBuffer.vertices());
	}

	public void dispose() {
		this.vertexBuffers.values().forEach(VertexBuffer::dispose);
		this.vertexBuffers.clear();
		INSTANCES.remove(this.file, this);
	}

	public StructureBoundingBox boundingBox() {
		return this.boundingBox;
	}

}
