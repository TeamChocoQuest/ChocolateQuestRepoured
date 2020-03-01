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
		GlStateManager.color(1.0F, 1.0F, 1.0F, 0.5F);
		GlStateManager.enableNormalize();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.translate((float) x, (float) y /*- 2 * entity.height + 0.5*entity.getPassengerHeight()*/, (float) z);

		float scale = 2.25F + entity.getPassengerHeight();
		
		GL11.glScalef(scale, scale, scale);
		this.bindTexture(this.getEntityTexture(entity));
		this.model.render(entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		GlStateManager.disableBlend();
		GlStateManager.disableNormalize();
		GlStateManager.popMatrix();
	}
	

	@Override
	protected ResourceLocation getEntityTexture(EntityBubble entity) {
		return TEXTURE;
	}

}
