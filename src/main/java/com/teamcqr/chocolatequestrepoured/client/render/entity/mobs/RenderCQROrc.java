package com.teamcqr.chocolatequestrepoured.client.render.entity.mobs;

import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQREntity;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQROrc;

import net.minecraft.client.renderer.entity.RenderManager;

public class RenderCQROrc extends RenderCQREntity<EntityCQROrc> {

	public RenderCQROrc(RenderManager rendermanagerIn) {
		super(rendermanagerIn, "mob/orc", true);
	}

}
