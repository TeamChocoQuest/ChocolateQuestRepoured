package com.teamcqr.chocolatequestrepoured.client.render.entity;

import com.teamcqr.chocolatequestrepoured.client.models.entities.ModelSummoningCircle;
import com.teamcqr.chocolatequestrepoured.objects.entity.misc.EntitySummoningCircle;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderSummoningCircle extends Render<EntitySummoningCircle> {

	public static final ResourceLocation[] TEXTURES = new ResourceLocation[] {
		new ResourceLocation(Reference.MODID, "textures/entity/summoning_circles/zombie.png"),
		new ResourceLocation(Reference.MODID, "textures/entity/summoning_circles/skeleton.png"),
		new ResourceLocation(Reference.MODID, "textures/entity/summoning_circles/flying_skull.png"),
		new ResourceLocation(Reference.MODID, "textures/entity/summoning_circles/flying_sword.png"),
		new ResourceLocation(Reference.MODID, "textures/entity/summoning_circles/meteor.png")
	};
	
	private final ModelBase model;
	
	public RenderSummoningCircle(RenderManager renderManager) {
		super(renderManager);
		this.model = new ModelSummoningCircle(0);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntitySummoningCircle entity) {
		if(entity.getTextureID() >= TEXTURES.length) {
			return TEXTURES[0];
		}
		else return TEXTURES[entity.getTextureID()];
	}
	
	@Override
	public void doRender(EntitySummoningCircle entity, double x, double y, double z, float entityYaw,
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
