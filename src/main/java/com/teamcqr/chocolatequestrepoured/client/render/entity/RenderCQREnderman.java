package com.teamcqr.chocolatequestrepoured.client.render.entity;

import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQREnderman;

import net.minecraft.client.model.ModelEnderman;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;

public class RenderCQREnderman extends RenderCQREntity<EntityCQREnderman> {

	public RenderCQREnderman(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelEnderman(0.0F), 0.5F, "entity_mob_cqrenderman", 1.0D, 1.0D);
		try {
			for(LayerRenderer layer : this.layerRenderers) {
				if(layer instanceof LayerHeldItem) {
					this.layerRenderers.remove(layer);
				}
			}
			//TODO: Add enderman equipment layer
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

}
