package com.teamcqr.chocolatequestrepoured.client.render.entity.mounts;

import com.teamcqr.chocolatequestrepoured.client.models.entities.ModelCQRPollo;
import com.teamcqr.chocolatequestrepoured.objects.mounts.EntityPollo;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderPollo extends RenderLiving<EntityPollo>{
	
	static ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/entity/mounts/pollo.png");

	public RenderPollo(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelCQRPollo(), 0.5F);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityPollo entity) {
		return TEXTURE;
	}

}
