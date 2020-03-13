package com.teamcqr.chocolatequestrepoured.client.render.entity;

import java.util.ArrayList;
import java.util.List;

import com.teamcqr.chocolatequestrepoured.client.models.entities.ModelCQRGremlin;
import com.teamcqr.chocolatequestrepoured.client.render.entity.layers.LayerCQREntityArmor;
import com.teamcqr.chocolatequestrepoured.client.render.entity.layers.LayerCQREntityPotion;
import com.teamcqr.chocolatequestrepoured.client.render.entity.layers.LayerCQRHeldItem;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRGremlin;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.inventory.EntityEquipmentSlot;

public class RenderCQRGremlin extends RenderCQREntity<EntityCQRGremlin> {

	public RenderCQRGremlin(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelCQRGremlin(0F), 0.5F, "entity_mob_cqrgremlin", 1.0D, 1.0D);

		List<LayerRenderer<?>> toRemove = new ArrayList<LayerRenderer<?>>();
		for (LayerRenderer<?> layer : this.layerRenderers) {
			if (layer instanceof LayerBipedArmor || layer instanceof LayerHeldItem || layer instanceof LayerCQREntityPotion) {
				toRemove.add(layer);
			}
		}
		for (LayerRenderer<?> layer : toRemove) {
			this.layerRenderers.remove(layer);
		}

		this.addLayer(new LayerCQRHeldItem(this));
		/*
		this.addLayer(new LayerBipedArmor(this) {
			@Override
			protected void initArmor() {
				this.modelLeggings = new ModelCQRGremlinArmor(0.5F, true);
				this.modelArmor = new ModelCQRGremlinArmor(1.0F, false);
			}
		});
		*/
		this.addLayer(new LayerCQREntityArmor(this) {
			@Override
			public void setupHeadOffsets(ModelRenderer modelRenderer, EntityEquipmentSlot slot) {
				this.rotate(modelRenderer, false);
				GlStateManager.translate(0.0D, 0.25D, 0.0D);
				this.rotate(modelRenderer, true);
			}

			@Override
			public void setupRightArmOffsets(ModelRenderer modelRenderer, EntityEquipmentSlot slot) {
				this.rotate(modelRenderer, false);
				GlStateManager.translate(-0.0625D, 0.0D, 0.0D);
				this.rotate(modelRenderer, true);
			}

			@Override
			public void setupLeftArmOffsets(ModelRenderer modelRenderer, EntityEquipmentSlot slot) {
				this.rotate(modelRenderer, false);
				GlStateManager.translate(0.0625D, 0.0D, 0.0D);
				this.rotate(modelRenderer, true);
			}

			@Override
			public void setupRightLegOffsets(ModelRenderer modelRenderer, EntityEquipmentSlot slot) {
				if (slot == EntityEquipmentSlot.LEGS) {
					this.rotate(modelRenderer, false);
					GlStateManager.translate(-0.125D, -0.375D, 0.0D);
					GlStateManager.translate(0.0D, 0.875D, 0.0D);
					GlStateManager.scale(1.0D, 0.5D, 1.0D);
					this.rotate(modelRenderer, true);
				} else if (slot == EntityEquipmentSlot.FEET) {
					this.rotate(modelRenderer, false);
					GlStateManager.translate(-0.125D, -0.375D, 0.0D);
					GlStateManager.translate(0.0D, 0.375D, 0.0D);
					GlStateManager.scale(1.0D, 0.8D, 1.0D);
					this.rotate(modelRenderer, true);
				}
			}

			@Override
			public void setupLeftLegOffsets(ModelRenderer modelRenderer, EntityEquipmentSlot slot) {
				if (slot == EntityEquipmentSlot.LEGS) {
					this.rotate(modelRenderer, false);
					GlStateManager.translate(0.125D, -0.375D, 0.0D);
					GlStateManager.translate(0.0D, 0.875D, 0.0D);
					GlStateManager.scale(1.0D, 0.5D, 1.0D);
					this.rotate(modelRenderer, true);
				} else if (slot == EntityEquipmentSlot.FEET) {
					this.rotate(modelRenderer, false);
					GlStateManager.translate(0.125D, -0.375D, 0.0D);
					GlStateManager.translate(0.0D, 0.375D, 0.0D);
					GlStateManager.scale(1.0D, 0.8D, 1.0D);
					this.rotate(modelRenderer, true);
				}
			}
		});
	}

}
