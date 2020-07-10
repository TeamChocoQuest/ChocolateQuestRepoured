package com.teamcqr.chocolatequestrepoured.client.render.entity.mobs;

import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQREntity;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRDummy;

import net.minecraft.client.renderer.entity.RenderManager;

public class RenderCQRDummy extends RenderCQREntity<EntityCQRDummy> {

	public RenderCQRDummy(RenderManager rendermanagerIn) {
		super(rendermanagerIn, "mob/dummy");
	}

}
