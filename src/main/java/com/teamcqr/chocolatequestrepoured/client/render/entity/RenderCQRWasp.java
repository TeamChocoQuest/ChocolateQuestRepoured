package com.teamcqr.chocolatequestrepoured.client.render.entity;

import com.teamcqr.chocolatequestrepoured.client.models.entities.ModelCQRWasp;
import com.teamcqr.chocolatequestrepoured.objects.entity.misc.EntityCQRWasp;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderCQRWasp extends RenderLiving<EntityCQRWasp> {
	
	private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/entity/entity_wasp.png");

	public RenderCQRWasp(RenderManager renderManager) {
		this(renderManager, new ModelCQRWasp(0F), 0F);
	}
	
	public RenderCQRWasp(RenderManager rendermanagerIn, ModelBase modelbaseIn, float shadowsizeIn) {
		super(rendermanagerIn, modelbaseIn, shadowsizeIn);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityCQRWasp entity) {
		return TEXTURE;
	}

}
