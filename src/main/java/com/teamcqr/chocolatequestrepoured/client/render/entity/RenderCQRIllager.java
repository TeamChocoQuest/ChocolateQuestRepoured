package com.teamcqr.chocolatequestrepoured.client.render.entity;

import java.util.ArrayList;
import java.util.List;

import com.teamcqr.chocolatequestrepoured.client.models.entities.ModelCQRIllager;
import com.teamcqr.chocolatequestrepoured.client.render.entity.layers.LayerCQRHeldItem;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRIllager;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumHandSide;

public class RenderCQRIllager extends RenderCQREntity<EntityCQRIllager> {

	public RenderCQRIllager(RenderManager rendermanager)
    {
		super(rendermanager, new ModelCQRIllager(0F), 0.5F, "entity_mob_cqrillager", 1.0D, 1.0D);
		
		List<LayerRenderer<?>> toRemove = new ArrayList<LayerRenderer<?>>();
		for (LayerRenderer<?> layer : this.layerRenderers) {
			if (layer instanceof LayerBipedArmor || layer instanceof LayerHeldItem) {
				toRemove.add(layer);
			}
		}
		for (LayerRenderer<?> layer : toRemove) {
			this.layerRenderers.remove(layer);
		}
		//TODO: Illager armor
		/*this.addLayer(new LayerBipedArmor(this) {
			@Override
			protected void initArmor() {
				this.modelLeggings = new ModelCQREndermanArmor(0.5F, true);
				this.modelArmor = new ModelCQREndermanArmor(1.0F, false);
			}
		});*/
		
		this.addLayer(new LayerCQRHeldItem(this)
        {
        	@Override
            public void doRenderLayer(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
            {
                if (entity instanceof EntityCQRIllager && ((EntityCQRIllager) entity).isAggressive())
                {
                    super.doRenderLayer(entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
                }
            }
        	@Override
            protected void translateToHand(EnumHandSide hand)
            {
                ((ModelCQRIllager)this.livingEntityRenderer.getMainModel()).getArm(hand).postRender(0.0625F);
            }
        });
    }
    
}
