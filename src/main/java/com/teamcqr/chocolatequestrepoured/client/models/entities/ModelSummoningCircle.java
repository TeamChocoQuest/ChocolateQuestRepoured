package com.teamcqr.chocolatequestrepoured.client.models.entities;

import com.teamcqr.chocolatequestrepoured.objects.entity.misc.EntitySummoningCircle;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;

/**
 * SummoningCircle - DerToaster
 * Created using Tabula 7.0.1
 */
public class ModelSummoningCircle extends ModelBase {
    public ModelRenderer circle;

    public ModelSummoningCircle(float scale) {
        this.textureWidth = 128;
        this.textureHeight = 64;
        this.circle = new ModelRenderer(this, -64, 0);
        this.circle.setRotationPoint(0.0F, 0.05F, 0.0F);
        this.circle.addBox(-32.0F, 0.0F, -32.0F, 64, 0, 64, scale);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
    	//Scaling
        GlStateManager.pushMatrix();
        GlStateManager.translate(this.circle.offsetX, this.circle.offsetY, this.circle.offsetZ);
        GlStateManager.translate(this.circle.rotationPointX * f5, this.circle.rotationPointY * f5, this.circle.rotationPointZ * f5);
        GlStateManager.scale(0.5D, 1.0D, 0.5D);
        GlStateManager.translate(-this.circle.offsetX, -this.circle.offsetY, -this.circle.offsetZ);
        GlStateManager.translate(-this.circle.rotationPointX * f5, -this.circle.rotationPointY * f5, -this.circle.rotationPointZ * f5);
        
        this.circle.rotateAngleY = this.circle.rotateAngleY + 0.015625F;
        
        this.circle.render(f5);
        GlStateManager.popMatrix();
        
        if(entity instanceof EntitySummoningCircle) {
        	if(((EntitySummoningCircle)entity).isSpawningParticles()) {
        		entity.world.spawnParticle(EnumParticleTypes.SPELL_WITCH, entity.posX, entity.posY + 0.02, entity.posZ, 0F, 0.5F, 0F, 2);
        		
        		entity.world.spawnParticle(EnumParticleTypes.SPELL_WITCH, entity.posX, entity.posY + 0.02, entity.posZ, 0.5F, 0.0F, 0.5F, 1);
        		entity.world.spawnParticle(EnumParticleTypes.SPELL_WITCH, entity.posX, entity.posY + 0.02, entity.posZ, 0.5F, 0.0F, -0.5F, 1);
        		entity.world.spawnParticle(EnumParticleTypes.SPELL_WITCH, entity.posX, entity.posY + 0.02, entity.posZ, -0.5F, 0.0F, 0.5F, 1);
        		entity.world.spawnParticle(EnumParticleTypes.SPELL_WITCH, entity.posX, entity.posY + 0.02, entity.posZ, -0.5F, 0.0F, -0.5F, 1);
        	}
        }
    }
    
    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
    		float headPitch, float scaleFactor, Entity entityIn) {
    	//super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
    	
    	this.circle.rotateAngleY = this.circle.rotateAngleY + 0.125F;
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
