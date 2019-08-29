package com.teamcqr.chocolatequestrepoured.client.render.tileentity;

import com.teamcqr.chocolatequestrepoured.tileentity.TileEntityExporter;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntityExporterRenderer extends TileEntitySpecialRenderer<TileEntityExporter> {

	// Code: see as how the TileEntityStructureRenderer does this
	/**
	 * //TODO: Use Tesselator to draw a box that contains the structure (Box texture
	 * like worldborder, but not that bold?)
	 * //https://www.minecraftforge.net/forum/topic/66168-1122-using-minecrafts-tessellator-and-bufferbuilder/
	 * //https://www.minecraftforge.net/forum/topic/41255-question-regarding-the-vertexbuffer-and-the-old-tesselator/
	 * //http://jabelarminecraft.blogspot.com/p/minecraft-forge-172-quick-tips-gl11-and.html
	 * 
	 * Tessellator tessellator = Tessellator.getInstance(); BufferBuilder
	 * bufferBuilder = tessellator.getBuffer();
	 * 
	 * GlStateManager.disableTexture2D(); GlStateManager.disableBlend();
	 * 
	 * GlStateManager.glLineWidth(2.0F); bufferBuilder.begin(3,
	 * DefaultVertexFormats.POSITION_COLOR); //Lines //Bottom lines
	 * bufferBuilder.pos((float)startX, (float)startY, (float)startZ).color(255, 0,
	 * 0, 0).endVertex(); bufferBuilder.pos((float)endX, (float)startY,
	 * (float)startZ).color(255,0,0,0).endVertex();
	 * 
	 * bufferBuilder.pos((float)startX, (float)startY, (float)endZ).color(255, 0, 0,
	 * 0).endVertex(); bufferBuilder.pos((float)endX, (float)startY,
	 * (float)endZ).color(255,0,0,0).endVertex();
	 * 
	 * bufferBuilder.pos((float)startX, (float)startY, (float)startZ).color(255, 0,
	 * 0, 0).endVertex(); bufferBuilder.pos((float)startX, (float)startY,
	 * (float)endZ).color(255,0,0,0).endVertex();
	 * 
	 * 
	 * tessellator.draw(); GlStateManager.glLineWidth(1.0f);
	 * GlStateManager.enableTexture2D(); GlStateManager.enableBlend();
	 */

	@Override
	public void render(TileEntityExporter te, double x, double y, double z, float partialTicks, int destroyStage,
			float alpha) {
		// timerTillRenderUpdate++;
		// if(timerTillRenderUpdate >= 400) {
		// timerTillRenderUpdate = 0;
		
		if (te.startX == te.endX && te.startY == te.endY && te.startZ == te.endZ) {
			// System.out.println("No Data on Client!");
			return;
		}
		//super.render(te, x, y, z, partialTicks, destroyStage, alpha);
		
		//Problem: This is CLIENT-SIDE, the data lies SERVER-SIDE -> No Data here
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
		GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
		GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.glLineWidth(8.0F); GlStateManager.disableTexture2D();
		GlStateManager.depthMask(false);
		 
		RenderGlobal.renderFilledBox(te.getSelectionAABB(), 0.0f, 0.0f, 1.0f, 0.5f);
		 
		GlStateManager.depthMask(true); 
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
		 

		/*
		 * System.out.println("x1: " + te.getSelectionAABB().minX);
		 * System.out.println("y1: " + te.getSelectionAABB().minY);
		 * System.out.println("z1: " + te.getSelectionAABB().minZ);
		 */

		// RenderGlobal.renderFilledBox(te.getSelectionAABB(), 0.0f, 0.0f, 1.0f, 0.5f);

		// I could also use the RenderGlobal class ...
		// }
		/*super.render(te, x, y, z, partialTicks, destroyStage, alpha);

		if (te.startX == te.endX && te.startY == te.endY && te.startZ == te.endZ) {
			// System.out.println("No Data on Client!");
			return;
		}

		BlockPos startPos = new BlockPos(te.startX, te.startY, te.startZ).subtract(te.getPos());
		BlockPos endPos = new BlockPos(te.endX, te.endY, te.endZ).subtract(te.getPos()).subtract(startPos);
		
		//System.out.println("block: " + te.getPos().toString());
		//System.out.println("start: " + startPos.toString());
		//System.out.println("end: " + endPos.toString());
		//System.out.println("X: " + x + "  Y: " + y + "  Z: " + z);

		double y1 = y + (double) startPos.getY() - 0.01D;
		double y2 = y1 + (double) endPos.getY() - 0.02D;

		double offsetEndX = (double) endPos.getX() + 0.02D;
		double offsetEndZ = (double) endPos.getZ() + 0.02D;

		double x1 = x+ (offsetEndX < 0.0D ? (double) startPos.getX() + 1.0D + 0.01D : (double) startPos.getX() - 0.01D);
		double x2 = x1 + offsetEndX;

		double z1 = z+ (offsetEndZ < 0.0D ? (double) startPos.getZ() + 1.0D + 0.01D : (double) startPos.getZ() - 0.01D);
		double z2 = z1 + offsetEndZ;

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

		this.renderBox(tessellator, bufferbuilder, x1, y1, z1, x2, y2, z2, 255, 223, 127);

		this.setLightmapDisabled(false);
		GlStateManager.glLineWidth(1.0F);
		GlStateManager.enableLighting();
		GlStateManager.enableTexture2D();
		GlStateManager.enableDepth();
		GlStateManager.depthMask(true);
		GlStateManager.enableFog();*/
	}

	private void renderBox(Tessellator tessellator, BufferBuilder buffer, double minX, double minY, double minZ,
			double maxX, double maxY, double maxZ, int clRed, int clGreen, int clBlue) {
		float clAlpha = 0.0f;
		GlStateManager.glLineWidth(2.0F);
		buffer.begin(3, DefaultVertexFormats.POSITION_COLOR);
		buffer.pos(minX, minY, minZ).color((float) clGreen, (float) clGreen, (float) clGreen, clAlpha).endVertex();
		clAlpha = clRed;
		buffer.pos(minX, minY, minZ).color(clGreen, clGreen, clGreen, clAlpha).endVertex();
		buffer.pos(maxX, minY, minZ).color(clGreen, clBlue, clBlue, clAlpha).endVertex();
		buffer.pos(maxX, minY, maxZ).color(clGreen, clGreen, clGreen, clAlpha).endVertex();
		buffer.pos(minX, minY, maxZ).color(clGreen, clGreen, clGreen, clAlpha).endVertex();
		buffer.pos(minX, minY, minZ).color(clBlue, clBlue, clBlue, clAlpha).endVertex();
		buffer.pos(minX, maxY, minZ).color(clBlue, clGreen, clBlue, clAlpha).endVertex();
		buffer.pos(maxX, maxY, minZ).color(clGreen, clGreen, clGreen, clAlpha).endVertex();
		buffer.pos(maxX, maxY, maxZ).color(clGreen, clGreen, clGreen, clAlpha).endVertex();
		buffer.pos(minX, maxY, maxZ).color(clGreen, clGreen, clGreen, clAlpha).endVertex();
		buffer.pos(minX, maxY, minZ).color(clGreen, clGreen, clGreen, clAlpha).endVertex();
		buffer.pos(minX, maxY, maxZ).color(clGreen, clGreen, clGreen, clAlpha).endVertex();
		buffer.pos(minX, minY, maxZ).color(clGreen, clGreen, clGreen, clAlpha).endVertex();
		buffer.pos(maxX, minY, maxZ).color(clGreen, clGreen, clGreen, clAlpha).endVertex();
		buffer.pos(maxX, maxY, maxZ).color(clGreen, clGreen, clGreen, clAlpha).endVertex();
		buffer.pos(maxX, maxY, minZ).color(clGreen, clGreen, clGreen, clAlpha).endVertex();
		buffer.pos(maxX, minY, minZ).color(clGreen, clGreen, clGreen, clAlpha).endVertex();
		clAlpha = 0.0F;
		buffer.pos(maxX, minY, minZ).color((float) clGreen, (float) clGreen, (float) clGreen, clAlpha).endVertex();
		tessellator.draw();
		GlStateManager.glLineWidth(1.0F);
	}
	
	@Override
	public boolean isGlobalRenderer(TileEntityExporter te) {
		return true;
	}

}
