package com.teamcqr.chocolatequestrepoured.client.render.entity;

import com.teamcqr.chocolatequestrepoured.client.models.entities.ModelCQRSpectre;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRSpectre;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;

public class RenderCQRSpectre extends RenderCQREntity<EntityCQRSpectre> {

	public RenderCQRSpectre(RenderManager rendermanagerIn) {
		this(rendermanagerIn, new ModelCQRSpectre(0,  false), 0.5F, "entity_mob_cqrspectre", 1.0D, 1.0D);
	}
	
	public RenderCQRSpectre(RenderManager rendermanagerIn, ModelBase model, float shadowSize, String entityName,
			double widthScale, double heightScale) {
		super(rendermanagerIn, model, shadowSize, entityName, widthScale, heightScale);
	}
	
	@Override
	public void doRender(EntityCQRSpectre entity, double x, double y, double z, float entityYaw, float partialTicks) {
		GlStateManager.pushMatrix();
		
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		
		GlStateManager.color(1.0F, 1.0F, 1.0F, 0.5F);
		
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		
		GlStateManager.disableAlpha();
		GlStateManager.disableBlend();
		
		GlStateManager.popMatrix();
	}

}
