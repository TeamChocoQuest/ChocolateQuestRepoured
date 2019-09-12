package com.teamcqr.chocolatequestrepoured.client.render.entity;

import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRGoblin;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderManager;

public class RenderCQRGoblin extends RenderCQREntity<EntityCQRGoblin> {

	public RenderCQRGoblin(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelBiped(), 0.5F, "entity_mob_cqrgoblin", 1.0D, 1.0D);
	}

}
