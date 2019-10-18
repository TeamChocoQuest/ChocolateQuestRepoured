package com.teamcqr.chocolatequestrepoured.client.render.entity;

import java.util.ArrayList;
import java.util.List;

import com.teamcqr.chocolatequestrepoured.client.models.entities.ModelCQRMonkey;
import com.teamcqr.chocolatequestrepoured.client.models.entities.customarmor.ModelCQRMonkeyArmor;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRMandril;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;

public class RenderCQRMandril extends RenderCQREntity<EntityCQRMandril> {
	
	public RenderCQRMandril(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelCQRMonkey(0F), 0.5F, "entity_mob_cqrmandril", 1.0D, 1.0D);
		
		List<LayerRenderer<?>> toRemove = new ArrayList<LayerRenderer<?>>();
		for (LayerRenderer<?> layer : this.layerRenderers) {
			if (layer instanceof LayerBipedArmor ) {
				toRemove.add(layer);
			}
		}
		for (LayerRenderer<?> layer : toRemove) {
			this.layerRenderers.remove(layer);
		}
		
		this.addLayer(new LayerBipedArmor(this) {
			@Override
			protected void initArmor() {
				this.modelLeggings = new ModelCQRMonkeyArmor(0.5F);
				this.modelArmor = new ModelCQRMonkeyArmor(1.0F);
			}
		});
	}

}
