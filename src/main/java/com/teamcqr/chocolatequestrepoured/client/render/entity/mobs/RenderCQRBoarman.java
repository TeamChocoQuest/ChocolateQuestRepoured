package com.teamcqr.chocolatequestrepoured.client.render.entity.mobs;

import com.teamcqr.chocolatequestrepoured.client.models.entities.mobs.ModelCQRBoarman;
import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQREntity;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRBoarman;

import net.minecraft.client.renderer.entity.RenderManager;

public class RenderCQRBoarman extends RenderCQREntity<EntityCQRBoarman> {

	public RenderCQRBoarman(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelCQRBoarman(), 0.5F, "mob/boarman", 1.0D, 1.0D);
	}

}
