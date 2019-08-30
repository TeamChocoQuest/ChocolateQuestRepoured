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

		//Both positions are relative coordinates, the first one is the vector from the block's position to the lower corner of the selection
		//The second position is a vector from the lower corner to the upper corner of the selection -> structures size!
		BlockPos startPos = te.getStructurePosVector();//new BlockPos(te.startX, te.startY, te.startZ).subtract(te.getPos());
		BlockPos endPos = te.getStructureSize();//new BlockPos(te.endX, te.endY, te.endZ).subtract(te.getPos()).subtract(startPos);
		
		if(endPos == null || startPos == null || !(endPos.getX() > 0 && endPos.getY() > 0 && endPos.getZ() > 0)) {
			return;
		}
		
		//System.out.println("block: " + te.getPos().toString());
		//System.out.println("start: " + startPos.toString());
		//System.out.println("end: " + endPos.toString());
		//System.out.println("X: " + x + "  Y: " + y + "  Z: " + z);

		//System.out.println("Pos 1: " + startPos.toString());
		//System.out.println("Pos 2: " + endPos.toString());
		
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

		this.renderBox(tessellator, bufferbuilder, x1, y1, z1, x2, y2, z2);
		
		this.setLightmapDisabled(false);
		GlStateManager.glLineWidth(1.0F);
		GlStateManager.enableLighting();
		GlStateManager.enableTexture2D();
		GlStateManager.enableDepth();
		GlStateManager.depthMask(true);
		GlStateManager.enableFog();
		//GlStateManager.disableBlend();
	}

	private void renderBox(Tessellator tessellator, BufferBuilder buffer, double x1, double y1, double z1, double x2, double y2, double z2)
    {
		int cl1 = 255;
		int cl2 = 223;
		int cl3 = 127;
				
        GlStateManager.glLineWidth(2.0F);
        buffer.begin(3, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(x1, y1, z1).color((float)cl2, (float)cl2, (float)cl2, 0.0F).endVertex();
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
        buffer.pos(x2, y1, z1).color((float)cl2, (float)cl2, (float)cl2, 0.0F).endVertex();
        tessellator.draw();
        GlStateManager.glLineWidth(1.5F);
    }
	
	@Override
	public boolean isGlobalRenderer(TileEntityExporter te) {
		return true;
	}
	
	

}
