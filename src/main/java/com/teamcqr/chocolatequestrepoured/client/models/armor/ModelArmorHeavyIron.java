package com.teamcqr.chocolatequestrepoured.client.models.armor;

import net.minecraft.client.model.ModelRenderer;

/**
 * ModelCustomArmor - Silentine
 * Created using Tabula 7.0.1
 */
public class ModelArmorHeavyIron extends ModelCustomArmorBase {
    public ModelRenderer pauldronRight;
    public ModelRenderer pauldronLeft;
    public ModelRenderer helmetExtension;
    public ModelRenderer chestArmor;

    public ModelArmorHeavyIron(float scale) {
    	super(scale, 128, 128);
    	
    	this.chestArmor = new ModelRenderer(this, 0, 88);
        this.chestArmor.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.chestArmor.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, scale);
        
        this.helmetExtension = new ModelRenderer(this, 0, 64);
        this.helmetExtension.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.helmetExtension.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, scale);
    	
    	 this.pauldronRight = new ModelRenderer(this, 0, 80);
         this.pauldronRight.setRotationPoint(0.0F, 0.0F, 0.0F);
         this.pauldronRight.addBox(-3.0F, -2.0F, -2.0F, 4, 4, 4, scale);
         
         this.pauldronLeft = new ModelRenderer(this, 0, 80);
         this.pauldronLeft.mirror = true;
         this.pauldronLeft.setRotationPoint(0.0F, 0.0F, 0.0F);
         this.pauldronLeft.addBox(-1.0F, -2.0F, -2.0F, 4, 4, 4, scale);
    	
    	 this.bipedBody.addChild(this.chestArmor);
         this.bipedHead.addChild(this.helmetExtension);
         this.bipedLeftArm.addChild(this.pauldronLeft);
         this.bipedRightArm.addChild(this.pauldronRight);
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