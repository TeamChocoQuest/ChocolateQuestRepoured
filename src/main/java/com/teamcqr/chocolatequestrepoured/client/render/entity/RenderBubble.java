package com.teamcqr.chocolatequestrepoured.client.render.entity;

import org.lwjgl.opengl.GL11;

import com.teamcqr.chocolatequestrepoured.client.models.entities.ModelBubble;
import com.teamcqr.chocolatequestrepoured.objects.entity.misc.EntityBubble;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderBubble extends Render<EntityBubble> {

	protected final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/entity/bubble_entity.png");
	
	private final ModelBase model;
	
	public RenderBubble(RenderManager renderManager) {
		super(renderManager);
		this.model = new ModelBubble();
	}
	
	@Override
	public void doRenderShadowAndFire(Entity entityIn, double x, double y, double z, float yaw, float partialTicks) {
		//Supposed to be empty
	}
	
	
	@Override
	public void doRender(EntityBubble entity, double x, double y, double z, float entityYaw, float partialTicks) {
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x, (float) y - entity.height /2, (float) z);

		double scale = 2.0D + entity.getLowestRidingEntity().height * 0.7D;
		
		GL11.glScaled(scale, scale, scale);
		this.bindTexture(this.getEntityTexture(entity));
		this.model.render(entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		GlStateManager.popMatrix();
	}
	

	@Override
	protected ResourceLocation getEntityTexture(EntityBubble entity) {
		return TEXTURE;
	}

}
