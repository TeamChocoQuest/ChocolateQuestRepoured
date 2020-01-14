package com.teamcqr.chocolatequestrepoured.client.models.entities;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

/**
 * ModelPlayer - Either Mojang or a mod author
 * Created using Tabula 7.0.1
 */
public class ModelCQRGremlin extends ModelCQRBiped {
    public ModelRenderer hornR1;
    public ModelRenderer hornL1;
    public ModelRenderer hornR2;
    public ModelRenderer hornL2;
    public ModelRenderer nose;

    public ModelCQRGremlin(float modelSize) {
    	this(modelSize, 0, 96, 96);
    }
    
    public ModelCQRGremlin(float modelSize, float p_i1149_2_, int textureWidthIn, int textureHeightIn) {
    	super(modelSize, p_i1149_2_, textureWidthIn, textureHeightIn, false);
		this.textureWidth = textureWidthIn;
		this.textureHeight = textureHeightIn;
		
		this.bipedLeftLeg = new ModelRenderer(this, 0, 22);
        this.bipedLeftLeg.mirror = true;
        this.bipedLeftLeg.setRotationPoint(2.0F, 18.0F, 6.0F);
        this.bipedLeftLeg.addBox(0.0F, 0.0F, -2.0F, 4, 6, 4, modelSize);
        this.hornR2 = new ModelRenderer(this, 56, 4);
        this.hornR2.setRotationPoint(0.0F, -0.7F, 0.0F);
        this.hornR2.addBox(-0.5F, -1.0F, -0.5F, 1, 1, 1, modelSize);
        this.setRotateAngle(hornR2, -0.7853981633974483F, 0.7853981633974483F, 0.0F);
        this.bipedHead = new ModelRenderer(this, 0, 0);
        this.bipedHead.setRotationPoint(0.0F, 10.0F, -4.0F);
        this.bipedHead.addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8, modelSize + 0.125F);
        this.bipedHeadwear = new ModelRenderer(this, 32, 0);
        this.bipedHeadwear.setRotationPoint(0.0F, 10.0F, -4.0F);
        this.bipedHeadwear.addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8, modelSize + 0.25F);
        this.bipedRightArm = new ModelRenderer(this, 40, 16);
        this.bipedRightArm.setRotationPoint(-4.0F, 14.5F, -2.5F);
        this.bipedRightArm.addBox(-4.0F, -2.0F, -2.0F, 4, 12, 4, modelSize);
        this.setRotateAngle(bipedRightArm, -0.39269908169872414F, 0.0F, 0.0F);
        this.bipedRightLeg = new ModelRenderer(this, 0, 22);
        this.bipedRightLeg.setRotationPoint(-2.0F, 18.0F, 6.0F);
        this.bipedRightLeg.addBox(-4.0F, 0.0F, -2.0F, 4, 6, 4, modelSize);
        this.bipedLeftArm = new ModelRenderer(this, 40, 16);
        this.bipedLeftArm.mirror = true;
        this.bipedLeftArm.setRotationPoint(4.0F, 14.5F, -1.5F);
        this.bipedLeftArm.addBox(0.0F, -2.0F, -2.0F, 4, 12, 4, modelSize);
        this.setRotateAngle(bipedLeftArm, -0.3839724354387525F, 0.0F, 0.0F);
        this.bipedBody = new ModelRenderer(this, 16, 16);
        this.bipedBody.setRotationPoint(0.0F, 13.0F, -4.0F);
        this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F);
        this.setRotateAngle(bipedBody, 1.0471975511965976F, 0.0F, 0.0F);
        this.hornR1 = new ModelRenderer(this, 56, 0);
        this.hornR1.setRotationPoint(-3.5F, -3.5F, -3.5F);
        this.hornR1.addBox(-1.0F, -1.0F, -1.0F, 2, 2, 2, modelSize);
        this.setRotateAngle(hornR1, 0.7853981633974483F, 0.7853981633974483F, 0.0F);
        this.hornL2 = new ModelRenderer(this, 56, 4);
        this.hornL2.setRotationPoint(0.0F, -0.7F, 0.0F);
        this.hornL2.addBox(-0.5F, -1.0F, -0.5F, 1, 1, 1, modelSize);
        this.setRotateAngle(hornL2, -0.6829473363053812F, 0.091106186954104F, -0.7853981633974483F);
        this.hornL1 = new ModelRenderer(this, 56, 0);
        this.hornL1.mirror = true;
        this.hornL1.setRotationPoint(3.5F, -3.5F, -3.5F);
        this.hornL1.addBox(-1.0F, -1.0F, -1.0F, 2, 2, 2, modelSize);
        this.setRotateAngle(hornL1, 0.7853981633974483F, -0.7853981633974483F, 0.0F);
        this.nose = new ModelRenderer(this, 64, 0);
        this.nose.setRotationPoint(0.0F, -1.0F, -4.0F);
        this.nose.addBox(-1.0F, 0.0F, 0.0F, 2, 3, 2, modelSize);
        this.setRotateAngle(nose, -0.4363323129985824F, 0.0F, 0.0F);
        this.hornR1.addChild(this.hornR2);
        this.bipedHead.addChild(this.hornR1);
        this.hornL1.addChild(this.hornL2);
        this.bipedHead.addChild(this.hornL1);
        this.bipedHead.addChild(this.nose);
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) { 
    	this.isRiding = false;
       super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    	this.bipedBody.render(scale);
        this.bipedLeftArm.render(scale);
        this.bipedRightLeg.render(scale);
        this.bipedHead.render(scale);
        this.bipedLeftLeg.render(scale);
        this.bipedRightArm.render(scale);
    }
    
    @Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
    	this.isRiding = false;
    	super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
    	
    	this.bipedLeftArm.rotateAngleX += MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
        this.bipedRightArm.rotateAngleX += MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount * 0.5F;
        

        this.bipedBody.setRotationPoint(0.0F, 13.0F, -4.0F);
		this.bipedRightArm.setRotationPoint(-4.0F, 14.5F, -2.5F);
		this.bipedLeftArm.setRotationPoint(4.0F, 14.5F, -1.5F);
		this.bipedRightLeg.setRotationPoint(-2.0F, 18.0F, 6.0F);
		this.bipedLeftLeg.setRotationPoint(2.0F, 18.0F, 6.0F);
		this.bipedHead.setRotationPoint(0.0F, 10.0F, -4.0F);
		this.bipedHeadwear.setRotationPoint(0.0F, 10.0F, -4.0F);
		this.setRotateAngle(bipedBody, 1.0471975511965976F, 0.0F, 0.0F);
		
		copyModelAngles(this.bipedHead, this.bipedHeadwear);
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
