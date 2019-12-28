package com.teamcqr.chocolatequestrepoured.client.render.entity;

import com.teamcqr.chocolatequestrepoured.client.models.entities.ModelFlyingSkull;
import com.teamcqr.chocolatequestrepoured.objects.entity.misc.EntityFlyingSkullMinion;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderFlyingSkull extends RenderLiving<EntityFlyingSkullMinion> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/entity/flying_skull.png");

	public RenderFlyingSkull(RenderManager renderManager) {
		super(renderManager, new ModelFlyingSkull(0F), 0F);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityFlyingSkullMinion entity) {
		return TEXTURE;
	}
	
	@Override
	public void doRender(EntityFlyingSkullMinion entity, double x, double y, double z, float entityYaw,
			float partialTicks) {
		//this.mainModel.render(entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}
	
	@Override
	public void doRenderShadowAndFire(Entity entityIn, double x, double y, double z, float yaw, float partialTicks) {
		//Should be empty!!
	}
}
