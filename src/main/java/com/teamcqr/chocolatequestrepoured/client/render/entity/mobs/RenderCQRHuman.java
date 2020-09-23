package com.teamcqr.chocolatequestrepoured.client.render.entity.mobs;

import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQREntity;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRHuman;

import net.minecraft.client.renderer.entity.RenderManager;

public class RenderCQRHuman extends RenderCQREntity<EntityCQRHuman> {

	public RenderCQRHuman(RenderManager rendermanagerIn) {
		super(rendermanagerIn, "mob/human", true);
	}

}
