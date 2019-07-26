package com.teamcqr.chocolatequestrepoured.client.render.entity;

import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRDwarf;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderCQRDwarf extends RenderLiving<EntityCQRDwarf> {
	
	public static final ResourceLocation TEXTURES = new ResourceLocation((Reference.MODID + ":textures/entity/entity_mob_cqrdwarf.png"));

	 public RenderCQRDwarf(RenderManager manager)
	    {
	        //Using the vanilla model - if we wanted to use a custom model this is where we could insert that instead
	        super(manager, new ModelPlayer(0.5F, false), 0.5F);
	    }
	 
	@Override
	protected ResourceLocation getEntityTexture(EntityCQRDwarf entity) {
		return TEXTURES;
	}
	
	@Override
	protected void applyRotations(EntityCQRDwarf entityLiving, float p_77043_2_, float rotationYaw, float partialTicks) {
        super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);
    }

}
