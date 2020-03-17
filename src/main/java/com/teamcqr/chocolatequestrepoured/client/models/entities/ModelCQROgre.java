package com.teamcqr.chocolatequestrepoured.client.models.entities;

import net.minecraft.client.model.ModelRenderer;

/**
 * ModelOgre - Sir Squidly
 * Created using Tabula 7.0.1
 */
public class ModelCQROgre extends ModelCQRBiped {
    public ModelRenderer BodyLower1;
    public ModelRenderer Nose1;
    public ModelRenderer Jaw1;

    public ModelCQROgre(float modelSize) {
    	this(modelSize, 0, 96, 64);
    }
    
    public ModelCQROgre(float modelSize, float p_i1149_2_, int tWidth, int tHeight) {
    	super(modelSize, p_i1149_2_, tWidth, tHeight, true);
        
        this.bipedBody = new ModelRenderer(this, 16, 16);
        this.bipedBody.setRotationPoint(0.0F, 1.0F, 0.0F);
        this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 4, 4, 0.0F);
        this.bipedLeftLeg = new ModelRenderer(this, 16, 48);
        this.bipedLeftLeg.setRotationPoint(2.3F, 12.0F, 0.1F);
        this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
        this.bipedLeftArm = new ModelRenderer(this, 32, 48);
        this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
        this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
        this.setRotateAngle(bipedLeftArm, -0.0F, 0.0F, -0.10000736613927509F);
        this.bipedBodyWear = new ModelRenderer(this, 16, 32);
        this.bipedBodyWear.setRotationPoint(0.0F, 0.0F, -0.5F);
        this.bipedBodyWear.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.55F);
        this.Nose1 = new ModelRenderer(this, 64, 0);
        this.Nose1.setRotationPoint(-1.0F, -3.3F, -4.5F);
        this.Nose1.addBox(0.0F, 0.0F, 0.0F, 2, 2, 1, 0.0F);
        this.setRotateAngle(Nose1, -0.2617993877991494F, 0.0F, 0.0F);
        this.BodyLower1 = new ModelRenderer(this, 66, 19);
        this.BodyLower1.setRotationPoint(-4.5F, 4.0F, -3.5F);
        this.BodyLower1.addBox(0.0F, 0.0F, 0.0F, 9, 7, 6, 0.0F);
        this.bipedLeftLegwear = new ModelRenderer(this, 0, 48);
        this.bipedLeftLegwear.setRotationPoint(2.2F, 12.0F, 0.1F);
        this.bipedLeftLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.25F);
        this.bipedHeadwear = new ModelRenderer(this, 32, 0);
        this.bipedHeadwear.setRotationPoint(0.0F, -1.0F, -1.0F);
        this.bipedHeadwear.addBox(-4.0F, -7.0F, -4.0F, 8, 8, 8, 0.25F);
        this.bipedRightArmwear = new ModelRenderer(this, 40, 32);
        this.bipedRightArmwear.setRotationPoint(-5.0F, 2.0F, 0.0F);
        this.bipedRightArmwear.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, 0.25F);
        this.setRotateAngle(bipedRightArmwear, 0.0F, 0.0F, 0.10000736613927509F);
        this.Jaw1 = new ModelRenderer(this, 60, 32);
        this.Jaw1.setRotationPoint(-4.5F, -2.5F, -4.5F);
        this.Jaw1.addBox(0.0F, 0.0F, 0.0F, 9, 4, 9, 0.0F);
        this.bipedRightArm = new ModelRenderer(this, 40, 16);
        this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
        this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
        this.setRotateAngle(bipedRightArm, 0.0F, 0.0F, 0.10000736613927509F);
        this.bipedHead = new ModelRenderer(this, 0, 0);
        this.bipedHead.setRotationPoint(0.0F, -1.0F, -1.0F);
        this.bipedHead.addBox(-4.0F, -7.0F, -4.0F, 8, 8, 8, 0.0F);
        this.bipedLeftArmwear = new ModelRenderer(this, 48, 48);
        this.bipedLeftArmwear.setRotationPoint(5.0F, 2.0F, 0.0F);
        this.bipedLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, 0.25F);
        this.setRotateAngle(bipedLeftArmwear, -0.0F, 0.0F, -0.10000736613927509F);
        this.bipedRightLegwear = new ModelRenderer(this, 0, 32);
        this.bipedRightLegwear.setRotationPoint(-2.2F, 12.0F, 0.1F);
        this.bipedRightLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.25F);
        this.bipedRightLeg = new ModelRenderer(this, 0, 16);
        this.bipedRightLeg.setRotationPoint(-2.3F, 12.0F, 0.0F);
        this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
        this.bipedHead.addChild(this.Nose1);
        this.bipedBody.addChild(this.BodyLower1);
        this.bipedHead.addChild(this.Jaw1);
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
