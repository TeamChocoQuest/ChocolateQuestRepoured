package com.teamcqr.chocolatequestrepoured.client.render.entity;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

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
	
	@Override
	protected void renderModel(EntityCQRMandril entitylivingbaseIn, float limbSwing, float limbSwingAmount,
			float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
		if(entitylivingbaseIn.isSitting()) {
			GL11.glTranslatef(0, 0, 0.25F);
		} else {
			GL11.glTranslatef(0, 0, 0.15F);
		}
		super.renderModel(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
	}

}
