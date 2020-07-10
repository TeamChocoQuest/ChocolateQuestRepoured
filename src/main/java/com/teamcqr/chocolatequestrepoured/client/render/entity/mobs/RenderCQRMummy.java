package com.teamcqr.chocolatequestrepoured.client.render.entity.mobs;

import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQREntity;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRMummy;

import net.minecraft.client.renderer.entity.RenderManager;

public class RenderCQRMummy extends RenderCQREntity<EntityCQRMummy> {

	public RenderCQRMummy(RenderManager rendermanagerIn) {
		super(rendermanagerIn, "mob/mummy", true);
	}

}
