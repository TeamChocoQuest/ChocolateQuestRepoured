package team.cqr.cqrepoured.client.render.tileentity;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;
import team.cqr.cqrepoured.tileentity.TileEntityExporter;

@OnlyIn(Dist.CLIENT)
public class TileEntityExporterRenderer extends TileEntityRenderer<TileEntityExporter> {

	@Override
	public void render(TileEntityExporter te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		super.render(te, x, y, z, partialTicks, destroyStage, alpha);

		BlockPos tileEntityPos = te.getPos();
		BlockPos pos1 = te.getMinPosRelative();
		BlockPos pos2 = te.getMaxPosRelative();

		double x1 = x + pos1.getX() - 0.01D;
		double y1 = y + pos1.getY() - 0.01D;
		double z1 = z + pos1.getZ() - 0.01D;
		double x2 = x + pos2.getX() + 1.01D;
		double y2 = y + pos2.getY() + 1.01D;
		double z2 = z + pos2.getZ() + 1.01D;

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();

		{
			// render structure outline
			GlStateManager.disableFog();
			GlStateManager.disableLighting();
			GlStateManager.disableTexture2D();
			this.setLightmapDisabled(true);

			this.renderBox(tessellator, bufferbuilder, x1, y1, z1, x2, y2, z2);

			this.setLightmapDisabled(false);
			GlStateManager.enableTexture2D();
			GlStateManager.enableLighting();
			GlStateManager.enableFog();
		}

		{
			// render unprotected blocks
			double d1 = 1.0D / 1024.0D;
			double d2 = 1.0D + d1;
			double d3 = 1.0D / 512.0D;
			double d4 = 1.0D + d1;

			GlStateManager.glLineWidth(2.0F);
			GlStateManager.disableFog();
			GlStateManager.disableLighting();
			GlStateManager.disableTexture2D();
			GlStateManager.depthMask(false);
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
			this.setLightmapDisabled(true);

			GlStateManager.color(1.0F, 0.8F, 0.0F, 0.35F);
			bufferbuilder.setTranslation(x - tileEntityPos.getX(), y - tileEntityPos.getY(), z - tileEntityPos.getZ());
			bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
			for (BlockPos pos : te.getUnprotectedBlocks()) {
				this.renderBox(bufferbuilder, pos.getX() - d1, pos.getY() - d1, pos.getZ() - d1, pos.getX() + d2, pos.getY() + d2, pos.getZ() + d2);
			}
			tessellator.draw();
			GlStateManager.color(1.0F, 0.8F, 0.0F, 1.0F);
			bufferbuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
			for (BlockPos pos : te.getUnprotectedBlocks()) {
				this.renderBoxOutline(bufferbuilder, pos.getX() - d3, pos.getY() - d3, pos.getZ() - d3, pos.getX() + d4, pos.getY() + d4, pos.getZ() + d4);
			}
			tessellator.draw();
			bufferbuilder.setTranslation(0.0D, 0.0D, 0.0D);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

			this.setLightmapDisabled(false);
			GlStateManager.disableBlend();
			GlStateManager.depthMask(true);
			GlStateManager.enableTexture2D();
			GlStateManager.enableLighting();
			GlStateManager.enableFog();
			GlStateManager.glLineWidth(1.0F);
		}
	}

	private void renderBox(Tessellator tessellator, BufferBuilder buffer, double x1, double y1, double z1, double x2, double y2, double z2) {
		int cl1 = 255;
		int cl2 = 223;
		int cl3 = 127;

		GlStateManager.glLineWidth(2.0F);
		buffer.begin(3, DefaultVertexFormats.POSITION_COLOR);
		buffer.pos(x1, y1, z1).color(cl2, cl2, cl2, 0.0F).endVertex();
		buffer.pos(x1, y1, z1).color(cl2, cl2, cl2, cl1).endVertex();
		buffer.pos(x2, y1, z1).color(cl2, cl3, cl3, cl1).endVertex();
		buffer.pos(x2, y1, z2).color(cl2, cl2, cl2, cl1).endVertex();
		buffer.pos(x1, y1, z2).color(cl2, cl2, cl2, cl1).endVertex();
		buffer.pos(x1, y1, z1).color(cl3, cl3, cl2, cl1).endVertex();
		buffer.pos(x1, y2, z1).color(cl3, cl2, cl3, cl1).endVertex();
		buffer.pos(x2, y2, z1).color(cl2, cl2, cl2, cl1).endVertex();
		buffer.pos(x2, y2, z2).color(cl2, cl2, cl2, cl1).endVertex();
		buffer.pos(x1, y2, z2).color(cl2, cl2, cl2, cl1).endVertex();
		buffer.pos(x1, y2, z1).color(cl2, cl2, cl2, cl1).endVertex();
		buffer.pos(x1, y2, z2).color(cl2, cl2, cl2, cl1).endVertex();
		buffer.pos(x1, y1, z2).color(cl2, cl2, cl2, cl1).endVertex();
		buffer.pos(x2, y1, z2).color(cl2, cl2, cl2, cl1).endVertex();
		buffer.pos(x2, y2, z2).color(cl2, cl2, cl2, cl1).endVertex();
		buffer.pos(x2, y2, z1).color(cl2, cl2, cl2, cl1).endVertex();
		buffer.pos(x2, y1, z1).color(cl2, cl2, cl2, cl1).endVertex();
		buffer.pos(x2, y1, z1).color(cl2, cl2, cl2, 0.0F).endVertex();
		tessellator.draw();
		GlStateManager.glLineWidth(1.0F);
	}

	private void renderBoxOutline(BufferBuilder buffer, double x1, double y1, double z1, double x2, double y2, double z2) {
		buffer.pos(x1, y1, z1).endVertex();
		buffer.pos(x2, y1, z1).endVertex();
		buffer.pos(x2, y1, z1).endVertex();
		buffer.pos(x2, y1, z2).endVertex();
		buffer.pos(x2, y1, z2).endVertex();
		buffer.pos(x1, y1, z2).endVertex();
		buffer.pos(x1, y1, z2).endVertex();
		buffer.pos(x1, y1, z1).endVertex();

		buffer.pos(x1, y1, z1).endVertex();
		buffer.pos(x1, y2, z1).endVertex();
		buffer.pos(x1, y1, z2).endVertex();
		buffer.pos(x1, y2, z2).endVertex();
		buffer.pos(x2, y1, z1).endVertex();
		buffer.pos(x2, y2, z1).endVertex();
		buffer.pos(x2, y1, z2).endVertex();
		buffer.pos(x2, y2, z2).endVertex();

		buffer.pos(x1, y2, z2).endVertex();
		buffer.pos(x2, y2, z2).endVertex();
		buffer.pos(x2, y2, z2).endVertex();
		buffer.pos(x2, y2, z1).endVertex();
		buffer.pos(x2, y2, z1).endVertex();
		buffer.pos(x1, y2, z1).endVertex();
		buffer.pos(x1, y2, z1).endVertex();
		buffer.pos(x1, y2, z2).endVertex();
	}

	private void renderBox(BufferBuilder buffer, double x1, double y1, double z1, double x2, double y2, double z2) {
		// down
		buffer.pos(x1, y1, z1).endVertex();
		buffer.pos(x2, y1, z1).endVertex();
		buffer.pos(x2, y1, z2).endVertex();
		buffer.pos(x1, y1, z2).endVertex();

		// south
		buffer.pos(x1, y1, z2).endVertex();
		buffer.pos(x2, y1, z2).endVertex();
		buffer.pos(x2, y2, z2).endVertex();
		buffer.pos(x1, y2, z2).endVertex();

		// north
		buffer.pos(x2, y1, z1).endVertex();
		buffer.pos(x1, y1, z1).endVertex();
		buffer.pos(x1, y2, z1).endVertex();
		buffer.pos(x2, y2, z1).endVertex();

		// up
		buffer.pos(x1, y2, z2).endVertex();
		buffer.pos(x2, y2, z2).endVertex();
		buffer.pos(x2, y2, z1).endVertex();
		buffer.pos(x1, y2, z1).endVertex();

		// west
		buffer.pos(x1, y1, z1).endVertex();
		buffer.pos(x1, y1, z2).endVertex();
		buffer.pos(x1, y2, z2).endVertex();
		buffer.pos(x1, y2, z1).endVertex();

		// east
		buffer.pos(x2, y1, z2).endVertex();
		buffer.pos(x2, y1, z1).endVertex();
		buffer.pos(x2, y2, z1).endVertex();
		buffer.pos(x2, y2, z2).endVertex();
	}

	@Override
	public boolean isGlobalRenderer(TileEntityExporter te) {
		return true;
	}

}
