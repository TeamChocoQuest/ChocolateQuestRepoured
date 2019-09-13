package com.teamcqr.chocolatequestrepoured.client.render.entity;

import org.lwjgl.opengl.GL11;

import com.teamcqr.chocolatequestrepoured.client.render.entity.layers.LayerCQREntityCape;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerArrow;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerElytra;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class RenderCQREntity<T extends AbstractEntityCQR> extends RenderLiving<T> {

	public ResourceLocation texture;
	public double widthScale;
	public double heightScale;

	public RenderCQREntity(RenderManager rendermanagerIn, String entityName) {
		this(rendermanagerIn, entityName, 1.0D, 1.0D);
	}

	public RenderCQREntity(RenderManager rendermanagerIn, String entityName, double widthScale, double heightScale) {
		this(rendermanagerIn, new ModelBiped(), 0.5F, entityName, widthScale, heightScale);
	}

	public RenderCQREntity(RenderManager rendermanagerIn, ModelBase model, float shadowSize, String entityName,
			double widthScale, double heightScale) {
		super(rendermanagerIn, model, shadowSize);
		this.texture = new ResourceLocation(Reference.MODID, "textures/entity/" + entityName + ".png");
		this.widthScale = widthScale;
		this.heightScale = heightScale;
		this.addLayer(new LayerBipedArmor(this));
		this.addLayer(new LayerHeldItem(this));
		this.addLayer(new LayerArrow(this));
		this.addLayer(new LayerElytra(this));
		this.addLayer(new LayerCQREntityCape(this));
	}

	@Override
	public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
		GL11.glPushMatrix();
		GL11.glScaled(this.widthScale + entity.getSizeVariation() /2, this.heightScale + entity.getSizeVariation(), this.widthScale + entity.getSizeVariation() /2);
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		GL11.glPopMatrix();
		
		if(this.mainModel instanceof ModelBiped) {
			ModelBiped model = (ModelBiped)this.mainModel;
			
			ItemStack itemMainHand = entity.getHeldItemMainhand();
			ItemStack itemOffHand = entity.getHeldItemOffhand();
			
			ModelBiped.ArmPose armPoseMain = ModelBiped.ArmPose.EMPTY;
            ModelBiped.ArmPose armPoseOff = ModelBiped.ArmPose.EMPTY;
			//Main arm
			if(!itemMainHand.isEmpty() && entity.getItemInUseCount() > 0) {
				EnumAction action = itemMainHand.getItemUseAction();
				switch(action) {
				case BLOCK:
					armPoseMain = ModelBiped.ArmPose.BLOCK;
					break;
				case BOW:
					armPoseMain = ModelBiped.ArmPose.BOW_AND_ARROW;
					break;
				case NONE:
					armPoseMain = ModelBiped.ArmPose.EMPTY;
				default:
					armPoseMain = ModelBiped.ArmPose.ITEM;
					break;
				
				}
			}
			//Off arm
			if(!itemOffHand.isEmpty() && entity.getItemInUseCount() > 0) {
				EnumAction action = itemOffHand.getItemUseAction();
				switch(action) {
				case BLOCK:
					armPoseOff = ModelBiped.ArmPose.BLOCK;
					break;
				case BOW:
					armPoseOff = ModelBiped.ArmPose.BOW_AND_ARROW;
					break;
				case NONE:
					armPoseOff = ModelBiped.ArmPose.EMPTY;
					break;
				default:
					armPoseOff = ModelBiped.ArmPose.ITEM;
					break;
				
				}
			}
			
			model.rightArmPose = armPoseMain;
			model.leftArmPose = armPoseOff;
		}
	}

	protected ResourceLocation getEntityTexture(T entity) {
		return this.texture;
	}

}
