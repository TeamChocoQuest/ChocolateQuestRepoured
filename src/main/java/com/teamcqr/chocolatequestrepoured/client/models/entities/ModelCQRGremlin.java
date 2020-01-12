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

    public ModelCQRGremlin(float modelSize) {
    	this(modelSize, 0, 96, 96);
    }
    
    public ModelCQRGremlin(float modelSize, float p_i1149_2_, int textureWidthIn, int textureHeightIn) {
    	super(modelSize, p_i1149_2_, textureWidthIn, textureHeightIn, false);
		this.textureWidth = textureWidthIn;
		this.textureHeight = textureHeightIn;
		
        this.bipedBody = new ModelRenderer(this, 0, 16);
        this.bipedBody.setRotationPoint(0.0F, 19.0F, 6.0F);
        this.bipedBody.addBox(-4.0F, -12.0F, -2.0F, 8, 12, 4, modelSize);
        this.setRotateAngle(bipedBody, 0.7853981633974483F, 0.0F, 0.0F);
        this.bipedLeftArm = new ModelRenderer(this, 25, 16);
        this.bipedLeftArm.setRotationPoint(5.0F, 11.5F, -1.5F);
        this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 3, 15, 4, modelSize);
        this.setRotateAngle(bipedLeftArm, -0.3839724354387525F, 0.0F, 0.0F);
        this.hornR2 = new ModelRenderer(this, 56, 6);
        this.hornR2.setRotationPoint(0.0F, -0.7F, 0.0F);
        this.hornR2.addBox(-0.5F, -3.0F, -0.5F, 1, 3, 1, modelSize);
        this.setRotateAngle(hornR2, -0.7853981633974483F, 0.7853981633974483F, 0.0F);
        this.bipedRightLeg = new ModelRenderer(this, 0, 48);
        this.bipedRightLeg.setRotationPoint(-2.0F, 18.0F, 6.0F);
        this.bipedRightLeg.addBox(-5.0F, 0.0F, -2.0F, 4, 6, 4, modelSize);
        this.bipedHead = new ModelRenderer(this, 0, 0);
        this.bipedHead.setRotationPoint(0.0F, 10.0F, -4.0F);
        this.bipedHead.addBox(-3.5F, -3.5F, -3.5F, 7, 7, 7, modelSize);
        this.bipedHeadwear = new ModelRenderer(this, 56, 16);
        this.bipedHeadwear.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.bipedHeadwear.addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8, modelSize);
        this.hornR1 = new ModelRenderer(this, 56, 0);
        this.hornR1.setRotationPoint(-3.5F, -3.5F, -3.5F);
        this.hornR1.addBox(-1.0F, -1.0F, -1.0F, 2, 2, 2, modelSize);
        this.setRotateAngle(hornR1, 0.7853981633974483F, 0.7853981633974483F, 0.0F);
        this.bipedLeftLeg = new ModelRenderer(this, 16, 48);
        this.bipedLeftLeg.setRotationPoint(2.0F, 18.0F, 6.0F);
        this.bipedLeftLeg.addBox(1.0F, 0.0F, -2.0F, 4, 6, 4, modelSize);
        this.hornL1 = new ModelRenderer(this, 56, 0);
        this.hornL1.setRotationPoint(3.5F, -3.5F, -3.5F);
        this.hornL1.addBox(-1.0F, -1.0F, -1.0F, 2, 2, 2, modelSize);
        this.setRotateAngle(hornL1, 0.7853981633974483F, -0.7853981633974483F, 0.0F);
        this.hornL2 = new ModelRenderer(this, 56, 6);
        this.hornL2.setRotationPoint(0.0F, -0.7F, 0.0F);
        this.hornL2.addBox(-0.5F, -3.0F, -0.5F, 1, 3, 1, modelSize);
        this.setRotateAngle(hornL2, -0.6829473363053812F, 0.091106186954104F, -0.7853981633974483F);
        this.bipedRightArm = new ModelRenderer(this, 40, 16);
        this.bipedRightArm.setRotationPoint(-4.0F, 11.5F, -1.5F);
        this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 3, 15, 4, modelSize);
        this.setRotateAngle(bipedRightArm, -0.39269908169872414F, 0.0F, 0.0F);
        this.hornR1.addChild(this.hornR2);
        this.bipedHead.addChild(this.bipedHeadwear);
        this.bipedHead.addChild(this.hornR1);
        this.bipedHead.addChild(this.hornL1);
        this.hornL1.addChild(this.hornL2);
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) { 
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
    	super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
    	
    	this.bipedRightArm.rotateAngleX += MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.bipedLeftArm.rotateAngleX += MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;

		this.bipedBody.setRotationPoint(0.0F, 19.0F, 6.0F);
		this.setRotateAngle(bipedBody, 0.7853981633974483F, 0.0F, 0.0F);
		
		

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
