package team.cqr.cqrepoured.client.render.tileentity;

import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.client.init.CQRRenderTypes;
import team.cqr.cqrepoured.tileentity.TileEntityExporter;

@OnlyIn(Dist.CLIENT)
public class TileEntityExporterRenderer implements BlockEntityRenderer<TileEntityExporter> {

	public TileEntityExporterRenderer(BlockEntityRenderDispatcher blockEntityRendererDispatcher) {
		super();
	}

	@Override
	public void render(TileEntityExporter pBlockEntity, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pCombinedLight, int pCombinedOverlay) {
		BlockPos pos1 = pBlockEntity.getMinPosRelative();
		BlockPos pos2 = pBlockEntity.getMaxPosRelative();

		double x1 = pos1.getX() - 0.01D;
		double y1 = pos1.getY() - 0.01D;
		double z1 = pos1.getZ() - 0.01D;
		double x2 = pos2.getX() + 1.01D;
		double y2 = pos2.getY() + 1.01D;
		double z2 = pos2.getZ() + 1.01D;

		LevelRenderer.renderLineBox(pMatrixStack, pBuffer.getBuffer(RenderType.lines()), x1, y1, z1, x2, y2, z2, 0.9F, 0.9F, 0.9F, 1.0F, 0.5F, 0.5F, 0.5F);

		this.renderUnprotectedBlocks(pBlockEntity, pMatrixStack, pBuffer);
	}

	private void renderUnprotectedBlocks(TileEntityExporter pBlockEntity, PoseStack pMatrixStack, MultiBufferSource pBuffer) {
		BlockPos pos = pBlockEntity.getBlockPos();
		double d1 = 1.0D / 1024.0D;
		double d2 = 1.0D + d1;
		double d3 = 1.0D / 512.0D;
		double d4 = 1.0D + d3;

		VertexConsumer vertexBuilder = pBuffer.getBuffer(CQRRenderTypes.overlayQuads());
		for (BlockPos pos1 : pBlockEntity.getUnprotectedBlocks()) {
			int dx = pos1.getX() - pos.getX();
			int dy = pos1.getY() - pos.getY();
			int dz = pos1.getZ() - pos.getZ();
			renderBox(pMatrixStack, vertexBuilder, dx - d1, dy - d1, dz - d1, dx + d2, dy + d2, dz + d2, 1.0F, 0.8F, 0.0F, 0.35F);
		}

		vertexBuilder = pBuffer.getBuffer(CQRRenderTypes.overlayLines());
		for (BlockPos pos1 : pBlockEntity.getUnprotectedBlocks()) {
			int dx = pos1.getX() - pos.getX();
			int dy = pos1.getY() - pos.getY();
			int dz = pos1.getZ() - pos.getZ();
			renderBox(pMatrixStack, vertexBuilder, dx - d3, dy - d3, dz - d3, dx + d4, dy + d4, dz + d4, 1.0F, 0.8F, 0.0F, 1.0F);
		}
	}

	public static void renderBox(PoseStack matrixStack, VertexConsumer vertexBuilder, double x1, double y1, double z1, double x2, double y2, double z2, float red, float green, float blue, float alpha) {
		Matrix4f matrix = matrixStack.last().pose();
		// down
		vertexBuilder.vertex(matrix, (float) x1, (float) y1, (float) z1).color(red, green, blue, alpha).endVertex();
		vertexBuilder.vertex(matrix, (float) x2, (float) y1, (float) z1).color(red, green, blue, alpha).endVertex();
		vertexBuilder.vertex(matrix, (float) x2, (float) y1, (float) z2).color(red, green, blue, alpha).endVertex();
		vertexBuilder.vertex(matrix, (float) x1, (float) y1, (float) z2).color(red, green, blue, alpha).endVertex();

		// up
		vertexBuilder.vertex(matrix, (float) x1, (float) y2, (float) z2).color(red, green, blue, alpha).endVertex();
		vertexBuilder.vertex(matrix, (float) x2, (float) y2, (float) z2).color(red, green, blue, alpha).endVertex();
		vertexBuilder.vertex(matrix, (float) x2, (float) y2, (float) z1).color(red, green, blue, alpha).endVertex();
		vertexBuilder.vertex(matrix, (float) x1, (float) y2, (float) z1).color(red, green, blue, alpha).endVertex();

		// north
		vertexBuilder.vertex(matrix, (float) x2, (float) y1, (float) z1).color(red, green, blue, alpha).endVertex();
		vertexBuilder.vertex(matrix, (float) x1, (float) y1, (float) z1).color(red, green, blue, alpha).endVertex();
		vertexBuilder.vertex(matrix, (float) x1, (float) y2, (float) z1).color(red, green, blue, alpha).endVertex();
		vertexBuilder.vertex(matrix, (float) x2, (float) y2, (float) z1).color(red, green, blue, alpha).endVertex();

		// south
		vertexBuilder.vertex(matrix, (float) x1, (float) y1, (float) z2).color(red, green, blue, alpha).endVertex();
		vertexBuilder.vertex(matrix, (float) x2, (float) y1, (float) z2).color(red, green, blue, alpha).endVertex();
		vertexBuilder.vertex(matrix, (float) x2, (float) y2, (float) z2).color(red, green, blue, alpha).endVertex();
		vertexBuilder.vertex(matrix, (float) x1, (float) y2, (float) z2).color(red, green, blue, alpha).endVertex();

		// west
		vertexBuilder.vertex(matrix, (float) x1, (float) y1, (float) z1).color(red, green, blue, alpha).endVertex();
		vertexBuilder.vertex(matrix, (float) x1, (float) y1, (float) z2).color(red, green, blue, alpha).endVertex();
		vertexBuilder.vertex(matrix, (float) x1, (float) y2, (float) z2).color(red, green, blue, alpha).endVertex();
		vertexBuilder.vertex(matrix, (float) x1, (float) y2, (float) z1).color(red, green, blue, alpha).endVertex();

		// east
		vertexBuilder.vertex(matrix, (float) x2, (float) y1, (float) z2).color(red, green, blue, alpha).endVertex();
		vertexBuilder.vertex(matrix, (float) x2, (float) y1, (float) z1).color(red, green, blue, alpha).endVertex();
		vertexBuilder.vertex(matrix, (float) x2, (float) y2, (float) z1).color(red, green, blue, alpha).endVertex();
		vertexBuilder.vertex(matrix, (float) x2, (float) y2, (float) z2).color(red, green, blue, alpha).endVertex();
	}

	@Override
	public boolean shouldRenderOffScreen(TileEntityExporter pTe) {
		return true;
	}

}
