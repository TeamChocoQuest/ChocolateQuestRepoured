package com.teamcqr.chocolatequestrepoured.client.render.entity;

import com.teamcqr.chocolatequestrepoured.client.models.entities.ModelCQRBoarman;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRPigman;

import net.minecraft.client.renderer.entity.RenderManager;

public class RenderCQRBoarman extends RenderCQREntity<EntityCQRPigman> {

	public RenderCQRBoarman(RenderManager rendermanagerIn, String entityName) {
		super(rendermanagerIn, new ModelCQRBoarman(0F), 0.5F, entityName, 1.0D, 1.0D);
	}

}
