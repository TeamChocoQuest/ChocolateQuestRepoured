package com.teamcqr.chocolatequestrepoured.client.render.entity;

import java.util.ArrayList;
import java.util.List;

import com.teamcqr.chocolatequestrepoured.client.models.entities.ModelCQRTriton;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRTriton;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;

public class RenderCQRTriton extends RenderCQREntity<EntityCQRTriton> {

	public RenderCQRTriton(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelCQRTriton(0), 0.5F, "entity_mob_cqrtriton", 1.0D, 1.0D);
		
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
				this.modelLeggings = new ModelBiped(0.5F);
				this.modelLeggings.setVisible(false);
				this.modelArmor = new ModelBiped(1.0F);
			}
			
			@Override
			public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount,
					float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
				super.doRenderLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch,
						scale);
				this.modelLeggings.setVisible(false);
			}
			
		});
	}

}
