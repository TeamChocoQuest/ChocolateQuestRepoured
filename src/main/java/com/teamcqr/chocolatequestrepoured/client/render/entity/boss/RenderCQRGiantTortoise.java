package com.teamcqr.chocolatequestrepoured.client.render.entity.boss;

import com.teamcqr.chocolatequestrepoured.client.models.entities.boss.ModelGiantTortoise;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRGiantTortoise;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderCQRGiantTortoise extends RenderLiving<EntityCQRGiantTortoise> {

	public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/entity/boss/giant_tortoise.png");

	public RenderCQRGiantTortoise(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelGiantTortoise(), 1.5F);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityCQRGiantTortoise entity) {
		return TEXTURE;
	}

}
