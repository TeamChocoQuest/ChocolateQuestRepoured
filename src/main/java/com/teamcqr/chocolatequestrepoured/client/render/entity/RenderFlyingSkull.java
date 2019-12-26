package com.teamcqr.chocolatequestrepoured.client.render.entity;

import com.teamcqr.chocolatequestrepoured.client.models.entities.ModelFlyingSkull;
import com.teamcqr.chocolatequestrepoured.objects.entity.misc.EntityFlyingSkullMinion;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderFlyingSkull extends Render<EntityFlyingSkullMinion> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/entity/flying_skull.png");
	private final ModelBase model;

	public RenderFlyingSkull(RenderManager renderManager) {
		super(renderManager);
		this.model = new ModelFlyingSkull(0F);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityFlyingSkullMinion entity) {
		return TEXTURE;
	}
	
	@Override
	public void doRender(EntityFlyingSkullMinion entity, double x, double y, double z, float entityYaw,
			float partialTicks) {
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x, (float) y, (float) z);
		this.bindTexture(getEntityTexture(entity));
		this.model.render(entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		GlStateManager.popMatrix();
	}
	
	@Override
	public void doRenderShadowAndFire(Entity entityIn, double x, double y, double z, float yaw, float partialTicks) {
		//Should be empty!!
	}
}
