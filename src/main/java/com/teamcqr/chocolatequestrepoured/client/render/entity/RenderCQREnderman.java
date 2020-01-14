package com.teamcqr.chocolatequestrepoured.client.render.entity;

import java.util.ArrayList;
import java.util.List;

import com.teamcqr.chocolatequestrepoured.client.models.entities.customarmor.ModelCQREndermanArmor;
import com.teamcqr.chocolatequestrepoured.client.render.entity.layers.LayerCQRHeldItem;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQREnderman;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelEnderman;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderCQREnderman extends RenderCQREntity<EntityCQREnderman> {

	public RenderCQREnderman(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelEnderman(0.0F), 0.5F, "entity_mob_cqrenderman", 1.0D, 1.0D);
		List<LayerRenderer<?>> toRemove = new ArrayList<LayerRenderer<?>>();
		for (LayerRenderer<?> layer : this.layerRenderers) {
			if (layer instanceof LayerBipedArmor || layer instanceof LayerHeldItem) {
				toRemove.add(layer);
			}
		}
		for (LayerRenderer<?> layer : toRemove) {
			this.layerRenderers.remove(layer);
		}
		this.addLayer(new LayerCQRHeldItem(this));
		this.addLayer(new LayerBipedArmor(this) {
			@Override
			protected void initArmor() {
				//super.initArmor();
				this.modelLeggings = new ModelCQREndermanArmor(0.5F, true);
				//ModelBiped newLeggings = new ModelCQREndermanArmor(0.5F, true);
				this.modelArmor = new ModelCQREndermanArmor(1.0F, false);
				//ModelBiped newArmor = new ModelCQREndermanArmor(1.0F, false);
				
				//copyAnglesAndPositions(newLeggings, modelLeggings);
				//copyAnglesAndPositions(newArmor, modelArmor);
			}
			
			/*private void copyAnglesAndPositions(ModelBiped source, ModelBiped dest) {
				ModelBase.copyModelAngles(source.bipedBody, dest.bipedBody);
				copyRotationPointAndOffsets(source.bipedBody, dest.bipedBody);
				
				ModelBase.copyModelAngles(source.bipedHead, dest.bipedHead);
				copyRotationPointAndOffsets(source.bipedHead, dest.bipedHead);
				
				ModelBase.copyModelAngles(source.bipedRightArm, dest.bipedRightArm);
				copyRotationPointAndOffsets(source.bipedRightArm, dest.bipedRightArm);
				
				ModelBase.copyModelAngles(source.bipedLeftArm, dest.bipedLeftArm);
				copyRotationPointAndOffsets(source.bipedLeftArm, dest.bipedLeftArm);
				
				ModelBase.copyModelAngles(source.bipedRightLeg, dest.bipedRightLeg);
				copyRotationPointAndOffsets(source.bipedRightLeg, dest.bipedRightLeg);
				
				ModelBase.copyModelAngles(source.bipedLeftLeg, dest.bipedLeftLeg);
				copyRotationPointAndOffsets(source.bipedLeftLeg, dest.bipedLeftLeg);
			}

			private void copyRotationPointAndOffsets(ModelRenderer source, ModelRenderer dest) {
				dest.setRotationPoint(source.rotationPointX, source.rotationPointY, source.rotationPointZ);
				dest.offsetX = source.offsetX;
				dest.offsetY = source.offsetY;
				dest.offsetZ = source.offsetZ;
			}*/

			@Override
			protected void setModelSlotVisible(ModelBiped modelIn, EntityEquipmentSlot slotIn) {
				if (modelIn == this.modelArmor || modelIn == this.modelLeggings) {
					super.setModelSlotVisible(modelIn, slotIn);
				} else {
					this.setModelVisible(modelIn);
				}
			}
		});
	}

}
