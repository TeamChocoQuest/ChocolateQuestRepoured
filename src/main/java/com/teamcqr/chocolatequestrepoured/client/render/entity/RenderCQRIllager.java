package com.teamcqr.chocolatequestrepoured.client.render.entity;

import java.util.ArrayList;
import java.util.List;

import com.teamcqr.chocolatequestrepoured.client.models.entities.ModelCQRIllager;
import com.teamcqr.chocolatequestrepoured.client.render.entity.layers.LayerCQREntityArmor;
import com.teamcqr.chocolatequestrepoured.client.render.entity.layers.LayerCQRHeldItem;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRIllager;
import com.teamcqr.chocolatequestrepoured.objects.items.ItemPotionHealing;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.EnumHandSide;

public class RenderCQRIllager extends RenderCQREntity<EntityCQRIllager> {

	public RenderCQRIllager(RenderManager rendermanager) {
		super(rendermanager, new ModelCQRIllager(0.0F), 0.5F, "entity_mob_cqrillager", 0.9375D, 0.9375D);

		List<LayerRenderer<?>> toRemove = new ArrayList<LayerRenderer<?>>();
		for (LayerRenderer<?> layer : this.layerRenderers) {
			if (layer instanceof LayerBipedArmor || layer instanceof LayerHeldItem) {
				toRemove.add(layer);
			}
		}
		for (LayerRenderer<?> layer : toRemove) {
			this.layerRenderers.remove(layer);
		}

		this.addLayer(new LayerCQRHeldItem(this) {
			@Override
			public void doRenderLayer(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
				if (entity instanceof EntityCQRIllager && (((EntityCQRIllager) entity).isAggressive() || ((EntityCQRIllager) entity).getHeldItemMainhand().getItem() instanceof ItemPotionHealing)) {
					super.doRenderLayer(entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
				}
			}
		});

		/*
		this.addLayer(new LayerBipedArmor(this) {

			@Override
			protected void initArmor() {
				this.modelLeggings = new ModelCQRIllagerArmor(0.5F);
				this.modelArmor = new ModelCQRIllagerArmor(1.0F);
			}
		});
		*/
		this.addLayer(new LayerCQREntityArmor(this) {
			@Override
			public void setupHeadOffsets(ModelRenderer modelRenderer, EntityEquipmentSlot slot) {
				this.rotate(modelRenderer, false);
				GlStateManager.translate(0.0D, -0.125D, 0.0D);
				this.rotate(modelRenderer, true);
			}

			@Override
			public void setupBodyOffsets(ModelRenderer modelRenderer, EntityEquipmentSlot slot) {
				this.rotate(modelRenderer, false);
				GlStateManager.scale(1.1875D, 1.125D, 1.375D);
				this.rotate(modelRenderer, true);
			}
		});
	}

}
