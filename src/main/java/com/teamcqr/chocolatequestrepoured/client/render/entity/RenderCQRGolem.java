package com.teamcqr.chocolatequestrepoured.client.render.entity;

import java.util.ArrayList;
import java.util.List;

import com.teamcqr.chocolatequestrepoured.client.models.entities.ModelCQRGolem;
import com.teamcqr.chocolatequestrepoured.client.models.entities.customarmor.ModelCQRGolemSmallArmor;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRGolem;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;

public class RenderCQRGolem extends RenderCQREntity<EntityCQRGolem> {

	public RenderCQRGolem(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelCQRGolem(0F), 0.5F, "entity_mob_cqrgolemsmall", 1.0D, 1.0D);

		List<LayerRenderer<?>> toRemove = new ArrayList<LayerRenderer<?>>();
		for (LayerRenderer<?> layer : this.layerRenderers) {
			if (layer instanceof LayerBipedArmor) {
				toRemove.add(layer);
			}
		}
		for (LayerRenderer<?> layer : toRemove) {
			this.layerRenderers.remove(layer);
		}

		this.addLayer(new LayerBipedArmor(this) {
			@Override
			protected void initArmor() {
				this.modelLeggings = new ModelCQRGolemSmallArmor(0.5F);
				this.modelArmor = new ModelCQRGolemSmallArmor(1.0F);
			}
		});
	}

}
