package com.teamcqr.chocolatequestrepoured.client.render.entity.mobs;

import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQREntity;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRPirate;

import net.minecraft.client.renderer.entity.RenderManager;

public class RenderCQRPirate extends RenderCQREntity<EntityCQRPirate> {

	public RenderCQRPirate(RenderManager rendermanagerIn) {
		super(rendermanagerIn, "mob/pirate", true);
	}

}
