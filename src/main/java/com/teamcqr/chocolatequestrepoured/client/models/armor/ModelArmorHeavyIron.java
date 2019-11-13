package com.teamcqr.chocolatequestrepoured.client.models.armor;

import net.minecraft.client.model.ModelRenderer;

/**
 * ModelCustomArmor - Silentine
 * Created using Tabula 7.0.1
 */
public class ModelArmorHeavyIron extends ModelCustomArmorBase {
	 public ModelRenderer pauldronR1;
	    public ModelRenderer pauldronR2;
	    public ModelRenderer skirtR;
	    public ModelRenderer lowerHeadArmor;
	    public ModelRenderer chestExtension;
	    public ModelRenderer pauldronL1;
	    public ModelRenderer pauldronL2;
	    public ModelRenderer skirtL;

    public ModelArmorHeavyIron(float scale) {
    	super(scale, 128, 128);
    	
        this.pauldronR2 = new ModelRenderer(this, 0, 106);
        this.pauldronR2.setRotationPoint(1.0F, 2.5F, 0.0F);
        this.pauldronR2.addBox(-0.5F, -6.0F, -3.0F, 2, 6, 6, 0.0F);
        this.setRotateAngle(pauldronR2, 0.0F, 0.0F, -0.2617993877991494F);
        
        this.pauldronL1 = new ModelRenderer(this, 0, 96);
        this.pauldronL1.mirror = true;
        this.pauldronL1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.pauldronL1.addBox(-1.5F, -2.5F, -2.5F, 5, 5, 5, 0.0F);
        this.setRotateAngle(pauldronL1, 0.0F, 0.0F, 0.08726646259971647F);
        
        this.chestExtension = new ModelRenderer(this, 20, 96);
        this.chestExtension.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.chestExtension.addBox(-4.5F, -0.5F, -2.5F, 9, 13, 5, 0.0F);
        
        this.lowerHeadArmor = new ModelRenderer(this, 0, 64);
        this.lowerHeadArmor.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.lowerHeadArmor.addBox(-4.5F, -8.5F, -4.5F, 9, 9, 9, 0.0F);
        
        this.pauldronL2 = new ModelRenderer(this, 0, 106);
        this.pauldronL2.mirror = true;
        this.pauldronL2.setRotationPoint(-2.0F, 2.5F, 0.0F);
        this.pauldronL2.addBox(-0.5F, -6.0F, -3.0F, 2, 6, 6, 0.0F);
        this.setRotateAngle(pauldronL2, 0.0F, 0.0F, 0.2617993877991494F);
        
        this.pauldronR1 = new ModelRenderer(this, 0, 96);
        this.pauldronR1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.pauldronR1.addBox(-3.5F, -2.5F, -2.5F, 5, 5, 5, 0.0F);
        this.setRotateAngle(pauldronR1, 0.0F, 0.0F, -0.08726646259971647F);
        
        this.skirtL = new ModelRenderer(this, 64, 64);
        this.skirtL.mirror = true;
        this.skirtL.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.skirtL.addBox(-1.5F, -2.0F, -3.0F, 5, 5, 6, 0.0F);
        this.setRotateAngle(skirtL, 0.0F, 0.0F, -0.2617993877991494F);
        
        this.skirtR = new ModelRenderer(this, 64, 64);
        this.skirtR.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.skirtR.addBox(-3.5F, -2.0F, -3.0F, 5, 5, 6, 0.0F);
        this.setRotateAngle(skirtR, 0.0F, 0.0F, 0.2617993877991494F);
        
        this.bipedRightArm.addChild(this.pauldronR2);
        this.bipedLeftArm.addChild(this.pauldronL1);
        this.bipedBody.addChild(this.chestExtension);
        this.bipedHead.addChild(this.lowerHeadArmor);
        this.bipedLeftArm.addChild(this.pauldronL2);
        this.bipedRightArm.addChild(this.pauldronR1);
        this.bipedLeftLeg.addChild(this.skirtL);
        this.bipedRightLeg.addChild(this.skirtR);
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