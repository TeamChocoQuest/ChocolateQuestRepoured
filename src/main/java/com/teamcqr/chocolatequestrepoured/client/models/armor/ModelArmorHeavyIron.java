package com.teamcqr.chocolatequestrepoured.client.models.armor;

import net.minecraft.client.model.ModelRenderer;

/**
 * ModelCustomArmor - Silentine
 * Created using Tabula 7.0.1
 */
public class ModelArmorHeavyIron extends ModelCustomArmorBase {
    public ModelRenderer RightArmPauldon;
    public ModelRenderer LeftArmPauldron;
    public ModelRenderer HeadHelmet;
    public ModelRenderer ChestArmor;

    public ModelArmorHeavyIron(float scale) {
    	super(scale, 128, 128);
    	
    	this.ChestArmor = new ModelRenderer(this, 0, 88);
        this.ChestArmor.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.ChestArmor.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, scale);
        
        this.HeadHelmet = new ModelRenderer(this, 0, 64);
        this.HeadHelmet.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.HeadHelmet.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, scale);
    	
    	 this.RightArmPauldon = new ModelRenderer(this, 0, 80);
         this.RightArmPauldon.setRotationPoint(0.0F, 0.0F, 0.0F);
         this.RightArmPauldon.addBox(-3.0F, -2.0F, -2.0F, 4, 4, 4, scale);
         
         this.LeftArmPauldron = new ModelRenderer(this, 0, 80);
         this.LeftArmPauldron.mirror = true;
         this.LeftArmPauldron.setRotationPoint(0.0F, 0.0F, 0.0F);
         this.LeftArmPauldron.addBox(-1.0F, -2.0F, -2.0F, 4, 4, 4, scale);
    	
    	 this.bipedBody.addChild(this.ChestArmor);
         this.bipedHead.addChild(this.HeadHelmet);
         this.bipedLeftArm.addChild(this.LeftArmPauldron);
         this.bipedRightArm.addChild(this.RightArmPauldon);
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