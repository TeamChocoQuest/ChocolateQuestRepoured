package com.teamcqr.chocolatequestrepoured.client.render.tileentity;

import com.teamcqr.chocolatequestrepoured.tileentity.TileEntityExporter;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntityExporterRenderer extends TileEntitySpecialRenderer<TileEntityExporter> {

	@Override
	public void render(TileEntityExporter te, double x, double y, double z, float partialTicks, int destroyStage,
			float alpha) {
		super.render(te, x, y, z, partialTicks, destroyStage, alpha);

		BlockPos pos1 = te.getRenderMinPos();
		BlockPos pos2 = te.getRenderMaxPos();

		double x1 = x + pos1.getX() - 0.01D;
		double y1 = y + pos1.getY() - 0.01D;
		double z1 = z + pos1.getZ() - 0.01D;
		double x2 = x + pos2.getX() + 1.01D;
		double y2 = y + pos2.getY() + 1.01D;
		double z2 = z + pos2.getZ() + 1.01D;

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		GlStateManager.disableFog();
		GlStateManager.disableLighting();
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
				GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
				GlStateManager.DestFactor.ZERO);
		this.setLightmapDisabled(true);

		this.renderBox(tessellator, bufferbuilder, x1, y1, z1, x2, y2, z2);

		this.setLightmapDisabled(false);
		GlStateManager.glLineWidth(1.0F);
		GlStateManager.enableLighting();
		GlStateManager.enableTexture2D();
		GlStateManager.enableDepth();
		GlStateManager.depthMask(true);
		GlStateManager.enableFog();
		// GlStateManager.disableBlend();
	}

	private void renderBox(Tessellator tessellator, BufferBuilder buffer, double x1, double y1, double z1, double x2,
			double y2, double z2) {
		int cl1 = 255;
		int cl2 = 223;
		int cl3 = 127;

		GlStateManager.glLineWidth(2.0F);
		buffer.begin(3, DefaultVertexFormats.POSITION_COLOR);
		buffer.pos(x1, y1, z1).color((float) cl2, (float) cl2, (float) cl2, 0.0F).endVertex();
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
		buffer.pos(x2, y1, z1).color((float) cl2, (float) cl2, (float) cl2, 0.0F).endVertex();
		tessellator.draw();
		GlStateManager.glLineWidth(2.0F);
	}

	@Override
	public boolean isGlobalRenderer(TileEntityExporter te) {
		return true;
	}

}
