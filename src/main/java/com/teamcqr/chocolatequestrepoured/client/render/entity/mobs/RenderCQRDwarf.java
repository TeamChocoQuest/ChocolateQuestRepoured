package com.teamcqr.chocolatequestrepoured.client.render.entity.mobs;

import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQREntity;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRDwarf;

import net.minecraft.client.renderer.entity.RenderManager;

public class RenderCQRDwarf extends RenderCQREntity<EntityCQRDwarf> {

	public RenderCQRDwarf(RenderManager rendermanagerIn) {
		super(rendermanagerIn, "mob/dwarf", 0.9D, 0.65D, true);
	}

}
