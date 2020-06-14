package com.teamcqr.chocolatequestrepoured.client.render.entity.boss;

import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRGiantSpider;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderCQRGiantSpider extends RenderLiving<EntityCQRGiantSpider> {

	public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/entity/boss/giant_spider.png");
	
	public RenderCQRGiantSpider(RenderManager rendermanagerIn, ModelBase modelbaseIn, float shadowsizeIn) {
		super(rendermanagerIn, modelbaseIn, shadowsizeIn);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityCQRGiantSpider entity) {
		return TEXTURE;
	}

}
