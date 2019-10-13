package com.teamcqr.chocolatequestrepoured.client.render.entity.layers;

import org.lwjgl.opengl.GL11;

import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQREntity;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class LayerCQRSpeechbubble extends AbstractLayerCQR {

	private static final int CHANGE_BUBBLE_INTERVAL = 50;
	
	public LayerCQRSpeechbubble(RenderCQREntity<?> livingEntityRendererIn) {
		super(livingEntityRendererIn);
		//System.out.println("NEW INSTANCE");
	}
	
	
	@Override
	public void doRenderLayer(AbstractEntityCQR entity, float limbSwing, float limbSwingAmount, float partialTicks,
			float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		if(entity.isChatting()) {
			Tessellator tessellator = Tessellator.getInstance();
			Minecraft minecraft = Minecraft.getMinecraft();
			
			GlStateManager.pushMatrix();
			GlStateManager.rotate(entity.rotationYawHead +90, 0.0F, 1.0F, 0.0F);
			GlStateManager.translate(-0.5, -1.7, 0);
			
			//System.out.println("Entity age: " + ageInTicks);
			if(ageInTicks % CHANGE_BUBBLE_INTERVAL == 0) {
				//currentBubble = ESpeechBubble.getRandom(entity.getRNG());
				entity.chooseNewRandomSpeechBubble();
				//System.out.println("Bubble: " + currentBubble.name());
			}
			
			//minecraft.renderEngine.bindTexture(currentBubble.getResourceLocation());
			minecraft.renderEngine.bindTexture(entity.getCurrentSpeechBubble().getResourceLocation());
			
			BufferBuilder buffer = tessellator.getBuffer();
			buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			buffer.pos(0,1,0).tex(0, 1).endVertex();
			buffer.pos(1,1,0).tex(1,1).endVertex();
			buffer.pos(1,0,0).tex(1,0).endVertex();
			buffer.pos(0,0,0).tex(0,0).endVertex();
			
			tessellator.draw();
			
			GlStateManager.popMatrix();
			
		}
		
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}

}
