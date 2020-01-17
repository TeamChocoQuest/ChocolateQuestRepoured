package com.teamcqr.chocolatequestrepoured.client.models.entities.customarmor;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelCQRGremlinArmor extends ModelBiped {

	public ModelCQRGremlinArmor(float modelSize, boolean legs) {
		super(modelSize);
		this.bipedHead = new ModelRenderer(this, 0, 0);
		this.bipedHead.setRotationPoint(0.0F, 10.0F, -4.0F);
		this.bipedHead.addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8, modelSize + 0.125F);
        
        this.bipedHeadwear = new ModelRenderer(this, 0, 0);
        
        this.bipedBody = new ModelRenderer(this, 16, 16);
        this.bipedBody.setRotationPoint(0.0F, 13.0F, -4.0F);
        this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, modelSize);
        this.setRotateAngle(bipedBody, 1.0471975511965976F, 0.0F, 0.0F);
        
        this.bipedRightArm = new ModelRenderer(this, 40, 16);
        this.bipedRightArm.setRotationPoint(-4.0F, 14.5F, -2.5F);
        this.bipedRightArm.addBox(-4.0F, -2.0F, -2.0F, 4, 12, 4, modelSize);
        
        this.bipedLeftArm = new ModelRenderer(this, 40, 16);
        this.bipedLeftArm.mirror = true;
        this.bipedLeftArm.setRotationPoint(4.0F, 14.5F, -1.5F);
        this.bipedLeftArm.addBox(0.0F, -2.0F, -2.0F, 4, 12, 4, modelSize);
        
        if(legs) {
        	this.bipedRightLeg = new ModelRenderer(this, 0, 0);
            this.bipedLeftLeg = new ModelRenderer(this, 0, 0);
        } else {
        	this.bipedRightLeg = new ModelRenderer(this, 0, 22);
        	 this.bipedRightLeg.setRotationPoint(-2.0F, 18.0F, 6.0F);
             this.bipedRightLeg.addBox(-4.0F, 0.0F, -2.0F, 4, 6, 4, modelSize);
            
            this.bipedLeftLeg = new ModelRenderer(this, 0, 22);
            this.bipedLeftLeg.mirror = true;
            this.bipedLeftLeg.setRotationPoint(2.0F, 18.0F, 6.0F);
            this.bipedLeftLeg.addBox(0.0F, 0.0F, -2.0F, 4, 6, 4, modelSize);
        }
	}
	
	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		this.isRiding = false;
		super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
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
	
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

}
