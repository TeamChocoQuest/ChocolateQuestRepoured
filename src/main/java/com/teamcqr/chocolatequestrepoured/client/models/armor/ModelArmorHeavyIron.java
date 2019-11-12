package com.teamcqr.chocolatequestrepoured.client.models.armor;

import net.minecraft.client.model.ModelRenderer;

/**
 * ModelCustomArmor - Silentine
 * Created using Tabula 7.0.1
 */
public class ModelArmorHeavyIron extends ModelCustomArmorBase {
	public ModelRenderer RightArmPauldon;
    public ModelRenderer RightLegTasset;
    public ModelRenderer HeadHelmet;
    public ModelRenderer ChestArmor;
    public ModelRenderer LeftArmPauldron;
    public ModelRenderer LeftLegTasset;

    public ModelArmorHeavyIron(float scale) {
    	super(scale, 128, 128);
    	
    	this.RightLegTasset = new ModelRenderer(this, 0, 110);
        this.RightLegTasset.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.RightLegTasset.addBox(-3.5F, -1.5F, -2.6F, 5, 5, 5, scale);
        this.setRotateAngle(RightLegTasset, 0.0F, 0.0F, 0.2617993877991494F);
        
        this.HeadHelmet = new ModelRenderer(this, 0, 64);
        this.HeadHelmet.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.HeadHelmet.addBox(-4.5F, -8.5F, -4.5F, 9, 9, 9, scale);
        
        this.LeftLegTasset = new ModelRenderer(this, 0, 110);
        this.LeftLegTasset.mirror = true;
        this.LeftLegTasset.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.LeftLegTasset.addBox(-1.5F, -1.5F, -2.5F, 5, 5, 5, scale);
        this.setRotateAngle(LeftLegTasset, 0.0F, 0.0F, -0.2617993877991494F);
        
        this.RightArmPauldon = new ModelRenderer(this, 0, 82);
        this.RightArmPauldon.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.RightArmPauldon.addBox(-3.5F, -2.5F, -2.5F, 5, 5, 5, scale);
        
        this.LeftArmPauldron = new ModelRenderer(this, 0, 82);
        this.LeftArmPauldron.mirror = true;
        this.LeftArmPauldron.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.LeftArmPauldron.addBox(-1.5F, -2.5F, -2.5F, 5, 5, 5, scale);
        
        this.ChestArmor = new ModelRenderer(this, 0, 92);
        this.ChestArmor.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.ChestArmor.addBox(-4.5F, -0.5F, -2.5F, 9, 13, 5, scale);
        
        this.bipedRightLeg.addChild(this.RightLegTasset);
        this.bipedHead.addChild(this.HeadHelmet);
        this.bipedLeftLeg.addChild(this.LeftLegTasset);
        this.bipedRightArm.addChild(this.RightArmPauldon);
        this.bipedLeftArm.addChild(this.LeftArmPauldron);
        this.bipedBody.addChild(this.ChestArmor);
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