package com.teamcqr.chocolatequestrepoured.client.render.entity;

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
		// Supposed to be empty
	}

	@Override
	public void doRender(EntityBubble entity, double x, double y, double z, float entityYaw, float partialTicks) {
		if (entity.isBeingRidden()) {
			GlStateManager.pushAttrib();
			GlStateManager.enableAlpha();
			GlStateManager.enableBlend();
			GlStateManager.disableCull();
			GlStateManager.pushMatrix();

			GlStateManager.color(1.0F, 1.0F, 1.0F, 0.5F);
			GlStateManager.translate(x, y, z);
			float scale = entity.height / 0.875F;
			GlStateManager.scale(-scale, -scale, scale);
			this.bindTexture(this.getEntityTexture(entity));
			this.model.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);

			GlStateManager.popMatrix();
			GlStateManager.popAttrib();
		}
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityBubble entity) {
		return this.TEXTURE;
	}

}
