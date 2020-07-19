package com.teamcqr.chocolatequestrepoured.client.render.entity.boss;

import com.teamcqr.chocolatequestrepoured.client.render.entity.layers.LayerCQRNecromancerBoneShield;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRNecromancer;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderManager;

public class RenderCQRNecromancer extends RenderCQRMage<EntityCQRNecromancer> {

	public RenderCQRNecromancer(RenderManager rendermanagerIn, ModelBiped model, String entityName) {
		super(rendermanagerIn, model, entityName);
		this.addLayer(new LayerCQRNecromancerBoneShield(this));
	}

}
