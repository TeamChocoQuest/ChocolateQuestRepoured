package com.teamcqr.chocolatequestrepoured.client.render.tileentity;

import com.teamcqr.chocolatequestrepoured.tileentity.TileEntityExporter;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntityExporterRenderer extends TileEntitySpecialRenderer<TileEntityExporter> {

	//Code: see as how the TileEntityStructureRenderer does this
	/**
	 * //TODO: Use Tesselator to draw a box that contains the structure (Box texture like worldborder, but not that bold?)
			//https://www.minecraftforge.net/forum/topic/66168-1122-using-minecrafts-tessellator-and-bufferbuilder/
			//https://www.minecraftforge.net/forum/topic/41255-question-regarding-the-vertexbuffer-and-the-old-tesselator/
			//http://jabelarminecraft.blogspot.com/p/minecraft-forge-172-quick-tips-gl11-and.html
			
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder bufferBuilder = tessellator.getBuffer();
			
            GlStateManager.disableTexture2D();
			GlStateManager.disableBlend();
			
			GlStateManager.glLineWidth(2.0F);
			bufferBuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
			//Lines
			//Bottom lines
			bufferBuilder.pos((float)startX, (float)startY, (float)startZ).color(255, 0, 0, 0).endVertex();
			bufferBuilder.pos((float)endX, (float)startY, (float)startZ).color(255,0,0,0).endVertex();
			
			bufferBuilder.pos((float)startX, (float)startY, (float)endZ).color(255, 0, 0, 0).endVertex();
			bufferBuilder.pos((float)endX, (float)startY, (float)endZ).color(255,0,0,0).endVertex();
			
			bufferBuilder.pos((float)startX, (float)startY, (float)startZ).color(255, 0, 0, 0).endVertex();
			bufferBuilder.pos((float)startX, (float)startY, (float)endZ).color(255,0,0,0).endVertex();
			
			
			tessellator.draw();
			GlStateManager.glLineWidth(1.0f);
            GlStateManager.enableTexture2D();
            GlStateManager.enableBlend();
	 */
	
	@Override
	public void render(TileEntityExporter te, double x, double y, double z, float partialTicks, int destroyStage,
			float alpha) {
		super.render(te, x, y, z, partialTicks, destroyStage, alpha);
		
		//I could also use the RenderGlobal class ...
	}

}
