package com.teamcqr.chocolatequestrepoured.client.render.entity.layers;

import org.lwjgl.opengl.GL11;

import com.teamcqr.chocolatequestrepoured.client.init.ESpeechBubble;
import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQREntity;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class LayerCQRSpeechbubble extends AbstractLayerCQR {

	private ModelRenderer bipedHead;
	private ESpeechBubble currentBubble;
	
	private static final int CHANGE_BUBBLE_INTERVAL = 50;
	
	public LayerCQRSpeechbubble(RenderCQREntity<?> livingEntityRendererIn, ModelRenderer headBox) {
		super(livingEntityRendererIn);
		this.bipedHead = headBox;
		this.currentBubble = ESpeechBubble.ITEM_BEER;
	}
	
	
	@Override
	public void doRenderLayer(AbstractEntityCQR entity, float limbSwing, float limbSwingAmount, float partialTicks,
			float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		if(entity.isChatting()) {
			super.doRenderLayer(entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
			
			Tessellator tessellator = Tessellator.getInstance();
			Minecraft minecraft = Minecraft.getMinecraft();
			
			EntityPlayerSP player = minecraft.player;
			
			double diffX = (entity.prevPosX + (entity.posX - entity.prevPosX)) - (player.prevPosX +(player.posX - player.prevPosX));
			double diffY = (entity.prevPosY + (entity.posY - entity.prevPosY)) - (player.prevPosY +(player.posY - player.prevPosY));
			double diffZ = (entity.prevPosZ + (entity.posZ - entity.prevPosZ)) - (player.prevPosZ +(player.posZ - player.prevPosZ));
			
			GL11.glPushMatrix();
			GL11.glTranslated(diffX, diffY +3, diffZ);
			GL11.glRotatef(-entity.rotationYaw, 0.0F, 1.0F, 0.0F);
			GL11.glPushMatrix();
			
			GL11.glRotatef(-90, 1, 0, 0);
			GL11.glTranslated(0, -1, 0);
			
			if(ageInTicks % CHANGE_BUBBLE_INTERVAL == 0) {
				currentBubble = ESpeechBubble.getRandom(entity.getRNG());
			}
			
			minecraft.renderEngine.bindTexture(currentBubble.getResourceLocation());
			
			BufferBuilder buffer = tessellator.getBuffer();
			buffer.begin(3, DefaultVertexFormats.POSITION_COLOR);
			buffer.addVertexData(new int[] {-1, 1, 1, 0, 0});
			buffer.addVertexData(new int[] {-1, 1, -1, 0, 1});
			buffer.addVertexData(new int[] {1, 1, -1, 1, 1});
			buffer.addVertexData(new int[] {1, 1, 1, 1, 0});
			
			buffer.addVertexData(new int[] {-1, 1, 1, 0, 0});
			buffer.addVertexData(new int[] {1, 1, 1, 1, 0});
			buffer.addVertexData(new int[] {1, 1, -1, 1, 1});
			buffer.addVertexData(new int[] {-1, 1, -1, 0, 1});
			
			tessellator.draw();
			
			GL11.glPopMatrix();
			GL11.glPopMatrix();
			
		}
		
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}

}
