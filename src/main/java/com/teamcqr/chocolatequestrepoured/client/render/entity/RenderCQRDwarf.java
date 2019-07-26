package com.teamcqr.chocolatequestrepoured.client.render.entity;

import org.lwjgl.opengl.GL11;

import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRDwarf;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerArrow;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerElytra;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.util.ResourceLocation;

public class RenderCQRDwarf extends RenderLiving<EntityCQRDwarf> {
	
	public static final ResourceLocation TEXTURES = new ResourceLocation((Reference.MODID + ":textures/entity/entity_mob_cqrdwarf.png"));

	 public RenderCQRDwarf(RenderManager manager)
	    {
	        //Using the vanilla model - if we wanted to use a custom model this is where we could insert that instead
	        super(manager, new ModelBiped(), 0.5F);
	        
	        this.addLayer(new LayerBipedArmor(this));
	        this.addLayer(new LayerHeldItem(this));
	        this.addLayer(new LayerArrow(this));
	        this.addLayer(new LayerElytra(this));
	    }
	 
	@Override
	protected ResourceLocation getEntityTexture(EntityCQRDwarf entity) {
		return TEXTURES;
	}
	
	@Override
	protected void applyRotations(EntityCQRDwarf entityLiving, float p_77043_2_, float rotationYaw, float partialTicks) {
        super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);
    }
	
	@Override
	protected void preRenderCallback(EntityCQRDwarf entitylivingbaseIn, float partialTickTime) {
		if(entitylivingbaseIn != null) {
			super.preRenderCallback(entitylivingbaseIn, partialTickTime);
			
			GL11.glScalef(0.9F, 0.65F, 0.9F);
		}
		
	}

}
