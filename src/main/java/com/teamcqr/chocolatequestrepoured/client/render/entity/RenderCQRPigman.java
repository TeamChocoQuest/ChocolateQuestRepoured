package com.teamcqr.chocolatequestrepoured.client.render.entity;

import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRPigman;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderCQRPigman extends RenderLiving<EntityCQRPigman> {
	
	public static final ResourceLocation TEXTURES = new ResourceLocation((Reference.MODID + ":textures/entity/entity_mob_cqrpigman.png"));

	 public RenderCQRPigman(RenderManager manager)
	    {
	        //Using the vanilla model - if we wanted to use a custom model this is where we could insert that instead
	        super(manager, new ModelZombie(), 0.5F);
	    }
	 
	@Override
	protected ResourceLocation getEntityTexture(EntityCQRPigman entity) {
		return TEXTURES;
	}
	
	@Override
	protected void applyRotations(EntityCQRPigman entityLiving, float p_77043_2_, float rotationYaw, float partialTicks) {
        super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);
    }

}
